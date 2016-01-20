package com.joshdevs.josh.readingsschedulev2;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joshdevs.josh.readingsschedulev2.Models.ReadingModel;
import com.joshdevs.josh.readingsschedulev2.Models.SubHeaderModel;

/**
 * Created by Josh on 2015-09-09.
 */
public class SubHeaderViewHolder extends BaseViewHolder<SubHeaderModel> {

    RelativeLayout sectionRL;
    ImageView plusIcon;
    Button headingText;
    LinearLayout readingContainer;

    public SubHeaderViewHolder(ViewGroup parent) {
        super(parent, R.layout.stub_sub_header);
    }


    @Override
    protected void setupView(View view) {
        sectionRL = (RelativeLayout) view.findViewById(R.id.reading_layout);
        plusIcon = (ImageView) view.findViewById(R.id.plus_icon);
        sectionRL.setOnClickListener(this);
        headingText = (Button) view.findViewById(R.id.button);
        readingContainer = (LinearLayout) view.findViewById(R.id.reading_container);
    }

    @Override
    protected void loadItem(SubHeaderModel item) {

        headingText.setText(item.getTitle());
        readingContainer.removeAllViews();

        switch (item.viewState) {
            case HIDDEN:
                plusIcon.setImageResource(R.mipmap.plus_icon);
                readingContainer.setVisibility(View.GONE);
                break;
            case SHOWN:
                sectionRL.setVisibility(View.VISIBLE);
                plusIcon.setImageResource(R.mipmap.minus_icon);
                readingContainer.setVisibility(View.VISIBLE);
                loadReadingsList(item);
                break;
        }
    }

    private void clearReadingsList(SubHeaderModel item) {

    }

    private void loadReadingsList(SubHeaderModel item) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService((Context.LAYOUT_INFLATER_SERVICE));
        int itemSize = item.getReadingModelsArray().size();


        for (int i = 0; i < itemSize; i++) {
            ReadingModel readingModel = item.getReadingModelsArray().get(i);
            if(readingModel.getTitle() != null) {
                View view = initializeReadingView(inflater.inflate(R.layout.stub_reading, readingContainer, false), readingModel);
                readingContainer.addView(view);
            }
        }
    }

    private View initializeReadingView(View view, ReadingModel model) {
        TextView textView = (TextView) view.findViewById(R.id.reading_title);
        textView.setText(model.getTitle());

        if(model.isDate()){
            textView.setText(Html.fromHtml("<b>" + model.getCourseName() + "</b>: " + model.getTitle()));
        }

        CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkbox);
        checkbox.setChecked(model.isChecked());

        RelativeLayout readingButton = (RelativeLayout) view.findViewById(R.id.reading_button);
        readingButton.setOnClickListener(this);

        return view;
    }
}