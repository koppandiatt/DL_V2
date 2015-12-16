package fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.koppa.driverlicensev2.R;

import java.util.ArrayList;

import DataAccessLayer.DataAccessLayer;
import Models.QuestionModel;


public class TestLicense extends Fragment {


    private ProgressBar progressBar;

    private ArrayList<QuestionModel> _questions;

    public TestLicense() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        progressBar = (ProgressBar) getActivity().findViewById(R.id.mainprogressBar);
        progressBar.setVisibility(View.GONE);
        View view = inflater.inflate(R.layout.fragment_test_license, container, false);
        Button btnNext = (Button) view.findViewById(R.id.BTNtestnext);
        Button btnBack = (Button) view.findViewById(R.id.BTNtestback);
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                nextStep();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                prevStep();
            }
        });


        return view;

    }

    public void nextStep(){

    }

    public void prevStep(){

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



    private class TestData extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... params) {




            return "Error occured in connection!";

        }

        @Override
        protected void onPostExecute(String message) {

            progressBar.setVisibility(View.GONE);

        }
    }


}
