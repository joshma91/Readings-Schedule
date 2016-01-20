package com.joshdevs.josh.readingsschedulev2;

import java.util.ArrayList;

/**
 * Created by Josh on 2015-09-11.
 */
public class EnumUtilities <E extends Enum<E>> {

    public static ArrayList<String> getCurrentList(ArrayList<ReadingsMap> mapArrayList, long currentDate, long previousClass, long nextClass) {

        ArrayList<String> list = new ArrayList<>();

        for (ReadingsMap readingsMap : mapArrayList) {
            long readingDate = readingsMap.getClassDate();
            if (readingDate > currentDate && readingDate > previousClass && readingDate == nextClass) {
                list.add(readingsMap.getReading());
            }
        }

        return list;
    }

    public static ArrayList<String> getMissingList(ArrayList<ReadingsMap> mapArrayList, long currentDate, long previousClass, long nextClass) {

        ArrayList<String> list = new ArrayList<>();

        for (ReadingsMap readingsMap : mapArrayList) {
            long readingDate = readingsMap.getClassDate();
            if (readingDate <= currentDate) {
                list.add(readingsMap.getReading());
            }
        }

        return list;
    }

    public static ArrayList<String> getCompletedList(ArrayList<ReadingsMap> mapArrayList, long currentDate, long previousClass, long nextClass) {

        ArrayList<String> list = new ArrayList<>();

        for (ReadingsMap readingsMap : mapArrayList) {
            long readingDate = readingsMap.getClassDate();
            if (readingDate <= currentDate) {
                list.add(readingsMap.getReading());
            }
        }

        return list;
    }

    public static ArrayList<String> getFutureList(ArrayList<ReadingsMap> mapArrayList, long currentDate, long previousClass, long nextClass) {

        ArrayList<String> list = new ArrayList<>();

        for (ReadingsMap readingsMap : mapArrayList) {
            long readingDate = readingsMap.getClassDate();
            if (readingDate > nextClass && readingDate > currentDate) {
                list.add(readingsMap.getReading());
            }
        }

        return list;
    }

    public static ArrayList<String> getFullList(ArrayList<ReadingsMap> mapArrayList, long currentDate, long previousClass, long nextClass){

        ArrayList<String> list = new ArrayList<>();

        for(ReadingsMap readingsMap: mapArrayList){
            list.add(readingsMap.getReading());
        }

        return list;
    }
}
