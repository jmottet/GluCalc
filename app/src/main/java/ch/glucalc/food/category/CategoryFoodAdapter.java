package ch.glucalc.food.category;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.glucalc.R;

public class CategoryFoodAdapter extends ArrayAdapter<CategoryFood> {

  private final Context context;
  private final List<CategoryFood> values;

  public CategoryFoodAdapter(Context context, List<CategoryFood> values) {
    super(context, R.layout.category_food_item_view, values);
    this.context = context;
    this.values = values;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View rowView = inflater.inflate(R.layout.category_food_item_view, parent, false);

    final CategoryFood categoryFood = values.get(position);
    final TextView nameTextView = (TextView) rowView.findViewById(R.id.name);
    nameTextView.setText(categoryFood.getName());

    return rowView;
  }
}
