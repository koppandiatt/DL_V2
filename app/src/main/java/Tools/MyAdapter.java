package Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.koppa.driverlicensev2.R;

import java.util.ArrayList;

import Models.StatisticModel;

/**
 * Created by kovacslev on 12/16/2015.
 */
public class MyAdapter  extends ArrayAdapter<StatisticModel> {
    private final Context context;
    private ArrayList<StatisticModel> statisticModels;

    public MyAdapter(Context context, ArrayList<StatisticModel> statModels) {
        super(context, R.layout.statitem_layout, statModels);
        this.context = context;
        statisticModels = statModels;

        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.statitem_layout, parent, false);
        TextView statTVtime = (TextView) rowView.findViewById(R.id.statTVtime);
        TextView statTVsuccess = (TextView) rowView.findViewById(R.id.statTVSuccess);
        TextView statTVpoints = (TextView) rowView.findViewById(R.id.statTVpoints);

        statTVtime.setText(statisticModels.get(position).getTime());
        statTVsuccess.setText(statisticModels.get(position).getSuccess());
        statTVpoints.setText(statisticModels.get(position).getReachedPoints());
        // Change the icon for Windows and iPhone


        return rowView;
    }
}
