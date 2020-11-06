package com.example.athenaeum;

import android.os.Parcelable;
import android.widget.AdapterView;
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


    final String TAG = "Sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookList = findViewById(R.id.book_list);
        bookDataList = new ArrayList<>();

        bookAdapter = new CustomBookList(this, bookDataList);

        // For testing only.
        Book book1 = new Book("2472374", "Asdf Qwerty", "Lorem Ipsum");
        Book book2 = new Book("34727348", "Asdf Qwerty", "Dolor Sit Amet");
        Book book3 = new Book("34727347", "Asdf Qwerty", "Bees");
        book1.setDescription("Desc1");
        book2.setDescription("Desc2");
        book3.setDescription("Desc3");
        book1.setStatus("available");
        book2.setStatus("requested");
        book3.setStatus("borrowed");
        bookDataList.add(book1);
        bookDataList.add(book2);
        bookDataList.add(book3);

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
