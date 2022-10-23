/*
 * AddBookActivity
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
 *
 * Referenced for Retrieving Book Info
 * https://developers.google.com/books/docs/v1/using
 * by Google Developers
 * Published May 18, 2020
 * Licensed under the Apache 2.0 License.
 * https://developer.android.com/training/volley/simple
 * by Android Developers
 * Published June 16, 2020
 * Licensed under the Apache 2.0 license.
 */

package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * This Activity allows a user to add a new book.
 * Selecting the camera button allows the user to use their phone camera to scan an ISBN off of a book.
 * Successfully scanning an ISBN will fill the corresponding EditText fields with the book information, if found.
 */
public class AddBookActivity extends AppCompatActivity {
    private EditText author;
    private EditText ISBN;
    private EditText title;
    private EditText description;
    private final ScanIsbn scanner = new ScanIsbn();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // Initialize the EditTexts.
        author = findViewById(R.id.author);
        ISBN = findViewById(R.id.ISBN);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);

        // Initialize the button for adding a book
        final Button add_book = findViewById(R.id.buttonAddBook);

        // Retrieve the user id.
        final String uid = getIntent().getExtras().getString("UID");

        // Set the picture id.
        int pic_id = 1;

        // Initialize the scan button and the listener for clicking it.
        Button scan_button = findViewById(R.id.scan_button);
        scan_button.setOnClickListener(view -> {
            // Clicking the scan button calls scanCode from the ScanISBN class.
            scanner.scanCode(AddBookActivity.this);
        });

        // Initialize the click listener for adding a book.
        add_book.setOnClickListener(view -> {
            // Retrieve the values from the EditTexts.
            // If the author, ISBN, or title are blank, set an error and prevent the book from being added.
            String authorString = author.getText().toString();
            if (authorString.length() == 0) {
                author.setError("You must enter an author name.");
                return;
            }
            String ISBNString = ISBN.getText().toString();
            if (ISBNString.length() == 0) {
                ISBN.setError("You must enter an ISBN.");
                return;
            }
            String titleString = title.getText().toString();
            if (titleString.length() == 0) {
                title.setError("You must enter a title.");
                return;
            }
            String descriptionString = description.getText().toString();

            // Create a new book using the given EditTexts
            Book newBook = new Book(ISBNString, authorString, titleString);

            // Set the other values.
            newBook.setDescription(descriptionString);
            newBook.setOwnerUID(uid);
            newBook.setBorrowerUID(uid);

            // Initialize the user db and add the book to the user.
            UserDB userDB = new UserDB();
            User user = userDB.getUser(uid);
            user.addBook(ISBNString);

            // Initialize the book DB and add the user to the user db and book to the book db to update them.
            BookDB bookDB = new BookDB();
            userDB.addUser(user, uid);
            bookDB.addBook(newBook);

            // Set the result to 1 to show that a book has changed.
            setResult(1);
            finish();
        });
    }

    /**
     * This function returns a string that is parsed from a larger string.
     * The given string comes from the Google Books API, and includes many lines in the form
     * "search term": "value".
     * It finds the search term in the given string, trims out everything until the
     * first " after the search term, then retrieves the string until the next " in order to parse the value.
     * @param givenString This is the large string that is being parsed.
     * @param searchTerm This is the term that is being searched for in the given string.
     * @return a String containing the value corresponding to the search term.
     */
    private String parseString(String givenString, String searchTerm) {
        if (!givenString.contains(searchTerm)) {
            return null;
        }
        int startIndex = givenString.indexOf("\"", (givenString.indexOf(searchTerm) + searchTerm.length() + 1)) + 1;
        int endIndex = givenString.indexOf("\"", startIndex);
        return givenString.substring(startIndex, endIndex);
    }

    /**
     * This function retrieves the result from the scanner activity and sends it to be parsed.
     * @param requestCode Contains the request code sent to the other activity.
     * @param resultCode Contains the result code from the other activity, if applicable.
     * @param data Contains the data from the other activity.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Retrieve the result value from the scanner.
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            // Display the dialog asking a user if they want to scan again or choose the given ISBN.
            scanner.displayDialog(this, this, result.getContents());
            // If the ISBN exists, retrieve the information connected to the ISBN.
            if (result.getContents() != null) {
                retrieveBookInfo(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * This function connects to the Google Books API to retrieve information about the book
     * based on a given ISBN, if it exists.
     * @param isbnString The ISBN to search for.
     */
    private void retrieveBookInfo(String isbnString) {
        // Create the requestQueue for Volley.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create the url that will be searched for the book information.
        String url ="https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbnString;

        // Set the ISBN EditText to the retrieved ISBN.
        ISBN.setText(isbnString);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Empty the EditTexts.
                    title.setText("");
                    author.setText("");
                    description.setText("");

                    // For each value, parse the response to retrieve the data. Set the text of the
                    // EditText if it's found, otherwise notify the user.
                    String authorString = parseString(response, "authors");
                    if (authorString == null) {
                        Toast.makeText(AddBookActivity.this, "Some book info could not be found.", Toast.LENGTH_LONG).show();
                    } else {
                        author.setText(authorString);
                    }
                    String titleString = parseString(response, "title");
                    if (titleString == null) {
                        Toast.makeText(AddBookActivity.this, "Some book info could not be found.", Toast.LENGTH_LONG).show();
                    } else {
                        title.setText(titleString);
                    }
                    String descriptionString = parseString(response, "description");
                    if (descriptionString == null) {
                        Toast.makeText(AddBookActivity.this, "Some book info could not be found.", Toast.LENGTH_LONG).show();
                    } else {
                        description.setText(descriptionString);
                    }
                }, error -> Log.d("RESPONSE", "Could not reach Google Books."));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
