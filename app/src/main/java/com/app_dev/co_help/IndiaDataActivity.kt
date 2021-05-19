package com.app_dev.co_help

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_india_data.*
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import org.json.JSONArray
import org.json.JSONException
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class IndiaDataActivity : AppCompatActivity() {

        private var progressDialog: ProgressDialog? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_india_data)
            FetchData()

            supportActionBar!!.title = "India Covid Statistics "
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)

            actMain_swipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
                FetchData()
                actMain_swipeRefresh.setRefreshing(false)
            })

            activity_main_world_data_lin.setOnClickListener{
                val intent = Intent(this,WorldActivity::class.java)
                startActivity(intent)
            }

            activity_main_statewise_lin.setOnClickListener{
                val intent = Intent(this, StateWiseDataActivity::class.java)
                startActivity(intent)
            }
        }

        private fun FetchData(){
            val requestQueue = Volley.newRequestQueue(this)
            val apiUrl = "https://api.covid19india.org/data.json"

            ShowDialog(this)
            activity_main_piechart.clearChart()

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                { response ->
                    var all_state_jsonArray: JSONArray? = null
                    var testData_jsonArray: JSONArray? = null
                    try {
                        all_state_jsonArray = response.getJSONArray("statewise")
                        testData_jsonArray = response.getJSONArray("tested")
                        val data_india = all_state_jsonArray.getJSONObject(0)
                        val test_data_india =
                            testData_jsonArray.getJSONObject(testData_jsonArray.length() - 1)

                        var str_confirmed = data_india.getString("confirmed")
                        var str_confirmed_new = data_india.getString("deltaconfirmed")
                        var str_active = data_india.getString("active")
                        var str_recovered = data_india.getString("recovered")
                        var str_recovered_new = data_india.getString("deltarecovered")
                        var str_death = data_india.getString("deaths")
                        var str_death_new = data_india.getString("deltadeaths")
                        var str_last_update_time = data_india.getString("lastupdatedtime")
                        var str_tests = test_data_india.getString("totalsamplestested")
                        var str_tests_new = test_data_india.getString("samplereportedtoday")

                        val delayToshowProgess = Handler()
                        delayToshowProgess.postDelayed({

                            activity_main_confirmed_textview.text = NumberFormat.getInstance().format(str_confirmed.toInt().toLong())
                            activity_main_confirmed_new_textview.text = "+" + NumberFormat.getInstance().format(str_confirmed_new.toInt().toLong())
                            activity_main_active_textview.text = NumberFormat.getInstance().format(str_active.toInt().toLong())

                            var int_active_new = str_confirmed_new.toInt() - (str_recovered_new.toInt() + str_death_new.toInt())

                            activity_main_active_new_textview.text =
                                "+" + NumberFormat.getInstance().format(int_active_new)
                            activity_main_recovered_textview.text = NumberFormat.getInstance().format(str_recovered.toInt().toLong())
                            activity_main_recovered_new_textview.text = "+" + NumberFormat.getInstance().format(str_recovered_new.toInt().toLong())
                            activity_main_death_textview.text = NumberFormat.getInstance().format(str_death.toInt().toLong())
                            activity_main_death_new_textview.text = "+" + NumberFormat.getInstance().format(str_death_new.toInt().toLong())
                            activity_main_samples_textview.text = NumberFormat.getInstance().format(str_tests.toInt().toLong())
                            activity_main_samples_new_textview.text = "+" + NumberFormat.getInstance().format(str_tests_new.toInt().toLong())

                            activity_main_time_textview.text = FormatDate(str_last_update_time, 1)
                            activity_main_date_textview.text = FormatDate(str_last_update_time, 2)

                            activity_main_piechart.addPieSlice(
                                PieModel(
                                    "Active",
                                    str_active.toInt().toFloat(),
                                    Color.parseColor("#007afe")
                                )
                            )
                            activity_main_piechart.addPieSlice(
                                PieModel(
                                    "Recovered",
                                    str_recovered.toInt().toFloat(),
                                    Color.parseColor("#08a045")
                                )
                            )
                            activity_main_piechart.addPieSlice(
                                PieModel(
                                    "Deceased",
                                    str_death.toInt().toFloat(),
                                    Color.parseColor("#F6404F")
                                )
                            )
                            activity_main_piechart.startAnimation()

                            DismissDialog()

                        }, 1000)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            ) { error -> error.printStackTrace() }
            requestQueue.add(jsonObjectRequest)
        }

        fun FormatDate(date: String?, testCase: Int): String? {
            var mDate: Date? = null
            val dateFormat: String
            return try {
                mDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).parse(date)
                if (testCase == 0) {
                    dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a").format(mDate)
                    dateFormat
                } else if (testCase == 1) {
                    dateFormat = SimpleDateFormat("dd MMM yyyy").format(mDate)
                    dateFormat
                } else if (testCase == 2) {
                    dateFormat = SimpleDateFormat("hh:mm a").format(mDate)
                    dateFormat
                } else {
                    Log.d("error", "Wrong input! Choose from 0 to 2")
                    "Error"
                }
            } catch (e: ParseException) {
                e.printStackTrace()
                date
            }
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
            if (item.itemId == android.R.id.home) finish()
            return super.onOptionsItemSelected(item)
        }

    }
