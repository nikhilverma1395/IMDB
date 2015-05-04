package com.nikhilvermavit.vlog;

/**
 * Created by Nikhil Verma on 2/5/2015.
 */
public class SqlModel {
    private String username;
    private long id;
    private String password;

    public SqlModel() {
    }

    public SqlModel(String username, String password) {

        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
