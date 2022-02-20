package rk.android.app.privacydashboard.util;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import de.dlyt.yanndroid.oneui.view.RecyclerView;
import de.dlyt.yanndroid.oneui.widget.ProgressBar;
import rk.android.app.privacydashboard.R;

public class DashboardChart {
    private Context mContext;
    private PermissionInfoTask mPermissionInfoTask;
    private DashboardChartAdapter mDashboardChartAdapter;

    int[] mPrivacyOverViewPermission = {R.string.permission_camera, R.string.permission_microphone, R.string.permission_location};
    String[] mPrivacyOverViewPermissionId = {Constants.PERMISSION_CAMERA, Constants.PERMISSION_MICROPHONE, Constants.PERMISSION_LOCATION};

    private boolean mUpdateFlag;
    int mCameraUsageCount;
    int mLocationUsageCount;
    int mMicrophoneUsageCount;
    int mMaxCount;

    public DashboardChart(Context context) {
        mContext = context;
        mDashboardChartAdapter = getAdapter();
    }

    public DashboardChartAdapter getAdapter() {
        return mDashboardChartAdapter == null ?
                new DashboardChartAdapter(mContext, mPrivacyOverViewPermission, R.layout.oui_view_dashboard_chart_item_layout) : mDashboardChartAdapter;
    }

    public void updateLatestPermissionInfo(int cameraCount, int microphoneCount, int locationCount) {
        if (mUpdateFlag) {
            mDashboardChartAdapter.notifyDataSetChanged();
        }
        if (mPermissionInfoTask == null) {
            mPermissionInfoTask = new PermissionInfoTask();
            mPermissionInfoTask.execute(cameraCount, microphoneCount, locationCount);
        }
    }

    public void cancelGetPermissionInfo() {
        if (mPermissionInfoTask != null) {
            mPermissionInfoTask.cancel(true);
            mPermissionInfoTask = null;
        }
    }


    private class PermissionInfoTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected void onPreExecute() {
            mUpdateFlag = false;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            mCameraUsageCount = params[0];
            mMicrophoneUsageCount = params[1];
            mLocationUsageCount = params[2];
            mMaxCount = Math.max(mMicrophoneUsageCount, Math.max(mCameraUsageCount, mLocationUsageCount));

            return null;
        }

        protected void onPostExecute(Void result) {
            mUpdateFlag = true;
            mDashboardChartAdapter.notifyDataSetChanged();
        }
    }

    public class DashboardChartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public Context mContext;
        public int[] mOverViewList;
        int mRes;

        public DashboardChartAdapter(Context context, int[] list, int resId) {
            mContext = context;
            mOverViewList = list;
            mRes = resId;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView mPermissionImgView;
            TextView mPermissionNameView;
            ProgressBar mProgressBar;
            LinearLayout mUsageBar;
            TextView mUsageCount;

            public ViewHolder(View itemView) {
                super(itemView);
                mPermissionImgView = (ImageView) itemView.findViewById(R.id.item_permission_image);
                mPermissionNameView = (TextView) itemView.findViewById(R.id.item_permission_name);
                mUsageBar = (LinearLayout) itemView.findViewById(R.id.item_usage_bar);
                mUsageCount = (TextView) itemView.findViewById(R.id.item_usage_count);
                mProgressBar = (ProgressBar) itemView.findViewById(R.id.item_progress_bar);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(mRes, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ViewHolder) {
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.mPermissionNameView.setText(mPrivacyOverViewPermission[position]);
                if (mUpdateFlag) {
                    viewHolder.mProgressBar.setVisibility(View.GONE);
                    viewHolder.mUsageCount.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mProgressBar.setVisibility(View.VISIBLE);
                    viewHolder.mUsageCount.setVisibility(View.GONE);
                }

                switch (position) {
                    case 0:
                        viewHolder.mPermissionImgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.oui_icon_perm_group_camera));
                        viewHolder.mUsageBar.getLayoutParams().width = getUsageBarWidth(mCameraUsageCount);
                        viewHolder.mUsageCount.setText(mContext.getResources().getQuantityString(R.plurals.oui_ps_apps, mCameraUsageCount, mCameraUsageCount));
                        break;
                    case 1:
                        viewHolder.mPermissionImgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.oui_icon_perm_group_microphone));
                        viewHolder.mUsageBar.getLayoutParams().width = getUsageBarWidth(mMicrophoneUsageCount);
                        viewHolder.mUsageCount.setText(mContext.getResources().getQuantityString(R.plurals.oui_ps_apps, mMicrophoneUsageCount, mMicrophoneUsageCount));
                        break;
                    case 2:
                        viewHolder.mPermissionImgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.oui_icon_perm_group_location));
                        viewHolder.mUsageBar.getLayoutParams().width = getUsageBarWidth(mLocationUsageCount);
                        viewHolder.mUsageCount.setText(mContext.getResources().getQuantityString(R.plurals.oui_ps_apps, mLocationUsageCount, mLocationUsageCount));
                        break;
                    default:
                        throw new IllegalStateException("wrong view binded");
                }

                viewHolder.itemView.setOnClickListener(v -> Utils.openHistoryActivity(mContext, mPrivacyOverViewPermissionId[position]));
            }
        }

        @Override
        public int getItemCount() {
            return mOverViewList.length;
        }

        public int getUsageBarWidth(int count) {
            return (int) (((float) count / (float) (((getMaxUsageCount() + 9) / 10) * 10)) * mContext.getResources().getDimension(R.dimen.oui_permission_graph_height));
        }

        public int getMaxUsageCount() {
            return mMaxCount;
        }
    }
}
