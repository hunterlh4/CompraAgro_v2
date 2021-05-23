package com.example.compraagro.model;

public class Commentary {
    String title;
    String description;
    String stars;
    String nameCommentator;

    String idCommentary;
    String idProfile;
    String idCommentator;

    public Commentary(String title, String description, String stars, String idCommentary, String nameCommentator, String idProfile, String idCommentator) {
        this.title = title;
        this.description = description;
        this.stars = stars;
        this.idCommentary = idCommentary;
        this.nameCommentator = nameCommentator;
        this.idProfile = idProfile;
        this.idCommentator = idCommentator;
    }

    public Commentary() {
    }


    public String getNameCommentator() {
        return nameCommentator;
    }

    public void setNameCommentator(String nameCommentator) {
        this.nameCommentator = nameCommentator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getIdCommentary() {
        return idCommentary;
    }

    public void setIdCommentary(String idCommentary) {
        this.idCommentary = idCommentary;
    }

    public String getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(String idProfile) {
        this.idProfile = idProfile;
    }

    public String getIdCommentator() {
        return idCommentator;
    }

    public void setIdCommentator(String idCommentator) {
        this.idCommentator = idCommentator;
    }
}
