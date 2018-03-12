package com.example.admin.budget3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import Classes.Methods;
import Classes.User;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
