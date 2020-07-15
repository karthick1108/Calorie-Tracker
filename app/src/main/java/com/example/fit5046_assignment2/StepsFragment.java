package com.example.fit5046_assignment2;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;


public class StepsFragment extends Fragment {
    DailyStepsDatabase db;
    EditText readSteps;
    Button stepsAdd;
    Button b_delete;
    List<HashMap<String, String>> readStepsArray;
    SimpleAdapter myStepsListAdapter;
    TextView tv_show;
    ListView stepsList;
    DailySteps firstValue;
    List<DailySteps> dailyStepsList;
    HashMap<String, String> map = new HashMap<String, String>();
    String[] colHEAD = new String[]{"STEPID", "STEPSCOUNT", "DATETIME"};
    int[] dataCell = new int[]{R.id.tv_stepid, R.id.tv_stepscount, R.id.tv_date};
    String value;
    View vSteps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        vSteps = inflater.inflate(R.layout.fragment_steps, container, false);
        stepsAdd = (Button) vSteps.findViewById(R.id.b_setSteps);
        tv_show = (TextView) vSteps.findViewById(R.id.tv_show);
        stepsList = vSteps.findViewById(R.id.list_view);
        readSteps = vSteps.findViewById(R.id.et_steps);
        b_delete = vSteps.findViewById(R.id.b_delete);


        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                DailyStepsDatabase.class, "DailyStepsDatabase")
                .fallbackToDestructiveMigration()
                .build();

        readData read = new readData();
        read.execute();

        stepsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputtedStep = readSteps.getText().toString();
                InsertData addDatabase = new InsertData();
                addDatabase.execute(inputtedStep);
                //addTotalSteps();
            }
        });

        b_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteData deleteDatabase = new DeleteData();
                deleteDatabase.execute();
            }
        });


        stepsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                firstValue = dailyStepsList.get(position);
                System.out.println("Item is"+" "+firstValue);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setTitle("UPDATE");
                alertBuilder.setMessage("Enter Goal");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                final EditText et_input = new EditText(getActivity());
                et_input.setLayoutParams(lp);
                alertBuilder.setView(et_input);
                alertBuilder.setPositiveButton("OK",
                       null);

                alertBuilder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog mAlertDialog = alertBuilder.create();
                mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialog) {

                        Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // TODO Do something
                                value = et_input.getText().toString();
                                if(!value.isEmpty()) {
                                    updateData updateDatabase = new updateData();
                                    updateDatabase.execute();
                                    mAlertDialog.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Cannot be empty. Please enter a value",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                mAlertDialog.show();
            }
        });

        return vSteps;
    }


    private class readData extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            String userId = getArguments().getString("userid");
            Double Id = Double.parseDouble(userId);
            Integer newId = (Integer) Id.intValue();
            readSteps = vSteps.findViewById(R.id.et_steps);
            readStepsArray = new ArrayList<HashMap<String, String>>();
            Boolean stepsCount = false;
            dailyStepsList = db.dailyStepsDao().findByUsrid(newId);
            System.out.println("Steps list are" + " " + dailyStepsList);
            if (!(dailyStepsList.isEmpty() || dailyStepsList == null)) {
                stepsCount = true;

                for (DailySteps stepsEntered : dailyStepsList) {
                    map = new HashMap<String, String>();
                    map.put("STEPID", Integer.toString(stepsEntered.getStepId()));
                    map.put("STEPSCOUNT", Integer.toString(stepsEntered.getUsrSteps()));
                    map.put("DATETIME", stepsEntered.getEntryDate().toString());
                    readStepsArray.add(map);
                }
            }
            return stepsCount;

        }

        @Override
        protected void onPostExecute(Boolean stepsCount) {

            if (!stepsCount)
            {
                tv_show.setText("Steps history not found");
            }
            else {
                myStepsListAdapter = new SimpleAdapter(vSteps.getContext(), readStepsArray, R.layout.list_view, colHEAD, dataCell);
                stepsList.setAdapter(myStepsListAdapter);
                myStepsListAdapter.notifyDataSetChanged();
            }

        }

    }

    private class InsertData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String chkSteps = readSteps.getText().toString();
            String userId = getArguments().getString("userid");
            // converting userid to Integer
            Double Id = Double.parseDouble(userId);
            Integer newId = (Integer) Id.intValue();

            // converting steps count entered into string
            Integer stepsCount = Integer.parseInt(chkSteps);
            Date date = new Date();
            DailySteps dailySteps = new DailySteps(newId, stepsCount, date);
            long stepId = db.dailyStepsDao().insert(dailySteps);
            String newStepId = Long.toString(stepId);
            return newStepId;
        }

        @Override
        protected void onPostExecute(String details) {
            tv_show.setText("Inserted");
            readData read = new readData();
            read.execute();
        }
    }

    private class updateData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            int newValue = Integer.parseInt(value);
            System.out.println("Entered value is"+ " "+newValue);
            if (firstValue!=null) {
                firstValue.setUsrSteps(newValue);
                db.dailyStepsDao().update(firstValue);
            }
            return "Success";
        }

        @Override
        protected void onPostExecute(String value) {
            tv_show.setText("Updated");
            readData read = new readData();
            read.execute();
        }
    }


    private class DeleteData extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... params) {
            db.dailyStepsDao().deleteAll();
            return "";
        }
        @Override
        protected void onPostExecute(String result) {
            tv_show.setText("Deleted");
        }
    }

}




