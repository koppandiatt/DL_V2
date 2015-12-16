package DataAccessLayer;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Models.QuestionModel;

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




    public ArrayList<QuestionModel> getQuestion(int n){


        ArrayList<QuestionModel> questionModels = new ArrayList<QuestionModel>();

        try {
            for (int i = 0; i < n;++i){

                QuestionModel questionModel = new QuestionModel();
                //"SELECT * FROM questions WHERE Status = 1 ORDER BY RAND() LIMIT 1";

                String query = "SELECT * FROM questions WHERE Status = 1 ORDER BY RAND() LIMIT 1";
                Statement statement = _conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                int QID = -1;
                if (resultSet.next()) {


                    QID = resultSet.getInt("ID");
                    questionModel.setId(QID);
                    questionModel.setQuestion(resultSet.getString("Text"));
                    questionModel.setimgUrl("Image");

                    Log.v("teszt",questionModel.getQuestion());

                }

                Log.v("QID", "" + QID);
                statement.close();

                query = "SELECT * FROM answers WHERE Status = 1 and QID = '" + QID + "'" ;
                statement = _conn.createStatement();
                resultSet = statement.executeQuery(query);
                int correctIndex = 1;

                while (resultSet.next()) {

                    questionModel.addNewAnswer(resultSet.getString("Text"));
                    int id = resultSet.getInt("Correct");
                    Log.v("teszt",resultSet.getString("Text"));
                    if (id == 1) questionModel.setCorrectAnsIndex(correctIndex);
                    correctIndex++;
                }

                questionModels.add(questionModel);
            }
        return  questionModels;

        } catch (Exception ex) {
            Log.e("dberror", ex.getMessage());
            return null;
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
}







