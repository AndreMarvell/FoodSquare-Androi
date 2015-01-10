package com.andremarvell.foodsquare.classe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Marvell on 13/12/14.
 */
public class User  implements Serializable {
    /**
     *  identifiant de l'utilisateur
     */
    private int id;

    /**
     *  nom de l'utilisateur
     */
    private String nom;

    /**
     * prenom
     */
    private String prenom;

    /**
     * adresse mail du user
     */
    private String email;


    /**
     * Identifiant Facebook du user
     */
    private String facebookId;

    /**
     * Token du user
     */
    private String token;

    /**
     * Role de l'utilisateur (Admin,  User)
     */
    private int role = 1;

    /**
     * boolean pour savoir si le user est bloqué ou banni
     */
    private boolean locked = false;

    /**
     * Date d'inscription du user
     */
    private String dateInscription;

    /**
     * photo de l'utilisateur
     */
    private String photo = "unknow.jpg";

    /**
     * Code pin à 4 chiffre de l'utilisateur
     */
    private int pin;

    /**
     * Date de derniere connection de l'utilisateur
     */
    private String lastConnection;

    /**
     * Nombre de commentaires de l'utilisateur
     */
    private int nbComment;

    /**
     * Nombre de notes de l'utilisateur
     */
    private int nbNotes;



    public User() {
    }

    public User(JSONObject json) {
        try {
            id = json.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            nom = (json.getString("nom")!=null)?json.getString("nom"):null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            prenom = (json.getString("prenom")!=null)?json.getString("prenom"):null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            email = (json.getString("email")!=null)?json.getString("email"):null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(json.has("facebookId"))
                facebookId = (json.getString("facebookId")!=null)?json.getString("facebookId"):null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(json.has("token"))
                token = (json.getString("token")!=null)?json.getString("token"):null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            role = json.getInt("role");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            locked = json.getBoolean("locked");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            photo = (json.getString("photo")!=null)?json.getString("photo"):null;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(String dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public JSONObject getUserJson(){
        JSONObject json = new JSONObject();

        try {
            json.put("nom", (nom!=null)?nom:"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("prenom", (prenom!=null)?prenom:"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("facebookId", (facebookId!=null)?facebookId:"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("pin",pin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("photo", photo);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return json;
    }

    public String getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(String lastConnection) {
        this.lastConnection = lastConnection;
    }

    public String getNbComment() {
        if(nbComment==0)
            return "Vous n'avez pas encore commenté de restaurants";
        else
            return "Vous avez déjà publié "+nbComment+" avis sur des restaurants";
    }

    public void setNbComment(int nbComment) {
        this.nbComment = nbComment;
    }

    public String getNbNotes() {
        if(nbNotes==0)
            return "Vous n'avez pas encore noté de restaurants";
        else
            return "Vous avez noté "+nbNotes+" restaurants";
    }

    public void setNbNotes(int nbNotes) {
        this.nbNotes = nbNotes;
    }

    public String getScore(){
        int score = nbComment*15 +nbNotes*10;
        return score+" point(s)";
    }
}
