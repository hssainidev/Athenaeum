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

public class OwnedBookActivity extends AppCompatActivity {
    TextView name;
    ListView bookList;
    Spinner mySpinner;
    ArrayAdapter<Book> bookAdapter;
    String[] status = {"All", "Available", "Accepted", "Requested", "Borrowed"};
    final BookDB booksDB = new BookDB();
    ArrayList<Book> bookDataList = new ArrayList<>();
    String uid;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_ownedbooklist);


        AthenaeumProfile profile = (AthenaeumProfile) getIntent().getExtras().getSerializable("profile");
        uid = (String) getIntent().getExtras().getSerializable("UID");
        //noinspection unchecked
        final ArrayList<String> books = (ArrayList<String>) getIntent().getExtras().getSerializable("ownedBooks");

        for (String isbn : books) {
            Book myBook = booksDB.getBook(isbn);
            bookDataList.add(myBook);
        }
        name = findViewById(R.id.headerLabel);
        name.setText(String.format("%s's Books", profile.getUsername()));

        bookList = findViewById(R.id.myBookList);
        bookAdapter = new CustomBookList(this, bookDataList);
        bookList.setAdapter(bookAdapter);

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
                ArrayList<Book> newBooks = new ArrayList<>();
                if (statusID == 0) {
                    bookAdapter = new CustomBookList(OwnedBookActivity.this, books);
                } else {
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

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) parent.getAdapter().getItem(position);
                Intent intent = new Intent(OwnedBookActivity.this, BookInfoActivity.class);
                intent.putExtra("BOOK", book);
                intent.putExtra("UID", uid);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
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
