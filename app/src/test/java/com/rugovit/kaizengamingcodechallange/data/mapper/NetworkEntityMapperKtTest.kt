package com.rugovit.kaizengamingcodechallange.data.mapper

import com.rugovit.kaizengamingcodechallange.data.network.models.EventNetwork
import com.rugovit.kaizengamingcodechallange.data.network.models.SportNetwork
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class DataMapperTest {

    @Test
    fun `SportNetwork toEntity maps sportId and sportName`() {
        val network = SportNetwork(
            i = "sport123",
            d = "Basketball",
            e = emptyList()
        )

        val entity = network.toEntity()

        assertEquals("sport123", entity.sportId)
        assertEquals("Basketball", entity.sportName)
    }

    @Test
    fun `EventNetwork toEntity maps all fields for true and false favorite flags`() {
        val baseNetwork = EventNetwork(
            i = "eventX",
            si = "sport123",
            d = "Final Match",
            tt = 1620000000L
        )

        val entityFavTrue = baseNetwork.toEntity(isFavorite = true)
        assertEquals("eventX", entityFavTrue.eventId)
        assertEquals("sport123", entityFavTrue.sportId)
        assertEquals("Final Match", entityFavTrue.eventName)
        assertEquals(1620000000L, entityFavTrue.startTime)
        assertTrue(entityFavTrue.isFavorite)

        val entityFavFalse = baseNetwork.toEntity(isFavorite = false)
        assertEquals("eventX", entityFavFalse.eventId)
        assertFalse(entityFavFalse.isFavorite)
    }

    @Test
    fun `SportNetwork with events list mapping produces corresponding EventEntity list`() {
        val netEvents = listOf(
            EventNetwork("e1", "s1", "Game1", 100L),
            EventNetwork("e2", "s1", "Game2", 200L)
        )
        val network = SportNetwork(
            i = "s1",
            d = "Soccer",
            e = netEvents
        )

        val sportEntity = network.toEntity()
        val eventEntities = netEvents.map { it.toEntity(isFavorite = false) }

        assertEquals("s1", sportEntity.sportId)
        assertEquals("Soccer", sportEntity.sportName)
        assertEquals(netEvents.size, eventEntities.size)

        eventEntities.forEachIndexed { index, entity ->
            val net = netEvents[index]
            assertEquals(net.i, entity.eventId)
            assertEquals(net.tt, entity.startTime)
            assertEquals("Game${index + 1}", entity.eventName)
            assertFalse(entity.isFavorite)
        }
    }

    @Test
    fun `Combined mapping chain ensures foreign key consistency and favorite preservation`() {
        val network = SportNetwork(
            i = "s2",
            d = "Tennis",
            e = listOf(EventNetwork("e3", "s2", "Final", 300L))
        )

        val sportEntity = network.toEntity()
        val eventEntities = network.e.map { it.toEntity(isFavorite = true) }

        assertEquals("s2", sportEntity.sportId)
        assertEquals("Tennis", sportEntity.sportName)
        assertEquals(1, eventEntities.size)
        assertEquals(sportEntity.sportId, eventEntities[0].sportId)
        assertTrue(eventEntities[0].isFavorite)
    }

    @Test
    fun `mapping empty event list yields no EventEntity`() {
        val network = SportNetwork(i = "s3", d = "Hockey", e = emptyList())
        val eventEntities = network.e.map { it.toEntity(isFavorite = false) }
        assertTrue(eventEntities.isEmpty())
    }
}
