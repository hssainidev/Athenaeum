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

public class CustomProfileList extends ArrayAdapter<AthenaeumProfile> {
    private ArrayList<AthenaeumProfile> profiles;
    private Context context;

    public CustomProfileList(Context context, ArrayList<AthenaeumProfile> profiles) {
        super(context, 0, profiles);
        this.profiles = profiles;
        this.context = context;
    }

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
