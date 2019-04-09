package com.github2136.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * 信息摘要
 */
object MessageDigestUtil {
    @JvmStatic
    fun getMessageDigest(bytes: ByteArray, algorithm: String): String {
        val hash: ByteArray
        try {
            hash = MessageDigest.getInstance(algorithm).digest(bytes)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Huh, $algorithm should be supported?", e)
        }

        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            if (b.toInt() and 0xFF < 0x10) hex.append("0")
            hex.append(Integer.toHexString(b.toInt() and 0xFF))
        }
        return hex.toString()
    }

    @JvmStatic
    fun getMessageDigest2ByteArray(bytes: ByteArray, algorithm: String): ByteArray {
        try {
            return MessageDigest.getInstance(algorithm).digest(bytes)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Huh, $algorithm should be supported?", e)
        }
    }
}