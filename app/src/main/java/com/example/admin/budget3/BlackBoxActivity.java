package com.example.admin.budget3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.bson.types.ObjectId;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import Classes.BlackBox;
import Classes.Methods;
import Classes.User;
import Classes.balanceAction;

public class BlackBoxActivity extends AppCompatActivity {

    TextView onAccount;
    Button put, take, setGoal;

    BlackBox box;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_box);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        onAccount=findViewById(R.id.textView52);
        put=findViewById(R.id.button20);
        take=findViewById(R.id.button19);
        setGoal=findViewById(R.id.button21);

        try
        {
            FileInputStream fis = openFileInput("blackbox");
            ObjectInputStream is = new ObjectInputStream(fis);
            try
            {
                box = (BlackBox) is.readObject();
            }catch (ClassNotFoundException e) { Log.d("BlackBoxSave",
                    "File not loaded ClassNotFound"); }
            is.close();
            fis.close();
            Log.d("BlackBoxSave","File loaded");
        }catch (IOException e){ box=new BlackBox(); box.save(BlackBoxActivity.this); }

        user=Methods.load(BlackBoxActivity.this);

        onAccount.setText(String.valueOf(box.getSum())+"₴ ");

        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(BlackBoxActivity.this);

                final View textEntryView = factory.inflate(R.layout.box_operations, null);

                final EditText Sum = textEntryView.findViewById(R.id.editText19);

                final AlertDialog.Builder alert = new AlertDialog.Builder(BlackBoxActivity.this);
                alert.setTitle(
                        "Додати кошти до чорної скриньки:").setView(
                        textEntryView).setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(!Objects.equals(Sum.getText().toString(), ""))
                                {
                                    box.setSum(box.getSum()+Double.valueOf(Sum.getText().toString()));
                                    box.save(BlackBoxActivity.this);
                                    balanceAction action =new balanceAction("Різне", new Date(), -1*Double.valueOf(Sum.getText().toString()), "Відкладення у чорну скриньку");
                                    user.data.balanceActions.add(action);
                                    Methods.save(user,BlackBoxActivity.this);
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            }
                        });
                alert.show();
            }
        });

        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(BlackBoxActivity.this);

                final View textEntryView = factory.inflate(R.layout.box_operations, null);

                final EditText Sum = textEntryView.findViewById(R.id.editText19);

                final AlertDialog.Builder alert = new AlertDialog.Builder(BlackBoxActivity.this);
                alert.setTitle(
                        "Зняти кошти з чорної скриньки:").setView(
                        textEntryView).setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(!Objects.equals(Sum.getText().toString(), ""))
                                {
                                    if(box.takeFromBox(Double.valueOf(Sum.getText().toString()))==1)
                                    {
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "В скриньці занадто мало коштів", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                    else
                                    {
                                        balanceAction action =new balanceAction("Інше", new Date(), Double.valueOf(Sum.getText().toString()), "Зняття коштів з чорної скриньки");
                                        user.data.balanceActions.add(action);
                                        Methods.save(user,BlackBoxActivity.this);
                                    }
                                    box.save(BlackBoxActivity.this);
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            }
                        });
                alert.show();
            }
        });

        setGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(BlackBoxActivity.this);

                final View textEntryView = factory.inflate(R.layout.box_operations, null);

                final EditText Sum = textEntryView.findViewById(R.id.editText19);

                final AlertDialog.Builder alert = new AlertDialog.Builder(BlackBoxActivity.this);
                alert.setTitle(
                        "Встановити суму для накопичення:").setView(
                        textEntryView).setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(!Objects.equals(Sum.getText().toString(), ""))
                                {
                                    box.setGoal(Double.valueOf(Sum.getText().toString()));
                                    box.save(BlackBoxActivity.this);
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            }
                        });
                alert.show();
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
