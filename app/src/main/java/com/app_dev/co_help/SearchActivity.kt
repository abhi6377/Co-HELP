package com.app_dev.co_help

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app_dev.co_help.Adapters.SearchAdapter
import com.app_dev.co_help.Daos.PostDao
import com.app_dev.co_help.Models.Post
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), SearchAdapter.OnMailClickListenerS {

    /* CHANGED BY ME 10 - added interface , implemented its member and changed adapter line 72*/

    private lateinit var sAdapter: SearchAdapter
    private lateinit var searchList: ArrayList<Post>
    private lateinit var post: Post

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar!!.title = "Search City "
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        intent.getStringExtra("googleEmail")


        btn_SearchS.setOnClickListener{
            filter(searchCityS.text)
        }
    }

    @ExperimentalStdlibApi
    private fun filter(text: Editable) {

        searchList = ArrayList<Post>()
        var postDao = PostDao()
        val postsCollections = postDao.postCollections
        val query = postsCollections.whereEqualTo("city","${text.toString().lowercase()}")

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
                if(searchList.isEmpty()){
                    Toast.makeText(this,"No post available",Toast.LENGTH_LONG).show()
                    animationempty.visibility = View.VISIBLE
                }else{
                    animationempty.visibility = View.GONE
                }
                sAdapter.notifyDataSetChanged()

            }else{
                Toast.makeText(this,"Failed Search .. Try different city name ", Toast.LENGTH_SHORT).show()
            }
        })

        sAdapter = SearchAdapter(searchList,this)

        recyclerViewS.adapter = sAdapter
        recyclerViewS.layoutManager = LinearLayoutManager(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onMailClickedS(email: String?) {
        Toast.makeText(this,"EmailS: $email",Toast.LENGTH_SHORT).show()
        var intent = Intent(this,SendEmailActivity::class.java)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Post Creator's Email")
        builder.setMessage("Please copy this email of the respective post creater to help them.\nEmail: $email")
            .setPositiveButton("Done",
                DialogInterface.OnClickListener { dialog, id ->
                    intent.putExtra("rEmail",email)
                    startActivity(intent)
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    startActivity(Intent(this,SearchActivity::class.java))
                })
        builder.create()

        builder.show()
    }

}