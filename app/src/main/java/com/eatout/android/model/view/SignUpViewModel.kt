package com.eatout.android.model.view

import android.content.Context
import android.databinding.ObservableField

/**
 * Created by prashant.gup on 20/09/17.
 */
class SignUpViewModel(
        val inputFirstNameString: ObservableField<String> = ObservableField(""),
        val inputLastNameString: ObservableField<String> = ObservableField(""),
        val inputEmailString: ObservableField<String> = ObservableField(""),
        val inputPasswordString: ObservableField<String> = ObservableField(""),
        val inputEmailStringError: ObservableField<String> = ObservableField(""),
        val inputPasswordConfirmationString: ObservableField<String> = ObservableField(""),
        val inputPasswordConfirmationStringError: ObservableField<String> = ObservableField(""),
        val context: Context
) {

    init {
        if(context !is SignUpViewModelChangeListener) {
            throw Exception("The activity using SignUpViewModel has to implement SignUpViewModelChangeListener")
        }
    }

    fun signUpClicked() {
        inputEmailStringError.set("")
        inputPasswordConfirmationStringError.set("")

        val email = inputEmailString.get()
        val password = inputPasswordString.get()

        if(email.trim() == "") {
            inputEmailStringError.set("Email address is required")
            (context as SignUpViewModelChangeListener).raiseEmailError()
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmailStringError.set("Can't you enter a valid email address")
            (context as SignUpViewModelChangeListener).raiseEmailError()
        } else if(password != inputPasswordConfirmationString.get()) {
            inputPasswordConfirmationStringError.set("Password does not match!")
            (context as SignUpViewModelChangeListener).raisePasswordError()
        } else {
            (context as SignUpViewModelChangeListener).signUpClicked(email, password)
        }
    }

    fun loginLinkClicked() {
        (context as SignUpViewModelChangeListener).loginLinkClicked()
    }

    interface SignUpViewModelChangeListener {
        fun signUpClicked(email : String, password : String)
        fun loginLinkClicked()
        fun raiseEmailError()
        fun raisePasswordError()
    }
}