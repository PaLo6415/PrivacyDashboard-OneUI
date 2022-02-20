package rk.android.app.privacydashboard.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.activity.appinfo.OldAppInfoActivity;
import rk.android.app.privacydashboard.activity.donation.DonationActivity;
import rk.android.app.privacydashboard.activity.permission.log.database.LogsRepository;
import rk.android.app.privacydashboard.manager.PreferenceManager;

public class Dialogs {
    public static void showWhatsNewDialog(Context context, PreferenceManager preferenceManager, boolean force) {
        int currentVersionNumber = 1;

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersionNumber  = packageInfo.versionCode;
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        if (currentVersionNumber > preferenceManager.getVersionNumber() || force) {
            preferenceManager.setVersionNumber(currentVersionNumber);

            new de.dlyt.yanndroid.oneui.dialog.AlertDialog.Builder(context)
                    .setTitle(R.string.app_release_title)
                    .setMessage(R.string.app_release)
                    .setCancelable(true)
                    .setPositiveButton(R.string.app_release_button, (dialog, which) -> dialog.dismiss())
                    .setNeutralButton(R.string.profile_donate, (dialog, which) -> context.startActivity(new Intent(context, DonationActivity.class)))
                    .setNegativeButton(R.string.profile_github, (dialog, which) -> Utils.openLink(context, Constants.LINK_GITHUB))
                    .show();
        }
    }

    public static void showPermissionsHelpDialog(Context context) {
        int[] permIcons = {R.drawable.oui_icon_perm_group_camera, R.drawable.oui_icon_perm_group_location, R.drawable.oui_icon_perm_group_microphone};
        int[] permName = {R.string.oui_camera, R.string.oui_location, R.string.oui_microphone};
        int[] permDesc = {R.string.oui_permission_help_camera_description, R.string.oui_permission_help_location_description, R.string.oui_permission_help_microphone_description};

        View dialogContent = LayoutInflater.from(context).inflate(R.layout.oui_dialog_permission_help_layout, null);
        LinearLayout container = dialogContent.findViewById(R.id.container);
        container.removeAllViews();

        for (int i = 0; i < permName.length; i++) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.oui_dialog_permission_help_item, null);
            ImageView icon = itemView.findViewById(R.id.icon);
            TextView name = itemView.findViewById(R.id.subtitle);
            TextView description = itemView.findViewById(R.id.description);

            icon.setImageDrawable(context.getDrawable(permIcons[i]));
            name.setText(context.getString(permName[i]));
            description.setText(context.getString(permDesc[i]));

            container.addView(itemView);
        }

        new de.dlyt.yanndroid.oneui.dialog.AlertDialog.Builder(context)
                .setTitle(R.string.oui_permission_help_title)
                .setView(dialogContent)
                .setPositiveButton(R.string.oui_ok, (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static void showLimitationsDialog(Context context) {
        new de.dlyt.yanndroid.oneui.dialog.AlertDialog.Builder(context)
                .setTitle(R.string.oui_limitations_help_title)
                .setMessage(R.string.oui_limitations_help_description)
                .setCancelable(true)
                .setPositiveButton(R.string.oui_close, (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static void deleteLogs(Activity activity, Context context, String title, String info, String permission) {
        new de.dlyt.yanndroid.oneui.dialog.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(info)
                .setCancelable(true)
                .setPositiveButton(R.string.oui_yes, (dialog, which) -> {
                    LogsRepository logsRepository = new LogsRepository(activity.getApplication());
                    if (permission != null) {
                        logsRepository.clearLogs(permission);
                    }else {
                        logsRepository.clearLogs();
                    }
                    dialog.dismiss();
                })
                .setPositiveButtonColor(context.getColor(R.color.sesl_functional_red))
                .setPositiveButtonProgress(true)
                .setNegativeButton(R.string.oui_no, (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static void deleteAppLogs(Activity activity, Context context, String title, String info, String packageName) {
        new de.dlyt.yanndroid.oneui.dialog.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(info)
                .setCancelable(true)
                .setPositiveButton(R.string.oui_yes, (dialog, which) -> {
                    LogsRepository logsRepository = new LogsRepository(activity.getApplication());
                    if (packageName != null) {
                        logsRepository.clearAppLogs(packageName);
                    }else {
                        logsRepository.clearLogs();
                    }
                    dialog.dismiss();
                })
                .setPositiveButtonColor(context.getColor(R.color.sesl_functional_red))
                .setPositiveButtonProgress(true)
                .setNegativeButton(R.string.oui_no, (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static void excludeApp(Context context, String title, String info, String packageName, OldAppInfoActivity.OnDialogSubmit listener) {
        new de.dlyt.yanndroid.oneui.dialog.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(info)
                .setCancelable(true)
                .setPositiveButton(R.string.oui_yes, (dialog, which) -> {
                    if (packageName != null) {
                        listener.OnSubmit();
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.oui_no, (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static void showExcludeType(Context context, PreferenceManager preferenceManager) {
        CharSequence[] values = {context.getString(R.string.oui_settings_exclude_type_indicator),
                context.getString(R.string.oui_settings_exclude_type_notification),
                context.getString(R.string.oui_settings_exclude_type_logs)};
        boolean[] checkedValues = {preferenceManager.isPrivacyExcludeIndicator(),
                preferenceManager.isPrivacyExcludeNotification(),
                preferenceManager.isPrivacyExcludeLogs()};

        new de.dlyt.yanndroid.oneui.dialog.AlertDialog.Builder(context)
                .setTitle(R.string.oui_excluded_apps_type)
                .setMultiChoiceItems(values, checkedValues, (dialog, which, isChecked) -> {
                    switch (which) {
                        case 0:
                            checkedValues[0] = isChecked;
                            break;
                        case 1:
                            checkedValues[1] = isChecked;
                            break;
                        case 2:
                            checkedValues[2] = isChecked;
                            break;
                    }
                })
                .setCancelable(true)
                .setNegativeButton(R.string.oui_close, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.oui_done, (dialog, which) -> {
                    preferenceManager.setPrivacyExcludeIndicator(checkedValues[0]);
                    preferenceManager.setPrivacyExcludeNotification(checkedValues[1]);
                    preferenceManager.setPrivacyExcludeLogs(checkedValues[2]);
                    dialog.dismiss();
                })
                .show();
    }
}
