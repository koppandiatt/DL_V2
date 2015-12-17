package fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import java.util.ArrayList;
import java.util.Set;
import java.util.zip.Inflater;

import DataAccessLayer.DataAccessLayer;
import Models.ICommonChannel;
import Models.IFragmentsStarter;
import Models.QuestionModel;
import Models.Settings;
import Models.UserModel;

/**
 * Created by koppa on 26.11.2015.
 */
public class LoginFragment extends Fragment {

    EditText txtUser, txtPass;
    Button btnLogin, btnRegister;
    ProgressBar progressBar;

    View view;

    private static final int ADMIN = 294;
    private static final int CLIENT = 223;

    private IFragmentsStarter _fragmentStarter = null;
    private ICommonChannel _commonChannel = null;



    public LoginFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_login, container, false);

        txtUser = (EditText) view.findViewById(R.id.txtName);
        txtPass = (EditText) view.findViewById(R.id.txtPass);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.mainprogressBar);
        progressBar.setVisibility(View.GONE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
                //callSelectedUserFragment(CLIENT);
            }
        });

        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RegisterFragment cuQuestionFragment = new RegisterFragment();
                fragmentTransaction.replace(R.id.fragment_container, cuQuestionFragment);
                fragmentTransaction.commit();
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

        private void readData()
        {

            Resources resources = getActivity().getResources();
            String[] questions = resources.getStringArray(R.array.questions);

            for(int i=0; i<questions.length; ++i)
            {


                String[] oneQuestion = questions[i].split("#");

                if (oneQuestion[5].equals("null")) continue;


                int helyes = Integer.parseInt(oneQuestion[4]);
                long id = DataAccessLayer.getInstance().InsertNewQuestion(oneQuestion[0],oneQuestion[5]);
                for (int k = 1; k <=3;++k){
                    if (k == helyes)
                        DataAccessLayer.getInstance().InsertNewAnswers(id,oneQuestion[k],1);
                    else
                        DataAccessLayer.getInstance().InsertNewAnswers(id,oneQuestion[k],0);
                }

            }
        }


        @Override
        protected String doInBackground(String... params) {


            String message = SUCCESS;
            role = "";
            try{
                if(userid.trim().equals("") || userpass.trim().equals("")){
                    message = "Please enter the UserName and Password";
                }else{

                    UserModel userModel  = DataAccessLayer.getInstance().getUser(userid.trim(),userpass.trim());
                    ArrayList<Integer>  settings = DataAccessLayer.getInstance().getSettings();
                    Settings.questionNUM = settings.get(0);
                    Settings.time = settings.get(1);
                    Settings.limitPoint = settings.get(2);
                    Log.v("settings",settings.get(0) + " " + settings.get(1) + " " + settings.get(2)  );

                    if (userModel == null){
                        message = "Wrong credentials!";
                    }else{

                        role = userModel.getRole();

                    }

                    _commonChannel.setUser(userModel);


                }
                return message;
            }catch (Exception ex){
                Log.e("err",ex.getMessage());
            }

            return "Error occured in connection!";

        }

        @Override
        protected void onPostExecute(String message) {
            progressBar.setVisibility(View.GONE);
            if (!message.equals(SUCCESS)){
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                return;
            }
          //  callSelectedUserFragment(CLIENT);
            if (_fragmentStarter == null) return;
            switch (role)
            {
                case DataAccessLayer.ADMIN:
                    _fragmentStarter.addAdminFragment();
                    break;
                case DataAccessLayer.CLIENT:
                    _fragmentStarter.addClientFragment();
                    break;
                default:
                    Toast.makeText(getActivity(),"Wrong credentials!",Toast.LENGTH_LONG).show();
               break;
            }

        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
           _fragmentStarter = (IFragmentsStarter) activity;
            _commonChannel = (ICommonChannel) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
