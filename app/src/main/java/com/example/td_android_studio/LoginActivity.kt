package com.example.td_android_studio

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    var id = "admin"
    var pass = "admin"
    var userpref : SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userpref = getSharedPreferences(Constants.userpref, Context.MODE_PRIVATE)
        checkPreferences()

        validBTN.setOnClickListener {
            doLogin()
        }
    }

    fun check(id: String, pass: String): Boolean {
        return id == this.id && pass == this.pass
    }

    fun doLogin() {
        var id = idBOX.text.toString()
        var pass = passBOX.text.toString()

        if (check(id, pass)) {
            val toast = Toast.makeText(applicationContext, "Bonjour !", Toast.LENGTH_SHORT)
            toast.show()
            saveUser(idBOX.text.toString(), passBOX.text.toString())
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    fun saveUser(identifier: String, password: String) {
        val editor = userpref?.edit()
        editor?.putString(Constants.kid, identifier)
        editor?.putString(Constants.kpass, password)
        editor?.apply()
    }

    fun checkPreferences() {
        val identifier = userpref?.getString(Constants.kid, null) ?: ""
        val password = userpref?.getString(Constants.kpass, null) ?: ""
        idBOX.setText(identifier)
        passBOX.setText(password)
        doLogin()
    }
}
