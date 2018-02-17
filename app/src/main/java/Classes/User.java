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
    public int ID;
    public String email;
    public String password;

    public ArrayList<balanceAction> balanceActions;
    public ArrayList<ShoppingList> shoppingLists;
    public ArrayList<String> categoriesOutlay;
    public ArrayList<String> categoriesIncome;

    public User(int id, String email, String password)
    {
        this.ID=id;
        this.email=email;
        this.password=password;

        this.balanceActions=new ArrayList<>();
        this.shoppingLists=new ArrayList<>();
    }

    public User()
    {
        this.balanceActions=new ArrayList<>();
        this.shoppingLists=new ArrayList<>();
        this.categoriesOutlay=new ArrayList<>();
        this.categoriesIncome=new ArrayList<>();

        //"Їжа","Транспорт", "Одяг", "Різне"
        categoriesOutlay.add("Їжа");
        categoriesOutlay.add("Транспорт");
        categoriesOutlay.add("Одяг");
        categoriesOutlay.add("Різне");

        //"Зарплата","Аванс","Премія","Інше"
        categoriesIncome.add("Зарплата");
        categoriesIncome.add("Аванс");
        categoriesIncome.add("Премія");
        categoriesIncome.add("Інше");

    }

    public double getBalance()
    {
        double balance=0;
        for(int i=0;i<balanceActions.size();i++)
        {
            balance+=balanceActions.get(i).amount;
        }
        return balance;
    }

    public int getPlannedShoppings()
    {
        int count=0;
        for(int i=0;i<shoppingLists.size(); i++)
        {
            count+=shoppingLists.get(i).getActiveShoppings();
        }
        return count;
    }

    //-------------<OUTLAY CALCULATION>-------------
    public double getDailyOutlay()
    {
        double outlay=0;
        Date today=new Date();
        for(int i=0;i<balanceActions.size();i++)
        {
            balanceAction action=balanceActions.get(i);
            if(action.date.getYear()==today.getYear() && action.date.getMonth()==today.getMonth() && action.date.getDate()==today.getDate() && action.amount<0) {
                outlay += Math.abs(action.amount);
            }
        }
        return  outlay;
    }
    public double getMonthlyOutlay()
    {
        double outlay=0;
        Date today=new Date();
        for(int i=0;i<balanceActions.size();i++)
        {
            balanceAction action=balanceActions.get(i);
            if(action.date.getMonth()==today.getMonth() && action.date.getYear()==today.getYear() && action.amount<0) {
                outlay += Math.abs(action.amount);
            }
        }
        return  outlay;
    }
    public double getYearlyOutlay()
    {
        double outlay=0;
        for(int i=0;i<balanceActions.size();i++)
        {
            balanceAction action=balanceActions.get(i);
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
        for(int i=0;i<balanceActions.size();i++)
        {
            balanceAction action=balanceActions.get(i);
            if(action.date.getYear()==today.getYear() && action.date.getMonth()==today.getMonth() && action.date.getDate()==today.getDate() && action.amount>0) {
                income += action.amount;
                Log.d("incomeDateIssue", action.date.toString());
            }
        }
        return  income;
    }
    public double getMonthlyIncome()
    {
        double income=0;
        Date today=new Date();
        for(int i=0;i<balanceActions.size();i++)
        {
            balanceAction action=balanceActions.get(i);
            if(action.date.getMonth()==today.getMonth() && action.date.getYear()==today.getYear() && action.amount>0)
                income+=action.amount;
        }
        return  income;
    }
    public double getYearlyIncome()
    {
        double income=0;
        for(int i=0;i<balanceActions.size();i++)
        {
            balanceAction action=balanceActions.get(i);
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
        for(int i=0;i<balanceActions.size();i++)
        {
            balanceAction action=balanceActions.get(i);
            if(action.date.getYear()==new Date().getYear() && action.date.getMonth()==new Date().getMonth() && action.date.getDate()==new Date().getDate())
                balance+=action.amount;
        }
        return  balance;
    }
    public double getMonthlyBalance()
    {
        double balance=0;
        for(int i=0;i<balanceActions.size();i++)
        {
            balanceAction action=balanceActions.get(i);
            if(action.date.getMonth()==new Date().getMonth() && action.date.getYear()==new Date().getYear())
                balance+=action.amount;
        }
        return  balance;
    }
    public double getYearlyBalance()
    {
        double balance=0;
        for(int i=0;i<balanceActions.size();i++)
        {
            balanceAction action=balanceActions.get(i);
            if(action.date.getYear()==new Date().getYear())
                balance+=action.amount;
        }
        return  balance;
    }
    //-------------</BALANCE CALCULATION>-------------
}
