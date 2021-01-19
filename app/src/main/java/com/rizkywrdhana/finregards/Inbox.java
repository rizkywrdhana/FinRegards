package com.rizkywrdhana.finregards;

import java.io.Serializable;

public class Inbox implements Serializable {
    private String regid;
    private String image;
    private String name;
    private String from;
    private String message;
    private String file;

    public Inbox(String regid, String image, String name, String from, String message, String file) {
        this.regid = regid;
        this.image = image;
        this.name = name;
        this.from = from;
        this.message = message;
        this.file = file;
    }

    public Inbox() {

    }

    public String getRegid() {
        return regid;
    }

    public void setRegid(String regid) {
        this.regid = regid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
