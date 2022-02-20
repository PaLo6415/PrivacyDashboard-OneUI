package rk.android.app.privacydashboard.util;

import rk.android.app.privacydashboard.BuildConfig;

public class Constants {
    public static final String NOTIFICATION_CHANNEL = "Privacy Service";
    public static final String PERMISSION_NOTIFICATION_CHANNEL = "Permission Usage Notification";
    public static final int NOTIFICATION_ID = 101;

    public static final String SHARED_PREFERENCE_NAME = BuildConfig.APPLICATION_ID + "_preferences";

    public static final String LINK_GITHUB = "https://github.com/BlackMesa123/PrivacyDashboard";
    public static final String LINK_TELEGRAM = "https://t.me/privacy_dashboard";
    public static final String LINK_TWITTER = "https://twitter.com/RushikeshDesign";

    public static final int SCROLL_DIRECTION_UP = -1;
    public static final int TOOLBAR_SCROLL_ELEVATION = 8;
    public static final int TOOLBAR_DEFAULT_ELEVATION = 0;

    public static final String EXTRA_APP = "app.extra";
    public static final String EXTRA_PERMISSION = "permission.extra";
    public static final String PERMISSION_CAMERA = "permission.camera";
    public static final String PERMISSION_MICROPHONE = "permission.microphone";
    public static final String PERMISSION_LOCATION = "permission.location";

    public static final int POSITION_CAMERA = 0;
    public static final int POSITION_MICROPHONE = 1;
    public static final int POSITION_LOCATION = 2;

    public static final int STATE_ON = 1;
    public static final int STATE_OFF = 0;
    public static final int STATE_INVALID = -1;

    public static final float DOTS_MARGIN = 0.01f;

    public static final int DOTS_COLOR_DEFAULT = -11224220; //#54BB64

}
