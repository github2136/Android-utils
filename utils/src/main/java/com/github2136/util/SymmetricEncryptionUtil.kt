package com.github2136.util

import androidx.annotation.StringDef
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * 对称加密<br>
 * ECB不可添加向量IV<br>
 * DES/DESede IV长度为8字节<br>
 * AES IV长度为16字节
 * DES key长度为8字节<br>
 * DESede key长度为16字节或24字节<br>
 * AES key长度为16字节、24字节或32字节<br>
 */
object SymmetricEncryptionUtil {
    const val DES = "DES"//64位(bit)
    const val DESede = "DESede"//128位(bit)192位(bit)
    const val AES = "AES"//128位(bit)192位(bit)

    const val MODE_CBC = "/CBC"//DES、DESede、AES
    const val MODE_CFB = "/CFB"//DES、DESede、AES
    const val MODE_CTR = "/CTR"//DES、DESede、AES
    const val MODE_CTS = "/CTS"//DES、DESede、AES
    const val MODE_ECB = "/ECB"//DES、DESede、AES
    const val MODE_OFB = "/OFB"//DES、DESede、AES

    const val PADDING_PKCS5 = "/PKCS5Padding"//DES、DESede、AES
    //如果加密内容不是8字节的整数倍就会报错
    const val PADDING_NO = "/NoPadding"//DES、DESede、AES
    const val PADDING_ISO10126 = "/ISO10126Padding"//DES、DESede、AES

    @StringDef(DES, DESede, AES)
    internal annotation class EncryptType

    @StringDef(PADDING_PKCS5, PADDING_NO, PADDING_ISO10126)
    internal annotation class EncryptPadding

    @StringDef(MODE_CBC, MODE_CFB, MODE_CTR, MODE_CTS, MODE_ECB, MODE_OFB)
    internal annotation class EncryptMode

    /**
     * 加密
     */
    fun encrypt(mKey: ByteArray,
                @EncryptType mEncryptType: String,
                mData: ByteArray,
                @EncryptMode mMode: String = "",
                @EncryptPadding mPadding: String = "",
                mIV: ByteArray? = null): ByteArray? {
        return try {
            val key = SecretKeySpec(mKey, mEncryptType)//生成密钥
            val cipher = Cipher.getInstance(mEncryptType + mMode + mPadding)
            if (mMode != MODE_ECB) {
                val ips = IvParameterSpec(mIV)
                cipher.init(Cipher.ENCRYPT_MODE, key, ips)
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key)
            }
            cipher.doFinal(mData)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun decrypt(mKey: ByteArray,
                @EncryptType mEncryptType: String,
                mData: ByteArray,
                @EncryptMode mMode: String = "",
                @EncryptPadding mPadding: String = "",
                mIV: ByteArray? = null): ByteArray? {
        return try {
            val key = SecretKeySpec(mKey, mEncryptType)//生成密钥
            val cipher = Cipher.getInstance(mEncryptType + mMode + mPadding)
            if (mMode != MODE_ECB) {
                val ips = IvParameterSpec(mIV)
                cipher.init(Cipher.DECRYPT_MODE, key, ips)
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key)
            }
            cipher.doFinal(mData)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 生成key
     */
    fun getKey(@EncryptType type: String, size: Int): SecretKey? {
        return try {
            val keyGenerator = KeyGenerator.getInstance(type)
            keyGenerator.init(size)
            keyGenerator.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }
    }
}