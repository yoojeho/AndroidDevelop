package com.example.cjnote.ae4teamapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.cjnote.ae4teamapp.R.id.addServiceButton;
import static com.example.cjnote.ae4teamapp.R.id.changeNumberButton;
import static com.example.cjnote.ae4teamapp.R.id.logOutButton;
import static com.example.cjnote.ae4teamapp.R.id.noticeButton;
import static com.example.cjnote.ae4teamapp.R.id.storeIntroButton;
import static com.example.cjnote.ae4teamapp.R.id.storeRegisterButton;
import static com.example.cjnote.ae4teamapp.R.id.storeSelectButton;
import static com.example.cjnote.ae4teamapp.R.id.thunderScreenButon;
import static com.example.cjnote.ae4teamapp.R.id.userProfileButton;
import static com.example.cjnote.ae4teamapp.R.id.withdrawalButton;

public class Test3Activity extends AppCompatActivity {
    ActionBar actionBar;
    final Context context = this;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        findViewById(storeIntroButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(storeSelectButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(storeRegisterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(userProfileButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userProFileIntent = new Intent(Test3Activity.this,  userProfileActivity.class);
                startActivity(userProFileIntent);
                finish();
                overridePendingTransition(R.anim.rightin_activity,R.anim.leftout_activity);
            }
        });

        findViewById(changeNumberButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(logOutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutClick();
            }
        });

        findViewById(thunderScreenButon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(noticeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(addServiceButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(withdrawalButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (mAuth.getCurrentUser() == null) {
            Intent loginintent = new Intent(Test3Activity.this, LoginActivity.class);
            startActivity(loginintent);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        Intent loginIntent = new Intent(Test3Activity.this,  LoginActivity.class);
        startActivity(loginIntent);
        finish();
        overridePendingTransition(R.anim.leftin_activity,R.anim.rightout_activity);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent loginIntent = new Intent(Test3Activity.this,  LoginActivity.class);
                startActivity(loginIntent);
                finish();
                overridePendingTransition(R.anim.leftin_activity,R.anim.rightout_activity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    signOut();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked

                    break;
            }
        }
    };

    public void logoutClick() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("로그아웃");
        alertDialogBuilder.setMessage("로그아웃 시 30일 이상 경과된 번개톡 대화내용은 모두 삭제됩니다.\n\n로그아웃 하시겠습니까?");
        alertDialogBuilder.setPositiveButton("확인", dialogClickListener).setNegativeButton("취소", dialogClickListener).show();
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(Test3Activity.this, "로그아웃 되었습니다", Toast.LENGTH_LONG).show();
        Intent loginIntent = new Intent(Test3Activity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
