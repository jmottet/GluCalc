package ch.glucalc.food.favourite.food;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import ch.glucalc.DecimalDigitsInputFilter;
import ch.glucalc.DialogHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.KeyboardHelper;
import ch.glucalc.R;
import ch.glucalc.food.Food;

import static ch.glucalc.food.favourite.food.FavouriteFoodConstants.FAVOURITE_FOOD_ID_PARAMETER;

public class EditFavouriteFoodFragment extends Fragment {

    private static String TAG = "GluCalc";

    private EditText favouriteFoodQuantity;
    private EditText favouriteFoodCarbohydrate;
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
                if (favouriteFood.areSomeMandatoryFieldsMissing()) {
                    DialogHelper.displayErrorMessageAllFieldsMissing(getActivity());
                } else {
                    KeyboardHelper.hideKeyboard(getActivity());
                    saveFavouriteFood();
                    log("EditFoodFragment.onClick : DONE");
                    mCallback.openFavouriteFoodListFragment(favouriteFood.getMealTypeId());
                }
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
        final ImageButton buttonQuantityClearText = (ImageButton) layout.findViewById(R.id.quantity_clear_text);
        final ImageButton buttonCarbohydrateClearText = (ImageButton) layout.findViewById(R.id.carbohydrate_clear_text);

        favouriteFoodQuantity = (EditText) layout.findViewById(R.id.favourite_food_quantity_edittext);
        favouriteFoodCarbohydrate = (EditText) layout.findViewById(R.id.favourite_food_carbohydrate_edittext);

        favouriteFoodQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showClearButtonIfNecessary(favouriteFoodQuantity, buttonQuantityClearText);
            }
        });

        favouriteFoodQuantity.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                Float newFoodQuantityAsFloat = null;
                showClearButtonIfNecessary(favouriteFoodQuantity, buttonQuantityClearText);

                try {
                    newFoodQuantityAsFloat = Float.valueOf(favouriteFoodQuantity.getText().toString());
                } catch (final NumberFormatException nfe) {
                }


                if (favouriteFoodQuantity.hasFocus() && mustFieldBeComputed(true, false)) {
                    Float result = (newFoodQuantityAsFloat != null ? newFoodQuantityAsFloat : 0) * food.getCarbonhydrate() / food.getQuantity();
                    favouriteFoodCarbohydrate.setText(format(result));
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        buttonQuantityClearText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                favouriteFoodQuantity.setText("");
                buttonQuantityClearText.setVisibility(View.INVISIBLE);
            }
        });

        favouriteFoodCarbohydrate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showClearButtonIfNecessary(favouriteFoodCarbohydrate, buttonCarbohydrateClearText);
            }
        });

        favouriteFoodCarbohydrate.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                Float newFoodCarbohydrateAsFloat = null;
                showClearButtonIfNecessary(favouriteFoodCarbohydrate, buttonCarbohydrateClearText);

                try {
                    newFoodCarbohydrateAsFloat = Float.valueOf(favouriteFoodCarbohydrate.getText().toString());
                } catch (final NumberFormatException nfe) {
                }


                if (favouriteFoodCarbohydrate.hasFocus() && mustFieldBeComputed(false, true)) {
                    Float result = (newFoodCarbohydrateAsFloat != null ? newFoodCarbohydrateAsFloat : 0) * food.getQuantity() / food.getCarbonhydrate();
                    favouriteFoodQuantity.setText(format(result));
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        buttonCarbohydrateClearText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                favouriteFoodCarbohydrate.setText("");
                buttonCarbohydrateClearText.setVisibility(View.INVISIBLE);

            }
        });

        updateFieldText(favouriteFoodQuantity, String.valueOf(favouriteFood.getQuantity()));
        updateFieldText(favouriteFoodCarbohydrate, String.valueOf(favouriteFood.getCarbonhydrate()));

        TextView favourite_food_quantity_unit = (TextView) layout.findViewById(R.id.favourite_food_quantity_unit_textview);
        favourite_food_quantity_unit.setText(food.getUnit());

        TextView selected_food_name = (TextView) layout.findViewById(R.id.favourite_food_selected_food_name_value_textview);
        selected_food_name.setText(food.getName());

        TextView selected_food_quantity = (TextView) layout.findViewById(R.id.favourite_food_selected_food_quantity_value_textview);
        selected_food_quantity.setText(String.valueOf(food.getQuantity()));

        TextView selected_food_carbohydrate = (TextView) layout.findViewById(R.id.favourite_food_selected_food_carbohydrate_value_textview);
        selected_food_carbohydrate.setText(String.valueOf(food.getCarbonhydrate()));

        TextView selected_food_unit = (TextView) layout.findViewById(R.id.favourite_food_selected_food_unit_value_textview);
        selected_food_unit.setText(food.getUnit());
    }

    private void showClearButtonIfNecessary(EditText champEditable, ImageButton boutonEffacer) {
        if (champEditable.hasFocus() && !TextUtils.isEmpty(champEditable.getText())) {
            boutonEffacer.setVisibility(View.VISIBLE);
        } else {
            boutonEffacer.setVisibility(View.INVISIBLE);
        }
    }

    private boolean mustFieldBeComputed(boolean hasQuantityChanged, boolean hasCarbohydrateChanged) {
        boolean result = false;
        Float foodQuantityAsFloat = null;
        try {
            foodQuantityAsFloat = Float.valueOf(favouriteFoodQuantity.getText().toString());
        } catch (final NumberFormatException nfe) {
            if (hasQuantityChanged) {
                return false;
            } else {
                foodQuantityAsFloat = Float.valueOf(0);
            }
        }



        Float foodCarbohydrateAsFloat = null;
        try {
            foodCarbohydrateAsFloat = Float.valueOf(favouriteFoodCarbohydrate.getText().toString());
        } catch (final NumberFormatException nfe) {
            if (hasCarbohydrateChanged) {
                return false;
            } else {
                foodCarbohydrateAsFloat = Float.valueOf(0);
            }
        }
        return !(format(foodQuantityAsFloat).equals(format(foodCarbohydrateAsFloat * food.getQuantity() / food.getCarbonhydrate())));
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
    }

    private void initFavouriteFoodFromFields() {
        final String newFoodQuantityText = favouriteFoodQuantity.getText().toString();
        try {
            final Float newFoodQuantityAsFloat = Float.valueOf(newFoodQuantityText);
            favouriteFood.setQuantity(newFoodQuantityAsFloat);
        } catch (final NumberFormatException nfe) {
            favouriteFood.setQuantity(null);
        }

        final String newFoodCarbonHydrateText = favouriteFoodCarbohydrate.getText().toString();
        try {
            final Float newFoodCarbonHydrateAsFloat = Float.valueOf(newFoodCarbonHydrateText);
            favouriteFood.setCarbonhydrate(newFoodCarbonHydrateAsFloat);
        } catch (final NumberFormatException nfe) {
            favouriteFood.setCarbonhydrate(null);
        }
    }

    private String format(float number) {
        String result =  String.format("%.2f", number).replaceAll(",", ".");
        if (result.endsWith(".00")) {
            result = result.substring(0, result.length() - 3);
        }
        return result;
    }

//    private void propagateResultForEdition() {
//        final Intent intent = new Intent();
//        intent.putExtra(FavouriteFoodConstants.MODIFIED_ID_RESULT, getFavouriteFoodId());
//    }

}
