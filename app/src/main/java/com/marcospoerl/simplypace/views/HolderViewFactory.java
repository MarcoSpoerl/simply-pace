package com.marcospoerl.simplypace.views;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.marcospoerl.simplypace.R;
import com.marcospoerl.simplypace.model.Term;

public class HolderViewFactory {

    public static HolderView create(int type, ViewGroup parent) {
        switch (type) {
            case Term.TYPE_DISTANCE:
                return (HolderView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_distance, parent, false);
            case Term.TYPE_TIME:
                return (HolderView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_time, parent, false);
            case Term.TYPE_PACE:
                return (HolderView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pace, parent, false);
            default:
                return null;
        }
    }
}
