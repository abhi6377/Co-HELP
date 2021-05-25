package com.app_dev.co_help

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app_dev.co_help.Daos.PostDao
import com.app_dev.co_help.Models.Post
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePostActivity : AppCompatActivity() {

    lateinit var postList: ArrayList<Post>
    private lateinit var postDao: PostDao

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        supportActionBar!!.title = "Create Post "
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        postDao = PostDao()

        postButton.setOnClickListener {
            val input = postInput.text.toString().uppercase().trim()
            val inputCity = postInputCity.text.toString().trim()
            val inputAge = postInputAge.text.toString().trim()
            val inputGender = postInputGender.text.toString().trim()
            if (input.isNotEmpty() && inputCity.isNotEmpty()) {
                postDao?.addPost(input, inputCity, inputAge, inputGender)
                postList = ArrayList<Post>()
                finish()
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}