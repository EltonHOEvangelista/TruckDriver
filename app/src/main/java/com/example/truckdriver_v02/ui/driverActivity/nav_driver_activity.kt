package com.example.truckdriver_v02.ui.driverActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.truckdriver_v02.R
import com.example.truckdriver_v02.data.SQLite.DbHandler
import com.example.truckdriver_v02.data.driverActivity.DriverActivity

class nav_driver_activity : Fragment() {
    lateinit var btnActOne: Button
    lateinit var btnActTwo: Button
    var txtActivityTwo: TextView? = null

    //get driver activities
    var getDriverActivity: ArrayList<DriverActivity>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_driver_activity, container, false)

        btnActOne = view.findViewById(R.id.btnActOne)
        btnActTwo = view.findViewById(R.id.btnActTwo)

        btnActOne.setOnClickListener(View.OnClickListener {
            val driverActivityDetails = DriverActivityDetails()
            driverActivityDetails.show(
                requireActivity().supportFragmentManager,
                "fragment_driver_activity_details"
            )
        })

        btnActTwo.setOnClickListener(View.OnClickListener {
            val driverActivityDetails = DriverActivityDetails()
            driverActivityDetails.show(
                requireActivity().supportFragmentManager,
                "fragment_driver_activity_details"
            )
        })

        //method to load driver activities.
        driverActivity

        return view
    }

    private val driverActivity: Unit
        get() {
            //Instantiating DbHandler

            val dbHandler = DbHandler(activity)

            //get opened session (equal 1) on app by account id
            val accountId = dbHandler.openedSession

            //get driver activities
            getDriverActivity = dbHandler.getDriverActivity(accountId)

            //close dbHandler
            dbHandler.close()

            val activityOne =
                getDriverActivity!!.get(0).driverActivityId.toString() + " | " + getDriverActivity!!.get(
                    0
                ).driverActivityDate.toString() + " | " + getDriverActivity!!.get(0).driverActivityContractor.toString()

            val activityTwo =
                getDriverActivity!!.get(1).driverActivityId.toString() + " | " + getDriverActivity!!.get(
                    1
                ).driverActivityDate.toString() + " | " + getDriverActivity!!.get(1).driverActivityContractor.toString()

            //set name on list
            btnActOne!!.text = activityOne
            btnActTwo!!.text = activityTwo
        }

    companion object {
        fun newInstance(): nav_driver_activity {
            return nav_driver_activity()
        }
    }
}