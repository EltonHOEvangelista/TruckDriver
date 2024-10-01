package com.example.truckdriver_v02.data.vehicle;

public class TrailerModel {
    private int TrailerTypeId;
    private String TrailerTypeName;

    public TrailerModel(int trailerTypeId, String trailerTypeName) {
        TrailerTypeId = trailerTypeId;
        TrailerTypeName = trailerTypeName;
    }

    public TrailerModel(String trailerTypeName) {
        TrailerTypeName = trailerTypeName;
    }

    public int getTrailerTypeId() {
        return TrailerTypeId;
    }

    public void setTrailerTypeId(int trailerTypeId) {
        TrailerTypeId = trailerTypeId;
    }

    public String getTrailerTypeName() {
        return TrailerTypeName;
    }

    public void setTrailerTypeName(String trailerTypeName) {
        TrailerTypeName = trailerTypeName;
    }
}
