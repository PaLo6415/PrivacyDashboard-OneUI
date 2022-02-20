package rk.android.app.privacydashboard.fragment.settings.excluded;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rk.android.app.privacydashboard.activity.settings.excluded.database.ExcludedRepository;
import rk.android.app.privacydashboard.databinding.FragmentSettingsExcludeBinding;
import rk.android.app.privacydashboard.model.Apps;
import rk.android.app.privacydashboard.view.AppPickerView;

public class ExcludeSettingsFragment extends Fragment implements AppPickerView.OnBindListener {
    private Context context;
    private FragmentSettingsExcludeBinding binding;
    ExcludedRepository repository;

    private boolean showSystemApps;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        repository = new ExcludedRepository(getActivity().getApplication());
    }

    @Override
    public void onResume() {
        super.onResume();
        fillListView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsExcludeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (binding != null) {
            binding.excludeAppPickerView.setItemAnimator(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onBindViewHolder(AppPickerView.ViewHolder vh, int position, String packageName) {
        CheckBox checkBox = vh.getCheckBox();
        checkBox.setChecked(repository.isExcluded(packageName));
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            Apps app = new Apps(packageName);
            if (b) {
                repository.insert(app);
            } else {
                repository.delete(app);
            }
        });

        View item = vh.getItem();
        item.setOnClickListener(view -> checkBox.setChecked(!checkBox.isChecked()));
    }

    private Set<String> getInstalledPackageNameUnmodifiableSet(Context context) {
        HashSet<String> set = new HashSet<>();
        for (ApplicationInfo appInfo : getInstalledAppList(context)) {
            set.add(appInfo.packageName);
        }
        return Collections.unmodifiableSet(set);
    }

    private List<ApplicationInfo> getInstalledAppList(Context context) {
        ArrayList<ApplicationInfo> list = new ArrayList<>();
        List<ApplicationInfo> apps = context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo appInfo : apps) {
            if ((appInfo.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP | ApplicationInfo.FLAG_SYSTEM)) > 0 && !showSystemApps) {
                continue;
            }
            list.add(appInfo);
        }
        return list;
    }

    public boolean isShowSystemApps() {
        return showSystemApps;
    }

    public void setShowSystemApps(boolean show) {
        showSystemApps = show;

        showProgressCircle(true);
        new Thread() {
            @Override
            public void run() {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(() -> {
                        ArrayList<String> installedAppSet = new ArrayList<>(getInstalledPackageNameUnmodifiableSet(activity));
                        binding.excludeAppPickerView.resetPackages(installedAppSet);
                        showProgressCircle(false);
                    });
                }
            }
        }.start();
    }

    private void fillListView() {
        showProgressCircle(true);
        new Thread() {
            @Override
            public void run() {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(() -> {
                        ArrayList<String> installedAppSet = new ArrayList<>(getInstalledPackageNameUnmodifiableSet(activity));
                        binding.excludeAppPickerView.setAppPickerView(AppPickerView.TYPE_LIST_CHECKBOX, installedAppSet, AppPickerView.ORDER_ASCENDING_IGNORE_CASE);
                        binding.excludeAppPickerView.setOnBindListener(ExcludeSettingsFragment.this);
                        showProgressCircle(false);
                    });
                }
            }
        }.start();
    }

    private void showProgressCircle(boolean show) {
        binding.excludeProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.excludeAppPickerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
