package com.lambdasoup.xpnet

import org.junit.Assert
import org.junit.Test
import java.nio.ByteBuffer
import java.util.*

class ParserTest {

    @Test
    fun `should parse BEACON`() {
        val s64 = "QkVDTgABAgEAAADBuwEAAQAAAGi/U0FUVVJOAHK/"
        val ba64 = Base64.getDecoder().decode(s64)
        val bb = ByteBuffer.wrap(ba64)
        val parser = Parser()
        val expected = Beacon(
            majorVersion = 1,
            minorVersion = 1,
            applicationHostId = 123,
            versionNumber = 1234,
            role = 1,
            port = 43234,
            computerName = "SATURN"
        )

        val actual = parser.parse(bb)

        Assert.assertEquals(expected, actual)
    }
}
