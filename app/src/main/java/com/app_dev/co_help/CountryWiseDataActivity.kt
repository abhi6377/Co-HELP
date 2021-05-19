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
import com.app_dev.co_help.Adapters.CountryWiseAdapter
import com.app_dev.co_help.Models.CountryWiseModel
import kotlinx.android.synthetic.main.activity_country_wise_data.*
import org.json.JSONException

class CountryWiseDataActivity : AppCompatActivity() {
    private var rv_country_wise: RecyclerView? = null
    private var countryWiseAdapter: CountryWiseAdapter? = null
    private var countryWiseModelArrayList: ArrayList<CountryWiseModel>? = null
    private var str_country: String? = null
    private var str_confirmed: String? = null
    private var str_confirmed_new: String? = null
    private var str_active: String? = null
    private val str_active_new: String? = null
    private var str_recovered: String? = null
    private val str_recovered_new: String? = null
    private var str_death: String? = null
    private var str_death_new: String? = null
    private var str_tests: String? = null

    private val activity = IndiaDataActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_wise_data)

        supportActionBar!!.title = "World Data (Select Country)"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        Init()

        FetchCountryWiseData()

        activity_country_wise_swipe_refresh_layout.setOnRefreshListener {
            FetchCountryWiseData()
            activity_country_wise_swipe_refresh_layout.isRefreshing = false
        }

        activity_country_wise_search_editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                Filter(s.toString())
            }
        })
    }

    private fun Filter(text: String) {
        val filteredList: ArrayList<CountryWiseModel> = ArrayList()
        for (item in countryWiseModelArrayList!!) {
            if (item.country.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        countryWiseAdapter!!.filterList(filteredList, text)
    }

    private fun FetchCountryWiseData() {

        activity.ShowDialog(this)
        val requestQueue = Volley.newRequestQueue(this)
        val apiURL = "https://corona.lmao.ninja/v2/countries"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            apiURL,
            null,
            { response ->
                try {
                    countryWiseModelArrayList!!.clear()
                    for (i in 0 until response.length()) {
                        val countryJSONObject = response.getJSONObject(i)
                        str_country = countryJSONObject.getString("country")
                        str_confirmed = countryJSONObject.getString("cases")
                        str_confirmed_new = countryJSONObject.getString("todayCases")
                        str_active = countryJSONObject.getString("active")
                        str_recovered = countryJSONObject.getString("recovered")
                        str_death = countryJSONObject.getString("deaths")
                        str_death_new = countryJSONObject.getString("todayDeaths")
                        str_tests = countryJSONObject.getString("tests")
                        val flagObject = countryJSONObject.getJSONObject("countryInfo")
                        val flagUrl = flagObject.getString("flag")


                        val countryWiseModel = CountryWiseModel(
                            str_country, str_confirmed, str_confirmed_new, str_active,
                            str_death, str_death_new, str_recovered, str_tests, flagUrl
                        )

                        countryWiseModelArrayList!!.add(countryWiseModel)
                    }
                    countryWiseModelArrayList!!.sortWith(Comparator { o1, o2 ->
                        if (o1.confirmed.toInt() > o2.confirmed.toInt()) {
                            -1
                        } else {
                            1
                        }
                    })
                    val makeDelay = Handler()
                    makeDelay.postDelayed({
                        countryWiseAdapter!!.notifyDataSetChanged()
                        activity.DismissDialog()
                    }, 1000)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { }
        requestQueue.add(jsonArrayRequest)
    }

    private fun Init() {

        rv_country_wise = findViewById(R.id.activity_country_wise_recyclerview)
        rv_country_wise!!.setHasFixedSize(true)
        rv_country_wise!!.setLayoutManager(LinearLayoutManager(this))
        countryWiseModelArrayList = ArrayList()
        countryWiseAdapter =
            CountryWiseAdapter(this@CountryWiseDataActivity, countryWiseModelArrayList)
        rv_country_wise!!.setAdapter(countryWiseAdapter)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}