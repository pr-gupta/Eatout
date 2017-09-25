package com.eatout.android

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.eatout.android.util.Setup
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        Setup().setUpProject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.uid)
                startActivity(Intent(this, HomeActivity::class.java))
                finish()

            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                Log.d(TAG, "onAuthStateChanged:signed_out")
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_log_out-> {
                Toast.makeText(this, "Item 1 Selected", Toast.LENGTH_LONG).show()
                true
            }
            R.id.item2 -> {
                Toast.makeText(this, "Item 2 Selected", Toast.LENGTH_LONG).show()
                true
            }
            R.id.item3 -> {
                Toast.makeText(this, "Item 3 Selected", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        mAuth.removeAuthStateListener(mAuthListener)
        super.onStop()
    }
}
