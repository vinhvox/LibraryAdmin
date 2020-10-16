package com.example.libraryadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setupViews();
    }

    private void setupViews() {
        CardView cardViewMoreBooks = findViewById(R.id.cardViewMoreBooks);
        CardView cardViewAddEmployee = findViewById(R.id.cardViewAddEmployee);
        CardView cardViewAddStudents = findViewById(R.id.cardViewAddStudents);
        CardView cardViewListAccount = findViewById(R.id.cardViewListAccount);
        CardView cardViewAddAuthor = findViewById(R.id.cardViewAddAuthor);
        CardView cardViewAddPublishingCompany = findViewById(R.id.cardViewAddPublishingCompany);
        CardView cardViewBorrowList = findViewById(R.id.cardViewBorrowList);
        cardViewAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, AddStaff.class));
            }
        });
        cardViewAddStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, AddAccoutCustomer.class));
            }
        });
        cardViewListAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, ListOfAccount.class));
            }
        });
        cardViewMoreBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, AddBook.class));
            }
        });
        cardViewAddAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, AddAuthor.class));
            }
        });
        cardViewAddPublishingCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, AddPublish.class));
            }
        });
        cardViewBorrowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this, BorrowTheList.class));
            }
        });

    }
}