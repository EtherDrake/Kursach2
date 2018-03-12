package Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by admin on 29.12.2017.
 */

public class ShoppingList implements Serializable {
    public String name;
    public List<Product> products;
    public Date updatedAt;

    public ShoppingList()
    {
        products=new ArrayList<>();
    }

    public double getTotalPrice()
    {
        double total=0;
        for(int i=0;i<products.size();i++)
            total+=products.get(i).price*=products.get(i).quantity;
        return total;
    }

    public int getActiveShoppings()
    {
        int count=0;
        for(int i=0; i<products.size();i++)
            if(!products.get(i).bought)count++;

        return count;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof ShoppingList)) return false;
        ShoppingList c = (ShoppingList) o;
        return Objects.equals(this.name, c.name);
    }
}
