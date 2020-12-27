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

        monthlyIncome=findViewById(R.id.textView5);
        yearlyIncome=findViewById(R.id.textView9);

        dailyOutlay=findViewById(R.id.textView14);
        monthlyOutlay=findViewById(R.id.textView12);
        yearlyOutlay=findViewById(R.id.textView16);

        monthlyBalance=findViewById(R.id.textView25);
        yearlyBalance=findViewById(R.id.textView24);

        toShop=findViewById(R.id.textView28);

        addIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, AddIncome.class);
                startActivity(intent);
            }
        });

        addOutlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, AddOutlay.class);
                startActivity(intent);
            }
        });


        openShoppingLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, shoppingListsList.class);
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------


        user=null;


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
