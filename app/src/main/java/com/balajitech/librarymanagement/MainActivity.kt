package com.balajitech.librarymanagement

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.balajitech.librarymanagement.*
import com.balajitech.librarymanagement.Activities.AdminActivity
import com.balajitech.librarymanagement.Activities.SplashScreenActivity
import com.balajitech.librarymanagement.Fragments.DashBoardFragment
import com.balajitech.librarymanagement.Fragments.HomeFragment
import com.balajitech.librarymanagement.Fragments.NotificationsFragment
import com.balajitech.librarymanagement.Fragments.UserProfileFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    var frameLayout: FrameLayout? = null
    var bottomNavigationView: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Assigning framelayout resource file to show appropriate fragment using address
        frameLayout = findViewById<View>(R.id.UserFragmentContainer) as FrameLayout
        //Assigining Bottomnavigaiton Menu
        bottomNavigationView =
            findViewById<View>(R.id.UserBottomNavigationView) as BottomNavigationView
        val menuNav = bottomNavigationView!!.menu
        //Setting the default fragment as HomeFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.UserFragmentContainer, HomeFragment()).commit()
        //Calling the bottoNavigationMethod when we click on any menu item
        bottomNavigationView!!.setOnNavigationItemSelectedListener(bottomNavigationMethod)
    }

    private val bottomNavigationMethod =
        BottomNavigationView.OnNavigationItemSelectedListener { item -> //Assigining Fragment as Null
            var fragment: Fragment? = null
            when (item.itemId) {
                R.id.HomeMenu -> fragment = HomeFragment()
                R.id.DashBoardMenu -> fragment = DashBoardFragment()
                R.id.NotificationsMenu -> fragment = NotificationsFragment()
                R.id.ProfileMenu -> fragment = UserProfileFragment()
            }
            //Sets the selected Fragment into the Framelayout
            supportFragmentManager.beginTransaction()
                .replace(R.id.UserFragmentContainer, fragment!!).commit()
            true
        }

    override fun onStart() {
        super.onStart()
        //checking user already logged or not
        val mUser = FirebaseAuth.getInstance().currentUser
        if (mUser == null) {
            val intent = Intent(applicationContext, SplashScreenActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            //Checks for user Role and starts the appropriate activity
            val id = GoogleSignIn.getLastSignedInAccount(applicationContext).id
            val reference =
                FirebaseDatabase.getInstance().reference.child("AllUsers").child(id).child("role")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value.toString() != null) {
                        val data = snapshot.value.toString()
                        Toast.makeText(applicationContext, data, Toast.LENGTH_SHORT).show()
                        if (data == "Admin") {
                            val intent = Intent(applicationContext, AdminActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            //do nothing
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }
}