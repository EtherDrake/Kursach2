package Classes;

import java.io.Serializable;

/**
 * Created by admin on 29.12.2017.
 */

public class Product implements Serializable {
    public String Name;
    public double quantity;
    public double price;
    public String category;
    public boolean bought;

    public Product(String name)
    {
        this.Name=name;
        this.price=0;
        this.quantity=0;
    }
}
