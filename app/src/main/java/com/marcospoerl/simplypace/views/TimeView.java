package com.marcospoerl.simplypace.views;

import android.content.Context;
import android.util.AttributeSet;

import com.marcospoerl.simplypace.R;
import com.marcospoerl.simplypace.model.TimeTerm;

import java.util.concurrent.TimeUnit;

public class TimeView extends HolderView {

    public TimeView(Context context) {
        super(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void showCaption(int position) {
        switch (position) {
            case 0:
                this.captionTextView.setText(R.string.time_caption_0);
                break;
            case 1:
                this.captionTextView.setText(R.string.time_caption_1);
                break;
            case 2:
                this.captionTextView.setText(R.string.time_caption_2);
                break;
        }
    }

    @Override
    protected void bind() {
        final TimeTerm timeTerm = (TimeTerm) this.term;
        final long seconds = timeTerm.getTimeInSeconds().longValue();
        final String text = String.format("%02dh:%02dm:%02ds",
                TimeUnit.SECONDS.toHours(seconds),
                TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(seconds)),
                TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds)));
        this.valueTextView.setText(text);
    }
}
