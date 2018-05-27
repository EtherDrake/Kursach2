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

        final User user = Methods.load(this);
        final Group group = new Group(new ObjectId(user.ID));
        group.load(this);
        final ArrayList<String> list = new ArrayList<>();
        final ArrayList<User> users = new ArrayList<>();

        final String category = getIntent().getStringExtra("category");

        for (final Map.Entry<ObjectId, String> entry : group.members.entrySet()) {
            AsyncHttpClient client = new AsyncHttpClient();
            String url = "https://balance-rest.herokuapp.com/api/users/" + entry.getKey().toHexString();
            Log.d("Connection", entry.getKey().toHexString());

            client.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    try {
                        Log.d("connection", response.toString());
                        JSONObject userJSON = (JSONObject) response;

                        String id = userJSON.getString("_id");
                        String Email = userJSON.getString("email");
                        String Password = userJSON.getString("password");

                        String rawData = userJSON.getString("data");
                        Gson gson = new Gson();
                        UserData data = gson.fromJson(rawData, UserData.class);

                        Log.d("id", id);
                        Log.d("Email", Email);
                        Log.d("Password", Password);
                        Log.d("retrievedData", data.categoriesOutlay.get(0));

                        User retrievedUser = new User(id, Email, Password, data);
                        DecimalFormat format = new DecimalFormat("##.##");
                        users.add(retrievedUser);

                        list.clear();

                        ArrayList<Double> sums=new ArrayList<>();
                        ArrayList<String> userlist=new ArrayList<>();

                        for (int i = 0; i < users.size(); i++) {
                            double sum=users.get(i).getOutlayByCategory(category);
                            sums.add(sum);
                            userlist.add(group.members.get(new ObjectId(users.get(i).ID)));
                            list.add(group.members.get(new ObjectId(users.get(i).ID)) + ":" + format.format(sum));
                        }

                        ArrayAdapter adapter = new ArrayAdapter(GroupOutlayCategory.this, android.R.layout.simple_list_item_1, list);
                        overallOutlays.setAdapter(adapter);

                        ArrayList<Entry> yvalues = new ArrayList<Entry>();
                        ArrayList<String> xVals = new ArrayList<String>();
                        for(int i=0;i<sums.size(); i++)
                        {
                            double value=sums.get(i);
                            String label = userlist.get(i);
                            if(value>0)
                            {
                                yvalues.add(new Entry((float) value, i));
                                xVals.add(label);
                            }
                        }

                        PieDataSet dataSet = new PieDataSet(yvalues, "");
                        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
                        PieData piedata = new PieData(xVals, dataSet);
                        //piedata.setValueFormatter(new PercentFormatter());
                        piedata.setValueTextSize(16f);
                        pieChart.setData(piedata);
                        pieChart.setDescription("");
                        pieChart.getLegend().setEnabled(false);
                        pieChart.invalidate();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("connection", "JSON object");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("connection Array", response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    //super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d("connection fail", "fail");
                }
            });
        }

    }

}
