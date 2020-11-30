package com.example.athenaeum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

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
        if (notificationCounter > 0) {
            TextView view = (TextView) navigationView.getMenu().findItem(R.id.menu_notifications).getActionView();
            view.setWidth(80);
            view.setHeight(80);
            view.setText(String.valueOf(notificationCounter));
            view.setTextColor(Color.WHITE);

            view.setBackground(getResources().getDrawable(R.drawable.shape));
        }
        /*final int[] notificationCounter = new int[]{0};
        for (String book : user_ISBNs) {
            final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Books").document(book);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.w(TAG, "Listen failed", error);
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Map<String, Object> data = snapshot.getData();

                        assert data != null;
                        Object myVar = data.get("status");
                        if (!Objects.equals(data.get("status"), "Available")) {
                            ArrayList<String> requesters = (ArrayList<String>) data.get("requesters");
                            if (requesters.size() > 0) {
                                notificationCounter[0] += requesters.size();
                            } else {
                                notificationCounter[0]++;
                            }
                            Log.d(TAG, "Current data: " + snapshot.getData());
                            TextView view = (TextView) navigationView.getMenu().findItem(R.id.menu_notifications).getActionView();
                            view.setText(String.valueOf(notificationCounter[0]));

                            view.setBackground(getResources().getDrawable(R.drawable.shape));

                        }
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }*/


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
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user", currentUser);
                            bundle.putSerializable("profile", currentUser.getProfile());
                            bundle.putString("UID", uid);
                            bundle.putBoolean("fromSearch", false);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.menu_owned) {
                            Intent intent = new Intent(MainActivity.this, OwnedBookActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ownedBooks", currentUser.getBooks());
                            bundle.putSerializable("UID", uid);
                            bundle.putSerializable("profile", currentUser.getProfile());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.menu_notifications) {
                            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ownedBooks", currentUser.getBooks());
                            bundle.putSerializable("UID", uid);
                            bundle.putSerializable("profile", currentUser.getProfile());
                            bundle.putSerializable("acceptedBooks", booksDB.getAcceptedBooks(uid));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.menu_scan) {
                            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("UID", uid);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.menu_borrowed) {
                            Intent intent = new Intent(MainActivity.this, BorrowedBookActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("UID", uid);
                            bundle.putSerializable("profile", currentUser.getProfile());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.menu_requested) {
                            Intent intent = new Intent(MainActivity.this, RequestedBookActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("UID", uid);
                            bundle.putSerializable("profile", currentUser.getProfile());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.menu_accepted) {
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

        final Button addBookButton = findViewById(R.id.addBook);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
                intent.putExtra("UID", uid);
                startActivityForResult(intent,1);
            }
        });

        final Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            bookDataList.clear();
            ArrayList<String> user_ISBNs = users.getUser(uid).getBooks();
            for (String isbn : user_ISBNs) {
                bookDataList.add(booksDB.getBook(isbn));
            }
            bookAdapter.notifyDataSetChanged();
        }
    }
}
