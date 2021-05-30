package com.app_dev.co_help

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.app_dev.co_help.Daos.PostDao
import com.app_dev.co_help.Models.Post
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePostActivity : AppCompatActivity() {

    lateinit var postList: ArrayList<Post>
    private lateinit var postDao: PostDao
    private  var email: String? = ""



    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        supportActionBar!!.title = "Create Post "
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val option =ArrayList<String>()
        option.add("AB+")
        option.add("AB-")
        option.add("A+")
        option.add("A-")
        option.add("B+")
        option.add("B-")
        option.add("O+")
        option.add("O-")
        val arrayAdapter=ArrayAdapter(this,R.layout.dropdown_items,option)

        bloodgrpselection.setAdapter(arrayAdapter)

        val option2 =ArrayList<String>()
        option2.add("Male")
        option2.add("Female")

        val arrayAdapter2=ArrayAdapter(this,R.layout.dropdown_items,option2)
//        bloodgrpselection.setText(arrayAdapter.getItem(0))
        Gender.setAdapter(arrayAdapter2)

        postDao = PostDao()

        /* CHANGED BY ME 7 - getting current users email via firebase BloodUsers*/

        val db = FirebaseFirestore.getInstance()
        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid


        db.collection("BloodUsers").document("$currentUserId").get().addOnSuccessListener {

            it.getString("uemail")?.let { it1 -> Log.d("Data: ", it1) }
            email = it.getString("uemail")

        }


        postButton.setOnClickListener {
            val input = bloodgrpselection.text.toString().uppercase().trim()
            val inputCity = postInputCity.text.toString().lowercase().trim()
            val inputAge = postInputAge.text.toString().trim()
            val inputGender = Gender.text.toString().trim()
            if (input.isNotEmpty() && inputCity.isNotEmpty()) {
                postDao.addPost(input, inputCity, inputAge, inputGender,email)
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