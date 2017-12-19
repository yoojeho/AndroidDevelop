package com.example.cjnote.ae4teamapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class userProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_FROM_ALBUM=0;
    private static final int PICK_FROM_CAMERA=1;
    private static final int CROP_FROM_IMAGE=2;
    private Uri mlmageCaptureUri;
    private ImageView iv_UserPhoto;
    private int id_view;
    private String absolutePath;
    private FirebaseAuth mAuth;
    private TextView nicknameTextView;
    private TextView emailTextView;
    private TextView contackTextView;
    private String TAG = "dbCheck";
    private CollectionReference userColRef = FirebaseFirestore.getInstance().collection("user");
    private RadioButton womanbutton;
    private RadioButton manbutton;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    StorageReference imagesRef = storageRef.child("images");
    StorageReference spaceRef = storageRef.child("images/space.jpg");
    StorageReference rootRef = spaceRef.getRoot();
    StorageReference earthRef = spaceRef.getParent().child("earth.jpg");
    StorageReference nullRef = spaceRef.getRoot().getParent();
    StorageReference mountainsRef = storageRef.child("mountains.jpg");
    StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");

    private View imageView;


    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mountainsRef.getName().equals(mountainImagesRef.getName());
        mountainsRef.getPath().equals(mountainImagesRef.getPath());

        imagesRef = spaceRef.getParent();
        womanbutton = (RadioButton) findViewById(R.id.womanButton);
        manbutton = (RadioButton) findViewById(R.id.manButton);
        mAuth = FirebaseAuth.getInstance();
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        iv_UserPhoto = (ImageView) this.findViewById(R.id.userProfileFicture);
        iv_UserPhoto.setOnClickListener(this);

        contackTextView = (TextView) findViewById(R.id.userContactInfoTextView);
        nicknameTextView = (TextView) findViewById(R.id.userNickNameTextView);
        emailTextView = (TextView) findViewById(R.id.userEmailTextView);

        if ((mAuth.getCurrentUser() != null)) {
            userGenderCheck();
        } else {
            Intent loginintent = new Intent(userProfileActivity.this, LoginActivity.class);
            startActivity(loginintent);
            finish();
        }
        findViewById(R.id.userContactInfoTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactinfointent = new Intent(userProfileActivity.this,  ContactInfoActivity.class);
                startActivity(contactinfointent);
                finish();
                overridePendingTransition(R.anim.rightin_activity,R.anim.leftout_activity);
            }
        });
        findViewById(R.id.womanButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGenderButton(R.id.womanButton);
            }
        });
        findViewById(R.id.manButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGenderButton(R.id.manButton);
            }
        });
    }

    public void changeGenderButton(int index) {
        FirebaseUser user = mAuth.getCurrentUser();
        Map<String, Object> userMap = new HashMap<>();
        switch (index) {
            case R.id.womanButton:
                userMap.put("gender", 0);
                break;
            case R.id.manButton:
                userMap.put("gender", 1);
                break;
        }
        userColRef.document(user.getUid())
                .update(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(userProfileActivity.this, "변경되었습니다", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public void userGenderCheck() {
        final FirebaseUser user = mAuth.getCurrentUser();

        userColRef.document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Map userInfo = task.getResult().getData();
                        if (userInfo.get("gender") != null) {
                           int gendercheck = Integer.parseInt(userInfo.get("gender").toString());

                            textViewSetting();

                            switch (gendercheck) {
                                case 0:
                                    womanbutton.setChecked(true);
                                    break;
                                case 1:
                                    manbutton.setChecked(true);
                                    break;
                            }
                        } else {
                            textViewSetting();
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                 }
        });
    }

    public void textViewSetting() {
        FirebaseUser user = mAuth.getCurrentUser();
        userColRef.document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        documentSnapshot.getData().get("nickname").toString();  //바로
                        Map snap = documentSnapshot.getData();
                        Users users = new Users(snap.get("email").toString(), snap.get("nickname").toString());

                        //텍스트뷰 세팅
                        nicknameTextView.setText(users.getNickname());

                        final SpannableStringBuilder emailsps = new SpannableStringBuilder("이메일\n"+users.getEmail());
                        emailsps.setSpan(new AbsoluteSizeSpan(43), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        emailsps.setSpan(new ForegroundColorSpan(Color.rgb(170,170,170)), 4, emailsps.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        emailsps.setSpan(new StyleSpan(Typeface.BOLD),0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        emailTextView.setText(emailsps);

                        final SpannableStringBuilder contactsps = new SpannableStringBuilder("연락처 정보\n휴대폰 번호, 카톡아이디");
                        contactsps.setSpan(new AbsoluteSizeSpan(43), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        contactsps.setSpan(new ForegroundColorSpan(Color.rgb(160,160,160)), 7, contactsps.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        contactsps.setSpan(new StyleSpan(Typeface.BOLD),0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        contackTextView.setText(contactsps);
                    }
                });
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
        int permissionCheck = ContextCompat.checkSelfPermission(userProfileActivity.this, Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(userProfileActivity.this, new String[]{Manifest.permission.CAMERA},0);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
            mlmageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,mlmageCaptureUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
            Log.i(TAG, "test1-camera");
        }
    }

    //앨범에서 사진 가져오기
    public void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
        Log.i(TAG, "test1-album");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == 0) {
                Toast.makeText(this, "카메라 권한이 승인됨", Toast.LENGTH_SHORT).show();
            } else {
                //권한 거절된 경우
                Toast.makeText(this, "카메라 권한이 거절 되었습니다. 카메라를 이용하려면 권한을 승낙하여야 합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, String.valueOf(requestCode));
        Log.i(TAG, String.valueOf(resultCode));
        if(resultCode != RESULT_OK) {
            return;
        }

        switch(requestCode) {
            case PICK_FROM_ALBUM:
            {
                mlmageCaptureUri = data.getData();
                Log.i(TAG, "test2");
            }

            case PICK_FROM_CAMERA:
            {
                Log.i(TAG, "test3");

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mlmageCaptureUri, "image/*");

                intent.putExtra("outputX", 100);
                intent.putExtra("outputY", 100);
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
                Log.i(TAG, "test4");
                final Bundle extras = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp/"+System.currentTimeMillis()+".jpg";

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
        File copyFile = new File(filePath);
        BufferedOutputStream out = null;
        Log.i(TAG, "test5");
//        imageupload();
        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Log.i(TAG, "test6");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));


            out.flush();
            out.close();
         } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void imageupload () {
//        Log.i(TAG, "imageupload");
//        imageView.setDrawingCacheEnabled(true);
//        imageView.buildDrawingCache();
//        Bitmap bitmap = imageView.getDrawingCache();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = mountainsRef.putBytes(data);
//        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
//        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
//        uploadTask = riversRef.putFile(file);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                Log.i(TAG, "dowloadUrl: " + downloadUrl);
//
//            }
//        });
//    }

}





