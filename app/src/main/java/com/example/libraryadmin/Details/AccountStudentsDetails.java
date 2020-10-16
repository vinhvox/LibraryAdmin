package com.example.libraryadmin.Details;

public class AccountStudentsDetails {
    String name;
    String studentCode;
    String birthDay;
    String address;
    String email;
    String cmnd;
    Long phone;
    String sex;
    String image;

    public AccountStudentsDetails() {
    }

    public AccountStudentsDetails(String name, String studentCode, String birthDay, String address, String email, String cmnd, Long phone, String sex, String image) {
        this.name = name;
        this.studentCode = studentCode;
        this.birthDay = birthDay;
        this.address = address;
        this.email = email;
        this.cmnd = cmnd;
        this.phone = phone;
        this.sex = sex;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
