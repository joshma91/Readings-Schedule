package com.joshdevs.josh.readingsschedulev2.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Josh on 2015-12-26.
 */
public class ReadingEntryModel implements Parcelable {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    String readingName;
    long readingDateInMillis;
    long nextReadingDate;

    public long getNextReadingDate() {
        return nextReadingDate;
    }

    public void setNextReadingDate(long nextReadingDate) {
        this.nextReadingDate = nextReadingDate;
    }

    public long getReadingDateInMillis() {
        return readingDateInMillis;
    }

    public void setReadingDateInMillis(long readingDateInMillis) {
        this.readingDateInMillis = readingDateInMillis;
    }

    public String getReadingName() {
        return readingName;
    }

    public void setReadingName(String readingName) {
        this.readingName = readingName;

    }

}
