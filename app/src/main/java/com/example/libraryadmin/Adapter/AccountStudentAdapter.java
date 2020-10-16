package com.example.libraryadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryadmin.Details.AccountStudentsDetails;
import com.example.libraryadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AccountStudentAdapter extends RecyclerView.Adapter<AccountStudentAdapter.ViewHolder> {
    List<AccountStudentsDetails> accountStudentsDetailsList;
    Context context;
    Callback callback;

    public AccountStudentAdapter(List<AccountStudentsDetails> accountStudentsDetails, Context context, Callback callback) {
        this.accountStudentsDetailsList = accountStudentsDetails;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.item_account;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =  inflater.inflate(layoutId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View view = holder.getView();
        inflaterToViews(view, position);

    }
    private  void  inflaterToViews(View view, int position){
        ImageView profileImageItem = view.findViewById(R.id.imageViewAccount);
        ImageView imageViewDeleteAccount = view.findViewById(R.id.imageViewDeleteAccount);
        ImageView imageViewEditAccount = view.findViewById(R.id.imageViewEditAccount);
        TextView txtNameAccount = view.findViewById(R.id.txtNameAccount);
        TextView txtCodeAccount = view.findViewById(R.id.txtCodeAccount);
        TextView txtPositionAccount = view.findViewById(R.id.txtPositionAccount);
        AccountStudentsDetails accountStudentsDetails = accountStudentsDetailsList.get(position);
        Picasso.get().load(accountStudentsDetails.getImage()).into(profileImageItem);
        txtNameAccount.setText(accountStudentsDetails.getName());
        txtCodeAccount.setText(accountStudentsDetails.getStudentCode());
        imageViewDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.deleteItem(accountStudentsDetails.getStudentCode());
            }
        });
        imageViewEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.editItem(accountStudentsDetails.getStudentCode());
            }
        });

    }

    @Override
    public int getItemCount() {
        return accountStudentsDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }

        public View getView() {
            return view;
        }
    }
    public  interface Callback{
        void  deleteItem(String id);
        void editItem(String id);
    }
}
