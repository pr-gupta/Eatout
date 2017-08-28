package com.eatout.android.util

import android.content.Context

/**
 * Created by prashant.gup on 23/08/17.
 */
class Setup {
    fun setUpProject(context: Context) {
        PropertyUtil.init(context)
    }
}