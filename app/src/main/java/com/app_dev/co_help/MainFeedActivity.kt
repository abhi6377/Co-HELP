package com.app_dev.co_help

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app_dev.co_help.Adapters.PostAdapter
import com.app_dev.co_help.Daos.PostDao
import com.app_dev.co_help.Models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main_feed.*

class MainFeedActivity : AppCompatActivity(),PostAdapter.OnMailClickListener {

    private lateinit var adapter: PostAdapter
    private lateinit var postDao: PostDao
    private lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_feed)

        supportActionBar!!.title = "Plasma Feeds"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val googleEmail: String? = intent.getStringExtra("googleEmail")

        fab.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }
        setUpRecyclerView()

        btn_Search.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
            intent.putExtra("googleEmail", googleEmail)

        }
    }

    private fun setUpRecyclerView() {
        postDao = PostDao()
        val postsCollections = postDao.postCollections
        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions =
            FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions,this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onMailClicked(postId: String) {
        Toast.makeText(this, "Checking $postId", Toast.LENGTH_SHORT).show()


        val db = FirebaseFirestore.getInstance()
        val postCollections = db.collection("posts")
        val intent = Intent(this@MainFeedActivity, SendEmailActivity::class.java)

        postCollections.document(postId).get().addOnSuccessListener {
            it.getString("email")?.let { it1 -> Log.d("Data: ", it1) }
            Toast.makeText(this, "Required: ${it.getString("email")}", Toast.LENGTH_SHORT).show()
            val rEmail = it.getString("email").toString()
            userEmail = it.getString("email").toString()
            //intent.putExtra(rEmail,"${rEmail}")
            Toast.makeText(this, "Required2: ${userEmail}", Toast.LENGTH_SHORT).show()

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Post Creator's Email")
            builder.setMessage("Please copy this email of the respective post creater to help them.\nEmail: $rEmail")
                .setPositiveButton("Done",
                    DialogInterface.OnClickListener { dialog, id ->
                        intent.putExtra("rEmail", rEmail)
                        startActivity(intent)
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        startActivity(Intent(this, MainActivity::class.java))
                    })
            builder.create()

            builder.show()

        }
    }
}

/* CHANGED BY ME 6 - added interface and method*/