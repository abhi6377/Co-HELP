package com.app_dev.co_help

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app_dev.co_help.Models.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.util.*

class MyPostAdapter(options: FirestoreRecyclerOptions<Post>, private val listener: OnDelClickListener) : FirestoreRecyclerAdapter<Post, MyPostAdapter.MyPostViewHolder>(
    options) {

    class MyPostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val postText: TextView = itemView.findViewById(R.id.postBloodNewmy)
        val postCity: TextView = itemView.findViewById(R.id.postCityNewmy)
        val userText: TextView = itemView.findViewById(R.id.postUserNewmy)
        val createdAt: TextView = itemView.findViewById(R.id.postTimeNewmy)
        val postGender: TextView = itemView.findViewById(R.id.postGenderNewmy)
        val userAge: TextView = itemView.findViewById(R.id.postAgeNewmy)
        val postDel: ImageView = itemView.findViewById(R.id.postDelmy)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostViewHolder {
        val viewHolder = MyPostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_my, parent, false)
        )

        viewHolder.postDel.setOnClickListener {
            listener.onDelClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }

        return viewHolder
    }

    @ExperimentalStdlibApi
    override fun onBindViewHolder(holder: MyPostViewHolder, position: Int, model: Post) {
        holder.postText.text = model.GetBloodGr()
        holder.postCity.text = model.GetCity().capitalize()
        holder.userText.text = model.GetCreatedBy().toString()
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)
        holder.postGender.text = model.GetGender()
        holder.userAge.text = model.GetAge()
    }
}
interface OnDelClickListener{
    fun onDelClicked(postId: String)
}
