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
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Classes.Methods;
import Classes.ShoppingList;
import Classes.User;
import cz.msebera.android.httpclient.Header;

public class Inbox extends AppCompatActivity {

    User user;
    ArrayList<ShoppingList> inbox;

    ListView inboxView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user= Methods.load(this);
        inbox=new ArrayList<>();

        inboxView=findViewById(R.id.inboxView);

        AsyncHttpClient client = new AsyncHttpClient();
        String url="https://balance-rest.herokuapp.com/api/inbox/"+user.ID;

        client.get(url, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("InboxGet","JSON object");
                try {
                    String JSONOfLists = response.getString("data");
                    Gson gson = new Gson();
                    Type listType = new TypeToken< ArrayList<ShoppingList> >(){}.getType();
                    ArrayList<ShoppingList> data = gson.fromJson(JSONOfLists, listType);
                    if(data!=null) inbox=data;

                    List<String> listToShow=new ArrayList<>();

                    for(int i=0;i<inbox.size();i++)
                    {
                        listToShow.add(inbox.get(i).name+"("+inbox.get(i).products.size()+"):"+inbox.get(i).getTotalPrice()+"₴");
                    }

                    ArrayAdapter adapter = new ArrayAdapter(Inbox.this, android.R.layout.simple_list_item_1, listToShow);
                    inboxView.setAdapter(adapter);

                    inboxView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(Inbox.this, ListReview.class);
                            intent.putExtra("position", position);
                            intent.putExtra("inbox", inbox);
                            startActivityForResult(intent, 1);
                            finish();
                        }
                    });


                }catch (Exception ex){ex.printStackTrace();}

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("InboxGet", "JSON Array");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("InboxGet", "Fail");
            }

        });
    }

}
