package ch.glucalc.food.favourite.food;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;
import ch.glucalc.food.Food;

import static ch.glucalc.food.favourite.food.FavouriteFoodConstants.FAVOURITE_FOOD_ID_PARAMETER;

public class EditFavouriteFoodFragment extends Fragment {

    private static String TAG = "GluCalc";

    private EditText favouriteFoodQuantity;
    private EditText favouriteFoodCarbonhydrate;
    private FavouriteFood favouriteFood;
    private Food food;

    private OnFavouriteFoodSaved mCallback;

    // Container Activity must implement this interface
    public interface OnFavouriteFoodSaved {

        public void openFavouriteFoodListFragment(long mealTypeId);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnFavouriteFoodSaved) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFavouriteFoodSaved");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        favouriteFood = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).loadFavouriteFood(getFavouriteFoodId());
        food = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).loadFood(favouriteFood.getFoodId());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        log("EditFavouriteFoodFragment.onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.accept_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("EditFavouriteFoodFragment.onCreate");
        View layout = inflater.inflate(R.layout.edit_favourite_food, container, false);
        initFieldsAndButtonForEdition(layout);
        return layout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        log("EditFoodFragment.onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.save:
                initFavouriteFoodFromFields();
                //    DialogHelper.displayErrorMessageAllFieldsMissing(getActivity());
                //} else {
                    saveFavouriteFood();
                    log("EditFoodFragment.onClick : DONE");
                    mCallback.openFavouriteFoodListFragment(favouriteFood.getMealTypeId());
                //}
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        log("EditFoodFragment.onDestroy");
        super.onDestroy();
    }

    private void initFieldsAndButtonForEdition(View layout) {
        log("EditFoodFragment.initFieldsAndButtonForEdition");

        favouriteFoodQuantity = (EditText) layout.findViewById(R.id.favourite_food_quantity_edittext);
        favouriteFoodCarbonhydrate = (EditText) layout.findViewById(R.id.favourite_food_carbonhydrate_edittext);

        updateFieldText(favouriteFoodQuantity, String.valueOf(favouriteFood.getQuantity()));
        updateFieldText(favouriteFoodCarbonhydrate, String.valueOf(favouriteFood.getCarbonhydrate()));

        TextView favourite_food_quantity_unit = (TextView) layout.findViewById(R.id.favourite_food_quantity_unit_textview);
        favourite_food_quantity_unit.setText(food.getUnit());

        TextView selected_food_name = (TextView) layout.findViewById(R.id.favourite_food_selected_food_name_value_textview);
        selected_food_name.setText(food.getName());

        TextView selected_food_quantity = (TextView) layout.findViewById(R.id.favourite_food_selected_food_quantity_value_textview);
        selected_food_quantity.setText(String.valueOf(food.getQuantity()));

        TextView selected_food_carbonhydrate = (TextView) layout.findViewById(R.id.favourite_food_selected_food_carbonhydrate_value_textview);
        selected_food_carbonhydrate.setText(String.valueOf(food.getCarbonhydrate()));

        TextView selected_food_unit = (TextView) layout.findViewById(R.id.favourite_food_selected_food_unit_value_textview);
        selected_food_unit.setText(food.getUnit());


    }

    private void updateFieldText(EditText editText, String text) {
        editText.setText(text);
    }

    private long getFavouriteFoodId() {
        return getArguments().getLong(FAVOURITE_FOOD_ID_PARAMETER);
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

    private void saveFavouriteFood() {
        GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).updateFavouriteFood(favouriteFood);
        //propagateResultForEdition();
    }

    private void initFavouriteFoodFromFields() {
        final String newFoodQuantityText = favouriteFoodQuantity.getText().toString();
        try {
            final Float newFoodQuantityAsFloat = Float.valueOf(newFoodQuantityText);
            favouriteFood.setQuantity(newFoodQuantityAsFloat);
        } catch (final NumberFormatException nfe) {
        }

        final String newFoodCarbonHydrateText = favouriteFoodCarbonhydrate.getText().toString();
        try {
            final Float newFoodCarbonHydrateAsFloat = Float.valueOf(newFoodCarbonHydrateText);
            favouriteFood.setCarbonhydrate(newFoodCarbonHydrateAsFloat);
        } catch (final NumberFormatException nfe) {
        }
    }

//    private void propagateResultForEdition() {
//        final Intent intent = new Intent();
//        intent.putExtra(FavouriteFoodConstants.MODIFIED_ID_RESULT, getFavouriteFoodId());
//    }

}
