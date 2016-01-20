package com.joshdevs.josh.readingsschedulev2.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Josh on 2016-01-01.
 */
public class ReadingDateModel implements Parcelable{
    private String courseName;
    private String readingName;
    private long readingDate;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getReadingName() {
        return readingName;
    }

    public void setReadingName(String readingName) {
        this.readingName = readingName;
    }

    public void setReadingDate(long readingDate) {
        this.readingDate = readingDate;
    }

    public long getReadingDate() {
        return readingDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
