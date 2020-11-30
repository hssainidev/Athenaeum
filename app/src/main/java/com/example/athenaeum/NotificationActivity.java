package com.example.athenaeum;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "Hello! ";
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_notification);
        linearLayout=findViewById(R.id.notification_activity);

        final String uid = getIntent().getExtras().getString("UID");
        final ArrayList<String> books = (ArrayList<String>) getIntent().getExtras().getSerializable("ownedBooks");
        final ArrayList<Book> acceptedBooks=(ArrayList<Book>) getIntent().getExtras().getSerializable("acceptedBooks");
        final UserDB users = new UserDB();
        final BookDB booksDB = new BookDB();
        final User currentUser = users.getUser(uid);

        for (String book : books) {
            final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Books").document(book);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.w(TAG, "Listen failed", error);
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Map<String, Object> data = snapshot.getData();

                        if (Objects.equals(data.get("status"), "Requested")) {
                            ArrayList<String> requesters = (ArrayList<String>) data.get("requesters");
                            String borrowerUID = (String) data.get("borrowerUID");
                            User borrower = null;
                            if (borrowerUID != null) {
                                borrower = users.getUser(borrowerUID);
                            }
                            String title = (String) data.get("title");
                            String ownerUID = (String) data.get("ownerUID");
                            User owner = users.getUser(ownerUID);
                            TextView requestedString = new TextView(NotificationActivity.this);
                            requestedString.setText(String.format("%s has requested %s", borrower, title));
//                            linearLayout.setBackgroundColor(Color.TRANSPARENT);
                            if (borrower!=null) {
                                linearLayout.addView(requestedString);
                            }


                            Log.d(TAG, "Current data: " + snapshot.getData());

                        }

                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }
        for (Book book: acceptedBooks) {
            User owner=users.getUser(book.getOwnerUID());
            String title=book.getTitle();
            TextView acceptedString=new TextView(NotificationActivity.this);
            acceptedString.setText(String.format("%s has accepted your request for %s", owner.getProfile().getUsername(), title));
            linearLayout.addView(acceptedString);

        }


    }


}
