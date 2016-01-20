package com.joshdevs.josh.readingsschedulev2;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joshdevs.josh.readingsschedulev2.Models.HeaderModel;

/**
 * Created by Josh on 2015-09-09.
 */
public class HeaderViewHolder extends BaseViewHolder<HeaderModel> {

    private TextView titleTV;

    public HeaderViewHolder(ViewGroup parent) {
        super(parent, R.layout.stub_header);
    }

    @Override
    protected void setupView(View view) {

        titleTV = (TextView) view.findViewById(R.id.title);

    }

    @Override
    protected void loadItem(HeaderModel item) {

        titleTV.setText(item.getTitle());

    }
}