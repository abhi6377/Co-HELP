package com.app_dev.co_help

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
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
                        if (it.isSuccessful && auth.currentUser!!.isEmailVerified) {

                            Email.setText("")
                            Password.setText("")

                            val intent = Intent(this@loginactivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()

                        } else if(!auth.currentUser!!.isEmailVerified){
                            Toast.makeText(
                                applicationContext,
                                "You have not verified your Email",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                        else{
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

        forgotpassword.setOnClickListener {
            startActivity(Intent(this@loginactivity,ForgotPassword::class.java))

        }

}

    override fun onBackPressed() {
        AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
            .setMessage("Are you sure?")
            .setPositiveButton("yes", DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }).setNegativeButton("no", null).show()
    }


}