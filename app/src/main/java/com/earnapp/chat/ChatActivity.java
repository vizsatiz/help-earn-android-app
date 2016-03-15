package com.earnapp.chat;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.earnapp.helpbucks.R;

public class ChatActivity extends AppCompatActivity {

    private EditText messageInput;
    private FloatingActionButton send_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        send_message = (FloatingActionButton) findViewById(R.id.send_chat_message);
        messageInput = (EditText) findViewById(R.id.chat_input_message);
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_message();
            }
        });


    }

    private void send_message() {

    }


}
