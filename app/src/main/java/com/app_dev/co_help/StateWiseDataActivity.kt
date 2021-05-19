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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app_dev.co_help.Adapters.StateWiseAdapter
import com.app_dev.co_help.Models.StateWiseModel
import kotlinx.android.synthetic.main.activity_state_wise_data.*
import org.json.JSONException

class StateWiseDataActivity : AppCompatActivity() {
    private var rv_state_wise: RecyclerView? = null
    private var stateWiseAdapter: StateWiseAdapter? = null
    private var stateWiseModelArrayList: ArrayList<StateWiseModel>? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var et_search: EditText? = null

    private var str_state: String? = null
    private  var str_confirmed:String? = null
    private  var str_confirmed_new:String? = null
    private  var str_active:String? = null
    private  var str_active_new:String? = null
    private  var str_recovered:String? = null
    private  var str_recovered_new:String? = null
    private var str_death: String? = null
    private  var str_death_new:String? = null
    private  var str_lastupdatedate:String? = null

    private val activity = IndiaDataActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_wise_data)


        supportActionBar!!.title = "Select State"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


        Init()

        FetchStateWiseData()

        activity_state_wise_swipe_refresh_layout.setOnRefreshListener {
            FetchStateWiseData()
            activity_state_wise_swipe_refresh_layout.isRefreshing = false
        }

        activity_state_wise_search_editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                Filter(s.toString())
            }
        })
    }

    private fun Filter(text: String) {
        val filteredList: ArrayList<StateWiseModel> = ArrayList()
        for (item in stateWiseModelArrayList!!) {
            if (item.state.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        stateWiseAdapter!!.filterList(filteredList, text)
    }

    private fun FetchStateWiseData() {

        activity.ShowDialog(this)
        val requestQueue = Volley.newRequestQueue(this)
        val apiURL = "https://api.covid19india.org/data.json"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            apiURL,
            null,
            { response ->
                try {
                    val jsonArray = response.getJSONArray("statewise")
                    stateWiseModelArrayList!!.clear()
                    for (i in 1 until jsonArray.length()) {
                        val statewise = jsonArray.getJSONObject(i)

                        str_state = statewise.getString("state")
                        str_confirmed = statewise.getString("confirmed")
                        str_confirmed_new = statewise.getString("deltaconfirmed")
                        str_active = statewise.getString("active")
                        str_death = statewise.getString("deaths")
                        str_death_new = statewise.getString("deltadeaths")
                        str_recovered = statewise.getString("recovered")
                        str_recovered_new = statewise.getString("deltarecovered")
                        str_lastupdatedate = statewise.getString("lastupdatedtime")


                        val stateWiseModel = StateWiseModel(
                            str_state,
                            str_confirmed,
                            str_confirmed_new,
                            str_active,
                            str_death,
                            str_death_new,
                            str_recovered,
                            str_recovered_new,
                            str_lastupdatedate
                        )

                        stateWiseModelArrayList!!.add(stateWiseModel)
                    }
                    val makeDelay = Handler()
                    makeDelay.postDelayed(Runnable {
                        stateWiseAdapter!!.notifyDataSetChanged()
                        activity.DismissDialog()
                    }, 1000)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error -> error.printStackTrace() })
        requestQueue.add(jsonObjectRequest)
    }

    private fun Init() {

        rv_state_wise = findViewById(R.id.activity_state_wise_recyclerview)
        rv_state_wise!!.setHasFixedSize(true)
        rv_state_wise!!.setLayoutManager(LinearLayoutManager(this))
        stateWiseModelArrayList = ArrayList()
        stateWiseAdapter = StateWiseAdapter(this@StateWiseDataActivity, stateWiseModelArrayList)
        rv_state_wise!!.setAdapter(stateWiseAdapter)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}