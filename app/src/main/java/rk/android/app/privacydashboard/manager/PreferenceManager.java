package rk.android.app.privacydashboard.manager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import rk.android.app.privacydashboard.util.Constants;

public class PreferenceManager {
    public static class PREF_CONSTANTS {
        public static final String FIRST_LAUNCH = "first.launch";
        public static final String NIGHT_MODE = "night.mode";
        public static final String VERSION_NUMBER = "version.number";

        public static final String PRIVACY_DOTS = "privacy.dots";
        public static final String PRIVACY_DOTS_COLOR = "privacy.dots.color";
        public static final String PRIVACY_DOTS_POSITION = "privacy.dots.position";
        public static final String PRIVACY_DOTS_MARGIN = "privacy.dots.margin";
        public static final String PRIVACY_DOTS_CLICK = "privacy.dots.click";
        public static final String PRIVACY_DOTS_SIZE = "privacy.dots.size";
        public static final String PRIVACY_DOTS_OPACITY = "privacy.dots.opacity";
        public static final String PRIVACY_DOTS_HIDE = "privacy.dots.hide";
        public static final String PRIVACY_DOTS_HIDE_TIMER = "privacy.dots.hide.timer";
        public static final String PRIVACY_DOTS_AUTO_HIDE = "privacy.dots.auto_hide";
        public static final String PRIVACY_DOTS_AUTO_HIDE_TIMER = "privacy.dots.auto_hide.timer";

        public static final String PRIVACY_NOTIFICATION = "privacy.notification";
        public static final String PRIVACY_NOTIFICATION_ONGOING = "privacy.notification.ongoing";
        public static final String PRIVACY_NOTIFICATION_ICON = "privacy.notification.icon";
        public static final String PRIVACY_NOTIFICATION_CLICK = "privacy.notification.click";

        public static final String PRIVACY_EXCLUDE = "privacy.exclude";
        public static final String EXCLUDE_TYPE_INDICATOR = "exclude.type.indicator";
        public static final String EXCLUDE_TYPE_NOTIFICATION = "exclude.type.notification";
        public static final String EXCLUDE_TYPE_LOGS = "exclude.type.logs";
    }

     SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void clearPreferences(){
        sharedPreferences.edit()
                .clear()
                .apply();
    }

