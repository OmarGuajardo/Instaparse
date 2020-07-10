package com.example.instaparse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CommentDialog extends DialogFragment {
    public String TAG = "ComposeDialog";
    public TextInputLayout etComposeBodyContainer;
    public TextInputEditText etComposeBody;
    public onSubmitListener listener;
    public Button btnTweet;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view  = inflater.inflate(R.layout.comment_layout,null);

        builder.setView(view);

        etComposeBody = view.findViewById(R.id.etComposeBody);
        etComposeBodyContainer = view.findViewById(R.id.etComposeBodyContainer);
        btnTweet = view.findViewById(R.id.btnTweet);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.submitComment(etComposeBody.getText().toString());
            }
        });



        return builder.create();
    };


    //Attaches the function from TimeLineActivity to this Fragment
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (onSubmitListener)context;
        } catch (Exception e) {
            Log.e("ComposeDialog", "compose dialog error : ", e);
        }
    }

    public interface onSubmitListener{
        void submitComment(String body);
    }
}
