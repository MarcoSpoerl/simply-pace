package com.marcospoerl.simplypace.views;

import android.content.Context;
import android.util.AttributeSet;

import com.marcospoerl.simplypace.R;
import com.marcospoerl.simplypace.model.DistanceTerm;

public class DistanceView extends HolderView {

    public DistanceView(Context context) {
        super(context);
    }

    public DistanceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DistanceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void showCaption(int position) {
        switch (position) {
            case 0:
                this.captionTextView.setText(R.string.distance_caption_0);
                break;
            case 1:
                this.captionTextView.setText(R.string.distance_caption_1);
                break;
            case 2:
                this.captionTextView.setText(R.string.distance_caption_2);
                break;
        }
    }

    @Override
    protected void bind() {
        final DistanceTerm distanceTerm = (DistanceTerm) this.term;
        final double km = distanceTerm.getDistanceInKilometer().doubleValue();
        final String text = String.format("%.3f km", km);
        this.valueTextView.setText(text);
    }
}
