package fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koppa.driverlicensev2.R;

import java.util.ArrayList;

import DataAccessLayer.DataAccessLayer;
import Models.ClientModel;
import Models.QuestionModel;


public class TestLicense extends Fragment {


    private ProgressBar progressBar;

    private ArrayList<QuestionModel> _questions;

    private ArrayList<ArrayList<Boolean>> _answers;

    private int currentQPOS;

    private TextView questionView;

    private CheckBox answer1,answer2,answer3;



    public TestLicense() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentQPOS = -1;_questions = null;
        _answers = new ArrayList<ArrayList<Boolean>>();
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_test_license, container, false);
        initializeGUIReferences(view);
        progressBar.setVisibility(View.GONE);
        TestData testdata = new TestData();
        testdata.execute();
        Log.v("teszt","lassuk");

        return view;

    }

    private void initializeGUIReferences(View view){

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

        questionView = (TextView) view.findViewById(R.id.TVtestquestion);
        answer1 = (CheckBox) view.findViewById(R.id.answ1);
        answer2 = (CheckBox) view.findViewById(R.id.answ2);
        answer3 = (CheckBox) view.findViewById(R.id.answ3);

        progressBar = (ProgressBar) getActivity().findViewById(R.id.mainprogressBar);


    }

    private void fillQuestionGUI(int index){

        QuestionModel qModel = _questions.get(index);
        questionView.setText(qModel.getQuestion());
        answer1.setText(qModel.getAnswers().get(0).first);
        answer2.setText(qModel.getAnswers().get(1).first);
        answer3.setText(qModel.getAnswers().get(2).first);
        answer1.setChecked(false);
        answer2.setChecked(false);
        answer3.setChecked(false);
    }

    private void nextStep(){
        Log.v("teszt", _questions.size() + "");
        if (currentQPOS > -1){
            getAnswers();
        }

        if ((currentQPOS +  1) < _questions.size())
        {
            ++currentQPOS;
            fillQuestionGUI(currentQPOS);
        }else{
            winInformationDIalog();
        }
    }

    private void getAnswers(){
        ArrayList<Boolean> temp = new ArrayList<Boolean>();
        temp.add(answer1.isChecked());
        temp.add(answer2.isChecked());
        temp.add(answer3.isChecked());
        _answers.add(currentQPOS,temp);
    }

    private void prevStep(){
        if (currentQPOS > -1){
            getAnswers();
        }

        if ((currentQPOS - 1) >= 0){
            --currentQPOS;
            fillQuestionGUI(currentQPOS);
        }
    }

    public void checkAnswerIsCorrect(){
        int countCorrect = 0;
        for (int i = 0; i < _questions.size(); ++i){
            boolean isCorret = true;
            for (int j = 0; j< _questions.get(i).getAnswers().size();++i){
                if (_questions.get(i).getAnswers().get(j).second != _answers.get(i).get(j)){
                    isCorret = false;
                }
            }
            if (isCorret) countCorrect++;
        }



    }

    private void winInformationDIalog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Write your message here.");
        builder1.setCancelable(true);
        builder1.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



    private class TestData extends AsyncTask<String,String,Boolean> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Boolean doInBackground(String... params) {


            try {
                Thread.sleep(200);
                _questions = ClientModel.getQuestion(10);
                return true;
            }catch (Exception ex)
            {
                Log.e("error","Error occured at the get question!");
            }
            return false;


        }

        @Override
        protected void onPostExecute(Boolean message) {


            progressBar.setVisibility(View.GONE);
            if (message == false){
                Toast.makeText(getActivity(),"Error occured !",Toast.LENGTH_LONG).show();
                return;
            }

            nextStep();

        }
    }


}
