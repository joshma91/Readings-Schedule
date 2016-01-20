package com.joshdevs.josh.readingsschedulev2;


import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by joshma on 2015-06-10.
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener{

    private T item;
    protected OnClickListener onItemClickListener;
    protected boolean needsFullWidth = false;


    public BaseViewHolder(ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        setupView(itemView);
    }

    public BaseViewHolder(View itemView) {
        super(itemView);
        setupView(itemView);

    }

    protected abstract void setupView(View view);

    protected abstract void loadItem(T item);

    public void bind(T item) {
        this.item = item;
        loadItem(item);

        if (isClickable()) {
            itemView.setOnClickListener(this);
        }
    }


    public void onRecycle() {

    }

    public void onDetach() {

    }

    public boolean isClickable() {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onClick(BaseViewHolder.this, v);
        }
    }


    public Context getContext() {
        return itemView.getContext();
    }

    public Resources getResources() {
        return itemView.getResources();
    }

    public String getString(int id) {
        return itemView.getResources().getString(id);
    }

    public T getItem() {
        return item;
    }

    public void setOnClickListener(OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnClickListener {
        void onClick(BaseViewHolder holder, View view);

        void onLoseFocus(BaseViewHolder holder);

        void onGainFocus(BaseViewHolder holder);
    }
}
