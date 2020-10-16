package com.example.libraryadmin.Details;

public class BookDetail {
    String bookName;
    String authorCode;
    String nation;
    String language;
    String bookCode;
    String category;
    String publish;
    String publicationDate;
    int numberOfPages;
    String introduce;
    String coverImage;
    int currentViews;
    String dateCreate;

    public BookDetail() {
    }

    public BookDetail(String bookName, String authorCode, String nation, String language, String bookCode, String category, String publish, String publicationDate, int numberOfPages, String introduce, String coverImage, int currentViews, String dateCreate) {
        this.bookName = bookName;
        this.authorCode = authorCode;
        this.nation = nation;
        this.language = language;
        this.bookCode = bookCode;
        this.category = category;
        this.publish = publish;
        this.publicationDate = publicationDate;
        this.numberOfPages = numberOfPages;
        this.introduce = introduce;
        this.coverImage = coverImage;
        this.currentViews = currentViews;
        this.dateCreate = dateCreate;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorCode() {
        return authorCode;
    }

    public void setAuthorCode(String authorCode) {
        this.authorCode = authorCode;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public int getCurrentViews() {
        return currentViews;
    }

    public void setCurrentViews(int currentViews) {
        this.currentViews = currentViews;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }
}
