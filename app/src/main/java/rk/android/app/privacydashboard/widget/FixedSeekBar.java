package rk.android.app.privacydashboard.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.reflect.view.SeslHapticFeedbackConstantsReflector;

import de.dlyt.yanndroid.oneui.widget.SeekBar;

public class FixedSeekBar extends SeekBar {
    boolean centerBasedBar = false;
    boolean levelBar = false;
    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener;

    public FixedSeekBar(Context context, AttributeSet set) {
        super(context, set);
    }

    @Override
    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener l) {
        onSeekBarChangeListener = l;
        if (!levelBar) {
            super.setOnSeekBarChangeListener(l);
        }
    }

    @Override
    protected void drawTickMarks(Canvas canvas) {
        if (centerBasedBar) {
            Drawable tickMark = getTickMark();
            if (tickMark != null) {
                final int right = tickMark.getIntrinsicWidth() >= 0 ? tickMark.getIntrinsicWidth() / 2 : 1;
                final int bottom = tickMark.getIntrinsicHeight() >= 0 ? tickMark.getIntrinsicHeight() / 2 : 1;
                final int top = -bottom;
                final int left = -right;
                tickMark.setBounds(left, top, right, bottom);

                float dx = ((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) / 2.0f;
                int save = canvas.save();
                canvas.translate((float) getPaddingLeft(), ((float) getHeight()) / 2.0f);
                for (int i = 0; i <= 2; i++) {
                    tickMark.draw(canvas);
                    canvas.translate(dx, 0.0f);
                }
                canvas.restoreToCount(save);
            }
        } else {
            super.drawTickMarks(canvas);
        }
    }

    @Override
    public void setTickMark(Drawable drawable) {
        super.setTickMark(drawable);

        final boolean validDrawable = drawable != null;
        if (levelBar != validDrawable) {
            levelBar = validDrawable;
            if (validDrawable) {
                setProgressTintList(getContext().getColorStateList(android.R.color.transparent));

                super.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    final int HAPTIC_CONSTANT_CURSOR_MOVE = SeslHapticFeedbackConstantsReflector.semGetVibrationIndex(41);

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (levelBar && fromUser) {
                            if (centerBasedBar) {
                                if (progress == (getMin() + getMax()) / 2 || progress == getMin() || progress == getMax()) {
                                    seekBar.performHapticFeedback(HAPTIC_CONSTANT_CURSOR_MOVE);
                                }
                            } else {
                                seekBar.performHapticFeedback(HAPTIC_CONSTANT_CURSOR_MOVE);
                            }
                        }

                        if (onSeekBarChangeListener != null) {
                            onSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        if (onSeekBarChangeListener != null) {
                            onSeekBarChangeListener.onStartTrackingTouch(seekBar);
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if (onSeekBarChangeListener != null) {
                            onSeekBarChangeListener.onStopTrackingTouch(seekBar);
                        }
                    }
                });
            } else {
                super.setOnSeekBarChangeListener(onSeekBarChangeListener);
            }
        }
    }

    public void setCenterBasedBar(boolean centerBased) {
        centerBasedBar = centerBased;
        invalidate();
    }
}