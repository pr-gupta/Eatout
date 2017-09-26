package com.eatout.android.model.view

import android.content.Context
import android.content.Intent
import android.databinding.ObservableField
import android.util.Log
import android.view.View
import com.eatout.android.LoginActivity
import com.eatout.android.SignUpActivity
import com.eatout.android.databinding.ActivityLoginBinding

/**
 * Created by prashant.gup on 18/09/17.
 */
class LoginViewModel(
        val inputEmailString: ObservableField<String> = ObservableField(""),
        val inputPasswordString: ObservableField<String> = ObservableField(""),
        val inputEmailStringError: ObservableField<String> = ObservableField(""),
        val context: Context,
        val activityLoginBinding: ActivityLoginBinding
) {

    private val TAG = javaClass.simpleName

    fun onLoginClick(view: View) {

        Log.d(TAG, "Inside loginClick with email as ${inputEmailString.get()} and password as ${inputPasswordString.get()}")
        inputEmailStringError.set("")
        if (inputEmailString.get().isEmpty()) {
            inputEmailStringError.set("Email address is required")
            activityLoginBinding.emailEditText.requestFocus()
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmailString.get()).matches()) {
            inputEmailStringError.set("Can't you enter a valid email address")
            activityLoginBinding.emailEditText.requestFocus()
        } else
            (context as LoginActivity).login(inputEmailString.get(), inputPasswordString.get())

    }

    fun onSignUpLinkClick() {
        context.startActivity(Intent(context, SignUpActivity::class.java))
    }
}