package com.example.anuja.trendingnewsapp.model;

public class MainModel {

    /**
     * The selected item position in the navigation
     * drawer
     */
    private int selectedPosition;
    private String selectedPositionTitle;

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public String getSelectedPositionTitle() {
        return selectedPositionTitle;
    }

    public void setSelectedPositionTitle(String selectedPositionTitle) {
        this.selectedPositionTitle = selectedPositionTitle;
    }
}
