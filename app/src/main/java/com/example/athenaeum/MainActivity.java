package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {
    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;
    UserDB users;


    final String TAG = "BookRetrieval";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String uid = getIntent().getExtras().getString("UID");
        users = new UserDB();
        final User currentUser = users.getUser(uid);
        final BookDB booksDB = new BookDB();

        // Initialize the list of books.
        bookList = findViewById(R.id.book_list);
        bookDataList = new ArrayList<>();
        ArrayList<String> user_ISBNs = currentUser.getBooks();
        for (String isbn : user_ISBNs) {
            bookDataList.add(booksDB.getBook(isbn));
        }

//        for (Book book: bookDataList) {
//            Log.d("book", book.getISBN());
//        }

        bookAdapter = new CustomBookList(this, bookDataList);

        bookList.setAdapter(bookAdapter);

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) parent.getAdapter().getItem(position);
                Intent intent = new Intent(MainActivity.this, BookInfoActivity.class);
                intent.putExtra("BOOK", book);
                intent.putExtra("UID", uid);
                startActivity(intent);
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
        NavigationView navigationView = findViewById(R.id.nav_view);

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
                            bundle.putSerializable("profile", currentUser.getProfile());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.menu_owned) {
                            Intent intent = new Intent(MainActivity.this, OwnedBookActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ownedBooks", currentUser.getBooks());
                            bundle.putSerializable("profile", currentUser.getProfile());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.menu_notifications) {
                            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ownedBooks", currentUser.getBooks());
                            bundle.putSerializable("UID", uid);
                            bundle.putSerializable("profile", currentUser.getProfile());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.menu_scan) {
                            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("UID", uid);
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
                startActivity(intent);
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

        // Retrieve the information for the books and add it to the books list.
//        final CollectionReference collectionReference = booksDB.getCollection();
//
//        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
//                    FirebaseFirestoreException error) {
//                if (error != null) {
//                    return;
//                }
//                bookDataList.clear();
//                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
//                {
//                    Log.d(TAG, String.valueOf(doc.getData().get("ISBN")));
//                    String ISBN = (String) doc.getData().get("isbn");
//                    String author = (String) doc.getData().get("author");
//                    String title = (String) doc.getData().get("title");
//                    bookDataList.add(new Book(ISBN, author, title)); // Adding the cities and provinces from FireStore
//                }
//                bookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
//            }
//        });
    }
}
