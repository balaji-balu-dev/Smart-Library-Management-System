package com.balajitech.librarymanagement.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.balajitech.librarymanagement.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.util.HashMap


class LoginActivity : AppCompatActivity() {
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var progressBar:ProgressDialog
    lateinit var button: Button
    lateinit var spinner: Spinner
    var roles = arrayOf<String?>("Admin", "User")
    var userRole: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance()

        //Progress bar
        progressBar = ProgressDialog(this)
        progressBar!!.setTitle("Please Wait...")
        progressBar!!.setMessage("We are setting Everything for you...")
        progressBar!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        spinner = findViewById<View>(R.id.Spinner) as Spinner
        //Creating the ArrayAdapter instance having the country list
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                userRole = roles[i]
                Toast.makeText(applicationContext, roles[i], Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        button = findViewById(R.id.GoogleSignInBtn)


        //Google Signin Options to get gmail and performa gmail login
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(applicationContext, googleSignInOptions)


        //Implementing OnClickListener to perform Login action
        button.setOnClickListener { //Showing all Gmails
            val intent = googleSignInClient.getSignInIntent()
            startActivityForResult(intent, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val googleSignInAccountTask = GoogleSignIn
                .getSignedInAccountFromIntent(data)
            if (googleSignInAccountTask.isSuccessful) {
                progressBar.show()
                try {
                    val googleSignInAccount = googleSignInAccountTask.getResult(
                        ApiException::class.java
                    )
                    if (googleSignInAccount != null) {
                        val authCredential = GoogleAuthProvider
                            .getCredential(googleSignInAccount.idToken, null)
                        firebaseAuth.signInWithCredential(authCredential)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {

                                    //Hashmap to store the userdetails and setting it to fireabse
                                    val user_details = HashMap<String, Any?>()

                                    //Accessing the user details from gmail
                                    val id = googleSignInAccount.id.toString()
                                    val name = googleSignInAccount.displayName.toString()
                                    val mail = googleSignInAccount.email.toString()
                                    val pic = googleSignInAccount.photoUrl.toString()

                                    //storing data in hashmap
                                    user_details["id"] = id
                                    user_details["name"] = name
                                    user_details["mail"] = mail
                                    user_details["profilepic"] = pic
                                    user_details["role"] = userRole

                                    //Adding data to firebase
                                    FirebaseDatabase.getInstance().reference.child("AllUsers")
                                        .child(id)
                                        .updateChildren(user_details)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                progressBar.cancel()
                                                if (userRole == "Admin") {
                                                    //navigating to the main activity after user successfully registers
                                                    val intent = Intent(
                                                        applicationContext,
                                                        AdminActivity::class.java
                                                    )
                                                    //Clears older activities and tasks
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                    startActivity(intent)
                                                } else {
                                                    //navigating to the main activity after user successfully registers
                                                    val intent = Intent(
                                                        applicationContext,
                                                        UserDetailsActivity::class.java
                                                    )
                                                    //Clears older activities and tasks
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                    startActivity(intent)
                                                }
                                            }
                                        }
                                }
                            }
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            }
        }
    }
}