package com.app_dev.co_help

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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