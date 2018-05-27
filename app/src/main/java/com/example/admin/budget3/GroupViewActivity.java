package com.example.admin.budget3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import Classes.Group;
import Classes.Methods;
import Classes.User;
import Classes.UserData;
import Utility.CategoryData;
import Utility.categoryAdapter;
import cz.msebera.android.httpclient.Header;

public class GroupViewActivity extends AppCompatActivity {

    ListView overallOutlays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //int type = getIntent().getIntExtra("type", 0);

        overallOutlays = findViewById(R.id.GroupListView);
        final User user = Methods.load(this);
        final Group group = new Group(new ObjectId(user.ID));
        group.load(this);
        final ArrayList<User> users = new ArrayList<>();
        users.add(user);

        HashSet<String> categoriesSet = new HashSet<>();
        for (int i = 0; i < users.size(); i++) {
            User current = users.get(i);
            categoriesSet.addAll(current.data.categoriesOutlay);
        }
        final ArrayList<String> categories = new ArrayList<>();
        categories.addAll(categoriesSet);
        final ArrayList<CategoryData> listToShow= new ArrayList<>();

        for (int i = 0; i < categories.size(); i++) {
            String category = categories.get(i);
            double sum = 0;
            for (int j = 0; j < users.size(); j++)
                sum += users.get(j).getOutlayByCategory(category);
            if (sum != 0) listToShow.add(new CategoryData(category, sum));//list.add(category + ":" + format.format(sum));
        }

        categoryAdapter adapter = new categoryAdapter(GroupViewActivity.this, listToShow);
        overallOutlays.setAdapter(adapter);
        overallOutlays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                Intent intent = new Intent(GroupViewActivity.this, GroupOutlayCategory.class);
                //intent.putExtra("type", 2);
                intent.putExtra("category", listToShow.get(position).categoryName);
                startActivity(intent);
            }
        });

        for (final Map.Entry<ObjectId, String> entry : group.members.entrySet()) {
                AsyncHttpClient client = new AsyncHttpClient();
                String url = "https://balance-rest.herokuapp.com/api/users/" + entry.getKey().toHexString();

                client.get(url, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // If the response is JSONObject instead of expected JSONArray
                        try {
                            Log.d("GroupViewGet", response.toString());
                            JSONObject userJSON = (JSONObject) response;

                            String id = userJSON.getString("_id");
                            String Email = userJSON.getString("email");
                            String Password = userJSON.getString("password");

                            String rawData = userJSON.getString("data");
                            Gson gson = new Gson();
                            UserData data = gson.fromJson(rawData, UserData.class);

                            User retrievedUser = new User(id, Email, Password, data);
                            DecimalFormat format = new DecimalFormat("##.##");
                            users.add(retrievedUser);
                            HashSet<String> categoriesSet = new HashSet<>();
                            for (int i = 0; i < users.size(); i++) {
                                User current = users.get(i);
                                categoriesSet.addAll(current.data.categoriesOutlay);
                            }
                            final ArrayList<String> categories = new ArrayList<>();
                            categories.addAll(categoriesSet);
                            final ArrayList<CategoryData> listToShow= new ArrayList<>();

                            for (int i = 0; i < categories.size(); i++) {
                                String category = categories.get(i);
                                double sum = 0;
                                for (int j = 0; j < users.size(); j++)
                                    sum += users.get(j).getOutlayByCategory(category);
                                if (sum != 0) listToShow.add(new CategoryData(category, sum));//list.add(category + ":" + format.format(sum));
                            }

                            categoryAdapter adapter = new categoryAdapter(GroupViewActivity.this, listToShow);
                            overallOutlays.setAdapter(adapter);
                            overallOutlays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // Get the selected item text from ListView
                                    Intent intent = new Intent(GroupViewActivity.this, GroupOutlayCategory.class);
                                    //intent.putExtra("type", 2);
                                    intent.putExtra("category", listToShow.get(position).categoryName);
                                    startActivity(intent);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d("GroupViewGet", "JSON object");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Log.d("GroupViewGet", "JSON array");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("GroupViewGet", "fail");
                    }
                });
        }
    }

}
