package com.example.athenaeum;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "Hello! ";

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        final String uid = getIntent().getExtras().getString("UID");
        final ArrayList<String> books = (ArrayList<String>) getIntent().getExtras().getSerializable("ownedBooks");
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

                        if (data.get("status") != "Available") {
                            Log.d(TAG, "Current data: " + snapshot.getData());

                        }
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }


    }


}
