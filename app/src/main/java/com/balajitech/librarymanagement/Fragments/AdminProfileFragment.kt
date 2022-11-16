package com.balajitech.librarymanagement.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.balajitech.librarymanagement.Activities.SplashScreenActivity
import com.balajitech.librarymanagement.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AdminProfileFragment : Fragment() {
    var imageView: CircleImageView? = null
    var userName: TextView? = null
    var signOutBtn: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_profile, container, false)
        imageView = view.findViewById<View>(R.id.ProfilePic) as CircleImageView
        userName = view.findViewById<View>(R.id.UserNameTxt) as TextView
        signOutBtn = view.findViewById<View>(R.id.SignOutBtn) as Button

        //Getting user detials from GoogleSignin
        val acct = GoogleSignIn.getLastSignedInAccount(activity)
        if (acct != null) {
            userName!!.text = acct.displayName
            Picasso.get().load(acct.photoUrl).into(imageView)
        }


        //implementing onClickListener to make the user signOut
        signOutBtn!!.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()

            //GoogleSignInClient to access the current user
            val googleSignInClient = GoogleSignIn.getClient(activity, gso)
            googleSignInClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //User Signout
                    FirebaseAuth.getInstance().signOut()

                    //Redirecting to starting Activity
                    val intent = Intent(context, SplashScreenActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            }
        }
        return view
    }
}