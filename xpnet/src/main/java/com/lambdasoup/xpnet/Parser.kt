package com.lambdasoup.xpnet

import java.nio.ByteBuffer

class Parser {
    fun parse(bb: ByteBuffer): Beacon {
        return Beacon(
            majorVersion = bb.get(),
            minorVersion = bb.get().toInt(),
            applicationHostId = bb.int,
            versionNumber = bb.int.toUnsignedLong().toInt(),
            role = bb.long,
            port = bb.int,
            computerName = "vla"
        )
    }
}
