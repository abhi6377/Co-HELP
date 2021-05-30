package com.app_dev.co_help

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        resetpass.setOnClickListener {
            val email :String = forgotpassEmail.text.toString().trim {it<= ' '}
            if(email.isEmpty())
            {
                Toast.makeText(applicationContext,"Please enter your Email Address",Toast.LENGTH_SHORT).show()

            }
            else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            Toast.makeText(applicationContext,"Email sent successfully to reset your password!",Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        else
                        {
                            Toast.makeText(applicationContext,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }
}