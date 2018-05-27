package com.example.admin.budget3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import Classes.Group;
import Classes.Methods;
import Classes.User;
import Classes.UserData;
import Utility.CategoryData;
import Utility.categoryAdapter;
import cz.msebera.android.httpclient.Header;

public class GroupOutlayCategory extends AppCompatActivity {

    ListView overallOutlays;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_outlay_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        overallOutlays = findViewById(R.id.listView7);
        pieChart = findViewById(R.id.piechart2);
        final String category = getIntent().getStringExtra("category");

        final User user = Methods.load(this);
        final Group group = new Group(new ObjectId(user.ID));
        group.load(this);
        final ArrayList<User> users = new ArrayList<>();
        users.add(user);

        ArrayList<CategoryData> listToShow=new ArrayList<>();

        listToShow.add(new CategoryData("Я", user.getOutlayByCategory(category)));

        categoryAdapter adapter = new categoryAdapter(GroupOutlayCategory.this, listToShow);
        overallOutlays.setAdapter(adapter);

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        for(int i=0;i<listToShow.size(); i++)
        {
            double value=listToShow.get(i).categoryAmount;
            String label = listToShow.get(i).categoryName;
            if(value>0)
            {
                yvalues.add(new Entry((float) value, i));
                xVals.add(label);
            }
        }

        PieDataSet dataSet = new PieDataSet(yvalues, "");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        PieData piedata = new PieData(xVals, dataSet);
        piedata.setValueTextSize(16f);
        pieChart.setData(piedata);
        pieChart.setDescription("");
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate();



        for (final Map.Entry<ObjectId, String> entry : group.members.entrySet()) {
            AsyncHttpClient client = new AsyncHttpClient();
            String url = "https://balance-rest.herokuapp.com/api/users/" + entry.getKey().toHexString();

            client.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    try {
                        Log.d("GroupCategoryGet", response.toString());
                        JSONObject userJSON = (JSONObject) response;

                        String id = userJSON.getString("_id");
                        String Email = userJSON.getString("email");
                        String Password = userJSON.getString("password");

                        String rawData = userJSON.getString("data");
                        Gson gson = new Gson();
                        UserData data = gson.fromJson(rawData, UserData.class);


                        User retrievedUser = new User(id, Email, Password, data);
                        users.add(retrievedUser);

                        ArrayList<CategoryData> listToShow=new ArrayList<>();
                        listToShow.add(new CategoryData("Я", user.getOutlayByCategory(category)));

                        for (int i = 1; i < users.size(); i++) {
                            double sum=users.get(i).getOutlayByCategory(category);
                            String nickname;
                            listToShow.add(new CategoryData(group.members.get(new ObjectId(users.get(i).ID)), sum));
                        }

                        categoryAdapter adapter = new categoryAdapter(GroupOutlayCategory.this, listToShow);
                        overallOutlays.setAdapter(adapter);

                        ArrayList<Entry> yvalues = new ArrayList<Entry>();
                        ArrayList<String> xVals = new ArrayList<String>();
                        for(int i=0;i<listToShow.size(); i++)
                        {
                            double value=listToShow.get(i).categoryAmount;
                            String label = listToShow.get(i).categoryName;
                            if(value>0)
                            {
                                yvalues.add(new Entry((float) value, i));
                                xVals.add(label);
                            }
                        }

                        PieDataSet dataSet = new PieDataSet(yvalues, "");
                        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                        PieData piedata = new PieData(xVals, dataSet);
                        piedata.setValueTextSize(16f);
                        pieChart.setData(piedata);
                        pieChart.setDescription("");
                        pieChart.getLegend().setEnabled(false);
                        pieChart.invalidate();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("GroupCategoryGet", "JSON object");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("GroupCategoryGet", "JSON array");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    //super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("GroupCategoryGet", "fail");
                }
            });
        }

    }

}
