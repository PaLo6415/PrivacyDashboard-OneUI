package rk.android.app.privacydashboard.activity.permission.log.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.dlyt.yanndroid.oneui.view.RecyclerView;
import rk.android.app.privacydashboard.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    public ImageView icon;
    public TextView title, info;

    public HeaderViewHolder(View itemView) {
        super(itemView);

        icon = itemView.findViewById(R.id.logs_header_icon);
        title = itemView.findViewById(R.id.logs_header_title);
        info = itemView.findViewById(R.id.logs_header_sub_title);
    }
}
