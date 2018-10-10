package com.marcospoerl.simplypace.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.marcospoerl.simplypace.R;
import com.marcospoerl.simplypace.model.Term;

public abstract class HolderView extends CardView {

    private boolean editable;

    protected TextView captionTextView;
    protected TextView valueTextView;
    protected Term term;

    public HolderView(Context context) {
        super(context);
    }

    public HolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HolderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.captionTextView = (TextView) findViewById(R.id.caption);
        this.valueTextView = (TextView) findViewById(R.id.value);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        this.valueTextView.setEnabled(editable);
    }

    public boolean isEditable() {
        return editable;
    }

    public abstract void showCaption(int position);

    public void bind(Term term) {
        this.term = term;
        bind();
    }

    public Term getTerm() {
        return term;
    }

    protected abstract void bind();
}
