package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.text.AutoText;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements EditProfileFragment.OnFragmentInteractionListener{

    TextView title;
    TextView username;
    TextView name;
    TextView phoneNum;
    TextView email;
    UserDB db = new UserDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.profile_toolbar);

        username = findViewById(R.id.profile_username);
        name = findViewById(R.id.profile_name);
        phoneNum = findViewById(R.id.profile_phoneNum);
        email = findViewById(R.id.profile_email);

        AthenaeumProfile profile = (AthenaeumProfile) getIntent().getExtras().getSerializable("profile");

        username.setText(profile.getUsername());
        name.setText(profile.getName());
        phoneNum.setText(profile.getPhoneNum());
        email.setText(profile.getEmail());

        Button editButton = findViewById(R.id.profile_edit_button);

        if(getIntent().getExtras().getBoolean("fromSearch")){
            editButton.setVisibility(View.GONE);
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new EditProfileFragment();
                fragment.show(getSupportFragmentManager(), "EDIT_PROFILE");
            }
        });

        SearchView searchView = (SearchView) findViewById(R.id.profile_search);

        final ListView listView = findViewById(R.id.profile_search_results);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ConstraintLayout layout = findViewById(R.id.user_info_layout);
                if(hasFocus){
                    listView.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.GONE);
                } else {
                    listView.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<AthenaeumProfile> profileArrayList = db.searchUsers(query);
                ArrayAdapter<AthenaeumProfile> profileArrayAdapter = new CustomProfileList(ProfileActivity.this, profileArrayList);
                listView.setAdapter(profileArrayAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AthenaeumProfile profile = (AthenaeumProfile) parent.getAdapter().getItem(position);
                        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("profile", profile);
                        bundle.putBoolean("fromSearch", true);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onOkPressed(Bundle bundle) {
        User user = (User) getIntent().getExtras().getSerializable("user");
        user.getProfile().setName(bundle.getString("name"));
        user.getProfile().setPhoneNum(bundle.getString("phoneNum"));
        db.addUser(user, getIntent().getExtras().getString("UID"));
        name.setText(user.getProfile().getName());
        phoneNum.setText(user.getProfile().getPhoneNum());
    }
}