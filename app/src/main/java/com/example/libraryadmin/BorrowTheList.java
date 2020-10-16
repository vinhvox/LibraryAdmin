package com.example.libraryadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.example.libraryadmin.Adapter.FragmentBorrowList;
import com.example.libraryadmin.Adapter.FragmentListAccountAdapter;
import com.example.libraryadmin.Details.BorrowDetail;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BorrowTheList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_the_list);

        setupViews();

    }

    private void setupViews() {
        ViewPager viewPager = findViewById(R.id.viewPagerBorrowList);
        TabLayout tabLayout = findViewById(R.id.tabBorrowList);
        viewPager.setAdapter(new FragmentBorrowList(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);
    }
}