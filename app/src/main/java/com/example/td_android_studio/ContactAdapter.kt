package com.example.td_android_studio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_view_contact_cell.view.*
import java.util.ArrayList

class ContactAdapter (val contacts: ArrayList<ContactModel>): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_contact_cell, parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
    }

    //classe interne
    class ContactViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(contact: ContactModel) {
            view.NameInList.text = contact.name
            view.AddressInList.text = contact.tel
            view.PhoneInList.text = contact.mail
            if (contact.picture != null)
                view.AvatarInList.setImageBitmap(contact.picture)
            else
                view.AvatarInList.setImageResource(R.drawable.contact)
        }
    }

}