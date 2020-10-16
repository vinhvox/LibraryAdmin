package com.example.libraryadmin.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.libraryadmin.Fragment.ListAccountStaff;
import com.example.libraryadmin.Fragment.ListAccountStudent;
import com.example.libraryadmin.R;

import java.util.ArrayList;

public class FragmentListAccountAdapter extends FragmentStatePagerAdapter {
    ListAccountStaff listAccountStaff;
    ListAccountStudent listAccountStudent;
    ArrayList<String> title = new ArrayList<>();
    public FragmentListAccountAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        title.add(context.getString(R.string.staff));
        title.add(context.getString(R.string.student));
        listAccountStaff = new ListAccountStaff();
        listAccountStudent = new ListAccountStudent();

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return listAccountStaff;
            case 1:
                return listAccountStudent;
        }
        return null;
    }

    @Override
    public int getCount() {
        return title.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
