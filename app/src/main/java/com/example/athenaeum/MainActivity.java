/*
 * MainActivity
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
 * Referenced for Navigation Drawer
 * https://developer.android.com/guide/navigation/navigation-ui
 * by Android Developers
 * Published November 18, 2020
 * Licensed under the Apache 2.0 license.
 */

package com.example.athenaeum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * This activity is the main menu of the application. From here all other functions can be
 * accessed, including book searches, requests, user profile viewing, etc.
 */
public class MainActivity extends AppCompatActivity {
    private ListView bookList;
    private ArrayAdapter<Book> bookAdapter;
    private ArrayList<Book> bookDataList;
    private UserDB users;
    private BookDB booksDB;
    private String uid;
    private final String TAG = "BookRetrieval";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uid = getIntent().getExtras().getString("UID");
        users = new UserDB();
        final User currentUser = users.getUser(uid);
        booksDB = new BookDB();

        // Initialize the list of books.
        bookList = findViewById(R.id.book_list);
        bookDataList = new ArrayList<>();
        ArrayList<String> user_ISBNs = currentUser.getBooks();
        for (String isbn : user_ISBNs) {
            bookDataList.add(booksDB.getBook(isbn));
        }

        bookAdapter = new CustomBookList(this, bookDataList);

        bookList.setAdapter(bookAdapter);

        // Launch BookInfoActivity to display information on a book chosen from the bookList.
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) parent.getAdapter().getItem(position);
                Intent intent = new Intent(MainActivity.this, BookInfoActivity.class);
                intent.putExtra("BOOK", book);
                intent.putExtra("UID", uid);
                startActivityForResult(intent, 1);
            }
        });

        // Initialize the controller for the navigation menu.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setOpenableLayout(drawerLayout)
                        .build();
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);
        final NavigationView navigationView = findViewById(R.id.nav_view);


        //increments notificationCounter when a request is accepted or an owned book is requested
        int notificationCounter = 0;
        ArrayList<Book> acceptedBooks = booksDB.getAcceptedBooks(uid);
        for (Book book : acceptedBooks) {
            notificationCounter++;
        }
        for (String isbn : user_ISBNs) {
            Book book = booksDB.getBook(isbn);
            if (book.getStatus().equals("Requested")) {
                ArrayList<String> requesters = book.getRequesters();
                if (requesters.size() > 0) {
                    notificationCounter += requesters.size();
                }
            }
        }
        //adds notification icon to menu
        if (notificationCounter > 0) {
            TextView view = (TextView) navigationView.getMenu().findItem(R.id.menu_notifications).getActionView();
            view.setWidth(80);
            view.setHeight(80);
            view.setText(String.valueOf(notificationCounter));
            view.setTextColor(Color.WHITE);

            view.setBackground(getResources().getDrawable(R.drawable.shape));
        }


        // Add the current user's name and username to the header.
        View header = navigationView.getHeaderView(0);
        TextView nameHeader = header.findViewById(R.id.nameHeader);
        nameHeader.setText(currentUser.getProfile().getName());
        TextView usernameHeader = header.findViewById(R.id.usernameHeader);
        usernameHeader.setText(currentUser.getProfile().getUsername());

        final UserAuth userAuth = new UserAuth();

        // Initialize the listener for clicking any item in the menu.
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NotNull MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menu_logout) {
                            // If logout is clicked, sign the user out and return to the login screen.
                            userAuth.signOut();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } else if (menuItem.getItemId() == R.id.menu_profile) {
                            // If profile is selected, start a new intent with a bundle containing
                            // the currentUser, the currentUser's profile, the associated UID, and
                            // a flag communicating that the profile being displayed is owned by
                            // the logged in user, then go to the profile screen.
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user", currentUser);
                            bundle.putSerializable("profile", currentUser.getProfile());
                            bundle.putString("UID", uid);
                            bundle.putBoolean("fromSearch", false);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else if (menuItem.getItemId() == R.id.menu_owned) {
                            // If owned books is selected, start a new intent with a bundle
                            // containing the currentUser's books and profile, as well as the
                            // associated UID, then go to the owned books screen.
                            Intent intent = new Intent(MainActivity.this, OwnedBookActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ownedBooks", currentUser.getBooks());
                            bundle.putSerializable("UID", uid);
                            bundle.putSerializable("profile", currentUser.getProfile());
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else if (menuItem.getItemId() == R.id.menu_notifications) {
                            // If notifications are selected, start a new intent with a bundle
                            // containing the currentUser's owned and accepted books, their profile
                            // and UID. Then, go to the notification screen.
                            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ownedBooks", currentUser.getBooks());
                            bundle.putSerializable("UID", uid);
                            bundle.putSerializable("profile", currentUser.getProfile());
                            bundle.putSerializable("acceptedBooks", booksDB.getAcceptedBooks(uid));
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else if (menuItem.getItemId() == R.id.menu_scan) {
                            // If scan is selected, start an intent with a bundle containing the UID
                            // and go to the scan screen.
                            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("UID", uid);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else if (menuItem.getItemId() == R.id.menu_borrowed) {
                            // If borrowed books is selected, start a new intent with a bundle
                            // containing the currentUser's profile and UID and go to the borrowed
                            // books screen.
                            Intent intent = new Intent(MainActivity.this, BorrowedBookActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("UID", uid);
                            bundle.putSerializable("profile", currentUser.getProfile());
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else if (menuItem.getItemId() == R.id.menu_requested) {
                            // If requested books is selected, start an intent with a bundle
                            // containing the currentUser's profile and UID and go to the requested
                            // books screen.
                            Intent intent = new Intent(MainActivity.this, RequestedBookActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("UID", uid);
                            bundle.putSerializable("profile", currentUser.getProfile());
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else if (menuItem.getItemId() == R.id.menu_accepted) {
                            // If accepted books is selected, start an intent with a bundle
                            // containing the currentUser's profile and UID and go to the accepted
                            // books screen.
                            Intent intent = new Intent(MainActivity.this, AcceptedBookActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("UID", uid);
                            bundle.putSerializable("profile", currentUser.getProfile());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        return true;
                    }
                });

        //Sets up add book button
        final Button addBookButton = findViewById(R.id.addBook);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Starts AddBookActivity
                Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
                intent.putExtra("UID", uid);
                startActivityForResult(intent,1);
            }
        });

        //Sets up search button
        final Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Starts SearchActivity
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("UID", uid);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            //Refresh the book list display
            bookDataList.clear();
            ArrayList<String> user_ISBNs = users.getUser(uid).getBooks();
            for (String isbn : user_ISBNs) {
                bookDataList.add(booksDB.getBook(isbn));
            }
            bookAdapter.notifyDataSetChanged();
        }
    }
}
