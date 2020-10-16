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

import com.example.libraryadmin.Details.AccountEmployeeDetail;
import com.example.libraryadmin.Details.BookDetail;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AddBook extends AppCompatActivity {
    private static final int PERMISSION_CODE =141 ;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 142;
    private static final int REQUEST_CODE_PICK_CAPTURE =143;
    ImageView imageBookCreate;
    private Uri imageUri, uriDownload;
    ArrayList<String> listCategory;
    FirebaseDatabase database;
    DatabaseReference reference;
    String valueCategory;
    EditText edtBookName, edtBookNation, edtBookLaguage, edtBookCode, edtPublishingCompanyCode, edtDateOfPublication, edtNumbersOfPage, edtIntroBook, edtAthorCode;
    String name, nation, language, code, publishingCompanyCode, dateOfPublication, numbersOfPage, intro, authorCode;
    private ProgressDialog progressDialog;
    private String dateCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        setupViews();
    }

    private void setupViews() {
        database= FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        reference = database.getReference("Books");
        setupCategory();

        imageBookCreate = findViewById(R.id.imageBookCreate);
        imageBookCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRequestPermission();
                showMenuImages(v);
            }
        });
    }
    private  void  showMenuImages(View view){
        PopupMenu popupMenu = new PopupMenu(AddBook.this, view);
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
                Toast.makeText(AddBook.this, getString(R.string.denied), Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(AddBook.this, getString(R.string.denied), Toast.LENGTH_LONG).show();
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
            imageBookCreate.setImageBitmap(imageBitmap);

        }
        if (requestCode== REQUEST_CODE_PICK_CAPTURE&& resultCode== RESULT_OK&& data!= null){
            imageUri = data.getData();
            imageBookCreate.setImageURI(imageUri);
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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddBook.this, android.R.layout.simple_list_item_1, listCategory);
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
        edtBookName = findViewById(R.id.edtBookName);
        edtBookNation = findViewById(R.id.edtBookNation);
        edtBookLaguage = findViewById(R.id.edtBookLaguage);
        edtAthorCode = findViewById(R.id.edtAthorCode);
        edtBookCode = findViewById(R.id.edtBookCode);
        edtPublishingCompanyCode = findViewById(R.id.edtPublishingCompanyCode);
        edtDateOfPublication = findViewById(R.id.edtDateOfPublication);
        edtNumbersOfPage = findViewById(R.id.edtNumbersOfPage);
        edtIntroBook = findViewById(R.id.edtIntroBook);
        name = edtBookName.getText().toString().trim();
        code = edtBookCode.getText().toString().trim();
        authorCode = edtAthorCode.getText().toString().trim();
        nation = edtBookNation.getText().toString().trim();
        publishingCompanyCode = edtPublishingCompanyCode.getText().toString().trim();
        dateOfPublication = edtDateOfPublication.getText().toString().trim();
        numbersOfPage= edtNumbersOfPage.getText().toString().trim();
        intro = edtIntroBook.getText().toString().trim();
        if (name.isEmpty() || code.isEmpty()
                || nation.isEmpty()|| publishingCompanyCode.isEmpty()
                || dateOfPublication.isEmpty()|| numbersOfPage.isEmpty()){
            Toast.makeText(AddBook.this, getString(R.string.enter_full_information), Toast.LENGTH_LONG).show();
        }
        else {
            if (code.length()<11){
                edtBookCode.setError(getString(R.string.request_code_format));
            }
            else {
//                new saveDataInformation().execute();
                checkExistBook();

            }
        }


    }
    private void checkExistBook() {
        progressDialog = new ProgressDialog(AddBook.this);
        progressDialog.setMessage(getString(R.string.mess_save));
        progressDialog.show();
        Query query = reference.orderByChild("bookCode").equalTo(code);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    edtBookCode.setError("Book already exists");
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
        final StorageReference mountainsRef = storageRef.child("image/"+code+".jpg");
        imageBookCreate.setDrawingCacheEnabled(true);
        imageBookCreate.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageBookCreate.getDrawable()).getBitmap();
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
                            Toast.makeText(AddBook.this, "Download uri fail",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    }
                });
            }
        });
    }
    private void addDataToFireBaseDataBase( ){
        getDateCreate();
        int numberPage = Integer.parseInt(numbersOfPage);
        BookDetail bookDetail= new BookDetail(name, authorCode, nation, language, code,valueCategory,  publishingCompanyCode, dateOfPublication, numberPage,  intro,uriDownload.toString(), 0, dateCreate);
//        FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
//        DatabaseReference myRef = database.getReference("Books");
        reference.child(code).setValue(bookDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    clearInformationWhenSuccess();
                    Toast.makeText(AddBook.this, "Save data FireBase success", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddBook.this, e.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
    private void clearInformationWhenSuccess(){
        edtBookName.setText("");
        edtAthorCode.setText("");
        edtBookCode.setText("");
        edtBookLaguage.setText("");
        edtBookNation.setText("");
        edtDateOfPublication.setText("");
        edtPublishingCompanyCode.setText("");
        edtNumbersOfPage.setText("");
        edtIntroBook.setText("");
        imageBookCreate.setImageResource(R.drawable.addbook);
    }
    private void getDateCreate(){
        Calendar calendar = Calendar.getInstance();
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int month = calendar.get(Calendar.MONTH)+ ;
//        int year = calendar.get(Calendar.YEAR);
        SimpleDateFormat  dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateCreate = dateFormat.format(calendar.getTime());
    }

}