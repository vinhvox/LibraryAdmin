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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.libraryadmin.Details.AuthorDetail;
import com.example.libraryadmin.Details.BookDetail;
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

public class AddAuthor extends AppCompatActivity {
    private static final int PERMISSION_CODE = 115;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 116 ;
    private static final int REQUEST_CODE_PICK_CAPTURE = 117 ;
    FirebaseDatabase database;
    DatabaseReference reference;
    ImageView imageAuthorCreate;
    private Uri imageUri, uriDownload;
    ArrayList<String> listCategory;
    EditText edtPseudonym, edtAuthorName, edtAuthorCodeCreate, edtNationOfAuthor, edtBirthDayOfAuthor,edtArtwork;
    String valueCategory, pseudonym , authorName , authorCode, nationOfAuthor, birthDayOfAuthor, artwork;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_author);
        setupViews();
    }
    private void setupViews() {
        database= FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        reference = database.getReference("Books");
        setupCategory();

        imageAuthorCreate = findViewById(R.id.imageAuthorCreate);
        imageAuthorCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRequestPermission();
                showMenuImages(v);
            }
        });
    }
    private  void  showMenuImages(View view){
        PopupMenu popupMenu = new PopupMenu(AddAuthor.this, view);
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
                Toast.makeText(AddAuthor.this, getString(R.string.denied), Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(AddAuthor.this, getString(R.string.denied), Toast.LENGTH_LONG).show();
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
            imageAuthorCreate.setImageBitmap(imageBitmap);

        }
        if (requestCode== REQUEST_CODE_PICK_CAPTURE&& resultCode== RESULT_OK&& data!= null){
            imageUri = data.getData();
            imageAuthorCreate.setImageURI(imageUri);
        }
    }
    private  void  setupCategory(){
        listCategory  = new ArrayList<>();
        listCategory.add(getString(R.string.literary));
        listCategory.add(getString(R.string.psychological_life_skills));
        listCategory.add(getString(R.string.foreign_language_books));
        listCategory.add(getString(R.string.education_program));
        listCategory.add(getString(R.string.social_science));
        Spinner spinner = findViewById(R.id.spinnerCategory);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddAuthor.this, android.R.layout.simple_list_item_1, listCategory);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueCategory = listCategory.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        edtPseudonym = findViewById(R.id.edtPseudonym);
        edtAuthorName = findViewById(R.id.edtAuthorName);
        edtAuthorCodeCreate = findViewById(R.id.edtAuthorCodeCreate);
        edtNationOfAuthor = findViewById(R.id.edtNationOfAuthor);
        edtBirthDayOfAuthor = findViewById(R.id.edtBirthDayOfAuthor);
        edtArtwork = findViewById(R.id.edtArtwork);

        pseudonym = edtPseudonym.getText().toString().trim();
        authorName = edtAuthorName.getText().toString().trim();
        authorCode = edtAuthorCodeCreate.getText().toString().trim();
        nationOfAuthor = edtNationOfAuthor.getText().toString().trim();
        birthDayOfAuthor = edtBirthDayOfAuthor.getText().toString().trim();
        artwork = edtArtwork.getText().toString().trim();
        if (pseudonym.isEmpty() || authorName.isEmpty()
                || authorCode.isEmpty()|| nationOfAuthor.isEmpty()
                || birthDayOfAuthor.isEmpty()|| artwork.isEmpty()){
            Toast.makeText(AddAuthor.this, getString(R.string.enter_full_information), Toast.LENGTH_LONG).show();
        }
        else {
            if (authorCode.length()<11){
                edtAuthorCodeCreate.setError(getString(R.string.request_code_format));
            }
            else {
//                new saveDataInformation().execute();
                checkExistAuthor();

            }
        }


    }
    private void checkExistAuthor() {
        progressDialog = new ProgressDialog(AddAuthor.this);
        progressDialog.setMessage(getString(R.string.mess_save));
        progressDialog.show();
        Query query = reference.orderByChild("authorCode").equalTo(authorCode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    edtAuthorCodeCreate.setError("Book already exists");
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
        final StorageReference mountainsRef = storageRef.child("image/"+authorCode+".jpg");
        imageAuthorCreate.setDrawingCacheEnabled(true);
        imageAuthorCreate.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageAuthorCreate.getDrawable()).getBitmap();
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
                            Toast.makeText(AddAuthor.this, "Download uri fail",Toast.LENGTH_LONG).show();
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
        AuthorDetail authorDetail = new AuthorDetail(pseudonym, authorName, authorCode, nationOfAuthor, birthDayOfAuthor,valueCategory, artwork, uriDownload.toString());
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        DatabaseReference myRef = database.getReference("Authors");
        myRef.child(authorCode).setValue(authorDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                Toast.makeText(AddAuthor.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void clearInformationWhenSuccess(){
        edtPseudonym.setText("");
        edtAuthorName.setText("");
        edtAuthorCodeCreate.setText("");
        edtArtwork.setText("");
        edtNationOfAuthor.setText("");
        edtBirthDayOfAuthor.setText("");

        imageAuthorCreate.setImageResource(R.drawable.writer);
    }
}