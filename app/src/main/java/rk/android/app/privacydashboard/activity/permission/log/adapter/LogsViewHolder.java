package rk.android.app.privacydashboard.activity.permission.log.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;

import de.dlyt.yanndroid.oneui.view.RecyclerView;
import rk.android.app.privacydashboard.R;

public class LogsViewHolder extends RecyclerView.ViewHolder {
    public View detailBackgroundView;
    public View upperLine, lowerLine;
    public ImageView appIcon;
    public Group monthDateLayout;
    public TextView monthDateTextView;
    public TextView appNameTextView;
    public LinearLayout timestampContainer;

    public LogsViewHolder(View itemView) {
        super(itemView);

        detailBackgroundView = itemView.findViewById(R.id.detail_item_background);

        upperLine = itemView.findViewById(R.id.detail_upper_line);
        lowerLine = itemView.findViewById(R.id.detail_lower_line);

        appIcon = itemView.findViewById(R.id.detail_list_icon);

        monthDateLayout = itemView.findViewById(R.id.detail_month_date_layout);
        monthDateTextView = itemView.findViewById(R.id.detail_month_date);

        appNameTextView = itemView.findViewById(R.id.detail_item_name);

        timestampContainer = itemView.findViewById(R.id.detail_timestamp_list_container);
    }
}
