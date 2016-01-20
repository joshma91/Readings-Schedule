package com.joshdevs.josh.readingsschedulev2;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.joshdevs.josh.readingsschedulev2.Models.RowModel;

import java.util.ArrayList;

/**
 * Created by Josh on 2015-09-08.
 */
public class BaseFormAdapter  extends  RecyclerView.Adapter<BaseViewHolder<Object>> {

    private Activity parentActivity;
    private ArrayList<RowModel> formRows;
    private BaseViewHolder.OnClickListener onClickListener;

    public BaseFormAdapter(ArrayList<RowModel> formRows, BaseViewHolder.OnClickListener onClickListener, Activity parentActivity) {
        this.formRows = formRows;
        this.onClickListener = onClickListener;
        this.parentActivity = parentActivity;
    }

    public void getItem(int position) {

    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        BaseViewHolder holder = new HeaderViewHolder(viewGroup);

        switch (RowModel.Types.values()[viewType]) {
            case HEADER:
                holder = new HeaderViewHolder(viewGroup);
                break;
            case SUBHEADER:
                holder = new SubHeaderViewHolder(viewGroup);
                break;
            case READING:
                holder = new ReadingViewHolder(viewGroup);
                break;
        }

        if (holder != null) {
            holder.setOnClickListener(onClickListener);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<Object> baseViewHolder, int position) {
        baseViewHolder.bind(formRows.get(position));
    }

    @Override
    public int getItemCount() {
        return formRows.size();
    }

    @Override
    public int getItemViewType(int position) {
        RowModel rowModel = formRows.get(position);

        switch (rowModel.getType()) {
            case HEADER:
                return RowModel.Types.HEADER.ordinal();
            case SUBHEADER:
                return RowModel.Types.SUBHEADER.ordinal();
            case READING:
                return RowModel.Types.READING.ordinal();
            default:
                return RowModel.Types.HEADER.ordinal();
        }
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder<Object> holder) {
        super.onViewAttachedToWindow(holder);
    }


    @Override
    public void onViewDetachedFromWindow(BaseViewHolder<Object> holder) {
        super.onViewDetachedFromWindow(holder);

        holder.onDetach();
    }

}

