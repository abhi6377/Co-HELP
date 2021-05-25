package com.app_dev.co_help

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class registerActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth=FirebaseAuth.getInstance()

        backtoLogin.setOnClickListener {
            val intent= Intent(this@registerActivity, loginactivity::class.java)
            startActivity(intent)
        }

        btn_signUp.setOnClickListener {
            val username = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(applicationContext, "Please enter all the details!", Toast.LENGTH_SHORT)
                    .show()
            }


            if (password != confirmPassword) {

                Toast.makeText(
                    applicationContext,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                ).show()

            }
            if(username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password==confirmPassword)
                registerUser(username, email, password)


        }

    }

    private fun registerUser(userName:String,email:String,password:String)
    {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    val user:FirebaseUser? = auth.currentUser
                    val userId:String = user!!.uid

                    databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap.put("userId",userId)
                    hashMap.put("userName",userName)

                    databaseReference.setValue(hashMap).addOnCompleteListener(this){
                        if(it.isSuccessful){
//                            //open home activity
                            etEmail.setText("")
                            etPassword.setText("")
                            etName.setText("")
                            etConfirmPassword.setText("")
                            val intent=Intent(this@registerActivity, loginactivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                }
            }
    }


}