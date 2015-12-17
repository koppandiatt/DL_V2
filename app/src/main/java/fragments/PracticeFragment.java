package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koppa.driverlicensev2.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import Controller.ClientController;
import Models.IFragmentsStarter;
import Models.QuestionModel;
import Models.Settings;
import Models.UserModel;


public class PracticeFragment extends Fragment {

    private ProgressBar progressBar;

    private QuestionModel _question;



    private int currentQPOS;

    private TextView questionView;

    private CheckBox answer1,answer2,answer3;

    private Button btnNext;

    private TextView timerView;

    private ImageView imgView;

    private TextView pageNumView;

    private UserModel userModel;

    private RelativeLayout mainView;

    private int countCorrect = 0;

    private int countAnsw = 0;


    Timer timer;

    TimerTask timerTask;

    final Handler handler = new Handler();

    private IFragmentsStarter fragmentsStarter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_license, container, false);
    }

    private void initializeGUIReferences(View view){

        mainView = (RelativeLayout) view.findViewById(R.id.testMainContainer);
        mainView.setVisibility(View.INVISIBLE);

        btnNext = (Button) view.findViewById(R.id.BTNtestnext);
        Button btnBack = (Button) view.findViewById(R.id.BTNtestback);

        pageNumView = (TextView) view.findViewById(R.id.TVpagenum);

        timerView = (TextView) view.findViewById(R.id.TVtesttime);

        imgView = (ImageView) view.findViewById(R.id.IVtestimg);

        btnNext.setText("Next");

        pageNumView.setText("0/" + Settings.questionNUM);

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                nextStep();
            }
        });

        questionView = (TextView) view.findViewById(R.id.TVtestquestion);
        answer1 = (CheckBox) view.findViewById(R.id.answ1);
        answer2 = (CheckBox) view.findViewById(R.id.answ2);
        answer3 = (CheckBox) view.findViewById(R.id.answ3);



        progressBar = (ProgressBar) getActivity().findViewById(R.id.mainprogressBar);


    }

    private void nextStep(){
       GetQuestion getQuestion = new GetQuestion();
        getQuestion.execute();
    }


    public void checkAnswerIsCorrect(){
        ArrayList<Boolean> answers = new ArrayList<Boolean>();
        answers.add(answer1.isChecked());
        answers.add(answer2.isChecked());
        answers.add(answer3.isChecked());
        boolean isCorret = true;
        for (int j = 0; j < _question.getAnswers().size();++j){
                if (_question.getAnswers().get(j).second != answers.get(j)){
                    isCorret = false;
                }
            }
        if (isCorret) {
                ++countCorrect;
                timerView.setText(countCorrect + "/" + countAnsw);
                pageNumView.setText(countAnsw);
        }


    }

    private void fillQuestionGUI(){



        Picasso.with(getActivity()).load(_question.getimgUrl()).into(imgView);
        questionView.setText(_question.getQuestion());
        answer1.setText(_question.getAnswers().get(0).first);
        answer2.setText(_question.getAnswers().get(1).first);
        answer3.setText(_question.getAnswers().get(2).first);
        answer1.setChecked(false);
        answer2.setChecked(false);
        answer3.setChecked(false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private class GetQuestion extends AsyncTask<String,String,Boolean> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Boolean doInBackground(String... params) {


            try {
                Thread.sleep(200);
                _question = ClientController.getQuestion();
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
            mainView.setVisibility(View.VISIBLE);
            if (message == false){
                Toast.makeText(getActivity(), "Error occured !", Toast.LENGTH_LONG).show();
                return;
            }
            countAnsw++;
            fillQuestionGUI();



        }
    }


}
