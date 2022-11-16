package com.balajitech.librarymanagement.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.balajitech.librarymanagement.Model.Model
import com.balajitech.librarymanagement.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

private class UserNotificationsAdapter(options: FirebaseRecyclerOptions<Model?>) :
    FirebaseRecyclerAdapter<Model, UserNotificationsAdapter.Viewholder>(options) {
    override fun onBindViewHolder(holder: Viewholder, position: Int, model: Model) {
        holder.notificationTxt.text = model.notification
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        //the data objects are inflated into the xml file single_data_item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_notifications_layout, parent, false)
        return Viewholder(view)
    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    internal inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var notificationTxt: TextView

        init {
            notificationTxt = itemView.findViewById<View>(R.id.NotificationTxt) as TextView
        }
    }
}