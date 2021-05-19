package com.app_dev.co_help

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import com.app_dev.co_help.Adapters.CountryWiseAdapter.*
import kotlinx.android.synthetic.main.activity_each_country_data.*
import org.eazegraph.lib.models.PieModel
import java.text.NumberFormat


class EachCountryDataActivity : AppCompatActivity() {

    private var str_countryName: String? = null
    private var str_confirmed: String? = null
    private var str_confirmed_new: String? = null
    private var str_active: String? = null
    private val str_active_new: String? = null
    private var str_death: String? = null
    private var str_death_new: String? = null
    private var str_recovered: String? = null
    private val str_recovered_new: String? = null
    private var str_tests: String? = null

    private val activity = IndiaDataActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_each_country_data)
        GetIntent()

        supportActionBar!!.title = str_countryName

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        LoadCountryData()
    }

    private fun LoadCountryData() {

        activity.ShowDialog(this)
        val postDelayToshowProgress = Handler()
        postDelayToshowProgress.postDelayed(Runnable {
            activity_each_country_data_confirmed_textView.text = NumberFormat.getInstance().format(str_confirmed!!.toInt())
            activity_each_country_data_confirmed_new_textView!!.text =
                "+" + NumberFormat.getInstance().format(str_confirmed_new!!.toInt())
            activity_each_country_data_active_textView.text = NumberFormat.getInstance().format(str_active!!.toInt())
            activity_each_country_data_active_new_textView!!.text = "N/A"
            activity_each_country_data_death_textView.text = NumberFormat.getInstance().format(str_death!!.toInt())
            activity_each_country_data_death_new_textView!!.text = "+" + NumberFormat.getInstance().format(str_death_new!!.toInt())
            activity_each_country_data_recovered_textView.text = NumberFormat.getInstance().format(str_recovered!!.toInt())
            activity_each_country_data_recovered_new_textView!!.text = "N/A"
            activity_each_country_data_tests_textView.setText(NumberFormat.getInstance().format(str_tests!!.toInt()))


            activity_each_country_data_piechart.addPieSlice(
                PieModel(
                    "Active",
                    str_active!!.toInt().toFloat(),
                    Color.parseColor("#007afe")
                )
            )
            activity_each_country_data_piechart.addPieSlice(
                PieModel(
                    "Recovered",
                    str_recovered!!.toInt().toFloat(),
                    Color.parseColor("#08a045")
                )
            )
            activity_each_country_data_piechart.addPieSlice(
                PieModel(
                    "Deceased",
                    str_death!!.toInt().toFloat(),
                    Color.parseColor("#F6404F")
                )
            )
            activity_each_country_data_piechart.startAnimation()
            activity.DismissDialog()
        }, 1000)
    }

    private fun GetIntent() {
        val intent = intent
        str_countryName = intent.getStringExtra(COUNTRY_NAME)
        str_confirmed = intent.getStringExtra(COUNTRY_CONFIRMED)
        str_active = intent.getStringExtra(COUNTRY_ACTIVE)
        str_death = intent.getStringExtra(COUNTRY_DECEASED)
        str_recovered = intent.getStringExtra(COUNTRY_RECOVERED)
        str_confirmed_new = intent.getStringExtra(COUNTRY_NEW_CONFIRMED)
        str_death_new = intent.getStringExtra(COUNTRY_NEW_DECEASED)
        str_tests = intent.getStringExtra(COUNTRY_TESTS)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}