    // First launch
    public void setFirstLaunch(boolean isFirst) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_CONSTANTS.FIRST_LAUNCH, isFirst);
        editor.apply();
    }

    public boolean isFirstLaunch(){
        return sharedPreferences.getBoolean(PREF_CONSTANTS.FIRST_LAUNCH, true);
    }

    // Night mode settings
    public int getNightMode() {
        switch (sharedPreferences.getString(PREF_CONSTANTS.NIGHT_MODE, "0")) {
            case "0":
                return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            case "1":
                return AppCompatDelegate.MODE_NIGHT_NO;
            case "2":
                return AppCompatDelegate.MODE_NIGHT_YES;
            default:
                return AppCompatDelegate.getDefaultNightMode();
        }
    }

    // Version number
    public void setVersionNumber(int number) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_CONSTANTS.VERSION_NUMBER, number);
        editor.apply();
    }

    public int getVersionNumber() {
        return sharedPreferences.getInt(PREF_CONSTANTS.VERSION_NUMBER,0);
    }

    // Privacy dots
    public void setPrivacyDots(boolean isDots) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_CONSTANTS.PRIVACY_DOTS, isDots);
        editor.apply();
    }
    public boolean isPrivacyDots() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.PRIVACY_DOTS, true);
    }

    // Privacy dots position
    public void setPrivacyDotPosition(int position) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_CONSTANTS.PRIVACY_DOTS_POSITION, String.valueOf(position));
        editor.apply();
    }
    public int getPrivacyDotPosition() {
        return Integer.parseInt(sharedPreferences.getString(PREF_CONSTANTS.PRIVACY_DOTS_POSITION, "8388661"));
    }

    // Privacy dots margin
    public void setPrivacyDotMargin(boolean isMargin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_CONSTANTS.PRIVACY_DOTS_MARGIN, isMargin);
        editor.apply();
    }

    public boolean isPrivacyDotMargin() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.PRIVACY_DOTS_MARGIN, false);
    }

    // Privacy dots click action
    public void setPrivacyDotClick(boolean isClick) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_CONSTANTS.PRIVACY_DOTS_CLICK, isClick);
        editor.apply();
    }

    public boolean isPrivacyDotClick() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.PRIVACY_DOTS_CLICK, false);
    }

    // Privacy dots color selected
    public void setIconColor(int color) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_CONSTANTS.PRIVACY_DOTS_COLOR, color);
        editor.apply();
    }
    public int getIconColor() {
        return sharedPreferences.getInt(PREF_CONSTANTS.PRIVACY_DOTS_COLOR, Constants.DOTS_COLOR_DEFAULT);
    }

    // Privacy dots position
    public void setPrivacyDotSize(int size) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_CONSTANTS.PRIVACY_DOTS_SIZE, size);
        editor.apply();
    }

    public int getPrivacyDotSize() {
        final int oldSize = sharedPreferences.getInt(PREF_CONSTANTS.PRIVACY_DOTS_SIZE, 4);
        final int newSize;

        switch (oldSize) {
            case 1:
                newSize = 80;
                break;
            case 2:
                newSize = 110;
                break;
            case 3:
                newSize = 130;
                break;
            case 5:
                newSize = 180;
                break;
            case 6:
                newSize = 210;
                break;
            default:
            case 4:
                newSize = 150;
                break;
        }

        return newSize;
    }

    // Privacy dots position
    public void setPrivacyDotOpacity(int opacity) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_CONSTANTS.PRIVACY_DOTS_OPACITY, opacity);
        editor.apply();
    }

    public int getPrivacyDotOpacity() {
        return sharedPreferences.getInt(PREF_CONSTANTS.PRIVACY_DOTS_OPACITY, 100);
    }

    // Privacy dots hide
    public void setPrivacyDotHide(boolean isHide) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_CONSTANTS.PRIVACY_DOTS_HIDE, isHide);
        editor.apply();
    }

    public boolean isPrivacyDotHide() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.PRIVACY_DOTS_HIDE, false);
    }

    // Privacy dots hide timer
    public void setPrivacyDotHideTimer(int time) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_CONSTANTS.PRIVACY_DOTS_HIDE_TIMER, time);
        editor.apply();
    }

    public int getPrivacyDotHideTimer() {
        return sharedPreferences.getInt(PREF_CONSTANTS.PRIVACY_DOTS_HIDE_TIMER, 6);
    }

    // Privacy dots minimize
    public void setPrivacyDotAutoHide(boolean isHide) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_CONSTANTS.PRIVACY_DOTS_AUTO_HIDE, isHide);
        editor.apply();
    }

    public boolean isPrivacyDotAutoHide() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.PRIVACY_DOTS_AUTO_HIDE, false);
    }

    // Privacy dots minimize timer
    public void setPrivacyDotAutoHideTimer(int time) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PREF_CONSTANTS.PRIVACY_DOTS_AUTO_HIDE_TIMER, time);
        editor.apply();
    }

    public int getPrivacyDotAutoHideTimer() {
        return sharedPreferences.getInt(PREF_CONSTANTS.PRIVACY_DOTS_AUTO_HIDE_TIMER, 3);
    }

    // Privacy notification
    public void setPrivacyNotification(boolean isNotification) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_CONSTANTS.PRIVACY_NOTIFICATION, isNotification);
        editor.apply();
    }

    public boolean isPrivacyNotification() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.PRIVACY_NOTIFICATION, false);
    }

    // Privacy notification ongoing
    public void setPrivacyNotificationOngoing(boolean isHide) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_CONSTANTS.PRIVACY_NOTIFICATION_ONGOING, isHide);
        editor.apply();
    }

    public boolean isPrivacyNotificationOngoing() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.PRIVACY_NOTIFICATION_ONGOING, true);
    }

    // rivacy notification icon
    public void setPrivacyNotificationIcon(boolean isIcon) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_CONSTANTS.PRIVACY_NOTIFICATION_ICON, isIcon);
        editor.apply();
    }

    public boolean isPrivacyNotificationIcon() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.PRIVACY_NOTIFICATION_ICON, true);
    }

    // Privacy notification click
    public void setPrivacyNotificationClick(boolean isClick) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_CONSTANTS.PRIVACY_NOTIFICATION_CLICK, isClick);
        editor.apply();
    }

    public boolean isPrivacyNotificationClick() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.PRIVACY_NOTIFICATION_CLICK, false);
    }

    // Privacy exclude indicator
    public void setPrivacyExcluded(boolean isExclude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_CONSTANTS.PRIVACY_EXCLUDE, isExclude);
        editor.apply();
    }

    public boolean isPrivacyExcluded() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.PRIVACY_EXCLUDE, false);
    }

    // Privacy exclude indicator
    public void setPrivacyExcludeIndicator(boolean isExclude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_CONSTANTS.EXCLUDE_TYPE_INDICATOR, isExclude);
        editor.apply();
    }

    public boolean isPrivacyExcludeIndicator() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.EXCLUDE_TYPE_INDICATOR, true);
    }

    // Privacy exclude notification
    public void setPrivacyExcludeNotification(boolean isExclude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_CONSTANTS.EXCLUDE_TYPE_NOTIFICATION, isExclude);
        editor.apply();
    }

    public boolean isPrivacyExcludeNotification() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.EXCLUDE_TYPE_NOTIFICATION, false);
    }

    // Privacy exclude indicator
    public void setPrivacyExcludeLogs(boolean isExclude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_CONSTANTS.EXCLUDE_TYPE_LOGS, isExclude);
        editor.apply();
    }

    public boolean isPrivacyExcludeLogs() {
        return sharedPreferences.getBoolean(PREF_CONSTANTS.EXCLUDE_TYPE_LOGS, false);
    }
}
