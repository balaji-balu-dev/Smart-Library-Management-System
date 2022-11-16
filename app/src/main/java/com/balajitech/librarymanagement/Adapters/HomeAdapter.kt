package com.balajitech.librarymanagement.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.balajitech.librarymanagement.Activities.BooksActivity
import com.balajitech.librarymanagement.Model.Model
import com.balajitech.librarymanagement.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso


class HomeAdapter(options: FirebaseRecyclerOptions<Model?>) :
    FirebaseRecyclerAdapter<Model, HomeAdapter.Viewholder>(options) {
    override fun onBindViewHolder(holder: Viewholder, position: Int, model: Model) {
        val imageUrl = model.categoryImage
        val category = model.category.toString()
        Picasso.get().load(imageUrl).into(holder.imageView)
        holder.imageView.setOnClickListener { view ->
            val intent = Intent(view.context, BooksActivity::class.java)
            intent.putExtra("category", category)
            view.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        //the data objects are inflated into the xml file single_data_item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_category_file, parent, false)
        return Viewholder(view)
    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById<View>(R.id.CategoryImage) as ImageView
        }
    }
}