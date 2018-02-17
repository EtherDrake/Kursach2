package Classes;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by admin on 29.12.2017.
 */

public class balanceAction implements Serializable {
    public int id;
    public int userID;
    public String category;
    public Date date;
    public double amount;
    public String info;

    public balanceAction(String category, Date date, double price, String info)
    {
        this.category=category;
        this.date=date;
        this.amount=price;
        this.info=info;
    }
}
