package com.app_dev.co_help

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_world.*
import org.eazegraph.lib.models.PieModel
import org.json.JSONException
import java.text.NumberFormat


class WorldActivity : AppCompatActivity() {

    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_world)

        supportActionBar!!.title = "World Covid Statistics"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


        FetchWorldData()

        activity_world_data_swipe_refresh_layout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            FetchWorldData()
            activity_world_data_swipe_refresh_layout.setRefreshing(false)
        })

        activity_world_data_countrywise_lin.setOnClickListener {
            val intent = Intent(this@WorldActivity , CountryWiseDataActivity::class.java)
            startActivity(intent)
        }
    }

    private fun FetchWorldData() {


        ShowDialog(this)

        val requestQueue = Volley.newRequestQueue(this)
        val apiUrl = "https://corona.lmao.ninja/v2/all"

        activity_world_data_piechart.clearChart()

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            apiUrl,
            null,
            { response ->

                try {
                    var str_confirmed = response.getString("cases")
                    var str_confirmed_new = response.getString("todayCases")
                    var str_active = response.getString("active")
                    var str_recovered = response.getString("recovered")
                    var str_recovered_new = response.getString("todayRecovered")
                    var str_death = response.getString("deaths")
                    var str_death_new = response.getString("todayDeaths")
                    var str_tests = response.getString("tests")

                    val delayToshowProgress = Handler()

                    delayToshowProgress.postDelayed(Runnable {
                        activity_world_data_confirmed_textView.text = NumberFormat.getInstance().format(str_confirmed.toInt())
                        activity_world_data_confirmed_new_textView.text = "+" + NumberFormat.getInstance().format(str_confirmed_new.toInt())
                        activity_world_data_active_textView.text = NumberFormat.getInstance().format(str_active.toInt())
                        var int_active_new = str_confirmed_new.toInt() - (str_recovered_new.toInt() + str_death_new.toInt())
                        activity_world_data_active_new_textView.text = "+" + NumberFormat.getInstance().format(int_active_new)
                        activity_world_data_recovered_textView.text = NumberFormat.getInstance().format(str_recovered.toInt())
                        activity_world_data_recovered_new_textView.text = "+" + NumberFormat.getInstance().format(str_recovered_new.toInt())
                        activity_world_data_death_textView.text = NumberFormat.getInstance().format(str_death.toInt())
                        activity_world_data_death_new_textView.text = "+" + NumberFormat.getInstance().format(str_death_new.toInt())
                        activity_world_data_tests_textView.text = NumberFormat.getInstance().format(str_tests.toLong())

                        activity_world_data_piechart.addPieSlice(
                            PieModel(
                                "Active",
                                str_active.toInt().toFloat(),
                                Color.parseColor("#007afe")
                            )
                        )
                        activity_world_data_piechart.addPieSlice(
                            PieModel(
                                "Recovered",
                                str_recovered.toInt().toFloat(),
                                Color.parseColor("#08a045")
                            )
                        )
                        activity_world_data_piechart.addPieSlice(
                            PieModel(
                                "Deceased",
                                str_death.toInt().toFloat(),
                                Color.parseColor("#F6404F")
                            )
                        )
                        activity_world_data_piechart.startAnimation()
                        DismissDialog()
                    }, 1000)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { error -> error.printStackTrace() }
        requestQueue.add(jsonObjectRequest)
    }

    fun ShowDialog(context: Context?) {

        progressDialog = ProgressDialog(context)
        progressDialog!!.show()
        progressDialog!!.setContentView(R.layout.progress_dialog)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.window!!.setBackgroundDrawableResource(R.color.tranparent)
    }

    fun DismissDialog() {
        progressDialog!!.dismiss()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}