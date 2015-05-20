package ch.glucalc.food.favourite.food;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ch.glucalc.R;
import ch.glucalc.food.Food;
import ch.glucalc.meal.type.MealType;

public class FavouriteFoodAdapter extends ArrayAdapter<FavouriteFood> {

    private final Context context;
    private final List<FavouriteFood> values;

    public FavouriteFoodAdapter(Context context, List<FavouriteFood> values) {
        super(context, R.layout.row_favourite_food_view, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.row_favourite_food_view, parent, false);

        final FavouriteFood favouriteFood = values.get(position);
        final TextView textView = (TextView) rowView.findViewById(R.id.name);
        textView.setText(favouriteFood.getName());

        final TextView textView2 = (TextView) rowView.findViewById(R.id.quantity);
        textView2.setText("" + favouriteFood.getQuantity() + " g");

        final TextView textView3 = (TextView) rowView.findViewById(R.id.carbonhydrate);
        textView3.setText("" + favouriteFood.getCarbonhydrate());

        return rowView;
    }
}
