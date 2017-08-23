package com.eatout.android

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class SignUpActivity : AppCompatActivity() {


    private lateinit var _firstNameInput: EditText
    private lateinit var _lastNameInput: EditText
    private lateinit var _emailTextInput: EditText
    private lateinit var _passwordInput: EditText
    private lateinit var _passwordConfirmInput: EditText
    private lateinit var _createAccountButton: Button
    private lateinit var _loginActivityLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        _firstNameInput = findViewById(R.id.et_first_name) as EditText
        _lastNameInput = findViewById(R.id.et_last_name) as EditText
        _emailTextInput = findViewById(R.id.et_input_email) as EditText
        _passwordInput = findViewById(R.id.et_input_password) as EditText
        _passwordConfirmInput = findViewById(R.id.et_input_password_confirm) as EditText
        _createAccountButton = findViewById(R.id.btn_sign_up) as Button
        _loginActivityLink = findViewById(R.id.tv_link_login) as TextView

        _loginActivityLink.setOnClickListener({
            finish()
        })
    }
}
