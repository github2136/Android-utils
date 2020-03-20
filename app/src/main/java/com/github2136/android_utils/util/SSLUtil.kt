package com.github2136.android_utils.util

import java.io.IOException
import java.io.InputStream
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * Created by YB on 2020/3/20
 * https工具类
 */
object SSLUtil {
    /**
     * 不验证证书
     * val sSlObj = SSLUtil.notVerified()
     * OkHttpClient().newBuilder()
     * .sslSocketFactory(sSlObj.socketFactory, sSlObj.trustManager)
     * .hostnameVerifier(HostnameVerifier { hostname, session -> true })
     */
    @JvmStatic
    fun notVerified(): SSlObj {
        val sslContext = SSLContext.getInstance("TLS")
        val x509 = arrayOf(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })
        sslContext.init(null, x509, SecureRandom())

        return SSlObj(sslContext.socketFactory, x509[0] as X509TrustManager)
    }

    /**
     * 验证证书
     * val assets = app.assets
     * val sSlObj = SSLUtil.verified(assets.open("ca.cer"), assets.open("b.cer"))
     * OkHttpClient().newBuilder()
     * .sslSocketFactory(sSlObj.socketFactory, sSlObj.trustManager)
     * //不建议忽略主机验证
     * .hostnameVerifier(HostnameVerifier { hostname, session -> true })
     */
    fun verified(vararg cer: InputStream): SSlObj {
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val password = "password".toCharArray()
        val keyStore = newEmptyKeyStore(password)
        for ((index, certificate) in cer.withIndex()) {
            val certificates = certificateFactory.generateCertificate(certificate)
            val certificateAlias = index.toString()
            keyStore.setCertificateEntry(certificateAlias, certificates)
        }
        val keyManagerFactory = KeyManagerFactory.getInstance(
            KeyManagerFactory.getDefaultAlgorithm()
        )
        keyManagerFactory.init(keyStore, password)
        val trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory.init(keyStore)

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(keyManagerFactory.keyManagers, trustManagerFactory.trustManagers, null)
        return SSlObj(sslContext.socketFactory, trustManagerFactory.trustManagers[0] as X509TrustManager)
    }

    @Throws(GeneralSecurityException::class)
    private fun newEmptyKeyStore(password: CharArray): KeyStore {
        try {
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            val `in`: InputStream? = null // By convention, 'null' creates an empty key store.
            keyStore.load(`in`, password)
            return keyStore
        } catch (e: IOException) {
            throw AssertionError(e)
        }
    }
}

data class SSlObj(val socketFactory: SSLSocketFactory, val trustManager: X509TrustManager)