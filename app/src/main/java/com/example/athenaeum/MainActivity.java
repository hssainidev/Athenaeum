package com.example.athenaeum;

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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
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


    final String TAG = "Sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String uid=getIntent().getExtras().getString("UID");
        users=new UserDB();
        User currentUser=users.getUser(uid);

        bookList = findViewById(R.id.book_list);
        bookDataList = currentUser.getBooks();

        bookAdapter = new CustomBookList(this, bookDataList);

//        // For testing only.
//        Book book1 = new Book("2472374", "Asdf Qwerty", "Lorem Ipsum");
//        Book book2 = new Book("34727348", "Asdf Qwerty", "Dolor Sit Amet");

//        bookDataList.add(book1);
//        bookDataList.add(book2);

        bookList.setAdapter(bookAdapter);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setOpenableLayout(drawerLayout)
                        .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menu_logout) {
                            finish();
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

        // Access a Cloud Firestore instance from your Activity
        /*FirebaseFirestore db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Books");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                bookDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get("ISBN")));
                    String ISBN = (String) doc.getData().get("ISBN");
                    String author = (String) doc.getData().get("Author");
                    String title = (String) doc.getData().get("Title");
                    bookDataList.add(new Book(ISBN, author, title)); // Adding the cities and provinces from FireStore
                }
                bookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });*/
    }
}
