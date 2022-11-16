package com.balajitech.librarymanagement.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balajitech.librarymanagement.Adapters.EditDetailsAdapter
import com.balajitech.librarymanagement.Model.Model
import com.balajitech.librarymanagement.R
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase


class EditBookDetailsActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var adapter: EditDetailsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book_details)
        val category = intent.getStringExtra("category")

        //Assigning the Recyclerview
        recyclerView = findViewById<View>(R.id.EditBookDetailsRecyclerView) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext)


        //Firebase Recycler Options to get the data form firebase database using model class and reference
        val options = FirebaseRecyclerOptions.Builder<Model>()
            .setQuery(
                FirebaseDatabase.getInstance().reference.child("AllBooks").child(category!!),
                Model::class.java
            )
            .build()


        //Setting adapter to RecyclerView
        adapter = EditDetailsAdapter(options)
        recyclerView!!.adapter = adapter
    }

    public override fun onStart() {
        super.onStart()
        //Starts listening for data from firebase when this fragment starts
        adapter!!.startListening()
    }

    public override fun onStop() {
        super.onStop()
        //Stops listening for data from firebase
        adapter!!.stopListening()
    }
}