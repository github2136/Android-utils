package com.github2136.util

import androidx.annotation.StringDef
import java.io.ByteArrayOutputStream
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 * Created by YB on 2019/12/27
 */
object SignUtil {
    const val RSA = "RSA"//key长度1024-4096
    const val DSA = "DSA"//key长度1024-4096
    const val EC = "EC"//key长度256

    const val SIGN_MD5withRSA = "MD5withRSA"
    const val SIGN_NONEwithRSA = "NONEwithRSA"
    const val SIGN_SHA1withRSA = "SHA1withRSA"
    const val SIGN_SHA224withRSA = "SHA224withRSA"
    const val SIGN_SHA256withRSA = "SHA256withRSA"
    const val SIGN_SHA384withRSA = "SHA384withRSA"
    const val SIGN_SHA512withRSA = "SHA512withRSA"
    const val SIGN_DSA = "DSA"
    const val SIGN_NONEwithDSA = "NONEwithDSA"
    const val SIGN_SHA1withDSA = "SHA1withDSA"
    const val SIGN_SHA224withDSA = "SHA224withDSA"
    const val SIGN_SHA256withDSA = "SHA256withDSA"
    const val SIGN_ECDSA = "ECDSA"
    const val SIGN_ECDSAwithSHA1 = "ECDSAwithSHA1"
    const val SIGN_NONEwithECDSA = "NONEwithECDSA"
    const val SIGN_SHA1withECDSA = "SHA1withECDSA"
    const val SIGN_SHA224withECDSA = "SHA224withECDSA"
    const val SIGN_SHA256withECDSA = "SHA256withECDSA"
    const val SIGN_SHA384withECDSA = "SHA384withECDSA"
    const val SIGN_SHA512withECDSA = "SHA512withECDSA"


    @StringDef(
        SIGN_MD5withRSA,
        SIGN_NONEwithRSA,
        SIGN_SHA1withRSA,
        SIGN_SHA224withRSA,
        SIGN_SHA256withRSA,
        SIGN_SHA384withRSA,
        SIGN_SHA512withRSA,
        SIGN_DSA,
        SIGN_NONEwithDSA,
        SIGN_SHA1withDSA,
        SIGN_SHA224withDSA,
        SIGN_SHA256withDSA,
        SIGN_ECDSA,
        SIGN_ECDSAwithSHA1,
        SIGN_NONEwithECDSA,
        SIGN_SHA1withECDSA,
        SIGN_SHA224withECDSA,
        SIGN_SHA256withECDSA,
        SIGN_SHA384withECDSA,
        SIGN_SHA512withECDSA
    )
    internal annotation class SignType

    @StringDef(
        RSA,
        DSA,
        EC
    )
    internal annotation class EncryptType

    /**
     * 签名
     */
    fun sign(mPrivateKey: ByteArray, mData: ByteArray, @SignType mSignType: String = SIGN_SHA256withRSA): ByteArray? {
        return try {
            val pkcs8eks = PKCS8EncodedKeySpec(mPrivateKey)
            val keyFactory = KeyFactory.getInstance(getEncryptType(mSignType))
            val key = keyFactory.generatePrivate(pkcs8eks)

            val signature = Signature.getInstance(mSignType)
            signature.initSign(key)
            signature.update(mData)
            signature.sign()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 验签
     */
    fun verify(mPublicKey: ByteArray, mData: ByteArray, mSign: ByteArray, @SignType mSignType: String = SIGN_SHA256withRSA): Boolean? {
        return try {
            val x509eks = X509EncodedKeySpec(mPublicKey)
            val keyFactory = KeyFactory.getInstance(getEncryptType(mSignType))
            val key = keyFactory.generatePublic(x509eks)

            val signature = Signature.getInstance(mSignType)
            signature.initVerify(key)
            signature.update(mData)
            signature.verify(mSign)
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

    private fun getEncryptType(@SignType mSignType: String): String? {
        return when (mSignType) {
            SIGN_MD5withRSA,
            SIGN_NONEwithRSA,
            SIGN_SHA1withRSA,
            SIGN_SHA224withRSA,
            SIGN_SHA256withRSA,
            SIGN_SHA384withRSA,
            SIGN_SHA512withRSA   -> RSA
            SIGN_DSA,
            SIGN_NONEwithDSA,
            SIGN_SHA1withDSA,
            SIGN_SHA224withDSA,
            SIGN_SHA256withDSA   -> DSA
            SIGN_ECDSA,
            SIGN_ECDSAwithSHA1,
            SIGN_NONEwithECDSA,
            SIGN_SHA1withECDSA,
            SIGN_SHA224withECDSA,
            SIGN_SHA256withECDSA,
            SIGN_SHA384withECDSA,
            SIGN_SHA512withECDSA -> EC
            else                 -> null
        }
    }
}