package com.joshdevs.josh.readingsschedulev2.Models;


/**
 * Created by joshma on 2015-06-10.
 */
public class HeaderModel extends RowModel {
    private String infoText;
    private String subtitle;
    private boolean hasdivider;
    private boolean hasEdit;


    public HeaderModel() {
        this.type = Types.HEADER;
        this.infoText = null;
        this.hasEdit = false;
    }



    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setHasDivider(boolean hasdivider) {
        this.hasdivider = hasdivider;
    }

    public boolean hasdivider() {
        return hasdivider;
    }

    public void setHasEdit(boolean hasEdit) {
        this.hasEdit = hasEdit;
    }

    public boolean hasEdit() {
        return hasEdit;
    }
}

