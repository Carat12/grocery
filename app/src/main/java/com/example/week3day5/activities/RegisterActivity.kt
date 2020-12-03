package com.example.week3day5.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.week3day5.R
import com.example.week3day5.app.Config
import com.example.week3day5.app.EndPoints
import com.example.week3day5.helpers.MenuManager
import com.example.week3day5.models.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.tool_bar.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
    }

    private fun init() {
        //tool bar
        tool_bar.title = Config.TITLE_REGISTER
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //register
        btn_register.setOnClickListener{
            val name = edit_text_name.text.toString()
            val email = edit_text_email.text.toString()
            val pw = edit_text_pw.text.toString()
            val mobile = edit_text_mobile.text.toString()

            val request = JsonObjectRequest(
                Request.Method.POST,
                EndPoints.getRegisterPostUrl(),
                User.convertToJsonObj(name, email, pw, mobile),
                {
                    //Toast.makeText(applicationContext,"registered successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                },
                {
                    val responseData = String(it.networkResponse.data)
                    val errorMsg = JSONObject(responseData).getString(Config.KEY_ERROR_MESSAGE)
                    Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_SHORT).show()
                })
            Volley.newRequestQueue(this).add(request)
        }
        //to login
        btn_register_to_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MenuManager.menuOptionSelected(this, item)
    }
}