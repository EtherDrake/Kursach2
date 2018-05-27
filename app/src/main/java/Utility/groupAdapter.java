package Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.budget3.R;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Map;

import Classes.Methods;
import Classes.balanceAction;

/**
 * Created by admin on 05.05.2018.
 */

public class groupAdapter extends BaseAdapter {
    Context context;
    Map<ObjectId, String> data;
    private static LayoutInflater inflater = null;

    public groupAdapter(Context context, Map<ObjectId, String> data) {
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
            vi = inflater.inflate(R.layout.groupitemlayout, null);

        TextView id = (TextView) vi.findViewById(R.id.textView48);
        TextView nickname = (TextView) vi.findViewById(R.id.textView47);

        //id.setText(data.get(data.keySet()).date);

        return vi;
    }
}
