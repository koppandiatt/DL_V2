package fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koppa.driverlicensev2.R;

import java.util.ArrayList;

import Controller.ClientController;
import Models.UserModel;
import Models.StatisticModel;
import Tools.StatisticListAdapter;


public class StatisticFragment extends Fragment {


    private ListView listView;
    private StatisticListAdapter statisticListAdapter;
    private ProgressBar progressBar;

    private UserModel userModel;
    ArrayList<StatisticModel> statistics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        listView = (ListView) view.findViewById(R.id.statlist);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                detailView(statistics.get(position));
            }
        });


        progressBar = (ProgressBar) getActivity().findViewById(R.id.mainprogressBar);
        StatisData statisData = new StatisData();
        statisData.execute();
        return view;
    }

    public void setUserModel(UserModel userModel){
        this.userModel = userModel;
    }

    private void detailView(StatisticModel statisticModel){
       final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.detaildialog_layout);

        dialog.setTitle("Detail Window!");

        // set the custom dialog components - text, image and button
        TextView textDate = (TextView) dialog.findViewById(R.id.DVdate);
        TextView textTime = (TextView) dialog.findViewById(R.id.DVtime);
        TextView textEarnedPoints = (TextView) dialog.findViewById(R.id.DVearnedPoints);
        TextView textMaxPoints = (TextView) dialog.findViewById(R.id.DVmaxPoints);
        TextView textLimitPoints = (TextView) dialog.findViewById(R.id.DVlimitPoints);
        TextView textSuccess = (TextView) dialog.findViewById(R.id.DVsuccess);

        textDate.setText(statisticModel.getDate());
        textTime.setText(statisticModel.getTime());
        textEarnedPoints.setText(String.valueOf(statisticModel.getReachedPoints()));
        textMaxPoints.setText(String.valueOf(statisticModel.getMaxPoints()));
        textLimitPoints.setText(String.valueOf(statisticModel.getMinReachPoints()));
        textSuccess.setText(String.valueOf(statisticModel.getSuccess() > 0 ? true : false));

        Button dialogButton = (Button) dialog.findViewById(R.id.DVokebtn);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void initializeListView(){
        Log.v("size", statistics.size() + "");
        statisticListAdapter = new StatisticListAdapter(getActivity(),statistics);
        listView.setAdapter(statisticListAdapter);
    }

    private class StatisData extends AsyncTask<String,String,Boolean> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Boolean doInBackground(String... params) {


            try {
                Thread.sleep(200);
                statistics = ClientController.getClientStatistic(userModel.getId());

                return true;
            }catch (Exception ex)
            {
                Log.e("error", "Error occured at the get question!");
            }
            return false;


        }

        @Override
        protected void onPostExecute(Boolean message) {


            progressBar.setVisibility(View.GONE);
            if (message == false){
                Toast.makeText(getActivity(), "Error occured !", Toast.LENGTH_LONG).show();
                return;
            }
            initializeListView();

        }
    }

}
