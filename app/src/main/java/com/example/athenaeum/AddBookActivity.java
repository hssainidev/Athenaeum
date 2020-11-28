package com.example.athenaeum;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.util.List;

public class AddBookActivity extends AppCompatActivity {

    int pic_id;
    EditText author;
    EditText ISBN;
    EditText title;
    EditText description;

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
                scanCode();
                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, pic_id);*/
            }
        });


        add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String authorString = author.getText().toString();
                String ISBNString = ISBN.getText().toString();
                String titleString = title.getText().toString();
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
                Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
                intent.putExtra("UID", uid);
                startActivity(intent);
            }
        });
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code...");
        integrator.initiateScan();

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
            if (result.getContents() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Scanning Result");
                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        scanCode();
                    }
                }).setNegativeButton("Choose This ISBN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        retrieveBookInfo(result.getContents());
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(this, "No Results", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(AddBookActivity.this, "Book info could not be found.", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            author.setText(authorString);
                        }
                        String titleString = parseString(response, "title");
                        if (titleString == null) {
                            Toast.makeText(AddBookActivity.this, "Book info could not be found.", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            title.setText(titleString);
                        }
                        String descriptionString = parseString(response, "description");
                        if (descriptionString == null) {
                            Toast.makeText(AddBookActivity.this, "Book info could not be found.", Toast.LENGTH_LONG).show();
                            return;
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
