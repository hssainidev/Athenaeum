/*
 * CustomBookList
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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This custom ArrayAdapter is used to display Books. It has an ArrayList of books
 * and a Context for the activity it was constructed in.
 */
public class CustomBookList extends ArrayAdapter<Book> {
    private final ArrayList<Book> books;
    private final Context context;

    /**
     * This constructs a new CustomBookList with the context of the parent activity and
     * a list of books to be displayed.
     *
     * @param context The Context of the parent activity.
     * @param books An ArrayList of books to be displayed.
     */
    public CustomBookList(Context context, ArrayList<Book> books) {
        super(context, 0, books);
        this.books = books;
        this.context = context;
    }


    // Manually sets up view for displaying books
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
        bookOwner.setText(String.format("Owner: %s", users.getUser(book.getOwnerUID()).getProfile().getUsername()));

        // Only displays borrower if book is borrowed or accepted.
        if (book.getStatus().equals("Borrowed") || book.getStatus().equals("Accepted")) {
            bookBorrower.setVisibility(View.VISIBLE);
            if (book.getOwnerUID().equals(book.getBorrowerUID()) || book.getBorrowerUID() == null) {
                bookBorrower.setText(String.format("Borrower: %s", users.getUser(book.getRequesters().get(0)).getProfile().getUsername()));
            } else {
                bookBorrower.setText(String.format("Borrower: %s", users.getUser(book.getBorrowerUID()).getProfile().getUsername()));
            }
        }

        return view;

    }
}