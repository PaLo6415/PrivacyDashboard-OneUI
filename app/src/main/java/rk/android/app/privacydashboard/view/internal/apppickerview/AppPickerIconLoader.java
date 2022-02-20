package rk.android.app.privacydashboard.view.internal.apppickerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;

import de.dlyt.yanndroid.oneui.sesl.utils.ReflectUtils;

public class AppPickerIconLoader {
    private Context mContext;
    private PackageManager mPackageManager;

    public AppPickerIconLoader(Context context) {
        mContext = context;
        mPackageManager = context.getPackageManager();
    }

    public void loadIcon(String packageName, ImageView imageView) {
        if (!TextUtils.isEmpty(packageName) && imageView != null) {
            imageView.setTag(packageName);
            new LoadIconTask(new IconInfo(packageName, imageView)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private Drawable getAppIcon(String packageName) {
        Drawable iconForIconTray = semGetApplicationIconForIconTray(mPackageManager, packageName);
        if (iconForIconTray != null) {
            return iconForIconTray;
        }
        try {
            return mPackageManager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @SuppressLint("PrivateApi")
    private Drawable semGetApplicationIconForIconTray(PackageManager pm, String packageName) {
        Class<?> klass;
        String methodName = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ? "hidden_semGetApplicationIconForIconTray" : "semGetApplicationIconForIconTray";

        try {
            klass = Class.forName("android.app.ApplicationPackageManager");
        } catch (ClassNotFoundException e) {
            return null;
        }
        return (Drawable) ReflectUtils.genericInvokeMethod(klass, pm, methodName, packageName, 1);
    }

    public void startIconLoaderThread() {
    }

    public void stopIconLoaderThread() {
    }


    private static class IconInfo {
        Drawable drawable = null;
        ImageView imageView;
        String packageName;

        public IconInfo(String packageName, ImageView imageView) {
            this.packageName = packageName;
            this.imageView = imageView;
        }
    }

    class LoadIconTask extends AsyncTask<Void, Void, Drawable> {
        private final IconInfo mIconInfo;

        LoadIconTask(IconInfo iconInfo) {
            mIconInfo = iconInfo;
        }

        protected Drawable doInBackground(Void... params) {
            return getAppIcon(mIconInfo.packageName);
        }

        protected void onPostExecute(Drawable result) {
            if (mIconInfo != null && mIconInfo.imageView != null) {
                mIconInfo.imageView.setImageDrawable(result);
            }
        }
    }

}
