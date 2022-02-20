package rk.android.app.privacydashboard.activity.permission.log.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.dlyt.yanndroid.oneui.view.RecyclerView;
import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.util.Constants;
import rk.android.app.privacydashboard.model.Logs;
import rk.android.app.privacydashboard.util.Permissions;
import rk.android.app.privacydashboard.util.Utils;

public class LogsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final Context context;
    private List<Logs> logsList = new ArrayList<>();
    private final HashMap<Integer,String> dates = new HashMap<>();
    private final String permission;

    public LogsAdapter(Context context, String permission) {
        this.context = context;
        this.permission = permission;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.oui_layout_logs_header, parent, false);
            return new HeaderViewHolder(itemView);
        } else {
            View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.oui_view_permission_detail_item_layout, parent, false);
            LogsViewHolder viewHolder = new LogsViewHolder(inflatedView);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LogsViewHolder) {
            LogsViewHolder logsView = (LogsViewHolder) holder;
            final Logs previousItem = position - 1 == 0 ? null : logsList.get(position - 1);
            final Logs item = logsList.get(position);
            final Logs nextItem = position >= getItemCount() - 1 ? null : logsList.get(position + 1);
            String itemDate = Utils.getDateFromTimestamp(context, item.startTimestamp);

            logsView.detailBackgroundView.setOnClickListener(v -> Utils.openAppPermissionsActivity(context, logsList.get(position).packageName));

            boolean hideUpperLine = previousItem != null && !itemDate.equals(Utils.getDateFromTimestamp(context, previousItem.startTimestamp));
            if (position == 1 ||  hideUpperLine) {
                logsView.upperLine.setVisibility(View.GONE);
            }
            boolean hideLowerLine = nextItem != null && !itemDate.equals(Utils.getDateFromTimestamp(context, nextItem.startTimestamp));
            if (hideLowerLine || getItemCount() <= position + 1) {
                logsView.lowerLine.setVisibility(View.GONE);
            }

            if (!dates.containsValue(itemDate) | dates.containsKey(position)) {
                logsView.monthDateTextView.setText(itemDate);
                dates.put(position, itemDate);
            } else {
                logsView.monthDateLayout.setVisibility(View.GONE);
            }

            logsView.appIcon.setImageDrawable(Utils.getIconFromPackageName(context, item.packageName));
            logsView.appNameTextView.setText(Utils.getNameFromPackageName(context, item.packageName));

            logsView.timestampContainer.removeAllViews();
            if (item.endTimestamp == 0L) {
                logsView.timestampContainer.addView(createTimestampItem(logsView.timestampContainer, Constants.STATE_INVALID, item.startTimestamp));
            } else {
                logsView.timestampContainer.addView(createTimestampItem(logsView.timestampContainer, Constants.STATE_ON, item.startTimestamp));
                logsView.timestampContainer.addView(createTimestampItem(logsView.timestampContainer, Constants.STATE_OFF, item.endTimestamp));
            }
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerView = (HeaderViewHolder) holder;
            headerView.icon.setImageResource(Permissions.getIcon(permission));
            String permission_name = Permissions.getName(context, permission);
            headerView.title.setText(permission_name);
            int usedCount = getItemCount() - 1;
            headerView.info.setText(context.getResources().getQuantityString(R.plurals.oui_used_d_times, usedCount, usedCount));
        }
    }

    private View createTimestampItem(LinearLayout root, int state, long timestamp) {
        String stateType;
        switch (state) {
            case Constants.STATE_ON:
                stateType = context.getString(R.string.log_permission_start);
                break;
            case Constants.STATE_OFF:
                stateType = context.getString(R.string.log_permission_stop);
                break;
            case Constants.STATE_INVALID:
            default:
                stateType = context.getString(R.string.log_permission_invalid);
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.oui_view_permission_detail_timestamp_layout, root, false);
        ((TextView) itemView.findViewById(R.id.detail_timestamp_type)).setText(stateType);
        ((TextView) itemView.findViewById(R.id.detail_timestamp)).setText(Utils.getTimeFromTimestamp(context, timestamp));
        return itemView;
    }

    public void setLogsList(List<Logs> logsList) {
        this.logsList.clear();
        this.logsList.addAll(logsList);
        logsList.add(0, new Logs(0, 0, "header", "", 0, ""));
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return position;
        }
    }

    @Override
    public int getItemCount() {
        return logsList.size();
    }
}
