package com.balajitech.librarymanagement.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.balajitech.librarymanagement.Model.Model
import com.balajitech.librarymanagement.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import java.util.HashMap

class EditDetailsAdapter(options: FirebaseRecyclerOptions<Model?>) :
    FirebaseRecyclerAdapter<Model, EditDetailsAdapter.Viewholder>(options) {
    override fun onBindViewHolder(holder: Viewholder, position: Int, model: Model) {


        //Setting data to android materials
        holder.bookName.setText(model.bookName)
        holder.booksCount.setText(model.booksCount)
        holder.bookLocation.setText(model.bookLocation)
        Picasso.get().load(model.imageUrl).into(holder.imageView)
        val pushKey = model.pushKey.toString()
        val category = model.category.toString()

        //Implementing the OnClick Listener to delete the data from the database
        holder.updateDetailsBtn.setOnClickListener { view ->
            val bookName = holder.bookName.text.toString()
            val booksCount = holder.booksCount.text.toString()
            val bookLocation = holder.bookLocation.text.toString()

            //Hash map to store values
            val bookDetails: HashMap<String, String> = HashMap<String, String>()

            //adding the data to hashmap
            bookDetails["bookName"] = bookName
            bookDetails["booksCount"] = booksCount
            bookDetails["bookLocation"] = bookLocation
            bookDetails["category"] = category
            bookDetails["pushKey"] = pushKey
            FirebaseDatabase.getInstance().reference.child("AllBooks")
                .child(category)
                .child(pushKey)
                .updateChildren(bookDetails as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(
                        view.context,
                        "Details Updated Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        //the data objects are inflated into the xml file single_data_item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_edit_book_details_layout, parent, false)
        return Viewholder(view)
    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var bookName: EditText
        var booksCount: EditText
        var bookLocation: EditText
        var updateDetailsBtn: Button

        init {


            //Assigning Address of the android materials
            imageView = itemView.findViewById<View>(R.id.BookImage) as ImageView
            bookName = itemView.findViewById<View>(R.id.BookNameTxt) as EditText
            booksCount = itemView.findViewById<View>(R.id.BooksCountTxt) as EditText
            bookLocation = itemView.findViewById<View>(R.id.BooksLocationTxt) as EditText
            updateDetailsBtn = itemView.findViewById<View>(R.id.UpdateDataBtn) as Button
            updateDetailsBtn.text = "Update Details"
        }
    }
}