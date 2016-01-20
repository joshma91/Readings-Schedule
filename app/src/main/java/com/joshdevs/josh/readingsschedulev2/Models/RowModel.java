package com.joshdevs.josh.readingsschedulev2.Models;

/**
 * Created by Josh on 2015-09-08.
 */
public class RowModel {

        public String title;
        public Types type;
        public ViewState viewState;
        private int errorFieldIndex;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public RowModel() {
            this.viewState = ViewState.SHOWN;
            errorFieldIndex = -1;
        }

        public Types getType() {
            return type;
        }


        public void setViewState(ViewState viewState) {
            this.viewState = viewState;
        }

        public ViewState getViewState() {
            return viewState;
        }

        public enum Types {
            HEADER, SUBHEADER, READING
        }

        public enum ViewState{
            HIDDEN, SHOWN
        }

        public int getErrorFieldIndex() {
            return errorFieldIndex;
        }

        public void setErrorFieldIndex(int errorFieldIndex) {
            this.errorFieldIndex = errorFieldIndex;
        }



}
