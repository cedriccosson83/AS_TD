package com.example.td_android_studio

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_permission.*

class PermissionActivity : AppCompatActivity() {

    // code requests
    private val codePicture = 1
    private val codeContact = 2

    //shortcut
    private val granted = PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        reloadBTN.setOnClickListener{onChangePhoto()} // picture change
        requestPermission(android.Manifest.permission.READ_CONTACTS, codeContact) {readContacts()}
    }

    private fun onChangePhoto () {
        val gallIntent = Intent (Intent.ACTION_PICK)
        gallIntent.type = "image/*"
        val camIntent = Intent (MediaStore.ACTION_IMAGE_CAPTURE)
        val chooserIntent = Intent.createChooser(gallIntent, "Selectionnez une image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(camIntent))
        startActivityForResult(chooserIntent, codePicture)
    }

    private fun requestPermission(perm : String, requestCode: Int, handler: ()->Unit) {
        if (ContextCompat.checkSelfPermission(this, perm) != granted) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, perm))
                Toast.makeText(this, "Permission OK", Toast.LENGTH_LONG).show()
            else
                ActivityCompat.requestPermissions(this, arrayOf(perm), requestCode)
        } else {
            handler()
        }
    }

    private fun readContacts () {
        val listeContacts = ArrayList<ContactModel>()
        val contacts = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        Log.d("contacts", "${contacts}")
        while (contacts?.moveToNext() ?: false) {
            val name = contacts?.getString(contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            val contact = ContactModel()
            contact.name = "Nom : " + name.toString()
            listeContacts.add(contact)
        }
        contactsContainer.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        contactsContainer.adapter = ContactAdapter(listeContacts)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty()) {
            if (requestCode == codeContact && grantResults[0] == granted)
                readContacts()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == codePicture && resultCode == Activity.RESULT_OK) {
            if (data?.data != null) {
                reloadBTN.setImageURI(data.data)
            } else {
                val img = data?.extras?.get("data") as? Bitmap
                img?.let {reloadBTN.setImageBitmap(img)}
            }
        }
    }
}
