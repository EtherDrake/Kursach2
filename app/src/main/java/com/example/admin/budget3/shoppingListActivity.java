package com.example.admin.budget3;

import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import Classes.Methods;
import Classes.Product;
import Classes.ShoppingList;
import Classes.User;
import Classes.balanceAction;

public class shoppingListActivity extends AppCompatActivity {

    ShoppingList list;
    int index;
    User user;
    ListView listView;

    Button okButton, saveButton;
    EditText textInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        okButton=findViewById(R.id.button3);
        saveButton=findViewById(R.id.button16);
        textInput=findViewById(R.id.editText6);

        user= Methods.load(this);

        index=getIntent().getIntExtra("index",-1);
        listView=findViewById(R.id.listView);
        Log.d("Index",String.valueOf(index));

        if(index==-1)
        {
            list=new ShoppingList();
            Date tmpDate=new Date();
            list.name=tmpDate.getDate()+"/"+(tmpDate.getMonth()+1)+"/"+(tmpDate.getYear()+1900);
        }
        else
        {
            list=user.shoppingLists.get(index);
            refreshListView();
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             String name=textInput.getText().toString();
             if(!Objects.equals(name, ""))
             {
                 Product product = new Product(name);
                 list.products.add(product);
                 refreshListView();
             }
             textInput.setText("");
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index==-1) user.shoppingLists.add(list);
                else
                {
                    boolean isCompleted=true;
                    for(int i=0;i<list.products.size();i++)
                    {
                        if(!list.products.get(i).bought)
                        {
                            isCompleted=false; break;
                        }
                    }
                    if(isCompleted)
                    {
                        for(int i=0;i<list.products.size();i++)
                        {
                            balanceAction outlay=new balanceAction(user.categoriesOutlay.get(3),new Date(),-1*list.products.get(i).price*list.products.get(i).quantity, list.products.get(i).Name);
                            user.balanceActions.add(outlay);
                        }
                        user.shoppingLists.remove(index);
                    }
                    else user.shoppingLists.set(index,list);
                }



                Methods.save(user,shoppingListActivity.this);

                Intent intent = new Intent(shoppingListActivity.this, MainDrawer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product pr=list.products.get(position);
                pr.bought=!pr.bought;
                list.products.set(position, pr);
                Methods.save(user, shoppingListActivity.this);
                refreshListView();
                //refreshListView();
                // Get the selected item text from ListView
                //Intent intent = new Intent(shoppingListsList.this, shoppingListActivity.class);
                //intent.putExtra("index", position);
                //startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater factory = LayoutInflater.from(shoppingListActivity.this);

                final View textEntryView = factory.inflate(R.layout.changeitemlayout, null);

                final EditText nameInput = textEntryView.findViewById(R.id.editText12);
                final EditText priceInput = textEntryView.findViewById(R.id.editText14);
                final EditText quantityInput = textEntryView.findViewById(R.id.editText13);

                nameInput.setText(String.valueOf(list.products.get(position).Name));
                priceInput.setText(String.valueOf(list.products.get(position).price));
                quantityInput.setText(String.valueOf((int)list.products.get(position).quantity));


                final AlertDialog.Builder alert = new AlertDialog.Builder(shoppingListActivity.this);
                alert.setTitle(
                        "Enter the values:").setView(
                        textEntryView).setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Log.i("AlertDialog","TextEntry 1 Entered "+priceInput.getText().toString());
                                Log.i("AlertDialog","TextEntry 2 Entered "+quantityInput.getText().toString());
                                if(!Objects.equals(priceInput.getText().toString(), "") && !Objects.equals(quantityInput.getText().toString(), ""))
                                {
                                    String name=nameInput.getText().toString();
                                    double price=Double.valueOf(priceInput.getText().toString());
                                    double quantity=Double.valueOf(quantityInput.getText().toString());

                                    Product toChange=list.products.get(position);
                                    toChange.quantity=quantity;
                                    toChange.price=price;
                                    toChange.Name=name;

                                    list.products.set(position,toChange);
                                    Methods.save(user, shoppingListActivity.this);
                                    refreshListView();
                                }
                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                 /*
                                 * User clicked cancel so do some stuff
                                 */
                            }
                        });
                alert.show();
                return true;
            }
        });
    }

    public void refreshListView()
    {
        listView.setAdapter(null);
        ArrayList<String> listToShow=new ArrayList<>();
        NumberFormat format = new DecimalFormat("##.###");
        for(int i=0;i<list.products.size();i++)
        {
            listToShow.add(list.products.get(i).Name+"("+list.products.get(i).price+") - "+format.format(list.products.get(i).quantity));
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, listToShow);
        listView.setAdapter(adapter);

        for(int i=0;i<list.products.size();i++)
        {
            listView.setItemChecked(i,list.products.get(i).bought);
        }
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