package fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.koppa.driverlicensev2.R;

import java.util.ArrayList;

import DataAccessLayer.DataAccessLayer;

/**
 * Created by Attila on 16.12.2015.
 */
public class EditQuestionFragment extends Fragment {

    private int QID;
    private ArrayList<Integer> ids;
    private ArrayList<Pair<String,Boolean>> answers;
    ProgressBar progressBar;
    private EditText etQuestion, etAnswer1, etAnswer2, etAnswer3, etImage;
    private CheckBox cbAns1, cbAns2, cbAns3;
    private Button btnAdd;
    public String question, ans1, ans2, ans3, image;
    public int cb1, cb2, cb3;

    public EditQuestionFragment(int QID, String question, String image, ArrayList<Pair<String,Boolean>> answers){
        this.QID = QID;
        this.question = question;
        this.image = image;
        this.answers = answers;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressBar = (ProgressBar)getActivity().findViewById(R.id.mainprogressBar);
        progressBar.setVisibility(View.GONE);

        View view = inflater.inflate(R.layout.fragment_cu_question, container, false);
        etQuestion = (EditText) view.findViewById(R.id.ET_question);
        etAnswer1 = (EditText) view.findViewById(R.id.ET_answer1);
        cbAns1 = (CheckBox) view.findViewById(R.id.cb_answer1);
        etAnswer2 = (EditText) view.findViewById(R.id.ET_answer2);
        cbAns2 = (CheckBox) view.findViewById(R.id.cb_answer2);
        etAnswer3 = (EditText) view.findViewById(R.id.ET_answer3);
        cbAns3 = (CheckBox) view.findViewById(R.id.cb_answer3);
        etImage = (EditText) view.findViewById(R.id.ET_image);
        btnAdd = (Button) view.findViewById(R.id.btn_add);
        btnAdd.setText("Save modifications");

        etQuestion.setText(question);
        etAnswer1.setText(answers.get(0).first);
        etAnswer2.setText(answers.get(1).first);
        etAnswer3.setText(answers.get(2).first);
        cbAns1.setChecked(answers.get(0).second);
        cbAns2.setChecked(answers.get(1).second);
        cbAns3.setChecked(answers.get(2).second);
        etImage.setText(image);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                question = etQuestion.getText().toString().trim();
                image = etImage.getText().toString().trim();
                ans1 = etAnswer1.getText().toString().trim();
                ans2 = etAnswer2.getText().toString().trim();
                ans3 = etAnswer3.getText().toString().trim();
                cb1 = cbAns1.isChecked() ? 1 : 0;
                cb2 = cbAns2.isChecked() ? 1 : 0;
                cb3 = cbAns3.isChecked() ? 1 : 0;

                CUQuestion cuQuestion = new CUQuestion();
                cuQuestion.execute();

            }
        });


        return view;
    }

    private class CUQuestion extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... params) {

            long id = DataAccessLayer.getInstance().UpdateQuestion(QID, question, image);
            Log.d("QID", String.valueOf(id));
            ids = DataAccessLayer.getInstance().getAnswerId(QID);
            DataAccessLayer.getInstance().UpdateAnswer(ids.get(0), ans1, cb1);
            DataAccessLayer.getInstance().UpdateAnswer(ids.get(1), ans2, cb2);
            DataAccessLayer.getInstance().UpdateAnswer(ids.get(2), ans3, cb3);

            return "OK";

        }

        @Override
        protected void onPostExecute(String message) {

            progressBar.setVisibility(View.GONE);
            if (message.equals("OK")){
                Toast.makeText(getActivity(), "Question saved successfully!", Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                QuestionListFragment editQuestionFragment = new QuestionListFragment();
                fragmentTransaction.replace(R.id.fragment_container, editQuestionFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                return;
            }

        }
    }
}
