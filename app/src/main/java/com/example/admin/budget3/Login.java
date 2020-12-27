package com.example.admin.budget3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.DataAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Classes.Group;
import Classes.Methods;
import Classes.User;
import Classes.UserData;
import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {

    TextView changeWindow;
    EditText email, password;
    Button login;
    User user=null;
    Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user=Methods.load(Login.this);
        if(user!=null)
        {
            Intent intent = new Intent(Login.this, MainDrawer.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        changeWindow=findViewById(R.id.textView);
        email=findViewById(R.id.editText);
        password=findViewById(R.id.editText2);
        login=findViewById(R.id.button);

        changeWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncHttpClient client = new AsyncHttpClient();
                String url="https://kursatch-api.herokuapp.com/api/users";

                RequestParams params = new RequestParams();
                params.add("Email", email.getText().toString().trim());
                params.add("Password", password.getText().toString().trim());

                client.get(url, params, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {

                            String id=response.getString("_id");
                            String Email=response.getString("email");
                            String Password=response.getString("password");

                            String rawData= response.getString("data");
                            Gson gson = new Gson();
                            UserData data = gson.fromJson(rawData, UserData.class);

                            User retrievedUser =new User(id,email.getText().toString(),password.getText().toString(),data);
                            Methods.save(retrievedUser,Login.this);

                            group=new Group(new ObjectId(retrievedUser.ID));
                            group.save(Login.this);

                            Intent intent = new Intent(Login.this, MainDrawer.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Користувача не знайдено", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        try {
                            Log.d("LoginGet",response.toString());
                            JSONObject userJSON = (JSONObject) response.get(0);

                            String id=userJSON.getString("_id");
                            String Email=userJSON.getString("email");
                            String Password=userJSON.getString("password");

                            String rawData= userJSON.getString("data");
                            Gson gson = new Gson();
                            UserData data = gson.fromJson(rawData, UserData.class);

                            User retrievedUser =new User(id,email.getText().toString(),password.getText().toString(),data);
                            Methods.save(retrievedUser,Login.this);

                            group=new Group(new ObjectId(retrievedUser.ID));
                            group.save(Login.this);

                            Intent intent = new Intent(Login.this, MainDrawer.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Користувача не знайдено", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        //super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("LoginGet","fail");
                    }

                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
}
