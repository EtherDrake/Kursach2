package com.example.admin.budget3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import Classes.Methods;

public class Register extends AppCompatActivity {

    EditText email,password,confirm;
    TextView changeWindow;
    Button regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        changeWindow=findViewById(R.id.textView2);
        email=findViewById(R.id.editText3);
        password=findViewById(R.id.editText4);
        confirm=findViewById(R.id.editText5);
        regist=findViewById(R.id.button2);


        changeWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(password.getText().toString(), confirm.getText().toString())) {
                    Methods.register(email.getText().toString(), password.getText().toString());

                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Паролі не співпадають", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

}
