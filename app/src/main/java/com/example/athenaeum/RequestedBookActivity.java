package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RequestedBookActivity extends AppCompatActivity {
    TextView name;
    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    final BookDB booksDB = new BookDB();
    ArrayList<Book> bookDataList;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_requestedbooklist);
        final String uid = getIntent().getExtras().getString("UID");
        AthenaeumProfile profile = (AthenaeumProfile) getIntent().getExtras().getSerializable("profile");
        bookDataList = (ArrayList<Book>) booksDB.getRequestedBooks(uid);
        name = findViewById(R.id.headerLabel);
        name.setText(String.format("Books Requested by %s", profile.getUsername()));

        bookList = findViewById(R.id.requestedBookList);
        bookAdapter = new CustomBookList(this, bookDataList);
        bookList.setAdapter(bookAdapter);

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) parent.getAdapter().getItem(position);
                Intent intent = new Intent(RequestedBookActivity.this, BookInfoActivity.class);
                intent.putExtra("BOOK", book);
                intent.putExtra("UID", uid);
                startActivity(intent);
            }
        });

    }


}
