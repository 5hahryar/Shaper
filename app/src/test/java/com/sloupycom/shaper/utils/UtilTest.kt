package com.sloupycom.shaper.utils

import com.sloupycom.shaper.core.util.Util
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class UtilTest {

    @Test
    fun util_getWeekIndex() {
        val actual = Util.getWeekIndex(Calendar.getInstance())
        val expect = mutableListOf<String>()
        expect.add("20201021")
        expect.add("20201022")
        expect.add("20201023")
        expect.add("20201024")
        expect.add("20201025")
        expect.add("20201026")
        expect.add("20201027")

        assertEquals(expect, actual)
    }

    @Test
    fun addDayToDate() {
        val actual = Util.addDayToDate("20210311", 20)
        val expect = "20210516"

        assertEquals(expect, actual)
    }


}