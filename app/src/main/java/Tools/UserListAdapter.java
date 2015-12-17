package Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koppa.driverlicensev2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Models.QuestionModel;
import Models.UserModel;

/**
 * Created by Attila on 17.12.2015.
 */
public class UserListAdapter extends ArrayAdapter<UserModel> {

    private final Context context;
    private ArrayList<UserModel> userModels;

    public UserListAdapter(Context context, int resourceID, ArrayList<UserModel> userModels){
        super(context, resourceID, userModels);
        this.context = context;
        this.userModels = userModels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        textView.setText(userModels.get(position).getEmail());


        return rowView;
    }

}
