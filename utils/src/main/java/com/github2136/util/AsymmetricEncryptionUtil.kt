package com.github2136.util

import androidx.annotation.StringDef
import java.io.ByteArrayOutputStream
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


/**
 *      非对称加密
 *      RSA 生成的key至少1024位
 *      val keyData = AsymmetricEncryptionUtil.getKey()
 *      val publicKey = Base64.encodeToString(keyData!!.public.encoded, Base64.NO_WRAP)
 *      val privateKey = Base64.encodeToString(keyData.private.encoded, Base64.NO_WRAP)
 *      //加密后的内容
 *      val encryptStr = Base64.encodeToString(encryptData, Base64.NO_WRAP)
 *      //解密后的内容
 *      String(decryptData!!)
 */
object AsymmetricEncryptionUtil {
    const val RSA = "RSA"//1024+

    //如果添加了mode就必须添加padding
    const val MODE_ECB = "/ECB"// RSA
    const val MODE_NONE = "/NONE"//RSA

    const val PADDING_NO = "/NoPadding"//支持长度 keySize/8
    const val PADDING_OAEP = "/OAEPPadding"//支持长度 keySize/8 - 42
    const val PADDING_OAEP_SHA_1 = "/OAEPwithSHA-1andMGF1Padding"//支持长度 keySize/8 - 42
    const val PADDING_OAEP_SHA_256 = "/OAEPwithSHA-256andMGF1Padding"//支持长度 keySize/8 -66
    const val PADDING_PKCS1 = "/PKCS1Padding" //支持长度 keySize/8 - 11


    @StringDef(RSA)
    internal annotation class EncryptType

    @StringDef(PADDING_NO, PADDING_OAEP, PADDING_OAEP_SHA_1, PADDING_OAEP_SHA_256, PADDING_PKCS1)
    internal annotation class EncryptPadding

    @StringDef(MODE_ECB, MODE_NONE)
    internal annotation class EncryptMode

