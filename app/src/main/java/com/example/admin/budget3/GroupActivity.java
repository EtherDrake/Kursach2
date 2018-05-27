package com.example.admin.budget3;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import Classes.Group;
import Classes.Methods;
import Classes.Product;
import Classes.User;
import Classes.balanceAction;
import Utility.CategoryData;
import Utility.groupAdapter;

public class GroupActivity extends AppCompatActivity {

    EditText memberId, memberNickname;
    TextView yourId;
    Button add;
    ListView groupView;

    Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        memberId=findViewById(R.id.editText17);
        memberNickname=findViewById(R.id.editText18);
        yourId=findViewById(R.id.textView44);
        add=findViewById(R.id.button17);
        groupView=findViewById(R.id.listView5);


        User user = Methods.load(this);
        yourId.setText("Ваш ID:"+user.ID);
        group = new Group(new ObjectId(user.ID));
        Group fromBase=new Group(new ObjectId(user.ID));
        fromBase.getGroup();
        group.load(this);
        if(fromBase.members.size()>group.members.size()) group=fromBase;
        else if(fromBase.members.size()==0)group.createGroup(); else group.updateGroup();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectId newId=new ObjectId(memberId.getText().toString());
                String nickname=memberNickname.getText().toString();

                group.members.put(newId, nickname);
                group.save(GroupActivity.this);
                group.updateGroup();
                refreshGroupView();
            }
        });


        refreshGroupView();
    }

    public void refreshGroupView()
    {

        groupAdapter adapter = new groupAdapter(this, group);
        groupView.setAdapter(adapter);

        groupView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater factory = LayoutInflater.from(GroupActivity.this);

                final View textEntryView = factory.inflate(R.layout.change_group_member_layout, null);

                final EditText Name = textEntryView.findViewById(R.id.editText20);

                Name.setText((String)Methods.getElementByIndex(group.members, position));


                final AlertDialog.Builder alert = new AlertDialog.Builder(GroupActivity.this);
                alert.setTitle(
                        "Введіть дані:").setView(
                        textEntryView).setPositiveButton("Зберегти",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(!Objects.equals(Name.getText().toString(), ""))
                                {
                                    group.members.put(new ObjectId(Methods.getKeyByIndex(group.members, position).toString()), Name.getText().toString());
                                    group.save(GroupActivity.this);
                                    refreshGroupView();
                                }
                            }
                        }).setNegativeButton("Видалити",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                group.members.remove(new ObjectId(Methods.getKeyByIndex(group.members, position).toString()));
                                group.save(GroupActivity.this);
                                refreshGroupView();
                            }
                        });
                alert.show();
                return true;
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
