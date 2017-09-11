package com.eatout.android.util.uber

import android.content.Context
import com.uber.sdk.android.core.UberSdk
import com.uber.sdk.core.auth.Scope
import com.uber.sdk.rides.client.SessionConfiguration
import java.util.*


/**
 * Created by prashant.gup on 07/09/17.
 */
object UberUtil {
    lateinit var config: SessionConfiguration
    fun init () {
        config = SessionConfiguration.Builder()
                .setClientId("Xo94fFEBiZOTrYKaouKfWbwsqQsIn0R3")
                .setServerToken("YdIlV9dlcIMK6npgK0qWlRuBNDzBIRblkXm00c73")
                .setRedirectUri("http://localhost")
                .setScopes(Arrays.asList(Scope.RIDE_WIDGETS))
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build()
        UberSdk.initialize(config)
    }

    fun login(context: Context) {

//        val loginCallback = LoginCallback() {
//
//        }
//
//        val accessTokenManger = AccessTokenManager(context)
//        val loginManger = LoginManager(accessTokenManger,)
    }
}