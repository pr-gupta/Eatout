package com.eatout.android.util

import android.content.Context
import android.preference.ListPreference
import android.preference.MultiSelectListPreference
import android.util.AttributeSet

/**
 * Created by prashant.gup on 05/09/17.
 */
class CustomMultiSelectListPreference: MultiSelectListPreference {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)


    fun customClick() {
        showDialog(null)
    }


}