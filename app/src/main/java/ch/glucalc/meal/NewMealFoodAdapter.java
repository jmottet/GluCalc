package ch.glucalc.meal;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.glucalc.R;
import ch.glucalc.meal.diary.FoodDiary;

public class NewMealFoodAdapter extends ArrayAdapter<FoodDiary> {

    private final Context context;
    private final List<FoodDiary> values;

    public NewMealFoodAdapter(Context context, List<FoodDiary> values) {
        super(context, R.layout.row_favourite_food_view, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.row_new_meal_food_view, parent, false);

        final FoodDiary newMealFood = values.get(position);
        final TextView textView = (TextView) rowView.findViewById(R.id.name);
        textView.setText(newMealFood.getFoodName());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        final TextView textView2 = (TextView) rowView.findViewById(R.id.quantity);
        textView2.setText("" + newMealFood.getQuantity() + " g");

        final TextView textView3 = (TextView) rowView.findViewById(R.id.carbonhydrate);
        textView3.setText("" + newMealFood.getCarbohydrate() + " g");

        return rowView;
    }

}
