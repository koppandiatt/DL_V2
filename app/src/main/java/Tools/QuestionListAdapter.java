package Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.koppa.driverlicensev2.R;

import fragments.QuestionListFragment;

/**
 * Created by Attila on 16.12.2015.
 */

public class QuestionListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    public QuestionListAdapter(Context context, String[] values){
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_item, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        textView.setText(values[position]);
        return rowView;
    }
}
