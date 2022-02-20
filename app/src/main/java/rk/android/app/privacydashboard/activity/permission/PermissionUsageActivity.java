package rk.android.app.privacydashboard.activity.permission;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import de.dlyt.yanndroid.oneui.layout.ToolbarLayout;
import de.dlyt.yanndroid.oneui.menu.MenuItem;
import de.dlyt.yanndroid.oneui.sesl.tabs.TabLayoutMediator;
import de.dlyt.yanndroid.oneui.sesl.viewpager2.adapter.FragmentStateAdapter;

import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.databinding.ActivityPermissionUsageBinding;
import rk.android.app.privacydashboard.fragment.permission.PermissionListTabFragment;
import rk.android.app.privacydashboard.util.Dialogs;

public class PermissionUsageActivity extends AppCompatActivity {
    private Context context;
    private ActivityPermissionUsageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = PermissionUsageActivity.this;
        binding = ActivityPermissionUsageBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        initAppBar();
        initTabs();
    }

    private void initAppBar() {
        binding.toolbarLayout.setNavigationButtonIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_samsung_back, getTheme()));
        binding.toolbarLayout.setNavigationButtonOnClickListener(view -> onBackPressed());
        binding.toolbarLayout.setNavigationButtonTooltip(getString(R.string.sesl_action_bar_up_description));
        binding.toolbarLayout.setTitle("Permission usage");
        binding.toolbarLayout.inflateToolbarMenu(R.menu.oui_permission_usage_menu);
        binding.toolbarLayout.setOnToolbarMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.info_detail) {
                Dialogs.showPermissionsHelpDialog(context);
                return true;
            }
            return false;
        });
    }

    private void initTabs() {
        binding.tabs.seslSetSubTabStyle();
        binding.viewPager.setAdapter(new ViewPagerAdapter(this));

        new TabLayoutMediator(binding.tabs,
                binding.viewPager,
                (tab, position) -> {
                    tab.setText(getString(position == 0 ? R.string.oui_permissions : R.string.oui_apps));
                })
                .attach();
    }


    public class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int i) {
            return new PermissionListTabFragment();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
