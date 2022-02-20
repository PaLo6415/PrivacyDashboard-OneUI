package rk.android.app.privacydashboard.view.internal.apppickerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import de.dlyt.yanndroid.oneui.view.RecyclerView;
import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.view.AppPickerView;

class GridAdapter extends AbsAdapter {
    public GridAdapter(Context context, int type, int order, AppPickerIconLoader iconLoader) {
        super(context, type, order, iconLoader);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.oui_layout_app_picker_grid, parent, false);
        if (mType == AppPickerView.TYPE_GRID_NO_WIDGET) {
            view.findViewById(R.id.check_widget).setVisibility(View.GONE);
        }
        limitFontLarge2LinesHeight((TextView) view.findViewById(R.id.title));
        return new AppPickerView.ViewHolder(view);
    }

    @Override
    void onBindViewHolderAction(AppPickerView.ViewHolder vh, int position, String packageName) {
    }
}
