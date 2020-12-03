package com.example.week3day5.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.week3day5.R
import com.example.week3day5.app.Config
import com.example.week3day5.app.EndPoints
import com.example.week3day5.helpers.MenuManager
import com.example.week3day5.helpers.SessionManager
import com.example.week3day5.models.Address
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.tool_bar.*
import org.json.JSONObject

class AddAddressActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        init()
    }

    private fun init() {
        //tool bar
        tool_bar.title = Config.TITLE_ADD_ADDRESS
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Address.TYPE_LIST)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_type.adapter = adapter
        spinner_type.onItemSelectedListener = this


        btn_save_address.setOnClickListener {
            val street = edit_text_street.text.toString()
            val houseNo = edit_text_houseNo.text.toString()
            val city = edit_text_city.text.toString()
            val pincode = edit_text_pincode.text.toString().toInt()

            val userId = SessionManager.userLoggedIn._id
            val jsonObj = Address.convertToJsonObj(userId, type, street, houseNo, city, pincode)
            postAddress(jsonObj)
        }
    }

    private fun postAddress(jsonObj: JSONObject){
        val request = JsonObjectRequest(Request.Method.POST,
            EndPoints.getAddressPostUrl(),
            jsonObj,
            {
                Toast.makeText(applicationContext, it.getString("message"), Toast.LENGTH_SHORT).show()
                //startActivity(Intent(this, AddressActivity::class.java))
                finish()
            },
            {
                val response = String(it.networkResponse.data)
                Toast.makeText(applicationContext, JSONObject(response).getString(Config.KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show()
            })
        Volley.newRequestQueue(this).add(request)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MenuManager.menuOptionSelected(this, item)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        type = Address.TYPE_LIST[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}