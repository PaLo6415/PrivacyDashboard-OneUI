package rk.android.app.privacydashboard.activity.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.activity.settings.SettingsActivity;
import rk.android.app.privacydashboard.databinding.ActivityDashboardBinding;
import rk.android.app.privacydashboard.fragment.dashboard.DashboardFragment;
import rk.android.app.privacydashboard.manager.PreferenceManager;
import rk.android.app.privacydashboard.service.PrivacyService;
import rk.android.app.privacydashboard.util.Dialogs;
import rk.android.app.privacydashboard.util.Permissions;

public class DashboardActivity extends AppCompatActivity {
    private Context context;
    PreferenceManager preferenceManager;
    private ActivityDashboardBinding binding;
    private DashboardFragment fragment;
    Intent serviceIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = DashboardActivity.this;
        preferenceManager =  new PreferenceManager(getApplicationContext());
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        fragment = new DashboardFragment();

        setContentView(binding.getRoot());

        initAppBar();

        getSupportFragmentManager().beginTransaction().replace(binding.fragmentContainer.getId(), fragment).commit();

        Dialogs.showWhatsNewDialog(context, preferenceManager, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForAccessibilityAndStart();
    }

    private void initAppBar() {
        binding.toolbarLayout.setTitle(getString(R.string.app_name));
        binding.toolbarLayout.inflateToolbarMenu(R.menu.oui_dashboard_menu);
        binding.toolbarLayout.setOnToolbarMenuItemClickListener(menuItem -> {
            startActivity(new Intent(context, SettingsActivity.class));
            return true;
        });
    }

    private void checkForAccessibilityAndStart() {
        if (!Permissions.accessibilityPermission(getApplicationContext(), PrivacyService.class)) {
            serviceIntent = new Intent(DashboardActivity.this, PrivacyService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        }
    }
}
