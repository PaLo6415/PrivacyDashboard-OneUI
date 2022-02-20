package rk.android.app.privacydashboard.fragment.settings.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.dlyt.yanndroid.oneui.preference.InsetPreferenceCategory;
import de.dlyt.yanndroid.oneui.preference.Preference;
import de.dlyt.yanndroid.oneui.preference.PreferenceFragmentCompat;

import rk.android.app.privacydashboard.BuildConfig;
import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.util.Constants;
import rk.android.app.privacydashboard.manager.PreferenceManager;
import rk.android.app.privacydashboard.util.Utils;

public class NotificationSettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {
    private Context context;
    private PreferenceManager preferenceManager;

    private InsetPreferenceCategory notiSettingsPrefs;
    private Preference appNotiSettingsPref;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        preferenceManager = new PreferenceManager(context);
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        addPreferencesFromResource(R.xml.oui_notification_settings);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initPreferences();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (notiSettingsPrefs != null) {
            setChildPrefsEnabled(preferenceManager.isPrivacyNotification());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.item_background_color, context.getTheme()));
        getListView().seslSetLastRoundedCorner(false);
    }

    private void initPreferences() {
        notiSettingsPrefs = findPreference("category_0");

        appNotiSettingsPref = findPreference("privacy.notification.more");
        appNotiSettingsPref.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(@NonNull Preference preference) {
        switch (preference.getKey()) {
            case "privacy.notification.more":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startActivity(new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, BuildConfig.APPLICATION_ID)
                            .putExtra(Settings.EXTRA_CHANNEL_ID, Constants.PERMISSION_NOTIFICATION_CHANNEL));
                } else {
                    Utils.openAppSettings(context, BuildConfig.APPLICATION_ID);
                }
                return true;
        }
        return false;
    }

    public void setChildPrefsEnabled(boolean enabled) {
        notiSettingsPrefs.setEnabled(enabled);
    }
}
