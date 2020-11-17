package com.example.athenaeum;

import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
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
        BookDB booksDB = new BookDB();

        // Initialize the list of books.
        bookList = findViewById(R.id.book_list);
        bookDataList = currentUser.getBooks();

        bookAdapter = new CustomBookList(this, bookDataList);

        bookList.setAdapter(bookAdapter);

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) parent.getAdapter().getItem(position);
                Intent intent = new Intent(MainActivity.this, BookInfo.class);
                intent.putExtra("BOOK", book);
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
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
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
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menu_logout) {
                            // If logout is clicked, sign the user out and end the activity.
                            userAuth.signOut();
                            finish();
                        } else if (menuItem.getItemId() == R.id.menu_profile) {
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            Bundle bundle = new Bundle();
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
                startActivity(intent);
            }
        });

        final Button searchButton=findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("UID", uid);
                startActivity(intent);
            }
        });

        // Retrieve the information for the books and add it to the books list.
        final CollectionReference collectionReference = booksDB.getCollection();

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                bookDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get("ISBN")));
                    String ISBN = (String) doc.getData().get("isbn");
                    String author = (String) doc.getData().get("author");
                    String title = (String) doc.getData().get("title");
                    bookDataList.add(new Book(ISBN, author, title)); // Adding the cities and provinces from FireStore
                }
                bookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });
    }
}
