package rk.android.app.privacydashboard.util;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityManager;

import androidx.core.app.ActivityCompat;

import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.service.PrivacyService;

public class Permissions {
    public static String getString(Context context, int position) {
        switch (position) {
            case Constants.POSITION_LOCATION:
                return context.getString(R.string.oui_location);
            case Constants.POSITION_CAMERA:
                return context.getString(R.string.oui_camera);
            case Constants.POSITION_MICROPHONE:
                return context.getString(R.string.oui_microphone);
        }

        return "";
    }

    public static String getName(Context context, String permission) {
        switch (permission) {
            case Constants.PERMISSION_LOCATION:
                return context.getString(R.string.oui_location);
            case Constants.PERMISSION_CAMERA:
                return context.getString(R.string.oui_camera);
            case Constants.PERMISSION_MICROPHONE:
                return context.getString(R.string.oui_microphone);
        }

        return "";
    }

    public static int getIcon(String permission) {
        switch (permission) {
            case Constants.PERMISSION_LOCATION:
                return R.drawable.oui_icon_perm_group_location;
            case Constants.PERMISSION_CAMERA:
                return R.drawable.oui_icon_perm_group_camera;
            case Constants.PERMISSION_MICROPHONE:
                return R.drawable.oui_icon_perm_group_microphone;
        }

        return -1;
    }

    public static String getPermissionUsageInfo(Context context, int apps){
        if (apps != 0) {
            return context.getString(R.string.permission_info)
                    .replace("#ALIAS#",String.valueOf(apps));
        }else {
            return context.getString(R.string.permission_no_apps);
        }
    }

    public static int getColor(Context context, String permission) {
        switch (permission){
            case Constants.PERMISSION_LOCATION:
                return context.getColor(R.color.oui_icon_location_color);
            case Constants.PERMISSION_CAMERA:
                return context.getColor(R.color.oui_icon_camera_color);
            case Constants.PERMISSION_MICROPHONE:
                return context.getColor(R.color.oui_icon_microphone_color);
        }

        return Color.BLACK;
    }

    public static boolean accessibilityPermission(Context context, Class<?> cls) {
        ComponentName componentName = new ComponentName(context, cls);
        String string = Settings.Secure.getString(context.getContentResolver(), "enabled_accessibility_services");
        if (string == null) {
            return false;
        }
        TextUtils.SimpleStringSplitter simpleStringSplitter = new TextUtils.SimpleStringSplitter(':');
        simpleStringSplitter.setString(string);
        while (simpleStringSplitter.hasNext()) {
            ComponentName unflattenFromString = ComponentName.unflattenFromString(simpleStringSplitter.next());
            if (unflattenFromString != null && unflattenFromString.equals(componentName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkAccessibility(Context context) {
        AccessibilityManager manager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        return manager.isEnabled();
    }

    public static boolean checkLocation(Context context) {
        return (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestAccessibilityPermission(Activity activity) {
        /*try {
            Intent res = new Intent();
            String mPackage = "com.samsung.accessibility";
            String mClass = "com.samsung.accessibility.Activities$InstalledServicesActivity";
            res.setComponent(new ComponentName(mPackage,mClass));
            activity.startActivity(res);
        } catch (Exception e) {
            Log.e("perm", e.getMessage());
        }*/
        Intent intent = new Intent(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ? "com.samsung.accessibility.INSTALLED_SERVICES" : "com.samsung.accessibility.installed_service");
        if (intent.resolveActivity(activity.getPackageManager()) == null) {
            intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        }
        activity.startActivity(intent);
    }

    public static void requestLocationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 0);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    public static boolean isAccessibilityServiceRunning(Context context) {
        String prefString = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        return prefString != null && prefString.contains(context.getPackageName() + "/" + PrivacyService.class.getName());
    }

}
