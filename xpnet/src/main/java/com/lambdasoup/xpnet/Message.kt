package com.lambdasoup.xpnet

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets
import java.util.*

@Suppress("UsePropertyAccessSyntax")
class Message private constructor(
    private val buf: ByteBuffer
) {

    init {
        buf.position(5)
        buf.order(ByteOrder.LITTLE_ENDIAN)
    }

    fun uchar(): Byte {
        return buf.get()
    }

    fun xint(): Int {
        return buf.getInt()
    }

    fun uint(): UInt {
        return buf.int.toUInt()
    }

    fun ushort(): UShort {
        return buf.getShort().toUShort()
    }

    fun xchr(): String {
        val ba = ByteArray(buf.remaining())
        buf.get(ba)
        return String(ba).split('\u0000')[0]
    }

    fun base64String(): String {
        val clone = buf.array().clone()
        val ba64 = Base64.getEncoder().encode(clone)
        val buf64 = ByteBuffer.wrap(ba64)
        return StandardCharsets.US_ASCII.decode(buf64).toString()
    }

    fun xflt(): Float {
        return buf.float
    }

    companion object {
        fun wrap(byteArray: ByteArray, length: Int): Message {
            val buf = ByteBuffer.wrap(byteArray, 0, length)
            return Message(buf)
        }
    }
}
