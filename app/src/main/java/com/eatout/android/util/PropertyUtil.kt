package com.eatout.android.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import java.util.*

/**
 * Created by prashant.gup on 23/08/17.
 *
 */
object PropertyUtil {
    private val _properties: Properties = Properties()

    fun init(context: Context) {
        _properties.load(context.assets.open("private.properties"))
    }


    fun getProperty(key: String):String = _properties.getProperty(key)
}