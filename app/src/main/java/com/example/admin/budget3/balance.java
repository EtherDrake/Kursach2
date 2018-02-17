package com.example.admin.budget3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import Classes.Methods;
import Classes.User;

public class balance extends AppCompatActivity {

    PieChart pieChart;
    Spinner spinner;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pieChart = findViewById(R.id.piechart);
        spinner = findViewById(R.id.spinner4);

        String[] arraySpinner = new String[] {
                "Витрати", "Доходи"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setChart(position == 1);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        pieChart.setUsePercentValues(true);

        user= Methods.load(this);

        setChart(false);


    }

    public void setChart(boolean type)
    {
        double[] sums;
        if(type) sums=new double[user.categoriesIncome.size()];
        else sums=new double[user.categoriesOutlay.size()];

        for (int i = 0; i < sums.length; i++) sums[i] = 0;

        for (int j = 0; j < user.balanceActions.size(); j++) {
            if (user.balanceActions.get(j).amount > 0 && type) {
                int index = user.categoriesIncome.indexOf(user.balanceActions.get(j).category);
                sums[index] += Math.abs(user.balanceActions.get(j).amount);
            }
            else if(user.balanceActions.get(j).amount < 0 && !type)
            {
                int index = user.categoriesOutlay.indexOf(user.balanceActions.get(j).category);
                sums[index] += Math.abs(user.balanceActions.get(j).amount);
            }
        }

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        for(int i=0;i<sums.length; i++)
        {
            double value=sums[i];
            String label;
            if(type) label=user.categoriesIncome.get(i);
            else label=user.categoriesOutlay.get(i);

            if(value>0)
            {
                yvalues.add(new Entry((float) value, i));
                xVals.add(label);
            }

        }

        PieDataSet dataSet = new PieDataSet(yvalues, "");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(16f);
        pieChart.setData(data);
        pieChart.setDescription("");
        pieChart.getLegend().setEnabled(false);
    }

}
