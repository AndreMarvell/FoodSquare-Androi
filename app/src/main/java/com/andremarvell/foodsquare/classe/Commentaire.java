package com.andremarvell.foodsquare.classe;

import java.io.Serializable;

/**
 * Created by Marvell on 29/12/14.
 */
public class Commentaire  implements Serializable {

    private String id;
    private String commentaire;
    private String thread;
    private User commenter;
    private String date;

    public Commentaire(String id, String commentaire, String thread, String date, User commenter) {
        this.id = id;
        this.commentaire = commentaire;
        this.thread = thread;
        this.date = date;
        this.commenter = commenter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "commentaire='" + commentaire + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
