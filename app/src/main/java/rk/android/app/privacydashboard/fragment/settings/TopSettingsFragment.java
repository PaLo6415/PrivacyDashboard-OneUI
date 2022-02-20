package rk.android.app.privacydashboard.fragment.settings;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.List;

import de.dlyt.yanndroid.oneui.preference.DropDownPreference;
import de.dlyt.yanndroid.oneui.preference.Preference;
import de.dlyt.yanndroid.oneui.preference.PreferenceFragmentCompat;

import de.dlyt.yanndroid.oneui.preference.SwitchPreferenceCompat;
import de.dlyt.yanndroid.oneui.preference.SwitchPreferenceScreen;
import rk.android.app.privacydashboard.BuildConfig;
import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.activity.settings.excluded.database.ExcludedRepository;
import rk.android.app.privacydashboard.manager.PreferenceManager;
import rk.android.app.privacydashboard.manager.PreferenceManager.PREF_CONSTANTS;
import rk.android.app.privacydashboard.util.Dialogs;
import rk.android.app.privacydashboard.util.Permissions;
import rk.android.app.privacydashboard.util.Utils;

public class TopSettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    private Context context;
    private PreferenceManager preferenceManager;
    ExcludedRepository repository;

    private SwitchPreferenceScreen privacyDotsPref;
    private SwitchPreferenceScreen notificationPref;

    private SwitchPreferenceCompat locationPermPref;
    private SwitchPreferenceCompat accessibilityPermPref;

    private SwitchPreferenceScreen excludePref;
    private Preference deleteLogsPref;
    private DropDownPreference themePref;
    private Preference changelogPref;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        preferenceManager = new PreferenceManager(context);
        repository = new ExcludedRepository(getActivity().getApplication());
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        addPreferencesFromResource(R.xml.oui_top_settings);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initPreferences();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (privacyDotsPref != null) {
            privacyDotsPref.setChecked(preferenceManager.isPrivacyDots());
        }
        if (notificationPref != null) {
            notificationPref.setChecked(preferenceManager.isPrivacyNotification());
        }
        if (excludePref != null) {
            if (preferenceManager.isPrivacyExcluded()) {
                excludePref.setChecked(true);
                setExcludedAppsSummary();
            } else {
                excludePref.setChecked(false);
                excludePref.setSummary(getString(R.string.oui_settings_exclude_summary));
                excludePref.seslSetSummaryColor(getColoredSummaryColor(false));
            }
        }
        if (themePref != null) {
            themePref.setSummary(themePref.getEntry());
            themePref.seslSetSummaryColor(getColoredSummaryColor(true));
        }
    }

    @Override
    public void onResume() {
        if (locationPermPref != null) {
            if (Permissions.checkLocation(context)) {
                locationPermPref.setWidgetLayoutResource(R.layout.sesl_preference_widget_switch);
                locationPermPref.setChecked(true);
            } else {
                locationPermPref.setWidgetLayoutResource(R.layout.oui_preference_alert_switch_layout);
                locationPermPref.setChecked(false);
            }
        }
        if (accessibilityPermPref != null) {
            if (Permissions.checkAccessibility(context)) {
                accessibilityPermPref.setWidgetLayoutResource(R.layout.sesl_preference_widget_switch);
                accessibilityPermPref.setChecked(true);
            } else {
                accessibilityPermPref.setWidgetLayoutResource(R.layout.oui_preference_alert_switch_layout);
                accessibilityPermPref.setChecked(false);
            }
        }

        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.item_background_color, context.getTheme()));
        getListView().seslSetLastRoundedCorner(false);
    }

    private void initPreferences() {
        privacyDotsPref = findPreference(PREF_CONSTANTS.PRIVACY_DOTS);
        notificationPref = findPreference(PREF_CONSTANTS.PRIVACY_NOTIFICATION);

        locationPermPref = findPreference("privacy.location");
        accessibilityPermPref = findPreference("privacy.accessibility");
        locationPermPref.setOnPreferenceClickListener(this);
        locationPermPref.setOnPreferenceChangeListener(this);
        accessibilityPermPref.setOnPreferenceClickListener(this);
        accessibilityPermPref.setOnPreferenceChangeListener(this);

        excludePref = findPreference(PREF_CONSTANTS.PRIVACY_EXCLUDE);
        excludePref.setOnPreferenceChangeListener(this);
        deleteLogsPref = findPreference("delete.logs");
        deleteLogsPref.setOnPreferenceClickListener(this);

        themePref = findPreference(PREF_CONSTANTS.NIGHT_MODE);
        themePref.setOnPreferenceChangeListener(this);

        changelogPref = findPreference("changelog");
        changelogPref.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(@NonNull Preference preference) {
        switch (preference.getKey()) {
            case "privacy.location":
                if (Permissions.checkLocation(context)) {
                    try {
                        Intent intent = new Intent("android.intent.action.MANAGE_APP_PERMISSIONS");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("android.intent.extra.PACKAGE_NAME", BuildConfig.APPLICATION_ID);
                        intent.putExtra("hideInfoButton", true);
                        startActivity(intent);
                    } catch (SecurityException e) {
                        startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)));
                    }
                } else  {
                    Permissions.requestLocationPermission(getActivity());
                }
                return false;
            case "privacy.accessibility":
                Permissions.requestAccessibilityPermission(getActivity());
                return false;
            case "delete.logs":
                Dialogs.deleteLogs(getActivity(),
                        context,
                        getString(R.string.oui_details_delete_logs),
                        getString(R.string.oui_delete_all_logs_dialog_text),
                        null);
                return true;
            case "changelog":
                Dialogs.showWhatsNewDialog(context, preferenceManager, true);
                return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case PREF_CONSTANTS.PRIVACY_EXCLUDE:
                if ((boolean) newValue) {
                    setExcludedAppsSummary();
                } else {
                    excludePref.setSummary(getString(R.string.oui_settings_exclude_summary));
                    excludePref.seslSetSummaryColor(getColoredSummaryColor(false));
                }
                return true;
            case PREF_CONSTANTS.NIGHT_MODE:
                switch ((String) newValue) {
                    case "0":
                        Utils.setTheme((AppCompatActivity) getActivity(), AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        themePref.setSummary(themePref.getEntries()[2]);
                        break;
                    case "1":
                        Utils.setTheme((AppCompatActivity) getActivity(), AppCompatDelegate.MODE_NIGHT_NO);
                        themePref.setSummary(themePref.getEntries()[0]);
                        break;
                    case "2":
                        Utils.setTheme((AppCompatActivity) getActivity(), AppCompatDelegate.MODE_NIGHT_YES);
                        themePref.setSummary(themePref.getEntries()[1]);
                        break;
                }
                return true;
        }

        return false;
    }

    private void setExcludedAppsSummary() {
        String summary;
        List<String> packages = repository.getPackages();

        if (packages.size() > 3) {
            summary = getString(R.string.oui_settings_exclude_summary_enabled, packages.size());
        } else if (packages.size() > 0) {
            PackageManager pm = context.getPackageManager();
            StringBuilder strBuilder = new StringBuilder();
            String separator = "  •  ";

            for (String app : packages) {
                if (strBuilder.length() > 0) strBuilder.append(separator);
                try {
                    PackageInfo packageInfo = pm.getPackageInfo(app, 0);
                    strBuilder.append(packageInfo.applicationInfo.loadLabel(pm).toString());
                } catch (PackageManager.NameNotFoundException ignored) { }
            }

            summary = strBuilder.toString();
        } else {
            summary = getString(R.string.oui_settings_exclude_summary_enabled, 0);
        }

        excludePref.setSummary(summary);
        excludePref.seslSetSummaryColor(getColoredSummaryColor(true));
    }

    private ColorStateList getColoredSummaryColor(boolean enabled) {
        if (enabled) {
            TypedValue colorPrimaryDark = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorPrimaryDark, colorPrimaryDark, true);

            int[][] states = new int[][] {
                    new int[] {android.R.attr.state_enabled},
                    new int[] {-android.R.attr.state_enabled}
            };
            int[] colors = new int[] {
                    Color.argb(0xff, Color.red(colorPrimaryDark.data), Color.green(colorPrimaryDark.data), Color.blue(colorPrimaryDark.data)),
                    Color.argb(0x4d, Color.red(colorPrimaryDark.data), Color.green(colorPrimaryDark.data), Color.blue(colorPrimaryDark.data))
            };
            return new ColorStateList(states, colors);
        } else
            return context.getResources().getColorStateList(R.color.sesl_secondary_text, context.getTheme());
    }
}
