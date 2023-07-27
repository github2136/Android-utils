package com.github2136.android_utils.util.totp

import android.text.TextUtils
import java.lang.reflect.UndeclaredThrowableException
import java.math.BigInteger
import java.security.GeneralSecurityException
import java.util.Date
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Created by 44569 on 2023/7/27
 */
object TOTP {
    /**
     * 共享密钥
     */
    private const val SECRET_KEY = "ga35sdia43dhqj6k3f0la"
    /**
     * 时间步长 单位:毫秒 作为口令变化的时间周期
     */
    private const val STEP: Long = 30000
    /**
     * 转码位数 [1-8]
     */
    private const val CODE_DIGITS = 6
    /**
     * 初始化时间
     */
    private const val INITIAL_TIME: Long = 0
    /**
     * 柔性时间回溯
     */
    private const val FLEXIBILIT_TIME: Long = 5000
    /**
     * 数子量级
     */
    private val DIGITS_POWER = intArrayOf(1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000)
    /**
     * 生成一次性密码
     *
     * @param code 账户
     * @param pass 密码
     * @return String
     */
    fun generateMyTOTP(code: String, pass: String): String {
        if (TextUtils.isEmpty(code) || TextUtils.isEmpty(pass)) {
            throw RuntimeException("账户密码不许为空")
        }
        val now = Date().time
        val time = java.lang.Long.toHexString(timeFactor(now)).toUpperCase()
        return generateTOTP(code + pass + SECRET_KEY, time)
    }
    /**
     * 刚性口令验证
     *
     * @param code 账户
     * @param pass 密码
     * @param totp 待验证的口令
     * @return boolean
     */
    fun verifyTOTPRigidity(code: String, pass: String, totp: String): Boolean {
        return generateMyTOTP(code, pass) == totp
    }
    /**
     * 柔性口令验证
     *
     * @param code 账户
     * @param pass 密码
     * @param totp 待验证的口令
     * @return boolean
     */
    fun verifyTOTPFlexibility(code: String, pass: String, totp: String): Boolean {
        val now = Date().time
        val time = java.lang.Long.toHexString(timeFactor(now)).toUpperCase()
        val tempTotp = generateTOTP(code + pass + SECRET_KEY, time)
        if (tempTotp == totp) {
            return true
        }
        val time2 = java.lang.Long.toHexString(timeFactor(now - FLEXIBILIT_TIME)).toUpperCase()
        val tempTotp2 = generateTOTP(code + pass + SECRET_KEY, time2)
        return tempTotp2 == totp
    }
    /**
     * 获取动态因子
     *
     * @param targetTime 指定时间
     * @return long
     */
    private fun timeFactor(targetTime: Long): Long {
        return (targetTime - INITIAL_TIME) / STEP
    }
    /**
     * 哈希加密
     *
     * @param crypto   加密算法
     * @param keyBytes 密钥数组
     * @param text     加密内容
     * @return byte[]
     */
    private fun hmac_sha(crypto: String, keyBytes: ByteArray, text: ByteArray): ByteArray {
        return try {
            val hmac: Mac
            hmac = Mac.getInstance(crypto)
            val macKey = SecretKeySpec(keyBytes, "AES")
            hmac.init(macKey)
            hmac.doFinal(text)
        } catch (gse: GeneralSecurityException) {
            throw UndeclaredThrowableException(gse)
        }
    }

    private fun hexStr2Bytes(hex: String): ByteArray {
        val bArray = BigInteger("10$hex", 16).toByteArray()
        val ret = ByteArray(bArray.size - 1)
        System.arraycopy(bArray, 1, ret, 0, ret.size)
        return ret
    }

    private fun generateTOTP256(key: String, time: String): String {
        return generateTOTP(key, time, "HmacSHA256")
    }

    private fun generateTOTP512(key: String, time: String): String {
        return generateTOTP(key, time, "HmacSHA512")
    }

    private fun generateTOTP(key: String, time: String, crypto: String = "HmacSHA1"): String {
        var time: String? = time
        val timeBuilder = StringBuilder(time)
        while (timeBuilder.length < 16) timeBuilder.insert(0, "0")
        time = timeBuilder.toString()
        val msg = hexStr2Bytes(time)
        val k = key.toByteArray()
        val hash = hmac_sha(crypto, k, msg)
        return truncate(hash)
    }
    /**
     * 截断函数
     *
     * @param target 20字节的字符串
     * @return String
     */
    private fun truncate(target: ByteArray): String {
        val result: StringBuilder
        val offset = target[target.size - 1].toInt() and 0xf
        val binary = (target[offset].toInt() and 0x7f shl 24
            or (target[offset + 1].toInt() and 0xff shl 16)
            or (target[offset + 2].toInt() and 0xff shl 8) or (target[offset + 3].toInt() and 0xff))
        val otp = binary % DIGITS_POWER[CODE_DIGITS]
        result = StringBuilder(Integer.toString(otp))
        while (result.length < CODE_DIGITS) {
            result.insert(0, "0")
        }
        return result.toString()
    }
}