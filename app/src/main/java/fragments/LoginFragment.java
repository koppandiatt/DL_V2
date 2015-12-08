package fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koppa.driverlicensev2.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.zip.Inflater;

import DataAccessLayer.DataAccessLayer;

/**
 * Created by koppa on 26.11.2015.
 */
public class LoginFragment extends Fragment {

    DataAccessLayer dal;
    EditText txtUser, txtPass;
    Button btnLogin;
    ProgressBar progressBar;

    View view;

    private static final int ADMIN = 294;
    private static final int CLIENT = 223;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view =  inflater.inflate(R.layout.fragment_login, container, false);
        dal = new DataAccessLayer();

        txtUser = (EditText) view.findViewById(R.id.txtName);
        txtPass = (EditText) view.findViewById(R.id.txtPass);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DoLogin doLogin = new DoLogin();
                //doLogin.execute("");
                callSelectedUserFragment(CLIENT);
            }
        });

        return view;
    }

    private void callSelectedUserFragment(int user ){

        Fragment frag = null;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        NavigationView navigationView;
        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);

        if (user == ADMIN){
            navigationView.inflateMenu(R.menu.nav_menu_admin);
            ((TextView) getActivity().findViewById(R.id.txtUserName)).setText("Admin");
            ((TextView) getActivity().findViewById(R.id.txtUserMail)).setText("admin@admin.com");
            frag = new AdminFragment();
        }else if(user == CLIENT ){
            navigationView.inflateMenu(R.menu.nav_menu_client);
            ((TextView) getActivity().findViewById(R.id.txtUserName)).setText("Client");
            ((TextView) getActivity().findViewById(R.id.txtUserMail)).setText("client@client.com");
            frag = new ClientFragment();
        }
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toolbar.setVisibility(View.VISIBLE);

        fragmentTransaction.replace(R.id.fragment_container, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private class DoLogin extends AsyncTask<String,String,String>
    {

        String userid = txtUser.getText().toString();
        String userpass = txtPass.getText().toString();
        String role;

        private static final String SUCCESS  = "Success";

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... params) {

            String message = SUCCESS;

            if(userid.trim().equals("") || userpass.trim().equals("")){
                message = "Please enter the UserName and Password";
            }else{
                try{
                    Connection connection = dal.CONN();
                    if(connection == null){
                        message = "Error in connection to the Server";
                    }else
                    {
                        String query = "select * from users where User='" + userid + "' and Password='" + userpass + "'";
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);
                        if(resultSet.next())
                        {
                            role = resultSet.getString("role");
                            Log.d("ROLE", role);
                            // message = "Login successfull";

                        }
                        else{
                            message = "Invalid Credentials";

                        }
                    }
                }catch (Exception e){
                    message = "Exceptions";

                }
            }
            return message;
        }

        @Override
        protected void onPostExecute(String message) {
            progressBar.setVisibility(View.GONE);
            if (!message.equals(SUCCESS)){
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return;
            }
            switch (role)
            {
                case "admin":
                    callSelectedUserFragment(ADMIN);
                    break;
                case "client":
                    callSelectedUserFragment(CLIENT);
                    break;
                default:
                    break;
            }
        }

    }
}
