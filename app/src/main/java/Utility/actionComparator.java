package Utility;

import java.util.Comparator;

import Classes.balanceAction;

/**
 * Created by admin on 19.04.2018.
 */

public class actionComparator implements Comparator<balanceAction> {
    @Override
    public int compare(balanceAction o1, balanceAction o2) {
        return o1.date.compareTo(o2.date);
    }
}
