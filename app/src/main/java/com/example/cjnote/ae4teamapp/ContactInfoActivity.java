package com.example.cjnote.ae4teamapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RadioButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class ContactInfoActivity extends AppCompatActivity {
    private String TAG = "contactinfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        Intent userprofileIntent = new Intent(ContactInfoActivity.this,  userProfileActivity.class);
        startActivity(userprofileIntent);
        finish();
        overridePendingTransition(R.anim.leftin_activity,R.anim.rightout_activity);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent test3Intent = new Intent(ContactInfoActivity.this,  userProfileActivity.class);
                startActivity(test3Intent);
                finish();
                overridePendingTransition(R.anim.leftin_activity,R.anim.rightout_activity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
//    public void textViewSetting() {
//        Log.i(TAG, "textviewsetting");
//        FirebaseUser user = mAuth.getCurrentUser();
//        userColRef.document(user.getUid())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        documentSnapshot.getData().get("nickname").toString();  //바로
//                        Map snap = documentSnapshot.getData();
//                        Users users = new Users(snap.get("email").toString(), snap.get("nickname").toString(), snap.get("gender").toString());
//                        //getData단계에선 무슨형태임? 로그
//                        //특정하나가아니라 전체데이타는 못봄?
//
//
//                        //텍스트뷰 세팅
//                        nicknameTextView.setText(users.getNickname());
//
//                        final SpannableStringBuilder emailsps = new SpannableStringBuilder("이메일\n"+users.getEmail());
//                        emailsps.setSpan(new AbsoluteSizeSpan(43), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        emailsps.setSpan(new ForegroundColorSpan(Color.rgb(170,170,170)), 4, emailsps.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        emailsps.setSpan(new StyleSpan(Typeface.BOLD),0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        emailTextView.setText(emailsps);
//
//                        final SpannableStringBuilder contactsps = new SpannableStringBuilder("연락처 정보\n휴대폰 번호, 카톡아이디");
//                        contactsps.setSpan(new AbsoluteSizeSpan(43), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        contactsps.setSpan(new ForegroundColorSpan(Color.rgb(160,160,160)), 7, contactsps.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        contactsps.setSpan(new StyleSpan(Typeface.BOLD),0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        contackTextView.setText(contactsps);
//
//                        String gendercheck = users.getGender();
//
//                        final RadioButton womanbutton = (RadioButton) findViewById(R.id.womanButton);
//                        final RadioButton manbutton = (RadioButton) findViewById(R.id.manButton);
//                        switch (gendercheck) {
//                            case "woman":
//                                Log.d(TAG, "woman ture");
//                                womanbutton.setChecked(true);
//                                break;
//                            case "man":
//                                Log.d(TAG, "man setting");
//                                manbutton.setChecked(true);
//                                break;
//                        }
//                    }
//                });
//    }
}
