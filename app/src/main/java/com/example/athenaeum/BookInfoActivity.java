package com.example.athenaeum;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

// Can view all the details of a book
public class BookInfoActivity extends AppCompatActivity implements Serializable {
    private Book book;
    private String uid;
    private BookDB bookDB;
    private UserDB userDB;
    private int requestCode = 1;
    private ImageView imageView;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        this.book = (Book) getIntent().getSerializableExtra("BOOK");
        this.uid = (String) getIntent().getExtras().getString("UID");
        System.out.println(uid);

        bookDB = new BookDB();
        userDB = new UserDB();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        final boolean ownsBook = book.getOwnerUID().equals(uid);

        // connecting the variables to the TextView components of the layout
        final EditText title = findViewById(R.id.book_title);
        final EditText author = findViewById(R.id.book_info_author);
        final EditText bookDesc = (EditText) findViewById(R.id.description);
        TextView isbn = findViewById(R.id.book_info_isbn);
        final TextView status = (TextView) findViewById(R.id.status);

        // updating the variables to the corresponding values
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        bookDesc.setText(book.getDescription());
        if (!ownsBook) {
            title.setFocusable(false);
            author.setFocusable(false);
            bookDesc.setFocusable(false);
        }
        isbn.setText(book.getISBN());
        status.setText(book.getStatus());

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.book_info_toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView toolbar_title = findViewById(R.id.book_toolbar_title);
        toolbar_title.setText(book.getTitle() + "'s Info");

        // Update the book description if it has changed
        final Button update_button = (Button) findViewById(R.id.update_book_info);
        if (!ownsBook) {
            update_button.setVisibility(View.GONE);
        } else {
            update_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ownsBook) {
                        Toast.makeText(BookInfoActivity.this, "You do not have permission to update this book!", Toast.LENGTH_LONG).show();
                    }
                    if (title.getText().toString().length() == 0) {
                        title.setError("Must have a title!");
                    } else if (author.getText().toString().length() == 0) {
                        author.setError("Must have an author!");
                    } else {
                        book.setTitle(title.getText().toString());
                        book.setAuthor(author.getText().toString());
                        book.setDescription(bookDesc.getText().toString());
                        bookDB.addBook(book);
                        setResult(1);
                        Toast.makeText(BookInfoActivity.this, "Book successfully updated!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        // Delete a book
        final Button delete_button = (Button) findViewById(R.id.delete_book);
        if (!ownsBook) {
            delete_button.setVisibility(View.GONE);
        } else {
            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ownsBook) {
                        Toast.makeText(BookInfoActivity.this, "You do not have permission to delete this book!", Toast.LENGTH_LONG).show();
                    }
                    bookDB.deleteBook(book.getISBN());
                    userDB.deleteBookFromUser(uid, book.getISBN());
                    setResult(1);
                    finish();
                }
            });
        }

        boolean requestedByUser = (book.getStatus().equals("Requested") && book.getRequesters().contains(uid));
        final Button request_button = (Button) findViewById(R.id.request);
        if (ownsBook || (!book.getStatus().equals("Available") && (!book.getStatus().equals("Requested") || requestedByUser))) {
//            request_button.setVisibility(View.GONE);
        } else {
            request_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ownsBook) {
                        Toast.makeText(BookInfoActivity.this, "You cannot request a book that you own!", Toast.LENGTH_LONG).show();
                    } else if (!book.getStatus().equals("Available")) {
                        Toast.makeText(BookInfoActivity.this, "You cannot request this book right now!", Toast.LENGTH_LONG).show();
                    }
                    System.out.println((null == uid) ? "Yes" : "No");
                    bookDB.requestBook(book, uid);
                    System.out.println("After going to Request function");
                    status.setText(book.getStatus());
                    request_button.setVisibility(View.GONE);
                    Toast.makeText(BookInfoActivity.this, "Request sent!", Toast.LENGTH_LONG).show();
                }
            });
        }

        // View requests button for when the user is the owner of the book
        // and has received at least one request
        final Button viewRequests_button = (Button) findViewById(R.id.view_requests);
        if (!ownsBook || book.getRequesters().size() == 0) {
            viewRequests_button.setVisibility(View.GONE);
        } else {
            viewRequests_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ownsBook) {
                        Toast.makeText(BookInfoActivity.this, "You don't have permission to view the requests of this book!", Toast.LENGTH_LONG).show();
                    } else if (book.getRequesters().size() == 0) {
                        Toast.makeText(BookInfoActivity.this, "This book has no current requests to view!", Toast.LENGTH_LONG).show();
                    }
                    Intent i = new Intent(BookInfoActivity.this, ViewRequestActivity.class);
                    i.putExtra("BOOK", book);
                    i.putExtra("UID", uid);
                    startActivity(i);
                }
            });
        }
        imageView = findViewById(R.id.image);
        if (book.getPhoto()) {
            StorageReference ref=storageReference.child(book.getISBN()+".jpg");
            final long ONE_MEGABYTE=1024*1024*5;
            Glide.with(this).load(ref).into(imageView);
            System.out.println("Reached"+book.getPhoto());
        }
        else {
            imageView.setImageDrawable(null);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    bookDB.removePhoto(book);
                                    imageView.setImageDrawable(null);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    builder.create().show();
                }

            }
        });

        final Button location_button = (Button) findViewById(R.id.location);
        // Boolean for whether a book is accepted and currently in the owner's possession for them to set a location.
        boolean acceptedBook = (book.getStatus().equals("Accepted") && book.getOwnerUID().equals(uid));
        // Boolean for whether a book is marked as accepted by the owner and the location is set.
        boolean borrowingBook = (book.getStatus().equals("Accepted") && book.getRequesters().contains(uid) && book.getLocation() != null);
        if (acceptedBook || borrowingBook) {
            location_button.setVisibility(View.VISIBLE);
            location_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(BookInfoActivity.this, MapActivity.class);
                    intent.putExtra("BOOK", book);
                    intent.putExtra("UID", uid);
                    startActivityForResult(intent, requestCode);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9 && resultCode == RESULT_OK && data != null) {

            //Get selected image uri here
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
                StorageReference ref=storageReference.child(book.getISBN()+".jpg");
                ref.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                bookDB.addPhoto(book);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == this.requestCode) {
            LatLng location = data.getExtras().getParcelable("LOCATION");
            book.setLocation(location.latitude, location.longitude);
            bookDB.addBook(book);
        }
    }
}

