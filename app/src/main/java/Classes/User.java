package Classes;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 29.12.2017.
 */

public class User implements Serializable {
    public String ID;
    public String email;
    public String password;

    public UserData data=new UserData();

    public User(String id, String email, String password, UserData data)
    {
        this.ID=id;
        this.email=email;
        this.password=password;
        this.data=data;
    }

    public User(String id, String email, String password)
    {
        this.ID=id;
        this.email=email;
        this.password=password;

        this.data.balanceActions=new ArrayList<>();
        this.data.shoppingLists=new ArrayList<>();

        this.data.categoriesOutlay=new ArrayList<>();
        this.data.categoriesIncome=new ArrayList<>();

        //"Їжа","Транспорт", "Одяг", "Різне"
        data.categoriesOutlay.add("Їжа");
        data.categoriesOutlay.add("Транспорт");
        data.categoriesOutlay.add("Одяг");
        data.categoriesOutlay.add("Різне");

        //"Зарплата","Аванс","Премія","Інше"
        data.categoriesIncome.add("Зарплата");
        data.categoriesIncome.add("Аванс");
        data.categoriesIncome.add("Премія");
        data.categoriesIncome.add("Інше");
    }

    public User(String email, String password)
    {
        this.email=email;
        this.password=password;

        this.data.balanceActions=new ArrayList<>();
        this.data.shoppingLists=new ArrayList<>();

        this.data.categoriesOutlay=new ArrayList<>();
        this.data.categoriesIncome=new ArrayList<>();

        //"Їжа","Транспорт", "Одяг", "Різне"
        data.categoriesOutlay.add("Їжа");
        data.categoriesOutlay.add("Транспорт");
        data.categoriesOutlay.add("Одяг");
        data.categoriesOutlay.add("Різне");

        //"Зарплата","Аванс","Премія","Інше"
        data.categoriesIncome.add("Зарплата");
        data.categoriesIncome.add("Аванс");
        data.categoriesIncome.add("Премія");
        data.categoriesIncome.add("Інше");
    }

    public User(){}

    public double getBalance()
    {
        double balance=0;
        for(int i=0;i<data.balanceActions.size();i++)
        {
            balance+=data.balanceActions.get(i).amount;
        }
        return balance;
    }

    public int getPlannedShoppings()
    {
        int count=0;
        for(int i=0;i<data.shoppingLists.size(); i++)
        {
            count+=data.shoppingLists.get(i).getActiveShoppings();
        }
        return count;
    }

    //-------------<OUTLAY CALCULATION>-------------
    public double getDailyOutlay()
    {
        double outlay=0;
        Date today=new Date();
        for(int i=0;i<data.balanceActions.size();i++)
        {
            balanceAction action=data.balanceActions.get(i);
            if(action.date.getYear()==today.getYear() && action.date.getMonth()==today.getMonth() && action.date.getDate()==today.getDate() && action.amount<0)
                outlay += Math.abs(action.amount);

        }
        return  outlay;
    }
    public double getMonthlyOutlay()
    {
        double outlay=0;
        Date today=new Date();
        for(int i=0;i<data.balanceActions.size();i++)
        {
            balanceAction action=data.balanceActions.get(i);
            if(action.date.getMonth()==today.getMonth() && action.date.getYear()==today.getYear() && action.amount<0)
                outlay += Math.abs(action.amount);

        }
        return  outlay;
    }
    public double getYearlyOutlay()
    {
        double outlay=0;
        for(int i=0;i<data.balanceActions.size();i++)
        {
            balanceAction action=data.balanceActions.get(i);
            if(action.date.getYear()==new Date().getYear() && action.amount<0)
                outlay+=Math.abs(action.amount);
        }
        return  outlay;
    }
    //-------------</OUTLAY CALCULATION>-------------

    //-------------<INCOME CALCULATION>-------------
    public double getDailyIncome()
    {
        double income=0;
        Date today=new Date();
        for(int i=0;i<data.balanceActions.size();i++)
        {
            balanceAction action=data.balanceActions.get(i);
            if(action.date.getYear()==today.getYear() && action.date.getMonth()==today.getMonth() && action.date.getDate()==today.getDate() && action.amount>0)
                income += action.amount;
                Log.d("incomeDateIssue", action.date.toString());

        }
        return  income;
    }
    public double getMonthlyIncome()
    {
        double income=0;
        Date today=new Date();
        for(int i=0;i<data.balanceActions.size();i++)
        {
            balanceAction action=data.balanceActions.get(i);
            if(action.date.getMonth()==today.getMonth() && action.date.getYear()==today.getYear() && action.amount>0)
                income+=action.amount;
        }
        return  income;
    }
    public double getYearlyIncome()
    {
        double income=0;
        for(int i=0;i<data.balanceActions.size();i++)
        {
            balanceAction action=data.balanceActions.get(i);
            if(action.date.getYear()==new Date().getYear() && action.amount>0)
                income+=action.amount;
        }
        return  income;
    }
    //-------------</INCOME CALCULATION>-------------

    //-------------<BALANCE CALCULATION>-------------
    public double getDailyBalance()
    {
        double balance=0;
        for(int i=0;i<data.balanceActions.size();i++)
        {
            balanceAction action=data.balanceActions.get(i);
            if(action.date.getYear()==new Date().getYear() && action.date.getMonth()==new Date().getMonth() && action.date.getDate()==new Date().getDate())
                balance+=action.amount;
        }
        return  balance;
    }
    public double getMonthlyBalance()
    {
        double balance=0;
        for(int i=0;i<data.balanceActions.size();i++)
        {
            balanceAction action=data.balanceActions.get(i);
            if(action.date.getMonth()==new Date().getMonth() && action.date.getYear()==new Date().getYear())
                balance+=action.amount;
        }
        return  balance;
    }
    public double getYearlyBalance()
    {
        double balance=0;
        for(int i=0;i<data.balanceActions.size();i++)
        {
            balanceAction action=data.balanceActions.get(i);
            if(action.date.getYear()==new Date().getYear())
                balance+=action.amount;
        }
        return  balance;
    }
    //-------------</BALANCE CALCULATION>-------------
}
