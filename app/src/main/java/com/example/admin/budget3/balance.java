package com.example.admin.budget3;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

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
    ImageButton calendar;
    Date today=new Date();
    TextView date;
    int myYear, myMonth, myDay;


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
        calendar=findViewById(R.id.imageButton3);
        date=findViewById(R.id.textView38);

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
                calendar.setEnabled(position!=3);
                if(position==3) calendar.setVisibility(View.INVISIBLE);
                else calendar.setVisibility(View.VISIBLE);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                myYear = date.getYear()+1900;
                myMonth = date.getMonth();
                myDay = date.getDay();
                showDialog(1);
            }
        });

        pieChart.setUsePercentValues(true);

        user= Methods.load(this);

        setChart(false,3);
    }


    public void setChart(boolean type, int period)
    {
        double[] sums;
        if(type) sums=new double[user.data.categoriesIncome.size()];
        else sums=new double[user.data.categoriesOutlay.size()];
        for (int i = 0; i < sums.length; i++) sums[i] = 0;
        switch (period) {
            case 0:
                for (int j = 0; j < user.data.balanceActions.size(); j++) {
                    balanceAction action=user.data.balanceActions.get(j);

                    if (action.amount > 0 && action.date.getYear()==today.getYear() && action.date.getMonth()==today.getMonth() && action.date.getDate()==today.getDate() && type) {
                        int index = user.data.categoriesIncome.indexOf(action.category);
                        sums[index] += Math.abs(action.amount);
                    } else if (action.amount < 0 && action.date.getYear()==today.getYear() && action.date.getMonth()==today.getMonth() && action.date.getDate()==today.getDate() && !type) {
                        int index = user.data.categoriesOutlay.indexOf(action.category);
                        sums[index] += Math.abs(action.amount);
                    }
                }
                break;

            case 1:
                for (int j = 0; j < user.data.balanceActions.size(); j++) {
                    balanceAction action=user.data.balanceActions.get(j);

                    if (action.amount > 0 && action.date.getYear()==today.getYear() && action.date.getMonth()==today.getMonth() && type) {
                        int index = user.data.categoriesIncome.indexOf(action.category);
                        sums[index] += Math.abs(action.amount);
                    } else if (action.amount < 0 && action.date.getYear()==today.getYear() && action.date.getMonth()==today.getMonth() && !type) {
                        int index = user.data.categoriesOutlay.indexOf(action.category);
                        sums[index] += Math.abs(action.amount);
                    }
                }
                break;

            case 2:
                for (int j = 0; j < user.data.balanceActions.size(); j++) {
                    balanceAction action = user.data.balanceActions.get(j);

                    if (action.amount > 0 && action.date.getYear() == today.getYear() && type) {
                        int index = user.data.categoriesIncome.indexOf(action.category);
                        sums[index] += Math.abs(action.amount);
                    } else if (action.amount < 0 && action.date.getYear() == today.getYear() && !type) {
                        int index = user.data.categoriesOutlay.indexOf(action.category);
                        sums[index] += Math.abs(action.amount);
                    }
                }
                break;

            case 3:
                for (int j = 0; j < user.data.balanceActions.size(); j++) {
                    if (user.data.balanceActions.get(j).amount > 0 && type) {
                        int index = user.data.categoriesIncome.indexOf(user.data.balanceActions.get(j).category);
                        sums[index] += Math.abs(user.data.balanceActions.get(j).amount);
                    } else if (user.data.balanceActions.get(j).amount < 0 && !type) {
                        int index = user.data.categoriesOutlay.indexOf(user.data.balanceActions.get(j).category);
                        sums[index] += Math.abs(user.data.balanceActions.get(j).amount);
                    }
                }
                break;
        }

        String line="";

        if(type) line+="Доходи за ";
        else line+="Витрати за ";

        switch(period)
        {
            case 0: line+=Methods.formatDate(today); break;
            case 1: line+=(today.getMonth()+1)+"/"+(today.getYear()+1900); break;
            case 2: line+=(today.getYear()+1900); break;
            default: line+="весь час";
        }

        date.setText(line);

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        for(int i=0;i<sums.length; i++)
        {
            double value=sums[i];
            String label;
            if(type) label=user.data.categoriesIncome.get(i);
            else label=user.data.categoriesOutlay.get(i);

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

    protected Dialog onCreateDialog(int id)
    {
        if (id == 1) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, myYear, myMonth, myDay);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            today=new Date(year-1900,monthOfYear,dayOfMonth);
            setChart(spinner.getSelectedItemPosition() == 1, spinner2.getSelectedItemPosition());

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
