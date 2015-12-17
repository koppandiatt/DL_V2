package Models;

import android.util.Log;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import DataAccessLayer.DataAccessLayer;

/**
 * Created by kovac on 12/14/2015.
 */
public class UserModel {

    private long id;

    private String firstname;

    private String lastname;

    private String email;

    private String role;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastname(String fastname) {
        this.lastname = fastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
