package com.example.libraryadmin.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryadmin.Adapter.ListSubmitAdapter;
import com.example.libraryadmin.Details.BorrowDetail;
import com.example.libraryadmin.R;
import com.example.libraryadmin.ShowBorrowsDetails;
import com.example.libraryadmin.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListLate extends Fragment  implements ListSubmitAdapter.Callback {
    View view;
    ArrayList<BorrowDetail> borrowDetails;
    DatabaseReference reference ;
    ListSubmitAdapter adapter;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ImageView imageNoData;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_late, null);
        reference = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/")
                .getReference();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupViews();
    }
    private  void  setupViews(){
        progressBar = view.findViewById(R.id.progressLoadLate);
        imageNoData = view.findViewById(R.id.imageNoDataLate);
        recyclerView = view.findViewById(R.id.recyclerListLate);
        borrowDetails = new ArrayList<>();
        readData();
    }
    private  void  readData(){
        Query query = reference.child("BorrowBooks").orderByChild("status").equalTo(Status.Expired+"");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    BorrowDetail borrowDetail;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        borrowDetail = dataSnapshot.getValue(BorrowDetail.class);
                        borrowDetails.add(borrowDetail);
                    }
                    setupRecyclerView();
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                    imageNoData.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setupRecyclerView() {
//        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        imageNoData.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ListSubmitAdapter(borrowDetails, getContext(), this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onClickItem(BorrowDetail key) {
        Intent intent = new Intent(getContext(), ShowBorrowsDetails.class);
        intent.putExtra("KEY", key+"-expired");
        startActivity(intent);
    }
}
