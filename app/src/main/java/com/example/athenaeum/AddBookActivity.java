package com.example.athenaeum;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, pic_id);
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

    private String parseString(String givenString, String searchTerm) {
        if (givenString.indexOf(searchTerm) == -1) {
            return null;
        }
        int startIndex = givenString.indexOf("\"", (givenString.indexOf(searchTerm) + searchTerm.length() + 1)) + 1;
        int endIndex = givenString.indexOf("\"", startIndex);
        return givenString.substring(startIndex, endIndex);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            Bitmap bitmap = (Bitmap) data.getExtras()
                    .get("data");
            Log.d("SCAN", "Image scanned");

            InputImage image = InputImage.fromBitmap(bitmap, 0);

            BarcodeScanner scanner = BarcodeScanning.getClient();
            Task<List<Barcode>> result = scanner.process(image)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            // Task completed successfully
                            // ...
                            Log.d("SUCCESS", "Text retrieved");
                            for (Barcode barcode: barcodes) {

                                String rawValue = barcode.getRawValue();

                                int valueType = barcode.getValueType();
                                Log.d("BARCODE", rawValue);
                                Log.d("TYPE", String.valueOf(valueType));
                            }
                            // Hardcoded for testing
                            retrieveBookInfo("0439554004");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            // ...
                        }
                    });

            /*TextRecognizer recognizer = TextRecognition.getClient();

            Task<Text> result =
                    recognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {
                                    // Task completed successfully
                                    // ...
                                    Log.d("SUCCESS", "Text retrieved");
                                    Log.d("TEXT FOUND", visionText.getText());
                                    for (Text.TextBlock block : visionText.getTextBlocks()) {
                                        for (Text.Line line : block.getLines()) {
                                            for (Text.Element element : line.getElements()) {
                                                Log.d("TEXT FOUND", element.getText());
                                            }
                                        }
                                    }
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                            Log.d("Failure", "Text could not be retrieved");
                                        }
                                    });*/
        }
    }

    private void retrieveBookInfo(final String isbnString) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbnString;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println(response);
                        String titleString = parseString(response, "title");
                        if (titleString == null) {
                            // Error
                        } else {
                            title.setText(titleString);
                        }
                        String authorString = parseString(response, "authors");
                        if (authorString == null) {
                            // Error
                        } else {
                            author.setText(authorString);
                        }
                        String descriptionString = parseString(response, "description");
                        if (descriptionString == null) {
                            // Error
                        } else {
                            description.setText(descriptionString);
                        }
                        ISBN.setText(isbnString);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESPONSE", "Failed to get book info.");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
