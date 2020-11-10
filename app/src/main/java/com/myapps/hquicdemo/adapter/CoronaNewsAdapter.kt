package com.myapps.hquicdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myapps.hquicdemo.R
import com.myapps.hquicdemo.model.CoronaNewsItems
import kotlinx.android.synthetic.main.item_view.view.*

class CoronaNewsAdapter(
    private val coronaNews: List<CoronaNewsItems>, private var context: Context
) :
    RecyclerView.Adapter<CoronaNewsAdapter.CoronaNewsViewHolder>() {

    class CoronaNewsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun init(items: CoronaNewsItems) {

            itemView.title.text = items.source
            itemView.content.text = items.name
            itemView.description.text = items.description

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CoronaNewsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false)
        return CoronaNewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoronaNewsViewHolder, position: Int) {
        holder.init(coronaNews[position])
    }

    override fun getItemCount(): Int {
        return coronaNews.size
    }
}