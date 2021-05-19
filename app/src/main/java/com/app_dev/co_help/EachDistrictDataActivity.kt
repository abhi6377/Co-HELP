package com.app_dev.co_help

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import com.app_dev.co_help.Adapters.DistrictWiseAdapter.*
import kotlinx.android.synthetic.main.activity_each_district_data.*
import org.eazegraph.lib.models.PieModel
import java.text.NumberFormat

class EachDistrictDataActivity : AppCompatActivity() {


    private var str_districtName: String? = null
    private var str_confirmed: String? = null
    private var str_confirmed_new: String? = null
    private var str_active: String? = null
    private val str_active_new: String? = null
    private var str_death: String? = null
    private var str_death_new: String? = null
    private var str_recovered: String? = null
    private var str_recovered_new: String? = null

    private val activity = IndiaDataActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_each_district_data)

        GetIntent()

        supportActionBar!!.title = str_districtName

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        LoadDistrictData()
    }

    private fun LoadDistrictData() {


        activity.ShowDialog(this)
        val postDelayToshowProgress = Handler()
        postDelayToshowProgress.postDelayed(Runnable {
            activity_each_district_confirmed_textView.text = NumberFormat.getInstance().format(str_confirmed!!.toInt())

            activity_each_district_confirmed_new_textView!!.text =
                "+" + NumberFormat.getInstance().format(str_confirmed_new!!.toInt())

            activity_each_district_active_textView.text = NumberFormat.getInstance().format(str_active!!.toInt())

            val int_active_new =
                str_confirmed_new!!.toInt() - (str_recovered_new!!.toInt() + str_death_new!!.toInt())

            activity_each_district_active_new_textView!!.text = "+" + NumberFormat.getInstance()
                .format(if (int_active_new < 0) 0 else int_active_new)

            activity_each_district_death_textView.text = NumberFormat.getInstance().format(str_death!!.toInt())

            activity_each_district_death_new_textView!!.text = "+" + NumberFormat.getInstance().format(str_death_new!!.toInt())

            activity_each_district_recovered_textView.text = NumberFormat.getInstance().format(str_recovered!!.toInt())

            activity_each_district_recovered_new_textView!!.text =
                "+" + NumberFormat.getInstance().format(str_recovered_new!!.toInt())


            activity_each_district_piechart.addPieSlice(
                PieModel(
                    "Active",
                    str_active!!.toInt().toFloat(),
                    Color.parseColor("#007afe")
                )
            )
            activity_each_district_piechart.addPieSlice(
                PieModel(
                    "Recovered",
                    str_recovered!!.toInt().toFloat(),
                    Color.parseColor("#08a045")
                )
            )
            activity_each_district_piechart.addPieSlice(
                PieModel(
                    "Deceased",
                    str_death!!.toInt().toFloat(),
                    Color.parseColor("#F6404F")
                )
            )
            activity_each_district_piechart.startAnimation()
            activity.DismissDialog()
        }, 1000)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun GetIntent() {
        val intent = intent
        str_districtName = intent.getStringExtra(DISTRICT_NAME)
        str_confirmed = intent.getStringExtra(DISTRICT_CONFIRMED)
        str_confirmed_new = intent.getStringExtra(DISTRICT_CONFIRMED_NEW)
        str_active = intent.getStringExtra(DISTRICT_ACTIVE)
        str_death = intent.getStringExtra(DISTRICT_DEATH)
        str_death_new = intent.getStringExtra(DISTRICT_DEATH_NEW)
        str_recovered = intent.getStringExtra(DISTRICT_RECOVERED)
        str_recovered_new = intent.getStringExtra(DISTRICT_RECOVERED_NEW)

    }

}