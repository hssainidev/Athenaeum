package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddBookActivity extends AppCompatActivity {
    EditText author;
    EditText ISBN;
    EditText title;
    EditText description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        author=findViewById(R.id.author);
        ISBN=findViewById(R.id.ISBN);
        title=findViewById(R.id.title);
        description=findViewById(R.id.description);
        final Button add_book=findViewById(R.id.buttonAddBook);
        final String uid=getIntent().getExtras().getString("UID");


        add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String authorString=author.getText().toString();
                String ISBNString=ISBN.getText().toString();
                String titleString=title.getText().toString();
                String descriptionString=description.getText().toString();
                Book newBook=new Book(ISBNString,authorString,titleString);
                newBook.setDescription(descriptionString);
                newBook.setOwnerUID(uid);
                UserDB userDB=new UserDB();
                User user=userDB.getUser(uid);
                BookDB bookDB=new BookDB();
                user.addBook(newBook);
                userDB.addUser(user, uid);
                bookDB.addBook(newBook);
                Intent intent=new Intent(AddBookActivity.this, MainActivity.class);
                intent.putExtra("UID", uid);
                startActivity(intent);
            }
        });



    }
}
