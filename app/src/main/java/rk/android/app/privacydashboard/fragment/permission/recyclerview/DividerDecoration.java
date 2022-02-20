package rk.android.app.privacydashboard.fragment.permission.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import de.dlyt.yanndroid.oneui.view.RecyclerView;

public class DividerDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTR = {android.R.attr.listDivider};
    private Drawable divider;
    private int leftPadding = 0;
    private int rightPadding = 0;

    public DividerDecoration(Context context) {
        TypedArray a = context.obtainStyledAttributes(ATTR);
        divider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void seslOnDispatchDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.seslOnDispatchDraw(c, parent, state);

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (parent.getChildViewHolder(child).getItemViewType() != 0) {
                final int left = parent.getPaddingStart() + leftPadding;
                final int top = child.getBottom() + ((ViewGroup.MarginLayoutParams) child.getLayoutParams()).bottomMargin;
                final int right = parent.getWidth() - parent.getPaddingEnd() + rightPadding;
                final int bottom = divider.getIntrinsicHeight() + top;
                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }

    public void setDividerPadding(int left, int right) {
        leftPadding = left;
        rightPadding = right;
    }
}
