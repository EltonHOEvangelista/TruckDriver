package com.example.truckdriver_v02.data.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.truckdriver_v02.data.account.Driver
import com.example.truckdriver_v02.data.driverActivity.DriverActivity
import com.example.truckdriver_v02.data.vehicle.TrailerModel
import com.example.truckdriver_v02.data.vehicle.Truck

class DbHandler(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        //Create Account Table

        val CREATE_TABLE_ACCOUNT = ("CREATE TABLE " + TABLE_Account + "("
                + COLUMN_ACCOUNT_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_PHONE_NUMBER + " INTEGER,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_ENABLE + " INTEGER,"
                + COLUMN_DRIVER_STATUS + " INTEGER"
                + ")")
        db.execSQL(CREATE_TABLE_ACCOUNT)

        //Create Session Table
        val CREATE_TABLE_SESSION = ("CREATE TABLE " + TABLE_Session + "("
                + COLUMN_ACCOUNT_ID + " INTEGER,"
                + COLUMN_TIME_BEGIN + " TEXT,"
                + COLUMN_TIME_END + " TEXT,"
                + COLUMN_IS_ON + " INTEGER"
                + ")")
        db.execSQL(CREATE_TABLE_SESSION)

        //Create Vehicle Table
        val CREATE_TABLE_VEHICLE = ("CREATE TABLE " + TABLE_Vehicle + "("
                + COLUMN_VEHICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PLATE + " TEXT,"
                + COLUMN_VIN + " TEXT,"
                + COLUMN_MANUFACTURER + " TEXT,"
                + COLUMN_MODEL + " TEXT,"
                + COLUMN_COLOR + " TEXT,"
                + COLUMN_CAPACITY + " TEXT,"
                + COLUMN_TRAILER + " TEXT,"
                + COLUMN_ACCOUNT_ID + " INTEGER"
                + ")")
        db.execSQL(CREATE_TABLE_VEHICLE)

        val CREATE_TABLE_TRAILER_TYPE = ("CREATE TABLE " + TABLE_Trailer_Type + "("
                + COLUMN_TRAILER_TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TRAILER_NAME + " TEXT"
                + ")")
        db.execSQL(CREATE_TABLE_TRAILER_TYPE)

        //Create Activity Table
        val CREATE_TABLE_ACTIVITY = ("CREATE TABLE " + TABLE_Driver_Activity + "("
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
                + ")")
        db.execSQL(CREATE_TABLE_ACTIVITY)

        //call method to populate table including default values for trailer types
        populateDefaultData(db)

        //db.close();
    }

    // Check if the database file exists in the app's data directory
    fun DatabaseExist(context: Context): Boolean {
        val dbPath = context.getDatabasePath("TruckDriver_db").absolutePath
        return SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY) != null
    }

    //upgrade database if database version (variable) is updated
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if exist

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Account)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Session)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Vehicle)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Trailer_Type)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Driver_Activity)

        // Create tables again
        onCreate(db)
    }

    //populate table including default values for trailer types
    private fun populateDefaultData(db: SQLiteDatabase) {
        var INSERT_TABLE_Trailer_Type = "INSERT INTO " + TABLE_Trailer_Type +
                " (" + COLUMN_TRAILER_NAME + ") VALUES ('Car Carrier');"
        db.execSQL(INSERT_TABLE_Trailer_Type)

        INSERT_TABLE_Trailer_Type = "INSERT INTO " + TABLE_Trailer_Type +
                " (" + COLUMN_TRAILER_NAME + ") VALUES ('Drop Deck');"
        db.execSQL(INSERT_TABLE_Trailer_Type)

        INSERT_TABLE_Trailer_Type = "INSERT INTO " + TABLE_Trailer_Type +
                " (" + COLUMN_TRAILER_NAME + ") VALUES ('Dry Van');"
        db.execSQL(INSERT_TABLE_Trailer_Type)

        INSERT_TABLE_Trailer_Type = "INSERT INTO " + TABLE_Trailer_Type +
                " (" + COLUMN_TRAILER_NAME + ") VALUES ('Intermodal Container');"
        db.execSQL(INSERT_TABLE_Trailer_Type)

        INSERT_TABLE_Trailer_Type = "INSERT INTO " + TABLE_Trailer_Type +
                " (" + COLUMN_TRAILER_NAME + ") VALUES ('Livestock');"
        db.execSQL(INSERT_TABLE_Trailer_Type)

        INSERT_TABLE_Trailer_Type = "INSERT INTO " + TABLE_Trailer_Type +
                " (" + COLUMN_TRAILER_NAME + ") VALUES ('Refrigerated');"
        db.execSQL(INSERT_TABLE_Trailer_Type)

        INSERT_TABLE_Trailer_Type = "INSERT INTO " + TABLE_Trailer_Type +
                " (" + COLUMN_TRAILER_NAME + ") VALUES ('Tanker');"
        db.execSQL(INSERT_TABLE_Trailer_Type)


        //Tablet Driver Activity (Examples to demonstrate)
        var INSERT_TABLE_Driver_Activity = "INSERT INTO " + TABLE_Driver_Activity +
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
                "23112794" + ");"

        db.execSQL(INSERT_TABLE_Driver_Activity)

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
                "23112794" + ");"

        db.execSQL(INSERT_TABLE_Driver_Activity)
    }

    // Adding new Account. Return -1 if fail or accountID if success
    fun createAccount(
        accountId: Int?,
        firstName: String?,
        lastName: String?,
        phoneNumber: Long?,
        email: String?,
        password: String?,
        enable: Int?,
        driverStatus: Int?
    ): Boolean {
        //Get the Data Repository in write mode

        val db = this.writableDatabase

        //Create a new map of values, where column names are the keys
        val cValues = ContentValues()
        cValues.put(COLUMN_ACCOUNT_ID, accountId)
        cValues.put(COLUMN_FIRST_NAME, firstName)
        cValues.put(COLUMN_LAST_NAME, lastName)
        cValues.put(COLUMN_PHONE_NUMBER, phoneNumber)
        cValues.put(COLUMN_EMAIL, email)
        cValues.put(COLUMN_PASSWORD, password)
        cValues.put(COLUMN_ENABLE, enable)
        cValues.put(COLUMN_DRIVER_STATUS, driverStatus)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(TABLE_Account, null, cValues)

        //close database
        db.close()

        return if (newRowId == -1L) {
            false
        } else {
            true
        }
    }

    fun getAccountCredential(email: String?): Driver {

        var driver: Driver = Driver(null.toString(), null.toString())

        val queryString = "SELECT " + COLUMN_PASSWORD + ", " +
                COLUMN_ENABLE + ", " +
                COLUMN_FIRST_NAME + ", " +
                COLUMN_ACCOUNT_ID + ", " +
                COLUMN_EMAIL +
                " FROM " + TABLE_Account +
                " WHERE " + COLUMN_EMAIL + " = '" + email + "';"

        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            driver.passWord = cursor.getString(0)
            driver.isActive = cursor.getInt(1)
            driver.firstName = cursor.getString(2)
            driver.accountId = cursor.getInt(3)
            driver.eMail = cursor.getString(4)
        }

        cursor.close()
        db.close()

        return driver
    }

    // starting up new session in app.
    fun StartSession(
        accountId: Int?,
        time_begin: String?,
        time_end: String?,
        signedIn: Int?
    ): Boolean {
        //Get the Data Repository in write mode

        val db = this.writableDatabase

        //Create a new map of values, where column names are the keys
        val cValues = ContentValues()
        cValues.put(COLUMN_ACCOUNT_ID, accountId)
        cValues.put(COLUMN_TIME_BEGIN, time_begin)
        cValues.put(COLUMN_TIME_END, time_end)
        cValues.put(COLUMN_IS_ON, signedIn)


        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(TABLE_Session, null, cValues)

        //close database
        db.close()

        return if (newRowId == -1L) {
            false
        } else {
            true
        }
    }

    // finishing all account sessions on database
    fun endAllSession(): Boolean {
        //Get the Data Repository in write mode

        val db = this.writableDatabase

        // Update operation
        val cValues = ContentValues()
        cValues.put(COLUMN_IS_ON, 0)

        val queryString = "UPDATE " + TABLE_Session +
                " SET " + COLUMN_IS_ON + " = 0" +
                " WHERE " + COLUMN_IS_ON + " = 1;"

        db.execSQL(queryString)

        db.close()

        return true
    }

    val openedSession: Int
        get() {
            val accountNumber: Int

            val queryString = "SELECT " + COLUMN_ACCOUNT_ID +
                    " FROM " + TABLE_Session +
                    " WHERE " + COLUMN_IS_ON + " = 1;"

            val db = this.readableDatabase
            val cursor = db.rawQuery(queryString, null)

            accountNumber = if (cursor.moveToFirst()) {
                cursor.getInt(0)
            } else {
                0
            }

            cursor.close()
            db.close()

            return accountNumber
        }

    //retrieving account details.
    fun getAccountDetails(accountNumber: Int): Driver {

        val driver: Driver = Driver(null.toString(), null.toString())

        var accountNumber = accountNumber

        val queryString = "SELECT * FROM " +
                TABLE_Account +
                " WHERE " + COLUMN_ACCOUNT_ID + " = '" + accountNumber + "';"

        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            driver.accountId = cursor.getInt(0)
            driver.firstName = cursor.getString(1)
            driver.lastName = cursor.getString(2)
            driver.phoneNumber =cursor.getLong(3)
            driver.eMail = cursor.getString(4)
            driver.passWord = cursor.getString(5)
            driver.isActive = cursor.getInt(6)
            driver.driverStatus = cursor.getInt(7)
        }

        cursor.close()
        db.close()

        return driver
    }

    //Method to update Account Phone, Password and E-mail.
    fun updateAccount(accountId: Int?, phone: Long?, password: String?, email: String?): Boolean {
        //Get the Data Repository in write mode

        val db = this.writableDatabase

        //Create a new map of values, where column names are the keys
        val cValues = ContentValues()

        cValues.put(COLUMN_PHONE_NUMBER, phone)
        cValues.put(COLUMN_PASSWORD, password)
        cValues.put(COLUMN_EMAIL, email)

        //update record on database
        // The WHERE clause specifies which record(s) to update.
        val selectAccount = COLUMN_ACCOUNT_ID + " = ?"
        val selectionArgs = arrayOf(accountId.toString())

        // Perform the update.
        val count = db.update(
            TABLE_Account,
            cValues,
            selectAccount,
            selectionArgs
        )

        //close database
        db.close()

        return if (count > 0) {
            true
        } else {
            false
        }
    }

    //method to handle add or update vehicle requests. If null, create vehicle. Otherwise, updated vehicle.
    fun AddOrUpdateVehicle(
        plate: String?,
        vin: String?,
        manufacturer: String?,
        vModel: String?,
        vColor: String?,
        capacity: String?,
        trailer: String?,
        accountId: Int?
    ): Boolean {
        var addOrUpdateStatus = false

        val queryString = "SELECT " + COLUMN_ACCOUNT_ID +
                " FROM " + TABLE_Vehicle +
                " WHERE " + COLUMN_ACCOUNT_ID + " = " + accountId + ";"

        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)

        addOrUpdateStatus = if (cursor.moveToFirst()) {   //vehicle already created.

            //call method to update vehicle on database, given account number as reference

            updateVehicle(plate, vin, manufacturer, vModel, vColor, capacity, trailer, accountId)
        } else {       //create a new vehicle on database

            addVehicle(plate, vin, manufacturer, vModel, vColor, capacity, trailer, accountId)
        }

        cursor.close()
        db.close()

        return addOrUpdateStatus
    }

    // Create a new vehicle on database
    fun addVehicle(
        plate: String?,
        vin: String?,
        manufacturer: String?,
        vModel: String?,
        vColor: String?,
        capacity: String?,
        trailer: String?,
        accountId: Int?
    ): Boolean {
        //Get the Data Repository in write mode

        val db = this.writableDatabase

        //Create a new map of values, where column names are the keys
        val cValues = ContentValues()
        cValues.put(COLUMN_PLATE, plate)
        cValues.put(COLUMN_VIN, vin)
        cValues.put(COLUMN_MANUFACTURER, manufacturer)
        cValues.put(COLUMN_MODEL, vModel)
        cValues.put(COLUMN_COLOR, vColor)
        cValues.put(COLUMN_CAPACITY, capacity)
        cValues.put(COLUMN_TRAILER, trailer)
        cValues.put(COLUMN_ACCOUNT_ID, accountId)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(TABLE_Vehicle, null, cValues)

        //close database
        db.close()

        return if (newRowId == -1L) {
            false
        } else {
            true
        }
    }

    //method to update vehicle details using accountId as Key to find vehicle.
    fun updateVehicle(
        plate: String?,
        vin: String?,
        manufacturer: String?,
        vModel: String?,
        vColor: String?,
        capacity: String?,
        trailer: String?,
        accountId: Int?
    ): Boolean {
        //Get the Data Repository in write mode

        val db = this.writableDatabase

        //Create a new map of values, where column names are the keys
        val cValues = ContentValues()

        cValues.put(COLUMN_PLATE, plate)
        cValues.put(COLUMN_VIN, vin)
        cValues.put(COLUMN_MANUFACTURER, manufacturer)
        cValues.put(COLUMN_MODEL, vModel)
        cValues.put(COLUMN_COLOR, vColor)
        cValues.put(COLUMN_CAPACITY, capacity)
        cValues.put(COLUMN_TRAILER, trailer)
        cValues.put(COLUMN_ACCOUNT_ID, accountId)

        //update record on database
        // The WHERE clause specifies which record(s) to update.
        val selectAccount = COLUMN_ACCOUNT_ID + " = ?"
        val selectionArgs = arrayOf(accountId.toString())

        // Perform the update.
        val count = db.update(
            TABLE_Vehicle,
            cValues,
            selectAccount,
            selectionArgs
        )

        //close database
        db.close()

        return if (count > 0) {
            true
        } else {
            false
        }
    }

    //get vehicle details
    fun getVehicleDetails(accountNumber: Int?): Truck {

        val truck: Truck = Truck()

        val queryString = "SELECT * FROM " +
                TABLE_Vehicle +
                " WHERE " + COLUMN_ACCOUNT_ID + " = '" + accountNumber + "';"

        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            truck.vehicleId = cursor.getInt(0)
            truck.plate = cursor.getString(1)
            truck.vin = cursor.getString(2)
            truck.manufacturer = cursor.getString(3)
            truck.model = cursor.getString(4)
            truck.vehicleColor = cursor.getString(5)
            truck.capacity = cursor.getString(6)
            truck.trailer = cursor.getString(7)
            truck.ownerAccountNumber = cursor.getInt(8)
        }

        cursor.close()
        db.close()

        return truck
    }

    val trailerTypes: ArrayList<TrailerModel>
        //get trailer types (id and name)
        get() {
            val list = ArrayList<TrailerModel>()

            var TrailerTypeId: Int
            var TrailerTypeName: String

            val queryString = "SELECT * FROM " +
                    TABLE_Trailer_Type + ";"

            val db = this.readableDatabase
            val cursor = db.rawQuery(queryString, null)

            if (cursor.moveToFirst()) {
                do {
                    TrailerTypeId = cursor.getInt(0)
                    TrailerTypeName = cursor.getString(1)

                    list.add(TrailerModel(TrailerTypeId, TrailerTypeName))
                } while (cursor.moveToNext())
            } else {
                TrailerTypeId = 0
                list.add(TrailerModel(TrailerTypeId, null.toString()))
            }

            cursor.close()
            db.close()

            return list
        }

    val trailerNames: ArrayList<String?>
        //get trailer names
        get() {
            val list = ArrayList<String?>()

            var TrailerTypeName: String

            val queryString = "SELECT " + COLUMN_TRAILER_NAME + " FROM " +
                    TABLE_Trailer_Type + ";"

            val db = this.readableDatabase
            val cursor = db.rawQuery(queryString, null)

            if (cursor.moveToFirst()) {
                do {
                    TrailerTypeName = cursor.getString(0)

                    list.add(TrailerTypeName)
                } while (cursor.moveToNext())
            } else {
                list.add(null)
            }

            cursor.close()
            db.close()

            return list
        }

    //Method to update Driver Availability.
    fun updateDriverAvailability(accountId: Int?, driverStatus: Int?): Boolean {
        //Get the Data Repository in write mode

        val db = this.writableDatabase

        //Create a new map of values, where column names are the keys
        val cValues = ContentValues()

        cValues.put(COLUMN_DRIVER_STATUS, driverStatus)

        //update record on database
        // The WHERE clause specifies which record(s) to update.
        val selectAccount = COLUMN_ACCOUNT_ID + " = ?"
        val selectionArgs = arrayOf(accountId.toString())

        // Perform the update.
        val count = db.update(
            TABLE_Account,
            cValues,
            selectAccount,
            selectionArgs
        )

        //close database
        db.close()

        return if (count > 0) {
            true
        } else {
            false
        }
    }

    //retrieving getDriverActivity
    fun getDriverActivity(accountNumber: Int): ArrayList<DriverActivity> {
        val driverActivity = ArrayList<DriverActivity>()

        val queryString = "SELECT * FROM " +
                TABLE_Driver_Activity +
                " WHERE " + COLUMN_ACCOUNT_ID + " = " + accountNumber + ";"

        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            do {
                val data = DriverActivity()

                data.driverActivityId = cursor.getInt(0)
                data.driverActivityContractor = cursor.getString(1)
                data.driverActivityContractorEmployee = cursor.getString(2)
                data.driverActivityContractorPhone = cursor.getString(3)
                data.driverActivityDestination = cursor.getString(4)
                data.driverActivityTypeOfLoad = cursor.getString(5)
                data.driverActivityShippingOffer = cursor.getString(6)
                data.driverActivityStatus = cursor.getInt(7)
                data.driverActivityDate = cursor.getString(8)
                data.accountId = cursor.getInt(9)

                driverActivity.add(data)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return driverActivity
    }

    // Create addDriverActivity
    fun addDriverActivity(
        contractor: String?,
        contractorEmployee: String?,
        contractorPhone: String?,
        destination: String?,
        typeOfLoad: String?,
        shippingOffer: String?,
        status: Int,
        date: String?,
        accountId: Int
    ): Boolean {
        //Get the Data Repository in write mode

        val db = this.writableDatabase

        //Create a new map of values, where column names are the keys
        val cValues = ContentValues()
        cValues.put(COLUMN_CONTRACTOR, contractor)
        cValues.put(COLUMN_CONTRACTOR_EMPLOYEE, contractorEmployee)
        cValues.put(COLUMN_CONTRACTOR_PHONE, contractorPhone)
        cValues.put(COLUMN_DESTINATION, destination)
        cValues.put(COLUMN_LOAD_TYPE, typeOfLoad)
        cValues.put(COLUMN_SHIPPING_OFFER, shippingOffer)
        cValues.put(COLUMN_ACTIVITY_STATUS, status)
        cValues.put(COLUMN_ACTIVITY_DATE, date)
        cValues.put(COLUMN_ACCOUNT_ID, accountId)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(TABLE_Driver_Activity, null, cValues)

        //close database
        db.close()

        return if (newRowId == -1L) {
            false
        } else {
            true
        }
    }

    //method to update updateDriverActivity.
    fun updateDriverActivity(
        activityId: Int,
        contractor: String?,
        contractorEmployee: String?,
        contractorPhone: String?,
        destination: String?,
        typeOfLoad: String?,
        shippingOffer: String?,
        status: Int,
        date: String?,
        accountId: Int
    ): Boolean {
        //Get the Data Repository in write mode

        val db = this.writableDatabase

        //Create a new map of values, where column names are the keys
        val cValues = ContentValues()

        cValues.put(COLUMN_CONTRACTOR, contractor)
        cValues.put(COLUMN_CONTRACTOR_EMPLOYEE, contractorEmployee)
        cValues.put(COLUMN_CONTRACTOR_PHONE, contractorPhone)
        cValues.put(COLUMN_DESTINATION, destination)
        cValues.put(COLUMN_LOAD_TYPE, typeOfLoad)
        cValues.put(COLUMN_SHIPPING_OFFER, shippingOffer)
        cValues.put(COLUMN_ACTIVITY_STATUS, status)
        cValues.put(COLUMN_ACTIVITY_DATE, date)
        cValues.put(COLUMN_ACCOUNT_ID, accountId)

        //update record on database
        // The WHERE clause specifies which record(s) to update.
        val selectAccount = COLUMN_ACTIVITY_ID + " = ?"
        val selectionArgs = arrayOf(activityId.toString())

        // Perform the update.
        val count = db.update(
            TABLE_Driver_Activity,
            cValues,
            selectAccount,
            selectionArgs
        )

        //close database
        db.close()

        return if (count > 0) {
            true
        } else {
            false
        }
    }

    // finishing all account sessions on database
    fun UpdateAccountIdOnActivityDriver(accountId: Int) {
        //Get the Data Repository in write mode

        val db = this.writableDatabase

        // Update operation
        val cValues = ContentValues()
        cValues.put(COLUMN_ACCOUNT_ID, 9)

        val queryString = "UPDATE " + TABLE_Driver_Activity +
                " SET " + COLUMN_ACCOUNT_ID + " = " + accountId +
                " WHERE " + COLUMN_ACCOUNT_ID + " = " + accountId + ";"

        db.execSQL(queryString)

        db.close()
    }

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "TruckDriver_db"

        //variables for Account Table
        private const val TABLE_Account = "ACCOUNT"
        private const val COLUMN_ACCOUNT_ID = "AccountId"
        private const val COLUMN_FIRST_NAME = "FirstName"
        private const val COLUMN_LAST_NAME = "LastName"
        private const val COLUMN_PHONE_NUMBER = "PhoneNumber"
        private const val COLUMN_EMAIL = "Email"
        private const val COLUMN_PASSWORD = "Password"
        private const val COLUMN_ENABLE = "Enable"
        private const val COLUMN_DRIVER_STATUS = "DriverStatus"

        //variables for Session Table
        private const val TABLE_Session = "Session"
        private const val COLUMN_TIME_BEGIN = "TimeBegin"
        private const val COLUMN_TIME_END = "TimeEnd"
        private const val COLUMN_IS_ON = "IsON"

        //variables for Vehicle Table
        private const val TABLE_Vehicle = "Vehicle"
        private const val COLUMN_VEHICLE_ID = "VehicleId"
        private const val COLUMN_PLATE = "Plate"
        private const val COLUMN_VIN = "Vin"
        private const val COLUMN_MANUFACTURER = "Manufacturer"
        private const val COLUMN_MODEL = "Model"
        private const val COLUMN_COLOR = "Color"
        private const val COLUMN_CAPACITY = "Capacity"
        private const val COLUMN_TRAILER = "Trailer"

        //variables for Trailer Type Table
        private const val TABLE_Trailer_Type = "TrailerType"
        private const val COLUMN_TRAILER_TYPE_ID = "TrailerTypeId"
        private const val COLUMN_TRAILER_NAME = "TrailerTypeName"

        //variables for Driver Activities Table
        private const val TABLE_Driver_Activity = "ACTIVITY"
        private const val COLUMN_ACTIVITY_ID = "activityId"
        private const val COLUMN_CONTRACTOR = "contractor"
        private const val COLUMN_CONTRACTOR_EMPLOYEE = "contractorEmployee"
        private const val COLUMN_CONTRACTOR_PHONE = "contractorPhone"
        private const val COLUMN_DESTINATION = "destination"
        private const val COLUMN_LOAD_TYPE = "TypeOfLoad"
        private const val COLUMN_SHIPPING_OFFER = "ShippingOffer"
        private const val COLUMN_ACTIVITY_STATUS = "activityStatus"
        private const val COLUMN_ACTIVITY_DATE = "activityDate"
    }
}
