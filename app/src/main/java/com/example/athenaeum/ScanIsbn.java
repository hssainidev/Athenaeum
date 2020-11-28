package com.example.athenaeum;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.CaptureActivity;

public class ScanIsbn {

    public void displayDialog(final Activity activity, Context context, String result) {
        if (result != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(result);
            builder.setTitle("Scanning Result");
            builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    scanCode(activity);
                }
            }).setNegativeButton("Choose This ISBN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(context, "No Results", Toast.LENGTH_LONG).show();
        }
    }

    public void scanCode(Activity activity) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setCaptureActivity(CaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code...");
        integrator.initiateScan();
    }
}
