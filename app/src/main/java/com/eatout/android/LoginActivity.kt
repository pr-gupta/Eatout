package com.eatout.android

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.eatout.android.databinding.ActivityLoginBinding
import com.eatout.android.model.view.LoginViewModel
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private val TAG: String = javaClass.simpleName


    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.loginViewModel = LoginViewModel(context = this, activityLoginBinding = binding)
        mAuth = FirebaseAuth.getInstance()

        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.uid)
                Toast.makeText(this, "Login successful!! Taking you to home in a moment", Toast.LENGTH_SHORT).show()

                Handler().postDelayed({
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }, 1000)

            } else {
                Log.d(TAG, "onAuthStateChanged:signed_out")
            }
        }
    }

    fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, { task ->
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful)

                    if (!task.isSuccessful) {
                        Log.w(TAG, "signInWithEmail:failed", task.exception)
                        Toast.makeText(this@LoginActivity, "Unable to login, either email or passoword is incorrect",
                                Toast.LENGTH_SHORT).show()
                    }
                })
    }

    public override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }

    public override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener(mAuthListener)
    }

    private var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}
