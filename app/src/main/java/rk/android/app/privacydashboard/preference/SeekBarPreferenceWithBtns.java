package rk.android.app.privacydashboard.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import de.dlyt.yanndroid.oneui.preference.PreferenceViewHolder;
import de.dlyt.yanndroid.oneui.preference.SeekBarPreference;

import rk.android.app.privacydashboard.R;
import rk.android.app.privacydashboard.widget.FixedSeekBar;

public class SeekBarPreferenceWithBtns extends SeekBarPreference implements View.OnLongClickListener, View.OnTouchListener {
    private Context context;
    private boolean showTickMark;
    private boolean isLongKeyProcessing = false;
    private final Handler longPressHandler = new LongPressHandler(this);
    private ImageView addButton;
    private ImageView deleteButton;

    public SeekBarPreferenceWithBtns(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        setLayoutResource(R.layout.oui_view_seekbar_preference_layout);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekBarPreferenceWithBtns, defStyleAttr, defStyleRes);
        showTickMark = a.getBoolean(R.styleable.SeekBarPreferenceWithBtns_showTickMark, false);
    }

    public SeekBarPreferenceWithBtns(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeekBarPreferenceWithBtns(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekBarPreferenceWithBtns(@NonNull Context context) {
        this(context, null);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder viewHolder) {
        super.onBindViewHolder(viewHolder);

        View itemView = viewHolder.itemView;
        FixedSeekBar seekBar = itemView.findViewById(R.id.seekbar);
        addButton = itemView.findViewById(R.id.add_button);
        deleteButton = itemView.findViewById(R.id.delete_button);

        if (showTickMark) {
            seekBar.setTickMark(context.getDrawable(R.drawable.oui_seekbar_tick_mark));
        }

        addButton.setOnLongClickListener(this);
        deleteButton.setOnLongClickListener(this);
        addButton.setOnTouchListener(this);
        deleteButton.setOnTouchListener(this);
        addButton.setEnabled(isEnabled());
        deleteButton.setEnabled(isEnabled());
    }

    @Override
    public boolean onLongClick(View view) {
        isLongKeyProcessing = true;

        if (view.getId() != R.id.delete_button && view.getId() != R.id.add_button) {
            return false;
        }

        new Thread(() -> {
            while (isLongKeyProcessing) {
                longPressHandler.sendEmptyMessage(view.getId() == R.id.delete_button ? 1 : 2);
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Log.w(SeekBarPreferenceWithBtns.class.getSimpleName(), "InterruptedException!", e);
                }
            }
        }).start();

        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int action = motionEvent.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!isLongKeyProcessing) {
                    longPressHandler.sendEmptyMessage(view.getId() == R.id.delete_button ? 1 : 2);
                    view.playSoundEffect(SoundEffectConstants.CLICK);
                } else {
                    isLongKeyProcessing = false;
                    longPressHandler.removeMessages(1);
                    longPressHandler.removeMessages(2);
                }
                break;
        }

        return false;
    }

    private void onAddButtonClicked() {
        int value;

        if (getMax() == 100) {
            value = getValue() + 10;
        } else  {
            value = getValue() + 1;
        }

        if (value > getMax()) {
            value = getMax();
        }

        if (value != getValue() && callChangeListener(value)) {
            setValue(value);
            if (isLongKeyProcessing) {
                addButton.playSoundEffect(SoundEffectConstants.CLICK);
            }
        }
    }

    private void onDeleteButtonClicked() {
        int value;

        if (getMax() == 100) {
            value = getValue() - 10;
        } else  {
            value = getValue() - 1;
        }

        if (value < getMin()) {
            value = getMin();
        }

        if (value != getValue() && callChangeListener(value)) {
            setValue(value);
            if (isLongKeyProcessing) {
                deleteButton.playSoundEffect(SoundEffectConstants.CLICK);
            }
        }
    }


    private static class LongPressHandler extends Handler {
        private final WeakReference<SeekBarPreferenceWithBtns> weakReference;

        LongPressHandler(SeekBarPreferenceWithBtns seekBarPref) {
            super(Looper.getMainLooper());
            weakReference = new WeakReference<>(seekBarPref);
        }

        @Override
        public void handleMessage(Message msg) {
            SeekBarPreferenceWithBtns seekBarPref = weakReference.get();
            if (msg.what == 1) {
                seekBarPref.onDeleteButtonClicked();
            } else if (msg.what == 2) {
                seekBarPref.onAddButtonClicked();
            }
        }
    }
}
