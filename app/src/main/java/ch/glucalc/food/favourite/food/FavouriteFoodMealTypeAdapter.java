package ch.glucalc.food.favourite.food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.glucalc.R;
import ch.glucalc.meal.type.MealType;

public class FavouriteFoodMealTypeAdapter extends ArrayAdapter<MealType> {

  private final Context context;
  private final List<MealType> values;

  public FavouriteFoodMealTypeAdapter(Context context, List<MealType> values) {
    super(context, R.layout.favourite_food_meal_type_item_view, values);
    this.context = context;
    this.values = values;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    final View rowView = inflater.inflate(R.layout.favourite_food_meal_type_item_view, parent, false);

    final MealType mealType = values.get(position);
    final TextView nameTextView = (TextView) rowView.findViewById(R.id.name);
    nameTextView.setText(mealType.getName());

    return rowView;
  }
}
