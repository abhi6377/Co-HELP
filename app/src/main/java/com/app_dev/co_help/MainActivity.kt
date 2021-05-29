package com.app_dev.co_help

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_loginactivity.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.*

class MainActivity : AppCompatActivity() {
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var preferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var mDatabase:FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        guidebtn.setOnClickListener{
            startActivity(Intent(this,OnBoardActivity::class.java))
        }

        mDatabase= FirebaseDatabase.getInstance()
        databaseReference=mDatabase!!.reference!!.child("Users")
        auth = FirebaseAuth.getInstance()


        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        call1.setOnClickListener {
            val intent= Intent(Intent.ACTION_DIAL)
            val no:String = "1075"
            val temp :String = "tel:$no"
            intent.data = Uri.parse(temp)
            startActivity(intent);
        }

        call2.setOnClickListener {
            val intent= Intent(Intent.ACTION_DIAL)
            val no:String ="1098"
            val temp :String = "tel:$no"
            intent.data = Uri.parse(temp)
            startActivity(intent);
        }

        call3.setOnClickListener {
            val intent= Intent(Intent.ACTION_DIAL)
            val no:String = "08046110007"
            val temp :String = "tel:$no"
            intent.data = Uri.parse(temp)
            startActivity(intent);
        }

        call4.setOnClickListener {
            val intent= Intent(Intent.ACTION_DIAL)
            val no:String ="14567"
            val temp :String = "tel:$no"
            intent.data = Uri.parse(temp)
            startActivity(intent);
        }

        Nav_View.itemIconTintList=null

        Nav_View.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.Item1 -> {
                    val intent= Intent(this,IndiaDataActivity::class.java)
                    startActivity(intent)
                }

                R.id.Item2 -> {
                    val intent= Intent(this,BloodSignInACtivity::class.java)
                    startActivity(intent)
                }

                R.id.Item3 -> {
                    val intent= Intent(this,vaccine::class.java)
                    startActivity(intent)

                }

                R.id.LogOut -> {

                    FirebaseAuth.getInstance().signOut()

                    val intent= Intent(this,loginactivity::class.java)
                    intent.putExtra("remember",false)

                    startActivity(intent)
                    finish()

                }

            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if(toggle.onOptionsItemSelected(item))
    {
        return true
    }
    return super.onOptionsItemSelected(item)
}
    override fun onStart() {
        super.onStart()

        if(FirebaseAuth.getInstance().currentUser != null)
        {
        val mUser = auth.currentUser
        val mUserReference = databaseReference.child(mUser!!.uid)



        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                username_nav!!.text = "Hi, " + snapshot.child("userName").value as String?

            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
         }
        else
        {
            val intent= Intent(this,loginactivity::class.java)
            startActivity(intent)

        }
}}