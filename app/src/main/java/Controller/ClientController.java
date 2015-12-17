package Controller;

import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;

import java.sql.ResultSet;
import java.util.ArrayList;

import DataAccessLayer.DataAccessLayer;
import Models.QuestionModel;
import Models.StatisticModel;

/**
 * Created by kovacslev on 12/16/2015.
 */
public class ClientController {

    private static final String QRANDOMQUESTION  = "SELECT * FROM questions WHERE Status = 1 ORDER BY RAND() LIMIT 1";

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
                    questionModel.setimgUrl(resultSet.getString("Image"));

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


    public static void insertNewTestResult(StatisticModel statisticModel,long clientId){

        String query = "INSERT INTO Statistics(FID,Points,Time,Success,maxPoints,limitPoint,Date) VALUES (" +
                       clientId + "," + statisticModel.getReachedPoints() + ",'" +statisticModel.getTime() + "'," +
                        statisticModel.getSuccess() + "," + statisticModel.getMaxPoints() + "," + statisticModel.getMinReachPoints() + ",'" + statisticModel.getDate() +  "')";


        Log.v("query",query);

        DataAccessLayer.getInstance().insert(query);
    }

    public static ArrayList<StatisticModel> getClientStatistic(long id){
        try{

            ArrayList<StatisticModel> list = new ArrayList<StatisticModel>();
            String query = "SELECT * FROM Statistics WHERE FID = " + id ;
            ResultSet resultSet = DataAccessLayer.getInstance().select(query);
            while (resultSet.next()) {
                StatisticModel statisticModel = new StatisticModel();
                statisticModel.setTime(resultSet.getString("Time"));
                statisticModel.setMinReachPoints(resultSet.getInt("limitPoint"));
                statisticModel.setMaxPoints(resultSet.getInt("maxPoints"));
                statisticModel.setReachedPoints(resultSet.getInt("Points"));
                statisticModel.setSuccess(resultSet.getInt("Success"));
                statisticModel.setDate(resultSet.getString("Date"));

                Log.v("model",resultSet.getInt("Success")  + "");


                list.add(statisticModel);

            }
            return  list;


        }catch(Exception ex){
            Log.e("dberror",ex.getMessage());
        }

        return null;
    }

}
