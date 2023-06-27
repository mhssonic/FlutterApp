package com.mhssonic.flutter.service.http

import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate

class TrustAllCerts : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        // Do nothing, accept all client certificates
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        // Do nothing, accept all server certificates
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return emptyArray()
    }
}