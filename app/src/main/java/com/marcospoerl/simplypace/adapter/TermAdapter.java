package com.marcospoerl.simplypace.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.marcospoerl.simplypace.R;
import com.marcospoerl.simplypace.model.Term;
import com.marcospoerl.simplypace.views.HolderView;
import com.marcospoerl.simplypace.views.HolderViewFactory;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.LinkedList;

public class TermAdapter extends DragItemAdapter<Term, TermAdapter.ViewHolder> {

    public interface OnItemClickedListener {
        void onItemClicked(HolderView holderView);
    }

    private OnItemClickedListener onItemClickedListener;

    public TermAdapter() {
        setItemList(new LinkedList<Term>());
        setHasStableIds(true);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public void add(Term term, int location) {
        if (location > getItemList().size()) {
            getItemList().add(term);
        } else {
            getItemList().add(location, term);
        }
    }

    public int getPosition(Term term) {
        return getItemList().indexOf(term);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemList().get(position).getType();
    }

    @Override
    public long getUniqueItemId(int position) {
        return getItemList().get(position).getType();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final HolderView holderView = HolderViewFactory.create(viewType, parent);
        return new ViewHolder(holderView, R.id.drag_handle, false);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder.itemView instanceof HolderView) {
            final Term term = getItemList().get(position);
            final HolderView holderView = (HolderView) holder.itemView;
            holderView.showCaption(position);
            holderView.setEditable(position != 2);
            holderView.bind(term);
        }
    }

    public class ViewHolder extends DragItemAdapter.ViewHolder {

        ViewHolder(View itemView, int handleResId, boolean dragOnLongPress) {
            super(itemView, handleResId, dragOnLongPress);
        }

        @Override
        public void onItemClicked(View view) {
            if (onItemClickedListener != null && view instanceof HolderView) {
                onItemClickedListener.onItemClicked((HolderView) view);
            }
        }
    }
}
