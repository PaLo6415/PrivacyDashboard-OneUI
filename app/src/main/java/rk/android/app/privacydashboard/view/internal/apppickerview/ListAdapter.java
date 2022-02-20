package rk.android.app.privacydashboard.view.internal.apppickerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import de.dlyt.yanndroid.oneui.view.RecyclerView;
import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.view.AppPickerView;

class ListAdapter extends AbsAdapter {
    private static final int VIEW_TYPE_HEADER = 256;
    private static final int VIEW_TYPE_ITEM = 257;
    private static final int VIEW_TYPE_FOOTER = 258;
    private static final int VIEW_TYPE_SEPARATOR = 259;

    public ListAdapter(Context context, int type, int order, AppPickerIconLoader iconLoader) {
        super(context, type, order, iconLoader);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                layoutRes = hasAllAppsInList() ? R.layout.oui_layout_app_picker_list_header : R.layout.oui_layout_app_picker_list;
                break;
            case VIEW_TYPE_FOOTER:
                layoutRes = R.layout.oui_layout_app_picker_list_footer;
                break;
            case VIEW_TYPE_SEPARATOR:
                layoutRes = R.layout.oui_layout_app_picker_list_separator;
                break;
            default:
                layoutRes = R.layout.oui_layout_app_picker_list;
                break;
        }

        View view = LayoutInflater.from(mContext).inflate(layoutRes, parent, false);

        ViewGroup widgetContainer = (ViewGroup) view.findViewById(R.id.widget_frame);
        if (widgetContainer != null) {
            switch (mType) {
                case AppPickerView.TYPE_LIST_NO_WIDGET:
                case AppPickerView.TYPE_LIST_SWITCH:
                case AppPickerView.TYPE_LIST_SWITCH_2:
                    LayoutInflater.from(mContext).inflate(R.layout.oui_layout_app_picker_widget_switch, widgetContainer, true);
                    break;
                case AppPickerView.TYPE_LIST_ACTION_BUTTON:
                    LayoutInflater.from(mContext).inflate(R.layout.oui_layout_app_picker_widget_actionbutton, widgetContainer, true);
                    break;
                case AppPickerView.TYPE_LIST_CHECKBOX:
                case AppPickerView.TYPE_LIST_CHECKBOX_2:
                    LayoutInflater.from(mContext).inflate(R.layout.oui_layout_app_picker_widget_checkbox, (ViewGroup) view.findViewById(R.id.left_frame), true);
                    break;
                case AppPickerView.TYPE_LIST_RADIO_BUTTON:
                    view.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.oui_app_picker_list_radio_padding_start), 0, mContext.getResources().getDimensionPixelSize(R.dimen.oui_app_picker_list_radio_padding_end), 0);
                    LayoutInflater.from(mContext).inflate(R.layout.oui_layout_app_picker_widget_radiobutton, (ViewGroup) view.findViewById(R.id.left_frame), true);
                    break;
            }
        }

        limitFontLarge((TextView) view.findViewById(R.id.title));
        limitFontLarge((TextView) view.findViewById(R.id.summary));

        if (viewType == VIEW_TYPE_HEADER && hasAllAppsInList()) {
            return new AppPickerView.HeaderViewHolder(view);
        } else if (viewType == VIEW_TYPE_FOOTER) {
            return new AppPickerView.FooterViewHolder(view);
        } else if (viewType == VIEW_TYPE_SEPARATOR) {
            return new AppPickerView.SeparatorViewHolder(view);
        } else {
            return new AppPickerView.ViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getDataSet().get(position).isSeparator()) {
            return VIEW_TYPE_SEPARATOR;
        } else if (position == 0 && hasAllAppsInList()) {
            return VIEW_TYPE_HEADER;
        } else if (position == getItemCount() + -1) {
            return VIEW_TYPE_FOOTER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    void onBindViewHolderAction(AppPickerView.ViewHolder vh, int position, String packageName) {
        if (getItemViewType(position) != VIEW_TYPE_SEPARATOR) {
            switch (mType) {
                case AppPickerView.TYPE_LIST_NO_WIDGET:
                    vh.getWidgetContainer().setVisibility(View.GONE);
                    vh.getLeftContainer().setVisibility(View.GONE);
                    break;
                case AppPickerView.TYPE_LIST_ACTION_BUTTON:
                    vh.getWidgetContainer().setVisibility(View.VISIBLE);
                    vh.getActionButton().setVisibility(View.VISIBLE);
                    break;
                case AppPickerView.TYPE_LIST_CHECKBOX:
                case AppPickerView.TYPE_LIST_CHECKBOX_2:
                    vh.getLeftContainer().setVisibility(View.VISIBLE);
                    vh.getWidgetContainer().setVisibility(View.GONE);
                    vh.getItem().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            vh.getCheckBox().setChecked(!vh.getCheckBox().isChecked());
                        }
                    });
                    if (((AccessibilityManager) mContext.getSystemService(Context.ACCESSIBILITY_SERVICE)).isEnabled()) {
                        vh.getCheckBox().setFocusable(false);
                        vh.getCheckBox().setClickable(false);
                        vh.getItem().setContentDescription(null);
                    }
                    break;
                case AppPickerView.TYPE_LIST_RADIO_BUTTON:
                    vh.getLeftContainer().setVisibility(View.VISIBLE);
                    vh.getWidgetContainer().setVisibility(View.GONE);
                    vh.getItem().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            vh.getRadioButton().setChecked(!vh.getRadioButton().isChecked());
                        }
                    });
                    if (((AccessibilityManager) mContext.getSystemService(Context.ACCESSIBILITY_SERVICE)).isEnabled()) {
                        vh.getRadioButton().setFocusable(false);
                        vh.getRadioButton().setClickable(false);
                        vh.getItem().setContentDescription(null);
                    }
                    break;
                case AppPickerView.TYPE_LIST_SWITCH:
                case AppPickerView.TYPE_LIST_SWITCH_2:
                    vh.getLeftContainer().setVisibility(View.GONE);
                    vh.getWidgetContainer().setVisibility(View.VISIBLE);
                    vh.getItem().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            vh.getSwitch().setChecked(!vh.getSwitch().isChecked());
                        }
                    });
                    if (((AccessibilityManager) mContext.getSystemService(Context.ACCESSIBILITY_SERVICE)).isEnabled()) {
                        vh.getSwitch().setFocusable(false);
                        vh.getSwitch().setClickable(false);
                        vh.getItem().setContentDescription(null);
                    }
                    break;
            }
        }
    }
}
