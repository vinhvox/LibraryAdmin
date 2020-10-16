package com.example.libraryadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryadmin.Details.AccountEmployeeDetail;
import com.example.libraryadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AccountStaffAdapter extends RecyclerView.Adapter<AccountStaffAdapter.ViewHolder> {
    List<AccountEmployeeDetail> accountEmployeeDetails;
    Context context;
    Callback callback;

    public AccountStaffAdapter(List<AccountEmployeeDetail> accountEmployeeDetails, Context context, Callback callback) {
        this.accountEmployeeDetails = accountEmployeeDetails;
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
        AccountEmployeeDetail accountEmployeeDetail = accountEmployeeDetails.get(position);
        Picasso.get().load(accountEmployeeDetail.getImage()).into(profileImageItem);
        txtNameAccount.setText(accountEmployeeDetail.getName());
        txtCodeAccount.setText(accountEmployeeDetail.getEmployeeCode());
        txtPositionAccount.setText(accountEmployeeDetail.getPosition());
        imageViewDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.deleteItem(accountEmployeeDetail.getEmployeeCode());
            }
        });
        imageViewEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.editItem(accountEmployeeDetail.getEmployeeCode());
            }
        });

    }

    @Override
    public int getItemCount() {
        return accountEmployeeDetails.size();
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
