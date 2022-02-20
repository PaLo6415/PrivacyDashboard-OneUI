package rk.android.app.privacydashboard.view;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import de.dlyt.yanndroid.oneui.sesl.recyclerview.GridLayoutManager;
import de.dlyt.yanndroid.oneui.sesl.recyclerview.LinearLayoutManager;
import de.dlyt.yanndroid.oneui.sesl.utils.SeslRoundedCorner;
import de.dlyt.yanndroid.oneui.sesl.utils.SeslSubheaderRoundedCorner;
import de.dlyt.yanndroid.oneui.view.RecyclerView;
import de.dlyt.yanndroid.oneui.widget.Switch;

import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.view.internal.apppickerview.AbsAdapter;
import rk.android.app.privacydashboard.view.internal.apppickerview.AppPickerIconLoader;

public class AppPickerView extends RecyclerView implements RecyclerView.RecyclerListener {
    public static final int ORDER_ASCENDING = 1;
    public static final int ORDER_ASCENDING_IGNORE_CASE = 2;
    public static final int ORDER_DESCENDING = 3;
    public static final int ORDER_DESCENDING_IGNORE_CASE = 4;
    public static final int TYPE_LIST_NO_WIDGET = 0;
    public static final int TYPE_LIST_ACTION_BUTTON = 1;
    public static final int TYPE_LIST_CHECKBOX = 2;
    public static final int TYPE_LIST_CHECKBOX_2 = 3;
    public static final int TYPE_LIST_RADIO_BUTTON = 4;
    public static final int TYPE_LIST_SWITCH = 5;
    public static final int TYPE_LIST_SWITCH_2 = 6;
    public static final int TYPE_GRID_NO_WIDGET = 7;
    public static final int TYPE_GRID_CHECKBOX = 8;
    private AbsAdapter mAdapter;
    private AppPickerIconLoader mAppPickerIconLoader;
    private Context mContext;
    private RecyclerView.ItemDecoration mGridSpacingDecoration;
    private int mOrder;
    private int mRoundedColor = Color.BLACK;
    private SeslSubheaderRoundedCorner mRoundedCorner;
    private int mSpanCount = 4;
    private int mType;

    public interface OnBindListener {
        void onBindViewHolder(ViewHolder vh, int position, String packageName);
    }

    public interface OnSearchFilterListener {
        void onSearchFilterCompleted(int itemCount);
    }

    public AppPickerView(@NonNull Context context) {
        this(context, null);
    }

