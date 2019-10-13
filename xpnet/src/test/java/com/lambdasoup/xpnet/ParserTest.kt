package com.lambdasoup.xpnet

import org.junit.Assert
import org.junit.Test
import java.util.*

class ParserTest {

    @ExperimentalUnsignedTypes
    @Test
    fun `should parse BEACON`() {
        val s64 = "QkVDTgABAgEAAADBuwEAAQAAAGi/U0FUVVJOAHK/"
        val ba64 = Base64.getDecoder().decode(s64)
        val msg = Message.wrap(ba64, ba64.size)
        val parser = Parser()
        val expected = Beacon(
            majorVersion = 1,
            minorVersion = 2,
            applicationHostId = 1,
            versionNumber = 113601,
            role = 1.toUInt(),
            port = 49000.toUShort(),
            computerName = "SATURN"
        )

        val actual = parser.parse(msg)

        Assert.assertEquals(expected, actual)
    }
}
