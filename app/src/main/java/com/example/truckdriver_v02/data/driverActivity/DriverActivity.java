package com.example.truckdriver_v02.data.driverActivity;

public class DriverActivity implements  DriverActivity_Interface{

    private int activityId;
    private String contractor;
    private String contractorEmployee;
    private String contractorPhone;
    private String destination;
    private String TypeOfLoad;
    private String ShippingOffer;
    private int activityStatus;
    private String activityDate;
    private int accountId;

    private int DriverActivityStatus;

    @Override
    public void setDriverActivityId(int Entry) {
        activityId = Entry;
    }

    @Override
    public int getDriverActivityId() {
        return activityId;
    }

    @Override
    public void setDriverActivityContractor(String Entry) {
        contractor = Entry;
    }

    @Override
    public String getDriverActivityContractor() {
        return contractor;
    }

    @Override
    public void setDriverActivityContractorEmployee(String Entry) {
        contractorEmployee = Entry;
    }

    @Override
    public String getDriverActivityContractorEmployee() {
        return contractorEmployee;
    }

    @Override
    public void setDriverActivityContractorPhone(String Entry) {
        contractorPhone = Entry;
    }

    @Override
    public String getDriverActivityContractorPhone() {
        return contractorPhone;
    }

    @Override
    public void setDriverActivityDestination(String Entry) {
        destination = Entry;
    }

    @Override
    public String getDriverActivityDestination() {
        return destination;
    }

    @Override
    public void setDriverActivityTypeOfLoad(String Entry) {
        TypeOfLoad = Entry;
    }

    @Override
    public String getDriverActivityTypeOfLoad() {
        return TypeOfLoad;
    }

    @Override
    public void setDriverActivityShippingOffer(String Entry) {
        ShippingOffer = Entry;
    }

    @Override
    public String getDriverActivityShippingOffer() {
        return ShippingOffer;
    }

    @Override
    public void setDriverActivityStatus(int Entry) {
        activityStatus = Entry;
    }

    @Override
    public int getDriverActivityStatus() {
        return activityStatus;
    }

    @Override
    public void setDriverActivityDate(String Entry) {
        activityDate = Entry;
    }

    @Override
    public String getDriverActivityDate() {
        return activityDate;
    }

    @Override
    public void setAccountId(int Entry) {
        accountId = Entry;
    }

    @Override
    public int getAccountId() {
        return accountId;
    }

}
