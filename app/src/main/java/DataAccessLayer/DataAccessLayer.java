package DataAccessLayer;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Models.UserModel;

/**
 * Created by koppa on 26.11.2015.
 */
public class DataAccessLayer {

    private String ip = "86.123.132.51:3333";
    private String classe = "com.mysql.jdbc.Driver";
    private String db = "driverlicense";
    private String un = "sapientia";
    private String password = "MSSapi2015**";


    private static DataAccessLayer _instance = null;

    private Connection _conn = null;

    public static final String ADMIN = "admin";

    public static final String CLIENT = "client";

    public static final String ERROR = "connerror";


    private DataAccessLayer() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String ConnURL = null;

            Class.forName(classe);
            ConnURL = "jdbc:mysql://" + ip + "/"
                    + db + "?user=" + un + "&password="
                    + password;
            _conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("SQLERROR", se.getMessage());
        } catch (ClassNotFoundException ce) {
            Log.e("SQLERROR", ce.getMessage());
        } catch (Exception e) {
            Log.e("SQLERROR", e.getMessage());
        }

    }

    public static synchronized DataAccessLayer getInstance() {
        if (_instance == null) {
            _instance = new DataAccessLayer();
        }
        return _instance;

    }


    public void insert(String query){

        if (_conn == null) {
            return;
        }
        try {
            Statement statement = _conn.createStatement();
            statement.executeQuery(query);
        }catch(Exception ex){
            Log.e("dberror","Error occured at selecting");
        }
        return;
    }


    public String getUserRole(String username, String password) {


        if (_conn == null) {
            return ERROR;
        }

        try {
            String query = "SELECT * FROM users WHERE User='" + username + "' and Password='" + password + "'";
            Statement statement = _conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getString("role");

            } else {
                return null;

            }
        } catch (Exception ex) {
            Log.e("dberror", ex.getMessage());
            return ERROR;
        }

    }

    public ResultSet select(String query){

        if (_conn == null) {
            return null;
        }
        try {
            Statement statement = _conn.createStatement();
            return statement.executeQuery(query);
        }catch(Exception ex){
            Log.e("dberror","Error occured at selecting");
        }
        return null;
    }

    public void insert(String query){

        if (_conn == null) {
            return;
        }
        try {
            Statement statement = _conn.createStatement();
            statement.executeUpdate(query);
        }catch(Exception ex){
            Log.e("dberror","Error occured at selecting" + ex.getMessage());
        }
        return;
    }

    public void addUser(String name, String pass)
    {
        if (_conn == null) {
            return;
        }

        try {

            String query = "INSERT INTO users (User, Password, Role)"
                    + " VALUES (?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = _conn.prepareStatement(query);
            preparedStmt.setString(1, name);
            preparedStmt.setString(2, pass);
            preparedStmt.setString(3, "client");
            // execute the preparedstatement
            int affectedRows = preparedStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

        } catch (SQLException se){
            Log.e("sqlError", se.getMessage());
            return;
        } catch (Exception ex) {
            Log.e("dberror", ex.getMessage());
            return;
        }
    }

    public UserModel getUser(String username, String password){

        if (_conn == null) {
            return null ;
        }

        try {
            String query = "SELECT * FROM users WHERE User='" + username + "' and Password='" + password + "'";
            Statement statement = _conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            UserModel userModel = new UserModel();  //Id, User , Password, Role
            if (resultSet.next()) {

                userModel.setId(Integer.parseInt(resultSet.getString("Id")));
                userModel.setRole(resultSet.getString("Role"));
                return userModel;

            } else {
                return null;

            }
        } catch (Exception ex) {
            Log.e("dberror", ex.getMessage());
            return null;
        }
    }


    public long InsertNewQuestion(String question, String imgUrl) {

        if (_conn == null) {
            return -1;
        }

        try {

            String query = "INSERT INTO questions (Text, Status, Image)"
                    + " VALUES (?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = _conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString(1, question);
            preparedStmt.setInt(2, 1);
            preparedStmt.setString(3, imgUrl);
            // execute the preparedstatement
            int affectedRows = preparedStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }


            ResultSet generatedKeys = preparedStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                 return generatedKeys.getLong(1);
            }
            else {
                throw new SQLException("Creating question failed, no ID obtained.");
            }


        } catch (SQLException se){
            Log.e("sqlError", se.getMessage());
            return -1;
        } catch (Exception ex) {
            Log.e("dberror", ex.getMessage());
            return -1;
        }

    }

    public int UpdateQuestion(int QID, String question, String image){
        if (_conn == null) {
            return -1;
        }

        try {

            String query = "UPDATE questions SET `Text`=?, `Image`=? WHERE ID=?";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = _conn.prepareStatement(query);
            preparedStmt.setString(1, question);
            preparedStmt.setString(2, image);
            preparedStmt.setInt(3, QID);

            // execute the preparedstatement
            int affectedRows = preparedStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            else {
                throw new SQLException("Creating question failed, no ID obtained.");
            }


        } catch (SQLException se){
            Log.e("sqlError", se.getMessage());
            return -1;
        } catch (Exception ex) {
            Log.e("dberror", ex.getMessage());
            return -1;
        }
    }

    public int InsertNewAnswers(long QID, String answer, int Correct) {

        if (_conn == null) {
            return -1;
        }

        try {

            String query = "INSERT INTO answers (QID, Text, Correct, Status)"
                    + " VALUES (?, ?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = _conn.prepareStatement(query);
            preparedStmt.setLong(1, QID);
            preparedStmt.setString(2, answer);
            preparedStmt.setInt(3, Correct);
            preparedStmt.setInt(4, 1);
            // execute the preparedstatement
            int affectedRows = preparedStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            String s = String.valueOf(affectedRows);
            Log.d("affectedRow", s);
            return affectedRows;


        } catch (SQLException se){
            Log.e("sqlError", se.toString());
            return -1;
        } catch (Exception ex) {
            Log.e("dberror", ex.getMessage());
            return -1;
        }
    }

    public void UpdateAnswer(int AID, String answer, int Correct)
    {
        if (_conn == null) {
            return;
        }

        try {

            String query = "UPDATE answers SET Text=?, Correct=? WHERE ID=?";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = _conn.prepareStatement(query);

            preparedStmt.setString(1, answer);
            preparedStmt.setInt(2, Correct);
            preparedStmt.setLong(3, AID);

            // execute the preparedstatement
            int affectedRows = preparedStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            String s = String.valueOf(affectedRows);
            Log.d("affectedRow", s);

        } catch (SQLException se){
            Log.e("sqlError", se.toString());
            return;
        } catch (Exception ex) {
            Log.e("dberror", ex.getMessage());
            return;
        }
    }

    public ArrayList<Integer> getAnswerId(int QID){
        String query = "SELECT * FROM answers WHERE QID=" + QID;
        ArrayList<Integer> ids = new ArrayList<Integer>();
        Statement statement = null;
        try {
            statement = _conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                ids.add( resultSet.getInt("ID"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }
}