    /**
     * 公钥加密
     */
    @JvmStatic
    fun encryptByPublicKey(mKey: ByteArray,
                           mData: ByteArray,
                           @EncryptMode mMode: String,
                           @EncryptPadding mPadding: String,
                           @EncryptType mEncryptType: String = RSA,
                           mKeySize: Int = 1024): ByteArray? {
        return try {
            //当前秘钥支持加密的最大字节数
            val encryptSize = getEncryptDataSize(mKeySize, mPadding)

            val x509eks = X509EncodedKeySpec(mKey)
            val keyFactory = KeyFactory.getInstance(mEncryptType)
            val key = keyFactory.generatePublic(x509eks)

            val cipher = Cipher.getInstance(mEncryptType + mMode + mPadding)
            cipher.init(Cipher.ENCRYPT_MODE, key)

            val inputLen = mData.size
            val out = ByteArrayOutputStream()
            var offSet = 0
            var cache: ByteArray
            var i = 0
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                cache = if (inputLen - offSet > encryptSize) {
                    cipher.doFinal(mData, offSet, encryptSize)
                } else {
                    cipher.doFinal(mData, offSet, inputLen - offSet)
                }
                out.write(cache, 0, cache.size)
                i++
                offSet = i * encryptSize
            }
            val encryptedData = out.toByteArray()
            out.close()
            encryptedData
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 私钥解密
     */
    @JvmStatic
    fun decryptByPrivateKey(mKey: ByteArray,
                            mData: ByteArray,
                            @EncryptMode mMode: String,
                            @EncryptPadding mPadding: String,
                            @EncryptType mEncryptType: String = RSA,
                            mKeySize: Int = 1024): ByteArray? {
        return try {
            val decryptSize = mKeySize / 8

            val pkcs8eks = PKCS8EncodedKeySpec(mKey)
            val keyFactory = KeyFactory.getInstance(mEncryptType)
            val key = keyFactory.generatePrivate(pkcs8eks)

            val cipher = Cipher.getInstance(mEncryptType + mMode + mPadding)
            cipher.init(Cipher.DECRYPT_MODE, key)

            val inputLen = mData.size
            val out = ByteArrayOutputStream()
            var offSet = 0
            var cache: ByteArray
            var i = 0
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                cache = if (inputLen - offSet > decryptSize) {
                    cipher.doFinal(mData, offSet, decryptSize)
                } else {
                    cipher.doFinal(mData, offSet, inputLen - offSet)
                }
                out.write(cache, 0, cache.size)
                i++
                offSet = i * decryptSize
            }
            val decryptData = out.toByteArray()
            out.close()
            decryptData
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 私钥加密
     */
    @JvmStatic
    fun encryptByPrivateKey(mKey: ByteArray,
                            mData: ByteArray,
                            @EncryptMode mMode: String,
                            @EncryptPadding mPadding: String,
                            @EncryptType mEncryptType: String = RSA,
                            mKeySize: Int = 1024): ByteArray? {
        return try {
            //当前秘钥支持加密的最大字节数
            val encryptSize = getEncryptDataSize(mKeySize, mPadding)

            val pkcs8eks = PKCS8EncodedKeySpec(mKey)
            val keyFactory = KeyFactory.getInstance(mEncryptType)
            val key = keyFactory.generatePrivate(pkcs8eks)

            val cipher = Cipher.getInstance(mEncryptType + mMode + mPadding)
            cipher.init(Cipher.ENCRYPT_MODE, key)


            val inputLen = mData.size
            val out = ByteArrayOutputStream()
            var offSet = 0
            var cache: ByteArray
            var i = 0
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                cache = if (inputLen - offSet > encryptSize) {
                    cipher.doFinal(mData, offSet, encryptSize)
                } else {
                    cipher.doFinal(mData, offSet, inputLen - offSet)
                }
                out.write(cache, 0, cache.size)
                i++
                offSet = i * encryptSize
            }
            val encryptedData = out.toByteArray()
            out.close()
            encryptedData
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 公钥解密
     */
    @JvmStatic
    fun decryptByPublicKey(mKey: ByteArray,
                           mData: ByteArray,
                           @EncryptMode mMode: String,
                           @EncryptPadding mPadding: String,
                           @EncryptType mEncryptType: String = RSA,
                           mKeySize: Int = 1024): ByteArray? {
        return try {
            val decryptSize = mKeySize / 8

            val x509eks = X509EncodedKeySpec(mKey)
            val keyFactory = KeyFactory.getInstance(mEncryptType)
            val key = keyFactory.generatePublic(x509eks)

            val cipher = Cipher.getInstance(mEncryptType + mMode + mPadding)
            cipher.init(Cipher.DECRYPT_MODE, key)

            val inputLen = mData.size
            val out = ByteArrayOutputStream()
            var offSet = 0
            var cache: ByteArray
            var i = 0
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                cache = if (inputLen - offSet > decryptSize) {
                    cipher.doFinal(mData, offSet, decryptSize)
                } else {
                    cipher.doFinal(mData, offSet, inputLen - offSet)
                }
                out.write(cache, 0, cache.size)
                i++
                offSet = i * decryptSize
            }
            val decryptedData = out.toByteArray()
            out.close()
            decryptedData
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 生成key
     */
    @JvmStatic
    fun getKey(size: Int = 1024, @EncryptType mEncryptType: String = RSA): KeyPair? {
        return try {
            val keyPairGenerator = KeyPairGenerator.getInstance(mEncryptType)
            keyPairGenerator.initialize(size, SecureRandom())
            keyPairGenerator.genKeyPair()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取对应填充方式可加密内容长度
     */
    private fun getEncryptDataSize(keySize: Int, padding: String) = when (padding) {
        PADDING_OAEP, PADDING_OAEP_SHA_1 -> keySize / 8 - 42
        PADDING_OAEP_SHA_256             -> keySize / 8 - 66
        PADDING_PKCS1                    -> keySize / 8 - 11
        //默认为PADDING_NO
        else                             -> keySize / 8
    }
}