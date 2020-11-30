/*
 * EditProfileFragment
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.regex.Pattern;

/**
 * This dialog fragment is used to get new strings to edit an AthenaeumProfile.
 */
public class EditProfileFragment extends DialogFragment{

    private EditText editName;
    private EditText editPhoneNum;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Bundle bundle);
    }

    //ensure the creating activity can handle onOkPressed()
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //get the view and EditText fields
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_profile_layout, null);
        editName = view.findViewById(R.id.edit_name);
        editPhoneNum = view.findViewById(R.id.edit_phoneNum);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Profile")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    //when OK is pressed
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //take strings from EditText fields
                        String name = editName.getText().toString();
                        String phoneNum = editPhoneNum.getText().toString();

                        //Ensure name field is not blank, otherwise close without saving changes
                        if(name == ""){
                            editName.setError("Name cannot be blank");
                            return;
                        }

                        //Ensure phone number field is not blank, otherwise close without saving
                        //changes
                        if(phoneNum == ""){
                            editPhoneNum.setError("Phone number cannot be blank");
                            return;
                        }

                        //Ensure phone number field only contains numbers, otherwise close without
                        //saving changes
                        if(Pattern.matches("0-9", phoneNum)){
                            editPhoneNum.setError("Phone number cannot contain letters or symbols");
                            return;
                        }

                        //If validation tests are passed, add new strings to a bundle and pass it
                        //back to the calling activity to be handled.
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        bundle.putString("phoneNum", phoneNum);

                        listener.onOkPressed(bundle);
                    }
                }).create();
    }
}
