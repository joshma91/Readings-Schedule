package com.joshdevs.josh.readingsschedulev2.Models;

import java.util.ArrayList;

/**
 * Created by Josh on 2015-09-09.
 */
public class SubHeaderModel extends RowModel {

    public ArrayList<ReadingModel> readingModelsArray;

    public SubHeaderModel() {
        this.type = Types.SUBHEADER;
        this.viewState = ViewState.HIDDEN;
        readingModelsArray = new ArrayList<>();
    }

    public ArrayList<ReadingModel> getReadingModelsArray() {
        return readingModelsArray;
    }

    public void setReadingModelsArray(ArrayList<ReadingModel> readingModelsArray) {
        this.readingModelsArray = readingModelsArray;
    }
}
