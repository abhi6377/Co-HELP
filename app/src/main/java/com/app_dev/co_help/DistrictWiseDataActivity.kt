package com.app_dev.co_help

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.app_dev.co_help.Adapters.DistrictWiseAdapter
import com.app_dev.co_help.Adapters.StateWiseAdapter.STATE_NAME
import com.app_dev.co_help.Models.DistrictWiseModel
import org.json.JSONException


class DistrictWiseDataActivity : AppCompatActivity() {
    private var rv_district_wise: RecyclerView? = null
    private var districtWiseAdapter: DistrictWiseAdapter? = null
    private var districtWiseModelArrayList: ArrayList<DistrictWiseModel>? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var et_search: EditText? = null
    private var str_state_name: String? = null
    private var str_district: String? = null
    private var str_confirmed: String? = null
    private var str_confirmed_new: String? = null
    private var str_active: String? = null
    private val str_active_new: String? = null
    private var str_recovered: String? = null
    private var str_recovered_new: String? = null
    private var str_death: String? = null
    private var str_death_new: String? = null

    private val activity = IndiaDataActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_district_wise_data)
        GetIntent()
        supportActionBar!!.title = "Region/District"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        Init()

        FetchDistrictWiseData()

        swipeRefreshLayout!!.setOnRefreshListener {
            FetchDistrictWiseData()
            swipeRefreshLayout!!.isRefreshing = false
        }


        et_search!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                Filter(s.toString())
            }
        })
    }

    private fun Filter(s: String) {
        val filteredList: ArrayList<DistrictWiseModel> = ArrayList()
        for (item in districtWiseModelArrayList!!) {
            if (item.district.toLowerCase().contains(s.toLowerCase())) {
                filteredList.add(item)
            }
        }
        districtWiseAdapter!!.filterList(filteredList, s)
    }

    private fun FetchDistrictWiseData() {

        activity.ShowDialog(this)
        val requestQueue = Volley.newRequestQueue(this)
        val apiURL = "https://api.covid19india.org/v2/state_district_wise.json"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apiURL,
            null,
            { response ->
                try {
                    var flag = 0
                    districtWiseModelArrayList!!.clear()
                    for (i in 1 until response.length()) {
                        val jsonObjectState = response.getJSONObject(i)
                        if (str_state_name!!.toLowerCase() == jsonObjectState.getString("state")
                                .toLowerCase()
                        ) {
                            val jsonArrayDistrict = jsonObjectState.getJSONArray("districtData")
                            for (j in 0 until jsonArrayDistrict.length()) {
                                val jsonObjectDistrict = jsonArrayDistrict.getJSONObject(j)
                                str_district = jsonObjectDistrict.getString("district")
                                str_confirmed = jsonObjectDistrict.getString("confirmed")
                                str_active = jsonObjectDistrict.getString("active")
                                str_death = jsonObjectDistrict.getString("deceased")
                                str_recovered = jsonObjectDistrict.getString("recovered")
                                val jsonObjectDistNew =
                                    jsonObjectDistrict.getJSONObject("delta")
                                str_confirmed_new = jsonObjectDistNew.getString("confirmed")
                                str_recovered_new = jsonObjectDistNew.getString("recovered")
                                str_death_new = jsonObjectDistNew.getString("deceased")


                                val districtWiseModel = DistrictWiseModel(
                                    str_district,
                                    str_confirmed,
                                    str_active,
                                    str_recovered,
                                    str_death,
                                    str_confirmed_new,
                                    str_recovered_new,
                                    str_death_new
                                )

                                districtWiseModelArrayList!!.add(districtWiseModel)
                            }
                            flag = 1
                        }
                        if (flag == 1) break
                    }
                    val makeDelay = Handler()
                    makeDelay.postDelayed({
                        districtWiseAdapter!!.notifyDataSetChanged()
                        activity.DismissDialog()
                    }, 1000)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error -> error.printStackTrace() })
        requestQueue.add(jsonArrayRequest)
    }

    private fun Init() {
        rv_district_wise = findViewById(R.id.activity_district_wise_recyclerview)
        swipeRefreshLayout = findViewById(R.id.activity_district_wise_swipe_refresh_layout)
        et_search = findViewById(R.id.activity_district_wise_search_editText)
        rv_district_wise?.setHasFixedSize(true)
        rv_district_wise?.setLayoutManager(LinearLayoutManager(this))
        districtWiseModelArrayList = ArrayList()
        districtWiseAdapter =
            DistrictWiseAdapter(this@DistrictWiseDataActivity, districtWiseModelArrayList)
        rv_district_wise?.setAdapter(districtWiseAdapter)
    }

    private fun GetIntent() {
        val intent = intent
        str_state_name = intent.getStringExtra(STATE_NAME)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}