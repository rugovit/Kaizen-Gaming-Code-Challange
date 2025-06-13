package com.rugovit.kaizengamingcodechallange.domain.usecase

import arrow.core.left
import arrow.core.right
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rugovit.kaizengamingcodechallange.core.common.AppError
import com.rugovit.kaizengamingcodechallange.data.database.entities.EventEntity
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportEntity
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportWithEventsPOJO
import com.rugovit.kaizengamingcodechallange.data.repository.SportsRepository
import com.rugovit.kaizengamingcodechallange.domain.mapper.toDomain
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock


class SyncSportsDataUseCaseTest {

    private lateinit var repository: SportsRepository
    private lateinit var useCase: SyncSportsDataUseCase

    @Before
    fun setUp() {
        repository = mock()
        useCase = SyncSportsDataUseCase(repository)
    }

    @Test
    fun `doWork returns domain list on success`() = runTest {
        // given a POJO with a sport and no events
        val pojos = listOf(
            SportWithEventsPOJO(
                sport = SportEntity(sportId = "s1", sportName = "Sport1"),
                events = emptyList()
            )
        )
        whenever(repository.syncSportsData()).thenReturn(pojos.right())

        // when
        val result = useCase.doWork(Unit)

        // then repository is called
        verify(repository).syncSportsData()
        // and we get a Right of the mapped domain list
        val expectedDomain = pojos.toDomain()
        assertEquals(expectedDomain.right(), result)
    }

    @Test
    fun `doWork maps events correctly`() = runTest {
        // given a POJO with one sport and two events
        val eventPojo1 = EventEntity(eventId = "e1", sportId = "s1", eventName = "match1", startTime = 0L, isFavorite = true)
        val eventPojo2 = EventEntity(eventId = "e2", sportId = "s1", eventName = "match2", startTime = 1L, isFavorite = false)
        val pojos = listOf(
            SportWithEventsPOJO(
                sport = SportEntity(sportId = "s1", sportName = "Sport1"),
                events = listOf(eventPojo1, eventPojo2)
            )
        )
        whenever(repository.syncSportsData()).thenReturn(pojos.right())

        // when
        val result = useCase.doWork(Unit)

        // then event mapping preserves fields
        result.onRight { domains ->
            val sportDomain = domains.first()
            assertEquals(2, sportDomain.eventDomainModels.size)
            val domainEvent1 = sportDomain.eventDomainModels.find { it.id == "e1" }!!
            assertTrue(domainEvent1.isFavorite)
            val domainEvent2 = sportDomain.eventDomainModels.find { it.id == "e2" }!!
            assertTrue(!domainEvent2.isFavorite)
        }
    }

    @Test
    fun `doWork returns error on failure`() = runTest {
        // given repository failure
        val error = AppError.UnknownError(Exception("fail"))
        whenever(repository.syncSportsData()).thenReturn(error.left())

        // when
        val result = useCase.doWork(Unit)

        // then we get the same error
        assertEquals(error.left(), result)
    }
}

