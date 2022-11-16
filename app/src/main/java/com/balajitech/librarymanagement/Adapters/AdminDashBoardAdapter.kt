package com.balajitech.librarymanagement.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.balajitech.librarymanagement.Model.Model
import com.balajitech.librarymanagement.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class

AdminDashBoardAdapter(options: FirebaseRecyclerOptions<Model?>) :
    FirebaseRecyclerAdapter<Model, AdminDashBoardAdapter.Viewholder>(options) {
    override fun onBindViewHolder(holder: Viewholder, position: Int, model: Model) {


        //Setting data to android materials
        holder.bookName.text = "Book Name: " + model.bookName
        holder.booksCount.text = "Available Books: " + model.booksCount
        holder.bookLocation.text = "Book Location: " + model.bookLocation
        holder.userNameTxt.text = model.name
        holder.userPhoneNumberTxt.text = model.phoneNumber
        holder.userAddressTxt.text = model.address
        Picasso.get().load(model.imageUrl).into(holder.imageView)

        //Implementing the OnClick Listener to delete the data from the database
        holder.cancleBtn.setOnClickListener { view ->
            //Getting user id from the gmail sing in
            val userId = model.userId
            //Path to the database
            val reference = FirebaseDatabase.getInstance().reference.child("OrderedBooks")
            reference.orderByChild("bookName").equalTo(model.bookName)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ds in snapshot.children) {

                            //getting the parent node of the data
                            val key = ds.key

                            //removing the data from the database
                            reference.child(key!!).removeValue().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    FirebaseDatabase.getInstance().reference.child("myOrderedBooks")
                                        .child(
                                            userId!!
                                        )
                                        .child(key).removeValue()
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {

                                                //Generating Unique Key
                                                val pushKey =
                                                    FirebaseDatabase.getInstance().reference.child("UserNotifications")
                                                        .push().key

                                                //Adding Notification To Database
                                                FirebaseDatabase.getInstance().reference.child("UserNotifications")
                                                    .child(
                                                        userId
                                                    ).child(pushKey!!)
                                                    .child("notification")
                                                    .setValue("Your Request for Book " + model.bookName + " is Accepted By Admin")
                                                    .addOnSuccessListener { //Showing the Toast message to the user
                                                        Toast.makeText(
                                                            view.context,
                                                            "Book Order Canceled Successfully",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                            }
                                        }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

        //implementing onClickListener
        holder.acceptBtn.setOnClickListener { view ->
            //Generating Unique key
            val pushKey =
                FirebaseDatabase.getInstance().reference.child("UserNotifications").push().key

            //Adding Notification to database
            FirebaseDatabase.getInstance().reference.child("UserNotifications")
                .child(model.userId!!)
                .child(pushKey!!)
                .child("notification")
                .setValue("Your Request for Book " + model.bookName + " is Accepted By Admin")
                .addOnSuccessListener { //Showing toast Message
                    Toast.makeText(view.context, "Book Accepted", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        //the data objects are inflated into the xml file single_data_item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_dashboard_layout_admin, parent, false)
        return Viewholder(view)
    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var bookName: TextView
        var booksCount: TextView
        var bookLocation: TextView
        var acceptBtn: Button
        var cancleBtn: Button
        var userNameTxt: TextView
        var userAddressTxt: TextView
        var userPhoneNumberTxt: TextView

        init {


            //Assigning Address of the android materials
            imageView = itemView.findViewById<View>(R.id.BookImage) as ImageView
            bookName = itemView.findViewById<View>(R.id.BookNameTxt) as TextView
            booksCount = itemView.findViewById<View>(R.id.BooksCountTxt) as TextView
            bookLocation = itemView.findViewById<View>(R.id.BooksLocationTxt) as TextView
            userNameTxt = itemView.findViewById<View>(R.id.UserNameTxt) as TextView
            userAddressTxt = itemView.findViewById<View>(R.id.UserAddressTxt) as TextView
            userPhoneNumberTxt = itemView.findViewById<View>(R.id.UserPhoneNumberTxt) as TextView
            acceptBtn = itemView.findViewById<View>(R.id.AcceptBookBtn) as Button
            cancleBtn = itemView.findViewById<View>(R.id.CancleBookBtn) as Button
        }
    }
}