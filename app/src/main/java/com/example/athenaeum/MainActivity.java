package com.example.athenaeum;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookList = findViewById(R.id.book_list);
        bookDataList = new ArrayList<>();

        Book book1 = new Book();
        Book book2 = new Book();

        book1.setISBN("2472374");
        book1.setTitle("Lorem Ipsum");
        book1.setAuthor("Asdf Qwerty");

        book2.setISBN("34727348");
        book2.setTitle("Dolor Sit Amet");
        book2.setAuthor("Asdf Qwerty");

        bookDataList.add(book1);
        bookDataList.add(book2);

        bookAdapter = new CustomBookList(this, bookDataList);

        bookList.setAdapter(bookAdapter);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(drawerLayout)
                        .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }
}
