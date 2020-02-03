package com.example.td_android_studio

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_save.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class SaveActivity : AppCompatActivity() {

    var date_now = Date()
    val kprenom = "prenom"
    val knom = "nom"
    val kanniv = "anniversaire"

    fun save () {
        var firstname = firstname.text.toString()
        var lastname = lastname.text.toString()
        var birthdate = birthdate.text.toString()
        if (firstname != "" && lastname != "" && birthdate != "") {
            var json = JSONObject()
            json.put(kprenom, firstname)
            json.put(knom, lastname)
            json.put(kanniv, birthdate)
            val file = File(cacheDir.absolutePath+"/ADDED.json")
            file.writeText(json.toString())
            Toast.makeText(this, "Sauvegarde ok", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, R.string.emptystr, Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)

        saveBTN.setOnClickListener{save()}
        readBTN.setOnClickListener{read()}
        birthdate.setOnFocusChangeListener {
            view,
            hasFocus ->

            if (hasFocus) {
                birthdate.clearFocus()
                var picker = DatePickerDialog (this,
                    DatePickerDialog.OnDateSetListener {
                        view, y, m, d ->
                        onDateChoose(y,m,d)
                    }, 1950, 1, 1)
                picker.show()
            }
        }
    }

    fun onDateChoose(annee: Int, mois: Int, jour: Int) {
        birthdate.setText(String.format("%02d/%02d/%04d", jour, mois, annee))
    }

    fun read() {
        val file = File(cacheDir.absolutePath+"/ADDED.json")
        val readString = file.readText()
        val json = JSONObject(readString)
        val dateString = json.getString(kanniv)
        val splitted = dateString.split("/")
        var age = getAge(splitted[2].toInt(), splitted[1].toInt(), splitted[0].toInt())
        Toast.makeText(this,"vous avez ${age} ans", Toast.LENGTH_LONG).show()
    }

    fun getAge(annee : Int, mois : Int, jour : Int) : Int {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val dateString = formatter.format(date_now)
        val splitted = dateString.split("/")


        var age = splitted[2].toInt() - annee
        if(splitted[1].toInt() < mois)
            -- age
        else if (splitted[1].toInt() == mois && splitted[0].toInt() < jour)
            -- age

        return age
    }
}
