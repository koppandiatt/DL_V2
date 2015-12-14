package Models;

import java.util.ArrayList;

/**
 * Created by kovacslev on 12/13/2015.
 */
public class QuestionModel {

    private int _correctAnsIndex;

    private  int _id;

    private ArrayList<String> _answers;

    private String _question;

    private String _imgUrl;

    public QuestionModel(ArrayList<String> answers, int correctAnsIndex, int id){

        this._answers = answers;
        this._correctAnsIndex = correctAnsIndex;
        this._id = id;

    }

    public QuestionModel(){
        this._id = -1;
        this._correctAnsIndex = -1;
        this._answers = new ArrayList<String>();
        this._question = null;
        this._imgUrl = null;

    }

    public ArrayList<String> getAnswers() {
        return _answers;
    }


    public void setAnswers(ArrayList<String> answers) {
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


    public void addNewAnswer(String answer){
        this._answers.add(answer);
    }


}
