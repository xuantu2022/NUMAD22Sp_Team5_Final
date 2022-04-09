package edu.neu.madcourse.numad22sp_team5;

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
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.os.Build;
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

import java.util.ArrayList;
import java.util.HashMap;

import edu.neu.madcourse.numad22sp_team5.fragment.HomeFragment;

public class PhotoActivity extends AppCompatActivity {
//    RecyclerView recyclerView;
//    Button add;
//    TextView textView;

//    ArrayList<Uri> uri = new ArrayList<>();
//    PhotoRecyclerAdapter adapter;
//
//    private static final int Read_Permission = 101;
    Uri imageUri;
    String myUrl = "";
    StorageTask uploadTask;
    StorageReference storageReference;

    ImageView close,image_added;
    TextView post;
    EditText description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        close = findViewById(R.id.close);
        image_added = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);

        storageReference = FirebaseStorage.getInstance().getReference("posts");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PhotoActivity.this, MainActivity.class));
                finish();

            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        CropImage.activity()
                .setAspectRatio(1, 1)
                .start(PhotoActivity.this);
    }

        private String getFileExtension(Uri uri){

            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getMimeTypeFromExtension(contentResolver.getType(uri));

        }

        private void uploadImage() {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Posting");

            progressDialog.show();

            if(imageUri != null){
                StorageReference filerefrence = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

                uploadTask = filerefrence.putFile(imageUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
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

                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("postid",postid);
                            hashMap.put("postimage",myUrl);
                            hashMap.put("description",description.getText().toString());
                            hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                            reference.child(postid).setValue(hashMap);

                            progressDialog.dismiss();

                            startActivity(new Intent(PhotoActivity.this,MainActivity.class));
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









//        recyclerView = findViewById(R.id.recyclerView_Gallery_Images);
//        add = findViewById(R.id.btn_add);
//        textView = findViewById(R.id.totalPhotos);

//        adapter = new PhotoRecyclerAdapter(uri);
//        recyclerView.setLayoutManager(new GridLayoutManager(PhotoActivity.this,3));
//        recyclerView.setAdapter(adapter);
//
//        if(ContextCompat.checkSelfPermission(PhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//
//            ActivityCompat.requestPermissions(PhotoActivity.this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);
//        }
//
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
//                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
//                }
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
//
//
//            }
//        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
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
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
//        super.onActivityResult(requestCode,resultCode,data);
//        if(requestCode == 1 && resultCode==Activity.RESULT_OK){
//            if(data.getClipData() != null){
//                int x = data.getClipData().getItemCount();
//
//                for(int i=0;i<x;i++){
//                    uri.add(data.getClipData().getItemAt(i).getUri());
//                }
//                adapter.notifyDataSetChanged();
////                textView.setText("Photos ("+uri.size()+")");
//            }else if(data.getData() != null){
//                String imageURL = data.getData().getPath();
//                uri.add(Uri.parse(imageURL));
//
//            }
//        }
//
//    }
}