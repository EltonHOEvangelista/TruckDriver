package com.example.truckdriver_v02.data.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.truckdriver_v02.data.driverActivity.DriverActivity;
import com.example.truckdriver_v02.data.vehicle.TrailerModel;

import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "TruckDriver_db";

    //variables for Account Table
    private static final String TABLE_Account = "ACCOUNT";
    private static final String COLUMN_ACCOUNT_ID = "AccountId";
    private static final String COLUMN_FIRST_NAME = "FirstName";
    private static final String COLUMN_LAST_NAME = "LastName";
    private static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
    private static final String COLUMN_EMAIL = "Email";
    private static final String COLUMN_PASSWORD = "Password";
    private static final String COLUMN_ENABLE = "Enable";
    private static final String COLUMN_DRIVER_STATUS = "DriverStatus";

    //variables for Session Table
    private static final String TABLE_Session = "Session";
    private static final String COLUMN_TIME_BEGIN = "TimeBegin";
    private static final String COLUMN_TIME_END = "TimeEnd";
    private static final String COLUMN_IS_ON = "IsON";

    //variables for Vehicle Table
    private static final String TABLE_Vehicle = "Vehicle";
    private static final String COLUMN_VEHICLE_ID = "VehicleId";
    private static final String COLUMN_PLATE = "Plate";
    private static final String COLUMN_VIN = "Vin";
    private static final String COLUMN_MANUFACTURER = "Manufacturer";
    private static final String COLUMN_MODEL = "Model";
    private static final String COLUMN_COLOR = "Color";
    private static final String COLUMN_CAPACITY = "Capacity";
    private static final String COLUMN_TRAILER = "Trailer";

    //variables for Trailer Type Table
    private static final String TABLE_Trailer_Type = "TrailerType";
    private static final String COLUMN_TRAILER_TYPE_ID = "TrailerTypeId";
    private static final String COLUMN_TRAILER_NAME = "TrailerTypeName";

    //variables for Driver Activities Table
    private static final String TABLE_Driver_Activity = "ACTIVITY";
    private static final String COLUMN_ACTIVITY_ID = "activityId";
    private static final String COLUMN_CONTRACTOR = "contractor";
    private static final String COLUMN_CONTRACTOR_EMPLOYEE = "contractorEmployee";
    private static final String COLUMN_CONTRACTOR_PHONE = "contractorPhone";
    private static final String COLUMN_DESTINATION = "destination";
    private static final String COLUMN_LOAD_TYPE = "TypeOfLoad";
    private static final String COLUMN_SHIPPING_OFFER = "ShippingOffer";
    private static final String COLUMN_ACTIVITY_STATUS = "activityStatus";
    private static final String COLUMN_ACTIVITY_DATE = "activityDate";

    public DbHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Account Table
        String CREATE_TABLE_ACCOUNT = "CREATE TABLE " + TABLE_Account + "("
                + COLUMN_ACCOUNT_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_PHONE_NUMBER + " INTEGER,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_ENABLE + " INTEGER,"
                + COLUMN_DRIVER_STATUS + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_ACCOUNT);

        //Create Session Table
        String CREATE_TABLE_SESSION = "CREATE TABLE " + TABLE_Session + "("
                + COLUMN_ACCOUNT_ID + " INTEGER,"
                + COLUMN_TIME_BEGIN + " TEXT,"
                + COLUMN_TIME_END + " TEXT,"
                + COLUMN_IS_ON + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_SESSION);

        //Create Vehicle Table
        String CREATE_TABLE_VEHICLE = "CREATE TABLE " + TABLE_Vehicle + "("
                + COLUMN_VEHICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PLATE + " TEXT,"
                + COLUMN_VIN + " TEXT,"
                + COLUMN_MANUFACTURER + " TEXT,"
                + COLUMN_MODEL + " TEXT,"
                + COLUMN_COLOR + " TEXT,"
                + COLUMN_CAPACITY + " TEXT,"
                + COLUMN_TRAILER + " TEXT,"
                + COLUMN_ACCOUNT_ID + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_VEHICLE);

        String CREATE_TABLE_TRAILER_TYPE = "CREATE TABLE " + TABLE_Trailer_Type + "("
                + COLUMN_TRAILER_TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TRAILER_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_TRAILER_TYPE);

        //Create Activity Table
        String CREATE_TABLE_ACTIVITY = "CREATE TABLE " + TABLE_Driver_Activity + "("
                + COLUMN_ACTIVITY_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_CONTRACTOR + " TEXT,"
                + COLUMN_CONTRACTOR_EMPLOYEE + " TEXT,"
                + COLUMN_CONTRACTOR_PHONE + " TEXT,"
                + COLUMN_DESTINATION + " TEXT,"
                + COLUMN_LOAD_TYPE + " TEXT,"
                + COLUMN_SHIPPING_OFFER + " TEXT,"
                + COLUMN_ACTIVITY_STATUS + " INTEGER,"
                + COLUMN_ACTIVITY_DATE + " TEXT,"
                + COLUMN_ACCOUNT_ID + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_ACTIVITY);

        //call method to populate table including default values for trailer types
        populateDefaultData(db);

        //db.close();
    }

    // Check if the database file exists in the app's data directory
    public boolean DatabaseExist(Context context) {
        String dbPath = context.getDatabasePath("TruckDriver_db").getAbsolutePath();
        return SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY) != null;
    }

    //upgrade database if database version (variable) is updated
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Account);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Session);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Vehicle);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Trailer_Type);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Driver_Activity);

        // Create tables again
        onCreate(db);

    }

    //populate table including default values for trailer types
    private void populateDefaultData(SQLiteDatabase db) {

        String INSERT_TABLE_Trailer_Type = "INSERT INTO " + TABLE_Trailer_Type +
                " (" + COLUMN_TRAILER_NAME + ") VALUES ('Car Carrier');";
        db.execSQL(INSERT_TABLE_Trailer_Type);

        INSERT_TABLE_Trailer_Type = "INSERT INTO " + TABLE_Trailer_Type +
                " (" + COLUMN_TRAILER_NAME + ") VALUES ('Drop Deck');";
        db.execSQL(INSERT_TABLE_Trailer_Type);

        INSERT_TABLE_Trailer_Type = "INSERT INTO " + TABLE_Trailer_Type +
                " (" + COLUMN_TRAILER_NAME + ") VALUES ('Dry Van');";
        db.execSQL(INSERT_TABLE_Trailer_Type);

        INSERT_TABLE_Trailer_Type = "INSERT INTO " + TABLE_Trailer_Type +
                " (" + COLUMN_TRAILER_NAME + ") VALUES ('Intermodal Container');";
        db.execSQL(INSERT_TABLE_Trailer_Type);

        INSERT_TABLE_Trailer_Type = "INSERT INTO " + TABLE_Trailer_Type +
                " (" + COLUMN_TRAILER_NAME + ") VALUES ('Livestock');";
        db.execSQL(INSERT_TABLE_Trailer_Type);

        INSERT_TABLE_Trailer_Type = "INSERT INTO " + TABLE_Trailer_Type +
                " (" + COLUMN_TRAILER_NAME + ") VALUES ('Refrigerated');";
        db.execSQL(INSERT_TABLE_Trailer_Type);

        INSERT_TABLE_Trailer_Type = "INSERT INTO " + TABLE_Trailer_Type +
                " (" + COLUMN_TRAILER_NAME + ") VALUES ('Tanker');";
        db.execSQL(INSERT_TABLE_Trailer_Type);


        //Tablet Driver Activity (Examples to demonstrate)
        String INSERT_TABLE_Driver_Activity = "INSERT INTO " + TABLE_Driver_Activity +
                " (" + COLUMN_CONTRACTOR +
                ", " + COLUMN_CONTRACTOR_EMPLOYEE +
                ", " + COLUMN_CONTRACTOR_PHONE +
                ", " + COLUMN_DESTINATION +
                ", " + COLUMN_LOAD_TYPE +
                ", " + COLUMN_SHIPPING_OFFER +
                ", " + COLUMN_ACTIVITY_STATUS +
                ", " + COLUMN_ACTIVITY_DATE +
                ", " + COLUMN_ACCOUNT_ID + ") VALUES ('Maple Leaf Foods Inc', " +
                             "'John', " +
                             "'+1 778-789-0000', " +
                             "'Toronto', " +
                             "'Refrigerated', " +
                             "'$ 5,000.00', " +
                             "3, " +
                             "'11/25/2023', " +
                             "23112794" + ");";

        db.execSQL(INSERT_TABLE_Driver_Activity);

        INSERT_TABLE_Driver_Activity = "INSERT INTO " + TABLE_Driver_Activity +
                " (" + COLUMN_CONTRACTOR +
                ", " + COLUMN_CONTRACTOR_EMPLOYEE +
                ", " + COLUMN_CONTRACTOR_PHONE +
                ", " + COLUMN_DESTINATION +
                ", " + COLUMN_LOAD_TYPE +
                ", " + COLUMN_SHIPPING_OFFER +
                ", " + COLUMN_ACTIVITY_STATUS +
                ", " + COLUMN_ACTIVITY_DATE +
                ", " + COLUMN_ACCOUNT_ID + ") VALUES ('Canadian National Railway', " +
                "'Kevin', " +
                "'+1 234-289-2300', " +
                "'Montreal', " +
                "'Intermodal Container', " +
                "'$ 4,000.00', " +
                "3, " +
                "'11/30/2023', " +
                "23112794" + ");";

        db.execSQL(INSERT_TABLE_Driver_Activity);

    }

    // Adding new Account. Return -1 if fail or accountID if success
    public boolean createAccount(int accountId, String firstName, String lastName, long phoneNumber, String email, String password, int enable, int driverStatus){

        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_ACCOUNT_ID, accountId);
        cValues.put(COLUMN_FIRST_NAME, firstName);
        cValues.put(COLUMN_LAST_NAME, lastName);
        cValues.put(COLUMN_PHONE_NUMBER, phoneNumber);
        cValues.put(COLUMN_EMAIL, email);
        cValues.put(COLUMN_PASSWORD, password);
        cValues.put(COLUMN_ENABLE, enable);
        cValues.put(COLUMN_DRIVER_STATUS, driverStatus);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_Account,null, cValues);

        //close database
        db.close();

        if(newRowId == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public String[] getAccountCredential(String email){

        String[] credential = new String[5];
        String password;
        int enable;
        String firstName;
        int accountNumber;

        String queryString = "SELECT " + COLUMN_PASSWORD + ", " +
                                         COLUMN_ENABLE + ", " +
                                         COLUMN_FIRST_NAME + ", " +
                                         COLUMN_ACCOUNT_ID +
                            " FROM " + TABLE_Account +
                            " WHERE " + COLUMN_EMAIL + " = '" + email + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            password = cursor.getString(0);
            enable = cursor.getInt(1);
            firstName = cursor.getString(2);
            accountNumber = cursor.getInt(3);
        }
        else{
            email = null;
            password = null;
            enable = 0;
            firstName = null;
            accountNumber = 0;
        }

        credential[0] = email;
        credential[1] = password;
        credential[2] = String.valueOf(enable);
        credential[3] = firstName;
        credential[4] = String.valueOf(accountNumber);;

        cursor.close();
        db.close();

        return credential;
    }

    // starting up new session in app.
    public boolean StartSession(int accountId, String time_begin, String time_end, int signedIn){

        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_ACCOUNT_ID, accountId);
        cValues.put(COLUMN_TIME_BEGIN, time_begin);
        cValues.put(COLUMN_TIME_END, time_end);
        cValues.put(COLUMN_IS_ON, signedIn);


        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_Session,null, cValues);

        //close database
        db.close();

        if(newRowId == -1){
            return false;
        }
        else{
            return true;
        }
    }

    // finishing all account sessions on database
    public boolean endAllSession(){

        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Update operation
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_IS_ON, 0);

        String queryString = "UPDATE " + TABLE_Session +
                                " SET " + COLUMN_IS_ON + " = 0" +
                                " WHERE " + COLUMN_IS_ON + " = 1;";

        db.execSQL(queryString);

        db.close();

        return true;
    }

    public int getOpenedSession(){

        int accountNumber;

        String queryString = "SELECT " + COLUMN_ACCOUNT_ID +
                " FROM " + TABLE_Session +
                " WHERE " + COLUMN_IS_ON + " = 1;";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            accountNumber = cursor.getInt(0);
        }
        else{
            accountNumber = 0;
        }

        cursor.close();
        db.close();

        return accountNumber;
    }

    //retrieving account details.
    public String[] getAccountDetails(int accountNumber){

        String[] accountData = new String[8];

        String firstName;
        String lastName;
        long phone;
        String email;
        String password;
        int isEnable;
        int driverStatus;

        String queryString = "SELECT * FROM " +
                TABLE_Account +
                " WHERE " + COLUMN_ACCOUNT_ID + " = '" + accountNumber + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){

            accountNumber = cursor.getInt(0);
            firstName = cursor.getString(1);
            lastName = cursor.getString(2);
            phone = cursor.getLong(3);
            email = cursor.getString(4);
            password = cursor.getString(5);
            isEnable = cursor.getInt(6);
            driverStatus = cursor.getInt(7);
        }
        else{

            firstName = null;
            lastName = null;
            phone = 0;
            email = null;
            password = null;
            isEnable = 0;
            driverStatus = 0;
        }

        accountData[0] = String.valueOf(accountNumber);
        accountData[1] = firstName;
        accountData[2] = lastName;
        accountData[3] = String.valueOf(phone);
        accountData[4] = email;
        accountData[5] = password;
        accountData[6] = String.valueOf(isEnable);
        accountData[7] = String.valueOf(driverStatus);

        cursor.close();
        db.close();

        return accountData;
    }

    //Method to update Account Phone, Password and E-mail.
    public boolean updateAccount(int accountId, long phone, String password, String email){

        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();

        cValues.put(COLUMN_PHONE_NUMBER, phone);
        cValues.put(COLUMN_PASSWORD, password);
        cValues.put(COLUMN_EMAIL, email);

        //update record on database
        // The WHERE clause specifies which record(s) to update.
        String selectAccount = COLUMN_ACCOUNT_ID + " = ?";
        String[] selectionArgs = { String.valueOf(accountId) };

        // Perform the update.
        int count = db.update(
                TABLE_Account,
                cValues,
                selectAccount,
                selectionArgs);

        //close database
        db.close();

        if(count >0){
            return true;
        }
        else{
            return false;
        }
    }

    //method to handle add or update vehicle requests. If null, create vehicle. Otherwise, updated vehicle.
    public boolean AddOrUpdateVehicle(String plate, String vin, String manufacturer, String vModel, String vColor, String capacity, String trailer, int accountId){

        boolean addOrUpdateStatus = false;

        String queryString = "SELECT " + COLUMN_ACCOUNT_ID +
                " FROM " + TABLE_Vehicle +
                " WHERE " + COLUMN_ACCOUNT_ID + " = " + accountId + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){   //vehicle already created.

            //call method to update vehicle on database, given account number as reference
            addOrUpdateStatus = updateVehicle(plate, vin, manufacturer, vModel, vColor, capacity, trailer, accountId);

        }
        else{       //create a new vehicle on database

            addOrUpdateStatus = addVehicle(plate, vin, manufacturer, vModel, vColor, capacity, trailer, accountId);
        }

        cursor.close();
        db.close();

        return addOrUpdateStatus;
    }

    // Create a new vehicle on database
    public boolean addVehicle(String plate, String vin, String manufacturer, String vModel, String vColor, String capacity, String trailer, int accountId){

        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_PLATE, plate);
        cValues.put(COLUMN_VIN, vin);
        cValues.put(COLUMN_MANUFACTURER, manufacturer);
        cValues.put(COLUMN_MODEL, vModel);
        cValues.put(COLUMN_COLOR, vColor);
        cValues.put(COLUMN_CAPACITY, capacity);
        cValues.put(COLUMN_TRAILER, trailer);
        cValues.put(COLUMN_ACCOUNT_ID, accountId);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_Vehicle,null, cValues);

        //close database
        db.close();

        if(newRowId == -1){
            return false;
        }
        else{
            return true;
        }
    }

    //method to update vehicle details using accountId as Key to find vehicle.
    public boolean updateVehicle(String plate, String vin, String manufacturer, String vModel, String vColor, String capacity, String trailer, int accountId){

        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();

        cValues.put(COLUMN_PLATE, plate);
        cValues.put(COLUMN_VIN, vin);
        cValues.put(COLUMN_MANUFACTURER, manufacturer);
        cValues.put(COLUMN_MODEL, vModel);
        cValues.put(COLUMN_COLOR, vColor);
        cValues.put(COLUMN_CAPACITY, capacity);
        cValues.put(COLUMN_TRAILER, trailer);
        cValues.put(COLUMN_ACCOUNT_ID, accountId);

        //update record on database
        // The WHERE clause specifies which record(s) to update.
        String selectAccount = COLUMN_ACCOUNT_ID + " = ?";
        String[] selectionArgs = { String.valueOf(accountId) };

        // Perform the update.
        int count = db.update(
                TABLE_Vehicle,
                cValues,
                selectAccount,
                selectionArgs);

        //close database
        db.close();

        if(count >0){
            return true;
        }
        else{
            return false;
        }
    }

    //get vehicle details
    public String[] getVehicleDetails(int accountNumber){

        String[] vehicleData = new String[9];

        int VehicleId;
        String Plate;
        String Vin;
        String Manufacturer;
        String Model;
        String Color;
        String Capacity;
        String Trailer;

        String queryString = "SELECT * FROM " +
                TABLE_Vehicle +
                " WHERE " + COLUMN_ACCOUNT_ID + " = '" + accountNumber + "';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){

            VehicleId = cursor.getInt(0);
            Plate = cursor.getString(1);
            Vin = cursor.getString(2);
            Manufacturer = cursor.getString(3);
            Model = cursor.getString(4);
            Color = cursor.getString(5);
            Capacity = cursor.getString(6);
            Trailer = cursor.getString(7);
            accountNumber = cursor.getInt(8);
        }
        else{

            VehicleId = 0;
            Plate = null;
            Vin = null;
            Manufacturer = null;
            Model = null;
            Color = null;
            Capacity = null;
            Trailer = null;
        }

        vehicleData[0] = String.valueOf(VehicleId);
        vehicleData[1] = Plate;
        vehicleData[2] = Vin;
        vehicleData[3] = Manufacturer;
        vehicleData[4] = Model;
        vehicleData[5] = Color;
        vehicleData[6] = Capacity;
        vehicleData[7] = Trailer;
        vehicleData[8] = String.valueOf(accountNumber);

        cursor.close();
        db.close();

        return vehicleData;
    }

    //get trailer types (id and name)
    public ArrayList<TrailerModel> getTrailerTypes(){

        ArrayList<TrailerModel> list = new ArrayList<>();

        int TrailerTypeId;
        String TrailerTypeName;

        String queryString = "SELECT * FROM " +
                TABLE_Trailer_Type + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){

            do{

                TrailerTypeId = cursor.getInt(0);
                TrailerTypeName = cursor.getString(1);

                list.add(new TrailerModel(TrailerTypeId, TrailerTypeName));
            }
            while (cursor.moveToNext());
        }
        else{

            TrailerTypeId = 0;
            list.add(new TrailerModel(TrailerTypeId, null));
        }

        cursor.close();
        db.close();

        return list;
    }

    //get trailer names
    public ArrayList<String> getTrailerNames(){

        ArrayList<String> list = new ArrayList<>();

        String TrailerTypeName;

        String queryString = "SELECT " + COLUMN_TRAILER_NAME + " FROM " +
                TABLE_Trailer_Type + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){

            do{

                TrailerTypeName = cursor.getString(0);

                list.add(TrailerTypeName);
            }
            while (cursor.moveToNext());
        }
        else{

            list.add( null);
        }

        cursor.close();
        db.close();

        return list;
    }

    //Method to update Driver Availability.
    public boolean updateDriverAvailability(int accountId, int driverStatus){

        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();

        cValues.put(COLUMN_DRIVER_STATUS, driverStatus);

        //update record on database
        // The WHERE clause specifies which record(s) to update.
        String selectAccount = COLUMN_ACCOUNT_ID + " = ?";
        String[] selectionArgs = { String.valueOf(accountId) };

        // Perform the update.
        int count = db.update(
                TABLE_Account,
                cValues,
                selectAccount,
                selectionArgs);

        //close database
        db.close();

        if(count >0){
            return true;
        }
        else{
            return false;
        }
    }

    //retrieving getDriverActivity
    public ArrayList<DriverActivity> getDriverActivity(int accountNumber){

        ArrayList<DriverActivity> driverActivity = new ArrayList<>();

        String queryString = "SELECT * FROM " +
                TABLE_Driver_Activity +
                " WHERE " + COLUMN_ACCOUNT_ID + " = " + accountNumber + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){

            do{
                DriverActivity data = new DriverActivity();

                data.setDriverActivityId(cursor.getInt(0));
                data.setDriverActivityContractor(cursor.getString(1));
                data.setDriverActivityContractorEmployee(cursor.getString(2));
                data.setDriverActivityContractorPhone(cursor.getString(3));
                data.setDriverActivityDestination(cursor.getString(4));
                data.setDriverActivityTypeOfLoad(cursor.getString(5));
                data.setDriverActivityShippingOffer(cursor.getString(6));
                data.setDriverActivityStatus(cursor.getInt(7));
                data.setDriverActivityDate(cursor.getString(8));
                data.setAccountId(cursor.getInt(9));

                driverActivity.add(data);
            }
            while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        return driverActivity;
    }

    // Create addDriverActivity
    public boolean addDriverActivity(String contractor, String contractorEmployee, String contractorPhone, String destination, String typeOfLoad,
                                     String shippingOffer, int status, String date, int accountId){

        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_CONTRACTOR, contractor);
        cValues.put(COLUMN_CONTRACTOR_EMPLOYEE, contractorEmployee);
        cValues.put(COLUMN_CONTRACTOR_PHONE, contractorPhone);
        cValues.put(COLUMN_DESTINATION, destination);
        cValues.put(COLUMN_LOAD_TYPE, typeOfLoad);
        cValues.put(COLUMN_SHIPPING_OFFER, shippingOffer);
        cValues.put(COLUMN_ACTIVITY_STATUS, status);
        cValues.put(COLUMN_ACTIVITY_DATE, date);
        cValues.put(COLUMN_ACCOUNT_ID, accountId);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_Driver_Activity,null, cValues);

        //close database
        db.close();

        if(newRowId == -1){
            return false;
        }
        else{
            return true;
        }
    }


    //method to update updateDriverActivity.
    public boolean updateDriverActivity(int activityId, String contractor, String contractorEmployee, String contractorPhone, String destination, String typeOfLoad,
                                        String shippingOffer, int status, String date, int accountId){

        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();

        cValues.put(COLUMN_CONTRACTOR, contractor);
        cValues.put(COLUMN_CONTRACTOR_EMPLOYEE, contractorEmployee);
        cValues.put(COLUMN_CONTRACTOR_PHONE, contractorPhone);
        cValues.put(COLUMN_DESTINATION, destination);
        cValues.put(COLUMN_LOAD_TYPE, typeOfLoad);
        cValues.put(COLUMN_SHIPPING_OFFER, shippingOffer);
        cValues.put(COLUMN_ACTIVITY_STATUS, status);
        cValues.put(COLUMN_ACTIVITY_DATE, date);
        cValues.put(COLUMN_ACCOUNT_ID, accountId);

        //update record on database
        // The WHERE clause specifies which record(s) to update.
        String selectAccount = COLUMN_ACTIVITY_ID + " = ?";
        String[] selectionArgs = { String.valueOf(activityId) };

        // Perform the update.
        int count = db.update(
                TABLE_Driver_Activity,
                cValues,
                selectAccount,
                selectionArgs);

        //close database
        db.close();

        if(count >0){
            return true;
        }
        else{
            return false;
        }
    }

    // finishing all account sessions on database
    public void UpdateAccountIdOnActivityDriver(int accountId) {

        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Update operation
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_ACCOUNT_ID, 9);

        String queryString = "UPDATE " + TABLE_Driver_Activity +
                " SET " + COLUMN_ACCOUNT_ID + " = " + accountId +
                " WHERE " + COLUMN_ACCOUNT_ID + " = " + accountId + ";";

        db.execSQL(queryString);

        db.close();
    }

}
