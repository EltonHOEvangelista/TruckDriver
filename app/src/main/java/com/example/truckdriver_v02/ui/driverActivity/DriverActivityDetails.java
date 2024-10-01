package com.example.truckdriver_v02.ui.driverActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.example.truckdriver_v02.R;
import com.example.truckdriver_v02.data.SQLite.DbHandler;
import com.example.truckdriver_v02.data.driverActivity.DriverActivity;

import java.util.ArrayList;

public class DriverActivityDetails extends DialogFragment {

    Button btnClose;

    TextView txtId;
    TextView txtConstructor;
    TextView txtContEmployee;
    TextView txtPhone;
    TextView txtDestination;
    TextView txtLoadType;
    TextView txtOffer;
    TextView txtStatus;
    TextView txtDate;

    ArrayList<DriverActivity> getDriverActivity;

    public DriverActivityDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_driver_activity_details, container, false);

        btnClose = view.findViewById(R.id.btnActDetailClose);

        txtId = view.findViewById(R.id.txtActId);

        txtConstructor = view.findViewById(R.id.txtActConstructor);

        txtContEmployee = view.findViewById(R.id.txtActConstEmployee);

        txtPhone = view.findViewById(R.id.txtActPhone);

        txtDestination = view.findViewById(R.id.txtActDestination);

        txtLoadType = view.findViewById(R.id.txtActTLoad);

        txtOffer = view.findViewById(R.id.txtActSOffer);

        txtStatus = view.findViewById(R.id.txtActStatus);

        txtDate = view.findViewById(R.id.txtActDate);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //getDriverActivity
        getDriverActivity();

        return view;
    }

    private void getDriverActivity() {

        //Instantiating DbHandler
        DbHandler dbHandler = new DbHandler(getActivity());

        //get opened session (equal 1) on app by account id
        int accountId = dbHandler.getOpenedSession();

        //get driver activities
        getDriverActivity = dbHandler.getDriverActivity(accountId);

        //close dbHandler
        dbHandler.close();

        txtId.setText(String.valueOf(getDriverActivity.get(0).getDriverActivityId()));
        txtConstructor.setText(String.valueOf(getDriverActivity.get(0).getDriverActivityContractor()));
        txtContEmployee.setText(String.valueOf(getDriverActivity.get(0).getDriverActivityContractorEmployee()));
        txtPhone.setText(String.valueOf(getDriverActivity.get(0).getDriverActivityContractorPhone()));
        txtDestination.setText(String.valueOf(getDriverActivity.get(0).getDriverActivityDestination()));
        txtLoadType.setText(String.valueOf(getDriverActivity.get(0).getDriverActivityTypeOfLoad()));
        txtOffer.setText(String.valueOf(getDriverActivity.get(0).getDriverActivityShippingOffer()));
        txtStatus.setText(String.valueOf(getDriverActivity.get(0).getDriverActivityStatus()));
        txtDate.setText(String.valueOf(getDriverActivity.get(0).getDriverActivityDate()));
    }
}