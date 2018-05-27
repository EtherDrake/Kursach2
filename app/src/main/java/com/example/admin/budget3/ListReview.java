package com.example.admin.budget3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;

import Classes.Methods;
import Classes.Product;
import Classes.ShoppingList;
import Classes.User;

public class ListReview extends AppCompatActivity {
    ShoppingList list;
    User user;
    ArrayList<ShoppingList> inbox;

    ListView listView;
    Button accept, decline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user= Methods.load(this);
        final int position=getIntent().getIntExtra("position", 0);
        inbox=(ArrayList<ShoppingList>)getIntent().getSerializableExtra("inbox");
        list=inbox.get(position);

        listView=findViewById(R.id.listDisplay);
        accept=findViewById(R.id.button23);
        decline=findViewById(R.id.button24);

        ArrayList<String> listToShow=new ArrayList<>();
        NumberFormat format = new DecimalFormat("##.###");
        for(int i=0;i<list.products.size();i++)
        {
            listToShow.add(list.products.get(i).Name+"("+list.products.get(i).price+"₴) - "+format.format(list.products.get(i).quantity));
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, listToShow);
        listView.setAdapter(adapter);

        for(int i=0;i<list.products.size();i++)
        {
            listView.setItemChecked(i,list.products.get(i).bought);
        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.data.shoppingLists.add(list);
                inbox.remove(position);
                Methods.updateShoppingList(inbox,user.ID);
                Methods.save(user, ListReview.this);
                finish();
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inbox.remove(position);
                Methods.updateShoppingList(inbox,user.ID);
                finish();
            }
        });

    }

}
