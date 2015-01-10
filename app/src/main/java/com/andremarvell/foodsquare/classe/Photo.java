package com.andremarvell.foodsquare.classe;

import android.graphics.Bitmap;

import com.andremarvell.foodsquare.FoodSquareApplication;

import java.io.Serializable;

/**
 * Created by Marvell on 29/12/14.
 */
public class Photo  implements Serializable {

    private String id;
    private String url;
    private String miniature;
    private User proprietaire;

    public Photo(String id, String url, String miniature, User proprietaire) {
        this.id = id;
        this.url = url;
        this.miniature = miniature;
        this.proprietaire = proprietaire;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return FoodSquareApplication.getWebServiceImageUrl()+url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMiniature() {
        return FoodSquareApplication.getWebServiceImageUrl()+miniature;
    }

    public void setMiniature(String miniature) {
        this.miniature = miniature;
    }

    public User getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(User proprietaire) {
        this.proprietaire = proprietaire;
    }

}
