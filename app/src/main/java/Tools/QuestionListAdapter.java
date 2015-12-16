package Tools;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koppa.driverlicensev2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Models.AdminModel;
import Models.QuestionModel;
import fragments.QuestionListFragment;

/**
 * Created by Attila on 16.12.2015.
 */

public class QuestionListAdapter extends ArrayAdapter<QuestionModel> {

    private final Context context;
    private ArrayList<QuestionModel> questionModels;
    private static LayoutInflater inflater = null;

    public QuestionListAdapter(Context context, int resourceID, ArrayList<QuestionModel> questionModels){
        super(context, resourceID, questionModels);
        this.context = context;
        this.questionModels = questionModels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(questionModels.get(position).getQuestion());
        if(!questionModels.get(position).getimgUrl().equals(""))
        Picasso
                .with(context)
                .load(questionModels.get(position).getimgUrl())
                .fit()
                .into(imageView);

        return rowView;
    }
}
