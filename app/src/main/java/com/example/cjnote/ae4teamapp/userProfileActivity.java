package com.example.cjnote.ae4teamapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class userProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_FROM_CAMERA=0;
    private static final int PICK_FROM_ALBUM=1;
    private static final int CROP_FROM_IMAGE=2;
    private Uri mlmageCaptureUri;
    private ImageView iv_UserPhoto;
    private int id_view;
    private String absolutePath;
    private FirebaseAuth mAuth;
    private TextView nicknameTextView;
    private TextView emailTextView;
    private TextView contackTextView;

    protected void onCreate(Bundle savedInstanceState) {

        ActionBar actionBar;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        iv_UserPhoto = (ImageView) this.findViewById(R.id.userProfileFicture);
//        Button uploadPictureButton = (Button) this.findViewById(R.id.uploadPictureButton);

        iv_UserPhoto.setOnClickListener(this);
//        uploadPictureButton.setOnClickListener(this);


        nicknameTextView = (TextView)findViewById(R.id.userNickNameTextView);
        contackTextView = (TextView)findViewById(R.id.userContactInfoTextView);
        emailTextView = (TextView)findViewById(R.id.userEmailTextView);

        //텍스트뷰 세팅
        if (mAuth.getCurrentUser() != null) {
            nicknameTextView.setText(mAuth.getCurrentUser().getDisplayName());

            final SpannableStringBuilder emailsps = new SpannableStringBuilder("이메일\n"+mAuth.getCurrentUser().getEmail());
            emailsps.setSpan(new AbsoluteSizeSpan(43), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            emailsps.setSpan(new ForegroundColorSpan(Color.rgb(170,170,170)), 4, emailsps.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            emailsps.setSpan(new StyleSpan(Typeface.BOLD),0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            emailTextView.setText(emailsps);

            final SpannableStringBuilder contactsps = new SpannableStringBuilder("연락처 정보\n휴대폰 번호, 카톡아이디");
            contactsps.setSpan(new AbsoluteSizeSpan(43), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            contactsps.setSpan(new ForegroundColorSpan(Color.rgb(160,160,160)), 7, contactsps.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            contactsps.setSpan(new StyleSpan(Typeface.BOLD),0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            contackTextView.setText(contactsps);
        } else {
            nicknameTextView.setText("닉네임을 등록해주세요");

            final SpannableStringBuilder emailsps = new SpannableStringBuilder("이메일\n"+"이메일을 등록해주세요");
            emailsps.setSpan(new AbsoluteSizeSpan(43), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            emailsps.setSpan(new ForegroundColorSpan(Color.rgb(200,200,200)), 4, emailsps.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            emailsps.setSpan(new StyleSpan(Typeface.BOLD),0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            emailTextView.setText(emailsps);

            final SpannableStringBuilder contactsps = new SpannableStringBuilder("연락처 정보\n휴대폰 번호, 카톡아이디");
            contactsps.setSpan(new AbsoluteSizeSpan(43), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            contactsps.setSpan(new ForegroundColorSpan(Color.rgb(200,200,200)), 7, contactsps.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            contactsps.setSpan(new StyleSpan(Typeface.BOLD),0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            contackTextView.setText(contactsps);
        }

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

    @Override
    public void onClick(View v) {
        id_view = v.getId();
        Log.i("onCLick", "test");
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakePhotoAction();
            }
        };
        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakeAlbumAction();
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        new AlertDialog.Builder(this)
                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("사진촬영", cameraListener)
                .setNeutralButton("앨범선택", albumListener)
                .setNegativeButton("취소", cancelListener)
                .show();

    }

    //카메라로 사진 촬영하기
    public void doTakePhotoAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mlmageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,mlmageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    //앨범에서 사진 가져오기
    public void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) {
            return;
        }

        switch(requestCode) {
            case PICK_FROM_ALBUM:
            {
                mlmageCaptureUri = data.getData();

            }

            case PICK_FROM_CAMERA:
            {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mlmageCaptureUri, "image/*");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;
            }

            case CROP_FROM_IMAGE:
            {
                if(resultCode != RESULT_OK) {
                    return;
                }

                final Bundle extras = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmartWheel/"+System.currentTimeMillis()+".jpg";

                if(extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    iv_UserPhoto.setImageBitmap(photo);
                    storeCropImage(photo, filePath);
                    absolutePath = filePath;
                    break;
                }

                //임시파일 삭제
                File f = new File(mlmageCaptureUri.getPath());
                if(f.exists()) {
                    f.delete();
                }
                break;
            }
        }
    }

    private void storeCropImage(Bitmap bitmap, String filePath) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmartWheel";
        File directory_SmartWheel = new File(dirPath);
        if(!directory_SmartWheel.exists()){
            directory_SmartWheel.mkdir();
        }
        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));

            out.flush();
            out.close();
         } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
