package Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.budget3.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by admin on 21.03.2018.
 */

public class categoryAdapter extends BaseAdapter {
    Context context;
    ArrayList<CategoryData> data;
    private static LayoutInflater inflater = null;

    public categoryAdapter(Context context, ArrayList<CategoryData> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.category_row, null);

        TextView name = (TextView) vi.findViewById(R.id.textView39);
        TextView amount = (TextView) vi.findViewById(R.id.textView40);
        DecimalFormat df=new DecimalFormat("0.00");

        name.setText(data.get(position).categoryName);
        amount.setText(df.format(data.get(position).categoryAmount)+"₴");
        return vi;
    }
}
