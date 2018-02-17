package Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 29.12.2017.
 */

public class ShoppingList implements Serializable {
    public String name;
    public int userID;
    public List<Product> products;

    public ShoppingList()
    {
        products=new ArrayList<>();
    }

    public ShoppingList(String  name, List<Product> products)
    {
        this.name=name;
    }

    public ShoppingList(String  name, int user)
    {
        this.name=name;
    }

    public double getTotalPrice()
    {
        double total=0;
        for(int i=0;i<products.size();i++)
        {
            total+=products.get(i).price*=products.get(i).quantity;
        }
        return total;
    }

    public int getActiveShoppings()
    {
        int count=0;
        for(int i=0; i<products.size();i++)
        {
            if(!products.get(i).bought)count++;
        }
        return count;
    }
}
