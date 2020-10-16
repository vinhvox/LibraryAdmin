package com.example.libraryadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libraryadmin.Details.AccountEmployeeDetail;
import com.example.libraryadmin.Details.AccountStudentsDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class EditAccount extends AppCompatActivity {
String id, key, valueSex, valuePosition;
    ArrayList<String> arrayListSex, arrayListPosition ;
    Spinner spinnerSexEdit, spinnerPositionEdit;
    EditText edtNameAccountEdit, edtBirthdayEdit, edtAddressEdit, edtCMNDEdit, edtPhoneEdit;
    TextView txtPositionAccount;
    String name, birthDay, address, cmnd, phone;
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        Intent intent = getIntent();
        if (intent== null){
            return;
        }
        String value = intent.getStringExtra("KEY");
        String[] split = value.split("-");
        id = split[0];
        key = split[1];
         spinnerSexEdit = findViewById(R.id.spinnerSexEdit);
         spinnerPositionEdit = findViewById(R.id.spinnerPositionEdit);
         txtPositionAccount = findViewById(R.id.txtPositionAccount);

        if (key.equals("staff")){
            spinnerSexEdit.setVisibility(View.VISIBLE);
            spinnerPositionEdit.setVisibility(View.VISIBLE);
            txtPositionAccount.setVisibility(View.VISIBLE);
            readDataStaff();
        }
        else if (key.equals("student")){
            spinnerSexEdit.setVisibility(View.VISIBLE);
            spinnerPositionEdit.setVisibility(View.GONE);
            txtPositionAccount.setVisibility(View.GONE);
            readDataStudents();
        }
    }
    private void readDataStudents(){
        Log.e("DATACHECK", id + key);
        DatabaseReference reference = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/").getReference();
        Query query = reference.child("Accounts").child("Students").orderByChild("studentCode").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    AccountStudentsDetails accountStudentsDetails;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        accountStudentsDetails = dataSnapshot.getValue(AccountStudentsDetails.class);
                        setupViewsStudents(accountStudentsDetails);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private  void readDataStaff(){
        DatabaseReference reference = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/").getReference();
        Query query = reference.child("Accounts").child("Staff").orderByChild("employeeCode").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    AccountEmployeeDetail accountEmployeeDetail;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        accountEmployeeDetail = dataSnapshot.getValue(AccountEmployeeDetail.class);
                        setupViewsStaff(accountEmployeeDetail);
                        Log.e("DATACHECK", accountEmployeeDetail.getAddress());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private  void setupViewsStudents(AccountStudentsDetails accountStudentsDetails){
        ImageView imageAccountEdit = findViewById(R.id.imageAccountEdit);
        edtNameAccountEdit = findViewById(R.id.edtNameAccountEdit);
        edtBirthdayEdit = findViewById(R.id.edtBirthdayEdit);
        edtAddressEdit = findViewById(R.id.edtAddressEdit);
        edtCMNDEdit = findViewById(R.id.edtCMNDEdit);
        edtPhoneEdit = findViewById(R.id.edtPhoneEdit);
        TextView txtCodeEmployee = findViewById(R.id.edtCodeEmployee);


        txtCodeEmployee.setText(accountStudentsDetails.getStudentCode());
        Picasso.get().load(accountStudentsDetails.getImage()).into(imageAccountEdit);
        edtNameAccountEdit.setText(accountStudentsDetails.getName());
        edtAddressEdit.setText(accountStudentsDetails.getAddress());
        edtBirthdayEdit.setText(accountStudentsDetails.getBirthDay());
        edtCMNDEdit.setText(accountStudentsDetails.getCmnd());
        edtPhoneEdit.setText(accountStudentsDetails.getPhone()+"");
        setupDataSex(accountStudentsDetails.getSex());



    }

    private  void setupViewsStaff(AccountEmployeeDetail accountEmployeeDetail){
        ImageView imageAccountEdit = findViewById(R.id.imageAccountEdit);
         edtNameAccountEdit = findViewById(R.id.edtNameAccountEdit);
         edtBirthdayEdit = findViewById(R.id.edtBirthdayEdit);
         edtAddressEdit = findViewById(R.id.edtAddressEdit);
         edtCMNDEdit = findViewById(R.id.edtCMNDEdit);
         edtPhoneEdit = findViewById(R.id.edtPhoneEdit);
        TextView txtCodeEmployee = findViewById(R.id.edtCodeEmployee);


        txtCodeEmployee.setText(accountEmployeeDetail.getEmployeeCode());
        Picasso.get().load(accountEmployeeDetail.getImage()).into(imageAccountEdit);
        edtNameAccountEdit.setText(accountEmployeeDetail.getName());
        edtAddressEdit.setText(accountEmployeeDetail.getAddress());
        edtBirthdayEdit.setText(accountEmployeeDetail.getBirthDay());
        edtCMNDEdit.setText(accountEmployeeDetail.getCmnd());
        edtPhoneEdit.setText(accountEmployeeDetail.getPhone()+"");
        setupDataSex(accountEmployeeDetail.getSex());
        setupDataPosition(accountEmployeeDetail.getPosition());


    }
    private  void  setupDataSex(String value){
        arrayListSex = new ArrayList<>();
        arrayListSex.add(getString(R.string.male));
        arrayListSex.add(getString(R.string.female));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(EditAccount.this, android.R.layout.simple_list_item_1, arrayListSex);
        spinnerSexEdit.setAdapter(arrayAdapter);
        spinnerSexEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueSex = arrayListSex.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        int pos = arrayAdapter.getPosition(value);
        spinnerSexEdit.setSelection(pos);
    }
    private  void  setupDataPosition(String value){
        arrayListPosition = new ArrayList<>();
        arrayListPosition.add(getString(R.string.admin));
        arrayListPosition.add(getString(R.string.manager));
        arrayListPosition.add(getString(R.string.librarian));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(EditAccount.this, android.R.layout.simple_list_item_1, arrayListPosition);
        spinnerPositionEdit.setAdapter(arrayAdapter);
        spinnerPositionEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valuePosition = arrayListPosition.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        int pos = arrayAdapter.getPosition(value);
        spinnerPositionEdit.setSelection(pos);
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
                saveChangeData();
                break;


        }
        return super.onOptionsItemSelected(item);
    }
    private  void  saveChangeData(){
        name = edtNameAccountEdit.getText().toString().trim();
        address = edtAddressEdit.getText().toString().trim();
        birthDay = edtBirthdayEdit.getText().toString().trim();
        phone = edtPhoneEdit.getText().toString().trim();
        cmnd = edtCMNDEdit.getText().toString().trim();
        if (key.equals("staff")){
            saveStaff();
        }
        else if (key.equals("student")){
            saveStudents();
        }
    }

    private void saveStudents() {
        long numberPhone = Long.parseLong(phone);
         progressDialog = new ProgressDialog(EditAccount.this);
        progressDialog.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("birthDay", birthDay);
        map.put("address", address);
        map.put("cmnd", cmnd);
        map.put("phone", numberPhone);
        map.put("sex", valueSex);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        DatabaseReference myRef = database.getReference("Accounts").child("Students");
        myRef.child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(EditAccount.this, "Save data FireBase success", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditAccount.this, e.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }


    private void saveStaff( ){
        long numberPhone = Long.parseLong(phone);
        progressDialog = new ProgressDialog(EditAccount.this);
        progressDialog.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("birthDay", birthDay);
        map.put("address", address);
        map.put("cmnd", cmnd);
        map.put("phone", numberPhone);
        map.put("sex", valueSex);
        map.put("position", valuePosition);

         FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        DatabaseReference myRef = database.getReference("Accounts").child("Staff");
        myRef.child(id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(EditAccount.this, "Save data FireBase success", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditAccount.this, e.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
}