    public AppPickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppPickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setRecyclerListener(this);
        mAppPickerIconLoader = new AppPickerIconLoader(mContext);
    }

    public void setAppPickerView(int type) {
        setAppPickerView(type, null, ORDER_ASCENDING_IGNORE_CASE, null);
    }

    public void setAppPickerView(int type, List<String> packagesList) {
        setAppPickerView(type, packagesList, ORDER_ASCENDING_IGNORE_CASE, null);
    }

    public void setAppPickerView(int type, List<String> packagesList, int order) {
        setAppPickerView(type, packagesList, order, null);
    }

    public void setAppPickerView(int type, List<String> packagesList, int order, List<AppLabelInfo> appLabelInfos) {
        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.roundedCornerColor, typedValue, true);
        if (typedValue.resourceId > 0) {
            mRoundedColor = mContext.getResources().getColor(typedValue.resourceId, null);
        }
        mRoundedCorner = new SeslSubheaderRoundedCorner(mContext);
        mRoundedCorner.setRoundedCorners(SeslRoundedCorner.ROUNDED_CORNER_ALL);

        if (packagesList == null) {
            packagesList = getInstalledPackages(mContext);
        }
        mType = type;
        mOrder = order;
        mAdapter = AbsAdapter.getAppPickerAdapter(mContext, packagesList, type, order, appLabelInfos, mAppPickerIconLoader);

        switch (mType) {
            case TYPE_LIST_NO_WIDGET:
            case TYPE_LIST_ACTION_BUTTON:
            case TYPE_LIST_CHECKBOX:
            case TYPE_LIST_CHECKBOX_2:
            case TYPE_LIST_RADIO_BUTTON:
            case TYPE_LIST_SWITCH:
            case TYPE_LIST_SWITCH_2:
                addItemDecoration(new ListDividerItemDecoration(mContext, mType));
                break;
            case TYPE_GRID_NO_WIDGET:
            case TYPE_GRID_CHECKBOX:
                if (mGridSpacingDecoration == null) {
                    mGridSpacingDecoration = new GridSpacingItemDecoration(mContext, mSpanCount, 8, true);
                    addItemDecoration(mGridSpacingDecoration);
                }
                break;
        }

        setLayoutManager(getLayoutManager(mType));
        setAdapter(mAdapter);
        seslSetGoToTopEnabled(true);
        seslSetFastScrollerEnabled(true);
        seslSetFillBottomEnabled(true);
    }

    public int getType() {
        return mType;
    }

    public void setOnBindListener(OnBindListener listener) {
        if (mAdapter != null) {
            mAdapter.setOnBindListener(listener);
        }
    }

    public static List<String> getInstalledPackages(Context context) {
        List<ApplicationInfo> installedApps = context.getPackageManager().getInstalledApplications(0);
        ArrayList<String> list = new ArrayList<>();
        for (ApplicationInfo appInfo : installedApps) {
            list.add(appInfo.packageName);
        }
        return list;
    }

    public int getAppLabelOrder() {
        return mOrder;
    }

    public void setAppLabelOrder(int order) {
        mOrder = order;
        mAdapter.setOrder(order);
    }

    public void resetPackages(List<String> list) {
        mAdapter.resetPackages(list, true, null);
    }

    public void setSearchFilter(String filter) {
        setSearchFilter(filter, null);
    }

    public void setSearchFilter(String filter, OnSearchFilterListener listener) {
        if (listener != null) {
            mAdapter.setOnSearchFilterListener(listener);
        }
        mAdapter.getFilter().filter(filter);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ImageButton actionButton = viewHolder.getActionButton();
        if (actionButton != null && actionButton.hasOnClickListeners()) {
            actionButton.setOnClickListener(null);
        }

        ImageView appIcon = viewHolder.getAppIcon();
        if (appIcon != null && appIcon.hasOnClickListeners()) {
            appIcon.setOnClickListener(null);
        }

        CheckBox checkBox = viewHolder.getCheckBox();
        if (checkBox != null) {
            checkBox.setOnCheckedChangeListener(null);
        }

        View item = viewHolder.getItem();
        if (item != null && item.hasOnClickListeners()) {
            item.setOnClickListener(null);
        }

        Switch switchCompat = viewHolder.getSwitch();
        if (switchCompat != null) {
            switchCompat.setOnCheckedChangeListener(null);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAppPickerIconLoader.startIconLoaderThread();
    }

    @Override
    public void onDetachedFromWindow() {
        mAppPickerIconLoader.stopIconLoaderThread();
        super.onDetachedFromWindow();
    }

    private RecyclerView.LayoutManager getLayoutManager(int type) {
        if (type == TYPE_GRID_NO_WIDGET || type == TYPE_GRID_CHECKBOX) {
            return new GridLayoutManager(mContext, mSpanCount);
        } else {
            return new LinearLayoutManager(mContext);
        }
    }


    public class ListDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;
        private int mDividerLeft;
        private int mType;

        public ListDividerItemDecoration(Context context, int type) {
            mType = type;

            TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
            mDivider = a.getDrawable(0);
            a.recycle();

            mDividerLeft = getResources().getDimensionPixelSize(R.dimen.oui_app_picker_list_icon_frame_width);
        }

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.onDraw(c, parent, state);

            for (int i = 0; i < parent.getChildCount() - 1; i++) {
                View child = parent.getChildAt(i);

                if (!(parent.getChildViewHolder(child) instanceof SeparatorViewHolder)) {
                    final int start = i == 0 && mType == TYPE_LIST_SWITCH_2 ? parent.getPaddingLeft() : mDividerLeft;
                    final int top = child.getBottom() + ((ViewGroup.MarginLayoutParams) ((RecyclerView.LayoutParams) child.getLayoutParams())).bottomMargin;
                    final int end = parent.getWidth() - parent.getPaddingRight();
                    final int bottom = mDivider.getIntrinsicHeight() + top;

                    mDivider.setBounds(start, top, end, bottom);
                    mDivider.draw(c);
                }
            }
        }

        @Override
        public void seslOnDispatchDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.seslOnDispatchDraw(c, parent, state);
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                if (parent.getChildViewHolder(child) instanceof SeparatorViewHolder) {
                    mRoundedCorner.drawRoundedCorner(child, c);
                }
            }
        }
    }

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private float density;
        private boolean includeEdge;
        private int spacing;
        private int spacingTop;
        private int spanCount;

        public GridSpacingItemDecoration(Context context, int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            density = context.getResources().getDisplayMetrics().density;
            this.spacing = (int) (((float) spacing) * density);
            spacingTop = (int) (density * 12.0f);
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
            int childAdapterPosition = parent.getChildAdapterPosition(view);
            int i = childAdapterPosition % spanCount;
            if (includeEdge) {
                outRect.left = spacing - ((i * spacing) / spanCount);
                outRect.right = ((i + 1) * spacing) / spanCount;
                if (childAdapterPosition < spanCount) {
                    outRect.top = spacingTop;
                }
                outRect.bottom = spacingTop;
            } else {
                outRect.left = (i * spacing) / spanCount;
                outRect.right = spacing - (((i + 1) * spacing) / spanCount);
            }
        }
    }

    public void setGridSpanCount(int spanCount) {
        if (spanCount < 4) {
            mSpanCount = 4;
        } else if (spanCount > 8) {
            mSpanCount = 8;
        } else {
            mSpanCount = spanCount;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton mActionButton;
        private final ImageView mAppIcon;
        private final ViewGroup mAppIconContainer;
        private final TextView mAppName;
        private final CheckBox mCheckBox;
        private final ViewGroup mLeftContainer;
        private final RadioButton mRadioButton;
        private final TextView mSummary;
        private final Switch mSwitch;
        private final ViewGroup mTitleContainer;
        private final ViewGroup mWidgetContainer;

        public ViewHolder(View view) {
            super(view);
            mAppName = (TextView) view.findViewById(R.id.title);
            mAppIcon = (ImageView) view.findViewById(R.id.icon);
            mAppIconContainer = (ViewGroup) view.findViewById(R.id.icon_frame);
            mTitleContainer = (ViewGroup) view.findViewById(R.id.title_frame);
            mSummary = (TextView) view.findViewById(R.id.summary);
            mLeftContainer = (ViewGroup) view.findViewById(R.id.left_frame);
            mCheckBox = (CheckBox) view.findViewById(R.id.check_widget);
            mRadioButton = (RadioButton) view.findViewById(R.id.radio_widget);
            mWidgetContainer = (ViewGroup) view.findViewById(R.id.widget_frame);
            mSwitch = (Switch) view.findViewById(R.id.switch_widget);
            mActionButton = (ImageButton) view.findViewById(R.id.image_button);
        }

        public TextView getAppLabel() {
            return mAppName;
        }

        public ImageView getAppIcon() {
            return mAppIcon;
        }

        public ViewGroup getAppIconContainer() {
            return mAppIconContainer;
        }

        public TextView getSummary() {
            return mSummary;
        }

        public View getLeftContainer() {
            return mLeftContainer;
        }

        public CheckBox getCheckBox() {
            return mCheckBox;
        }

        public RadioButton getRadioButton() {
            return mRadioButton;
        }

        public ViewGroup getWidgetContainer() {
            return mWidgetContainer;
        }

        public Switch getSwitch() {
            return mSwitch;
        }

        public ImageButton getActionButton() {
            return mActionButton;
        }

        public View getItem() {
            return itemView;
        }
    }

    public static class HeaderViewHolder extends ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }

    public static class FooterViewHolder extends ViewHolder {
        public FooterViewHolder(View view) {
            super(view);
        }
    }

    public static class SeparatorViewHolder extends ViewHolder {
        private final TextView mSeparatorText;

        public SeparatorViewHolder(View view) {
            super(view);
            mSeparatorText = (TextView) view.findViewById(R.id.separator);
        }

        public TextView getSeparatorText() {
            return mSeparatorText;
        }
    }

    public static class AppLabelInfo {
        private final boolean mIsSeparator;
        private final String mLabel;
        private final String mPackageName;

        public AppLabelInfo(String packageName, String label) {
            mIsSeparator = false;
            mPackageName = packageName;
            mLabel = label;
        }

        public String getPackageName() {
            return mPackageName;
        }

        public String getLabel() {
            return mLabel;
        }

        public boolean isSeparator() {
            return mIsSeparator;
        }

        @NonNull
        public String toString() {
            if (mIsSeparator) {
                return "[AppLabel] separator=" + mIsSeparator;
            } else  {
                return "[AppLabel] label=" + mLabel + ", packageName=" + mPackageName;
            }
        }
    }
}
