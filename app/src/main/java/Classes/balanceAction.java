package Classes;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Created by admin on 29.12.2017.
 */

public class balanceAction implements Serializable {
    public String category;
    public Date date;
    public double amount;
    public String info;
    public Date updatedAt;

    public balanceAction(String category, Date date, double price, String info)
    {
        this.category=category;
        this.date=date;
        this.amount=price;
        this.info=info;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)return true;
        if (!(o instanceof balanceAction)) return false;
        balanceAction bal = (balanceAction) o;
        return Objects.equals(this.category, bal.category) && this.date == bal.date && this.amount == bal.amount && Objects.equals(this.info, bal.info);
    }

}
