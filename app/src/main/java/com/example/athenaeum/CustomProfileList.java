/*
 * CustomProfileList
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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This custom ArrayAdapter is used to display AthenaeumProfiles. It has an ArrayList of profiles
 * and a Context for the activity it was constructed in.
 */
public class CustomProfileList extends ArrayAdapter<AthenaeumProfile> {
    private ArrayList<AthenaeumProfile> profiles;
    private Context context;

    /**
     * This constructs a new CustomProfileList with the context of the parent activity and
     * a list of profiles to be displayed.
     *
     * @param context The Context of the parent activity.
     * @param profiles An ArrayList of AthenaeumProfiles to be displayed.
     */
    public CustomProfileList(Context context, ArrayList<AthenaeumProfile> profiles) {
        super(context, 0, profiles);
        this.profiles = profiles;
        this.context = context;
    }

    // Manually sets up view for displaying profiles
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.profile_list_content, parent, false);
        }

        AthenaeumProfile profile = profiles.get(position);

        TextView username = view.findViewById(R.id.search_username);
        TextView name = view.findViewById(R.id.search_name);

        username.setText(profile.getUsername());
        name.setText(profile.getName());

        return view;
    }
}
