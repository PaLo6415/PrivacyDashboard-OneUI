package rk.android.app.privacydashboard.fragment.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.dlyt.yanndroid.oneui.sesl.recyclerview.LinearLayoutManager;
import de.dlyt.yanndroid.oneui.utils.OnSingleClickListener;
import de.dlyt.yanndroid.oneui.view.RecyclerView;
import de.dlyt.yanndroid.oneui.view.Tooltip;

import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.activity.permission.log.database.LogsRepository;
import rk.android.app.privacydashboard.activity.settings.SettingsActivity;
import rk.android.app.privacydashboard.util.Constants;
import rk.android.app.privacydashboard.databinding.FragmentDashboardBinding;
import rk.android.app.privacydashboard.util.DashboardChart;
import rk.android.app.privacydashboard.util.Dialogs;
import rk.android.app.privacydashboard.util.Permissions;
import rk.android.app.privacydashboard.util.Utils;

public class DashboardFragment extends Fragment {
    private Context context;
    private FragmentDashboardBinding binding;
    private LogsRepository logsRepository;
    private DashboardChart dashboardChart;

    private String date = "01-Jan-2021";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        logsRepository = new LogsRepository(getActivity().getApplication());
        dashboardChart = new DashboardChart(context);
        date = Utils.getDateFromTimestamp(Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (binding != null) {
            binding.permissionTipCard.tipCardTitleText.setText(getString(R.string.oui_permission_tipcard_title));
            binding.permissionTipCard.tipCardSummaryText.setText(getString(R.string.oui_permission_tipcard_summary));
            binding.permissionTipCard.tipCardNegativeBtn.setVisibility(View.GONE);
            binding.permissionTipCard.tipCardPositiveBtn.setText(getString(R.string.oui_permission_tipcard_action));
            binding.permissionTipCard.tipCardPositiveBtn.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    startActivity(new Intent(context, SettingsActivity.class));
                }
            });
            showPermissionTipCard(!checkPermissionsStatus());

            initChartRecyclerView();
            binding.dashboardChart.dashboardChartViewAll.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    //startActivity(new Intent(getActivity(), PermissionUsageActivity.class));
                    Toast.makeText(context, "Still not ready!!", Toast.LENGTH_SHORT).show();
                }
            });

            if (ContextCompat.checkSelfPermission(context, "android.permission.GRANT_RUNTIME_PERMISSIONS") == PackageManager.PERMISSION_GRANTED) {
                binding.permissionDetailsCard.setTitleText(getString(R.string.oui_permission_manager));
            } else {
                binding.permissionDetailsCard.setTitleText(getString(R.string.oui_privacy_settings));
            }
            binding.permissionDetailsCard.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    Utils.openPrivacySettings(context);
                }
            });

            binding.limitationsCard.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    Dialogs.showLimitationsDialog(context);
                }
            });

            binding.dashboardFooter.footerButtonGithub.setOnClickListener(v -> Utils.openLink(context, Constants.LINK_GITHUB));
            Tooltip.setTooltipText(binding.dashboardFooter.footerButtonGithub, getString(R.string.settings_github));
            binding.dashboardFooter.footerButtonTelegram.setOnClickListener(v -> Utils.openLink(context, Constants.LINK_TELEGRAM));
            Tooltip.setTooltipText(binding.dashboardFooter.footerButtonTelegram, getString(R.string.settings_telegram));
            binding.dashboardFooter.footerButtonTwitter.setOnClickListener(v -> Utils.openLink(context,Constants.LINK_TWITTER));
            Tooltip.setTooltipText(binding.dashboardFooter.footerButtonTwitter, getString(R.string.settings_twitter));

            refreshValues();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dashboardChart != null) {
            dashboardChart.cancelGetPermissionInfo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showPermissionTipCard(!checkPermissionsStatus());
        refreshValues();
    }

    private boolean checkPermissionsStatus() {
        return Permissions.checkAccessibility(context) && Permissions.checkLocation(context);
    }

    private void initChartRecyclerView() {
        binding.dashboardChart.dashboardChartRecyclerview.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        binding.dashboardChart.dashboardChartRecyclerview.setAdapter(dashboardChart.getAdapter());
    }

    private void showPermissionTipCard(boolean show) {
        binding.permissionTipCard.getRoot().setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void refreshValues() {
        if (logsRepository != null && dashboardChart != null) {
            List<Integer> logs = new ArrayList<>();
            logs.add(logsRepository.getLogsCount(Constants.PERMISSION_CAMERA, date));
            logs.add(logsRepository.getLogsCount(Constants.PERMISSION_MICROPHONE, date));
            logs.add(logsRepository.getLogsCount(Constants.PERMISSION_LOCATION, date));

            dashboardChart.updateLatestPermissionInfo(logs.get(Constants.POSITION_CAMERA), logs.get(Constants.POSITION_MICROPHONE), logs.get(Constants.POSITION_LOCATION));
        }
    }
}
