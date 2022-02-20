package rk.android.app.privacydashboard.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.dlyt.yanndroid.oneui.preference.Preference;
import de.dlyt.yanndroid.oneui.preference.PreferenceViewHolder;
import rk.android.app.privacydashboard.R;

public class UnclickablePreference extends Preference {
    private static int NORMAL = 0;
    private static int FIRST_ITEM = 1;
    private static int SUBHEADER_ITEM = 1;
    private Context mContext;
    private int mPositionMode = 0;

    public UnclickablePreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        setSelectable(false);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UnclickablePreference);
            setLayoutResource(a.getResourceId(R.styleable.UnclickablePreference_android_layout, R.layout.oui_preference_unclickable_layout));
            seslSetSubheaderRoundedBackground(a.getInt(R.styleable.UnclickablePreference_roundStroke, 15));
            mPositionMode = a.getInt(R.styleable.UnclickablePreference_positionMode, 0);
            a.recycle();
        } else {
            setLayoutResource(R.layout.oui_preference_unclickable_layout);
            seslSetSubheaderRoundedBackground(15);
        }
    }

    public UnclickablePreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public UnclickablePreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnclickablePreference(@NonNull Context context) {
        this(context, null);
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder viewHolder) {
        super.onBindViewHolder(viewHolder);

        TextView titleTextView = (TextView) viewHolder.findViewById(R.id.title);
        titleTextView.setText(getTitle());
        titleTextView.setVisibility(View.VISIBLE);

        if (mContext != null) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) titleTextView.getLayoutParams();

            int top;
            int bottom;
            if (mPositionMode == FIRST_ITEM) {
                top = mContext.getResources().getDimensionPixelSize(R.dimen.oui_unclickablepref_first_margin_top);
                bottom = mContext.getResources().getDimensionPixelSize(R.dimen.oui_unclickablepref_first_margin_bottom);
            } else if (mPositionMode == SUBHEADER_ITEM) {
                top = mContext.getResources().getDimensionPixelSize(R.dimen.oui_unclickablepref_subheader_margin_top);
                bottom = mContext.getResources().getDimensionPixelSize(R.dimen.oui_unclickablepref_subheader_margin_bottom);
            } else {
                top = mContext.getResources().getDimensionPixelSize(R.dimen.oui_unclickablepref_margin_top);
                bottom = mContext.getResources().getDimensionPixelSize(R.dimen.oui_unclickablepref_margin_bottom);
            }

            lp.setMargins(mContext.getResources().getDimensionPixelSize(R.dimen.oui_unclickablepref_text_padding_start_end), top, mContext.getResources().getDimensionPixelSize(R.dimen.oui_unclickablepref_text_padding_start_end), bottom);
            titleTextView.setLayoutParams(lp);
        }

        viewHolder.setDividerAllowedAbove(false);
        viewHolder.setDividerAllowedBelow(false);
    }

    public void setPositionMode(int mode) {
        mPositionMode = mode;
    }
}
