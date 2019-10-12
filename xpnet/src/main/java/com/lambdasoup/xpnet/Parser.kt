package com.lambdasoup.xpnet

class Parser {
    fun parse(message: Message): Beacon {
        return Beacon(
            majorVersion = message.uchar(),
            minorVersion = message.uchar(),
            applicationHostId = message.xint(),
            versionNumber = message.xint(),
            role = message.uint(),
            port = message.ushort(),
            computerName = message.xchr()
        )
    }
}
