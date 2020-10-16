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
import com.example.libraryadmin.Details.AccountStudentsDetails;
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
import java.util.ArrayList;
import java.util.Objects;

public class AddAccoutCustomer extends AppCompatActivity {
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 120 ;
    private static final int PERMISSION_CODE =  1001;
    private static final int REQUEST_CODE_PICK_CAPTURE = 130;
    EditText edtNameStudent, edtStudentCode, edtBirthDayStudent, edtStudentAddress, edtEmailStudent, edtCardCodeStudent, edtPhoneStudent;
    ImageView imageStudentCreate;
    FirebaseDatabase database;
    DatabaseReference reference;
    String valueSex, name, code, birthday, address, email, cmnd, phone;
    private Uri imageUri, uriDownload;
    private ArrayList<String> arrayListSex;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accout_customer);
        setupViews();
    }

    private void setupViews() {
        database= FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        reference = database.getReference("Accounts").child("Students");
        edtNameStudent = findViewById(R.id.edtNameStudent);
        edtStudentCode = findViewById(R.id.edtStudentCode);
        edtBirthDayStudent = findViewById(R.id.edtBirthDayStudent);
        edtStudentAddress = findViewById(R.id.edtStudentAddress);
        edtEmailStudent = findViewById(R.id.edtEmailStudent);
        edtCardCodeStudent = findViewById(R.id.edtCardCodeStudent);
        edtPhoneStudent = findViewById(R.id.edtPhoneStudent);
        imageStudentCreate = findViewById(R.id.imageStudentCreate);
        setupDataSex();

        imageStudentCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRequestPermission();
                showMenuImages(v);
            }
        });

    }
    private  void  showMenuImages(View view){
        PopupMenu popupMenu = new PopupMenu(AddAccoutCustomer.this, view);
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
                Toast.makeText(AddAccoutCustomer.this, getString(R.string.denied), Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(AddAccoutCustomer.this, getString(R.string.denied), Toast.LENGTH_LONG).show();
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
            imageStudentCreate.setImageBitmap(imageBitmap);

        }
        if (requestCode== REQUEST_CODE_PICK_CAPTURE&& resultCode== RESULT_OK&& data!= null){
            imageUri = data.getData();
            imageStudentCreate.setImageURI(imageUri);
        }
    }
    private  void  setupDataSex(){
        arrayListSex = new ArrayList<>();
        arrayListSex.add(getString(R.string.male));
        arrayListSex.add(getString(R.string.female));
        Spinner spinner = findViewById(R.id.spinnerStudentSex);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddAccoutCustomer.this, android.R.layout.simple_list_item_1, arrayListSex);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueSex = arrayListSex.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void informationProcessing() {
        name = edtNameStudent.getText().toString().trim();
        code = edtStudentCode.getText().toString().trim();
        birthday = edtBirthDayStudent.getText().toString().trim();
        address = edtStudentAddress.getText().toString().trim();
        email = edtEmailStudent.getText().toString().trim();
        cmnd= edtCardCodeStudent.getText().toString().trim();
       phone = edtPhoneStudent.getText().toString().trim();
        if (name.isEmpty() || code.isEmpty()
                || birthday.isEmpty()|| address.isEmpty()
                || email.isEmpty()|| cmnd.isEmpty() || phone.isEmpty() ){
            Toast.makeText(AddAccoutCustomer.this, getString(R.string.enter_full_information), Toast.LENGTH_LONG).show();
        }
        else {
            if (!email.contains("@gmail.com")){
                edtEmailStudent.setError(getString(R.string.request_email_format));
            }
            if (code.length()<11){
                edtStudentCode.setError(getString(R.string.request_code_format));
            }
            else {
//                new saveDataInformation().execute();

                checkExistAccount();

            }
        }


    }
    private void checkExistAccount() {
        progressDialog = new ProgressDialog(AddAccoutCustomer.this);
        progressDialog.setMessage(getString(R.string.mess_save));
        progressDialog.show();
        Query query = reference.orderByChild("employeeCode").equalTo(code);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    edtStudentCode.setError("Account already exists");
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
        saveAccount();

    }
    private void saveAccount() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, code).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    saveImageToStorageAndDownloadUri();
                }
                else{
                    Toast.makeText(AddAccoutCustomer.this, task.getException()+"", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }
        });

    }
    private void saveImageToStorageAndDownloadUri(){
//        String path = pathBookImageInStorage+bookCode+".png";
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://library-80e61.appspot.com");
        StorageReference storageRef = storage.getReference();
// Create a reference to "mountains.jpg"
        final StorageReference mountainsRef = storageRef.child("image/"+code+".jpg");
        imageStudentCreate.setDrawingCacheEnabled(true);
        imageStudentCreate.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageStudentCreate.getDrawable()).getBitmap();
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
                            Toast.makeText(AddAccoutCustomer.this, "Download uri fail",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    }
                });
            }
        });
    }
    private void addDataToFireBaseDataBase( ){
        long phoneNumber = Long.parseLong(phone);
        AccountStudentsDetails accountDetail = new AccountStudentsDetails(name, code, birthday, address, email,cmnd, phoneNumber, valueSex, uriDownload.toString());
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        DatabaseReference myRef = database.getReference("Accounts").child("Students");
        myRef.child(code).setValue(accountDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    clearInformationWhenSuccess();
                    Toast.makeText(AddAccoutCustomer.this, "Save data FireBase success", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddAccoutCustomer.this, e.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
    private void clearInformationWhenSuccess(){
        edtStudentCode.setText("");
        edtEmailStudent.setText("");
        edtPhoneStudent.setText("");
        edtCardCodeStudent.setText("");
        edtStudentAddress.setText("");
        edtBirthDayStudent.setText("");
        edtNameStudent.setText("");
        imageStudentCreate.setImageResource(R.drawable.staff);
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
    private  void createQR(){
        
    }

}