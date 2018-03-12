package com.example.admin.budget3;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Classes.Methods;
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

        ArrayList<ShoppingList> list=user.data.shoppingLists;
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



        newList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(shoppingListsList.this, shoppingListActivity.class);
                startActivity(intent);
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
