package com.example.td_android_studio


import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.td_android_studio.RandomUserGenerator.RequestResultRUG
import com.example.td_android_studio.RandomUserGenerator.UserAdapter
import com.example.td_android_studio.RandomUserGenerator.UserRUG
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_web_service.*


class WebServiceActivity : AppCompatActivity() {

    private val url = "https://randomuser.me/api/?results=20&nat=fr"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_service)

        val queue = Volley.newRequestQueue(this)

        val request = StringRequest(Request.Method.GET, url, Response.Listener {
                response ->

            val gson = Gson()
            val result= gson.fromJson(response, RequestResultRUG::class.java)

            result.results?.let{
                Log.d("volley", it[0].picture?.large)
                recyclerViewUserWS.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
                recyclerViewUserWS.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
                recyclerViewUserWS.adapter = UserAdapter(it) { userItem: UserRUG -> postItemClicked(userItem) }
            }

        }, Response.ErrorListener {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
        })

        queue.add(request)
    }


    private fun postItemClicked(userItem : UserRUG) {
        val alertDialogB : AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogB.setTitle("Informations").setMessage("Nom : " + userItem.name?.last + "\nPrenom : " + userItem.name?.first+ "\nSexe : " + userItem.gender + "\nPhone : " + userItem.phone)
        var alertDialog: AlertDialog = alertDialogB.create()
        alertDialog.show()
        Toast.makeText(this, "Clicked: ${userItem.name?.first} ${userItem.name?.last} ", Toast.LENGTH_LONG).show()
    }

}
