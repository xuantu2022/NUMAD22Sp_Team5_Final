package edu.neu.madcourse.numad22sp_team5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;

public class AddBabyActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Button next;
    private EditText nickname, dob;

    Uri imageUri;
    String myUri = "";
    StorageTask uploadTask;
    StorageReference storageReference;

    private ImageView headshot;
    private ProgressBar progressBar;

    private String gender_picked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_baby);

        next = findViewById(R.id.next);
        nickname = findViewById(R.id.nickname_input);
        dob = findViewById(R.id.dob_input);
        headshot = findViewById(R.id.headshot);
        progressBar = findViewById(R.id.progressBar);

        headshot.setImageResource(R.drawable.ic_add_items);


        storageReference = FirebaseStorage.getInstance().getReference("headshots");

        //disable keyboard when click dob
        dob.setInputType(InputType.TYPE_NULL);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialogue();
            }
        });

        headshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .start(AddBabyActivity.this);
            }
        });

        // click next button to go back to baby list activity
        //and save baby data and relationship to database


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname_str = nickname.getText().toString();
                String dob_str = dob.getText().toString();
                if (TextUtils.isEmpty(nickname_str) || TextUtils.isEmpty(dob_str)) {
                    Toast.makeText(AddBabyActivity.this, "All fields are required!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    addBaby(nickname_str, dob_str);
                }
            }
        });

    }

    private String getFileExtension (Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void addBaby(String nickname_str, String dob_str) {

        progressBar.setVisibility(View.VISIBLE);

        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "."
                    + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isComplete()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        myUri = downloadUri.toString();

                        DatabaseReference referenceBaby = FirebaseDatabase.getInstance().getReference("Babys");
                        DatabaseReference referenceFollows = FirebaseDatabase.getInstance().getReference("Follow");
                        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        String babyid = referenceBaby.push().getKey();

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("babyid", babyid);
                        map.put("nickname", nickname_str);
                        map.put("ownerid", userid);
                        map.put("dob", dob_str);
                        map.put("headshot", myUri);
                        map.put("gender", gender_picked);

                        //store baby data to database
                        referenceBaby.child(babyid).setValue(map);

                        //store baby and user relationship to database
                        /*
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        String time = dtf.format(now);*/

                        referenceFollows.child(userid).child(babyid).setValue(true);

                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(AddBabyActivity.this, BabyListActivity.class));
                        finish();
                    } else {
                        Toast.makeText(AddBabyActivity.this, "Save failed!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddBabyActivity.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(AddBabyActivity.this, "No image selected",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            headshot.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Something gone wring!", Toast.LENGTH_SHORT).show();
        }

    }



    private void showDatePickerDialogue() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    //get date picked from datePicker
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String date = i + "/" + (i1+1) + "/" + i2;
        dob.setText(date);
    }

    //get gender info from radio button
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (checked) {
            gender_picked = ((RadioButton) view).getText().toString();
        }
    }

}