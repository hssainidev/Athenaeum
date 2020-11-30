package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

// Can view all the details of a book
public class BookInfoActivity extends AppCompatActivity implements Serializable {
    private Book book;
    private String uid;
    private BookDB bookDB;
    private UserDB userDB;
    private int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        this.book = (Book) getIntent().getSerializableExtra("BOOK");
        this.uid = (String) getIntent().getExtras().getString("UID");
        System.out.println(uid);

        bookDB = new BookDB();
        userDB = new UserDB();

        // connecting the variables to the TextView components of the layout
        TextView title = (TextView) findViewById(R.id.book_title);
        final EditText bookDesc = (EditText) findViewById(R.id.description);
        TextView status = (TextView) findViewById(R.id.status);

        // updating the variables to the corresponding values
        title.setText(book.getTitle());
        bookDesc.setText(book.getDescription());
        status.setText(book.getStatus());

        // Update the book description if it has changed
        final Button update_description_button = (Button) findViewById(R.id.update_description);
        update_description_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookDesc.getText().toString().length() == 0) {
                    bookDesc.setError("Must have a description");
                } else {
                    book.setDescription(bookDesc.getText().toString());
                    bookDB.addBook(book);
                }
            }
        });

        // Delete a book
        final Button delete_button = (Button) findViewById(R.id.delete_book);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookDB.deleteBook(book.getISBN());
                userDB.deleteBookFromUser(uid, book.getISBN());
                Intent intent = new Intent(BookInfoActivity.this, MainActivity.class);
                intent.putExtra("UID", uid);
                startActivity(intent);
            }
        });

        // View requests button for when the user is the owner of the book
        // and has received atleast one request
        final Button request_button = (Button) findViewById(R.id.request);
        request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println((null == uid) ? "Yes" : "No");
                bookDB.requestBook(book, uid);
                System.out.println("After going to Request function");
            }
        });

        final Button viewRequests_button = (Button) findViewById(R.id.view_requests);
        viewRequests_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookInfoActivity.this, ViewRequestActivity.class);
//                i.putExtra("book_ISBN", book.getISBN());
                i.putExtra("BOOK", book);
                i.putExtra("UID", uid);
//                i.putExtra("num", book.getRequesters().size());
//                System.out.println(book.getRequesters().size());
//                for(int j=0; j < book.getRequesters().size(); j++){
//                    String str=userDB.getUser(book.getRequesters().get(j)).getProfile().getUsername();
//                    String final_str = (book.getTitle() + " is requested by user: " + str);
//                    System.out.println(final_str);
//                    i.putExtra(String.valueOf(j), final_str);
//                }
                startActivity(i);
            }
        });

        final Button location_button = (Button) findViewById(R.id.location);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.requestCode) {
            LatLng location = data.getExtras().getParcelable("LOCATION");
            book.setLocation(location.latitude, location.longitude);
            bookDB.addBook(book);
        }
    }
}

