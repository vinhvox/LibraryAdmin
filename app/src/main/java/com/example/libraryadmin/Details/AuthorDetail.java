package com.example.libraryadmin.Details;

public class AuthorDetail {
    String pseudonym;
    String authorName;
    String authorCode;
    String authorNation;
    String authorDay;
    String category;
    String artwork;
    String imageURI;

    public AuthorDetail() {
    }

    public AuthorDetail(String pseudonym, String authorName, String authorCode, String authorNation, String authorDay, String category, String artwork, String imageURI) {
        this.pseudonym = pseudonym;
        this.authorName = authorName;
        this.authorCode = authorCode;
        this.authorNation = authorNation;
        this.authorDay = authorDay;
        this.category = category;
        this.artwork = artwork;
        this.imageURI = imageURI;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorCode() {
        return authorCode;
    }

    public void setAuthorCode(String authorCode) {
        this.authorCode = authorCode;
    }

    public String getAuthorNation() {
        return authorNation;
    }

    public void setAuthorNation(String authorNation) {
        this.authorNation = authorNation;
    }

    public String getAuthorDay() {
        return authorDay;
    }

    public void setAuthorDay(String authorDay) {
        this.authorDay = authorDay;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getArtwork() {
        return artwork;
    }

    public void setArtwork(String artwork) {
        this.artwork = artwork;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}
