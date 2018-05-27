package com.example.admin.budget3;

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

import java.util.ArrayList;
import java.util.Objects;

import Classes.Methods;
import Classes.User;
import Classes.balanceAction;

public class categoriesList extends AppCompatActivity {

    int type;
    EditText inputField;
    Button okButton;
    ListView listView;
    User user;
    ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_categories_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        type=getIntent().getIntExtra("type",0);

        inputField=findViewById(R.id.editText7);
        okButton=findViewById(R.id.button11);
        listView=findViewById(R.id.listView2);

        user= Methods.load(this);

        if(type==0) list=user.data.categoriesIncome;
        else list=user.data.categoriesOutlay;

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater factory = LayoutInflater.from(categoriesList.this);

                final View textEntryView = factory.inflate(R.layout.change_category, null);

                final EditText nameInput = textEntryView.findViewById(R.id.editText16);

                nameInput.setText(list.get(position));

                final AlertDialog.Builder alert = new AlertDialog.Builder(categoriesList.this);
                alert.setTitle(
                        "Редагувати").setView(
                        textEntryView).setPositiveButton("Зберегти",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(!Objects.equals(nameInput.getText().toString(), ""))
                                {
                                    String name=nameInput.getText().toString();
                                    String oldName=list.get(position);

                                    if(type==0)user.data.categoriesIncome.set(position,name);
                                    else user.data.categoriesOutlay.set(position,name);

                                    for(int i=0;i<user.data.balanceActions.size();i++)
                                        if(Objects.equals(user.data.balanceActions.get(i).category, oldName))
                                        {
                                            balanceAction toChange=user.data.balanceActions.get(i);
                                            toChange.category=name;
                                            user.data.balanceActions.set(i,toChange);
                                        }
                                    Methods.save(user, categoriesList.this);
                                    refreshList();
                                }
                            }
                        }).setNegativeButton("Видалити",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ArrayList<String> categoryTrashBin=Methods.loadCategoryTrashBin(categoriesList.this);
                                if(type==0) categoryTrashBin.add(user.data.categoriesIncome.get(position));
                                else categoryTrashBin.add(user.data.categoriesOutlay.get(position));
                                Methods.saveCategoryTrashBin(categoryTrashBin,categoriesList.this);
                                if(type==0) user.data.categoriesIncome.remove(position);
                                else user.data.categoriesOutlay.remove(position);
                                Methods.save(user, categoriesList.this);
                                refreshList();
                            }
                        });
                alert.show();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputField.getText().toString().trim().length() > 0)
                {
                    String category=inputField.getText().toString();

                    if (type == 0)user.data.categoriesIncome.add(category);
                    else user.data.categoriesOutlay.add(category);

                    Methods.save(user,categoriesList.this);

                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            }
        });
    }

    public void refreshList()
    {
        if(type==0) list=user.data.categoriesIncome;
        else list=user.data.categoriesOutlay;

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
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
