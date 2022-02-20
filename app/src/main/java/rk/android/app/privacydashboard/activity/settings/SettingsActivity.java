package rk.android.app.privacydashboard.activity.settings;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.databinding.ActivitySettingsBinding;
import rk.android.app.privacydashboard.fragment.settings.TopSettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;
    private TopSettingsFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        fragment = new TopSettingsFragment();

        setContentView(binding.getRoot());
        initAppBar();
        getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), fragment).commit();
    }

    private void initAppBar() {
        binding.toolbarLayout.setNavigationButtonIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_samsung_back, getTheme()));
        binding.toolbarLayout.setNavigationButtonOnClickListener(view -> onBackPressed());
        binding.toolbarLayout.setNavigationButtonTooltip(getString(R.string.sesl_action_bar_up_description));
        binding.toolbarLayout.setTitle(getString(R.string.oui_app_settings));
    }
}
