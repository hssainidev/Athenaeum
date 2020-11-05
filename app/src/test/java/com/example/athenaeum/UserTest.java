package com.example.athenaeum;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.junit.jupiter.api.Test;

public class UserTest {
    private UserDB mockDB() {
        return new UserDB();
    }
    private UserAuth mockAuth() {
        return new UserAuth();
    }

    @Test
    void testAdd() {
        UserAuth auth=mockAuth();
        UserDB db=mockDB();
        AthenaeumProfile profile=new AthenaeumProfile("user","password","dummy@gmail.com");
        Task<AuthResult> authentication=auth.addUser(profile);
        User user=new User(profile);
        db.addUser(user,authentication.getResult().getUser().getUid());

    }
}
