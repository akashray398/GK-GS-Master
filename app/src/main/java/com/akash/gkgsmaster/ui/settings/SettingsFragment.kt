package com.akash.gkgsmaster.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.akash.gkgsmaster.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        setupPreferences()
    }

    private fun setupPreferences() {
        // Dark Mode
        findPreference<SwitchPreferenceCompat>("dark_mode")?.setOnPreferenceChangeListener { _, newValue ->
            val isDark = newValue as Boolean
            AppCompatDelegate.setDefaultNightMode(
                if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
            true
        }

        // Language
        findPreference<ListPreference>("language")?.setOnPreferenceChangeListener { _, newValue ->
            Toast.makeText(requireContext(), "Language changed to $newValue. Restart app to apply.", Toast.LENGTH_SHORT).show()
            true
        }

        // Backup & Restore
        findPreference<Preference>("backup")?.setOnPreferenceClickListener {
            Toast.makeText(requireContext(), R.string.feature_coming_soon, Toast.LENGTH_SHORT).show()
            true
        }

        findPreference<Preference>("restore")?.setOnPreferenceClickListener {
            Toast.makeText(requireContext(), R.string.feature_coming_soon, Toast.LENGTH_SHORT).show()
            true
        }

        // About
        findPreference<Preference>("privacy_policy")?.setOnPreferenceClickListener {
            openUrl("https://gkgsmaster.com/privacy")
            true
        }

        findPreference<Preference>("terms")?.setOnPreferenceClickListener {
            openUrl("https://gkgsmaster.com/terms")
            true
        }

        findPreference<Preference>("rate_app")?.setOnPreferenceClickListener {
            openUrl("market://details?id=${requireContext().packageName}")
            true
        }

        findPreference<Preference>("share_app")?.setOnPreferenceClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Check out GK & GS Master app: https://play.google.com/store/apps/details?id=${requireContext().packageName}")
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
            true
        }

        findPreference<Preference>("contact_us")?.setOnPreferenceClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:support@gkgsmaster.com")
                putExtra(Intent.EXTRA_SUBJECT, "GK & GS Master Feedback")
            }
            startActivity(Intent.createChooser(emailIntent, "Send Email"))
            true
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Could not open link", Toast.LENGTH_SHORT).show()
        }
    }
}
