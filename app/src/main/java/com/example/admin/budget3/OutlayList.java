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

import Utility.outlayAdapter;

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
            ArrayList<balanceAction> listToShow2=new ArrayList<>();
            final ArrayList<Integer> indexes=new ArrayList<>();
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
                indexes.clear();
                String category=getIntent().getStringExtra("cat");
                NumberFormat format = new DecimalFormat("##.##");
                for (int i = 0; i < user.balanceActions.size(); i++)
                {
                    if (Objects.equals(user.balanceActions.get(i).category, category)) {
                        balanceAction action=user.balanceActions.get(i);
                        listToShow.add(Methods.formatDate(action.date)+":"+user.balanceActions.get(i).info+"("+format.format(Math.abs(user.balanceActions.get(i).amount))+"₴)");
                        listToShow2.add(user.balanceActions.get(i));
                        indexes.add(i);
                    }
                }

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        LayoutInflater factory = LayoutInflater.from(OutlayList.this);

                        final View textEntryView = factory.inflate(R.layout.changeoutlay, null);

                        final EditText priceInput = textEntryView.findViewById(R.id.editText9);
                        final EditText infoInput = textEntryView.findViewById(R.id.editText15);
                        final Spinner spinner = textEntryView.findViewById(R.id.spinner3);

                        priceInput.setText(String.valueOf(Math.abs(list.get(indexes.get(position)).amount)));
                        infoInput.setText(list.get(indexes.get(position)).info);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(OutlayList.this,
                                android.R.layout.simple_spinner_item, user.categoriesOutlay);

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(user.categoriesOutlay.indexOf(list.get(indexes.get(position)).category));


                        final AlertDialog.Builder alert = new AlertDialog.Builder(OutlayList.this);
                        alert.setTitle(
                                "Редагувати").setView(
                                textEntryView).setPositiveButton("Зберегти",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        if(!Objects.equals(priceInput.getText().toString(), ""))
                                        {
                                            double price=-1*Double.valueOf(priceInput.getText().toString());
                                            String info=infoInput.getText().toString();

                                            balanceAction toChange=list.get(indexes.get(position));
                                            toChange.amount=price;
                                            toChange.info=info;
                                            toChange.category=user.categoriesOutlay.get(spinner.getSelectedItemPosition());

                                            list.set(indexes.get(position),toChange);
                                            Methods.save(user, OutlayList.this);
                                            refreshListView(1);
                                        }
                                    }
                                }).setNegativeButton("Видалити",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        list.remove((int)indexes.get(position));
                                        Methods.save(user, OutlayList.this);
                                        refreshListView(1);
                                    }
                                });
                        alert.show();
                        return true;
                    }
                });
            }

            else if(type==3)
            {
                indexes.clear();
                String category=getIntent().getStringExtra("cat");
                NumberFormat format = new DecimalFormat("##.##");
                for (int i = 0; i < user.balanceActions.size(); i++)
                {
                    if (Objects.equals(user.balanceActions.get(i).category, category)) {
                        balanceAction action=user.balanceActions.get(i);
                        listToShow.add(Methods.formatDate(action.date)+":"+user.balanceActions.get(i).info+"("+format.format(Math.abs(user.balanceActions.get(i).amount))+"₴)");
                        listToShow2.add(user.balanceActions.get(i));
                        indexes.add(i);
                        Log.d("asd", String.valueOf(i));
                    }
                }

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        LayoutInflater factory = LayoutInflater.from(OutlayList.this);

                        final View textEntryView = factory.inflate(R.layout.changeoutlay, null);

                        final EditText priceInput = textEntryView.findViewById(R.id.editText9);
                        final EditText infoInput = textEntryView.findViewById(R.id.editText15);
                        final Spinner spinner = textEntryView.findViewById(R.id.spinner3);

                        priceInput.setText(String.valueOf(list.get(indexes.get(position)).amount));
                        infoInput.setText(list.get(indexes.get(position)).info);

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(OutlayList.this,
                                android.R.layout.simple_spinner_item, user.categoriesIncome);

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(user.categoriesIncome.indexOf(list.get(indexes.get(position)).category));
                        //Log.d("asd", String.valueOf(list.get(indexes.get(position)).info));


                        final AlertDialog.Builder alert = new AlertDialog.Builder(OutlayList.this);
                        alert.setTitle(
                                "Редагувати").setView(
                                textEntryView).setPositiveButton("Зберегти",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        if(!Objects.equals(priceInput.getText().toString(), ""))
                                        {
                                            double price=Double.valueOf(priceInput.getText().toString());
                                            String info=infoInput.getText().toString();


                                            balanceAction toChange=list.get(indexes.get(position));
                                            toChange.amount=price;
                                            toChange.info=info;
                                            toChange.category=user.categoriesIncome.get(spinner.getSelectedItemPosition());

                                            list.set(indexes.get(position),toChange);
                                            Methods.save(user, OutlayList.this);
                                            refreshListView(1);
                                        }
                                    }
                                }).setNegativeButton("Видалити",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        list.remove((int)indexes.get(position));
                                        //user.balanceActions= (ArrayList<balanceAction>) list;
                                        Methods.save(user, OutlayList.this);
                                        refreshListView(1);
                                    }
                                });
                        alert.show();
                        return true;
                    }
                });
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listToShow);
            outlayAdapter adapter2=new outlayAdapter(this, listToShow2);
            if(type==0 || type==1) listView.setAdapter(adapter);
            else listView.setAdapter(adapter2);

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
