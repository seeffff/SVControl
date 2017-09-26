package com.newwesterndev.svcontrol

import android.content.Intent
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceManager

class SettingsActivity : PreferenceActivity(), Preference.OnPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.prefs_general)

        bindPreferenceSummaryToValue(findPreference(getString(R.string.prefs_units_key)))
    }

    override fun onPreferenceChange(preference: Preference, o: Any): Boolean {
        val stringValue = o.toString()

        if(preference is ListPreference ){
            val prefIndex = preference.findIndexOfValue(stringValue)
            if(prefIndex >= 0)
                preference.setSummary(preference.entries[prefIndex])
        }

        return true
    }

    fun bindPreferenceSummaryToValue(preference: Preference){
        preference.setOnPreferenceChangeListener(this)

        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.context)
                        .getString(preference.key, ""))
    }

    override fun getParentActivityIntent(): Intent {
        if(atLeastAPI(16)){
            return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        return super.getParentActivityIntent()
    }

    private fun atLeastAPI(api: Int): Boolean {
        return api <= android.os.Build.VERSION.SDK_INT
    }
}
