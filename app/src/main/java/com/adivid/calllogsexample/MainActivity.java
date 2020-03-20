package com.adivid.calllogsexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CallLogHelper callLogHelper;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callLogHelper = new CallLogHelper(this);
        checkPermissions();
        setUpList();
    }


    private void checkPermissions() {
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CALL_LOG)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            }
        } else{
            cursor = callLogHelper.getAllCallLogs(this.getContentResolver());
        }
    }


    private void setUpList() {
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String callNumber = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.NUMBER));
                String callName = cursor
                        .getString(cursor
                                .getColumnIndex(CallLog.Calls.CACHED_NAME));
                String callDate = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.DATE));
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "dd-MMM-yyyy HH:mm");
                String dateString = formatter.format(new Date(Long
                        .parseLong(callDate)));
                String callType = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.TYPE));
                String isCallNew = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.NEW));
                String duration = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.DURATION));

                Log.d(TAG, "getAllCallLogs:callNumber" + callNumber);
                Log.d(TAG, "getAllCallLogs:callName" + callName);
                Log.d(TAG, "getAllCallLogs:dateString" + dateString);
                Log.d(TAG, "getAllCallLogs:callType" + callType);
                Log.d(TAG, "getAllCallLogs:isCallNew" + isCallNew);
                Log.d(TAG, "getAllCallLogs:duration" + duration);

            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 1:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                        cursor = callLogHelper.getAllCallLogs(this.getContentResolver());
                    }else {
                        Toast.makeText(this, "No permission granted", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }
}
