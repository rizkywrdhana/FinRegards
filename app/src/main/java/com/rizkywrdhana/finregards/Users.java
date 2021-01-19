package com.rizkywrdhana.finregards;

import java.io.Serializable;

public class Users implements Serializable {

    private String userid;
    private String fullname;
    private String username;
    private String status;
    private String access;

    public Users(String userid, String fullname, String username, String status, String access) {
        this.userid = userid;
        this.fullname = fullname;
        this.username = username;
        this.status = status;
        this.access = access;
    }

    public Users() {

    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
