/*
 * BookInfoActivity
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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

/**
 * This Activity displays the information about a book, with different options
 * depending on the book's status and whether or not the user owners it.
 */
public class BookInfoActivity extends AppCompatActivity implements Serializable {
    private Book book;
    private String uid;
    private BookDB bookDB;
    private UserDB userDB;
    private ImageView imageView;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        // Retrieve the book and uid.
        this.book = (Book) getIntent().getSerializableExtra("BOOK");
        this.uid = getIntent().getExtras().getString("UID");

        // Initialize the databases and the image storage.
        bookDB = new BookDB();
        userDB = new UserDB();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Create a boolean that records whether the user owns the book.
        final boolean ownsBook = book.getOwnerUID().equals(uid);

        // Initialize TextView and EditText components of the layout
        final EditText title = findViewById(R.id.book_title);
        final EditText author = findViewById(R.id.book_info_author);
        final EditText bookDesc = findViewById(R.id.description);
        TextView isbn = findViewById(R.id.book_info_isbn);
        final TextView status = findViewById(R.id.status);

        // Update the TextViews to the corresponding values
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        bookDesc.setText(book.getDescription());
        if (!ownsBook) {
            // If the user does not own the book, these should not be editable.
            title.setFocusable(false);
            author.setFocusable(false);
            bookDesc.setFocusable(false);
        }
        isbn.setText(book.getISBN());
        status.setText(book.getStatus());

