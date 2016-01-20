package com.joshdevs.josh.readingsschedulev2;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joshdevs.josh.readingsschedulev2.Models.ReadingModel;

/**
 * Created by Josh on 2015-09-09.
 */
public class ReadingViewHolder extends BaseViewHolder<ReadingModel> {

    private RelativeLayout readingRL;
    private TextView readingTV;
    private CheckBox readingCB;
    private RelativeLayout checkButton;

    public ReadingViewHolder(ViewGroup parent){
        super(parent, R.layout.stub_reading);
    }

    @Override
    protected void setupView(View view) {
        readingTV = (TextView) view.findViewById(R.id.reading_title);
        readingRL = (RelativeLayout) view.findViewById(R.id.reading_layout);
        readingCB = (CheckBox) view.findViewById(R.id.checkbox);
        checkButton = (RelativeLayout) view.findViewById(R.id.reading_button);
        checkButton.setOnClickListener(this);
    }

    @Override
    protected void loadItem(ReadingModel item) {
        readingTV.setText(item.getTitle());
        if(item.isDate()){
            readingTV.setText(Html.fromHtml("<b>" + item.getCourseName() + "</b>: " + item.getTitle()));
        }
    }
}
