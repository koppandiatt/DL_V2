package fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.koppa.driverlicensev2.R;

import java.util.ArrayList;

import DataAccessLayer.DataAccessLayer;
import Models.AdminModel;
import Models.UserModel;
import Tools.QuestionListAdapter;
import Tools.UserListAdapter;

/**
 * Created by Attila on 17.12.2015.
 */
public class AdminStatisticFragment extends Fragment {

    private ListView listView;
    private ProgressBar progressBar;
    private UserListAdapter userListAdapter;
    private ArrayList<UserModel> userModelArrayList;
    private View view;

    public AdminStatisticFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_list_users,container,false);

        progressBar = (ProgressBar) getActivity().findViewById(R.id.mainprogressBar);
        progressBar.setVisibility(View.GONE);

        listView = (ListView) view.findViewById(R.id.lvQuestion);

       /* String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);*/

        LoadQuestion loadQuestion = new LoadQuestion();
        loadQuestion.execute("");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(getActivity(), "" + us.get(position).getId(), Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                StatisticFragment statisticFragment = new StatisticFragment();
                UserModel user = new UserModel();
                user.setId(userModelArrayList.get(position).getId());
                statisticFragment.setUserModel(user);
                fragmentTransaction.replace(R.id.fragment_container, statisticFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private class LoadQuestion extends AsyncTask<String,String,String> {

        Context context;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            context = view.getContext();
        }


        @Override
        protected String doInBackground(String... params) {
            userModelArrayList = DataAccessLayer.getInstance().getClients();
            userListAdapter = new UserListAdapter(context, 0, userModelArrayList);
            return "OK";
        }

        @Override
        protected void onPostExecute(String message) {

            listView.setAdapter(userListAdapter);
            progressBar.setVisibility(View.GONE);

        }
    }

}
