package com.joshdevs.josh.readingsschedulev2;

/**
 * Created by Josh on 2015-09-11.
 */
public class ReadingsMap {

    private String reading;
    private long classDate;
    private boolean complete;
    private long endDate;

    public ReadingsMap(String reading, long classDate){

        this.reading = reading;
        this.classDate = classDate;

    }


    public ReadingsMap(String reading, long classDate, long endDate){

        this.reading = reading;
        this.classDate = classDate;
        this.endDate = endDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public boolean isComplete() {
        return complete;
    }

    public long getClassDate() {
        return classDate;
    }

    public void setClassDate(long classDate) {
        this.classDate = classDate;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }
}
