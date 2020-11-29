package com.example.athenaeum;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BorrowedBookActivity extends AppCompatActivity {

    TextView name;
    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    final BookDB booksDB = new BookDB();
    ArrayList<Book> bookDataList;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_borrowedbooklist);
        final String uid=getIntent().getExtras().getString("UID");
        AthenaeumProfile profile = (AthenaeumProfile) getIntent().getExtras().getSerializable("profile");
        bookDataList = (ArrayList<Book>) booksDB.getBorrowedBooks(uid);
        name = findViewById(R.id.headerLabel);
        name.setText(String.format("Books Borrowed by %s", profile.getUsername()));

        bookList = findViewById(R.id.borrowedBookList);

        bookAdapter = new CustomBookList(this, bookDataList);
        bookList.setAdapter(bookAdapter);

    }
}
