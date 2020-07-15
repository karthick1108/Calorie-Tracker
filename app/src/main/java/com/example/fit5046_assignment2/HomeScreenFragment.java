package com.example.fit5046_assignment2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class HomeScreenFragment extends Fragment {
    private View vHome;
    private TextView tv_view;
    private Button b_editgoal;
    private EditText et_goal;
    private TextView tv_showdate;
    private TextView tv_displaygoal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vHome = inflater.inflate(R.layout.fragment_homescreen, container, false);
        b_editgoal = (Button) vHome.findViewById(R.id.b_setgoal);
        et_goal = (EditText) vHome.findViewById(R.id.et_setgoal);

        tv_showdate = (TextView) vHome.findViewById(R.id.tv_showdate);
        tv_displaygoal = (TextView) vHome.findViewById(R.id.tv_displaygoal);
        String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
        tv_showdate.setText(currentDateTime);
        String message = getArguments().getString("firstname");
        TextView textView = (TextView)  vHome.findViewById(R.id.tv_view);
        textView.setText("Welcome"+ " " + message +"!"+ " " + "How are you?");
        textView.setTextColor(Color.BLUE);

        b_editgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String getGoal = et_goal.getText().toString();
                if(!(getGoal.isEmpty() || getGoal.equals("0"))) {
                    tv_displaygoal.setText("You have set your goal as:" + " " + getGoal);
                    storeGoal(getGoal);
                }
                else
                {
                    tv_displaygoal.setText("Goal cannot be empty or zero. Please type positive integer" );
                }
            }
        });


        return vHome;
    }

    public void storeGoal(String getGoal)
    {
        SharedPreferences spMyPreferences = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String details="Goal entered is: " + getGoal;
        SharedPreferences.Editor eMyGoal = spMyPreferences.edit();
        eMyGoal.putString("Goals", getGoal);
        eMyGoal.apply();
    }
}
