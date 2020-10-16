package com.example.libraryadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libraryadmin.Details.BorrowDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class ShowBorrowsDetails extends AppCompatActivity {
    DatabaseReference reference ;
    FirebaseDatabase database;
    BorrowDetail borrowDetail;
    ProgressDialog progressDialog;
    Button btnConfirm, btnRefuse;

    ;
    String key, formCode, parent, studentCode, bookCode, borrowRead, dateCreate, datePay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_borrows_details);
        database= FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        reference = database.getReference();
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        borrowDetail = (BorrowDetail) args.getSerializable("DATA");


    }

    @Override
    protected void onResume() {
        super.onResume();
        setupViews();
    }

   private  void  setupViews(){
       TextView txtFormCode = findViewById(R.id.txtFormCode);
       TextView txtFullName = findViewById(R.id.txtFullName);
       TextView txtStudentCode = findViewById(R.id.txtStudentCode);
       TextView txtBookCode = findViewById(R.id.txtBookCode);
       TextView txtBorrowRead = findViewById(R.id.txtBorrowRead);
       TextView txtDateCreate = findViewById(R.id.txtDateCreate);
       TextView txtDatePay = findViewById(R.id.txtDatePay);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnRefuse = findViewById(R.id.btnRefuse);

       txtFormCode.setText(borrowDetail.getPromissoryNoteCode());
       txtFullName.setText(borrowDetail.getFullName());
       txtStudentCode.setText(borrowDetail.getUserCode());
       txtBookCode.setText(borrowDetail.getBookCode());
       txtBorrowRead.setText(borrowDetail.getBorrowRead());
       txtDateCreate.setText(borrowDetail.getDayCreate());
       txtDatePay.setText(borrowDetail.getPayDay());
       viewSubmit();
//       if (parent.equals("submit")){
//           viewSubmit();
//       }
//       if (parent.equals("borrowed")){
//           btnConfirm.setVisibility(View.GONE);
//           btnRefuse.setVisibility(View.GONE);
//       }
//       if (parent.equals("expired")){
//           btnConfirm.setVisibility(View.GONE);
//           btnRefuse.setVisibility(View.GONE);
//       }
//       if (parent.equals("finish")){
//           btnConfirm.setVisibility(View.GONE);
//           btnRefuse.setVisibility(View.GONE);
//       }
   }
   private  void viewSubmit(){
       btnConfirm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               confirmRequest();
           }
       });
       btnRefuse.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               deleteDataPromissoryNote();
           }
       });
   }
    private void  deleteDataPromissoryNote(){
        progressDialog = new ProgressDialog(ShowBorrowsDetails.this);
        progressDialog.setMessage(getString(R.string.mess_save));
        progressDialog.show();
        Query query = reference.child("BorrowBooks").orderByChild("promissoryNoteCode").equalTo(formCode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        dataSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    finish();
                                    progressDialog.dismiss();
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(ShowBorrowsDetails.this, getString(R.string.failed), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                }
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }
   private  void confirmRequest() {
       progressDialog = new ProgressDialog(ShowBorrowsDetails.this);
       progressDialog.setMessage(getString(R.string.mess_save));
       progressDialog.show();
       HashMap<String, Object> map = new HashMap<>();
       map.put("status", Status.Borrowed);
      Query query=  reference.child("BorrowBooks").orderByChild("promissoryNoteCode").equalTo(borrowDetail.getPromissoryNoteCode());
       query.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                      dataSnapshot.getRef().updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              if (task.isSuccessful()){
                                  progressDialog.dismiss();
                              }
                              else {
                                  progressDialog.dismiss();
                                  Toast.makeText(ShowBorrowsDetails.this, getString(R.string.failed), Toast.LENGTH_LONG).show();
                              }
                          }
                      });
                   }
               }
               else {
                   Log.e("DATA", " ko");
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               progressDialog.dismiss();
           }
       });
   }
}