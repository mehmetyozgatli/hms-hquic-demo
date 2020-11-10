package com.myapps.hquicdemo.client

import android.content.Context
import android.util.Log
import com.myapps.hquicdemo.hquicservice.HQUICService
import com.myapps.hquicdemo.model.CoronaNewsItems
import com.myapps.hquicdemo.other.CAPACITY
import com.myapps.hquicdemo.other.NEWS_CLIENT_TAG
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import org.json.JSONObject
import java.nio.ByteBuffer

class CoronaNewsClient(context: Context): UrlRequest.Callback() {

    private var hQUICService : HQUICService? = null
    private var response : StringBuilder = java.lang.StringBuilder()
    private val listener = context as NewsClientListener

    init {
        hQUICService = HQUICService(context)
        hQUICService?.setCallback(this)
    }

    fun makeRequest(url: String, method: String, headers: HashMap<String, String>?=null){
        hQUICService?.sendRequest(url,method,headers)
    }

    override fun onRedirectReceived(
        request: UrlRequest?,
        info: UrlResponseInfo?,
        newLocationUrl: String?
    ) {
        request?.followRedirect()
    }

    override fun onResponseStarted(request: UrlRequest?, info: UrlResponseInfo?) {
        Log.i(NEWS_CLIENT_TAG, "onResponseStarted: ")
        val byteBuffer = ByteBuffer.allocateDirect(CAPACITY)
        request?.read(byteBuffer)
    }

    override fun onReadCompleted(
        request: UrlRequest?,
        info: UrlResponseInfo?,
        byteBuffer: ByteBuffer?
    ) {
        Log.i(NEWS_CLIENT_TAG, "onReadCompleted: method is called")
        val readCompleted= String(byteBuffer!!.array(), byteBuffer.arrayOffset(), byteBuffer.position())
        response.append(readCompleted)
        request?.read(ByteBuffer.allocateDirect(CAPACITY))
    }

    override fun onSucceeded(request: UrlRequest?, info: UrlResponseInfo?) {

        Log.d("Response", response.toString())
        //If everything is ok you can read the response body
        val json = JSONObject(response.toString())
        val array= json.getJSONArray("result")

        val list = ArrayList<CoronaNewsItems>()
        for (i in 0 until array.length()){
            val coronaNews= array.getJSONObject(i)
            val url= coronaNews.getString("url")
            val description= coronaNews.getString("description")
            val image= coronaNews.getString("image")
            val name= coronaNews.getString("name")
            val source= coronaNews.getString("source")
            val date = coronaNews.getString("date")

            list.add(CoronaNewsItems(url, description, image, name, source, date))
        }
        Log.d("ResponseList", list.toString())
        listener.onSuccess(list)
    }

    override fun onFailed(request: UrlRequest?, info: UrlResponseInfo?, error: CronetException?) {
        //If something fails you must report the error
        listener.onFailure(error.toString())
    }
}