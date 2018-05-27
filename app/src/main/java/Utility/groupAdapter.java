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

import Classes.Group;
import Classes.Methods;
import Classes.balanceAction;

/**
 * Created by admin on 05.05.2018.
 */

public class groupAdapter extends BaseAdapter {
    Context context;
    //Map<ObjectId, String> data;
    Group data;
    private static LayoutInflater inflater = null;

    public groupAdapter(Context context, Group data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.members.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.getByIndex(position);
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
            vi = inflater.inflate(R.layout.group_member_layout, null);

        TextView id = (TextView) vi.findViewById(R.id.textView54);
        TextView nickname = (TextView) vi.findViewById(R.id.textView53);

        id.setText("ID:"+data.members.keySet().toArray()[position]);
        nickname.setText(data.getByIndex(position));

        return vi;
    }
}
