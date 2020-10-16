package com.example.libraryadmin.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.libraryadmin.Fragment.ListBorrowing;
import com.example.libraryadmin.Fragment.ListFinish;
import com.example.libraryadmin.Fragment.ListLate;
import com.example.libraryadmin.Fragment.ListSubmit;
import com.example.libraryadmin.R;

import java.util.ArrayList;

public class FragmentBorrowList extends FragmentStatePagerAdapter  {
    ArrayList<String> titles;
    ListSubmit listSubmit;
    ListBorrowing listBorrowing;
    ListLate listLate;
    ListFinish listFinish;
    public FragmentBorrowList(@NonNull FragmentManager fm, Context context) {
        super(fm);
        titles = new ArrayList<>();
        titles.add(context.getString(R.string.list_submit));
        titles.add(context.getString(R.string.list_borrowing));
        titles.add(context.getString(R.string.list_late));
        titles.add(context.getString(R.string.list_finish));
        listSubmit = new ListSubmit();
        listBorrowing = new ListBorrowing();
        listLate = new ListLate();
        listFinish = new ListFinish();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return  listSubmit;
            case  1:
                return listBorrowing;
            case 2:
                return  listLate;
            case 3:
                return listFinish;
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
