package com.app_dev.co_help

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.app_dev.co_help.Adapters.OnBoardingViewPageAdapter
import com.app_dev.co_help.Models.OnBoardingData
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_on_board.*

class OnBoardActivity : AppCompatActivity() {

    var OnBoardingViewPageAdapter: OnBoardingViewPageAdapter? = null
    var tabLayout: TabLayout? = null
    var onBoardingViewPager : ViewPager? = null
    var position: Int = 0
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)

        tabLayout = findViewById(R.id.tab_indicator)

        val onBoardingData: MutableList<OnBoardingData> = ArrayList()

        onBoardingData.add(OnBoardingData("Covid Statistics","This provides you with the realtime covid statistics around the globe.",R.drawable.pathogen))
        onBoardingData.add(OnBoardingData("Nearby Vaccine Center","Lets you know the vaccine centers available within your area.",R.drawable.vaccine))
        onBoardingData.add(OnBoardingData("Plasma Donation","Create a community where people can share and help each other. Just click the mail icon in their post and let them know that you are here to help.",R.drawable.bloodtest))

        if (restorePrefData()){
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        }

        setOnBoardingViewAdapter(onBoardingData)

        position = onBoardingViewPager!!.currentItem

        next?.setOnClickListener{
            if(position<onBoardingData.size){
                position++
                onBoardingViewPager!!.currentItem = position
            }
            if(position == onBoardingData.size){
                savePrefData()
                val intent = Intent(applicationContext,MainActivity::class.java)
                startActivity(intent)
            }
        }

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                position = tab!!.position
                if(tab.position == onBoardingData.size-1){
                    next!!.text = "Let's Go"
                }else{
                    next!!.text = "Next"
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }
    private fun setOnBoardingViewAdapter(onBoardingData: MutableList<OnBoardingData>){

        onBoardingViewPager = findViewById(R.id.screenPager)
        OnBoardingViewPageAdapter = OnBoardingViewPageAdapter(this,
            onBoardingData as ArrayList<OnBoardingData>
        )
        onBoardingViewPager!!.adapter = OnBoardingViewPageAdapter
        tabLayout?.setupWithViewPager(onBoardingViewPager)

    }

    private fun savePrefData(){
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
        editor.putBoolean("isFirstTimeRun",true)
        editor.apply()
    }

    private fun restorePrefData(): Boolean{
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("isFirstTimeRUn",false)
    }

}