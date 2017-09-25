package com.eatout.android.util.auth

import com.eatout.android.beans.User
import com.eatout.android.beans.auth.RegistrationData

/**
 * Created by prashant.gup on 23/08/17.
 */
interface Registration {
    fun register(user: User, registrationData: RegistrationData): Boolean
}