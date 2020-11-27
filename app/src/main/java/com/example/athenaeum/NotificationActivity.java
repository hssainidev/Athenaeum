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

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "Hello! ";

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        final String uid = getIntent().getExtras().getString("UID");
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(uid);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed", error);
                }

                if (snapshot != null && snapshot.exists()) {
                    //noinspection unchecked
//                    Map<String, Object> changes = snapshot.getData();
                    Log.d(TAG, "Current data: " + snapshot.getData());

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

    }


}
