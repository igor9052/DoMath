package ua.com.igorka.android.game.domath.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.text.InputType;

import java.util.TreeSet;

import ua.com.igorka.android.game.domath.R;
import ua.com.igorka.android.game.domath.utils.App;
import ua.com.igorka.android.game.domath.utils.Util;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

        public SettingsFragment() {
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
            initPreferences();
        }

        private void initPreferences() {
            for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
                initPreferenceItem(getPreferenceScreen().getPreference(i));
            }
        }

        private void initPreferenceItem(Preference preference) {
            if (preference instanceof PreferenceCategory) {
                PreferenceCategory pCat = (PreferenceCategory) preference;
                for (int i = 0; i < pCat.getPreferenceCount(); i++) {
                    initPreferenceItem(pCat.getPreference(i));
                }
            } else {
                updatePreferenceItem(preference);
            }
        }

        private void updatePreferenceItem(Preference preference) {
            if (preference == null)
                return;

            if (preference instanceof ListPreference) {
                // List Preference
                ListPreference listPref = (ListPreference) preference;
                listPref.setSummary(listPref.getEntry());

            } else if (preference instanceof EditTextPreference) {
                // EditPreference
                EditTextPreference editTextPref = (EditTextPreference) preference;
                if ((editTextPref.getEditText().getInputType() & 0x80)  == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    editTextPref.setSummary("********");
                    return;
                }
                editTextPref.setSummary(editTextPref.getText());
            } else if (preference instanceof MultiSelectListPreference) {
                MultiSelectListPreference listPreference = (MultiSelectListPreference) preference;
                StringBuilder str = new StringBuilder();
                TreeSet<String> values = new TreeSet<>(listPreference.getValues());
                for (String item : values) {
                    str.append(item).append(Util.COMMA_SPACE);
                }
                if (str.length() > 0) {
                    listPreference.setSummary(str.toString().substring(0, str.length() - Util.COMMA_SPACE.length()));
                }
                else {
                    listPreference.setSummary("");
                }
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updatePreferenceItem(findPreference(key));
            App.Settings.updateSettings(key);
        }
    }

}
