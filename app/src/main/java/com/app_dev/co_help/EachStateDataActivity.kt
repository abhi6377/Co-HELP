package com.app_dev.co_help

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import com.app_dev.co_help.Adapters.StateWiseAdapter.*
import kotlinx.android.synthetic.main.activity_each_state_data.*
import org.eazegraph.lib.models.PieModel
import java.text.NumberFormat

class EachStateDataActivity : AppCompatActivity() {

    private var activity = IndiaDataActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_each_state_data)

        val intent = intent
        var str_stateName = intent.getStringExtra(STATE_NAME)


        supportActionBar?.setTitle(str_stateName)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


        LoadStateData()

        activity_each_state_lin.setOnClickListener{
            val intent = Intent(this@EachStateDataActivity , DistrictWiseDataActivity::class.java)
            intent.putExtra(STATE_NAME,str_stateName)
            startActivity(intent)
        }
    }

    private fun LoadStateData() {

        activity.ShowDialog(this)

        val postDelayToshowProgress = Handler()

        val intent = intent
        var str_stateName = intent.getStringExtra(STATE_NAME)
        var str_confirmed = intent.getStringExtra(STATE_CONFIRMED)
        var str_confirmed_new = intent.getStringExtra(STATE_CONFIRMED_NEW)
        var str_active = intent.getStringExtra(STATE_ACTIVE)
        var str_death = intent.getStringExtra(STATE_DEATH)
        var str_death_new = intent.getStringExtra(STATE_DEATH_NEW)
        var str_recovered = intent.getStringExtra(STATE_RECOVERED)
        var str_recovered_new = intent.getStringExtra(STATE_RECOVERED_NEW)
        var str_lastupdatedate = intent.getStringExtra(STATE_LAST_UPDATE)

        postDelayToshowProgress.postDelayed(Runnable {

            activity_each_state_confirmed_textView.text = NumberFormat.getInstance().format(str_confirmed!!.toInt())
            activity_each_state_confirmed_new_textView.text = "+" + NumberFormat.getInstance().format(str_confirmed_new!!.toInt())
            activity_each_state_active_textView.text = NumberFormat.getInstance().format(str_active!!.toInt())

            val int_active_new: Int =
                str_confirmed_new.toInt() - (str_recovered_new!!.toInt() + str_death_new!!.toInt())

            activity_each_state_active_new_textView.text = "+" + NumberFormat.getInstance().format(if (int_active_new < 0) 0 else int_active_new)
            activity_each_state_death_textView.text = NumberFormat.getInstance().format(str_death!!.toInt())
            activity_each_state_death_new_textView.text = "+" + NumberFormat.getInstance().format(str_death_new.toInt())
            activity_each_state_recovered_textView.text = NumberFormat.getInstance().format(str_recovered!!.toInt())
            activity_each_state_recovered_new_textView.text = "+" + NumberFormat.getInstance().format(str_recovered_new.toInt())

            val formatDate: String? = IndiaDataActivity().FormatDate(str_lastupdatedate, 0)

            activity_each_state_lastupdate_textView.text = formatDate
            activity_each_state_district_data_title.text = "District data of $str_stateName"

            activity_each_state_piechart.addPieSlice(
                PieModel(
                    "Active",
                    str_active.toInt().toFloat(),
                    Color.parseColor("#007afe")
                )
            )
            activity_each_state_piechart.addPieSlice(
                PieModel(
                    "Recovered",
                    str_recovered.toInt().toFloat(),
                    Color.parseColor("#08a045")
                )
            )
            activity_each_state_piechart.addPieSlice(
                PieModel(
                    "Deceased",
                    str_death.toInt().toFloat(),
                    Color.parseColor("#F6404F")
                )
            )
            activity_each_state_piechart.startAnimation()
            activity.DismissDialog()
        }, 2000)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

}