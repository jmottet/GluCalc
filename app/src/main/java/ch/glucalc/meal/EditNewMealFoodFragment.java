package ch.glucalc.meal;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ch.glucalc.DialogHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;
import ch.glucalc.food.Food;
import ch.glucalc.meal.diary.FoodDiary;

import static ch.glucalc.meal.NewMealConstants.NEW_MEAL_FOOD_ID_PARAMETER;


public class EditNewMealFoodFragment extends Fragment {

    private static String TAG = "GluCalc";

    private EditText newMealFoodQuantity;
    private EditText newMealFoodCarbohydrate;
    private FoodDiary newMealFood;
    private Food food;

    private OnNewMealFoodSaved mCallback;

    public interface OnNewMealFoodSaved {

        void openNewMealSecondStepFragment(long mealDiaryId);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnNewMealFoodSaved) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNewMealFoodSaved");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        newMealFood = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).loadFoodDiary(getNewMealFoodId());
        food = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).loadFoodByName(newMealFood.getFoodName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        log("EditNewMealFoodFragment.onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.accept_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("EditNewMealFoodFragment.onCreate");
        View layout = inflater.inflate(R.layout.edit_new_meal_food, container, false);
        initFieldsAndButtonForEdition(layout);
        return layout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        log("EditNewMealFoodFragment.onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.save:
                initNewMealFoodFromFields();
                if (newMealFood.areSomeMandatoryFieldsMissing()) {
                    DialogHelper.displayErrorMessageAllFieldsMissing(getActivity());
                } else {
                    saveNewMealFood();
                    log("EditNewMealFoodFragment.onClick : DONE");
                    mCallback.openNewMealSecondStepFragment(newMealFood.getMealDiaryId());
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        log("EditNewMealFoodFragment.onDestroy");
        super.onDestroy();
    }

    private void initFieldsAndButtonForEdition(View layout) {
        log("EditNewMealFoodFragment.initFieldsAndButtonForEdition");

        newMealFoodQuantity = (EditText) layout.findViewById(R.id.new_meal_food_quantity_edittext);
        newMealFoodCarbohydrate = (EditText) layout.findViewById(R.id.new_meal_food_carbohydrate_edittext);

        newMealFoodQuantity.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                Float newFoodQuantityAsFloat = null;
                try {
                    newFoodQuantityAsFloat = Float.valueOf(newMealFoodQuantity.getText().toString());
                } catch (final NumberFormatException nfe) {
                }


                if (mustFieldBeComputed()) {
                    Float result = (newFoodQuantityAsFloat != null ? newFoodQuantityAsFloat : 0) * food.getCarbonhydrate() / food.getQuantity();
                    newMealFoodCarbohydrate.setText(format(result));
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        newMealFoodCarbohydrate.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                Float newFoodCarbohydrateAsFloat = null;
                try {
                    newFoodCarbohydrateAsFloat = Float.valueOf(newMealFoodCarbohydrate.getText().toString());
                } catch (final NumberFormatException nfe) {
                }


                if (mustFieldBeComputed()) {
                    Float result = (newFoodCarbohydrateAsFloat != null ? newFoodCarbohydrateAsFloat : 0) * food.getQuantity() / food.getCarbonhydrate();
                    newMealFoodQuantity.setText(format(result));

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        updateFieldText(newMealFoodQuantity, String.valueOf(newMealFood.getQuantity()));
        updateFieldText(newMealFoodCarbohydrate, String.valueOf(newMealFood.getCarbohydrate()));

        TextView new_meal_food_quantity_unit = (TextView) layout.findViewById(R.id.new_meal_food_quantity_unit_textview);
        new_meal_food_quantity_unit.setText(food.getUnit());

        TextView selected_food_name = (TextView) layout.findViewById(R.id.new_meal_food_selected_food_name_value_textview);
        selected_food_name.setText(food.getName());

        TextView selected_food_quantity = (TextView) layout.findViewById(R.id.new_meal_food_selected_food_quantity_value_textview);
        selected_food_quantity.setText(String.valueOf(food.getQuantity()));

        TextView selected_food_carbohydrate = (TextView) layout.findViewById(R.id.new_meal_food_selected_food_carbohydrate_value_textview);
        selected_food_carbohydrate.setText(String.valueOf(food.getCarbonhydrate()));

        TextView selected_food_unit = (TextView) layout.findViewById(R.id.new_meal_food_selected_food_unit_value_textview);
        selected_food_unit.setText(food.getUnit());
    }

    private boolean mustFieldBeComputed() {
        boolean result = false;
        Float foodQuantityAsFloat = null;
        try {
            foodQuantityAsFloat = Float.valueOf(newMealFoodQuantity.getText().toString());
        } catch (final NumberFormatException nfe) {
            return true;
        }


        Float foodCarbohydrateAsFloat = null;
        try {
            foodCarbohydrateAsFloat = Float.valueOf(newMealFoodCarbohydrate.getText().toString());
        } catch (final NumberFormatException nfe) {
            return true;
        }
        return !(format(foodQuantityAsFloat).equals(format(foodCarbohydrateAsFloat * food.getQuantity() / food.getCarbonhydrate())));
    }

    private void updateFieldText(EditText editText, String text) {
        editText.setText(text);
    }

    private long getNewMealFoodId() {
        return getArguments().getLong(NEW_MEAL_FOOD_ID_PARAMETER);
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

    private void saveNewMealFood() {
        GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).updateFoodDiary(newMealFood);
    }

    private void initNewMealFoodFromFields() {
        final String newFoodQuantityText = newMealFoodQuantity.getText().toString();
        try {
            final Float newFoodQuantityAsFloat = Float.valueOf(newFoodQuantityText);
            newMealFood.setQuantity(newFoodQuantityAsFloat);
        } catch (final NumberFormatException nfe) {
            newMealFood.setQuantity(null);
        }

        final String newFoodCarbonHydrateText = newMealFoodCarbohydrate.getText().toString();
        try {
            final Float newFoodCarbonHydrateAsFloat = Float.valueOf(newFoodCarbonHydrateText);
            newMealFood.setCarbohydrate(newFoodCarbonHydrateAsFloat);
        } catch (final NumberFormatException nfe) {
            newMealFood.setCarbohydrate(null);
        }
    }

    private String format(float number) {
        return String.format("%.2f", number).replaceAll(",", ".");
    }

}
