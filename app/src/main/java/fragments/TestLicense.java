package fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.SystemClock;
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
import Models.UserModel;
import Models.IFragmentsStarter;
import Models.QuestionModel;
import Models.Settings;
import Models.StatisticModel;


public class TestLicense extends Fragment {


    private ProgressBar progressBar;

    private ArrayList<QuestionModel> _questions;

    private ArrayList<ArrayList<Boolean>> _answers;

    private int currentQPOS;

    private TextView questionView;

    private CheckBox answer1,answer2,answer3;

    private Button btnNext;

    private TextView timerView;

    private ImageView imgView;

    private TextView pageNumView;

    private UserModel userModel;

    private RelativeLayout mainView;

    private String startDate;

    Timer timer;

    TimerTask timerTask;

    final Handler handler = new Handler();

    private IFragmentsStarter fragmentsStarter;

    private long startTime = 0L;

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

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

    public void setUserModel(UserModel userModel){
        this.userModel = userModel;
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

        Picasso.with(getActivity()).load(qModel.getimgUrl()).into(imgView);
        questionView.setText(qModel.getQuestion());
        answer1.setText(qModel.getAnswers().get(0).first);
        answer2.setText(qModel.getAnswers().get(1).first);
        answer3.setText(qModel.getAnswers().get(2).first);
        answer1.setChecked(false);
        answer2.setChecked(false);
        answer3.setChecked(false);
    }

    private void nextStep(){
        try {

            Log.v("teszt", _questions.size() + "");

            if ((currentQPOS + 1) < _questions.size()) {





                if ((currentQPOS + 1) == (_questions.size() - 1)) {
                    btnNext.setText("Evaluate");
                }
                if (currentQPOS > -1) {
                    getAnswers();
                }
                ++currentQPOS;
                Log.v("currentPos", currentQPOS + "");
                pageNumView.setText("" + (currentQPOS + 1) + "/" + Settings.questionNUM);
                fillQuestionGUI(currentQPOS);
            } else {


                getAnswers();
                winInformationDIalog();
                stoptimertask();
            }
        }catch (Exception ex){
            Log.v("testerror",ex.getMessage());
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


        if ((currentQPOS - 1) < _questions.size()-1){
            btnNext.setText("Next");
        }

        if ((currentQPOS - 1) >= 0){

            --currentQPOS;
            Log.v("currentPos", currentQPOS + "");
            int tempPos = currentQPOS  + 1;
            if (tempPos < 0){
                tempPos = 0;
            }

            pageNumView.setText("" + tempPos + "/" + Settings.questionNUM);
            fillQuestionGUI(currentQPOS);
        }
    }

    public int checkAnswerIsCorrect(){
        int countCorrect = 0;

        Log.v("lofasz",_answers.size() + "");

        for (int i = 0; i < _questions.size(); ++i){
            boolean isCorret = true;

            for (int j = 0; j < _questions.get(i).getAnswers().size();++j){
                if (_questions.get(i).getAnswers().get(j).second != _answers.get(i).get(j)){
                    isCorret = false;
                }
            }
            if (isCorret) countCorrect++;
        }
        return countCorrect;


    }

    private void winInformationDIalog(){



        int resultCorrect = checkAnswerIsCorrect();
        Log.v("win", resultCorrect + "");
        StatisticModel statisticModel = new StatisticModel();
        statisticModel.setMaxPoints(Settings.questionNUM);
        statisticModel.setReachedPoints(resultCorrect);
        statisticModel.setMinReachPoints(Settings.limitPoint);
        statisticModel.setTime(timerView.getText().toString());
        statisticModel.setDate(startDate);

        int success = resultCorrect > Settings.limitPoint ? 1 : 0;

        UpdateResult updateResult = new UpdateResult(resultCorrect);
        updateResult.execute(statisticModel);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fragmentsStarter = (IFragmentsStarter) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public void startTimer() {

        timer = new Timer();

        initializeTimerTask();

        timer.schedule(timerTask, 5, 1000);
    }



    public void stoptimertask() {

        if (timer != null) {

            timer.cancel();

            timer = null;
        }
    }



    public void initializeTimerTask() {

        timerTask = new TimerTask() {

            public void run() {




                handler.post(new Runnable() {

                    public void run() {

                        //get the current timeStamp

                        timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

                        updatedTime = timeSwapBuff + timeInMilliseconds;
                        int secs = (int) (updatedTime / 1000);
                        int mins = secs / 60;
                        secs = secs % 60;
                        int milliseconds = (int) (updatedTime % 1000);
                        timerView.setText("" + mins + ":" + String.format("%02d", secs));

                    }

                });

            }

        };

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
                _questions = ClientController.getQuestions(Settings.questionNUM);
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
                Toast.makeText(getActivity(),"Error occured !",Toast.LENGTH_LONG).show();
                return;
            }

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat simpleDateFormat = new
                    SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");

            startDate = simpleDateFormat.format(calendar.getTime());

            nextStep();
            startTime = SystemClock.uptimeMillis();
            startTimer();


        }
    }

    private class UpdateResult extends AsyncTask<StatisticModel,String,Boolean> {

        private int result;

        public UpdateResult(int result){
            this.result = result;

        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }




        @Override
        protected Boolean doInBackground(StatisticModel... params) {


            try {

                ClientController.insertNewTestResult(params[0],userModel.getId());
                return true;
            }catch (Exception ex)
            {
                Log.e("error","sdsa");
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

            String succStr = result > Settings.limitPoint ? "Congratulation!" : "Failed!";
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage(succStr + " Your result is: " + result + " / " + Settings.questionNUM );
            builder1.setCancelable(true);
            builder1.setPositiveButton("OKE",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.cancel();
                            fragmentsStarter.addClientFragment();

                        }
                    });


            AlertDialog alert11 = builder1.create();
            alert11.show();


        }
    }


}
