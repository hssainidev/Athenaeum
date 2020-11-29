package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewRequestActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_requests);
        final ListView list = findViewById(R.id.requester_list);
        final BookDB booksDB=new BookDB();

        ArrayList<String> arrayList = new ArrayList<String>();
        Intent i = getIntent();
        final Book book=(Book) getIntent().getSerializableExtra("BOOK");
        final String uid=getIntent().getExtras().getString("UID");

//        String isbn = intent.getStringExtra("book_ISBN");
//        System.out.println(isbn);
//        Book book = new BookDB().searchBook(isbn);
//        System.out.println(book.getTitle());
//        System.out.println(book.getRequesters().size());

//        Integer size = i.getIntExtra("num", 0);

//        for(String requester: book.getRequesters()){
//            arrayList.add(requester);
//            System.out.println(requester);
//        }
        arrayList=book.getRequesters();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.view_requests_list, arrayList);
        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String entry = (String) parent.getItemAtPosition(position);
                final Button accept_button = (Button) findViewById(R.id.accept);
                accept_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        booksDB.acceptRequest(book, entry);
                        Intent intent=new Intent(ViewRequestActivity.this, MainActivity.class);
                        intent.putExtra("UID",uid);
                        startActivity(intent);
                    }
                });


                final Button decline_button = (Button) findViewById(R.id.decline);
                decline_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        list.remove(list.get(position));
//                        arrayAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
