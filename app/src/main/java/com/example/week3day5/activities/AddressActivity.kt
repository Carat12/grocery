package com.example.week3day5.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.week3day5.R
import com.example.week3day5.adapters.AdapterAddress
import com.example.week3day5.app.Config
import com.example.week3day5.app.EndPoints
import com.example.week3day5.helpers.MenuManager
import com.example.week3day5.helpers.SessionManager
import com.example.week3day5.models.Address
import com.example.week3day5.models.ResponseAddress
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.item_address.view.*
import kotlinx.android.synthetic.main.tool_bar.*
import org.json.JSONObject

class AddressActivity : AppCompatActivity(), AdapterAddress.onAdapterListener {

    lateinit var adapter: AdapterAddress
    private var selectedAddress: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        init()
    }

    override fun onStart() {
        super.onStart()
        updateAddressData()
    }

    private fun init() {
        //tool bar
        tool_bar.title = Config.TITLE_ADDRESS
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //recycler view
        adapter = AdapterAddress(this)
        adapter.setOnAdapterListener(this)
        recycler_view_address.adapter = adapter
        recycler_view_address.layoutManager = LinearLayoutManager(this)

        updateAddressData()

        //add new address
        card_view_add_addresss.setOnClickListener {
            startActivity(Intent(this, AddAddressActivity::class.java))
        }

        //continue to place order
        controlContinueButton()
        btn_address_continue.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra(Address.KEY_ADDRESS, selectedAddress)
            startActivity(intent)
        }
    }

    private fun updateAddressData() {
        val request = StringRequest(Request.Method.GET,
            EndPoints.getAddressGetUrl(SessionManager.userLoggedIn._id), //sm.getUser().id  //"5e78ecd520203d0017002106"
            {
                val response = Gson().fromJson(it, ResponseAddress::class.java)
                adapter.setData(response.data)
            },
            {
                val response = String(it.networkResponse.data)
                Toast.makeText(
                    applicationContext,
                    JSONObject(response).getString(Config.KEY_ERROR_MESSAGE),
                    Toast.LENGTH_SHORT
                ).show()
            })
        Volley.newRequestQueue(this).add(request)
    }

    fun controlContinueButton() {
        if (selectedAddress == null) {
            btn_address_continue.alpha = 0.5F
            btn_address_continue.isEnabled = false
        } else {
            btn_address_continue.alpha = 1F
            btn_address_continue.isEnabled = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MenuManager.menuOptionSelected(this, item)
    }

    override fun onItemSelected(view: View, position: Int) {
        selectedAddress = adapter.getItemData(position)
        view.material_card_address.strokeColor = ContextCompat.getColor(this, R.color.tab_green)
        val lastSelectedPosition = adapter.lastSelectedPosition
        if (lastSelectedPosition != position) {
            adapter.lastSelectedPosition = position
            adapter.notifyItemChanged(lastSelectedPosition)
        }
        controlContinueButton()
    }
}