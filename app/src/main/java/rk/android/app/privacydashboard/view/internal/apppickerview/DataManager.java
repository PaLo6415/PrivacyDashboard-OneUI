package rk.android.app.privacydashboard.view.internal.apppickerview;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rk.android.app.privacydashboard.view.AppPickerView;

class DataManager {
    public static List<AppPickerView.AppLabelInfo> resetPackages(Context context, List<String> packagesList, List<AppPickerView.AppLabelInfo> appLabelInfos) {
        HashMap<String, String> map;
        if (appLabelInfos != null) {
            map = new HashMap<>();
            for (AppPickerView.AppLabelInfo appLabelInfo : appLabelInfos) {
                map.put(appLabelInfo.getPackageName(), appLabelInfo.getLabel());
            }
        } else {
            map = null;
        }

        ArrayList<AppPickerView.AppLabelInfo> list = new ArrayList<>();
        for (String str : packagesList) {
            String str2 = map != null ? (String) map.get(str) : null;
            if (str2 == null) {
                str2 = getLabelFromPackageManager(context, str);
            }
            list.add(new AppPickerView.AppLabelInfo(str, str2));
        }
        return list;
    }

    private static String getLabelFromPackageManager(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                return (String) packageManager.getApplicationLabel(applicationInfo);
            }
            return "Unknown";
        } catch (PackageManager.NameNotFoundException unused) {
            Log.i("DataManager", "can't find label for " + packageName);
            return "Unknown";
        }
    }
}
