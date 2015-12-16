package Models;

import android.util.Log;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import DataAccessLayer.DataAccessLayer;

/**
 * Created by kovac on 12/14/2015.
 */
public class ClientModel {


    private static final String QRANDOMQUESTION  = "SELECT * FROM questions WHERE Status = 1 ORDER BY RAND() LIMIT 1";

    //  settings fields

    private static int clientID;

    private static int questionNUM;

    private static int time;


    public static ArrayList<QuestionModel> getQuestion(int n){


        ArrayList<QuestionModel> questionModels = new ArrayList<QuestionModel>();

        try {
            for (int i = 0; i < n;++i){

                QuestionModel questionModel = new QuestionModel();
                //"SELECT * FROM questions WHERE Status = 1 ORDER BY RAND() LIMIT 1";

                ResultSet resultSet = DataAccessLayer.getInstance().select(QRANDOMQUESTION);
                int QID = -1;
                if (resultSet.next()) {



                    QID = resultSet.getInt("ID");
                    questionModel.setId(QID);
                    questionModel.setQuestion(resultSet.getString("Text"));
                    questionModel.setimgUrl("Image");

                    Log.v("teszt", questionModel.getQuestion());

                }

                Log.v("QID", "" + QID);

                String query = "SELECT * FROM answers WHERE Status = 1 and QID = '" + QID + "'" ;

                resultSet = DataAccessLayer.getInstance().select(query);
                int correctIndex = 1;

                while (resultSet.next()) {
                    int id = resultSet.getInt("Correct");
                    boolean temp = id > 0 ? true: false;
                    questionModel.addNewAnswer(resultSet.getString("Text"),temp);

                    Log.v("teszt",resultSet.getString("Text"));
                    if (id == 1) questionModel.setCorrectAnsIndex(correctIndex);
                    correctIndex++;
                }

                questionModels.add(questionModel);
            }
            return  questionModels;

        } catch (Exception ex) {
            Log.e("dberror", ex.getMessage());
            return null;
        }

    }

    public static int getClientID() {
        return clientID;
    }

    public static int getQuestionNUM() {
        return questionNUM;
    }

    public static int getTime() {
        return time;
    }
}
