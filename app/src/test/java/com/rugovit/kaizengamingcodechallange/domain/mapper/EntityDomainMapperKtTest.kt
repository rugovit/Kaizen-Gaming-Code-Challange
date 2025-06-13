package com.rugovit.kaizengamingcodechallange.domain.mapper

import com.rugovit.kaizengamingcodechallange.data.database.entities.EventEntity
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportEntity
import com.rugovit.kaizengamingcodechallange.data.database.entities.SportWithEventsPOJO
import com.rugovit.kaizengamingcodechallange.domain.models.EventDomainModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class DomainMapperTest {

    @Test
    fun `EventEntity toDomain maps all fields`() {
        val entity = EventEntity(
            eventId = "e1",
            sportId = "s1",
            eventName = "TeamA vs TeamB",
            startTime = 12345L,
            isFavorite = true
        )

        val domain = entity.toDomain()

        assertEquals("e1", domain.id)
        assertEquals("s1", domain.sportId)
        assertEquals("TeamA vs TeamB", domain.name)
        assertEquals(12345L, domain.startTime)
        assertTrue(domain.isFavorite)
    }

    @Test
    fun `SportWithEventsPOJO toDomain maps sport and events`() {
        val sportEntity = SportEntity(
            sportId = "s1",
            sportName = "Soccer"
        )
        val event1 = EventEntity("e1", "s1", "Match1", 0L, false)
        val event2 = EventEntity("e2", "s1", "Match2", 1L, true)
        val pojo = SportWithEventsPOJO(
            sport = sportEntity,
            events = listOf(event1, event2)
        )

        val domain = pojo.toDomain()

        assertEquals("s1", domain.id)
        assertEquals("Soccer", domain.name)
        assertEquals(2, domain.eventDomainModels.size)

        // verify order preserved
        assertEquals("e1", domain.eventDomainModels[0].id)
        assertFalse(domain.eventDomainModels[0].isFavorite)
        assertEquals("e2", domain.eventDomainModels[1].id)
        assertTrue(domain.eventDomainModels[1].isFavorite)
    }

    @Test
    fun `List of SportWithEventsPOJO toDomain maps each item and preserves order`() {
        val pojo1 = SportWithEventsPOJO(
            sport = SportEntity("s1", "Basketball"),
            events = emptyList()
        )
        val pojo2 = SportWithEventsPOJO(
            sport = SportEntity("s2", "Tennis"),
            events = listOf(EventEntity("e3", "s2", "Final", 2L, false))
        )
        val list = listOf(pojo1, pojo2)

        val domainList = list.toDomain()

        assertEquals(2, domainList.size)
        assertEquals("s1", domainList[0].id)
        assertTrue(domainList[0].eventDomainModels.isEmpty())

        assertEquals("s2", domainList[1].id)
        assertEquals(1, domainList[1].eventDomainModels.size)
        assertEquals(EventDomainModel("e3", "s2", "Final", 2L, false), domainList[1].eventDomainModels[0])
    }

    @Test
    fun `toDomain on empty list returns empty list`() {
        val empty: List<SportWithEventsPOJO> = emptyList()
        val result = empty.toDomain()
        assertTrue(result.isEmpty())
    }
}
