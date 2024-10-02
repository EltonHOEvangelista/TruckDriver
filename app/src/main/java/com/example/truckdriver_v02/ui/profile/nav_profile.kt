package com.example.truckdriver_v02.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.truckdriver_v02.R
import com.example.truckdriver_v02.data.SQLite.DbHandler
import com.example.truckdriver_v02.data.account.Driver
import com.example.truckdriver_v02.data.vehicle.Truck

class nav_profile : Fragment() {
    lateinit var profile_firstName: EditText
    lateinit var profile_lastName: EditText
    lateinit var profile_accountNumber: EditText
    lateinit var profile_phone: EditText
    lateinit var profile_email: EditText
    lateinit var profile_password: EditText
    var accountNumber: Int? = 0

    lateinit var profile_Plate: EditText
    lateinit var profile_Vin: EditText
    lateinit var profile_Manufacturer: EditText
    lateinit var profile_Model: EditText
    lateinit var profile_Color: EditText
    lateinit var profile_Capacity: EditText
    lateinit var profile_Trailer: Spinner
    lateinit var btn_Profile_Update: Button

    //instantiate driver class
    var driver: Driver = Driver(null.toString(), null.toString())

    //instantiate truck class
    var truck: Truck = Truck()

    //spinner to handle trailer options
    var adapter: ArrayAdapter<String?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Find elements by their IDs
        profile_firstName = view.findViewById(R.id.txt_profile_firstName)
        profile_lastName = view.findViewById(R.id.txt_profile_lastName)
        profile_accountNumber = view.findViewById(R.id.txt_profile_accountNumber)
        profile_phone = view.findViewById(R.id.txt_profile_phone)
        profile_email = view.findViewById(R.id.txt_profile_email)
        profile_password = view.findViewById(R.id.txt_profile_password)

        profile_Plate = view.findViewById(R.id.txt_profile_plate)
        profile_Vin = view.findViewById(R.id.txt_profile_vin)
        profile_Manufacturer = view.findViewById(R.id.txt_profile_manufecturer)
        profile_Model = view.findViewById(R.id.txt_profile_model)
        profile_Color = view.findViewById(R.id.txt_profile_color)
        profile_Capacity = view.findViewById(R.id.txt_profile_capacity)
        profile_Trailer = view.findViewById(R.id.spinner_TrailerType)
        btn_Profile_Update = view.findViewById(R.id.btn_Profile)

        //load spinner (trailer options) retrieving data from database;
        loadSpinnerOptions()

        //button to update account profile (driver and vehicle)
        btn_Profile_Update.setOnClickListener(View.OnClickListener { //calling method to add or update vehicle
            val updateVehicle = AddOrUpdateVehicle()

            //calling method to add or update account
            val updateAccount = UpdateAccount()
            if (updateVehicle && updateAccount) {
                Toast.makeText(activity, "Update Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Update fail", Toast.LENGTH_SHORT).show()
            }
        })

        //calling method to retrieve account details from tables session and account on the database
        accountDetails

        //calling method to retrieve vehicle details from database
        vehicleDetails

        return view
    }

    private fun UpdateAccount(): Boolean {
        //get Account data from form and set at Driver Class

        driver.phoneNumber = profile_phone!!.text.toString().toLong()
        driver.passWord = profile_password!!.text.toString()
        driver.eMail = profile_email!!.text.toString()
        driver.accountId = profile_accountNumber!!.text.toString().toInt()

        var accountUpdated = false

        val dbHandler = DbHandler(activity)

        accountUpdated =
            dbHandler.updateAccount(driver.accountId, driver.phoneNumber, driver.passWord, driver.eMail)

        dbHandler.close()

        return accountUpdated
    }

    private fun loadSpinnerOptions() {
        var list = ArrayList<String?>()

        val dbHandler = DbHandler(activity)

        list = dbHandler.trailerNames

        dbHandler.close()

        adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            list
        )

        // Specify the layout to use when the list of choices appears
        adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        profile_Trailer!!.adapter = adapter
    }

    //method to add or update vehicle.
    private fun AddOrUpdateVehicle(): Boolean {
        //get vehicle data from form and set at Truck Class

        truck.plate = profile_Plate!!.text.toString()
        truck.vin = profile_Vin!!.text.toString()
        truck.manufacturer = profile_Manufacturer!!.text.toString()
        truck.model = profile_Model!!.text.toString()
        truck.vehicleColor = profile_Color!!.text.toString()
        truck.capacity = profile_Capacity!!.text.toString()
        truck.trailer = profile_Trailer!!.selectedItem.toString()
        driver.accountId = profile_accountNumber!!.text.toString().toInt()

        var vehicleUpdated = false

        val dbHandler = DbHandler(activity)

        vehicleUpdated = dbHandler.AddOrUpdateVehicle(
            truck.plate,
            truck.vin,
            truck.manufacturer,
            truck.model,
            truck.vehicleColor,
            truck.capacity,
            truck.trailer,
            driver.accountId
        )

        dbHandler.close()

        return vehicleUpdated
    }

    private val accountDetails: Unit
        get() {
            //Instantiating DbHandler

            val dbHandler = DbHandler(activity)

            //get opened session (equal 1) on app by account id
            val accountId = dbHandler.openedSession

            //get account details
            val driverData = dbHandler.getAccountDetails(accountId)

            //close dbHandler
            dbHandler.close()

            //call method to fill up the form with account details
            fillUpAccountForm(driverData)
        }

    //method to fill up the form with account details
    private fun fillUpAccountForm(driverData: Driver) {

        profile_accountNumber!!.setText(driverData.accountId.toString())
        profile_firstName!!.setText(driverData.firstName)
        profile_lastName!!.setText(driverData.lastName)
        profile_phone!!.setText(driverData.phoneNumber.toString())
        profile_email!!.setText(driverData.eMail)
        profile_password!!.setText(driverData.passWord)

        //store account number into global variable to be used in getVehicle method
        accountNumber = driverData.accountId
    }

    private val vehicleDetails: Unit
        //method to get vehicle detail
        get() {
            //Instantiating DbHandler

            val dbHandler = DbHandler(activity)

            //get account details
            val vehicleData = dbHandler.getVehicleDetails(accountNumber)

            //close dbHandler
            dbHandler.close()

            //call method to fill up the form with vehicle details
            fillUpVehicleForm(vehicleData)
        }

    private fun fillUpVehicleForm(vehicleData: Truck) {

        profile_Plate!!.setText(vehicleData.plate)
        profile_Vin!!.setText(vehicleData.vin)
        profile_Manufacturer!!.setText(vehicleData.manufacturer)
        profile_Model!!.setText(vehicleData.model)
        profile_Color!!.setText(vehicleData.vehicleColor)
        profile_Capacity!!.setText(vehicleData.capacity)

        //spinner (trailer options)
        val index = adapter!!.getPosition(vehicleData.trailer)
        profile_Trailer!!.setSelection(index)
    }

    companion object {
        fun newInstance(): nav_profile {
            return nav_profile()
        }
    }
}