/*
 * ScanIsbn
 *
 * November 30 2020
 *
 * Copyright 2020 Natalie Iwaniuk, Harpreet Saini, Jack Gray, Jorge Marquez Peralta, Ramana Vasanthan, Sree Nidhi Thanneeru
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Referenced for Scanner
 * https://www.youtube.com/watch?v=wfucGSKngq4&fbclid=IwAR3UD6MPE9OoUdn8sec-sSAB_zGgpifjVCSImgHwsoTu1jBeGfyA-dfq7Tc
 * by E.A.Y Team
 * Published April 20, 2020
 */

package com.example.athenaeum;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.CaptureActivity;

/**
 * This is a class that contains methods for scanning an ISBN code and displaying a dialog with the retrieved code.
 */
public class ScanIsbn {
    /**
     * This function displays an alert dialog to the given activity
     * @param activity Contains the activity to display the dialog on.
     * @param context Contains the context of the activity.
     * @param result Contains the isbn code result.
     */
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

    /**
     * This function launches a camera to scan a code off of a book.
     * @param activity Contains the activity that initialized the scan.
     */
    public void scanCode(Activity activity) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setCaptureActivity(CaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code...");
        integrator.initiateScan();
    }
}
