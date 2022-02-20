package rk.android.app.privacydashboard.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.activity.appinfo.OldAppInfoActivity;
import rk.android.app.privacydashboard.activity.permission.PermissionDetailActivity;

public class Utils {

    public static void openHistoryActivity(Context context, String permission) {
        Intent i = new Intent(context, PermissionDetailActivity.class);
        i.putExtra(Constants.EXTRA_PERMISSION, permission);
        context.startActivity(i);
    }

    public static void openAppInfoActivity(Context context, String packageName) {
        Intent i = new Intent(context, OldAppInfoActivity.class);
        i.putExtra(Constants.EXTRA_APP, packageName);
        context.startActivity(i);
    }

    public static void openAppPermissionsActivity(Context context, String packageName) {
        try {
            Intent intent = new Intent("android.intent.action.MANAGE_APP_PERMISSIONS");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("android.intent.extra.PACKAGE_NAME", packageName);
            intent.putExtra("hideInfoButton", true);
            context.startActivity(intent);
        } catch (SecurityException e) {
            context.startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", packageName, null)));
        }
    }

    public static void openPrivacySettings(Context context) {
        try {
            context.startActivity(new Intent("android.intent.action.MANAGE_PERMISSIONS"));
        } catch (SecurityException e) {
            context.startActivity(new Intent(Settings.ACTION_PRIVACY_SETTINGS));
        }
    }

    public static void openPermissionLog(Context context, String permission){
        Intent i = new Intent(context, PermissionDetailActivity.class);
        i.putExtra(Constants.EXTRA_PERMISSION,permission);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static void openAppSettings(Context context, String packageName){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

    public static void openLink(Context context, String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No suitable activity found", Toast.LENGTH_SHORT).show();
        }
    }

    public static void setTheme(AppCompatActivity activity, int mode) {
        AppCompatDelegate.setDefaultNightMode(mode);
        activity.getDelegate().applyDayNight();
    }

    public static int getAttrColor(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    public static String getTimeFromTimestamp(Context context, long timestamp){
        if (DateFormat.is24HourFormat(context)) {
            return DateFormat.format("HH:mm", new Date(timestamp)).toString();
        }else {
            return DateFormat.format("hh:mm a", new Date(timestamp)).toString();
        }
    }

    public static String getDateFromTimestamp(Context context, long timestamp){

        Calendar calendar = Calendar.getInstance();
        String today = DateFormat.format("dd MMM", calendar.getTimeInMillis()).toString();

        calendar.add(Calendar.DATE,-1);
        String yesterday = DateFormat.format("dd MMM", calendar.getTimeInMillis()).toString();

        String date = DateFormat.format("dd MMM", new Date(timestamp)).toString();
        if (today.equals(date)) {
            return context.getString(R.string.log_today);
        }

        if (yesterday.equals(date)){
            return context.getString(R.string.log_yesterday);
        }

        return date;
    }

    public static String getDateFromTimestamp(long timestamp){
        return DateFormat.format("dd-MMM-yyyy", new Date(timestamp)).toString();
    }

    public static String getNameFromPackageName(Context context, String packageName){
        final PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
    }

    public static Bitmap getAppIcon(Context context, String packageName){
        Drawable drawable = getIconFromPackageName(context, packageName);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable getIconFromPackageName(Context context, String packageName){
        try {
            Drawable drawable = context.getPackageManager().getApplicationIcon(packageName);
            if (drawable!=null)
                return drawable;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return ResourcesCompat.getDrawable(context.getResources(),R.mipmap.ic_launcher,context.getTheme());
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static List<String> getSystemApps(Context context) {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        List<String> helpPackages = new ArrayList<>();
        helpPackages.add("com.android.systemui");
        helpPackages.add("com.android.settings");
        if (resolveInfo!=null) {
            helpPackages.add(resolveInfo.activityInfo.packageName);
        }

        return helpPackages;
    }
}
