package com.eatout.android.util.firebase

import com.eatout.android.LoginActivity
import com.eatout.android.beans.User
import com.eatout.android.beans.auth.RegistrationData
import com.eatout.android.util.auth.Login
import com.eatout.android.util.auth.Registration
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.annotations.NotNull

/**
 * Created by prashant.gup on 23/08/17.
 */
class FirebaseRegistration: Registration {
    override fun register(user: User, registrationData: RegistrationData): Boolean = true
}