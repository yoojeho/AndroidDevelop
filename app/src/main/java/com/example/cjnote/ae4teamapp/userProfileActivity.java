package com.example.cjnote.ae4teamapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class userProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActionBar actionBar;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        Intent test3Intent = new Intent(userProfileActivity.this,  Test3Activity.class);
        startActivity(test3Intent);
        finish();
        overridePendingTransition(R.anim.leftin_activity,R.anim.rightout_activity);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent test3Intent = new Intent(userProfileActivity.this,  Test3Activity.class);
                startActivity(test3Intent);
                finish();
                overridePendingTransition(R.anim.leftin_activity,R.anim.rightout_activity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
