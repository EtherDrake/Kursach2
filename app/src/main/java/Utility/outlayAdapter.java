package Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.budget3.R;

import java.util.ArrayList;

import Classes.Methods;
import Classes.balanceAction;

/**
 * Created by admin on 24.02.2018.
 */

public class outlayAdapter extends BaseAdapter {

    Context context;
    ArrayList<balanceAction> data;
    private static LayoutInflater inflater = null;

    public outlayAdapter(Context context, ArrayList<balanceAction> data) {
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
            vi = inflater.inflate(R.layout.outlayrow, null);

        TextView date = (TextView) vi.findViewById(R.id.textView35);
        TextView info = (TextView) vi.findViewById(R.id.textView36);
        TextView price = (TextView) vi.findViewById(R.id.textView37);

        date.setText(Methods.formatDate(data.get(position).date));
        price.setText(String.valueOf(Math.abs(data.get(position).amount))+"₴");
        info.setText(data.get(position).info);
        return vi;
    }
}

