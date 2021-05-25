package com.app_dev.co_help.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app_dev.co_help.Models.Post
import com.app_dev.co_help.R
import com.app_dev.co_help.Utils

class SearchAdapter(private val searchList: ArrayList<Post>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var postText: TextView = itemView.findViewById(R.id.postBloodNew)
        val postCity: TextView = itemView.findViewById(R.id.postCityNew)
        val userText: TextView = itemView.findViewById(R.id.postUserNew)
        val createdAt: TextView = itemView.findViewById(R.id.postTimeNew)
        val postGender: TextView = itemView.findViewById(R.id.postGenderNew)
        val userAge: TextView = itemView.findViewById(R.id.postAgeNew)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return SearchAdapter.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_new, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.postText.text = searchList[position].bloodGr
        holder.postCity.text = searchList[position].city
        holder.userText.text = searchList[position].createdBy.displayName
        holder.createdAt.text = Utils.getTimeAgo(searchList[position].createdAt)
        holder.postGender.text = searchList[position].gender
        holder.userAge.text = searchList[position].age

    }

    override fun getItemCount(): Int {
        return searchList.size
    }


}