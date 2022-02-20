package rk.android.app.privacydashboard.view.internal.apppickerview;

import android.content.Context;
import android.icu.text.AlphabeticIndex;
import android.icu.text.Collator;
import android.os.Build;
import android.os.LocaleList;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import de.dlyt.yanndroid.oneui.sesl.utils.ReflectUtils;
import de.dlyt.yanndroid.oneui.view.RecyclerView;
import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.view.AppPickerView;

public abstract class AbsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable, SectionIndexer {
    private static final int MAX_OFFSET = 200;
    private AppPickerIconLoader mAppPickerIconLoader;
    protected Context mContext;
    private ForegroundColorSpan mForegroundColorSpan;
    private AppPickerView.OnBindListener mOnBindListener;
    private AppPickerView.OnSearchFilterListener mOnSearchFilterListener;
    private int mOrder;
    private int[] mPositionToSectionIndex;
    protected int mType;
    private List<AppPickerView.AppLabelInfo> mDataSet = new ArrayList<>();
    private List<AppPickerView.AppLabelInfo> mDataSetFiltered = new ArrayList<>();
    private Map<String, Integer> mSectionMap = new HashMap<>();
    private String[] mSections = new String[0];
    private String mSearchText = "";

    private static Comparator<AppPickerView.AppLabelInfo> APP_LABEL_ASCENDING = new Comparator<AppPickerView.AppLabelInfo>() {
        public int compare(AppPickerView.AppLabelInfo appLabelInfo, AppPickerView.AppLabelInfo appLabelInfo2) {
            Collator collator = Collator.getInstance(Locale.getDefault());
            collator.setStrength(Collator.TERTIARY);
            return collator.compare(appLabelInfo.getLabel(), appLabelInfo2.getLabel());
        }
    };

    private static Comparator<AppPickerView.AppLabelInfo> APP_LABEL_ASCENDING_IGNORE_CASE = new Comparator<AppPickerView.AppLabelInfo>() {
        public int compare(AppPickerView.AppLabelInfo appLabelInfo, AppPickerView.AppLabelInfo appLabelInfo2) {
            Collator collator = Collator.getInstance(Locale.getDefault());
            collator.setStrength(Collator.PRIMARY);
            return collator.compare(appLabelInfo.getLabel(), appLabelInfo2.getLabel());
        }
    };

    private static Comparator<AppPickerView.AppLabelInfo> APP_LABEL_DESCENDING = new Comparator<AppPickerView.AppLabelInfo>() {
        public int compare(AppPickerView.AppLabelInfo appLabelInfo, AppPickerView.AppLabelInfo appLabelInfo2) {
            Collator collator = Collator.getInstance(Locale.getDefault());
            collator.setStrength(Collator.TERTIARY);
            return collator.compare(appLabelInfo2.getLabel(), appLabelInfo.getLabel());
        }
    };

    private static Comparator<AppPickerView.AppLabelInfo> APP_LABEL_DESCENDING_IGNORE_CASE = new Comparator<AppPickerView.AppLabelInfo>() {
        public int compare(AppPickerView.AppLabelInfo appLabelInfo, AppPickerView.AppLabelInfo appLabelInfo2) {
            Collator collator = Collator.getInstance(Locale.getDefault());
            collator.setStrength(Collator.PRIMARY);
            return collator.compare(appLabelInfo2.getLabel(), appLabelInfo.getLabel());
        }
    };

    abstract void onBindViewHolderAction(AppPickerView.ViewHolder vh, int position, String packageName);

