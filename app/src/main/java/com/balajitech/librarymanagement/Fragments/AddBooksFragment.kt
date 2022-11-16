package com.balajitech.librarymanagement.Fragments


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.fragment.app.Fragment
import com.balajitech.librarymanagement.Activities.AdminActivity
import com.balajitech.librarymanagement.R
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.lang.ref.Reference
import java.util.ArrayList
import java.util.HashMap

class AddBooksFragment : Fragment(), AdapterView.OnItemSelectedListener {
    var submitBtn: Button? = null
    var imageView: ImageView? = null
    var bookNameEditTxt: EditText? = null
    var booksCountEditTxt: EditText? = null
    var booksLocationEditText: EditText? = null
    var databaseReference: DatabaseReference? = null
    lateinit var storageReference: StorageReference
    var imageUri: Uri? = null
    var spinner: Spinner? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_books, container, false)

        //Assigning the address of the android materials
        imageView = view.findViewById<View>(R.id.BookImage) as ImageView
        bookNameEditTxt = view.findViewById<View>(R.id.BookNameEditTxt) as EditText
        booksCountEditTxt = view.findViewById<View>(R.id.TotalBooksEditTxt) as EditText
        booksLocationEditText = view.findViewById<View>(R.id.BookLocationEditTxt) as EditText

        // Spinner element
        spinner = view.findViewById<View>(R.id.Spinner) as Spinner
        submitBtn = view.findViewById<View>(R.id.AddBookBtn) as Button
        databaseReference = FirebaseDatabase.getInstance().reference.child("AllBooks")
        storageReference = FirebaseStorage.getInstance().reference


        // Spinner click listener
        spinner!!.onItemSelectedListener = this

        // Spinner Drop down elements
        val categories: MutableList<String> = ArrayList()
        categories.add("Technology")
        categories.add("Health")


        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinner!!.adapter = dataAdapter


        //Setting onClick Listener for the imageView To select image
        imageView!!.setOnClickListener {
            val galleryIntent = Intent()
            //setting the intent action to get content
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            //setting the upload content type as image
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, 2)
        }
        submitBtn!!.setOnClickListener {
            //getting the data from the text view
            val bookName = bookNameEditTxt!!.text.toString()
            val booksCount = booksCountEditTxt!!.text.toString()
            val bookLocation = booksLocationEditText!!.text.toString()
            val category = spinner!!.selectedItem.toString()

            //checking all the fields are filled or not and performing the upload data action
            if (bookName.isEmpty() || booksCount.isEmpty() || bookLocation.isEmpty()) {
                Toast.makeText(context, "Please Enter Details", Toast.LENGTH_SHORT).show()
            } else if (imageUri == null) {
                Toast.makeText(context, "Please Upload Image", Toast.LENGTH_SHORT).show()
            } else {
                //calling the method to add data to fireabase
                uploadData(imageUri!!, bookName, booksCount, bookLocation, category)
                Toast.makeText(context, "Please,Wait uploading data...", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun uploadData(
        imageUri: Uri,
        bookName: String,
        booksCount: String,
        bookLocation: String,
        category: String
    ) {

        //setting the file name as current time with milli Seconds to make the image name unique
        val fileRef = storageReference!!.child(
            System.currentTimeMillis().toString() + "." + getFileExtension(imageUri)
        )
        //uploading the image to firebase
        fileRef.putFile(imageUri).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    //generating the unique key to add data under this node
                    val push = databaseReference!!.push().key.toString()

                    //Hash map to store values
                    val bookDetails: HashMap<String, String> = HashMap<String, String>()

                    //adding the data to hashmap
                    bookDetails["bookName"] = bookName
                    bookDetails["booksCount"] = booksCount
                    bookDetails["bookLocation"] = bookLocation
                    bookDetails["imageUrl"] = uri.toString()
                    bookDetails["category"] = category
                    bookDetails["pushKey"] = push

                    //uploading the data to the fireabase
                    databaseReference!!.child(category)
                        .child(push).setValue(bookDetails)
                        .addOnSuccessListener {
                            FirebaseDatabase.getInstance().reference.child("TotalBooksCategories")
                                .child(push).child("category").setValue(category)
                                .addOnSuccessListener { //Calling the same intent to reset all the current data
                                    val intent = Intent(context, AdminActivity::class.java)
                                    requireActivity().startActivity(intent)
                                    requireActivity().finish()

                                    //Showing the toast to user for confirmation
                                    Toast.makeText(
                                        context,
                                        "Data Uploaded Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                }
                    .addOnFailureListener { //Showing the toast message to the user to reUpload the data
                        Toast.makeText(
                            context,
                            "Failed To Upload Please,Try Again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    private fun getFileExtension(imageUri: Uri): String? {

        //getting the image extension
        val contentResolver = requireActivity().contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

            //Getting the image from the device and setting the image to imageView
            imageUri = data.data
            imageView!!.setImageURI(imageUri)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

        // On selecting a spinner item
        val item = parent.getItemAtPosition(position).toString()

        // Showing selected spinner item
        Toast.makeText(parent.context, "Selected: $item", Toast.LENGTH_LONG).show()
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {
        //Do nothing
    }
}