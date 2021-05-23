package com.app_dev.co_help

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.*

class MainActivity : AppCompatActivity() {
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var preferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        auth = FirebaseAuth.getInstance()
//        val nameUser: String = auth.currentUser?.displayName.toString()
//        username_nav.text=nameUser


        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Nav_View.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.Item1 -> {
                    val intent= Intent(this,IndiaDataActivity::class.java)
                    startActivity(intent)
                }

                R.id.Item2 -> {
                    val intent= Intent(this,blood::class.java)
                    startActivity(intent)
                }

                R.id.Item3 -> {
                    val intent= Intent(this,vaccine::class.java)
                    startActivity(intent)

                }

                R.id.LogOut -> {
                    val editor:SharedPreferences.Editor=preferences.edit()
                    editor.putBoolean("CHECKBOX",false)
                    editor.clear()
                    editor.apply()

                    val intent= Intent(this@MainActivity,loginactivity::class.java)
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
}