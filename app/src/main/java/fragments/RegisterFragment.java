package fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.koppa.driverlicensev2.R;

import DataAccessLayer.DataAccessLayer;

/**
 * Created by Attila on 16.12.2015.
 */

public class RegisterFragment extends Fragment{

    EditText txtUser, txtPass;
    Button btnLogin, btnRegister;
    ProgressBar progressBar;

    View view;

    public RegisterFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_register, container, false);

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

        return view;
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
            role = "";
            try{
                if(userid.trim().equals("") || userpass.trim().equals("")){
                    message = "Please enter the UserName and Password";
                }else{

                    // DataAccessLayer.getInstance();
                    DataAccessLayer.getInstance().addUser(userid.trim(),userpass.trim());
                    // long QID = DataAccessLayer.getInstance().InsertNewQuestion("dasdasd","asfasfas");
                    // int arows = DataAccessLayer.getInstance().InsertNewAnswers(QID, "lofasz", 1);
                    // readData();
                    // Log.v("TEMP ", arows + "");


                }
                return message;
            }catch (Exception ex){
                Log.e("err", ex.getMessage());
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
            Toast.makeText(getActivity(), "Account created successfully!", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            LoginFragment cuQuestionFragment = new LoginFragment();
            fragmentTransaction.replace(R.id.fragment_container, cuQuestionFragment);
            fragmentTransaction.commit();

        }

    }

}
