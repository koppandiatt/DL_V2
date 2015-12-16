package fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.koppa.driverlicensev2.R;

import DataAccessLayer.DataAccessLayer;

/**
 * Created by Attila on 15.12.2015.
 */
public class AddQuestionFragment extends Fragment {

    private ProgressBar progressBar;
    private EditText etQuestion, etAnswer1, etAnswer2, etAnswer3, etImage;
    private CheckBox cbAns1, cbAns2, cbAns3;
    private Button btnAdd;

    public String question, ans1, ans2, ans3, image;
    public int cb1, cb2, cb3;

    public AddQuestionFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        btnAdd.setText("Add Question");
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class CUQuestion extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... params) {

            long id = DataAccessLayer.getInstance().InsertNewQuestion(question, image);
            Log.d("QID", String.valueOf(id));
            DataAccessLayer.getInstance().InsertNewAnswers(id, ans1, cb1);
            DataAccessLayer.getInstance().InsertNewAnswers(id, ans2, cb2);
            DataAccessLayer.getInstance().InsertNewAnswers(id, ans3, cb3);

            return "OK";

        }

        @Override
        protected void onPostExecute(String message) {

            progressBar.setVisibility(View.GONE);
            if (message.equals("OK")){
                etQuestion.setText("");
                etImage.setText("");
                etAnswer1.setText("");
                etAnswer2.setText("");
                etAnswer3.setText("");
                cbAns1.setChecked(false);
                cbAns2.setChecked(false);
                cbAns3.setChecked(false);
                Toast.makeText(getActivity(), "Question inserted successfully!", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    }
}
