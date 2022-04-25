package edu.neu.madcourse.numad22sp_team5;

import static com.theartofdev.edmodo.cropper.CropImage.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.net.Uri;
import android.content.Intent;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Calendar;

import edu.neu.madcourse.numad22sp_team5.fragment.HomeFragment;

import java.util.ArrayList;

public class PhotoActivity extends AppCompatActivity {
    Uri imageUri;
    String myUrl = "";
    StorageTask uploadTask;
    StorageReference storageReference;

    ImageView close,image_added;
    TextView post;
    EditText description;
    TextView tag;
    String babyid;
    Date currentTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);


//        Bundle extras = getIntent().getExtras();
//        if(extras != null){
//            babyid = extras.getString("babyid");
//            Log.i("babyid from extras: %s", babyid);
//        }

        SharedPreferences babyPref = getSharedPreferences("babyInfo", MODE_PRIVATE);
        babyid = babyPref.getString("babyid","");




        close = findViewById(R.id.close);
        image_added = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);
        tag = findViewById(R.id.tag);

        storageReference = FirebaseStorage.getInstance().getReference("posts");
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("close", "close_onClick: ");

                //startActivity(new Intent(PhotoActivity.this, MainActivity.class));
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("post", "post_onClick: ");
                uploadImage();
            }
        });

        final int PIC_CROP = 1;

        Log.i("cropAct", "ddddd ");
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);


        Log.i("cropAct", "aaaaaa ");
        Log.i("cropAct", "bbb: ");
    }

        private String getFileExtension(Uri uri){
            Log.i("getFileExtension", "cccc: ");

            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getMimeTypeFromExtension(contentResolver.getType(uri));

        }

        private void uploadImage() {
            Log.i("uploadImage", "upload");
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Posting");

            progressDialog.show();

            if(imageUri != null){
                StorageReference filerefrence = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

                uploadTask = filerefrence.putFile(imageUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        Log.i("uploadImage", "success");
                        if(!task.isSuccessful()){
                            throw task.getException();

                        }
                        return filerefrence.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Uri downloadUri = (Uri) task.getResult();
                            myUrl = downloadUri.toString();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                            String postid = reference.push().getKey();
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                            LocalDateTime now = LocalDateTime.now();
                            String time = dtf.format(now);


                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("babyid",babyid);
                            hashMap.put("description",description.getText().toString());
                            hashMap.put("growth","");
                            hashMap.put("postid",postid);
                            hashMap.put("postImages",myUrl);
                            hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            hashMap.put("tag",tag.getText().toString());
                            hashMap.put("time",time);

                            if(tag.getText().toString().equals("")){
                                hashMap.put("postType","moments");
                            }else{
                                hashMap.put("postType","milestone");
                            }



                            HashMap<String,Object> postHash = new HashMap<>();
                            postHash.put(postid, hashMap);
                            reference.child(babyid).updateChildren(postHash);

                            // add notification branch in firebase
                            DatabaseReference reference_notify = FirebaseDatabase.getInstance().getReference("Notification");
                            String notificationId = reference_notify.push().getKey();
                            HashMap<String, Object> notifyMap = new HashMap<>();
                            notifyMap.put("postid", postid);
                            notifyMap.put("post publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            notifyMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            notifyMap.put("type", "post");
                            notifyMap.put("time", time);
                            notifyMap.put("description", description.getText().toString());
                            notifyMap.put("postImage", myUrl);
                            HashMap<String, Object> notifyHash = new HashMap<>();
                            notifyHash.put(notificationId, notifyMap);
                            reference_notify.child(babyid).updateChildren(notifyHash);

                            progressDialog.dismiss();

                            //startActivity(new Intent(PhotoActivity.this, MainActivity.class));
                            finish();

                        }else{
                            Toast.makeText(PhotoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PhotoActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
        }else{
                Toast.makeText(this,"No Image Selected",Toast.LENGTH_SHORT).show();
            }



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        Log.i("onAct", "onActivityResult: ");
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            image_added.setImageURI(imageUri);
        }else{
            Toast.makeText(this, "Something gone wrong!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PhotoActivity.this, MainActivity.class));

            finish();

        }
    }

    public void tagButton(View view){
        switch (view.getId()){
            case R.id.swim:
                tag.setText("1st Swimming");
                break;
            case R.id.walk:
                tag.setText("1st Walking");
                break;
            case R.id.medicine:
                tag.setText("1st Taking Medicine");
                break;
            case R.id.smile:
                tag.setText("1st Smiling");
                break;
            case R.id.flight:
                tag.setText("1st Taking Flight");
                break;
            case R.id.shower:
                tag.setText("1st Taking Shower");
                break;
            case R.id.nail:
                tag.setText("1st Clipping Nail");
                break;
            case R.id.ice:
                tag.setText("1st Eating Ice Cream");
                break;
            default:
                tag.setText("");
        }



    }

}