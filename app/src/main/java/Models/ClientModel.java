package Models;

import android.util.Log;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import DataAccessLayer.DataAccessLayer;

/**
 * Created by kovac on 12/14/2015.
 */
public class ClientModel {

    private long id;

    private String firstname;

    private String fastname;

    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFastname(String fastname) {
        this.fastname = fastname;
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

    public String getFastname() {
        return fastname;
    }

    public String getFirstname() {
        return firstname;
    }
}
