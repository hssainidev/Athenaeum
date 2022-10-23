/*
 * BorrowedBookActivity
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
 */

package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * This Activity allows a user to view all of the books they are currently borrowing.
 * Selecting a book will open the book's information page.
 */
public class BorrowedBookActivity extends AppCompatActivity {
    private final BookDB booksDB = new BookDB();

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_borrowedbooklist);

        // Retrieve uid.
        final String uid = getIntent().getExtras().getString("UID");
        // Retrieve profile for username.
        AthenaeumProfile profile = (AthenaeumProfile) getIntent().getExtras().getSerializable("profile");
        TextView name = findViewById(R.id.headerLabel);
        name.setText(String.format("Books Borrowed by %s", profile.getUsername()));

        // Retrieve the list of borrowed books from the database.
        ArrayList<Book> bookDataList = booksDB.getBorrowedBooks(uid);

        // Initialize the ListView for the books from the database.
        ListView bookList = findViewById(R.id.borrowedBookList);
        ArrayAdapter<Book> bookAdapter = new CustomBookList(this, bookDataList);
        bookList.setAdapter(bookAdapter);

        // Set the listener that opens the book information if a book is clicked.
        bookList.setOnItemClickListener((parent, view, position, id) -> {
            Book book = (Book) parent.getAdapter().getItem(position);
            Intent intent = new Intent(BorrowedBookActivity.this, BookInfoActivity.class);
            intent.putExtra("BOOK", book);
            intent.putExtra("UID", uid);
            startActivity(intent);
        });
    }
}
