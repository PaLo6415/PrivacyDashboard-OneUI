package rk.android.app.privacydashboard.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import rk.android.app.privacydashboard.R;

public class PermissionCardView extends LinearLayout {
    Context mContext;

    private ConstraintLayout mContainerView;
    private ImageView mIconImageView;
    private TextView mTitleTextView;
    private TextView mSummaryTextView;

    private int mIconColor;
    private Drawable mIconDrawable;
    private String mTitleText = "";
    private String mSummaryText = "";

    public PermissionCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setStyleable(attrs);
        init();
    }

    private void setStyleable(AttributeSet attrs) {
        TypedArray obtainStyledAttributes = mContext.obtainStyledAttributes(attrs, R.styleable.PermissionCardView);
        mIconDrawable = obtainStyledAttributes.getDrawable(R.styleable.PermissionCardView_IconDrawable);
        mTitleText = obtainStyledAttributes.getString(R.styleable.PermissionCardView_TitleText);
        mSummaryText = obtainStyledAttributes.getString(R.styleable.PermissionCardView_SummaryText);
        obtainStyledAttributes.recycle();
    }

    private void init() {
        removeAllViews();

        inflate(mContext, R.layout.oui_view_permissioncardview_layout, this);

        mContainerView = findViewById(R.id.oui_permissioncardview_container);

        mIconImageView = findViewById(R.id.oui_permissioncardview_icon);
        mIconImageView.setImageDrawable(mIconDrawable);
        mIconImageView.getDrawable().setTint(getResources().getColor(R.color.oui_permissioncardview_item_icon_color, mContext.getTheme()));

        mTitleTextView = findViewById(R.id.oui_permissioncardview_title);
        mTitleTextView.setText(mTitleText);

        mSummaryTextView = findViewById(R.id.oui_permissioncardview_summary);
        mSummaryTextView.setText(mSummaryText);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        setFocusable(enabled);
        setClickable(enabled);

        mContainerView.setEnabled(enabled);
        mIconImageView.setAlpha(enabled ? 1.0f : 0.4f);
        mTitleTextView.setAlpha(enabled ? 1.0f : 0.4f);
        mSummaryTextView.setAlpha(enabled ? 1.0f : 0.4f);
    }

    public Drawable getIconDrawable() {
        return mIconDrawable;
    }

    public void setIconDrawable(Drawable d) {
        mIconDrawable = d;
        mIconImageView.setImageDrawable(mIconDrawable);
        init();
    }

    public int getIconColor() {
        return mIconColor;
    }

    public void setIconColor(int color) {
        mIconColor = color;
        mIconImageView.getDrawable().setTint(mIconColor);
    }

    public String getTitleText() {
        return mTitleText;
    }

    public void setTitleText(String title) {
        mTitleText = title;
        mTitleTextView.setText(mTitleText);
    }

    public String getSummaryText() {
        return mSummaryText;
    }

    public void setSummaryText(String text) {
        if (text == null)
            text = "";

        mSummaryText = text;
        mSummaryTextView.setText(mSummaryText);
        if (mSummaryText.isEmpty())
            mSummaryTextView.setVisibility(View.GONE);
        else
            mSummaryTextView.setVisibility(View.VISIBLE);
    }
}
