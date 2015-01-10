package com.andremarvell.foodsquare.classe;

import java.io.Serializable;

/**
 * Created by Marvell on 29/12/14.
 */
public class Note  implements Serializable {

    private String id;
    private String note;
    private String thread;
    private User rater;
    private String date;

    public Note(String id, String note, String thread, String date, User rater) {
        this.id = id;
        this.note = note;
        this.thread = thread;
        this.date = date;
        this.rater = rater;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public User getRater() {
        return rater;
    }

    public void setRater(User rater) {
        this.rater = rater;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
