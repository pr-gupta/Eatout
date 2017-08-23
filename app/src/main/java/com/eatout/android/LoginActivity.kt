package com.eatout.android

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlin.jvm.javaClass

class LoginActivity : AppCompatActivity() {

    private val TAG: String = javaClass.simpleName

    private lateinit var _emailTextInput: EditText
    private lateinit var _passwordInput: EditText
    private lateinit var _loginButton: Button
    private lateinit var _signUpActivityLink: TextView

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

        _loginButton.setOnClickListener({
            Toast.makeText(this, "TODO Implement Login function", Toast.LENGTH_SHORT).show()
        })


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
