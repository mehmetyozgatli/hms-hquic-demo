package com.myapps.hquicdemo.hquicservice

import android.content.Context
import android.util.Log
import com.huawei.hms.hquic.HQUICManager
import com.myapps.hquicdemo.other.DEFAULT_ALTERNATE_PORT
import com.myapps.hquicdemo.other.DEFAULT_PORT
import com.myapps.hquicdemo.other.SERVICE_TAG
import org.chromium.net.CronetEngine
import org.chromium.net.UrlRequest
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class HQUICService(private val context: Context) {

    private val executor: Executor = Executors.newSingleThreadExecutor()
    private var croNetEngine: CronetEngine? = null
    private var callback: UrlRequest.Callback? = null

    /**
     * Asynchronous initialization.
     */
    init {
        HQUICManager.asyncInit(
            context,
            object : HQUICManager.HQUICInitCallback {
                override fun onSuccess() {
                    Log.i(SERVICE_TAG, "HQUICManager asyncInit success")
                }

                override fun onFail(e: Exception?) {
                    Log.w(SERVICE_TAG, "HQUICManager asyncInit fail")
                }
            })
    }

    /**
     * Create a Cronet engine.
     *
     * @param url URL.
     * @return cronetEngine Cronet engine.
     */
    private fun createCronetEngine(url: String): CronetEngine? {
        if (croNetEngine != null) {
            return croNetEngine
        }
        val builder = CronetEngine.Builder(context)
        builder.enableQuic(true)
        builder.addQuicHint(getHost(url), DEFAULT_PORT, DEFAULT_ALTERNATE_PORT)
        croNetEngine = builder.build()
        return croNetEngine
    }

    /**
     * Construct a request
     *
     * @param url Request URL.
     * @param method method Method type.
     * @param headers Request Headers
     * @return UrlRequest urlrequest instance.
     */
    private fun buildRequest(
        url: String,
        method: String,
        headers: HashMap<String, String>?
    ): UrlRequest? {
        val cronetEngine: CronetEngine? = createCronetEngine(url)
        val requestBuilder = cronetEngine?.newUrlRequestBuilder(url, callback, executor)
        requestBuilder?.apply {
            setHttpMethod(method)
            headers?.let{
                for (key in it.keys) {
                    addHeader(key, headers[key])
                }
            }
            return build()
        }
        return null
    }

    /**
     * Send a request to the URL.
     *
     * @param url Request URL.
     * @param method Request method type.
     * @param headers Request Headers
     */
    fun sendRequest(url: String, method: String, headers: HashMap<String, String>? = null) {
        Log.i(SERVICE_TAG, "callURL: url is " + url + "and method is " + method)
        val urlRequest: UrlRequest? = buildRequest(url, method, headers)
        urlRequest?.apply { urlRequest.start() }
    }

    /**
     * Parse the domain name to obtain the host name.
     *
     * @param url Request URL.
     * @return host Host name.
     */
    private fun getHost(url: String): String? {
        var host: String? = null
        try {
            val url1 = URL(url)
            host = url1.host
        } catch (e: MalformedURLException) {
            Log.e(SERVICE_TAG, "getHost: ", e)
        }
        return host
    }

    fun setCallback(mCallback: UrlRequest.Callback?) {
        callback = mCallback
    }

}