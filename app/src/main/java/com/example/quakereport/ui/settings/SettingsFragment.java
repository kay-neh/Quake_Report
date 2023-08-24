package com.example.quakereport.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.quakereport.R;

public class SettingsFragment extends PreferenceFragmentCompat{

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_main, rootKey);

        EditTextPreference limit = findPreference(getString(R.string.settings_limit_key));
        if (limit != null) {
            // setting only numeric input type
            limit.setOnBindEditTextListener(editText -> {
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        editText.setSelection(editText.getText().toString().length());
            });
            // Set constraint on list preference to prevent invalid values being passed in
            limit.setOnPreferenceChangeListener((preference, newValue) -> {
                Toast error = Toast.makeText(getContext(), "Please enter a valid number", Toast.LENGTH_SHORT);
                String stringValue = newValue.toString();
                    try {
                        int limit1 = Integer.parseInt(stringValue);
                        if (limit1 <= 0) {
                            error.show();
                            return false;
                        }
                    } catch (NumberFormatException nfe) {
                        error.show();
                        return false;
                    }
                return true;
            });
        }

    }

}
