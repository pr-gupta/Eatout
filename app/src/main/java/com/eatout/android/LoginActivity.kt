package com.eatout.android

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private val TAG: String = javaClass.simpleName

    private lateinit var _emailTextInput: EditText
    private lateinit var _passwordInput: EditText
    private lateinit var _loginButton: Button
    private lateinit var _signUpActivityLink: TextView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        _emailTextInput = findViewById(R.id.et_input_email) as EditText
        _passwordInput = findViewById(R.id.et_input_password) as EditText
        _loginButton = findViewById(R.id.btn_login) as Button
        _signUpActivityLink = findViewById(R.id.tv_link_sign_up) as TextView

        _signUpActivityLink.setOnClickListener({
            startActivity(Intent(this, SignUpActivity::class.java))
        })

        mAuth = FirebaseAuth.getInstance()

        _loginButton.setOnClickListener({

            if(_emailTextInput.text.toString().trim() == "") {
                _emailTextInput.error = "Email address is required"
                _emailTextInput.requestFocus()
            }
            else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(_emailTextInput.text.toString()).matches()) {
                _emailTextInput.error = "Can't you enter a valid email address"
                _emailTextInput.requestFocus()
            }
            else {

                mAuth.signInWithEmailAndPassword(_emailTextInput.text.toString(), _passwordInput.text.toString())
                        .addOnCompleteListener(this, { task ->
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful)

                            if (!task.isSuccessful) {
                                Log.w(TAG, "signInWithEmail:failed", task.exception)
                                Toast.makeText(this@LoginActivity, "Unable to login, either email or passoword is incorrect",
                                        Toast.LENGTH_SHORT).show()
                            }
                        })
            }
        })

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

        Handler().postDelayed({doubleBackToExitPressedOnce = false }, 2000)
    }
}
