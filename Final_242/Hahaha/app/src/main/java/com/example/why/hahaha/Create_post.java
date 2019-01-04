package com.example.why.hahaha;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Why on 04/12/2017.
 */

public class Create_post extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.new_post, container, false);



        Button publish = (Button)myView.findViewById(R.id.post_publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText post_title = (EditText)myView.findViewById(R.id.post_title);
                EditText post_content = (EditText)myView.findViewById(R.id.post_content);

                Integer post_no = ((final_242) getActivity().getApplication()).getPost_no();
                ((final_242) getActivity().getApplication()).incrementPost_no();

                Post p = new Post(post_title.getText().toString(),
                        post_content.getText().toString(),
                        FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getDisplayName(),
                                post_no);
                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database

                String path = "Post/"+post_no;
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(p);

                // Back to Post
                Bundle bundle = new Bundle();
                bundle.putParcelable("post", p);
                View_Post p_= new View_Post();
                p_.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, p_)
                        .commit();
            }
        });
        return myView;
    }

}
