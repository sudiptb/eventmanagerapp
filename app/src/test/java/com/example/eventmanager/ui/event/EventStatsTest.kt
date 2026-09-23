package com.example.eventmanager.ui.event

import org.junit.Assert.assertEquals
import org.junit.Test

class EventStatsTest {
    @Test fun upcomingAndPastCounts() {
        val now=System.currentTimeMillis()
        val times=listOf(now+3600000, now-3600000, now+7200000)
        val upcoming=times.count{it>=now}; val past=times.size-upcoming
        assertEquals(2,upcoming); assertEquals(1,past)
    }
}
