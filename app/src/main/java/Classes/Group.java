package Classes;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by admin on 28.04.2018.
 */

public class Group implements Serializable {
    public ObjectId _id;
    public LinkedHashMap<ObjectId, String> members;

    public Group (ObjectId ID)
    {
        this._id=ID;
        members=new LinkedHashMap<>();
    }

    public void createGroup()
    {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        Gson gson = new Gson();
        String data=gson.toJson(this.members);
        params.put("id", this._id);
        params.put("Data", data);

        String url="https://balance-rest.herokuapp.com/api/groups";

        client.post(url, params,  new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("connection","Success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d("connection","Fail");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public void updateGroup()
    {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        Gson gson = new Gson();
        String data=gson.toJson(this.members);
        params.put("Data", data);

        String url="https://balance-rest.herokuapp.com/api/groups/"+this._id.toString();

        client.put(url, params,  new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.d("connectionGroup","Success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d("connectionGroup","Fail");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public void getGroup()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        String url="https://balance-rest.herokuapp.com/api/groups/"+this._id.toString();
        //final User[] result = {new User()};



        client.get(url, new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Log.d("connection","JSON array");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    Log.d("connection",response.toString());
                    JSONObject userJSON = (JSONObject) response.get(0);

                    String rawData= userJSON.getString("data");
                    Gson gson = new Gson();
                    Type typeOfMap = new TypeToken<Map<String, ObjectId>>() { }.getType();
                    LinkedHashMap<ObjectId, String> data = gson.fromJson(rawData, typeOfMap);

                    Group.this.members=data;

                } catch (Exception e) { e.printStackTrace(); }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("fail","fail");
            }
        });

        Log.d("test","test");
    }

    public void save(Context ctx)
    {

        try {
            FileOutputStream fos = ctx.openFileOutput("Group", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
            Log.d("MyLogs","Group file saved");
        }catch (IOException e){Log.d("MyLogs","File not saved");}
    }

    public void load (Context ctx)
    {
        Group group=null;
        ObjectId tmp=this._id;
        try
        {
            FileInputStream fis = ctx.openFileInput("Group");
            ObjectInputStream is = new ObjectInputStream(fis);
            try
            {
                group = (Group) is.readObject();
            }catch (ClassNotFoundException e) { Log.d("MyLogs",
                    "File not loaded ClassNotFound"); }
            is.close();
            fis.close();
            Log.d("MyLogs","File loaded");
        }catch (IOException e){Log.d("MyLogs","File not loaded IOE");}

        if(group!=null)
        {
            this._id=group._id;
            this.members=group.members;
        }
        else
        {
            ObjectId ID=tmp;
            this._id=ID;
            this.members=new LinkedHashMap<>();
            this.save(ctx);
            Methods.postGroup(this);
        }
    }

    public String getByIndex(int i)
    {
        List<String> l = new ArrayList<String>(members.values());
        return l.get(i);
    }

}
