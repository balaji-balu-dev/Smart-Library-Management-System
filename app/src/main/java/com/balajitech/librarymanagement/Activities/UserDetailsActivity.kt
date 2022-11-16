package com.balajitech.librarymanagement.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.balajitech.librarymanagement.MainActivity
import com.balajitech.librarymanagement.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class UserDetailsActivity : AppCompatActivity() {
    var userPhoneNumber: EditText? = null
    var userAddress: EditText? = null
    var userCity: EditText? = null
    var userPinCode: EditText? = null
    var addDataBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        //Assigning the address of the android materials
        userPhoneNumber = findViewById<View>(R.id.PhoneNumberEditText) as EditText
        userAddress = findViewById<View>(R.id.AddressEditText) as EditText
        userCity = findViewById<View>(R.id.CityEditText) as EditText
        userPinCode = findViewById<View>(R.id.PinCodeExitText) as EditText
        addDataBtn = findViewById<View>(R.id.UpdateProfileBtn) as Button

        //implementing onclicklistener
        addDataBtn!!.setOnClickListener {
            //getting test from the edit text
            val phoneNumber = userPhoneNumber!!.text.toString().trim { it <= ' ' }
            val address = userAddress!!.text.toString().trim { it <= ' ' }
            val city = userCity!!.text.toString().trim { it <= ' ' }
            val pinCode = userPinCode!!.text.toString().trim { it <= ' ' }


            //checking for the empty fields
            if (phoneNumber.isEmpty() || address.isEmpty() || city.isEmpty() || pinCode.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please,Fill all the Details",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                //method to add data to firebase
                addUserDetails(phoneNumber, address, city, pinCode)
            }
        }
    }

    private fun addUserDetails(
        phoneNumber: String,
        address: String,
        city: String,
        pinCode: String
    ) {

        //Getting the user id form the google signin
        val id = GoogleSignIn.getLastSignedInAccount(applicationContext)!!.id

        //Creating Hashmap to store data
        val userDetails: HashMap<String, String> = HashMap<String, String>()
        userDetails["phoneNumber"] = phoneNumber
        userDetails["address"] = address
        userDetails["city"] = city
        userDetails["pincode"] = pinCode

        //Adding the user datials to firebase
        FirebaseDatabase.getInstance().reference.child("AllUsers").child(id!!)
            .updateChildren(userDetails as Map<String, Any>)
            .addOnCompleteListener(object : OnCompleteListener<Void?> {


                override fun onComplete(task: Task<Void?>) {
                    if (task.isSuccessful) {


                        //showing the toast message to user
                        Toast.makeText(
                            applicationContext,
                            "Details added Successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        //Changing current intent after adding the details to firebase
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            })
    }
}