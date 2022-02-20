package rk.android.app.privacydashboard.fragment.permission.recyclerview;

import android.content.Context;
import android.graphics.Canvas;

import androidx.annotation.NonNull;

import de.dlyt.yanndroid.oneui.sesl.utils.SeslRoundedCorner;
import de.dlyt.yanndroid.oneui.view.RecyclerView;

public class RoundedCornerDecoration extends RecyclerView.ItemDecoration {
    private SeslRoundedCorner mListRoundedCorner;
    //private SeslRoundedCorner mBottomRoundedCorner;

    public RoundedCornerDecoration(Context context) {
        mListRoundedCorner = new SeslRoundedCorner(context);
        mListRoundedCorner.setRoundedCorners(SeslRoundedCorner.ROUNDED_CORNER_ALL);
        //mBottomRoundedCorner = new SeslRoundedCorner(context);
    }

    @Override
    public void seslOnDispatchDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.seslOnDispatchDraw(c, parent, state);

        for (int i = 0; i < parent.getChildCount(); i++) {
            /*View childAt = recyclerView.getChildAt(i);
            int i2 = ((C1875v) recyclerView.m3867E1(childAt)).f7116u;
            int i3 = 15;
            if (i2 == 15) {
                bVar = this.f7113b;
            } else if ((i2 & 1) != 0 || (i2 & 2) != 0) {
                bVar = this.f7113b;
                i3 = 3;
            } else if ((i2 & 4) == 0 && (i2 & 8) == 0) {
                this.f7113b.m7888f(0);
                this.f7113b.mo7883b(childAt, canvas);
            } else {
                bVar = this.f7113b;
                i3 = 12;
            }
            bVar.m7888f(i3);
            this.f7113b.mo7883b(childAt, canvas);*/
        }

        mListRoundedCorner.drawRoundedCorner(c);
    }

}
