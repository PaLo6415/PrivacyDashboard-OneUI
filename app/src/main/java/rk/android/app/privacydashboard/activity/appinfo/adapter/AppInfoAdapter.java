package rk.android.app.privacydashboard.activity.appinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.dlyt.yanndroid.oneui.view.RecyclerView;
import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.activity.permission.log.adapter.HeaderViewHolder;
import rk.android.app.privacydashboard.activity.permission.log.adapter.LogsViewHolder;
import rk.android.app.privacydashboard.model.Logs;
import rk.android.app.privacydashboard.util.Permissions;
import rk.android.app.privacydashboard.util.Utils;

public class AppInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private final Context context;
    private final List<Logs> logsList = new ArrayList<>();
    private final HashMap<Integer,String> dates = new HashMap<>();
    private final String packageName;

    public AppInfoAdapter(Context context, String packageName){
        this.context = context;
        this.packageName = packageName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.oui_view_permission_detail_item_layout, parent, false);
            LogsViewHolder viewHolder = new LogsViewHolder(inflatedView);
            inflatedView.setOnClickListener(v -> Utils.openAppSettings(context,logsList.get(viewHolder.getAdapterPosition()-1).packageName));
            return viewHolder;
        }

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.oui_layout_logs_header, parent, false);
        return new HeaderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LogsViewHolder) {
            LogsViewHolder logsView = (LogsViewHolder) holder;

            final Logs item = logsList.get(position-1);
            String date = Utils.getDateFromTimestamp(context,item.startTimestamp);

            if (!dates.containsValue(date) | dates.containsKey(position)) {
                logsView.monthDateTextView.setText(date);
                //logsView.textDate.setVisibility(View.VISIBLE);
                //logsView.viewHorizontal.setVisibility(View.VISIBLE);
                dates.put(position,date);
            }else {
                logsView.monthDateLayout.setVisibility(View.GONE);
                //logsView.viewHorizontal.setVisibility(View.GONE);
            }

            //logsView.textTime.setText(Utils.getTimeFromTimestamp(context,item.timestamp));

            logsView.appIcon.setImageResource(Permissions.getIcon(item.permission));
            logsView.appNameTextView.setText(Permissions.getName(context,item.permission));

            /*switch (item.state){
                case Constants.STATE_ON:
                    logsView.textSession.setText(context.getString(R.string.log_permission_start));
                    break;

                case Constants.STATE_OFF:
                    logsView.textSession.setText(context.getString(R.string.log_permission_stop));
                    break;

                case Constants.STATE_INVALID:
                default:
                    logsView.textSession.setText(context.getString(R.string.log_permission_invalid));
            }*/

        }else if (holder instanceof HeaderViewHolder){

            HeaderViewHolder headerView = (HeaderViewHolder) holder;
            headerView.icon.setImageDrawable(Utils.getIconFromPackageName(context,packageName));
                headerView.title.setText(Utils.getNameFromPackageName(context,packageName));
                headerView.info.setText(context.getString(R.string.log_info_app)
                        .replace("#ALIAS#",Utils.getNameFromPackageName(context,packageName)));
        }
    }

    public void setLogsList(List<Logs> logsList) {
        this.logsList.addAll(logsList);
        notifyDataSetChanged();
    }

    public void stopLoading(){
        notifyItemChanged(0);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return logsList.size() + 1;
    }

}
