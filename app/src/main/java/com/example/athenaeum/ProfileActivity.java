/*
 * ProfileActivity
 *
 * November 30 2020
 *
 * Copyright 2020 Natalie Iwaniuk, Harpreet Saini, Jack Gray, Jorge Marquez Peralta, Ramana Vasanthan, Sree Nidhi Thanneeru
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.athenaeum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

/**
 * This activity shows profile data and allows searching for other user profiles.
 */
public class ProfileActivity extends AppCompatActivity implements EditProfileFragment.OnFragmentInteractionListener{

    private TextView title;
    private TextView name;
    private TextView phoneNum;
    private final UserDB db = new UserDB();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get activity toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.profile_toolbar);

        //get fields for displaying profile data
        TextView username = findViewById(R.id.profile_username);
        name = findViewById(R.id.profile_name);
        phoneNum = findViewById(R.id.profile_phoneNum);
        TextView email = findViewById(R.id.profile_email);

        //get profile from bundle stored in intent
        AthenaeumProfile profile = (AthenaeumProfile) getIntent().getExtras().getSerializable("profile");

        //set profile data
        username.setText(profile.getUsername());
        name.setText(profile.getName());
        phoneNum.setText(profile.getPhoneNum());
        email.setText(profile.getEmail());

        //get edit button
        Button editButton = findViewById(R.id.profile_edit_button);

        //if the profile activity was opened by searching for a profile, remove edit button
        if(getIntent().getExtras().getBoolean("fromSearch")){
            editButton.setVisibility(View.GONE);
        }

        //set up listener that launches an EditProfileFragment for editing profile data
        editButton.setOnClickListener(v -> {
            DialogFragment fragment = new EditProfileFragment();
            fragment.show(getSupportFragmentManager(), "EDIT_PROFILE");
        });

        //get search bar
        SearchView searchView = findViewById(R.id.profile_search);

        //get ListView for displaying search results
        final ListView listView = findViewById(R.id.profile_search_results);

        //when search bar is focused, remove profile data layout and replace with search results ListView
        //return to default when no longer focused
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            ConstraintLayout layout = findViewById(R.id.user_info_layout);
            if(hasFocus){
                listView.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
            } else {
                listView.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        });

        //listener that deals with searches when a user presses submit in the search bar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //return a list of profiles where username or name contain the query string
                //and displays them in a ListView
                ArrayList<AthenaeumProfile> profileArrayList = db.searchUsers(query);
                ArrayAdapter<AthenaeumProfile> profileArrayAdapter = new CustomProfileList(ProfileActivity.this, profileArrayList);
                listView.setAdapter(profileArrayAdapter);

                //when user clicks a profile represented in the ListView, launch a new ProfileActivity
                //displaying that profile's data
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    AthenaeumProfile profile1 = (AthenaeumProfile) parent.getAdapter().getItem(position);
                    Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("profile", profile1);
                    bundle.putBoolean("fromSearch", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //when user hits back button, finish activity
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    /**
     * When user finishes EditProfileFragment by pressing ok, update user in the firestore db
     * and update the data displayed in the ProfileActivity.
     * @param bundle A Bundle containing the strings to be used to update.
     */
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