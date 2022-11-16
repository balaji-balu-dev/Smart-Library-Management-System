package com.balajitech.librarymanagement.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balajitech.librarymanagement.Adapters.HomeAdapter
import com.balajitech.librarymanagement.Model.Model
import com.balajitech.librarymanagement.R
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase


class HomeFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var adapter: HomeAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //Assigning the Recyclerview
        recyclerView = view.findViewById<View>(R.id.HomeRecyclerView) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(context)


        //Firebase Recycler Options to get the data form firebase database using model class and reference
        val options = FirebaseRecyclerOptions.Builder<Model>()
            .setQuery(
                FirebaseDatabase.getInstance().reference.child("TotalBooksCategories"),
                Model::class.java
            )
            .build()


        //Setting adapter to RecyclerView
        adapter = HomeAdapter(options)
        recyclerView!!.adapter = adapter
        return view
    }

    override fun onStart() {
        super.onStart()
        //Starts listening for data from firebase when this fragment starts
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        //Stops listening for data from firebase
        adapter!!.stopListening()
    }
}