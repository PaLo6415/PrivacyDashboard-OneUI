package rk.android.app.privacydashboard.activity.settings.indicator;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.databinding.ActivitySettingsSwitchbarBinding;
import rk.android.app.privacydashboard.fragment.settings.indicator.IndicatorSettingsFragment;
import rk.android.app.privacydashboard.manager.PreferenceManager;

public class IndicatorSettingsActivity extends AppCompatActivity {
    private Context context;
    private ActivitySettingsSwitchbarBinding binding;
    private PreferenceManager preferenceManager;
    private IndicatorSettingsFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = IndicatorSettingsActivity.this;
        binding = ActivitySettingsSwitchbarBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(context);
        fragment = new IndicatorSettingsFragment();

        setContentView(binding.getRoot());
        initAppBar();
        getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), fragment).commit();
        initSwitchBar();
    }

    private void initAppBar() {
        binding.switchBarLayout.setNavigationButtonIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_samsung_back, getTheme()));
        binding.switchBarLayout.setNavigationButtonOnClickListener(view -> onBackPressed());
        binding.switchBarLayout.setNavigationButtonTooltip(getString(R.string.sesl_action_bar_up_description));
        binding.switchBarLayout.setTitle(getString(R.string.oui_settings_privacy_dots_title));
    }

    private void initSwitchBar() {
        binding.switchBarLayout.setChecked(preferenceManager.isPrivacyDots());
        binding.switchBarLayout.addOnSwitchChangeListener((switchCompat, checked) -> {
            preferenceManager.setPrivacyDots(checked);
            fragment.setChildPrefsEnabled(checked);
        });
    }
}
