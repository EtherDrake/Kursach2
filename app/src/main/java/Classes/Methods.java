package Classes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.admin.budget3.Login;
import com.example.admin.budget3.MainDrawer;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.Driver;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

import static java.lang.Math.max;

/**
 * Created by admin on 03.02.2018.
 */

public class Methods {

    public static void save(User user, Context ctx)
    {
        try {
            FileOutputStream fos = ctx.openFileOutput("User", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(user);
            os.close();
            fos.close();
            Log.d("MyLogs","File saved");
        }catch (IOException e){Log.d("MyLogs","File not saved");}
    }

    public static User load (Context ctx)
    {

        User user=null;
        try
        {
            FileInputStream fis = ctx.openFileInput("User");
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
        return user;
    }

    public static void saveTrashBin(ArrayList<balanceAction> trashBin, Context ctx)
    {
        try {
            FileOutputStream fos = ctx.openFileOutput("trash", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(trashBin);
            os.close();
            fos.close();
            Log.d("MyLogs","File saved");
        }catch (IOException e){Log.d("MyLogs","File not saved");}
    }

    public static ArrayList<balanceAction> loadTrashBin (Context ctx)
    {

        ArrayList<balanceAction> trashBin=new ArrayList<>();
        try
        {
            FileInputStream fis = ctx.openFileInput("trash");
            ObjectInputStream is = new ObjectInputStream(fis);
            try
            {
                trashBin = (ArrayList<balanceAction>) is.readObject();
            }catch (ClassNotFoundException e) { Log.d("MyLogs",
                    "File not loaded ClassNotFound"); }
            is.close();
            fis.close();
            Log.d("MyLogs","File loaded");
        }catch (IOException e){Log.d("MyLogs","File not loaded IOE");}
        return trashBin;
    }

    public static void saveCategoryTrashBin(ArrayList<String> trashBin, Context ctx)
    {
        try {
            FileOutputStream fos = ctx.openFileOutput("catrash", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(trashBin);
            os.close();
            fos.close();
            Log.d("MyLogs","File saved");
        }catch (IOException e){Log.d("MyLogs","File not saved");}
    }

    public static ArrayList<String> loadCategoryTrashBin (Context ctx)
    {

        ArrayList<String> trashBin=new ArrayList<>();
        try
        {
            FileInputStream fis = ctx.openFileInput("catrash");
            ObjectInputStream is = new ObjectInputStream(fis);
            try
            {
                trashBin = (ArrayList<String>) is.readObject();
            }catch (ClassNotFoundException e) { Log.d("MyLogs",
                    "File not loaded ClassNotFound"); }
            is.close();
            fis.close();
            Log.d("MyLogs","File loaded");
        }catch (IOException e){Log.d("MyLogs","File not loaded IOE");}
        return trashBin;
    }

    public static String formatDate(Date date)
    {
        String value=date.getDate()+"/"+Integer.valueOf(date.getMonth()+1)+"/"+(date.getYear()+1900);
        return value;
    }


    static public RequestParams userToParams(User user)
    {
        RequestParams params = new RequestParams();
        params.put("Email", user.email.trim());
        params.put("Password", user.password.trim());

        Gson gson = new Gson();
        String data=gson.toJson(user.data);
        params.put("Data", data);

        Log.d("DataSent",data.toString());
        return params;
    }

    public static void register (String email, String password)
    {
        AsyncHttpClient client = new AsyncHttpClient();

        User user=new User(email, password);
        RequestParams params = userToParams(user);

        String url="https://balance-rest.herokuapp.com/api/users";

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

    public static void login(final String email, final String password, final Context ctx)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        String url="https://balance-rest.herokuapp.com/api/users";
        //final User[] result = {new User()};

        RequestParams params = new RequestParams();
        params.add("Email", email.trim());
        params.add("Password", password.trim());

        client.get(url, params, new JsonHttpResponseHandler()
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

                    String id=userJSON.getString("_id");
                    String Email=userJSON.getString("email");
                    String Password=userJSON.getString("password");

                    String rawData= userJSON.getString("data");
                    Gson gson = new Gson();
                    UserData data = gson.fromJson(rawData, UserData.class);

                    Log.d("id",id);
                    Log.d("Email",Email);
                    Log.d("Password",Password);
                    Log.d("retrievedData",data.categoriesOutlay.get(0));

                    User retrievedUser =new User(id,email,password,data);
                    save(retrievedUser,ctx);

                    //Intent intent = new Intent(ctx, MainDrawer.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

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

    public static void updateUser(User user)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = userToParams(user);
        String url="https://balance-rest.herokuapp.com/api/users/"+user.ID;
        client.put(url, params,  new AsyncHttpResponseHandler() {

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

    public static void updateShoppingList(ArrayList<ShoppingList> data, String id)
    {
        Log.d("sendShoppingLists", "Lists are going to be sent");
        AsyncHttpClient client = new AsyncHttpClient();
        String json = new Gson().toJson(data);
        RequestParams params = new RequestParams();
        params.put("data", json);

        String url="https://balance-rest.herokuapp.com/api/inbox/"+id;
        client.put(url, params,  new AsyncHttpResponseHandler() {

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
                Log.d("connection", e.getLocalizedMessage());
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public static void postShoppingList(ArrayList<ShoppingList> data, String id)
    {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        Gson gson = new Gson();
        String json=gson.toJson(data);
        params.put("id", id);
        params.put("data", json);

        String url="https://balance-rest.herokuapp.com/api/inbox";

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

    /*public static <T> ArrayList<T> fuse(ArrayList<T> list1, ArrayList<T> list2)
    {
        ArrayList<T> result=list1;
        result.removeAll(list2);
        result.addAll(list2);
        return result;
        //Set<T> newSet = new HashSet<>(list1);
        //newSet.addAll(list2);
        //return new ArrayList<>(newSet);
    }*/

    public static ArrayList<balanceAction> fuseActions (ArrayList<balanceAction> list1, ArrayList<balanceAction> list2)
    {
        int maxLen=max(list1.size(),list2.size());
        ArrayList<balanceAction> fusion=new ArrayList<>();
        for(int i=0; i<maxLen;i++)
        {
            balanceAction action1=null, action2=null;

            try{ action1=list1.get(i);}catch(Exception e){}
            try{ action2=list2.get(i);}catch(Exception e){}

            if(action1==null) {if(!fusion.contains(action2)) fusion.add(action2);}
            else if(action2==null) {if(!fusion.contains(action2)) fusion.add(action1);}
            else
            {
                if(action1.equals(action2))fusion.add(action1);
                else
                {
                    if(action1.updatedAt.before(action2.updatedAt))fusion.add(action2);
                    else fusion.add(action1);
                }
            }
        }
        return fusion;
    }

    public static ArrayList<ShoppingList> fuseLists (ArrayList<ShoppingList> list1, ArrayList<ShoppingList> list2)
    {
        int maxLen=max(list1.size(),list2.size());
        ArrayList<ShoppingList> fusion=new ArrayList<>();

        for(int i=0; i<maxLen;i++)
        {
            ShoppingList shoppingList1=null, shoppingList2=null;

            try{ shoppingList1=list1.get(i);}catch(Exception e){}
            try{ shoppingList2=list2.get(i);}catch(Exception e){}

            if(shoppingList1==null) fusion.add(shoppingList2);
            else if(shoppingList2==null) fusion.add(shoppingList1);
            else
            {
                if(shoppingList1.equals(shoppingList2))fusion.add(shoppingList1);
                else
                {
                    if(shoppingList1.updatedAt.before(shoppingList2.updatedAt))fusion.add(shoppingList2);
                    else fusion.add(shoppingList1);
                }
            }
        }
        return fusion;
    }

    public static ArrayList<String> fuseStringLists (ArrayList<String> list1, ArrayList<String> list2)
    {
        int maxLen=max(list1.size(),list2.size());
        ArrayList<String> fusion=new ArrayList<>();

        for(int i=0; i<maxLen;i++)
        {
            String category1=null, category2=null;

            try{ category1=list1.get(i); if(!fusion.contains(category1)) fusion.add(category1);}catch(Exception e){}
            try{ category2=list2.get(i); if(!fusion.contains(category2)) fusion.add(category2);}catch(Exception e){}

            //if(category1==null) fusion.add(category2);
            //else if(category2==null) fusion.add(category1);
            //else
            //{
                /*if(category1.equals(category2))fusion.add(category1);
                else
                {
                    fusion.add(category1);
                    fusion.add(category2);
                }*/

            //}
        }
        return fusion;
    }

    public static void postGroup(Group group)
    {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        Gson gson = new Gson();
        String data=gson.toJson(group.members);
        String id=group._id.toHexString();
        params.put("id", id);
        params.put("Data", data);

        String url="https://balance-rest.herokuapp.com/api/groups/";

        client.post(url, params,  new AsyncHttpResponseHandler() {

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

    public static Object getKeyByIndex(LinkedHashMap map, int index){
        return (map.keySet().toArray())[ index ];
    }

    public static Object getElementByIndex(LinkedHashMap map, int index){
        return map.get( (map.keySet().toArray())[ index ] );
    }

}
