package com.joshdevs.josh.readingsschedulev2.Models;

/**
 * Created by Josh on 2015-09-09.
 */
public class ReadingModel extends RowModel {

    boolean checked;
    String courseName;
    boolean isDate;

    public boolean isDate() {
        return isDate;
    }

    public void setDate(boolean date) {
        isDate = date;
    }

    public ReadingModel() {
        this.type = Types.READING;
        this.isDate = false;
        this.checked = false;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public boolean isChecked() {
        return checked;
    }
}

