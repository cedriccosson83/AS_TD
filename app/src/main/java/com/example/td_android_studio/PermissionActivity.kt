package com.example.td_android_studio

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
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
import java.io.BufferedInputStream

class PermissionActivity : AppCompatActivity(), LocationListener {

    // code requests
    private val codePicture = 1
    private val codeContact = 2
    private val codeLocation = 3

    //shortcut
    private val granted = PackageManager.PERMISSION_GRANTED
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        reloadBTN.setOnClickListener{onChangePhoto()} // picture change
        changeImgText.setOnClickListener{onChangePhoto()} // picture change
        requestPermission(android.Manifest.permission.READ_CONTACTS, codeContact) {readContacts()}

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        requestPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION, codeLocation) { getGPS() }
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
        while (contacts?.moveToNext() == true) {

            val contact = ContactModel()

            /* NAME AND ID */
            val name = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            contact.name = "$name"
            val id = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts._ID))


            /* PHONE NUMBER */
            val phoneNumber = (contacts.getString(
                contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()
            if (phoneNumber > 0) {
                val cursorPhone = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                if(cursorPhone != null && cursorPhone.count > 0) {
                    while (cursorPhone.moveToNext()) {
                        val phoneNumValue = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        contact.tel = "Tel : $phoneNumValue"
                    }
                }
                cursorPhone?.close()
            }

            /* MAIL ADDRESS */
            var mail = ""
            val mailCursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                arrayOf<String>(id),
                null
            )
            while(mailCursor != null && mailCursor.moveToNext()) {
                mail += mailCursor.getString(
                    mailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)
                ) + "\n"
            }

            mailCursor?.close()
            contact.mail = "Email : $mail"

            /* CONTACT THUMBNAIL */

            val contactPhotoUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id)
            val photoStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, contactPhotoUri)
            val buffer = BufferedInputStream(photoStream)
            val contactPhoto = BitmapFactory.decodeStream(buffer)

            contact.picture = contactPhoto

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


    /* LOCATION INTERFACE MEMBERS */

    @SuppressLint("MissingPermission")
    private fun getGPS() {
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null)
        val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        location?.let {
            refreshPositionUI(it)
        }
    }

    fun refreshPositionUI(location: Location){
        userLat.text = "Latitude: ${location.latitude}"
        userLon.text = "Longitude: ${location.longitude}"
    }

    override fun onLocationChanged(location: Location?) {
        location?.let{
            refreshPositionUI(it)
        }
    }

    /* To Dismiss unimplement error */
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String?) {}
    override fun onProviderDisabled(provider: String?) {}
}
