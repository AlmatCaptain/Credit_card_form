package sh1gure.test.creditcardform.utils

import android.content.Context

class ResourceProvider(private val mContext: Context) {

    fun getInteger(resId: Int): Int {
        return mContext.resources.getInteger(resId)
    }
}