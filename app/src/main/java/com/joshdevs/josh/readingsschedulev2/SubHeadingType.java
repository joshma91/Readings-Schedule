package com.joshdevs.josh.readingsschedulev2;

/**
 * Created by Josh on 2015-12-29.
 */
public enum SubHeadingType {
    CURRENT,
    FUTURE,
    MISSED,
    COMPLETED,
    NODATE_INCOMPLETED,
    NODATE_COMPLETED;


    public int stringID;
    public int readingListID;

    SubHeadingType(){

    }

    public int getReadingListID() {
        return readingListID;
    }

    public void setReadingListID(int readingListID) {
        this.readingListID = readingListID;
    }

    public int getStringID() {
        return stringID;
    }

    public void setStringID(int stringID) {
        this.stringID = stringID;
    }

    public static int[] getCoursesStringArray(){
        int[] list = new int[7];
        int i = 0;
        for(SubHeadingType type: values()){
            list[i] = type.getStringID();
            i++;
        }
        return list;
    }
}
