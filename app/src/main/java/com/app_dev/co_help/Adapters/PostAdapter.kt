package com.app_dev.co_help.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app_dev.co_help.Models.Post
import com.app_dev.co_help.R
import com.app_dev.co_help.Utils
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class PostAdapter(options: FirestoreRecyclerOptions<Post>) : FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(
    options
) {

    private lateinit var  postList: ArrayList<Post>

    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val postText: TextView = itemView.findViewById(R.id.postBloodNew)
        val postCity: TextView = itemView.findViewById(R.id.postCityNew)
        val userText: TextView = itemView.findViewById(R.id.postUserNew)
        val createdAt: TextView = itemView.findViewById(R.id.postTimeNew)
        val postGender: TextView = itemView.findViewById(R.id.postGenderNew)
        val userAge: TextView = itemView.findViewById(R.id.postAgeNew)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post_new, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.postText.text = model.GetBloodGr()
        holder.postCity.text = model.GetCity()
        holder.userText.text = model.GetCreatedBy().displayName.toString()
        holder.createdAt.text = Utils.getTimeAgo(model.createdAt)
        holder.postGender.text = model.GetGender().toString()
        holder.userAge.text = model.GetAge()

    }

}
