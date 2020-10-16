package com.example.libraryadmin.Details;

import com.example.libraryadmin.Status;

import java.io.Serializable;

public class BorrowDetail implements Serializable {
    String bookCode;
    String userCode;
    String fullName;
    Status status;
    String borrowRead;
    String dayCreate;
    String payDay;
    String promissoryNoteCode;


    public BorrowDetail() {
    }

    public BorrowDetail(String bookCode, String userCode, String fullName, Status status, String borrowRead, String dayCreate, String payDay, String promissoryNoteCode) {
        this.bookCode = bookCode;
        this.userCode = userCode;
        this.fullName = fullName;
        this.status = status;
        this.borrowRead = borrowRead;
        this.dayCreate = dayCreate;
        this.payDay = payDay;
        this.promissoryNoteCode = promissoryNoteCode;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getBorrowRead() {
        return borrowRead;
    }

    public void setBorrowRead(String borrowRead) {
        this.borrowRead = borrowRead;
    }

    public String getDayCreate() {
        return dayCreate;
    }

    public void setDayCreate(String dayCreate) {
        this.dayCreate = dayCreate;
    }

    public String getPayDay() {
        return payDay;
    }

    public void setPayDay(String payDay) {
        this.payDay = payDay;
    }

    public String getPromissoryNoteCode() {
        return promissoryNoteCode;
    }

    public void setPromissoryNoteCode(String promissoryNoteCode) {
        this.promissoryNoteCode = promissoryNoteCode;
    }
}
