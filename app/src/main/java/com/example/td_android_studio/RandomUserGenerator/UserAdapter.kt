package com.example.td_android_studio.RandomUserGenerator

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.td_android_studio.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_view_contact_cell_ws.view.*

class UserAdapter (val users : ArrayList<UserRUG>, val clickListener: (UserRUG) -> Unit)
    : RecyclerView.Adapter<UserAdapter.UserRUGViewHolder> ()
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserRUGViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_contact_cell_ws, parent, false)
        return UserRUGViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.count()
    }

    override fun onBindViewHolder(holder: UserRUGViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user,clickListener)
    }

    class UserRUGViewHolder (val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(user: UserRUG, clickListener: (UserRUG) -> Unit){
            view.NameInList.text = "${user.name?.first} ${user.name?.last} (${user.name?.title})"
            view.AddressInList.text = user.location.toString()
            view.PhoneInList.text = user.phone
            Picasso.get().load("${user.picture?.large}").into(view.AvatarInList)
            view.setOnClickListener { clickListener(user) }
        }
    }

}