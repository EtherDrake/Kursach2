package Classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 09.03.2018.
 */

public class UserData implements Serializable {

    public ArrayList<balanceAction> balanceActions;
    public ArrayList<ShoppingList> shoppingLists;
    public ArrayList<String> categoriesOutlay;
    public ArrayList<String> categoriesIncome;

}
