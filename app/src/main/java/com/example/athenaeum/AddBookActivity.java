package com.example.athenaeum;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

public class AddBookActivity extends AppCompatActivity {

    int pic_id;
    EditText author;
    EditText ISBN;
    EditText title;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        author = findViewById(R.id.author);
        ISBN = findViewById(R.id.ISBN);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        final Button add_book = findViewById(R.id.buttonAddBook);
        final String uid = getIntent().getExtras().getString("UID");
        pic_id = 1;

        Button scan_button = findViewById(R.id.scan_button);
        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, pic_id);
            }
        });


        add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String authorString = author.getText().toString();
                String ISBNString = ISBN.getText().toString();
                String titleString = title.getText().toString();
                String descriptionString = description.getText().toString();
                Book newBook = new Book(ISBNString,authorString,titleString);
                newBook.setDescription(descriptionString);
                newBook.setOwnerUID(uid);
                UserDB userDB = new UserDB();
                User user = userDB.getUser(uid);
                BookDB bookDB = new BookDB();
                user.addBook(newBook);
                userDB.addUser(user, uid);
                bookDB.addBook(newBook);
                Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
                intent.putExtra("UID", uid);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            Bitmap bitmap = (Bitmap) data.getExtras()
                    .get("data");
            Log.d("SCAN", "Image scanned");

            InputImage image = InputImage.fromBitmap(bitmap, 0);
            TextRecognizer recognizer = TextRecognition.getClient();

            Task<Text> result =
                    recognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {
                                    // Task completed successfully
                                    // ...
                                    Log.d("SUCCESS", "Text retrieved");
                                    Log.d("TEXT FOUND", visionText.getText());
                                    for (Text.TextBlock block : visionText.getTextBlocks()) {
                                        for (Text.Line line : block.getLines()) {
                                            for (Text.Element element : line.getElements()) {
                                                Log.d("TEXT FOUND", element.getText());
                                            }
                                        }
                                    }
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                            Log.d("Failure", "Text could not be retrieved");
                                        }
                                    });
        }
    }
}
