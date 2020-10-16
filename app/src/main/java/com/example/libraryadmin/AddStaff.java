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

public class AddStaff extends AppCompatActivity {

    private static final int PERMISSION_CODE = 102;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 103;
    private static final int REQUEST_CODE_PICK_CAPTURE = 104 ;
    Uri imageUri , uriDownload;
    ImageView imageEmployeeCreate;
    FirebaseDatabase database;
    DatabaseReference reference;
    EditText edtCodeEmployee, edtNameEmployee, edtEmployeeBirthDay,edtEmployeeAddress, edtEmployeeEmail,edtEmployeeCMND , edtEmployeePhone;
    String valueSex, valuePosition, name, code, birthday, address, email, cmnd, phone;
    ArrayList<String> arrayListSex, arrayListPosition ;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);
        setupViews();
        setupDataSex();
        setupDataPosition();
    }

    private void setupViews() {
        database= FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        reference = database.getReference("Accounts").child("Staff");
         imageEmployeeCreate = findViewById(R.id.imageEmployeeCreate);
        imageEmployeeCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRequestPermission();
                showMenuImages(v);
            }
        });

    }
    private  void  showMenuImages(View view){
        PopupMenu popupMenu = new PopupMenu(AddStaff.this, view);
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
                Toast.makeText(AddStaff.this, getString(R.string.denied), Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(AddStaff.this, getString(R.string.denied), Toast.LENGTH_LONG).show();
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
            imageEmployeeCreate.setImageBitmap(imageBitmap);

        }
        if (requestCode== REQUEST_CODE_PICK_CAPTURE&& resultCode== RESULT_OK&& data!= null){
            imageUri = data.getData();
            imageEmployeeCreate.setImageURI(imageUri);
        }
    }
    private  void  setupDataSex(){
        arrayListSex = new ArrayList<>();
        arrayListSex.add(getString(R.string.male));
        arrayListSex.add(getString(R.string.female));
        Spinner spinner = findViewById(R.id.spinnerSexEmployee);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddStaff.this, android.R.layout.simple_list_item_1, arrayListSex);
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
    private  void  setupDataPosition(){
        arrayListPosition = new ArrayList<>();
        arrayListPosition.add(getString(R.string.admin));
        arrayListPosition.add(getString(R.string.manager));
        arrayListPosition.add(getString(R.string.librarian));
        Spinner spinner = findViewById(R.id.spinnerPositionEmployee);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddStaff.this, android.R.layout.simple_list_item_1, arrayListPosition);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valuePosition = arrayListPosition.get(position);
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
         edtNameEmployee = findViewById(R.id.edtNameEmployee);
        edtCodeEmployee = findViewById(R.id.edtCodeEmployee);
        edtEmployeeBirthDay = findViewById(R.id.edtEmployeeBirthDay);
        edtEmployeeAddress = findViewById(R.id.edtEmployeeAddress);
        edtEmployeeEmail = findViewById(R.id.edtEmployeeEmail);
        edtEmployeeCMND = findViewById(R.id.edtEmployeeCMND);
        edtEmployeePhone = findViewById(R.id.edtEmployeePhone);
        name = edtNameEmployee.getText().toString().trim();
        code = edtCodeEmployee.getText().toString().trim();
        birthday = edtEmployeeBirthDay.getText().toString().trim();
        address = edtEmployeeAddress.getText().toString().trim();
        email = edtEmployeeEmail.getText().toString().trim();
        cmnd= edtEmployeeCMND.getText().toString().trim();
        phone = edtEmployeePhone.getText().toString().trim();
        if (name.isEmpty() || code.isEmpty()
                || birthday.isEmpty()|| address.isEmpty()
                || email.isEmpty()|| cmnd.isEmpty() || phone.isEmpty()){
            Toast.makeText(AddStaff.this, getString(R.string.enter_full_information), Toast.LENGTH_LONG).show();
        }
        else {
            if (!email.contains("@gmail.com")){
                edtEmployeeEmail.setError(getString(R.string.request_email_format));
            }
            if (code.length()<11){
                edtCodeEmployee.setError(getString(R.string.request_code_format));
            }
            else {
//                new saveDataInformation().execute();
                checkExistAccount();

            }
        }


    }
    private void checkExistAccount() {
        progressDialog = new ProgressDialog(AddStaff.this);
        progressDialog.setMessage(getString(R.string.mess_save));
        progressDialog.show();
        Query query = reference.orderByChild("employeeCode").equalTo(code);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                   edtCodeEmployee.setError("Account already exists");
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
                    Toast.makeText(AddStaff.this, task.getException()+"", Toast.LENGTH_LONG).show();
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
        imageEmployeeCreate.setDrawingCacheEnabled(true);
        imageEmployeeCreate.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageEmployeeCreate.getDrawable()).getBitmap();
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
                            Toast.makeText(AddStaff.this, "Download uri fail",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    }
                });
            }
        });
    }
    private void addDataToFireBaseDataBase( ){
        long phoneNumber = Long.parseLong(phone);
        AccountEmployeeDetail accountDetail = new AccountEmployeeDetail(name, code, birthday, address, email,cmnd, phoneNumber, valueSex, valuePosition, uriDownload.toString());
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        DatabaseReference myRef = database.getReference("Accounts").child("Staff");
        myRef.child(code).setValue(accountDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    clearInformationWhenSuccess();
                    Toast.makeText(AddStaff.this, "Save data FireBase success", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddStaff.this, e.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
    private void clearInformationWhenSuccess(){
        edtCodeEmployee.setText("");
        edtEmployeeAddress.setText("");
        edtEmployeeBirthDay.setText("");
        edtEmployeeCMND.setText("");
        edtEmployeeEmail.setText("");
        edtNameEmployee.setText("");
        edtEmployeePhone.setText("");
        imageEmployeeCreate.setImageResource(R.drawable.staff);
    }

}