package com.eatout.android.util.auth

import com.google.firebase.auth.AuthResult

/**
 * Created by prashant.gup on 23/08/17.
 */
abstract class OnCompleteAuthListener {
    abstract fun onComplete(authResult: AuthResult)
}