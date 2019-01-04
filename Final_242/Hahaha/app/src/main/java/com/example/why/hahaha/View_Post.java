package com.example.why.hahaha;

import android.app.Fragment;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.text.ClipboardManager;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.appevents.internal.Constants;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Why on 04/12/2017.
 */

public class View_Post extends Fragment {
    View myView;
    private FirebaseListAdapter<Comment> adapter;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.post, container, false);
        String title = "Title", question = "Question Contents", post_no="" ;

        Post post;
        if(getArguments()!= null){
            post = getArguments().getParcelable("post");
            title = post.Title;
            question = post.Question;
            post_no = post.post_no;
        }

        TextView q_title = (TextView)myView.findViewById(R.id.question_title);
        TextView q_content = (TextView)myView.findViewById(R.id.question_content);

        q_title.setText(title);
        q_content.setText(question);

        FloatingActionButton fab =
                (FloatingActionButton)myView.findViewById(R.id.fab);





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)myView.findViewById(R.id.input);


                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push().setValue(
                        new Comment(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );

                // Clear the input
                input.setText("");
            }
        });

        ImageButton add_img = (ImageButton) myView.findViewById(R.id.add_img);
        add_img.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(final View view) {

                final View popupView = getLayoutInflater().inflate(R.layout.popup_addimg, null);

                // create the popup window
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT, focusable);
                // show the popup window
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                final EditText send_img_url= (EditText) popupView.findViewById(R.id.send_img_url);
                        Button paste = (Button) popupView.findViewById(R.id.paste);
                        paste.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                                send_img_url.setText(clipboard.getText().toString());
                            }
                        });

                        Button send_img = (Button) popupView.findViewById(R.id.send_img);
                        send_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = send_img_url.getText().toString();
                                Comment img = new Comment(url,
                                        FirebaseAuth.getInstance()
                                                .getCurrentUser()
                                                .getDisplayName());
                                img.img_url = url;
                                FirebaseDatabase.getInstance()
                                        .getReference()
                                        .push()
                                        .setValue(img);
                                popupWindow.dismiss();
                            }
                        });

            }});


        displayChatMessages();


        myView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                    //myView = inflater.inflate(R.layout.message, container, false);
                    Log.e("down", "called");
                    /*RelativeLayout x = (RelativeLayout)myView.findViewById(R.id.questopn_title_content);
                    x.setVisibility(View.INVISIBLE);*/
                        getFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, new Create_post())
                                .commit();
                    return true;
                }
                return false;
            }
        });

        return myView;
    }

    private void displayChatMessages() {
        ListView listOfMessages = (ListView)myView.findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<Comment>(this.getActivity(),Comment.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void populateView(View v, final Comment model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);
                final ImageView img = (ImageView) v.findViewById(R.id.img);

                Log.e("text", model.getText());
                // Set their text
                messageText.setText(model.getText());
                messageUser.setText(model.getUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getTime()));

                final String url = model.img_url;
                Glide.with(getContext())
                        .load(url)
                        .override(300, 300)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(img);

                img.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        final View popupView = getLayoutInflater().inflate(R.layout.popup_img, null);

                        // create the popup window
                        boolean focusable = true; // lets taps outside the popup also dismiss it
                        final PopupWindow popupWindow = new PopupWindow(popupView,
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT, focusable);
                        // show the popup window
                        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                        ImageView img_big = (ImageView)  popupView.findViewById(R.id.img_big) ;
                        //your logic for double click action
                        Glide.with(getContext())
                                .load(url)
                                .override(600, 600)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(img_big);
                    }});
            }
        };

        listOfMessages.setAdapter(adapter);
    }
}




/*final GestureDetector gesture = new GestureDetector(getActivity(),
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                    float velocityY) {
                    Log.i(Constants.APP_TAG, "onFling has been called!");
                    final int SWIPE_MIN_DISTANCE = 120;
                    final int SWIPE_MAX_OFF_PATH = 250;
                    final int SWIPE_THRESHOLD_VELOCITY = 200;
                    try {
                        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                            return false;
                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.i(Constants.APP_TAG, "Right to Left");
                        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.i(Constants.APP_TAG, "Left to Right");
                        }
                    } catch (Exception e) {
                        // nothing
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });*/
