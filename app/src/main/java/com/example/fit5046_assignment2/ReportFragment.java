package com.example.fit5046_assignment2;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


public class ReportFragment extends Fragment {

    Button btnPieChart;

    PieChart pieChart;

    View vReport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vReport = inflater.inflate(R.layout.fragment_report, container, false);
        pieChart = vReport.findViewById(R.id.piechart);
        btnPieChart = vReport.findViewById(R.id.b_piechart);
        btnPieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPieChart();

            }
        });

        return vReport;
    }


    private void displayPieChart() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(2500, "Calories Consumed"));
        entries.add(new PieEntry(500, "Calories Burned"));
        entries.add(new PieEntry(300, "Remaining Gaol"));

        PieDataSet set = new PieDataSet(entries, "Fitness Report");
        PieData data = new PieData(set);
        set.setValueTextSize(15f);
        data.setValueFormatter(new PercentFormatter());

        final int[] MY_COLORS = {Color.rgb(210, 5, 7), Color.rgb(100, 255, 250), Color.rgb(110, 120, 130)};
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : MY_COLORS) colors.add(c);
        set.setColors(colors);
        pieChart.setData(data);
        pieChart.invalidate();
        Description description = new Description();

        pieChart.setDescription(description);
        pieChart.animateXY(5000, 5000);

        pieChart.setVisibility(View.VISIBLE);

    }
}
