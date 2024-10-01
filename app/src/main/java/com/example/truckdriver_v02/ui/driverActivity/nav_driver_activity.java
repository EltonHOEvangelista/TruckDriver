package com.example.truckdriver_v02.ui.driverActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.truckdriver_v02.R;
import com.example.truckdriver_v02.data.SQLite.DbHandler;
import com.example.truckdriver_v02.data.driverActivity.DriverActivity;

import java.util.ArrayList;

public class nav_driver_activity extends Fragment {

    Button btnActOne;
    Button btnActTwo;
    TextView txtActivityTwo;

    //get driver activities
    ArrayList<DriverActivity> getDriverActivity;

    public static nav_driver_activity newInstance() {
        return new nav_driver_activity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_driver_activity, container, false);

        btnActOne = view.findViewById(R.id.btnActOne);
        btnActTwo = view.findViewById(R.id.btnActTwo);

        btnActOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DriverActivityDetails driverActivityDetails = new DriverActivityDetails();
                driverActivityDetails.show(getActivity().getSupportFragmentManager(), "fragment_driver_activity_details");
            }
        });

        btnActTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DriverActivityDetails driverActivityDetails = new DriverActivityDetails();
                driverActivityDetails.show(getActivity().getSupportFragmentManager(), "fragment_driver_activity_details");
            }
        });

        //method to load driver activities.
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

        String activityOne = String.valueOf(getDriverActivity.get(0).getDriverActivityId()) + " | " +
                String.valueOf(getDriverActivity.get(0).getDriverActivityDate()) + " | " +
                String.valueOf(getDriverActivity.get(0).getDriverActivityContractor());

        String activityTwo = String.valueOf(getDriverActivity.get(1).getDriverActivityId()) + " | " +
                String.valueOf(getDriverActivity.get(1).getDriverActivityDate()) + " | " +
                String.valueOf(getDriverActivity.get(1).getDriverActivityContractor());

        //set name on list
        btnActOne.setText(activityOne);
        btnActTwo.setText(activityTwo);
    }
}