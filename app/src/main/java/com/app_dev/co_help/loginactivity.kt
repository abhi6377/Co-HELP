package com.app_dev.co_help

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.Constants
import kotlinx.android.synthetic.main.activity_loginactivity.*
import kotlinx.android.synthetic.main.activity_loginactivity.Email
import kotlinx.android.synthetic.main.activity_loginactivity.Password
import kotlinx.android.synthetic.main.activity_loginactivity.btn_login_loginPage
import kotlinx.android.synthetic.main.activity_loginactivity.btn_signUp_loginPage
import kotlinx.android.synthetic.main.activity_loginactivity.*
import kotlinx.android.synthetic.main.nav_header.*

class loginactivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginactivity)

        auth = FirebaseAuth.getInstance()

        btn_login_loginPage.setOnClickListener {
            val email = Email.text.toString()
            val password = Password.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(
                    applicationContext,
                    "Please enter all the details",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {

                            Email.setText("")
                            Password.setText("")

                            val intent = Intent(this@loginactivity, MainActivity::class.java)
                            startActivity(intent)

                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Email or password invalid",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
            }
        }
        btn_signUp_loginPage.setOnClickListener {

            val intent = Intent(this@loginactivity, registerActivity::class.java)
            startActivity(intent)


        }

}
}