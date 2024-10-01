package com.example.truckdriver_v02.data.account;

public interface Account_Interface {

    public void setAccountId(int Entry);
    public int getAccountId();
    public void setFirstName(String Entry);
    public String getFirstName();
    public void setLastName(String Entry);
    public String getLastName();
    public void setPhone(long Entry);
    public long getPhone();
    public void setEmail(String Entry);
    public String getEmail();
    public void setPassword(String Entry);
    public String getPassword();
    public int getActiveAccount();
    public void setActiveAccount(int Entry);
    public int getDriverStatus();
    public void setDriverStatus(int Entry);

}
