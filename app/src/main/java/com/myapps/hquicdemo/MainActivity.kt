package com.myapps.hquicdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapps.hquicdemo.adapter.CoronaNewsAdapter
import com.myapps.hquicdemo.client.CoronaNewsClient
import com.myapps.hquicdemo.client.NewsClientListener
import com.myapps.hquicdemo.model.CoronaNewsItems

class MainActivity : AppCompatActivity(), NewsClientListener {

    private var recyclerView: RecyclerView? = null
    private var newsAdapter: CoronaNewsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerview)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = linearLayoutManager

        val map = HashMap<String,String>()
        map["Content-Type"] = "application/json"
        map["Authorization"] = "apikey"

        val coronaNewsClient = CoronaNewsClient(this)
        coronaNewsClient.makeRequest("https://api.collectapi.com/corona/coronaNews", "GET", map)
    }

    override fun onSuccess(coronaNews: ArrayList<CoronaNewsItems>) {
        runOnUiThread{
            Log.d("ResponseMainActivity", coronaNews.toString())
            newsAdapter = CoronaNewsAdapter(coronaNews, this)
            recyclerView?.adapter = newsAdapter
        }

    }

    override fun onFailure(error: String) {
        runOnUiThread {
            Toast.makeText(this, "Error onFailure", Toast.LENGTH_LONG).show()
        }
    }
}