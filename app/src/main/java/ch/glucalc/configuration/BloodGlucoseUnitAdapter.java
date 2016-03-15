package ch.glucalc.configuration;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.glucalc.EnumBloodGlucose;
import ch.glucalc.R;
import ch.glucalc.meal.diary.FoodDiary;

public class BloodGlucoseUnitAdapter extends ArrayAdapter<EnumBloodGlucose> {

    private final Context context;
    private final List<EnumBloodGlucose> values;
    private int selected_pos;

    public BloodGlucoseUnitAdapter(Context context, List<EnumBloodGlucose> values, int selected_pos) {
        super(context, R.layout.row_blood_glucose_unit_view, values);
        this.context = context;
        this.values = values;
        this.selected_pos = selected_pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.row_blood_glucose_unit_view, parent, false);

        if (position == getSelectedPosition()) {
            rowView.setBackgroundColor(rowView.getResources().getColor(R.color.lightSkyBlue));
        } else {
            rowView.setBackground(null);
        }

        final EnumBloodGlucose bloodGlucoseUnit = values.get(position);

        final TextView textView = (TextView) rowView.findViewById(R.id.description);
        textView.setText(context.getString(bloodGlucoseUnit.getDescriptionKey()));

        final TextView textView2 = (TextView) rowView.findViewById(R.id.abbreviation);
        textView2.setText(bloodGlucoseUnit.getLabel());

        return rowView;
    }

    public void setSelectedPosition(int selected_pos){
        this.selected_pos = selected_pos;
    }

    public int getSelectedPosition(){
        return selected_pos;
    }

}
