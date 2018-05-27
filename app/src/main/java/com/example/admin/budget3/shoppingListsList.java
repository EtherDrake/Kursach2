package com.example.admin.budget3;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import Classes.Methods;
import Classes.Product;
import Classes.ShoppingList;
import Classes.User;

public class shoppingListsList extends AppCompatActivity {
    Button newList;
    ListView listView;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_shopping_lists_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        newList=findViewById(R.id.button15);
        listView=findViewById(R.id.listView3);

        user= Methods.load(this);

        refreshListView();

        newList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(shoppingListsList.this, shoppingListActivity.class);
                startActivity(intent);
            }
        });
    }

    public void refreshListView()
    {
        final ArrayList<ShoppingList> list=user.data.shoppingLists;
        List<String> listToShow=new ArrayList<>();

        for(int i=0;i<list.size();i++)
        {
            listToShow.add(list.get(i).name+"("+list.get(i).products.size()+"):"+list.get(i).getTotalPrice()+"₴");
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listToShow);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(shoppingListsList.this, shoppingListActivity.class);
                intent.putExtra("index", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater factory = LayoutInflater.from(shoppingListsList.this);

                final View textEntryView = factory.inflate(R.layout.change_category, null);

                final EditText nameInput = textEntryView.findViewById(R.id.editText16);

                nameInput.setText(String.valueOf(list.get(position).name));

                final AlertDialog.Builder alert = new AlertDialog.Builder(shoppingListsList.this);
                alert.setTitle(
                        "Введіть дані:").setView(
                        textEntryView).setPositiveButton("Зберегти",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(!Objects.equals(nameInput.getText().toString(), ""))
                                {
                                    String name=nameInput.getText().toString();

                                    ShoppingList toChange=list.get(position);
                                    toChange.name=name;
                                    toChange.updatedAt=new Date();

                                    list.set(position,toChange);
                                    //user.data.shoppingLists=list;
                                    Methods.save(user, shoppingListsList.this);
                                    refreshListView();
                                }
                            }
                        }).setNegativeButton("Видалити",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                list.remove(position);
                                Methods.save(user, shoppingListsList.this);
                                refreshListView();
                            }
                        });
                alert.show();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
