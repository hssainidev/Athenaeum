package com.example.athenaeum;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.api.Distribution;

import java.util.regex.Pattern;

public class EditProfileFragment extends DialogFragment{

    private EditText editName;
    private EditText editPhoneNum;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Bundle bundle);
    }

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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_profile_layout, null);
        editName = view.findViewById(R.id.edit_name);
        editPhoneNum = view.findViewById(R.id.edit_phoneNum);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Profile")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editName.getText().toString();
                        String phoneNum = editPhoneNum.getText().toString();

                        if(name == ""){
                            editName.setError("Name cannot be blank");
                            return;
                        }

                        if(phoneNum == ""){
                            editPhoneNum.setError("Phone number cannot be blank");
                            return;
                        }

                        if(Pattern.matches("0-9", phoneNum)){
                            editPhoneNum.setError("Phone number cannot contain letters or symbols");
                            return;
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        bundle.putString("phoneNum", phoneNum);

                        listener.onOkPressed(bundle);
                    }
                }).create();
    }
}
