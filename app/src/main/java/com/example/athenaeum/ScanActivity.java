package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ScanActivity extends AppCompatActivity {
    EditText isbn_edittext;
    ScanIsbn scanner = new ScanIsbn();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        final String uid = getIntent().getExtras().getString("UID");
        final Button confirm = findViewById(R.id.confirm_scan_button);
        final Button open_scanner = findViewById(R.id.open_scanner_button);
        isbn_edittext = findViewById(R.id.scan_isbn_editText);
        UserDB users = new UserDB();
        final User currentUser = users.getUser(uid);
        final BookDB booksDB = new BookDB();

        open_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanner.scanCode(ScanActivity.this);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isbn = isbn_edittext.getText().toString();
                if (isbn.length() == 0) {
                    isbn_edittext.setError("You must enter an ISBN.");
                }
                Book book = booksDB.getBook(isbn);
                if (book.getOwnerUID().equals(uid)) {
                    if (book.getStatus().equals("Accepted")) {
                        // Confirming book was given to borrower.
                        book.giveBook(uid);
                        Toast.makeText(ScanActivity.this, "You confirmed that the book \"" + book.getTitle() + "\" was given to the borrower!", Toast.LENGTH_LONG).show();
                        return;
                    } else if (book.getStatus().equals("Borrowed") && book.getBorrowerUID().equals(uid)) {
                        // Confirming book was received from borrower.
                        book.receiveReturn();
                        Toast.makeText(ScanActivity.this, "You confirmed that the book \"" + book.getTitle() + "\" was received from the borrower!", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else if (book.getBorrowerUID().equals(uid) && book.getStatus().equals("Borrowed")) {
                    // Returning book to owner.
                    book.returnBook(book.getOwnerUID());
                    Toast.makeText(ScanActivity.this, "You confirmed that the book \"" + book.getTitle() + "\" was returned to the owner!", Toast.LENGTH_LONG).show();
                    return;
                } else if (book.getRequesters().contains(uid) && book.getBorrowerUID().equals(book.getOwnerUID()) && book.getStatus().equals("Borrowed")) {
                    // Receiving a book from owner.
                    book.confirm(uid);
                    Toast.makeText(ScanActivity.this, "You confirmed that the book \"" + book.getTitle() + "\" was received from the owner!", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ScanActivity.this, "That book's status could not be changed!", Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            scanner.displayDialog(this, this, result.getContents());
            if (result.getContents() != null) {
                isbn_edittext.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
