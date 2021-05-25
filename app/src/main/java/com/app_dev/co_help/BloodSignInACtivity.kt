package com.app_dev.co_help

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.app_dev.co_help.Daos.UserDao
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_blood_sign_in.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BloodSignInACtivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private  lateinit var googleSignInClient: GoogleSignInClient
    private val  RC_SIGN_IN:Int = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blood_sign_in)

        supportActionBar!!.title = "Blood Plasma Verification "
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

        signInBtn.setOnClickListener{
            signIn()
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {

        try {
            val account = task?.getResult(ApiException::class.java)!!

            Log.d(ContentValues.TAG,"firebaseAuthWithGoogle"+account.id)
            firebaseAuthWithGoogle(account.idToken!!)

        }catch (e: ApiException){
            Log.w(ContentValues.TAG,"signInResult:Failed code" + e.statusCode)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken,null)

        GlobalScope.launch(Dispatchers.IO) {
            runOnUiThread{
                signInBtn.visibility = View.GONE
                progressbar.visibility = View . VISIBLE
            }

            val auth = auth.signInWithCredential(credential).await()
            val firebaseUser = auth.user
            withContext(Dispatchers.Main){
                updateUI(firebaseUser)
            }
        }

    }

    @SuppressLint("RestrictedApi")
    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser!=null){

            val user = com.app_dev.co_help.Models.User(firebaseUser.uid, firebaseUser.displayName , firebaseUser.photoUrl.toString())
            val usersDao = UserDao()
            usersDao.addUser(user)

            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            runOnUiThread {

                signInBtn.visibility = View.VISIBLE
                progressbar.visibility = View.GONE
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}