package com.app_dev.co_help

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app_dev.co_help.Adapters.SearchAdapter
import com.app_dev.co_help.Daos.PostDao
import com.app_dev.co_help.Models.Post
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private lateinit var sAdapter: SearchAdapter
    private lateinit var searchList: ArrayList<Post>
    private lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar!!.title = "Search City "
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        filter(searchCityS.text)
        btn_SearchS.setOnClickListener{
            filter(searchCityS.text)
        }
    }

    private fun filter(text: Editable) {

        searchList = ArrayList<Post>()
        var postDao = PostDao()
        val postsCollections = postDao.postCollections
        val query = postsCollections.whereEqualTo("city","$text")

        query.get().addOnSuccessListener {
            for(document in it){
                Log.d("Post Id" , "${document.data}")
            }
        }
            .addOnFailureListener{
                Log.w("Failure", "Error getting documents",it)
            }

        query.get().addOnCompleteListener(OnCompleteListener(){
            if (it.isSuccessful){

                for (documents in it.result!!){
                    post = documents.toObject(Post::class.java)
                    searchList.add(post)
                }
                sAdapter.notifyDataSetChanged()

            }else{
                Toast.makeText(this,"Failed Search .. Try different city name ", Toast.LENGTH_SHORT).show()
            }
        })

        sAdapter = SearchAdapter(searchList)

        recyclerViewS.adapter = sAdapter
        recyclerViewS.layoutManager = LinearLayoutManager(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}