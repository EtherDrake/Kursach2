package com.example.admin.budget3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Classes.Methods;
import Classes.Product;
import Classes.User;
import Classes.balanceAction;

public class OutlayList extends AppCompatActivity {

    ListView listView;
    User user;
    List<balanceAction> list;
    int period, type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outlay_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user= Methods.load(this);
        listView=findViewById(R.id.listView4);
        list=user.balanceActions;

        type=getIntent().getIntExtra("type",0);

        for(int i=0;i<user.categoriesOutlay.size();i++)
        {
            boolean condition;
        }

        refreshListView(1);

        /*ArrayList<String> listToShow=new ArrayList<>();
        for(int i=0;i<list.size();i++)
        {
            listToShow.add(list.get(i).amount+"₴ ("+(list.get(i).date.getYear()+1900)+"/"+(list.get(i).date.getMonth()+1)+"/"+list.get(i).date.getDate()+")");
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listToShow);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final balanceAction action=list.get(position);
                LayoutInflater factory = LayoutInflater.from(OutlayList.this);

                final View textEntryView = factory.inflate(R.layout.changeoutlay, null);

                final EditText infoInput = textEntryView.findViewById(R.id.editText15);
                final EditText amountInput = textEntryView.findViewById(R.id.editText9);
                final Spinner categoryInput = textEntryView.findViewById(R.id.spinner3);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(OutlayList.this,
                        android.R.layout.simple_spinner_item, user.categoriesOutlay);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                int pos=user.categoriesOutlay.indexOf(action.category);

                infoInput.setText(action.info);
                amountInput.setText(String.valueOf(action.amount));
                categoryInput.setAdapter(adapter);
                categoryInput.setSelection(pos);

                final AlertDialog.Builder alert = new AlertDialog.Builder(OutlayList.this);
                alert.setTitle(
                        "Enter the values:").setView(
                        textEntryView).setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Log.i("AlertDialog","TextEntry 1 Entered "+priceInput.getText().toString());
                                //Log.i("AlertDialog","TextEntry 2 Entered "+quantityInput.getText().toString());
                                if(!Objects.equals(infoInput.getText().toString(), "") && !Objects.equals(amountInput.getText().toString(), ""))
                                {
                                    String info=infoInput.getText().toString();
                                    double price=Double.valueOf(amountInput.getText().toString());
                                    String category=categoryInput.getSelectedItem().toString();

                                    action.info=info;
                                    action.amount=price;
                                    action.category=category;

                                    list.set(position,action);
                                    user.balanceActions.set(position,action);
                                    Methods.save(user, OutlayList.this);
                                    refreshListView();
                                }
                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                 *//*
                                 * User clicked cancel so do some stuff
                                 *//*
                            }
                        });
                alert.show();
                return true;
            }
        });*/
    }

    public void refreshListView()
    {
        listView.setAdapter(null);
        ArrayList<String> listToShow=new ArrayList<>();
        for(int i=0;i<list.size();i++)
        {
            listToShow.add(list.get(i).amount+"₴ ("+list.get(i).date+")");
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listToShow);
        listView.setAdapter(adapter);
    }

    public void refreshListView(int pType)
    {

            listView.setAdapter(null);
            ArrayList<String> listToShow = new ArrayList<>();
            double[] sums;
            if(type==0)
            {
                sums=new double[user.categoriesOutlay.size()];
                switch (pType) {
                    case 1: {
                        for (int i = 0; i < user.categoriesOutlay.size(); i++) sums[i] = 0;

                        for (int j = 0; j < user.balanceActions.size(); j++) {
                            if (user.balanceActions.get(j).amount < 0) {
                                int index = user.categoriesOutlay.indexOf(user.balanceActions.get(j).category);
                                sums[index] += Math.abs(user.balanceActions.get(j).amount);
                            }
                        }

                        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listToShow);
                        //listView.setAdapter(adapter);

                        break;
                    }

                    case 2: {
                        break;
                    }

                    case 3: {
                        break;
                    }
                }
                for (int i = 0; i < sums.length; i++)
                    listToShow.add(user.categoriesOutlay.get(i) + ":" + sums[i] + "₴");

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get the selected item text from ListView
                        Intent intent = new Intent(OutlayList.this, OutlayList.class);
                        intent.putExtra("type", 2);
                        intent.putExtra("cat", user.categoriesOutlay.get(position));
                        startActivity(intent);
                    }
                });


            }
            else if(type==1)
            {
                sums=new double[user.categoriesIncome.size()];
                switch (pType) {
                    case 1: {
                        for (int i = 0; i < user.categoriesIncome.size(); i++) sums[i] = 0;

                        for (int j = 0; j < user.balanceActions.size(); j++) {
                            if (user.balanceActions.get(j).amount > 0) {
                                int index = user.categoriesIncome.indexOf(user.balanceActions.get(j).category);
                                sums[index] += Math.abs(user.balanceActions.get(j).amount);
                            }
                        }

                        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listToShow);
                        listView.setAdapter(adapter);

                        break;
                    }

                    case 2: {
                        break;
                    }

                    case 3: {
                        break;
                    }
                }
                for (int i = 0; i < sums.length; i++)
                    listToShow.add(user.categoriesIncome.get(i) + ":" + sums[i] + "₴");

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Get the selected item text from ListView
                        Intent intent = new Intent(OutlayList.this, OutlayList.class);
                        intent.putExtra("type", 3);
                        intent.putExtra("cat", user.categoriesIncome.get(position));
                        startActivity(intent);
                    }
                });

            }
            else if(type==2)
            {
                String category=getIntent().getStringExtra("cat");
                NumberFormat format = new DecimalFormat("##.##");
                for (int i = 0; i < user.balanceActions.size(); i++)
                {
                    if (Objects.equals(user.balanceActions.get(i).category, category)) {
                        balanceAction action=user.balanceActions.get(i);
                        listToShow.add((action.date.getDate())+"/"+(action.date.getMonth()+1)+"/"+(action.date.getYear()+1900)+":"+user.balanceActions.get(i).info+"("+format.format(Math.abs(user.balanceActions.get(i).amount))+"₴)");
                    }
                }
            }

            else if(type==3)
            {
                String category=getIntent().getStringExtra("cat");
                for (int i = 0; i < user.balanceActions.size(); i++)
                {
                    if (Objects.equals(user.balanceActions.get(i).category, category)) {
                        balanceAction action=user.balanceActions.get(i);
                        listToShow.add((action.date.getDate())+"/"+(action.date.getMonth()+1)+"/"+(action.date.getYear()+1900)+":"+user.balanceActions.get(i).info+"("+user.balanceActions.get(i).amount+"₴)");
                    }
                }
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listToShow);
            listView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
