package rk.android.app.privacydashboard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.dlyt.yanndroid.oneui.sesl.support.WindowManagerSupport;
import de.dlyt.yanndroid.oneui.utils.OnSingleClickListener;
import rk.android.app.privacydashboard.BuildConfig;
import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.activity.dashboard.DashboardActivity;
import rk.android.app.privacydashboard.databinding.ActivityMainBinding;
import rk.android.app.privacydashboard.manager.PreferenceManager;
import rk.android.app.privacydashboard.util.Permissions;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private PreferenceManager preferenceManager;
    private ActivityMainBinding binding;

    private boolean isFirstLaunch;
    private boolean launchCanceled = false;
    private boolean isLocationGranted = false;
    private boolean isAccessibilityGranted = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = MainActivity.this;
        preferenceManager = new PreferenceManager(context);

        isFirstLaunch = preferenceManager.isFirstLaunch();

        if (!BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            startActivity(new Intent(Settings.ACTION_PRIVACY_SETTINGS));
            finish();
        }

        refreshPermissionsStatus();
        if (!isFirstLaunch || isLocationGranted && isAccessibilityGranted) {
            launchDashboard();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        launchCanceled = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        launchCanceled = false;

        if (!isFirstLaunch && binding == null) {
            launchDashboard();
        }

        refreshPermissionsStatus();
        binding.permissionLocationCard.setEnabled(!isLocationGranted);
        binding.permissionAccessibilityCard.setEnabled(!isAccessibilityGranted);
    }

    private void launchDashboard() {
        if (!launchCanceled) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }
    }

    private void refreshPermissionsStatus() {
        if (context != null) {
            isLocationGranted = Permissions.checkLocation(context);
            isAccessibilityGranted = Permissions.checkAccessibility(context);
        }
    }

    private void initViews() {
        WindowManagerSupport.hideStatusBarForLandscape(this, getResources().getConfiguration().orientation);

        binding.permissionLocationCard.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Permissions.requestLocationPermission(MainActivity.this);
            }
        });
        binding.permissionLocationCard.setEnabled(!isLocationGranted);
        binding.permissionAccessibilityCard.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Permissions.requestAccessibilityPermission(MainActivity.this);
            }
        });
        binding.permissionAccessibilityCard.setEnabled(!isAccessibilityGranted);

        binding.permissionFooterButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                preferenceManager.setFirstLaunch(false);
                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                finish();
            }
        });
    }
}
