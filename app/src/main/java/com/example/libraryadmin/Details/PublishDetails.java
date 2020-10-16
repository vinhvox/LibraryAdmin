package com.example.libraryadmin.Details;

public class PublishDetails {
    String name;
    String publishCode;
    String intro;
    String address;
    String phone;
    String fax;
    String email;
    String website;
    String imageUri;

    public PublishDetails() {
    }

    public PublishDetails(String name, String publishCode, String intro, String address, String phone, String fax, String email, String website, String imageUri) {
        this.name = name;
        this.publishCode = publishCode;
        this.intro = intro;
        this.address = address;
        this.phone = phone;
        this.fax = fax;
        this.email = email;
        this.website = website;
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublishCode() {
        return publishCode;
    }

    public void setPublishCode(String publishCode) {
        this.publishCode = publishCode;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
