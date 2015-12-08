package DataAccessLayer;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by koppa on 26.11.2015.
 */
public class DataAccessLayer {

    String ip = "86.123.132.51:3333";
    String classe = "com.mysql.jdbc.Driver";
    String db = "driverlicense";
    String un = "sapientia";
    String password = "MSSapi2015**";
    @SuppressLint("NewApi")

    public Connection CONN(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try
        {
            Class.forName(classe);
            ConnURL = "jdbc:mysql://" + ip + "/"
                    + db + "?user=" + un + "&password="
                    + password ;
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se){
            Log.e("SQLERROR", se.getMessage());
        } catch (ClassNotFoundException ce) {
            Log.e("SQLERROR", ce.getMessage());
        } catch (Exception e){
            Log.e("SQLERROR", e.getMessage());
        }
        return conn;
    }

}
