package com.eatout.android.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import java.util.*

/**
 * Created by prashant.gup on 23/08/17.
 */
class PropertyUtil private constructor(private val _context: Context) {
    private var _properties: Properties = Properties()

    init {
        _properties.load(_context.assets.open("private.properties"))
    }

    fun getProperty(key: String):String = _properties.getProperty(key)
}