    public AbsAdapter(Context context, int type, int order, AppPickerIconLoader iconLoader) {
        mContext = context;
        mType = type;
        mOrder = order;
        mAppPickerIconLoader = iconLoader;

        TypedValue colorPrimary = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.colorPrimary, colorPrimary, true);
        mForegroundColorSpan = new ForegroundColorSpan(colorPrimary.resourceId != 0 ? mContext.getResources().getColor(colorPrimary.resourceId) : colorPrimary.data);
    }

    public static AbsAdapter getAppPickerAdapter(Context context, List<String> packagesList, int type, int order, List<AppPickerView.AppLabelInfo> appLabelInfos, AppPickerIconLoader iconLoader) {
        AbsAdapter adapter;
        if (type >= 7) {
            adapter = new GridAdapter(context, type, order, iconLoader);
        } else {
            adapter = new ListAdapter(context, type, order, iconLoader);
        }
        adapter.setHasStableIds(true);
        adapter.resetPackages(packagesList, false, appLabelInfos);
        return adapter;
    }

    public List<AppPickerView.AppLabelInfo> getDataSet() {
        return mDataSet;
    }

    public void resetPackages(List<String> packagesList, boolean refresh, List<AppPickerView.AppLabelInfo> appLabelInfos) {
        mDataSet.clear();
        mDataSet.addAll(DataManager.resetPackages(mContext, packagesList, appLabelInfos));
        if (getAppLabelComparator(mOrder) != null) {
            mDataSet.sort(getAppLabelComparator(mOrder));
        }
        if (hasAllAppsInList() && mDataSet != null && mDataSet.size() > 0) {
            mDataSet.add(0, new AppPickerView.AppLabelInfo("all_apps", ""));
        }
        mDataSetFiltered.clear();
        mDataSetFiltered.addAll(this.mDataSet);
        refreshSectionMap();
        if (refresh) {
            notifyDataSetChanged();
        }
    }

    public void setOrder(int order) {
        mOrder = order;
        if (getAppLabelComparator(order) != null) {
            mDataSet.sort(getAppLabelComparator(order));
            mDataSetFiltered.sort(getAppLabelComparator(order));
        }
        refreshSectionMap();
        notifyDataSetChanged();
    }

    private Comparator<AppPickerView.AppLabelInfo> getAppLabelComparator(int order) {
        switch (order) {
            case AppPickerView.ORDER_ASCENDING:
                return APP_LABEL_ASCENDING;
            case AppPickerView.ORDER_ASCENDING_IGNORE_CASE:
                return APP_LABEL_ASCENDING_IGNORE_CASE;
            case AppPickerView.ORDER_DESCENDING:
                return APP_LABEL_DESCENDING;
            case AppPickerView.ORDER_DESCENDING_IGNORE_CASE:
                return APP_LABEL_DESCENDING_IGNORE_CASE;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        String packageName = mDataSetFiltered.get(position).getPackageName();
        AppPickerView.ViewHolder viewHolder = (AppPickerView.ViewHolder) vh;
        if (!(viewHolder instanceof AppPickerView.HeaderViewHolder) && !(viewHolder instanceof AppPickerView.SeparatorViewHolder)) {
            if (mAppPickerIconLoader != null) {
                mAppPickerIconLoader.loadIcon(packageName, viewHolder.getAppIcon());
            }

            String label = mDataSetFiltered.get(position).getLabel();
            if (mSearchText.length() > 0) {
                // kang
                SpannableString spannableString = new SpannableString(label);
                StringTokenizer stringTokenizer = new StringTokenizer(mSearchText, " ");

                while (stringTokenizer.hasMoreTokens()) {
                    String nextToken = stringTokenizer.nextToken();
                    int i3 = 0;
                    String str = label;

                    do {
                        char[] prefixCharForSpan = (char[]) ReflectUtils.genericInvokeMethod(TextUtils.class,
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ? "hidden_semGetPrefixCharForSpan" : "hidden_semGetPrefixCharForSpan",
                                viewHolder.getAppLabel().getPaint(), str, nextToken.toCharArray());
                        if (prefixCharForSpan != null) {
                            nextToken = new String(prefixCharForSpan);
                        }

                        int i2;
                        String lowerCase = str.toLowerCase();
                        if (str.length() == lowerCase.length()) {
                            i2 = lowerCase.indexOf(nextToken.toLowerCase());
                        } else {
                            i2 = str.indexOf(nextToken);
                        }


                        int length = nextToken.length() + i2;
                        if (i2 < 0) {
                            break;
                        }

                        int i4 = i2 + i3;
                        i3 += length;
                        spannableString.setSpan(mForegroundColorSpan, i4, i3, 17);
                        str = str.substring(length);
                        if (str.toLowerCase().indexOf(nextToken.toLowerCase()) == -1) {
                            break;
                        }
                    } while (i3 < MAX_OFFSET);

                }
                viewHolder.getAppLabel().setText(spannableString);
                viewHolder.getItem().setContentDescription(spannableString);
                // kang
            } else {
                viewHolder.getAppLabel().setText(label);
                viewHolder.getItem().setContentDescription(label);
            }
        }

        onBindViewHolderAction(viewHolder, position, packageName);

        if (mOnBindListener != null) {
            mOnBindListener.onBindViewHolder(viewHolder, position, packageName);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSetFiltered.size();
    }

    public void setOnBindListener(AppPickerView.OnBindListener listener) {
        mOnBindListener = listener;
    }

    public void setOnSearchFilterListener(AppPickerView.OnSearchFilterListener listener) {
        mOnSearchFilterListener = listener;
    }

    public boolean hasAllAppsInList() {
        return mType == AppPickerView.TYPE_LIST_SWITCH || mType == AppPickerView.TYPE_LIST_SWITCH_2;
    }

    @Override
    public long getItemId(int position) {
        return (long) mDataSetFiltered.get(position).hashCode();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected Filter.FilterResults performFiltering(CharSequence sequence) {
                String str = sequence.toString();

                if (str.isEmpty()) {
                    mDataSetFiltered = mDataSet;
                    mSearchText = "";
                } else {
                    mSearchText = str;

                    ArrayList<AppPickerView.AppLabelInfo> list = new ArrayList<>();
                    for (AppPickerView.AppLabelInfo appLabelInfo : mDataSet) {
                        StringTokenizer tokenizer = new StringTokenizer(sequence.toString(), " ");
                        while (true) {
                            if (tokenizer.hasMoreTokens()) {
                                String nextToken = tokenizer.nextToken();
                                String label = appLabelInfo.getLabel();
                                if (!TextUtils.isEmpty(label) && label.replaceAll("\u200b", "").toLowerCase().contains(nextToken.toLowerCase())) {
                                    list.add(appLabelInfo);
                                    break;
                                }
                            }
                        }
                    }
                    mDataSetFiltered = list;
                }

                Filter.FilterResults results = new Filter.FilterResults();
                results.values = mDataSetFiltered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence sequence, Filter.FilterResults results) {
                mDataSetFiltered = (ArrayList<AppPickerView.AppLabelInfo>) results.values;
                refreshSectionMap();
                notifyDataSetChanged();
                if (mOnSearchFilterListener != null) {
                    mOnSearchFilterListener.onSearchFilterCompleted(getItemCount());
                }
            }
        };
    }

    @Override
    public int getPositionForSection(int i) {
        if (i >= mSections.length) {
            return 0;
        }
        return mSectionMap.get(mSections[i]);
    }

    @Override
    public int getSectionForPosition(int i) {
        if (i >= mPositionToSectionIndex.length) {
            return 0;
        }
        return mPositionToSectionIndex[i];
    }

    @Override
    public Object[] getSections() {
        return mSections;
    }

    public void refreshSectionMap() {
        mSectionMap.clear();

        ArrayList<String> list = new ArrayList<>();

        LocaleList locales = mContext.getResources().getConfiguration().getLocales();
        if (locales.size() == 0) {
            locales = new LocaleList(Locale.ENGLISH);
        }

        AlphabeticIndex<LocaleList> alphabeticIndex = new AlphabeticIndex<>(locales.get(0));
        for (int i = 1; i < locales.size(); i++) {
            alphabeticIndex.addLabels(locales.get(i));
        }
        alphabeticIndex.addLabels(Locale.ENGLISH);

        AlphabeticIndex.ImmutableIndex<LocaleList> immutableIndex = alphabeticIndex.buildImmutableIndex();

        mPositionToSectionIndex = new int[mDataSetFiltered.size()];

        for (int i = 0; i < mDataSetFiltered.size(); i++) {
            String label = mDataSetFiltered.get(i).getLabel();
            if (TextUtils.isEmpty(label)) {
                label = "";
            }

            String label2 = immutableIndex.getBucket(immutableIndex.getBucketIndex(label)).getLabel();
            if (!mSectionMap.containsKey(label2)) {
                list.add(label2);
                mSectionMap.put(label2, i);
            }

            mPositionToSectionIndex[i] = list.size() - 1;
        }

        mSections = new String[list.size()];
        list.toArray(mSections);
    }

    protected float limitFontScale(TextView textView) {
        float fontScale = textView.getResources().getConfiguration().fontScale;
        int i = (fontScale > 1.3f ? 1 : (fontScale == 1.3f ? 0 : -1));
        return i > 0 ? (textView.getTextSize() / fontScale) * 1.3f : textView.getTextSize();
    }

    public void limitFontLarge(TextView textView) {
        if (textView != null) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, limitFontScale(textView));
        }
    }

    public void limitFontLarge2LinesHeight(TextView textView) {
        if (textView != null) {
            float limitFontScale = limitFontScale(textView);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, limitFontScale);
            textView.setMinHeight(Math.round((limitFontScale * 2.0f) + 0.5f));
        }
    }
}
