package com.App1.app1

import android.os.Bundle
import android.preference.PreferenceActivity

class SettingsActivity : PreferenceActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}
