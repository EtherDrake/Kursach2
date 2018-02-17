package com.example.admin.budget3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Classes.User;

public class MainDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button addIncome, addOutlay, openBalance, openShoppingLists/*, addCategoryIncome,addCategoryOutlay*/;
    TextView dailyIncome, monthlyIncome, yearlyIncome, dailyOutlay, monthlyOutlay, yearlyOutlay,
            dailyBalance, monthlyBalance, yearlyBalance, toShop;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
                Intent intent = new Intent(MainDrawer.this, AddIncome.class);
                startActivity(intent);
            }
        });

        addOutlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainDrawer.this, AddOutlay.class);
                startActivity(intent);
            }
        });

        /*addCategoryOutlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainDrawer.this, categoriesList.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });

        addCategoryIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainDrawer.this, categoriesList.class);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
        });*/

        openShoppingLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainDrawer.this, shoppingListsList.class);
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------


        user=null;

        try
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
        }


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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.outlays)
        {
            Intent intent = new Intent(MainDrawer.this, OutlayList.class);
            intent.putExtra("type", 0);
            startActivity(intent);
        }
        else if (id == R.id.incomes)
        {
            Intent intent = new Intent(MainDrawer.this, OutlayList.class);
            intent.putExtra("type", 1);
            startActivity(intent);
        }
        else if (id == R.id.balance)
        {
            Intent intent = new Intent(MainDrawer.this, balance.class);
            startActivity(intent);
        }
        else if (id == R.id.shoppingLists)
        {
            Intent intent = new Intent(MainDrawer.this, shoppingListsList.class);
            startActivity(intent);
        }
        else if (id == R.id.incomeCategories)
        {
            Intent intent = new Intent(MainDrawer.this, categoriesList.class);
            intent.putExtra("type", 0);
            startActivity(intent);
        }
        else if (id == R.id.outlayCategories)
        {
            Intent intent = new Intent(MainDrawer.this, categoriesList.class);
            intent.putExtra("type", 1);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
