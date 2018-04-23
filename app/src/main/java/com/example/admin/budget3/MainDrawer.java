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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import Classes.Methods;
import Classes.User;
import Classes.UserData;
import Classes.balanceAction;
import cz.msebera.android.httpclient.Header;


public class MainDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button addIncome, addOutlay, openBalance, openShoppingLists;
    TextView dailyIncome, monthlyIncome, yearlyIncome, dailyOutlay, monthlyOutlay, yearlyOutlay,
            dailyBalance, monthlyBalance, yearlyBalance, toShop, dailyIncomeLabel, dailyBalanceLabel;
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
        dailyIncomeLabel=findViewById(R.id.textView6);
        monthlyIncome=findViewById(R.id.textView5);
        yearlyIncome=findViewById(R.id.textView9);

        dailyOutlay=findViewById(R.id.textView14);
        monthlyOutlay=findViewById(R.id.textView12);
        yearlyOutlay=findViewById(R.id.textView16);

        dailyBalance=findViewById(R.id.textView26);
        dailyBalanceLabel=findViewById(R.id.textView19);
        monthlyBalance=findViewById(R.id.textView25);
        yearlyBalance=findViewById(R.id.textView24);

        dailyIncomeLabel.setVisibility(View.INVISIBLE);
        dailyBalanceLabel.setVisibility(View.INVISIBLE);
        dailyIncome.setVisibility(View.INVISIBLE);
        dailyBalance.setVisibility(View.INVISIBLE);

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
            //Log.d("user",toSync.ID);

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



                        User fromBase = new User(id,Email,Password,data);
                        User fusion=new User(toSync.ID,toSync.email,toSync.password);

                        //Log.d("fromBaseId",id);
                        //Log.d("fromBaseEmail",Email);
                        //Log.d("fromBasePassword",Password);
                        //Log.d("fromBaseRetrievedData",data.categoriesOutlay.get(0));

                        fusion.ID=toSync.ID;
                        fusion.email=toSync.email;
                        fusion.password=toSync.password;

                        File file = new File("User");
                        Date lastModDate = new Date(file.lastModified());
                        fusion.data.balanceActions=Methods.fuseActions(toSync.data.balanceActions, fromBase.data.balanceActions);
                        fusion.data.shoppingLists=Methods.fuseLists(toSync.data.shoppingLists, fromBase.data.shoppingLists);
                        fusion.data.categoriesOutlay=Methods.fuseStringLists(toSync.data.categoriesOutlay, fromBase.data.categoriesOutlay);
                        fusion.data.categoriesIncome=Methods.fuseStringLists(toSync.data.categoriesIncome, fromBase.data.categoriesIncome);

                        for(int i=0;i<fusion.data.balanceActions.size();i++)
                        {
                            if(fusion.data.balanceActions.get(i).amount<0 && !fusion.data.categoriesOutlay.contains(fusion.data.balanceActions.get(i).category)
                                    || fusion.data.balanceActions.get(i).amount>0 && !fusion.data.categoriesIncome.contains(fusion.data.balanceActions.get(i).category))
                                fusion.data.balanceActions.remove(i);
                        }

                        ArrayList<balanceAction> trashBin=Methods.loadTrashBin(MainDrawer.this);
                        Log.d("trashBinSize:", String.valueOf(trashBin.size()));
                        //fusion.data.balanceActions.removeAll(trashBin);
                        for(int i=0;i<fusion.data.balanceActions.size();i++)
                        {
                            balanceAction action= fusion.data.balanceActions.get(i);

                            for(int j=0;j<trashBin.size();j++)
                            {

                                balanceAction trash=trashBin.get(j);
                                //int index=fusion.data.balanceActions.indexOf(trash);
                                //Log.d("indexOfTrash", String.valueOf(index));
                                //try{fusion.data.balanceActions.remove(index);} catch (Exception e){}
                                //if(fusion.data.balanceActions.contains(trash))
                                //{
                                //    Log.d("isInFusion", "true");
                                //    fusion.data.balanceActions.remove(trash);
                                //}
                                if(action.amount==trash.amount && action.updatedAt.equals(trash.updatedAt) && action.date.equals(trash.date) && action.category.equals(trash.category) && action.info.equals(trash.info))
                                {
                                    fusion.data.balanceActions.remove(i);
                                    break;
                                }
                            }
                        }

                        Methods.updateUser(fusion);

                        Methods.save(fusion,MainDrawer.this);

                        trashBin.clear();
                        Methods.saveTrashBin(trashBin,MainDrawer.this);

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
