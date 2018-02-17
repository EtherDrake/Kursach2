package com.example.admin.budget3;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import Classes.Methods;
import Classes.User;
import Classes.balanceAction;

public class AddIncome extends AppCompatActivity {

    EditText SUM, INFO;
    Spinner spinnerCATEGORY;
    TextView dateOutput;
    Button OK_BUTTON, changeDateButton;
    int index;
    Date incomeDate;
    User user;
    int myYear, myMonth, myDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        

        index=getIntent().getIntExtra("Index",-1);

        //CATEGORY=findViewById(R.id.editText7);
        spinnerCATEGORY=findViewById(R.id.spinner2);
        SUM=findViewById(R.id.editText8);
        INFO=findViewById(R.id.editText10);
        OK_BUTTON=findViewById(R.id.button4);
        dateOutput=findViewById(R.id.textView33);
        changeDateButton=findViewById(R.id.button12);

        user= Methods.load(this);

        if(index==-1)incomeDate=new Date();
        else incomeDate=user.balanceActions.get(index).date;
        dateOutput.setText(incomeDate.getYear()+1900+"/"+incomeDate.getMonth()+1+"/"+incomeDate.getDate());

        if(index!=-1)
        {
            //CATEGORY.setText(user.balanceActions.get(index).category);
            SUM.setText(String.valueOf(user.balanceActions.get(index).amount));
            INFO.setText(user.balanceActions.get(index).info);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, user.categoriesIncome);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCATEGORY.setAdapter(adapter);
        spinnerCATEGORY.setSelection(0);

        OK_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balanceAction income=new balanceAction(spinnerCATEGORY.getSelectedItem().toString(),
                        incomeDate, Double.parseDouble(SUM.getText().toString()),
                        INFO.getText().toString());

                if(index==-1)user.balanceActions.add(income);
                else user.balanceActions.set(index, income);

                Methods.save(user,AddIncome.this);

                Intent intent = new Intent(AddIncome.this, MainDrawer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        changeDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                myYear = date.getYear()+1900;
                myMonth = date.getMonth();
                myDay = date.getDay();
                Log.d("Date issue", String.valueOf(date.getYear()));
                showDialog(1);
            }
        });
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
            incomeDate=new Date(year-1900,monthOfYear,dayOfMonth);

            Log.d("Year",String.valueOf(incomeDate.getYear()));
            Log.d("Month",String.valueOf(incomeDate.getMonth()));
            Log.d("Day",String.valueOf(incomeDate.getDate()));
            dateOutput.setText(year+"/"+Integer.valueOf(monthOfYear+1)+"/"+dayOfMonth);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
