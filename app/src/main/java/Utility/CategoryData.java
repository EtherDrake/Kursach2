package Utility;

import android.support.annotation.NonNull;

/**
 * Created by admin on 21.03.2018.
 */

public class CategoryData implements Comparable {
    public String categoryName;
    public Double categoryAmount;

    public CategoryData(String name, Double amount)
    {
        categoryName=name;
        categoryAmount=amount;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        double compareAmount=((CategoryData)o).categoryAmount;
        return (int)(compareAmount - this.categoryAmount);
    }
}
