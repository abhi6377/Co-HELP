package com.app_dev.co_help

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app_dev.co_help.Daos.PostDao
import com.app_dev.co_help.Models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_my_feeds.*

class MyFeedsActivity : AppCompatActivity(), OnDelClickListener {

    private lateinit var myAdapter: MyPostAdapter
    private lateinit var post: Post
    private lateinit var auth: FirebaseAuth
    private lateinit var postDao: PostDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_feeds)

        setUp()
        auth = Firebase.auth
    }

    private fun setUp() {
        auth = Firebase.auth
        val currentUserEmail = auth.currentUser?.email
        postDao = PostDao()
        val postsCollections = postDao.postCollections
        val query = postsCollections.whereEqualTo("email","$currentUserEmail")
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

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
                }
            }else{
                Toast.makeText(this,"Failed Search .. Try different city name ", Toast.LENGTH_SHORT).show()
            }
        })

        myAdapter = MyPostAdapter(recyclerViewOptions,this)

        recyclerViewMy.adapter = myAdapter
        recyclerViewMy.layoutManager = LinearLayoutManager(this)

    }

    override fun onStart() {
        super.onStart()
        myAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        myAdapter.stopListening()
    }

    override fun onDelClicked(postId: String) {

        val postDao = PostDao()
        val postsCollections = postDao.postCollections

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Post")
        builder.setMessage("Are you sure you want to delete your post")
            .setPositiveButton("Done",
                DialogInterface.OnClickListener { dialog, id ->
                    postsCollections.document(postId)
                        .delete()
                        .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted!")
                            Toast.makeText(this,"Post Deleted Successfully", Toast.LENGTH_LONG).show()
                            myAdapter.notifyDataSetChanged()}
                        .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error deleting document", e) }
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    startActivity(Intent(this,MyFeedsActivity::class.java))
                })
        builder.create()

        builder.show()
    }
}