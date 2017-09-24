package com.newwesterndev.svcontrol.utils

import android.content.Context

class Utility(c: Context){

    private val mContext = c
    private val PREFS_FILENAME = "com.newwesterndev.svcontrol.prefs"
    private val USER_HIGH_VOLUME = "high_volume"

    fun getHighStream(): Int{
        val prefs = mContext.getSharedPreferences(PREFS_FILENAME, 0)
        return prefs.getInt(USER_HIGH_VOLUME, 0)
    }

    fun saveHighStream(hv: Int){
        val prefs = mContext.getSharedPreferences(PREFS_FILENAME, 0)
        val editor = prefs!!.edit()
        editor.putInt(USER_HIGH_VOLUME, hv)
        editor.apply()
    }

}
