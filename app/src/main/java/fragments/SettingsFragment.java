package fragments;

import android.app.Fragment;
import android.app.admin.DeviceAdminInfo;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.koppa.driverlicensev2.R;

import java.util.ArrayList;
import java.util.Set;

import Controller.ClientController;
import DataAccessLayer.DataAccessLayer;
import Models.Settings;

/**
 * Created by Attila on 17.12.2015.
 */
public class SettingsFragment extends Fragment {

    public static final String MY_PREF = "Settings";

    private EditText NrQ, Time, Limit;
    private Button btnSave;

    ProgressBar progressBar;
    private View view;

    private int nrq, time, limit;

    public SettingsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        progressBar = (ProgressBar)getActivity().findViewById(R.id.mainprogressBar);
        progressBar.setVisibility(View.GONE);

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        NrQ = (EditText) view.findViewById(R.id.ET_nrq);
        Time = (EditText) view.findViewById(R.id.ET_time);
        Limit = (EditText) view.findViewById(R.id.ET_limit);

        NrQ.setText(Settings.questionNUM+"");
        Time.setText(Settings.time+"");
        Limit.setText(Settings.limitPoint+"");

        btnSave = (Button) view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetData set = new SetData();
                set.execute("");
            }
        });


        return view;
    }

    private class SetData extends AsyncTask<String,String,Boolean> {

        @Override
        protected void onPreExecute() {

            progressBar.setVisibility(View.VISIBLE);
            nrq = Integer.parseInt(NrQ.getText().toString().trim());
            time = Integer.parseInt(Time.getText().toString().trim());
            limit = Integer.parseInt(Limit.getText().toString().trim());
        }


        @Override
        protected Boolean doInBackground(String... params) {


            try {
                DataAccessLayer.getInstance().setSettings(nrq, time, limit);

                return true;
            }catch (Exception ex)
            {
                Log.e("error", "Error occured at the get question!");
            }
            return false;


        }

        @Override
        protected void onPostExecute(Boolean message) {


            progressBar.setVisibility(View.GONE);
            if(message == true){
                Settings.questionNUM = nrq;
                Settings.time = time;
                Settings.limitPoint = limit;
            }
            Toast.makeText(getActivity(), "Settings saved successfully !", Toast.LENGTH_LONG).show();
        }
    }
}
