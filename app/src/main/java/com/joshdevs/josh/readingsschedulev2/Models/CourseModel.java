package com.joshdevs.josh.readingsschedulev2.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Josh on 2015-12-26.
 */
public class CourseModel implements Parcelable {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    private ArrayList<ReadingEntryModel> readingEntryModelArray;
    private String courseTitle;

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setReadingEntryModelArray(ArrayList<ReadingEntryModel> readingEntryModelArray) {
        this.readingEntryModelArray = readingEntryModelArray;
    }

    public ArrayList<ReadingEntryModel> getReadingEntryModelArray() {
        return readingEntryModelArray;
    }


}
