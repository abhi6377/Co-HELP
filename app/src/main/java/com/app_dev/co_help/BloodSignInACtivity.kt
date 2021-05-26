package com.app_dev.co_help

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app_dev.co_help.Daos.UserDao
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_blood_sign_in.*

class BloodSignInACtivity : AppCompatActivity() {

    var auth=FirebaseAuth.getInstance()
    private  lateinit var googleSignInClient: GoogleSignInClient
    private val  RC_SIGN_IN:Int = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blood_sign_in)

        supportActionBar!!.title = "Blood Plasma Verification "
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("776125300491-laosr0mmekh8holf9ueh06an8do1iism.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        signInBtn.setOnClickListener{
            signIn()
        }

    }
//
//    override fun onStart() {
//        super.onStart()
//
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
//    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val task:Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    private fun UpdateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken, null)

        val firebaseUser = auth.currentUser

        auth.signInWithCredential(credential).addOnCompleteListener { task->
            if(task.isSuccessful) {

                if(firebaseUser!=null) {
                    val user = com.app_dev.co_help.Models.User(
                        firebaseUser.uid,
                        firebaseUser.displayName.toString(),
                        firebaseUser.photoUrl.toString()
                    )
                    val usersDao = UserDao()
                    usersDao.addUser(user)

                    val intent = Intent(this, MainFeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            var personEmail: String? =null
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            if (acct != null) {
                personEmail= acct.email.toString()
            }
            startActivity(Intent(this, MainFeedActivity::class.java))
            intent.putExtra("googleEmail",personEmail)
            finish()
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

