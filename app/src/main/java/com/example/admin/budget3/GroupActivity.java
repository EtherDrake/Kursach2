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

public class GroupActivity extends AppCompatActivity {

    EditText memberId, memberNickname;
    TextView yourId;
    Button add;
    ListView groupView;

    BarcodeDetector detector;

    Group group;

    private static final int REQUEST_WRITE_PERMISSION = 20;
    private static final int PHOTO_REQUEST = 10;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    private Uri imageUri;

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

        Button scan=findViewById(R.id.button18);

        User user = Methods.load(this);
        yourId.setText("Ваш ID:"+user.ID);
        group = new Group(new ObjectId(user.ID));
        Group fromBase=new Group(new ObjectId(user.ID));
        fromBase.getGroup();
        group.load(this);
        if(fromBase.members.size()>group.members.size()) group=fromBase;
        else if(fromBase.members.size()==0)group.createGroup(); else group.updateGroup();
        //if(group.members.size()>0) Log.d("d",">0");

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

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(GroupActivity.this, new
                        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            }
        });

        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();
        if (!detector.isOperational()) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Could not set up the detector!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        refreshGroupView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    Toast.makeText(GroupActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "picture.jpg");
        imageUri = FileProvider.getUriForFile(GroupActivity.this,
                BuildConfig.APPLICATION_ID + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
            launchMediaScanIntent();
            try {
                Bitmap bitmap = decodeBitmapUri(this, imageUri);
                if (detector.isOperational() && bitmap != null) {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<Barcode> barcodes = detector.detect(frame);
                    for (int index = 0; index < barcodes.size(); index++) {
                        Barcode code = barcodes.valueAt(index);
                        //scanResults.setText(scanResults.getText() + code.displayValue + "\n");

                        //Required only if you need to extract the type of barcode
                        int type = barcodes.valueAt(index).valueFormat;
                        switch (type) {
                            case Barcode.CONTACT_INFO:
                                //Log.i(LOG_TAG, code.contactInfo.title);
                                break;
                            case Barcode.EMAIL:
                                //Log.i(LOG_TAG, code.email.address);
                                break;
                            case Barcode.ISBN:
                                //Log.i(LOG_TAG, code.rawValue);
                                break;
                            case Barcode.PHONE:
                                //Log.i(LOG_TAG, code.phone.number);
                                break;
                            case Barcode.PRODUCT:
                                //Log.i(LOG_TAG, code.rawValue);
                                break;
                            case Barcode.SMS:
                                //Log.i(LOG_TAG, code.sms.message);
                                break;
                            case Barcode.TEXT:
                                //Log.i(LOG_TAG, code.rawValue);
                                memberId.setText(code.rawValue);
                                break;
                            case Barcode.URL:
                                //Log.i(LOG_TAG, "url: " + code.url.url);
                                break;
                            case Barcode.WIFI:
                                //Log.i(LOG_TAG, code.wifi.ssid);
                                break;
                            case Barcode.GEO:
                                //Log.i(LOG_TAG, code.geoPoint.lat + ":" + code.geoPoint.lng);
                                break;
                            case Barcode.CALENDAR_EVENT:
                                //Log.i(LOG_TAG, code.calendarEvent.description);
                                break;
                            case Barcode.DRIVER_LICENSE:
                                //Log.i(LOG_TAG, code.driverLicense.licenseNumber);
                                break;
                            default:
                                //Log.i(LOG_TAG, code.rawValue);
                                break;
                        }
                    }
                    if (barcodes.size() == 0) {
                        //scanResults.setText("Scan Failed: Found nothing to scan");
                    }
                } else {
                    //scanResults.setText("Could not set up the detector!");
                }
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();
                //Log.e(LOG_TAG, e.toString());
            }
        }
    }

    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }

    public void refreshGroupView()
    {
        ArrayList<String> list=new ArrayList<>();
        for (Map.Entry<ObjectId, String> entry : group.members.entrySet())
        {
            list.add(entry.getValue()+":"+entry.getKey());
        }
        
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
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
    protected void onSaveInstanceState(Bundle outState) {
        if (imageUri != null) {
            outState.putString(SAVED_INSTANCE_URI, imageUri.toString());
            outState.putString(SAVED_INSTANCE_RESULT, memberId.getText().toString());
        }
        super.onSaveInstanceState(outState);
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
