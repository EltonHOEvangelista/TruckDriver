package com.example.truckdriver_v02.ui.driverActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.truckdriver_v02.R
import com.example.truckdriver_v02.data.SQLite.DbHandler
import com.example.truckdriver_v02.data.driverActivity.DriverActivity

class DriverActivityDetails : DialogFragment() {
    lateinit var btnClose: Button
    var txtId: TextView? = null
    var txtConstructor: TextView? = null
    var txtContEmployee: TextView? = null
    var txtPhone: TextView? = null
    var txtDestination: TextView? = null
    var txtLoadType: TextView? = null
    var txtOffer: TextView? = null
    var txtStatus: TextView? = null
    var txtDate: TextView? = null

    var getDriverActivity: ArrayList<DriverActivity>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_driver_activity_details, container, false)

        btnClose = view.findViewById(R.id.btnActDetailClose)

        txtId = view.findViewById(R.id.txtActId)

        txtConstructor = view.findViewById(R.id.txtActConstructor)

        txtContEmployee = view.findViewById(R.id.txtActConstEmployee)

        txtPhone = view.findViewById(R.id.txtActPhone)

        txtDestination = view.findViewById(R.id.txtActDestination)

        txtLoadType = view.findViewById(R.id.txtActTLoad)

        txtOffer = view.findViewById(R.id.txtActSOffer)

        txtStatus = view.findViewById(R.id.txtActStatus)

        txtDate = view.findViewById(R.id.txtActDate)

        btnClose.setOnClickListener(View.OnClickListener { dismiss() })

        //getDriverActivity
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

            txtId!!.text = getDriverActivity!!.get(0).driverActivityId.toString()
            txtConstructor!!.text = getDriverActivity!!.get(0).driverActivityContractor.toString()
            txtContEmployee!!.text =
                getDriverActivity!!.get(0).driverActivityContractorEmployee.toString()
            txtPhone!!.text = getDriverActivity!!.get(0).driverActivityContractorPhone.toString()
            txtDestination!!.text = getDriverActivity!!.get(0).driverActivityDestination.toString()
            txtLoadType!!.text = getDriverActivity!!.get(0).driverActivityTypeOfLoad.toString()
            txtOffer!!.text = getDriverActivity!!.get(0).driverActivityShippingOffer.toString()
            txtStatus!!.text = getDriverActivity!!.get(0).driverActivityStatus.toString()
            txtDate!!.text = getDriverActivity!!.get(0).driverActivityDate.toString()
        }
}