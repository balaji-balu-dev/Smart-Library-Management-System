package com.balajitech.librarymanagement.Adapters

import android.annotation.SuppressLint
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.HashMap


class AllBooksAdapter(options: FirebaseRecyclerOptions<Model?>) :
    FirebaseRecyclerAdapter<Model, AllBooksAdapter.Viewholder>(options) {
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Viewholder, position: Int, model: Model) {


        //Setting data to android materials
        holder.bookName.text = "Book Name: " + model.bookName
        holder.booksCount.text = "Available Books: " + model.booksCount
        holder.bookLocation.text = "Book Location: " + model.bookLocation
        Picasso.get().load(model.imageUrl).into(holder.imageView)


        //Implementing OnClickListener
        holder.collectBtn.setOnClickListener { view ->
            val bookLocation = model.bookLocation
            val bookName = model.bookName
            val booksCount = model.booksCount
            val imageUrl = model.imageUrl
            val userId = GoogleSignIn.getLastSignedInAccount(view.context)!!
                .id
            FirebaseDatabase.getInstance().reference.child("AllUsers").child(userId!!)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        //Getting user data using Model Class
                        val model1 = snapshot.getValue(
                            Model::class.java
                        )
                        val name = model1!!.name
                        val city = model1.city
                        val phoneNumber = model1.phoneNumber
                        val address = model1.address
                        val pincode = model1.pincode
                        val userDetails: HashMap<String, Any?> = HashMap<String, Any?>()
                        userDetails["name"] = name
                        userDetails["city"] = city
                        userDetails["phoneNumber"] = phoneNumber
                        userDetails["address"] = address
                        userDetails["pincode"] = pincode
                        userDetails["bookLocation"] = bookLocation
                        userDetails["bookName"] = bookName
                        userDetails["booksCount"] = booksCount
                        userDetails["imageUrl"] = imageUrl
                        userDetails["userId"] = userId
                        val push = FirebaseDatabase.getInstance().reference.child("OrderedBooks")
                            .push().key
                        FirebaseDatabase.getInstance().reference.child("OrderedBooks").child(push!!)
                            .updateChildren(userDetails)
                            .addOnSuccessListener {
                                FirebaseDatabase.getInstance().reference.child("myOrderedBooks")
                                    .child(userId).child(push)
                                    .updateChildren(userDetails)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            view.context,
                                            "Book Ordred Successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }


                        //Toast message
                        Toast.makeText(view.context, "Book Ordered", Toast.LENGTH_SHORT).show()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        //the data objects are inflated into the xml file single_data_item
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_book_layout, parent, false)
        return Viewholder(view)
    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var bookName: TextView
        var booksCount: TextView
        var bookLocation: TextView
        var collectBtn: Button

        init {


            //Assigning Address of the android materials
            imageView = itemView.findViewById<View>(R.id.BookImage) as ImageView
            bookName = itemView.findViewById<View>(R.id.BookNameTxt) as TextView
            booksCount = itemView.findViewById<View>(R.id.BooksCountTxt) as TextView
            bookLocation = itemView.findViewById<View>(R.id.BooksLocationTxt) as TextView
            collectBtn = itemView.findViewById<View>(R.id.CollectBookBtn) as Button
        }
    }
}