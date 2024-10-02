package com.example.truckdriver_v02.ui.dstatus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.truckdriver_v02.R
import com.example.truckdriver_v02.data.SQLite.DbHandler
import com.example.truckdriver_v02.data.account.Driver
import com.example.truckdriver_v02.ui.home.HomeFragment

class driver_status : DialogFragment() {
    lateinit var btnAvailable: Button
    lateinit var btnOutOfService: Button
    var driver: Driver = Driver(null.toString(), null.toString())
    val driverAvailable: Int = 1
    val driverUnavailable: Int = 0

    //to set driver availability on Driver Class
    var homeFragment: HomeFragment = HomeFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_driver_status, container, false)

        btnAvailable = view.findViewById(R.id.btnAvailable)
        btnOutOfService = view.findViewById(R.id.btnUnavailable)

        btnAvailable.setOnClickListener(View.OnClickListener { //calling method to set driver as available for new loadings on database
            SetDriverAsAvailable()

            //to set driver availability on Home Fragment, Driver Class
            homeFragment.driver.driverStatus = driverAvailable

            //calling method to update marker (vehicle) on map.
            homeFragment.ResetMarkerOption()
            dismiss()
        })

        btnOutOfService.setOnClickListener(View.OnClickListener { //calling method to set driver as available for new loadings on database
            SetDriverAsUnavailable()

            //to set driver availability on Home Fragment, Driver Class
            homeFragment.driver.driverStatus = driverUnavailable

            //calling method to update marker (vehicle) on map.
            homeFragment.ResetMarkerOption()
            dismiss()
        })

        return view
    }

    //method to set driver as available update Account database table
    private fun SetDriverAsAvailable(): Boolean {
        //calling method to retrieve account number from database and set it on Driver Class.

        GetAccountNumber()

        driver.driverStatus = driverAvailable

        var accountUpdated = false

        val dbHandler = DbHandler(activity)

        accountUpdated = dbHandler.updateDriverAvailability(driver.accountId, driver.driverStatus)

        dbHandler.close()

        return accountUpdated
    }

    //method to set driver as unavailable update Account database table
    private fun SetDriverAsUnavailable(): Boolean {
        //calling method to retrieve account number from database and set it on Driver Class.

        GetAccountNumber()

        driver.driverStatus = driverUnavailable

        var accountUpdated = false

        val dbHandler = DbHandler(activity)

        accountUpdated = dbHandler.updateDriverAvailability(driver.accountId, driver.driverStatus)

        dbHandler.close()

        return accountUpdated
    }

    //retrieve account number from database and set it on Driver Class.
    private fun GetAccountNumber() {
        //Instantiating DbHandler

        val dbHandler = DbHandler(activity)

        //get opened session (equal 1) on app by account id
        driver.accountId = dbHandler.openedSession

        //close dbHandler
        dbHandler.close()
    }
}