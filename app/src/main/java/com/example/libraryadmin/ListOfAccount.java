package com.example.libraryadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.libraryadmin.Adapter.FragmentListAccountAdapter;
import com.google.android.material.tabs.TabLayout;

public class ListOfAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_account);
        setupViews();
    }

    private void setupViews() {
        ViewPager viewPager = findViewById(R.id.viewPagerListAccount);
        TabLayout tabLayout = findViewById(R.id.tabList);
        viewPager.setAdapter(new FragmentListAccountAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);
    }
}