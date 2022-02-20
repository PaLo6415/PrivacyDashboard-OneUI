package rk.android.app.privacydashboard.fragment.settings.indicator;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import java.util.Arrays;

import de.dlyt.yanndroid.oneui.preference.ColorPickerPreference;
import de.dlyt.yanndroid.oneui.preference.LayoutPreference;
import de.dlyt.yanndroid.oneui.preference.ListPreference;
import de.dlyt.yanndroid.oneui.preference.Preference;
import de.dlyt.yanndroid.oneui.preference.PreferenceFragmentCompat;
import de.dlyt.yanndroid.oneui.preference.SwitchPreferenceCompat;

import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.manager.PreferenceManager;
import rk.android.app.privacydashboard.manager.PreferenceManager.PREF_CONSTANTS;
import rk.android.app.privacydashboard.preference.SeekBarPreferenceWithBtns;
import rk.android.app.privacydashboard.util.Utils;

public class IndicatorSettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
    private Context context;
    private PreferenceManager preferenceManager;

    private LinearLayout dotsPreview;
    private ImageView dotsCameraIcon;
    private ImageView dotsMicrophoneIcon;
    private ImageView dotsLocationIcon;

    private ColorPickerPreference dotsColorPref;
    private ListPreference dotsPositionPref;
    private SeekBarPreferenceWithBtns dotsSizePref;
    private SeekBarPreferenceWithBtns dotsAlphaPref;
    private SwitchPreferenceCompat dotsHidePref;
    private SeekBarPreferenceWithBtns dotsHideTimerPref;
    private SwitchPreferenceCompat dotsAutoHidePref;
    private SeekBarPreferenceWithBtns dotsAutoHideTimerPref;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        preferenceManager = new PreferenceManager(context);
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        addPreferencesFromResource(R.xml.oui_indicator_settings);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initPreferences();
    }

    @Override
    public void onStart() {
        super.onStart();
        setChildPrefsEnabled(preferenceManager.isPrivacyDots());
        if (dotsPositionPref != null) {
            dotsPositionPref.setSummary(dotsPositionPref.getEntry());
            dotsPositionPref.seslSetSummaryColor(getColoredSummaryColor(true));
        }
        if (dotsHidePref != null && dotsHideTimerPref != null) {
            dotsHideTimerPref.setEnabled(dotsHidePref.isChecked());
        }
        if (dotsAutoHidePref != null && dotsAutoHideTimerPref != null) {
            dotsAutoHideTimerPref.setEnabled(dotsAutoHidePref.isChecked());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.item_background_color, context.getTheme()));
        getListView().seslSetLastRoundedCorner(false);
    }

    private void initPreferences() {
        initPreview();

        dotsColorPref = findPreference(PREF_CONSTANTS.PRIVACY_DOTS_COLOR);
        dotsColorPref.setOnPreferenceChangeListener(this);

        dotsPositionPref = findPreference(PREF_CONSTANTS.PRIVACY_DOTS_POSITION);
        dotsSizePref = findPreference(PREF_CONSTANTS.PRIVACY_DOTS_SIZE);
        dotsAlphaPref = findPreference(PREF_CONSTANTS.PRIVACY_DOTS_OPACITY);
        dotsPositionPref.setOnPreferenceChangeListener(this);
        dotsSizePref.setOnPreferenceChangeListener(this);
        dotsAlphaPref.setOnPreferenceChangeListener(this);

        dotsHidePref = findPreference(PREF_CONSTANTS.PRIVACY_DOTS_HIDE);
        dotsHideTimerPref = findPreference(PREF_CONSTANTS.PRIVACY_DOTS_HIDE_TIMER);
        dotsHidePref.setOnPreferenceChangeListener(this);

        dotsAutoHidePref = findPreference(PREF_CONSTANTS.PRIVACY_DOTS_AUTO_HIDE);
        dotsAutoHideTimerPref = findPreference(PREF_CONSTANTS.PRIVACY_DOTS_AUTO_HIDE_TIMER);
        dotsAutoHidePref.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case PREF_CONSTANTS.PRIVACY_DOTS_COLOR:
                setPreviewColor((int) newValue);
                return true;
            case PREF_CONSTANTS.PRIVACY_DOTS_POSITION:
                dotsPositionPref.setSummary(dotsPositionPref.getEntries()[Arrays.asList(dotsPositionPref.getEntryValues()).indexOf((String) newValue)]);
                return true;
            case PREF_CONSTANTS.PRIVACY_DOTS_SIZE:
                final int newSize;

                switch ((int) newValue) {
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

                setPreviewSize(newSize);
                return true;
            case PREF_CONSTANTS.PRIVACY_DOTS_OPACITY:
                setPreviewOpacity((int) newValue);
                return true;
            case PREF_CONSTANTS.PRIVACY_DOTS_HIDE:
                dotsHideTimerPref.setEnabled((boolean) newValue);
                return true;
            case PREF_CONSTANTS.PRIVACY_DOTS_AUTO_HIDE:
                dotsAutoHideTimerPref.setEnabled((boolean) newValue);
                return true;
        }
        return false;
    }

    private void initPreview() {
        LayoutPreference dotsPreviewPref = findPreference("preview");
        dotsPreview = dotsPreviewPref.findViewById(R.id.dots_overlay_preview);
        dotsCameraIcon = dotsPreview.findViewById(R.id.icon_camera);
        dotsMicrophoneIcon = dotsPreview.findViewById(R.id.icon_microphone);
        dotsLocationIcon = dotsPreview.findViewById(R.id.icon_location);

        dotsPreview.setBackgroundResource(R.drawable.oui_overlay_background);
        dotsCameraIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.oui_privacy_dots_ic_camera));
        dotsMicrophoneIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.oui_privacy_dots_ic_microphone));
        dotsLocationIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.oui_privacy_dots_ic_location));

        setPreviewSize(preferenceManager.getPrivacyDotSize());
        setPreviewColor(preferenceManager.getIconColor());
        setPreviewOpacity(preferenceManager.getPrivacyDotOpacity());
    }

    private void setPreviewColor(int color) {
        dotsPreview.setBackgroundTintList(ColorStateList.valueOf(color));

        int iconTint;
        if (ColorUtils.calculateLuminance(color) > 0.5) {
            iconTint = context.getColor(R.color.oui_overlay_icon_tint_light);
        } else {
            iconTint = context.getColor(R.color.oui_overlay_icon_tint_dark);
        }
        dotsCameraIcon.setImageTintList(ColorStateList.valueOf(iconTint));
        dotsMicrophoneIcon.setImageTintList(ColorStateList.valueOf(iconTint));
        dotsLocationIcon.setImageTintList(ColorStateList.valueOf(iconTint));
    }

    private void setPreviewSize(int size) {
        GradientDrawable bgDrawable = (GradientDrawable) dotsPreview.getBackground();
        if (bgDrawable != null) {
            final float radius = (10 * Utils.getDensity(context) * size / 100);
            bgDrawable.setCornerRadius(radius);
        }

        final int height = (int) (18 * Utils.getDensity(context) * size / 100);
        final int padding = (int) (5 * Utils.getDensity(context) * size / 100);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) dotsPreview.getLayoutParams();
        lp.height = height;
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        dotsPreview.setLayoutParams(lp);
        dotsPreview.setPadding(padding, 0, padding, 0);

        final int iconWidth = (int) (10 * Utils.getDensity(context) * size / 100);
        final int iconHeight = (int) (18 * Utils.getDensity(context) * size / 100);
        final int firstIconMargin = (int) (2 * Utils.getDensity(context) * size / 100);
        final int iconMargin = (int) (5 * Utils.getDensity(context) * size / 100);

        ViewGroup.MarginLayoutParams firstIconParams = new LinearLayout.LayoutParams(iconWidth, iconHeight);
        firstIconParams.setMarginStart(firstIconMargin);
        dotsCameraIcon.setLayoutParams(firstIconParams);

        ViewGroup.MarginLayoutParams params = new LinearLayout.LayoutParams(iconWidth, iconHeight);
        params.setMarginStart(iconMargin);
        dotsMicrophoneIcon.setLayoutParams(params);
        dotsLocationIcon.setLayoutParams(params);
    }

    private void setPreviewOpacity(float alpha) {
        dotsPreview.setAlpha(alpha / 100.0f);
    }

    public void setChildPrefsEnabled(boolean enabled) {
        getPreferenceScreen().setEnabled(enabled);
    }

    private ColorStateList getColoredSummaryColor(boolean enabled) {
        if (enabled) {
            TypedValue colorPrimaryDark = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.colorPrimaryDark, colorPrimaryDark, true);

            int[][] states = new int[][] {
                    new int[] {android.R.attr.state_enabled},
                    new int[] {-android.R.attr.state_enabled}
            };
            int[] colors = new int[] {
                    Color.argb(0xff, Color.red(colorPrimaryDark.data), Color.green(colorPrimaryDark.data), Color.blue(colorPrimaryDark.data)),
                    Color.argb(0x4d, Color.red(colorPrimaryDark.data), Color.green(colorPrimaryDark.data), Color.blue(colorPrimaryDark.data))
            };
            return new ColorStateList(states, colors);
        } else
            return context.getResources().getColorStateList(R.color.sesl_secondary_text, context.getTheme());
    }
}
