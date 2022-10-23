/*
 * NotificationActivity
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

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * This Activity looks at the user's books and displays notifications
 * based on how many of their books are requested and how many books
 * they've requested are accepted.
 */
@SuppressWarnings("unchecked")
public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "Hello! ";
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_notification);
        linearLayout = findViewById(R.id.notification_activity);

        // Retrieve the uid and initialize the book lists and book and user databases.
        final String uid = getIntent().getExtras().getString("UID");
        final ArrayList<String> books = (ArrayList<String>) getIntent().getExtras().getSerializable("ownedBooks");
        final ArrayList<Book> acceptedBooks = (ArrayList<Book>) getIntent().getExtras().getSerializable("acceptedBooks");
        final UserDB users = new UserDB();
        final BookDB booksDB = new BookDB();
        final User currentUser = users.getUser(uid);

        // Look through all of the user's own books.
        for (String book : books) {
            final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Books").document(book);
            documentReference.addSnapshotListener((snapshot, error) -> {
                if (error != null) {
                    Log.w(TAG, "Listen failed", error);
                }

                if (snapshot != null && snapshot.exists()) {
                    Map<String, Object> data = snapshot.getData();

                    // Check if a book is requested.
                    assert data != null;
                    if (Objects.equals(data.get("status"), "Requested")) {
                        ArrayList<String> requesters = (ArrayList<String>) data.get("requesters");
                        String borrowerUID = (String) data.get("borrowerUID");
                        String ownerUID = (String) data.get("ownerUID");
                        User borrower;
                        String title = (String) data.get("title");

                        if (borrowerUID != null && !borrowerUID.equals(ownerUID)) {
                            // If the borrowerUID exists and is not the same as the ownerUID, then add a notification with that username.
                            borrower = users.getUser(borrowerUID);
                            User owner = users.getUser(ownerUID);
                            TextView requestedString = new TextView(NotificationActivity.this);
                            requestedString.setText(String.format("%s has requested %s", borrower.getProfile().getUsername(), title));
                            linearLayout.addView(requestedString);
                        } else {
                            assert requesters != null;
                            if (requesters.size() > 0) {
                                // Otherwise, if there's any requesters, then add their username.
                                for (String requester : requesters) {
                                    if (Objects.equals(requester, borrowerUID)) continue;
                                    TextView requestedString = new TextView(NotificationActivity.this);
                                    requestedString.setText(String.format("%s has requested %s", users.getUser(requester).getProfile().getUsername(), title));
                                    linearLayout.addView(requestedString);
                                }
                            }
                        }
                        Log.d(TAG, "Current data: " + snapshot.getData());

                    }

                } else {
                    Log.d(TAG, "Current data: null");
                }
            });
        }
        // Go through the accepted books of the user and create notifications for those as well.
        for (Book book: acceptedBooks) {
            User owner = users.getUser(book.getOwnerUID());
            String title = book.getTitle();
            TextView acceptedString = new TextView(NotificationActivity.this);
            acceptedString.setText(String.format("%s has accepted your request for %s", owner.getProfile().getUsername(), title));
            linearLayout.addView(acceptedString);
        }


    }


}
