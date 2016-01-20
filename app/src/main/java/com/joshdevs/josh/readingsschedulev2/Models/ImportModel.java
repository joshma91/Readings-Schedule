package com.joshdevs.josh.readingsschedulev2.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Josh on 2015-12-26.
 */
public class ImportModel implements Parcelable{
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    private ArrayList<CourseModel> courses;
    private ArrayList<ReadingDateModel> dates;

    private static ImportModel instance = null;

    public ImportModel() {
        courses = new ArrayList<>();
        dates = new ArrayList<>();
    }

    public static ImportModel getInstance() {
        if (instance == null) {
            instance = new ImportModel();

        }
        return instance;
    }

    public void resetImport(){
        courses = new ArrayList<>();
        dates = new ArrayList<>();
    }

    public ArrayList<ReadingDateModel> getDates() {
        return dates;
    }

    public void addDate(ReadingDateModel date) {
        this.dates.add(date);
    }

    public void setDates(ArrayList<ReadingDateModel> dates) {
        this.dates = dates;
    }

    public ArrayList<CourseModel> getCourses() {
        return courses;
    }

    public void addCourse(CourseModel course) {
        this.courses.add(course);
    }


}
