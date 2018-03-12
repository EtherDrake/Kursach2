package com.example.admin.budget3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Classes.User;

public class MainMenuActivity extends AppCompatActivity {

    Button addIncome, addOutlay, openBalance, openShoppingLists/*, addCategoryIncome,addCategoryOutlay*/;
    TextView dailyIncome, monthlyIncome, yearlyIncome, dailyOutlay, monthlyOutlay, yearlyOutlay,
            dailyBalance, monthlyBalance, yearlyBalance, toShop;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        addIncome=findViewById(R.id.button5);
        addOutlay=findViewById(R.id.button6);
        openBalance=findViewById(R.id.button7);
        openShoppingLists=findViewById(R.id.button8);
        //addCategoryOutlay=findViewById(R.id.button13);
        //addCategoryIncome=findViewById(R.id.button14);

        dailyIncome=findViewById(R.id.textView7);
        monthlyIncome=findViewById(R.id.textView5);
        yearlyIncome=findViewById(R.id.textView9);

        dailyOutlay=findViewById(R.id.textView14);
        monthlyOutlay=findViewById(R.id.textView12);
        yearlyOutlay=findViewById(R.id.textView16);

        dailyBalance=findViewById(R.id.textView26);
        monthlyBalance=findViewById(R.id.textView25);
        yearlyBalance=findViewById(R.id.textView24);

        toShop=findViewById(R.id.textView28);

        addIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, AddIncome.class);
                //EditText editText = (EditText) findViewById(R.id.editText);
                //String message = editText.getText().toString();
                //intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });

        addOutlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, AddOutlay.class);
                //EditText editText = (EditText) findViewById(R.id.editText);
                //String message = editText.getText().toString();
                //intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });

        /*addCategoryOutlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, categoriesList.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });*/

        /*addCategoryIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, categoriesList.class);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        });*/

        openShoppingLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, shoppingListsList.class);
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------


        user=null;

        /*try
        {
            FileInputStream fis = openFileInput("User");
            ObjectInputStream is = new ObjectInputStream(fis);
            try
            {
                user = (User) is.readObject();
            }catch (ClassNotFoundException e) { Log.d("MyLogs",
                    "File not loaded ClassNotFound"); }
            is.close();
            fis.close();
            Log.d("MyLogs","File loaded");
        }catch (IOException e){Log.d("MyLogs","File not loaded IOE");}

        if(user==null)
        {
            user = new User();
            try {
                FileOutputStream fos = openFileOutput("User",
                        Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(user);
                os.close();
                fos.close();
                Log.d("MyLogs","File saved");
            }catch (IOException e){Log.d("MyLogs","File not saved");}
        }*/


        dailyIncome.setText(String.valueOf(user.getDailyIncome()));
        monthlyIncome.setText(String.valueOf(user.getMonthlyIncome()));
        yearlyIncome.setText(String.valueOf(user.getYearlyIncome()));

        dailyOutlay.setText(String.valueOf(user.getDailyOutlay()));
        monthlyOutlay.setText(String.valueOf(user.getMonthlyOutlay()));
        yearlyOutlay.setText(String.valueOf(user.getYearlyOutlay()));

        dailyBalance.setText(String.valueOf(user.getDailyBalance()));
        monthlyBalance.setText(String.valueOf(user.getMonthlyBalance()));
        yearlyBalance.setText(String.valueOf(user.getYearlyBalance()));

        toShop.setText(String.valueOf(user.getPlannedShoppings()));
    }
}
