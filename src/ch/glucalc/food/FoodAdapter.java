package ch.glucalc.food;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.glucalc.R;

public class FoodAdapter extends ArrayAdapter<Food> {

  private final Context context;
  private final List<Food> values;

  public FoodAdapter(Context context, List<Food> values) {
    super(context, R.layout.food_item_view, values);
    this.context = context;
    this.values = values;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View rowView = inflater.inflate(R.layout.food_item_view, parent, false);

    final Food food = values.get(position);

    final TextView textView = (TextView) rowView.findViewById(R.id.name);
    textView.setText(food.getName());

    final TextView textView2 = (TextView) rowView.findViewById(R.id.quantity);
    textView2.setText("" + food.getQuantity() + " " + food.getUnit());

    final TextView textView3 = (TextView) rowView.findViewById(R.id.carbonhydrate);
    textView3.setText("" + food.getCarbonhydrate());

    return rowView;
  }

}
