package rk.android.app.privacydashboard.activity.permission;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import de.dlyt.yanndroid.oneui.sesl.recyclerview.LinearLayoutManager;
import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.activity.permission.log.LogsViewModel;
import rk.android.app.privacydashboard.activity.permission.log.adapter.LogsAdapter;
import rk.android.app.privacydashboard.util.Constants;
import rk.android.app.privacydashboard.databinding.ActivityPermissionDetailBinding;
import rk.android.app.privacydashboard.util.Dialogs;
import rk.android.app.privacydashboard.util.Permissions;

public class PermissionDetailActivity extends AppCompatActivity {
    private String permission;
    private String permission_name;
    private Context context;
    private ActivityPermissionDetailBinding binding;
    private LogsViewModel logsViewModel;
    private LogsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = PermissionDetailActivity.this;
        binding = ActivityPermissionDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        initAppBar();
        initValues();
    }

    private void initAppBar() {
        binding.toolbarLayout.setNavigationButtonIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_samsung_back, getTheme()));
        binding.toolbarLayout.setNavigationButtonOnClickListener(view -> onBackPressed());
        binding.toolbarLayout.setNavigationButtonTooltip(getString(R.string.sesl_action_bar_up_description));
        binding.toolbarLayout.setTitle(getString(R.string.oui_log_title));
        binding.toolbarLayout.inflateToolbarMenu(R.menu.oui_detail_menu);
        binding.toolbarLayout.getToolbarMenu().getItem(0).setVisible(false);
        binding.toolbarLayout.setOnToolbarMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.logs_delete) {
                Dialogs.deleteLogs(PermissionDetailActivity.this, context, getString(R.string.oui_details_delete_logs_title, permission_name), getString(R.string.oui_details_delete_logs_text, permission_name), permission);
                return true;
            }
            return false;
        });
    }

    private void initValues() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.EXTRA_PERMISSION)) {
                permission = bundle.getString(Constants.EXTRA_PERMISSION);
                permission_name = Permissions.getName(context, permission);
            }
        }

        logsViewModel = new ViewModelProvider(this).get(LogsViewModel.class);
        binding.detailRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new LogsAdapter(context, permission);
        binding.detailRecyclerView.setAdapter(adapter);
        binding.detailRecyclerView.setItemAnimator(null);

        logsViewModel.getLogs(permission).observe(this, logs -> {
            if (logs.isEmpty()) {
                binding.toolbarLayout.setExpanded(false);
                binding.toolbarLayout.getToolbarMenu().getItem(1).setEnabled(false);
                binding.detailRecyclerView.setVisibility(View.GONE);
                binding.noItemsLayout.setVisibility(View.VISIBLE);
                binding.noItemTitleText.setText(permission_name);
                binding.noItemSubText.setText(getString(R.string.oui_app_detail_no_items));
            } else {
                binding.noItemsLayout.setVisibility(View.GONE);
                binding.detailRecyclerView.setVisibility(View.VISIBLE);
                adapter.setLogsList(logs);
            }
        });
    }
}
