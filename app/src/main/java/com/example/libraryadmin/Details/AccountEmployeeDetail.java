package com.example.libraryadmin.Details;

public class AccountEmployeeDetail {
    String name;
    String employeeCode;
    String birthDay;
    String address;
    String email;
    String cmnd;
    Long phone;
    String sex;
    String position;
    String image;

    public AccountEmployeeDetail() {
    }

    public AccountEmployeeDetail(String name, String employeeCode, String birthDay, String address, String email, String cmnd, Long phone, String sex, String position, String image) {
        this.name = name;
        this.employeeCode = employeeCode;
        this.birthDay = birthDay;
        this.address = address;
        this.email = email;
        this.cmnd = cmnd;
        this.phone = phone;
        this.sex = sex;
        this.position = position;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
