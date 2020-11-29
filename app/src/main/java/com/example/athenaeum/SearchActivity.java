package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    EditText keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        keyword = findViewById(R.id.keyword);
        final Button search = findViewById(R.id.search);
        final String uid = getIntent().getExtras().getString("UID");
        final BookDB db = new BookDB();


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword_string = keyword.getText().toString();
                ArrayList<Book> list = db.searchBooks(keyword_string);
                ListView listView = findViewById(R.id.result_list);
                ArrayAdapter<Book> adapter = new CustomBookList(SearchActivity.this, list);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Book book = (Book) parent.getAdapter().getItem(position);
                        Intent intent = new Intent(SearchActivity.this, BookInfoActivity.class);
                        intent.putExtra("BOOK", book);
                        intent.putExtra("UID", uid);
                        startActivity(intent);
                    }
                });

            }
        });


    }
}
