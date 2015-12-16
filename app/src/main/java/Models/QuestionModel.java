package Models;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by kovacslev on 12/13/2015.
 */
public class QuestionModel {

    private int _correctAnsIndex;

    private  int _id;

    private ArrayList<Pair<String,Boolean>> _answers;



    private String _question;

    private String _imgUrl;

    private boolean _wasAnswered;

    public QuestionModel(ArrayList<Pair<String,Boolean>> answers, int correctAnsIndex, int id){

        this._answers = answers;
        this._correctAnsIndex = correctAnsIndex;
        this._id = id;

    }

    public QuestionModel(){
        this._id = -1;
        this._correctAnsIndex = -1;
        this._answers = new ArrayList<Pair<String,Boolean>>();
        this._question = null;
        this._imgUrl = null;
        this._wasAnswered = false;

    }

    public ArrayList<Pair<String,Boolean>> getAnswers() {
        return _answers;
    }


    public void setAnswers(ArrayList<Pair<String,Boolean>> answers) {
        this._answers = answers;
    }

    public void setCorrectAnsIndex(int correctAnsIndex) {
        this._correctAnsIndex = correctAnsIndex;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public void setQuestion(String _question) {
        this._question = _question;
    }

    public void setHaveAnsweredCorrect(boolean _haveAnsweredCorrect) {
        this._wasAnswered = _haveAnsweredCorrect;
    }

    public void setimgUrl(String _imgUrl) {
        this._imgUrl = _imgUrl;
    }

    public String getimgUrl() {
        return _imgUrl;
    }

    public String getQuestion() {
        return _question;
    }

    public int getId() {
        return _id;
    }

    public int getCorrectAnsIndex() {
        return _correctAnsIndex;
    }

    public boolean getHaveAnsweredCorrect(boolean _haveAnsweredCorrect) {
        return _haveAnsweredCorrect;
    }


    public void addNewAnswer(String answer, boolean correct){
        this._answers.add(new Pair<String, Boolean>(answer,correct));
    }



}
