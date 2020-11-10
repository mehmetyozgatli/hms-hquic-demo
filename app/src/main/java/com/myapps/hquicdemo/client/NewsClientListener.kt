package com.myapps.hquicdemo.client

import com.myapps.hquicdemo.model.CoronaNewsItems

interface NewsClientListener {

    fun onSuccess(coronaNews : ArrayList<CoronaNewsItems>)
    fun onFailure(error: String)

}