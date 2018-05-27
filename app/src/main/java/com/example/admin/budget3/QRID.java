package com.example.admin.budget3;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import Classes.Methods;
import Classes.User;

public class QRID extends AppCompatActivity {

    ImageView image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        image_view = findViewById(R.id.imageView2);
        User user = Methods.load(this);

        try {
            // generate a 150x150 QR code
            QRCodeWriter writer = new QRCodeWriter();

            BitMatrix bitMatrix = writer.encode(user.ID, BarcodeFormat.QR_CODE, 238, 238);
            int height = bitMatrix.getHeight();
            int width = bitMatrix.getWidth();
            Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bm.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            if (bm != null) {
                image_view.setImageBitmap(bm);
            }
        } catch (WriterException e) { //eek }

            /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });*/
        }

    }
}
