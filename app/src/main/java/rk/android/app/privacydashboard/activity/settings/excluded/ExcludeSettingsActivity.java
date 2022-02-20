package rk.android.app.privacydashboard.activity.settings.excluded;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.databinding.ActivitySettingsSwitchbarBinding;
import rk.android.app.privacydashboard.fragment.settings.excluded.ExcludeSettingsFragment;
import rk.android.app.privacydashboard.manager.PreferenceManager;
import rk.android.app.privacydashboard.util.Dialogs;

public class ExcludeSettingsActivity extends AppCompatActivity {
    private Context context;
    private ActivitySettingsSwitchbarBinding binding;
    private PreferenceManager preferenceManager;
    private ExcludeSettingsFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = ExcludeSettingsActivity.this;
        binding = ActivitySettingsSwitchbarBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(context);
        fragment = new ExcludeSettingsFragment();

        setContentView(binding.getRoot());
        initAppBar();
        getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), fragment).commit();
        initSwitchBar();
    }

    private void initAppBar() {
        binding.switchBarLayout.setNavigationButtonIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_samsung_back, getTheme()));
        binding.switchBarLayout.setNavigationButtonOnClickListener(view -> onBackPressed());
        binding.switchBarLayout.setNavigationButtonTooltip(getString(R.string.sesl_action_bar_up_description));
        binding.switchBarLayout.setTitle(getString(R.string.oui_excluded_apps));
        binding.switchBarLayout.inflateToolbarMenu(R.menu.oui_excluded_apps_menu);
        binding.switchBarLayout.setOnToolbarMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.exclude_type:
                    Dialogs.showExcludeType(context, preferenceManager);
                    return true;
                case R.id.show_system_apps:
                    if (fragment.isShowSystemApps()) {
                        fragment.setShowSystemApps(false);
                        menuItem.setTitle(getString(R.string.oui_show_system_apps));
                    } else {
                        fragment.setShowSystemApps(true);
                        menuItem.setTitle(getString(R.string.oui_hide_system_apps));
                    }
                    return true;
            }
            return false;
        });
    }

    private void initSwitchBar() {
        binding.switchBarLayout.setChecked(preferenceManager.isPrivacyExcluded());
        binding.switchBarLayout.getToolbarMenu().getItem(0).setEnabled(binding.switchBarLayout.getSwitchBar().isChecked());
        binding.switchBarLayout.addOnSwitchChangeListener((switchCompat, checked) -> {
            binding.switchBarLayout.getToolbarMenu().getItem(0).setEnabled(checked);
            preferenceManager.setPrivacyExcluded(checked);
        });
    }
}
