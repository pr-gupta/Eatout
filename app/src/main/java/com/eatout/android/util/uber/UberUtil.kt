package com.eatout.android.util.uber

import com.eatout.android.util.PropertyUtil
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
                .setClientId(PropertyUtil.getProperty("uberClientID"))
                .setServerToken(PropertyUtil.getProperty("uberServerToken"))
                .setRedirectUri("http://localhost")
                .setScopes(Arrays.asList(Scope.RIDE_WIDGETS))
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build()
        UberSdk.initialize(config)
    }
}