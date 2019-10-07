package com.niki.eorder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private DataPassing dataPassing = DataPassing.getInstance();
    private ZXingScannerView scannerView;
    private static int MY_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // disable action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_qr_code_scanner);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_REQUEST_CODE);
            }
        }


        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(com.google.zxing.Result result) {
        try{
            JSONObject object = new JSONObject(result.getText());
            String location = object.getString("location");
            String seatNumber = object.getString("seatNumber");

            Intent intent = new Intent(QrCodeScanner.this, StandList.class);
            dataPassing.setLocation(location);
            dataPassing.setSeatNumber(Integer.parseInt(seatNumber));
            startActivity(intent);
            finish();
        } catch (Exception e){
            Toast.makeText(QrCodeScanner.this, "Aw, Snap!, Your Qr Code is broken", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(QrCodeScanner.this, Dashboard.class);
            startActivity(intent);
            finish();
        }
    }
}
