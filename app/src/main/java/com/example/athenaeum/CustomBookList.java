package com.example.athenaeum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomBookList extends ArrayAdapter<Book> {
    private ArrayList<Book> books;
    private Context context;

    public CustomBookList(Context context, ArrayList<Book> books) {
        super(context, 0, books);
        this.books = books;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.book_list_content, parent, false);
        }

        Book book = books.get(position);
        UserDB users = new UserDB();

        TextView bookTitle = view.findViewById(R.id.book_title);
        TextView bookAuthor = view.findViewById(R.id.book_author);
        TextView bookISBN = view.findViewById(R.id.book_isbn);
        TextView bookStatus = view.findViewById(R.id.book_status);
        TextView bookOwner = view.findViewById(R.id.book_owner);
        TextView bookBorrower = view.findViewById(R.id.book_borrower);

        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookISBN.setText(book.getISBN());
        bookStatus.setText(book.getStatus());
        bookOwner.setText("Owner: " + users.getUser(book.getOwnerUID()).getProfile().getUsername());
        if (book.getStatus().equals("Borrowed") || book.getStatus().equals("Accepted")) {
            bookBorrower.setVisibility(View.VISIBLE);
            if (book.getOwnerUID().equals(book.getBorrowerUID()) || book.getBorrowerUID() == null) {
                bookBorrower.setText("Borrower: " + users.getUser(book.getRequesters().get(0)).getProfile().getUsername());
            } else {
                bookBorrower.setText("Borrower: " + users.getUser(book.getBorrowerUID()).getProfile().getUsername());
            }
        }

        return view;

    }
}