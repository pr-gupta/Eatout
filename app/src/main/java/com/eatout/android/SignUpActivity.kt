package com.eatout.android

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class SignUpActivity : AppCompatActivity() {


    private lateinit var _firstNameInput: EditText
    private lateinit var _lastNameInput: EditText
    private lateinit var _emailTextInput: EditText
    private lateinit var _passwordInput: EditText
    private lateinit var _passwordConfirmInput: EditText
    private lateinit var _createAccountButton: Button
    private lateinit var _loginActivityLink: TextView

    private val TAG = javaClass.simpleName

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

        _createAccountButton.setOnClickListener({
            val email = _emailTextInput.text.toString()
            val password = _passwordInput.text.toString()

            if(_emailTextInput.text.toString().trim() == "") {
                _emailTextInput.error = "Email address is required"
                _emailTextInput.requestFocus()
            }
            else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(_emailTextInput.text.toString()).matches()) {
                _emailTextInput.error = "Can't you enter a valid email address"
                _emailTextInput.requestFocus()
            } else if(password != _passwordConfirmInput.text.toString()) {
                _passwordConfirmInput.error = "Password does not match!"
                _passwordConfirmInput.requestFocus()
            }
            else {
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, { task ->
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful)

                            if (!task.isSuccessful) {
                                Log.w(TAG, task.exception)
                                Toast.makeText(this@SignUpActivity, "Authentication failed",
                                        Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@SignUpActivity, "SignUp Successful",
                                        Toast.LENGTH_SHORT).show()
                                Handler().postDelayed({finish()}, 1000)
                            }
                        })
            }
        })


    }
}
