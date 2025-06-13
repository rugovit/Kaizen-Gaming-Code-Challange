package com.rugovit.kaizengamingcodechallange.data.repository

import arrow.core.Either
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rugovit.kaizengamingcodechallange.core.common.AppError
import com.rugovit.kaizengamingcodechallange.core.common.TransactionRunner
import com.rugovit.kaizengamingcodechallange.data.database.dao.SportDao
import com.rugovit.kaizengamingcodechallange.data.database.entities.EventEntity
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportEntity
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportWithEventsPOJO
import com.rugovit.kaizengamingcodechallange.data.mapper.toEntity
import com.rugovit.kaizengamingcodechallange.data.network.ApiService
import com.rugovit.kaizengamingcodechallange.data.network.models.EventNetwork
import com.rugovit.kaizengamingcodechallange.data.network.models.SportNetwork
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class SportsRepositoryImplTest {

    private lateinit var txRunner: TransactionRunner
    private lateinit var sportDao: SportDao
    private lateinit var apiService: ApiService
    private lateinit var repository: SportsRepositoryImpl

    @Before
    fun setUp() = runTest {
        txRunner = mock()
        sportDao = mock()
        apiService = mock()

        // Default: no stale sports
        whenever(sportDao.getAllSports()).thenReturn(emptyList())

        whenever(txRunner.run(any<suspend () -> Any?>()))
            .thenAnswer { invocation ->
                @Suppress("UNCHECKED_CAST")
                runBlocking { (invocation.getArgument<suspend () -> Any?>(0))() }
            }
        repository = SportsRepositoryImpl(txRunner, sportDao, apiService)
    }

    @Test
    fun `getSportsWithEvents returns flow from dao`() = runTest {
        val expected = listOf(
            SportWithEventsPOJO(SportEntity("s1", "Sport1"), emptyList())
        )
        whenever(sportDao.getSportsWithEventsStream()).thenReturn(flowOf(expected))

        val result = repository.getSportsWithEvents().first()

        assertEquals(expected, result)
    }

    @Test
    fun `syncSportsData succeeds and updates database including deleting stale sports`() = runTest {
        val storedEvents = listOf(
            EventEntity("e1", "s1", "team1-team2", 1_749_615_901L, true),
            EventEntity("e2", "s1", "team3-team4", 1_749_613_901L, false)
        )
        val networkSports = listOf(
            SportNetwork(
                i = "s1", d = "Sport1",
                e = listOf(
                    EventNetwork("e2", "FOOT", "Event2", 1_749_655_131L),
                    EventNetwork("e3", "FOOT", "Event3", 1_749_655_131L)
                )
            )
        )
        // Add a stale sport in DB
        val staleSport = SportEntity("old", "OldSport")
        whenever(sportDao.getAllSports()).thenReturn(listOf(staleSport))
        whenever(apiService.getSports()).thenReturn(networkSports)
        whenever(sportDao.getAllEvents()).thenReturn(storedEvents)
        val expectedPojo = listOf(
            SportWithEventsPOJO(SportEntity("s1", "Sport1"), storedEvents)
        )
        whenever(sportDao.getSportsWithEvents()).thenReturn(expectedPojo)

        val result = repository.syncSportsData()

        // verify deletion of stale event and sport
        verify(sportDao).deleteEvents(listOf("e1"))
        verify(sportDao).deleteSports(listOf("old"))

        // verify inserts
        verify(sportDao).insertSports(networkSports.map { it.toEntity() })
        verify(sportDao).insertEvents(networkSports.flatMap { sport ->
            sport.e.map { ev ->
                ev.toEntity(storedEvents.find { it.eventId == ev.i }?.isFavorite ?: false)
            }
        })

        verify(sportDao).getSportsWithEvents()
        assertEquals(Either.Right(expectedPojo), result)
    }

    @Test
    fun `syncSportsData does not delete when IDs unchanged`() = runTest {
        val storedEvents = listOf(
            EventEntity("e1", "s1", "game", 0L, false)
        )
        val networkSports = listOf(
            SportNetwork(
                "s1", "Sport1", listOf(
                    EventNetwork("e1", "FOOT", "game", 0L)
                )
            )
        )
        // No stale sports
        whenever(sportDao.getAllSports()).thenReturn(listOf(SportEntity("s1", "Sport1")))
        whenever(apiService.getSports()).thenReturn(networkSports)
        whenever(sportDao.getAllEvents()).thenReturn(storedEvents)
        whenever(sportDao.getSportsWithEvents()).thenReturn(
            listOf(SportWithEventsPOJO(SportEntity("s1", "Sport1"), storedEvents))
        )

        repository.syncSportsData()

        verify(sportDao, never()).deleteEvents(any())
        verify(sportDao, never()).deleteSports(any())
    }

    @Test
    fun `syncSportsData empty API deletes all events and sports`() = runTest {
        val storedEvents = listOf(
            EventEntity("e1", "s1", "game", 0L, false)
        )
        val storedSports = listOf(
            SportEntity("s1", "Sport1"),
            SportEntity("s2", "Sport2")
        )
        whenever(apiService.getSports()).thenReturn(emptyList())
        whenever(sportDao.getAllEvents()).thenReturn(storedEvents)
        whenever(sportDao.getAllSports()).thenReturn(storedSports)
        whenever(sportDao.getSportsWithEvents()).thenReturn(emptyList())

        repository.syncSportsData()

        // all old IDs should be deleted
        verify(sportDao).deleteEvents(listOf("e1"))
        verify(sportDao).deleteSports(listOf("s1", "s2"))
    }

    @Test
    fun `syncSportsData preserves favorites for matching events`() = runTest {
        val stored = listOf(
            EventEntity("e1", "s1", "game", 0L, true)
        )
        whenever(sportDao.getAllSports()).thenReturn(listOf(SportEntity("s1", "Sport1")))
        val networkSports = listOf(
            SportNetwork(
                "s1", "Sport1", listOf(
                    EventNetwork("e1", "FOOT", "game", 0L)
                )
            )
        )

        whenever(apiService.getSports()).thenReturn(networkSports)
        whenever(sportDao.getAllEvents()).thenReturn(stored)
        whenever(sportDao.getSportsWithEvents()).thenReturn(
            listOf(SportWithEventsPOJO(SportEntity("s1", "Sport1"), stored))
        )

        repository.syncSportsData()

        // capture insertEvents and assert favorite flag
        val captor = argumentCaptor<List<EventEntity>>()
        verify(sportDao).insertEvents(captor.capture())
        assertTrue(captor.firstValue.any { it.eventId == "e1" && it.isFavorite })
    }

    @Test
    fun `syncSportsData fails when API throws exception`() = runTest {
        whenever(apiService.getSports()).thenThrow(RuntimeException("Network error"))

        val result = repository.syncSportsData()

        assertTrue(result.isLeft())
        result.onLeft { assertTrue(it is AppError) }
    }

    @Test
    fun `toggleFavorite succeeds`() = runTest {
        whenever(sportDao.toggleFavorite("e1")).thenReturn(Unit)

        val result = repository.toggleFavorite("e1")

        verify(sportDao).toggleFavorite("e1")
        assertEquals(Either.Right(Unit), result)
    }

    @Test
    fun `toggleFavorite fails`() = runTest {
        whenever(sportDao.toggleFavorite("e1")).thenThrow(RuntimeException("DB error"))

        val result = repository.toggleFavorite("e1")

        assertTrue(result.isLeft())
        result.onLeft { assertTrue(it is AppError) }
    }
}
