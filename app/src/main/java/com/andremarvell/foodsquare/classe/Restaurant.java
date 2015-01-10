package com.andremarvell.foodsquare.classe;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marvell on 28/12/14.
 */
public class Restaurant implements Serializable{


    private String id;
    private String googleId;
    private String adresse;
    private String longitude;
    private String latitude;
    private Double googleRate;
    private String photo;
    private String nom;
    private int nbCommentaire;
    private Double note;
    private Double distance;
    private boolean isUserRater = false;
    private boolean isFavoris = false;
    private int userRate;
    private List<Photo> gallerie;
    private List<Commentaire> comments;

    public Restaurant(String id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Restaurant(String id, String googleId, String adresse, String longitude, String latitude, Double googleRate, String photo, String nom, int nbCommentaire, Double note) {
        this.id = id;
        this.googleId = googleId;
        this.adresse = adresse;
        this.longitude = longitude;
        this.latitude = latitude;
        this.googleRate = googleRate;
        this.photo = photo;
        this.nom = nom;
        this.nbCommentaire = nbCommentaire;
        this.note = note;
        this.gallerie = new ArrayList<Photo>();
        this.comments = new ArrayList<Commentaire>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Double getGoogleRate() {
        return googleRate;
    }

    public void setGoogleRate(Double googleRate) {
        this.googleRate = googleRate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNbCommentaire() {
        return nbCommentaire;
    }

    public void setNbCommentaire(int nbCommentaire) {
        this.nbCommentaire = nbCommentaire;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    public String getDistance() {

        DecimalFormat df = new DecimalFormat("#.##");

        if(1000*distance>1000)
            return df.format(distance)+" Km";
        else
            return df.format(1000*distance)+" m";

    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void addPhoto(Photo p){
        gallerie.add(p);
    }

    public void addComment(Commentaire c){
        comments.add(c);
    }

    public int nbPhoto(){
        return gallerie.size();
    }

    public boolean isUserRater() {
        return isUserRater;
    }

    public void setUserRater(boolean isUserRater) {
        this.isUserRater = isUserRater;
    }

    public List<Photo> getGallerie() {
        return gallerie;
    }

    public void setGallerie(List<Photo> gallerie) {
        this.gallerie = gallerie;
    }

    public List<Commentaire> getComments() {
        return comments;
    }

    public void setComments(List<Commentaire> comments) {
        this.comments = comments;
    }

    public int getUserRate() {
        return userRate;
    }

    public void setUserRate(int userRate) {
        this.userRate = userRate;
    }

    public boolean isFavoris() {
        return isFavoris;
    }

    public void setFavoris(boolean isFavoris) {
        this.isFavoris = isFavoris;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + id + '\'' +
                ", googleId='" + googleId + '\'' +
                ", adresse='" + adresse + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", googleRate=" + googleRate +
                ", photo='" + photo + '\'' +
                ", nom='" + nom + '\'' +
                ", nbCommentaire=" + nbCommentaire +
                ", note=" + note +
                ", distance=" + distance +
                ", isUserRater=" + isUserRater +
                ", userRate=" + userRate +
                ", gallerie=" + gallerie +
                ", comments=" + comments +
                '}';
    }
}
