package com.example.fit5046_assignment2;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class CalorieTrackerScreenFragment extends Fragment {
    DailyStepsDatabase db;
    TextView tv_showgoalsset;
    TextView tv_burnedcalatrest;
    TextView tv_showstepstaken;
    TextView tv_burnedpersteps;
    TextView tv_consumedcal;
    Double newsteps;
    Integer userid;

    View vCalorieTracker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vCalorieTracker = inflater.inflate(R.layout.fragment_calorietracker, container, false);
        SharedPreferences spMyPreferences = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String goalDetails= spMyPreferences.getString("Goals",null);
        tv_showgoalsset = (TextView) vCalorieTracker.findViewById(R.id.tv_showgoalsset);
        tv_burnedcalatrest = (TextView) vCalorieTracker.findViewById(R.id.tv_burnedcalatrest);
        tv_showstepstaken = (TextView) vCalorieTracker.findViewById(R.id.tv_showstepstaken);
        tv_burnedpersteps = (TextView) vCalorieTracker.findViewById(R.id.tv_burnedpersteps);
        tv_consumedcal = (TextView) vCalorieTracker.findViewById(R.id.tv_consumedcal);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                DailyStepsDatabase.class, "DailyStepsDatabase")
                .fallbackToDestructiveMigration()
                .build();

        tv_showgoalsset.setText("Goal set is:"+" "+goalDetails);
        String Id = getArguments().getString("userid");
        Double newId = Double.valueOf(Id);
        userid = newId.intValue();
        TotalStepsAsync totalStepsAsync = new TotalStepsAsync();
        totalStepsAsync.execute();
        ShowBurnedCaloriesAtRestAsync burnedCalorieAsync = new ShowBurnedCaloriesAtRestAsync();
        burnedCalorieAsync.execute(userid);
        CaloriesConsumedAsync caloriesConsumed = new CaloriesConsumedAsync();
        caloriesConsumed.execute(userid);
        return vCalorieTracker;
    }

    private class ShowBurnedCaloriesAtRestAsync extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            return RestAPIClient.calculateCaloriesBurnedAtRest(params[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            tv_burnedcalatrest.setText("Total Calories Burned at Rest:" + " "+result);
        }
    }

    private class CaloriesBurnedByStepAsync extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            return RestAPIClient.calculateCaloriesBurnedPerStep(params[0],params[1]);
        }
        @Override
        protected void onPostExecute(String result) {

            tv_burnedpersteps.setText("Total Calories Burned by steps:" + " "+result);
        }
    }

    private class CaloriesConsumedAsync extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            return RestAPIClient.findCaloriesConsumedForOneUser(params[0]);
        }
        @Override
        protected void onPostExecute(String result) {
            Double finalResult = Double.valueOf(result);
            tv_consumedcal.setText("Total Calories Consumed:" + " "+result);
        }
    }

    private class TotalStepsAsync extends AsyncTask<Void, Void, List<DailySteps>>
    {
        protected List<DailySteps> doInBackground(Void...params){
            List<DailySteps> totalSteps = db.dailyStepsDao().getAll();
            return totalSteps;
        }

        protected void onPostExecute(List<DailySteps> totalSteps) {
            int usrsteps = receivedSteps(totalSteps);
            tv_showstepstaken.setText("Steps:"+" "+usrsteps);
            CaloriesBurnedByStepAsync caloriesBurnedByStepAsync = new CaloriesBurnedByStepAsync();
            caloriesBurnedByStepAsync.execute(userid,usrsteps);
        }

    }

    public int receivedSteps(List<DailySteps> totalSteps)
    {
        int total = 0;
        for(DailySteps StepsList : totalSteps )
        {
            total += StepsList.getUsrSteps();
            System.out.println(total);
        }
        return total;
    }

}