        // Initialize the toolbar.
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.book_info_toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());
        TextView toolbar_title = findViewById(R.id.book_toolbar_title);
        toolbar_title.setText(String.format("%s's Info", book.getTitle()));

        // Initialize the button for updating the book's information.
        final Button update_button = findViewById(R.id.update_book_info);
        if (!ownsBook) {
            // Hide the button if the user is does not own the book.
            update_button.setVisibility(View.GONE);
            Toast.makeText(BookInfoActivity.this, "You do not have permission to update this book!", Toast.LENGTH_LONG).show();

        } else {
            update_button.setOnClickListener(view -> {
                // If the user somehow still pressed the button, prevent them from updating it if they do not own it.
                // Check that the title and author have something in them.
                if (title.getText().toString().length() == 0) {
                    title.setError("Must have a title!");
                } else if (author.getText().toString().length() == 0) {
                    author.setError("Must have an author!");
                } else {
                    // Update the book's information, then re-add it to the database to update it there.
                    book.setTitle(title.getText().toString());
                    book.setAuthor(author.getText().toString());
                    book.setDescription(bookDesc.getText().toString());
                    bookDB.addBook(book);

                    // Set the result to 1 to ensure that the previous activity will be updated when the user returns to it.
                    setResult(1);

                    // Notify the user.
                    Toast.makeText(BookInfoActivity.this, "Book successfully updated!", Toast.LENGTH_LONG).show();
                }
            });
        }

        // Initialize the button for deleting a book.
        final Button delete_button = findViewById(R.id.delete_book);
        if (!ownsBook) {
            // Hide the button if the user is does not own the book.
            delete_button.setVisibility(View.GONE);
            Toast.makeText(BookInfoActivity.this, "You do not have permission to delete this book!", Toast.LENGTH_LONG).show();

        } else {
            delete_button.setOnClickListener(view -> {
                // If the user somehow still pressed the button, prevent them from deleting it if they do not own it.
                // Delete the book from the database and from the user.
                bookDB.deleteBook(book.getISBN());
                userDB.deleteBookFromUser(uid, book.getISBN());

                // Set the result to 1 to ensure that the previous activity will be updated.
                setResult(1);
                // Return to the previous activity.
                finish();
            });
        }

        // Initialize a boolean that is true if the current user requested the book.
        final boolean requestedByUser = (book.getStatus().equals("Requested") && book.getRequesters().contains(uid));

        // Initialize the request button.
        final Button request_button = findViewById(R.id.request);
        if (ownsBook || (!book.getStatus().equals("Available") && (!book.getStatus().equals("Requested") || requestedByUser))) {
            // Hide the request button if the user owns the book, or if the book is not available and is also not requested or requested by the user.
            request_button.setVisibility(View.GONE);
            Toast.makeText(BookInfoActivity.this, "You cannot request a book that you own!", Toast.LENGTH_LONG).show();
            return;
        } else {
            request_button.setOnClickListener(view -> {
                // Ensure the button cannot be used if it wasn't supposed to be, even if the user was still able to click on it.
                if ((!book.getStatus().equals("Available") && (!book.getStatus().equals("Requested") || requestedByUser))) {
                    Toast.makeText(BookInfoActivity.this, "You cannot request this book right now!", Toast.LENGTH_LONG).show();
                    return;
                }

                // Request the book and reset the status and request_button visibility.
                bookDB.requestBook(book, uid);
                status.setText(book.getStatus());
                request_button.setVisibility(View.GONE);

                // Set the result to 1 to ensure that the previous activity will be updated.
                setResult(1);

                // Notify the user.
                Toast.makeText(BookInfoActivity.this, "Request sent!", Toast.LENGTH_LONG).show();
            });
        }

        // Initialize the view requests button for when the user is the owner of the book
        // and has received at least one request
        final Button viewRequests_button = findViewById(R.id.request);
        // Hide the button if the user doesn't own the book or the book has no requesters.
        viewRequests_button.setVisibility(View.GONE);

        // Initialize the image view.
        imageView = findViewById(R.id.image);
        if (book.getPhoto()) {
            // Retrieve the photo if it exists.
            StorageReference ref=storageReference.child(book.getISBN()+".jpg");
            final long ONE_MEGABYTE=1024*1024*5;
            Glide.with(this).load(ref).into(imageView);
            System.out.println("Reached"+book.getPhoto());
        }
        else {
            imageView.setImageDrawable(null);
        }
        if (ownsBook) {
            // If the user owns the book, allow them to click the photo.
            // If the photo does not exist, they can set the photo,
            // otherwise they can delete the photo.
            imageView.setOnClickListener(view -> {
                if (!book.getPhoto()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");

                    startActivityForResult(intent, 9);
                }
                else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(BookInfoActivity.this);
                    builder.setMessage("Delete Photo?")
                            .setTitle("Photo")
                            .setPositiveButton("Yes", (dialogInterface, i) -> {
                                bookDB.removePhoto(book);
                                imageView.setImageDrawable(null);
                            })
                            .setNegativeButton("No", (dialogInterface, i) -> {

                            });
                    builder.create().show();
                }

            });
        }

        // Initialize the button for checking the location.
        final Button location_button = findViewById(R.id.location);
        // Boolean for whether a book is accepted and currently in the owner's possession for them to set a location.
        boolean acceptedBook = (book.getStatus().equals("Accepted") && book.getOwnerUID().equals(uid));
        // Boolean for whether a book is marked as accepted by the owner and the location is set.
        boolean borrowingBook = (book.getStatus().equals("Accepted") && book.getRequesters().contains(uid) && book.getLocation() != null);
        if (acceptedBook || borrowingBook) {
            location_button.setVisibility(View.VISIBLE);
            location_button.setOnClickListener(view -> {
                Intent intent = new Intent(BookInfoActivity.this, MapActivity.class);
                intent.putExtra("BOOK", book);
                intent.putExtra("UID", uid);
                startActivityForResult(intent, 1);
            });
        }
    }

    /**
     * This function retrieves the result from the various accessible activities and parses them
     * @param requestCode Contains the request code sent to the other activity.
     * @param resultCode Contains the result code from the other activity, if applicable.
     * @param data Contains the data from the other activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // This is the case of a photo update.
        if (requestCode == 9 && resultCode == RESULT_OK && data != null) {

            //Get selected image uri here
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
                StorageReference ref=storageReference.child(book.getISBN()+".jpg");
                ref.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> bookDB.addPhoto(book))
                        .addOnFailureListener(e -> {

                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == 1) {
            // This is the case of a location change.
            assert data != null;
            LatLng location = data.getExtras().getParcelable("LOCATION");
            book.setLocation(location.latitude, location.longitude);
            bookDB.addBook(book);
            // Set the result to 1 to ensure that the previous activity will be updated.
            setResult(1);
        } else if (resultCode == 2) {
            // This is a case of a status update.
            final TextView status = findViewById(R.id.status);
            // Update the variables to the corresponding values
            book = bookDB.getBook(book.getISBN());
            status.setText(book.getStatus());
            final Button viewRequests_button = findViewById(R.id.request);
            if (book.getRequesters().size() == 0 || !book.getStatus().equals("Requested")) {
                viewRequests_button.setVisibility(View.GONE);
            }
            // Set the result to 1 to ensure that the previous activity will be updated.
            setResult(1);
        }
    }
}

