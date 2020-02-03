package com.example.td_android_studio

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        cycleBTN.setOnClickListener { startActivity(Intent(this, LifeCycleActivity::class.java)) }
        saveBTN.setOnClickListener { startActivity(Intent(this, SaveActivity::class.java)) }
        permissionBTN.setOnClickListener { startActivity(Intent(this, PermissionActivity::class.java)) }
        WSBTN.setOnClickListener { startActivity(Intent(this, WebServiceActivity::class.java)) }

        LOGOUT.setOnClickListener {
            val userPref = getSharedPreferences(Constants.userpref, Context.MODE_PRIVATE)
            val editor = userPref.edit()
            editor.clear()
            editor.apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}
