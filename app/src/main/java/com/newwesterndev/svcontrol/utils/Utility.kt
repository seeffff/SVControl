package com.newwesterndev.svcontrol.utils

import android.content.Context
import android.preference.PreferenceManager
import com.newwesterndev.svcontrol.R

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

    fun getSpeedUnits(): String{
        val prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        return prefs.getString(mContext.resources.getString(R.string.prefs_units_key),
                mContext.resources.getString(R.string.mph_speed_text))
    }

}
