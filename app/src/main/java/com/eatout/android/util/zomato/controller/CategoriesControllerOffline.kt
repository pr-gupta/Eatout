package com.eatout.android.util.zomato.controller

import android.content.Context
import com.eatout.android.db.DBCategoryHelper
import com.eatout.android.util.zomato.events.GetCategoryCompletionEvent
import org.greenrobot.eventbus.EventBus

/**
 * Created by prashant.gup on 15/09/17.
 */
class CategoriesControllerOffline(val context: Context): ICategories(context) {
    override fun getCategories() {
        EventBus.getDefault().post(GetCategoryCompletionEvent(DBCategoryHelper(context).getCategories()))
    }
}