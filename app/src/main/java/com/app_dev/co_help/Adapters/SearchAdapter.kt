package com.app_dev.co_help.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app_dev.co_help.Models.Post
import com.app_dev.co_help.R
import com.app_dev.co_help.Utils

class SearchAdapter(private val searchList: ArrayList<Post>, private val listener: OnMailClickListenerS) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    /*CHANGED BY ME 10 - added interface and method alon with onclick inside ViewBinder*/

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var postText: TextView = itemView.findViewById(R.id.postBloodNew)
        val postCity: TextView = itemView.findViewById(R.id.postCityNew)
        val userText: TextView = itemView.findViewById(R.id.postUserNew)
        val createdAt: TextView = itemView.findViewById(R.id.postTimeNew)
        val postGender: TextView = itemView.findViewById(R.id.postGenderNew)
        val userAge: TextView = itemView.findViewById(R.id.postAgeNew)

        val postMail: ImageView = itemView.findViewById(R.id.postMail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = SearchAdapter.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_new, parent, false)
        )

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.postText.text = searchList[position].bloodGr
        holder.postCity.text = searchList[position].city
        holder.userText.text = searchList[position].createdBy.displayName
        holder.createdAt.text = Utils.getTimeAgo(searchList[position].createdAt)
        holder.postGender.text = searchList[position].gender
        holder.userAge.text = searchList[position].age

        holder.postMail.setOnClickListener{
            listener.onMailClickedS(searchList[position].email)
        }


    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    interface OnMailClickListenerS{
        fun onMailClickedS(email: String?)
    }

}