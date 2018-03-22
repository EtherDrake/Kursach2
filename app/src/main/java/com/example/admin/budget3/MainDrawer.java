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
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;

import Classes.Methods;
import Classes.User;
import Classes.UserData;
import cz.msebera.android.httpclient.Header;


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

        openShoppingLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainDrawer.this, shoppingListsList.class);
                startActivity(intent);
            }
        });

        openBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainDrawer.this, balance.class);
                startActivity(intent);
            }
        });
        refreshMenu();
    }

    public void refreshMenu()
    {

        user=Methods.load(MainDrawer.this);
        Log.d("MainDrawerID",user.ID);

        DecimalFormat df=new DecimalFormat("0.00");

        dailyIncome.setText(df.format(user.getDailyIncome()));
        monthlyIncome.setText(df.format(user.getMonthlyIncome()));
        yearlyIncome.setText(df.format(user.getYearlyIncome()));

        dailyOutlay.setText(df.format(user.getDailyOutlay()));
        monthlyOutlay.setText(df.format(user.getMonthlyOutlay()));
        yearlyOutlay.setText(df.format(user.getYearlyOutlay()));

        dailyBalance.setText(df.format(user.getDailyBalance()));
        monthlyBalance.setText(df.format(user.getMonthlyBalance()));
        yearlyBalance.setText(df.format(user.getYearlyBalance()));

        toShop.setText(String.valueOf(user.getPlannedShoppings()));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        refreshMenu();
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
        int id = item.getItemId();
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
        else if (id == R.id.synchronise)
        {
            final User toSync= Methods.load(MainDrawer.this);
            Log.d("user",toSync.ID);

            final AsyncHttpClient client = new AsyncHttpClient();
            String url="https://balance-rest.herokuapp.com/api/users/"+toSync.ID;


            client.get(url, new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Log.d("connection",response.toString());

                        String id=response.getString("_id");
                        String Email=response.getString("email");
                        String Password=response.getString("password");

                        String rawData= response.getString("data");
                        Gson gson = new Gson();
                        UserData data = gson.fromJson(rawData, UserData.class);



                        User fromBase =new User(id,Email,Password,data);
                        User fusion=new User(toSync.ID,toSync.email,toSync.password);

                        Log.d("fromBaseId",id);
                        Log.d("fromBaseEmail",Email);
                        Log.d("fromBasePassword",Password);
                        Log.d("fromBaseRetrievedData",data.categoriesOutlay.get(0));

                        fusion.ID=toSync.ID;
                        fusion.email=toSync.email;
                        fusion.password=toSync.password;

                        fusion.data.balanceActions=Methods.fuseActions(toSync.data.balanceActions, fromBase.data.balanceActions);
                        fusion.data.shoppingLists=Methods.fuseLists(toSync.data.shoppingLists, fromBase.data.shoppingLists);
                        fusion.data.categoriesOutlay=Methods.fuseStringLists(toSync.data.categoriesOutlay, fromBase.data.categoriesOutlay);
                        fusion.data.categoriesIncome=Methods.fuseStringLists(toSync.data.categoriesIncome, fromBase.data.categoriesIncome);

                        Methods.updateUser(fusion);

                        Methods.save(fusion,MainDrawer.this);

                        refreshMenu();

                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Ви успішно синхронізували свої дані", Toast.LENGTH_SHORT);
                        toast.show();

                    } catch (Exception e) { e.printStackTrace(); }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                }
            });
         }
        else if (id == R.id.logOut)
        {
            deleteFile("User");
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
