package com.example.admin.budget3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import java.util.Date;

import Classes.Methods;
import Classes.User;
import Classes.balanceAction;

public class balance extends AppCompatActivity {

    PieChart pieChart;
    Spinner spinner, spinner2;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pieChart = findViewById(R.id.piechart);
        spinner = findViewById(R.id.spinner4);
        spinner2=findViewById(R.id.spinner6);

        String[] arraySpinner = new String[] {
                "Витрати", "Доходи"
        };

        String[] arraySpinner2 = new String[] {
                "День", "Місяць", "Рік", "Весь час"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner2.setAdapter(adapter2);
        spinner2.setSelection(3);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setChart(position == 1, 3);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setChart(spinner.getSelectedItemPosition() == 1, position);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        pieChart.setUsePercentValues(true);

        user= Methods.load(this);

        setChart(false,3);


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

    public void setChart(boolean type, int period)
    {
        double[] sums;
        if(type) sums=new double[user.categoriesIncome.size()];
        else sums=new double[user.categoriesOutlay.size()];

        for (int i = 0; i < sums.length; i++) sums[i] = 0;
        Date today=new Date();

        switch (period) {
            case 0:
                for (int j = 0; j < user.balanceActions.size(); j++) {
                    balanceAction action=user.balanceActions.get(j);

                    if (action.amount > 0 && action.date.getYear()==today.getYear() && action.date.getMonth()==today.getMonth() && action.date.getDate()==today.getDate() && type) {
                        int index = user.categoriesIncome.indexOf(action.category);
                        sums[index] += Math.abs(action.amount);
                    } else if (action.amount < 0 && action.date.getYear()==today.getYear() && action.date.getMonth()==today.getMonth() && action.date.getDate()==today.getDate() && !type) {
                        int index = user.categoriesOutlay.indexOf(action.category);
                        sums[index] += Math.abs(action.amount);
                    }
                }
                break;

            case 1:
                for (int j = 0; j < user.balanceActions.size(); j++) {
                    balanceAction action=user.balanceActions.get(j);

                    if (action.amount > 0 && action.date.getYear()==today.getYear() && action.date.getMonth()==today.getMonth() && type) {
                        int index = user.categoriesIncome.indexOf(action.category);
                        sums[index] += Math.abs(action.amount);
                    } else if (action.amount < 0 && action.date.getYear()==today.getYear() && action.date.getMonth()==today.getMonth() && !type) {
                        int index = user.categoriesOutlay.indexOf(action.category);
                        sums[index] += Math.abs(action.amount);
                    }
                }
                break;

            case 2:
                for (int j = 0; j < user.balanceActions.size(); j++) {
                    balanceAction action = user.balanceActions.get(j);

                    if (action.amount > 0 && action.date.getYear() == today.getYear() && type) {
                        int index = user.categoriesIncome.indexOf(action.category);
                        sums[index] += Math.abs(action.amount);
                    } else if (action.amount < 0 && action.date.getYear() == today.getYear() && !type) {
                        int index = user.categoriesOutlay.indexOf(action.category);
                        sums[index] += Math.abs(action.amount);
                    }
                }
                break;

            case 3:
                for (int j = 0; j < user.balanceActions.size(); j++) {
                    if (user.balanceActions.get(j).amount > 0 && type) {
                        int index = user.categoriesIncome.indexOf(user.balanceActions.get(j).category);
                        sums[index] += Math.abs(user.balanceActions.get(j).amount);
                    } else if (user.balanceActions.get(j).amount < 0 && !type) {
                        int index = user.categoriesOutlay.indexOf(user.balanceActions.get(j).category);
                        sums[index] += Math.abs(user.balanceActions.get(j).amount);
                    }
                }
                break;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
