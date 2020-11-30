package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

public class AddBookActivity extends AppCompatActivity {

    int pic_id;
    EditText author;
    EditText ISBN;
    EditText title;
    EditText description;
    ScanIsbn scanner = new ScanIsbn();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        author = findViewById(R.id.author);
        ISBN = findViewById(R.id.ISBN);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        final Button add_book = findViewById(R.id.buttonAddBook);
        final String uid = getIntent().getExtras().getString("UID");

        pic_id = 1;

        Button scan_button = findViewById(R.id.scan_button);
        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanner.scanCode(AddBookActivity.this);
            }
        });


        add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String authorString = author.getText().toString();
                if (authorString.length() == 0) {
                    author.setError("You must enter an author name.");
                    return;
                }
                String ISBNString = ISBN.getText().toString();
                if (ISBNString.length() == 0) {
                    author.setError("You must enter an ISBN.");
                    return;
                }
                String titleString = title.getText().toString();
                if (titleString.length() == 0) {
                    author.setError("You must enter a title.");
                    return;
                }
                String descriptionString = description.getText().toString();

                Book newBook = new Book(ISBNString, authorString, titleString);
                newBook.setDescription(descriptionString);
                newBook.setOwnerUID(uid);
                newBook.setBorrowerUID(uid);
                UserDB userDB = new UserDB();
                User user = userDB.getUser(uid);
                BookDB bookDB = new BookDB();
                user.addBook(ISBNString);

                userDB.addUser(user, uid);
                bookDB.addBook(newBook);
                setResult(1);
                finish();
            }
        });
    }

    private String parseString(String givenString, String searchTerm) {
        if (givenString.indexOf(searchTerm) == -1) {
            return null;
        }
        int startIndex = givenString.indexOf("\"", (givenString.indexOf(searchTerm) + searchTerm.length() + 1)) + 1;
        int endIndex = givenString.indexOf("\"", startIndex);
        return givenString.substring(startIndex, endIndex);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            scanner.displayDialog(this, this, result.getContents());
            if (result.getContents() != null) {
                retrieveBookInfo(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void retrieveBookInfo(String isbnString) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbnString;
        ISBN.setText(isbnString);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        title.setText("");
                        author.setText("");
                        description.setText("");
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
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESPONSE", "Could not reach Google Books.");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
