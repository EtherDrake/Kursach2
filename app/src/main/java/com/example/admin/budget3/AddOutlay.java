package com.example.admin.budget3;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;

import Classes.Methods;
import Classes.User;
import Classes.balanceAction;

public class AddOutlay extends AppCompatActivity {

    Button OK_BUTTON, changeDateButton, testButton;
    Date outlayDate;
    String name, info;
    double sum;

    EditText SUM, INFO;
    Spinner spinnerCATEGORY;
    TextView dateOutput;
    int myYear, myMonth, myDay, index;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_outlay);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        OK_BUTTON = findViewById(R.id.button9);
        changeDateButton = findViewById(R.id.button10);
        //testButton=findViewById(R.id.button13);

        SUM = findViewById(R.id.editText11);
        INFO = findViewById(R.id.editText12);
        spinnerCATEGORY=findViewById(R.id.spinner);

        dateOutput = findViewById(R.id.textView22);

        index=getIntent().getIntExtra("Index",-1);

        user= Methods.load(this);

        if(index==-1)outlayDate=new Date();
        else outlayDate=user.balanceActions.get(index).date;
        dateOutput.setText(outlayDate.getYear()+1900+"/"+outlayDate.getMonth()+1+"/"+outlayDate.getDate());


        if(index!=-1)
        {
            SUM.setText(String.valueOf(user.balanceActions.get(index).amount));
            INFO.setText(user.balanceActions.get(index).info);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, user.categoriesOutlay);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCATEGORY.setAdapter(adapter);
        spinnerCATEGORY.setSelection(0);
        //spinnerCATEGORY.getSelectedItem().toString();

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

        OK_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balanceAction outlay=new balanceAction(spinnerCATEGORY.getSelectedItem().toString(),
                        outlayDate, -1*Double.parseDouble(SUM.getText().toString()),
                        INFO.getText().toString());

                if(index==-1)user.balanceActions.add(outlay);
                else user.balanceActions.set(index, outlay);

                Log.d("Date",String.valueOf(outlay.date));

                Methods.save(user,AddOutlay.this);


                Intent intent = new Intent(AddOutlay.this, MainDrawer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
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
            outlayDate=new Date(year-1900,monthOfYear,dayOfMonth);
            //outlayDate=new Date();
            Log.d("Year",String.valueOf(outlayDate.getYear()));
            Log.d("Month",String.valueOf(outlayDate.getMonth()));
            Log.d("Day",String.valueOf(outlayDate.getDate()));
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
