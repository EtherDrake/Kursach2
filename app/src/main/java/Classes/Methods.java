package Classes;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

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
        User user=new User();
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

    public static String formatDate(Date date)
    {
        String value=date.getDate()+"/"+Integer.valueOf(date.getMonth()+1)+"/"+(date.getYear()+1900);
        return value;
    }
}
