package com.marcospoerl.simplypace.views;

import android.content.Context;
import android.util.AttributeSet;

import com.marcospoerl.simplypace.R;
import com.marcospoerl.simplypace.model.PaceTerm;

import java.util.concurrent.TimeUnit;

public class PaceView extends HolderView {

    public PaceView(Context context) {
        super(context);
    }

    public PaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void showCaption(int position) {
        switch (position) {
            case 0:
                this.captionTextView.setText(R.string.pace_caption_0);
                break;
            case 1:
                this.captionTextView.setText(R.string.pace_caption_1);
                break;
            case 2:
                this.captionTextView.setText(R.string.pace_caption_2);
                break;
        }
    }

    @Override
    protected void bind() {
        final PaceTerm paceTerm = (PaceTerm) this.term;
        final long seconds = paceTerm.getSecondsPerKilometer().longValue();
        final String text = String.format("%d:%02d min/km",
                TimeUnit.SECONDS.toMinutes(seconds),
                TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds)));
        this.valueTextView.setText(text);
    }
}
