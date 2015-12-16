package Models;

import android.util.Log;

import java.sql.ResultSet;
import java.util.ArrayList;

import DataAccessLayer.DataAccessLayer;

/**
 * Created by Attila on 16.12.2015.
 */
public class AdminModel {

    private static final String QUESTIONS  = "SELECT * FROM questions WHERE Status = 1";

    public static ArrayList<QuestionModel> getQuestions(){

        ArrayList<QuestionModel> questionModels = new ArrayList<QuestionModel>();

        try {
            ResultSet resultSet = DataAccessLayer.getInstance().select(QUESTIONS);

            int QID = -1;
            while (resultSet.next())
            {
                QuestionModel questionModel = new QuestionModel();
                QID = resultSet.getInt("ID");
                questionModel.setId(QID);
                questionModel.setQuestion(resultSet.getString("Text"));
                questionModel.setimgUrl(resultSet.getString("Image"));

                String query = "SELECT * FROM answers WHERE Status = 1 and QID = '" + QID + "'" ;

                ResultSet resultSet2 = DataAccessLayer.getInstance().select(query);
                int correctIndex = 1;

                while (resultSet2.next()) {
                    int id = resultSet2.getInt("Correct");
                    boolean temp = id > 0 ? true: false;
                    questionModel.addNewAnswer(resultSet2.getString("Text"),temp);

                    Log.v("teszt",resultSet2.getString("Text"));
                    if (id == 1) questionModel.setCorrectAnsIndex(correctIndex);
                    correctIndex++;
                }

                questionModels.add(questionModel);
            }
        }catch (Exception e){
            Log.e("Error", e.getMessage());
        }

        return questionModels;
    }
}
