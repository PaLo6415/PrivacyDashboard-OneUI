package rk.android.app.privacydashboard.activity.about;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.button.MaterialButton;

import rk.android.app.privacydashboard.BuildConfig;
import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {
    private Context context;
    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = AboutActivity.this;

        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initAppBar();
        initViews();

        setOrientationLayout();
        setTextSize();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setButtonsWidth();
        setOrientationLayout();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setButtonsWidth();
        }
    }

    private void initAppBar() {
        binding.toolbarLayout.findViewById(R.id.toolbar_layout_app_bar).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.splash_background, getTheme()));
        binding.toolbarLayout.findViewById(R.id.toolbar_layout_collapsing_toolbar_layout).setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.splash_background, getTheme()));

        binding.toolbarLayout.setNavigationButtonIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_samsung_back, getTheme()));
        binding.toolbarLayout.setNavigationButtonOnClickListener(view -> onBackPressed());
        binding.toolbarLayout.setNavigationButtonTooltip(getString(R.string.sesl_action_bar_up_description));

        binding.toolbarLayout.inflateToolbarMenu(R.menu.oui_about_page);
        binding.toolbarLayout.getToolbarMenu().findItem(R.id.app_info).setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_samsung_info_2, getTheme()));
        binding.toolbarLayout.setOnToolbarMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.app_info) {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void initViews() {
        binding.appNameTextView.setText(getString(R.string.app_name));
        binding.oneUiTextView.setText("OneUI");

        String version = "1.0";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (Exception ignored) { }

        binding.appVersionTextView.setText(getString(R.string.sesl_version) + " " + version);

        binding.detailsButtonPortrait.setText(getString(R.string.oui_aboutpage_details_button));
        binding.detailsButtonLandscape.setText(getString(R.string.oui_aboutpage_details_button));
    }

    private void setButtonsWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            displayWidth /= 2;
        }

        final MaterialButton btnPortrait = binding.detailsButtonPortrait;
        final MaterialButton btnLandscape = binding.detailsButtonLandscape;

        int newWidth = btnPortrait.getMeasuredWidth();

        double maxWidth = displayWidth * 0.75d;
        if (newWidth > maxWidth) {
            newWidth = (int) maxWidth;
        }

        double minWidth = displayWidth * 0.6d;
        if (newWidth < minWidth) {
            newWidth = (int) minWidth;
        }

        btnPortrait.setWidth(newWidth);
        btnLandscape.setWidth(newWidth);
    }

    private void setOrientationLayout() {
        final boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        final double displayHeight = (double) getResources().getDisplayMetrics().heightPixels;

        final double topMiddleSpacing = isPortrait ? 0.07d : 0.036d;
        final double bottomSpacing = isPortrait ? 0.05d : 0.036d;

        binding.topSpacing.getLayoutParams().height = (int) (displayHeight * topMiddleSpacing);
        binding.middleSpacing.getLayoutParams().height = (int) (displayHeight * topMiddleSpacing);
        binding.bottomSpacing.getLayoutParams().height = (int) (displayHeight * bottomSpacing);

        if (isPortrait) {
            binding.aboutPageContainer.setOrientation(LinearLayout.VERTICAL);
            binding.aboutPageContent.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            binding.buttonsContainerPortrait.setVisibility(View.VISIBLE);
            binding.buttonsContainerLandscape.setVisibility(View.GONE);
        } else {
            binding.aboutPageContainer.setOrientation(LinearLayout.HORIZONTAL);
            ((LinearLayout.LayoutParams) binding.aboutPageContent.getLayoutParams()).weight = 5.0f;
            ((LinearLayout.LayoutParams) binding.buttonsContainerLandscape.getLayoutParams()).weight = 5.0f;
            binding.aboutPageContent.setGravity(Gravity.CENTER);
            binding.appInfoContainer.setGravity(Gravity.CENTER);
            binding.buttonsContainerPortrait.setVisibility(View.GONE);
            binding.buttonsContainerLandscape.setVisibility(View.VISIBLE);
            binding.buttonsContainerLandscape.setGravity(Gravity.CENTER);
        }
    }

    private void setTextSize() {
        setLargeTextSize(binding.appNameTextView, getResources().getDimension(R.dimen.oui_aboutpage_app_name_text_size));
        setLargeTextSize(binding.oneUiTextView, getResources().getDimension(R.dimen.oui_aboutpage_oneui_text_size));
        setLargeTextSize(binding.appVersionTextView, getResources().getDimension(R.dimen.oui_aboutpage_secondary_text_size));
        setLargeTextSize(binding.detailsButtonPortrait, getResources().getDimension(R.dimen.oui_aboutpage_button_text_size));
        setLargeTextSize(binding.detailsButtonLandscape, getResources().getDimension(R.dimen.oui_aboutpage_button_text_size));
    }

    private void setLargeTextSize(TextView textView, float size) {
        if (textView != null) {
            float scale = 1.3f;
            final float systemScale = context.getResources().getConfiguration().fontScale;

            if (systemScale <= scale) {
                scale = systemScale;
            }

            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) Math.ceil((size / systemScale) * scale));
        }
    }
}
