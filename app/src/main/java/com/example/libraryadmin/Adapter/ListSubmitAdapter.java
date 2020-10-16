package com.example.libraryadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryadmin.Details.BorrowDetail;
import com.example.libraryadmin.R;

import java.util.ArrayList;

public class ListSubmitAdapter extends RecyclerView.Adapter<ListSubmitAdapter.ViewHolder> {
    ArrayList<BorrowDetail> borrowDetails;
    Context context;
    Callback callback;

    public ListSubmitAdapter(ArrayList<BorrowDetail> borrowDetails, Context context, Callback callback) {
        this.borrowDetails = borrowDetails;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.item_borrow_list;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View view = holder.getView();
        inflaterToViews(view, position);

    }
    private  void inflaterToViews(View view, int position){
        TextView txtFormCode = view.findViewById(R.id.txtFormCode);
        TextView txtDataCreate = view.findViewById(R.id.txtDataCreate);
        TextView txtPayDate = view.findViewById(R.id.txtPayDate);
        TextView txtStatus = view.findViewById(R.id.txtStatus);
        Button button = view.findViewById(R.id.btnDetails);
        BorrowDetail borrowDetail = borrowDetails.get(position);
        txtFormCode.setText(borrowDetail.getPromissoryNoteCode());
        txtDataCreate.setText(borrowDetail.getDayCreate());
        txtPayDate.setText(borrowDetail.getPayDay());
        txtStatus.setText(borrowDetail.getStatus().toString());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickItem(borrowDetail);
            }
        });


    }

    @Override
    public int getItemCount() {
        return borrowDetails.size();
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
        void onClickItem(BorrowDetail borrowDetail);
    }
}
