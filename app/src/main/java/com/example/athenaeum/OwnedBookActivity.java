/*
 * OwnedBookActivity
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * This Activity allows a user to view all of the books they own and filter them by status.
 * Selecting a book will open the book's information page.
 */
public class OwnedBookActivity extends AppCompatActivity {
    private TextView name;
    private ListView bookList;
    private Spinner mySpinner;
    private ArrayAdapter<Book> bookAdapter;
    private String[] status = {"All", "Available", "Accepted", "Requested", "Borrowed"};
    private final BookDB booksDB = new BookDB();
    private ArrayList<Book> bookDataList = new ArrayList<>();
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_ownedbooklist);

        // Retrieve profile for username.
        AthenaeumProfile profile = (AthenaeumProfile) getIntent().getExtras().getSerializable("profile");
        name = findViewById(R.id.headerLabel);
        name.setText(String.format("%s's Books", profile.getUsername()));
        // Retrieve uid.
        uid = (String) getIntent().getExtras().getSerializable("UID");
        // Retrieve book ISBNs.
        final ArrayList<String> books = (ArrayList<String>) getIntent().getExtras().getSerializable("ownedBooks");

        // Add books to the data list.
        for (String isbn : books) {
            Book myBook = booksDB.getBook(isbn);
            bookDataList.add(myBook);
        }

        // Initialize the ListView
        bookList = findViewById(R.id.myBookList);
        bookAdapter = new CustomBookList(this, bookDataList);
        bookList.setAdapter(bookAdapter);

        // Initialize the dropdown menu for the filter.
        mySpinner = findViewById(R.id.mySpinner);
        mySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, status));
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < status.length) {
                    getSelectedStatusData(position, bookDataList);
                } else {
                    Toast.makeText(OwnedBookActivity.this, "Selected category does not exist!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
            private void getSelectedStatusData(int statusID, ArrayList<Book> books) {
                // Create an array for the filtered books.
                ArrayList<Book> newBooks = new ArrayList<>();
                if (statusID == 0) {
                    // If the chosen filter was "All", simply use the full book list.
                    bookAdapter = new CustomBookList(OwnedBookActivity.this, books);
                } else {
                    // Otherwise, sort through the books and filter only the books that match the chosen status.
                    for (Book book : books) {
                        if (book.getStatus().equals(status[statusID])) {
                            newBooks.add(book);
                        }
                    }
                    bookAdapter = new CustomBookList(OwnedBookActivity.this, newBooks);
                }
                bookList.setAdapter(bookAdapter);
            }
        });

        // Set the listener that opens the book information if a book is clicked.
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) parent.getAdapter().getItem(position);
                Intent intent = new Intent(OwnedBookActivity.this, BookInfoActivity.class);
                intent.putExtra("BOOK", book);
                intent.putExtra("UID", uid);
                // Open the activity for a result so that the books can be updated if a change is made.
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Retrieve the result from opening a book information page.
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            // This means that a book was updated, so we should re-retrieve the books.
            UserDB users = new UserDB();
            bookDataList.clear();
            ArrayList<String> user_ISBNs = users.getUser(uid).getBooks();
            for (String isbn : user_ISBNs) {
                bookDataList.add(booksDB.getBook(isbn));
            }
            bookAdapter.notifyDataSetChanged();
        }
    }
}
