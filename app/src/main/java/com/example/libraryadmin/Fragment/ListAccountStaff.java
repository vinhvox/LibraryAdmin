package com.example.libraryadmin.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryadmin.Adapter.AccountStaffAdapter;
import com.example.libraryadmin.Details.AccountEmployeeDetail;
import com.example.libraryadmin.EditAccount;
import com.example.libraryadmin.R;
import com.example.libraryadmin.ShowBorrowsDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListAccountStaff extends Fragment implements AccountStaffAdapter.Callback {
    View view;
    DatabaseReference reference ;
    List<AccountEmployeeDetail> listData;
    AccountStaffAdapter accountStaffAdapter;
    RecyclerView recyclerStaffAccount;
    ImageView imageNoData ;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_staff, container, false);
        reference = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/")
                .getReference("Accounts").child("Staff");
        recyclerStaffAccount = view.findViewById(R.id.recyclerStaffAccount);
        imageNoData = view.findViewById(R.id.imageViewListStaffNoData);
        progressBar = view.findViewById(R.id.progressBarListStaff);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupViews();
    }

    private void setupViews() {
        new readDataAccount().execute();
    }

    @Override
    public void deleteItem(String id) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(getString(R.string.notification));
        alertDialog.setMessage(R.string.you_sure);
        alertDialog.setNegativeButton(getText(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton(getText(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDataPromissoryNote(id);
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void editItem(String id) {
        Intent intent = new Intent(getContext(), EditAccount.class);
        intent.putExtra("KEY", id+"-staff");
        startActivity(intent);

    }

    private  class readDataAccount extends AsyncTask<Void, Void, List<AccountEmployeeDetail>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listData = new ArrayList<>();


        }

        @Override
        protected List<AccountEmployeeDetail> doInBackground(Void... voids) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        AccountEmployeeDetail accountEmployeeDetail;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            accountEmployeeDetail = dataSnapshot.getValue(AccountEmployeeDetail.class);
                            listData.add(accountEmployeeDetail);
                            publishProgress();

                        }
                    }
                    else {
                        noData();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return listData;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.e("DATA", listData.size()+" ");
            accountStaffAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(List<AccountEmployeeDetail> accountEmployeeDetails) {
            super.onPostExecute(accountEmployeeDetails);
//            Log.e("DATA", accountEmployeeDetails.size()+" ");
            setupRecyclerView();
        }
    }
    private  void  noData(){
        imageNoData.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        recyclerStaffAccount.setVisibility(View.GONE);
    }
    private  void  setupRecyclerView(){
        imageNoData.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerStaffAccount.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerStaffAccount.setLayoutManager(linearLayoutManager);
         accountStaffAdapter = new AccountStaffAdapter(listData, getContext(), this);
        recyclerStaffAccount.setAdapter(accountStaffAdapter);
        }
    private void  deleteDataPromissoryNote(String employCode){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.mess_save));
        progressDialog.show();
        Query query = reference.orderByChild("employeeCode").equalTo(employCode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        dataSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                    onResume();
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_LONG).show();
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

}

