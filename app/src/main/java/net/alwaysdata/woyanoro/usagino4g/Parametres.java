package net.alwaysdata.woyanoro.usagino4g;

        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.preference.CheckBoxPreference;
        import android.preference.EditTextPreference;
        import android.preference.ListPreference;
        import android.preference.Preference;
        import android.preference.PreferenceActivity;
        import android.preference.PreferenceFragment;
        import android.preference.PreferenceManager;

public class Parametres extends PreferenceActivity {
    public static class Presf extends PreferenceFragment {
        private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

            prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    Preference pref = findPreference(key);
                    if (pref instanceof ListPreference) {
                        ListPreference listPref = (ListPreference) pref;
                        if (listPref.getEntry().equals("Serveur")) {
                            MainActivity.mode = 2;
                        }
                        if (listPref.getEntry().equals("Off")) {
                            MainActivity.mode = 0;
                        }
                        if (listPref.getEntry().equals("Client")) {
                            MainActivity.mode = 1;
                        }
                    }
                    //
                    if (pref instanceof ListPreference) {
                        ListPreference listPref = (ListPreference) pref;
                        if (listPref.getEntry().equals("Blanc")) {
                            MainActivity.theme = 1;
                        }
                        if (listPref.getEntry().equals("Noir")) {
                            MainActivity.theme = 0;
                        }

                       MainActivity.themechange=true;
                    }
                    //
                    if (pref instanceof CheckBoxPreference) {
                        CheckBoxPreference boxPref = (CheckBoxPreference) pref;
                        if (boxPref.getKey().equals("CodeSimplify")){
                            if (boxPref.isChecked()){
                                MainActivity.simplifyBox.setChecked(true);
                                MainActivity.zeroBox.setChecked(false);
                            }
                            else{
                                MainActivity.simplifyBox.setChecked(false);
                            }
                        }
                    }
                    if (pref instanceof EditTextPreference) {
                        EditTextPreference textPref = (EditTextPreference) pref;
                        if (textPref.getKey().equals("TelNumber")){
                            MainActivity.num_tel.setText(textPref.getText());
                        }
                    }
                }
            };
            prefs.registerOnSharedPreferenceChangeListener(prefListener);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add 'general' preferences, defined in the XML file
        // finaly for setup Prefrence I used this and replace that by new Prefs Class, Hoya its Worked and data input via user is change and weather is update :D
        getFragmentManager().beginTransaction().replace(android.R.id.content, new Presf()).commit();
    }
}
