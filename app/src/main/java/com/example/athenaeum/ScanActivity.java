/*
 * ScanActivity
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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * This Activity allows a user to scan the ISBN off of a book.
 * If the ISBN matches one that the user owns, it will change the status if the
 * book is accepted or if the book has been returned.
 * If the ISBN matches one that the user requested or borrowed,
 * if the book is accepted or if the book is borrowed it will change the status.
 */
public class ScanActivity extends AppCompatActivity {
    EditText isbn_edittext;
    ScanIsbn scanner = new ScanIsbn();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // Retrieve the uid
        final String uid = getIntent().getExtras().getString("UID");

        // Initialize the buttons and EditText
        final Button confirm = findViewById(R.id.confirm_scan_button);
        final Button open_scanner = findViewById(R.id.open_scanner_button);
        isbn_edittext = findViewById(R.id.scan_isbn_editText);

        // Initialize the databases.
        UserDB users = new UserDB();
        final User currentUser = users.getUser(uid);
        final BookDB booksDB = new BookDB();

        // Set the listener for the scanner button to call ScanISBN scanCode when clicked.
        open_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanner.scanCode(ScanActivity.this);
            }
        });

        // Initialize the listener for the confirm button.
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the ISBN and check to make sure that an ISBN was entered.
                String isbn = isbn_edittext.getText().toString();
                if (isbn.length() == 0) {
                    isbn_edittext.setError("You must enter an ISBN.");
                }
                // Retrieve the book.
                Book book = booksDB.getBook(isbn);
                if (book == null) {
                    Toast.makeText(ScanActivity.this, "That book could not be found.", Toast.LENGTH_LONG).show();
                } else if (book.getOwnerUID().equals(uid)) {
                    // If the user owns the book, check the two potential states.
                    if (book.getStatus().equals("Accepted")) {
                        // Confirming book was given to borrower.
                        booksDB.giveBookUpdate(book, uid);
                        Toast.makeText(ScanActivity.this, "You confirmed that the book \"" + book.getTitle() + "\" was given to the borrower!", Toast.LENGTH_LONG).show();
                        return;
                    } else if (book.getStatus().equals("Available") && !book.getBorrowerUID().equals(uid)) {
                        // Confirming book was received from borrower.
                        booksDB.confirmReturn(book);
                        Toast.makeText(ScanActivity.this, "You confirmed that the book \"" + book.getTitle() + "\" was received from the borrower!", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else if (book.getBorrowerUID().equals(uid) && book.getStatus().equals("Borrowed")) {
                    // Returning book to owner.
                    booksDB.returnToOwner(book);
                    Toast.makeText(ScanActivity.this, "You confirmed that the book \"" + book.getTitle() + "\" was returned to the owner!", Toast.LENGTH_LONG).show();
                    return;
                } else if (book.getRequesters().contains(uid) && book.getBorrowerUID().equals(book.getOwnerUID()) && book.getStatus().equals("Borrowed")) {
                    // Receiving a book from owner.
                    booksDB.confirmBorrow(book, uid);
                    Toast.makeText(ScanActivity.this, "You confirmed that the book \"" + book.getTitle() + "\" was received from the owner!", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ScanActivity.this, "That book's status could not be changed!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * This function retrieves the result from the scanner activity and sets the ISBN to the retrieved value.
     * @param requestCode Contains the request code sent to the other activity.
     * @param resultCode Contains the result code from the other activity, if applicable.
     * @param data Contains the data from the other activity.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            // Display the scanner dialog to confirm the ISBN or scan again.
            scanner.displayDialog(this, this, result.getContents());

            // Set the ISBN EditText to the scanned ISBN.
            if (result.getContents() != null) {
                isbn_edittext.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
