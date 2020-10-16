package com.example.libraryadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.libraryadmin.Details.PublishDetails;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class AddPublish extends AppCompatActivity {
        private static final int PERMISSION_CODE = 115;
        private static final int REQUEST_CODE_IMAGE_CAPTURE = 116 ;
        private static final int REQUEST_CODE_PICK_CAPTURE = 117 ;
        FirebaseDatabase database;
        DatabaseReference reference;
        ImageView imagePublishCreate;
        private Uri imageUri, uriDownload;
        ArrayList<String> listCategory;
        EditText edtPublishName,edtPublishCode, edtIntroPublish, edtPublishAddress, edtPhonePublish, edtFaxPublish,edtEmailPublish, edtWebsitePublish;
        String publishName,publishCode,  introPublish , publishAddress , phonePublish, faxPublish, emailPublish, websitePublish;
        private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_publish);
        setupViews();
    }
    private void setupViews() {
        database= FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        reference = database.getReference("Publish");
        imagePublishCreate = findViewById(R.id.imagePublishCreate);
        imagePublishCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRequestPermission();
                showMenuImages(v);
            }
        });
    }
    private  void  showMenuImages(View view){
        PopupMenu popupMenu = new PopupMenu(AddPublish.this, view);
        popupMenu.inflate(R.menu.menu_image);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuOpenCamera:
                        openCamera();
                        break;
                    case R.id.menuOpenGallery:
                        openGallery();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
    private void  checkRequestPermission(){
        if (Build.VERSION.SDK_INT> Build.VERSION_CODES.LOLLIPOP_MR1){
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permission ={ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
            else {
                Toast.makeText(AddPublish.this, getString(R.string.denied), Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(AddPublish.this, getString(R.string.denied), Toast.LENGTH_LONG).show();
        }
    }
    private  void openCamera(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_IMAGE_CAPTURE);
    }
    private void openGallery(){
        Intent intentPickGallery = new Intent(Intent.ACTION_PICK);
        intentPickGallery.setType("image/*");
        startActivityForResult(intentPickGallery, REQUEST_CODE_PICK_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== REQUEST_CODE_IMAGE_CAPTURE&& resultCode== RESULT_OK&& data!= null){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagePublishCreate.setImageBitmap(imageBitmap);

        }
        if (requestCode== REQUEST_CODE_PICK_CAPTURE&& resultCode== RESULT_OK&& data!= null){
            imageUri = data.getData();
            imagePublishCreate.setImageURI(imageUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSaveInformation:
                informationProcessing();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void informationProcessing() {
        edtPublishName = findViewById(R.id.edtPublishName);
        edtPublishCode = findViewById(R.id.edtPublishCode);
        edtIntroPublish = findViewById(R.id.edtIntroPublish);
        edtPublishAddress = findViewById(R.id.edtPublishAddress);
        edtPhonePublish = findViewById(R.id.edtPhonePublish);
        edtFaxPublish = findViewById(R.id.edtFaxPublish);
        edtEmailPublish = findViewById(R.id.edtEmailPublish);
        edtWebsitePublish = findViewById(R.id.edtWebsitePublish);

        publishName = edtPublishName.getText().toString().trim();
        publishCode = edtPublishCode.getText().toString().trim();
        introPublish = edtIntroPublish.getText().toString().trim();
        publishAddress = edtPublishAddress.getText().toString().trim();
        phonePublish = edtPhonePublish.getText().toString().trim();
        faxPublish = edtFaxPublish.getText().toString().trim();
        emailPublish = edtEmailPublish.getText().toString().trim();
        websitePublish = edtWebsitePublish.getText().toString().trim();
        if (publishName.isEmpty() || introPublish.isEmpty()
                || publishAddress.isEmpty()|| phonePublish.isEmpty()
                || faxPublish.isEmpty()|| emailPublish.isEmpty() || websitePublish.isEmpty()){
            Toast.makeText(AddPublish.this, getString(R.string.enter_full_information), Toast.LENGTH_LONG).show();
        }
        else {
            if (publishCode.length()<11){
                edtPublishName.setError(getString(R.string.request_code_format));
            }
            else {
//                new saveDataInformation().execute();
                checkExistPublish();

            }
        }


    }
    private void checkExistPublish() {
        progressDialog = new ProgressDialog(AddPublish.this);
        progressDialog.setMessage(getString(R.string.mess_save));
        progressDialog.show();
        Query query = reference.orderByChild("publishCode").equalTo(publishCode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    edtPublishCode.setError("Book already exists");
                    progressDialog.dismiss();
                }
                else {
                    saveInformation();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void saveInformation() {
        saveImageToStorageAndDownloadUri();
    }
    private void saveImageToStorageAndDownloadUri(){
//        String path = pathBookImageInStorage+bookCode+".png";
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://library-80e61.appspot.com");
        StorageReference storageRef = storage.getReference();
// Create a reference to "mountains.jpg"
        final StorageReference mountainsRef = storageRef.child("image/"+publishCode+".jpg");
        imagePublishCreate.setDrawingCacheEnabled(true);
        imagePublishCreate.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imagePublishCreate.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] data = baos.toByteArray();
        final UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }
                        return mountainsRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            uriDownload = task.getResult();
                            addDataToFireBaseDataBase();
                        } else {
                            Toast.makeText(AddPublish.this, "Download uri fail",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    }
                });
            }
        });
    }
    private void addDataToFireBaseDataBase( ){
//        FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
//        DatabaseReference myRef = database.getReference("Books");
        PublishDetails publishDetails = new PublishDetails(publishName, publishCode, introPublish, publishAddress, phonePublish, faxPublish, emailPublish, websitePublish, uriDownload.toString());
//        FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
//        DatabaseReference myRef = database.getReference("Authors");
        reference.child(publishCode).setValue(publishDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    clearInformationWhenSuccess();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddPublish.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void clearInformationWhenSuccess(){
        edtPublishName.setText("");
        edtPublishCode.setText("");
        edtIntroPublish.setText("");
        edtPublishAddress.setText("");
        edtPhonePublish.setText("");
        edtEmailPublish.setText("");
        edtFaxPublish.setText("");
        edtWebsitePublish.setText("");

        imagePublishCreate.setImageResource(R.drawable.agency);
    }
}
