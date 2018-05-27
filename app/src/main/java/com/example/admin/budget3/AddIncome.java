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
        spinnerCATEGORY=findViewById(R.id.spinner2);
        SUM=findViewById(R.id.editText8);
        INFO=findViewById(R.id.editText10);
        OK_BUTTON=findViewById(R.id.button4);
        dateOutput=findViewById(R.id.textView33);
        changeDateButton=findViewById(R.id.button12);

        user= Methods.load(this);

        if(index==-1)incomeDate=new Date();
        else incomeDate=user.data.balanceActions.get(index).date;
        dateOutput.setText(Methods.formatDate(incomeDate));

        if(index!=-1)
        {
            SUM.setText(String.valueOf(user.data.balanceActions.get(index).amount));
            INFO.setText(user.data.balanceActions.get(index).info);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, user.data.categoriesIncome);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCATEGORY.setAdapter(adapter);
        spinnerCATEGORY.setSelection(0);

        OK_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balanceAction income=new balanceAction(spinnerCATEGORY.getSelectedItem().toString(),
                        incomeDate, Double.parseDouble(SUM.getText().toString()),
                        INFO.getText().toString());

                income.updatedAt=new Date();

                if(index==-1)user.data.balanceActions.add(income);
                else user.data.balanceActions.set(index, income);

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
            dateOutput.setText(dayOfMonth+"/"+Integer.valueOf(monthOfYear+1)+"/"+year);
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
