package com.eatout.android

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.eatout.android.databinding.ActivitySignUpBinding
import com.eatout.android.model.view.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class SignUpActivity : AppCompatActivity(), SignUpViewModel.SignUpViewModelChangeListener {

    private val TAG = javaClass.simpleName

    private lateinit var _binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        _binding.signUpViewModel = SignUpViewModel(context = this)
    }

    override fun loginLinkClicked() {
        finish()
    }

    override fun signUpClicked(email: String, password: String) {
        _binding.signUpViewModel.isLoading.set(true)

        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, { task ->
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful)
                    _binding.signUpViewModel.isLoading.set(false)
                    if (!task.isSuccessful) {
                        Log.w(TAG, task.exception)
                        Toast.makeText(this@SignUpActivity, "Authentication failed",
                                Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@SignUpActivity, "SignUp Successful",
                                Toast.LENGTH_SHORT).show()

                        val map: HashMap<String, String> = hashMapOf()
                        map.put("FirstName", _binding.signUpViewModel.inputFirstNameString.get())
                        map.put("LastName", _binding.signUpViewModel.inputLastNameString.get())
                        map.put("profileImageURL", "https://i.imgur.com/akSIG69.png")
                        Log.v(TAG, map.toString())

                        FirebaseDatabase.getInstance().reference
                                .child("users").child(task.result.user.uid)
                                .setValue(map)
                        Handler().postDelayed({finish()}, 1000)
                    }
                })
    }

    override fun raiseEmailError() {
        _binding.emailEditText.requestFocus()
    }

    override fun raisePasswordError() {
        _binding.confirmPasswordEditText.requestFocus()
    }
}
