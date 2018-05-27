package Classes;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by admin on 22.05.2018.
 */

public class BlackBox implements Serializable {
    double onAccount;
    double Goal;

    public BlackBox()
    {
        onAccount=0;
        Goal=0;
    }

    public BlackBox(double sum, double goal)
    {
        onAccount=sum;
        Goal=goal;
    }

    public int takeFromBox(double sum)
    {
        if(onAccount-sum>=0)
        {
            onAccount-=sum;
            return 0;
        }
        else return 1;
    }

    public void setGoal(double sum)
    {
        Goal=sum;
    }
    public void setSum(double sum)
    {
        onAccount=sum;
    }
    public double getSum()
    {
        return onAccount;
    }
    public double getGoal()
    {
        return Goal;
    }

    public void save(Context ctx)
    {
        try {
            FileOutputStream fos = ctx.openFileOutput("blackbox", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
            Log.d("BlackBox","File saved");
        }catch (IOException e){Log.d("BlackBox","File not saved");}
    }
}
