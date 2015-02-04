package ch.glucalc.food;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import ch.glucalc.DialogHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;
import ch.glucalc.food.category.CategoryFood;

import static ch.glucalc.food.FoodConstants.FAKE_DEFAULT_FLOAT_ID;
import static ch.glucalc.food.FoodConstants.FAKE_DEFAULT_LONG_ID;
import static ch.glucalc.food.FoodConstants.FOOD_CARBONHYDRATE_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_CATEGORY_ID_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_ID_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_NAME_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_QUANTITY_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_UNIT_PARAMETER;
import static ch.glucalc.food.category.CategoryFoodConstants.FAKE_DEFAULT_ID;

public class EditFoodFragment extends Fragment implements OnClickListener {

    private static String TAG = "GluCalc";

    private EditText newFoodName;
    private Spinner newFoodCategorySpinner;
    private EditText newFoodCarbonHydrate;
    private EditText newFoodQuantity;
    private EditText newFoodUnit;
    private Button saveButton;
    private List<CategoryFood> categoriesOfFood;

    private OnFoodSaved mCallback;

    // Container Activity must implement this interface
    public interface OnFoodSaved {

        public void openFoodListFragment();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnFoodSaved) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFoodSaved");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("EditFoodFragment.onCreate");
        View layout = inflater.inflate(R.layout.edit_food, container, false);

        if (getFoodCategoryId() == FAKE_DEFAULT_ID) {
            initFieldsAndButtonForCreation(layout);
        } else {
            initFieldsAndButtonForEdition(layout);
        }
        saveButton.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick(View v) {
        log("EditFoodFragment.onClick : START");
        final Food newFood = initFoodFromFields();
        if (newFood.areSomeMandatoryFieldsMissing()) {
            DialogHelper.displayErrorMessageAllFieldsMissing(getActivity());
        } else {
            saveNewFood(newFood);
            log("EditFoodFragment.onClick : DONE");
            mCallback.openFoodListFragment();
        }
    }

    @Override
    public void onDestroy() {
        log("EditFoodFragment.onDestroy");
        super.onDestroy();
    }

    @Override
    public void onResume() {
        log("EditFoodFragment.onResume");
        super.onResume();
    }

    private void initFieldsAndButtonForCreation(View layout) {
        log("EditFoodFragment.initFieldsAndButtonForCreation");
        newFoodName = (EditText) layout.findViewById(R.id.food_edittext);
        newFoodCategorySpinner = (Spinner) layout.findViewById(R.id.food_category_spinner);
        newFoodCarbonHydrate = (EditText) layout.findViewById(R.id.food_carbonhydrate_edittext);
        newFoodQuantity = (EditText) layout.findViewById(R.id.food_quantity_edittext);
        newFoodUnit = (EditText) layout.findViewById(R.id.food_unit_edittext);
        saveButton = (Button) layout.findViewById(R.id.food_save_button);
        populateSpinner(null);
    }

    private void initFieldsAndButtonForEdition(View layout) {
        log("EditFoodFragment.initFieldsAndButtonForEdition");
        newFoodName = (EditText) layout.findViewById(R.id.food_edittext);
        newFoodCategorySpinner = (Spinner) layout.findViewById(R.id.food_category_spinner);
        newFoodCarbonHydrate = (EditText) layout.findViewById(R.id.food_carbonhydrate_edittext);
        newFoodQuantity = (EditText) layout.findViewById(R.id.food_quantity_edittext);
        newFoodUnit = (EditText) layout.findViewById(R.id.food_unit_edittext);
        saveButton = (Button) layout.findViewById(R.id.food_save_button);

        populateSpinner(getFoodCategoryId());

        updateFieldText(newFoodName, getFoodName());
        updateFieldText(newFoodCarbonHydrate, String.valueOf(getFoodCarbonHydrate()));
        updateFieldText(newFoodQuantity, String.valueOf(getFoodQuantity()));
        updateFieldText(newFoodUnit, getFoodUnit());
    }

    private void populateSpinner(Long categoryId) {
        log("EditFoodFragment.populateSpinner");
        Integer selectedIndex = null;
        final ArrayAdapter<CharSequence> categoryAdapter = new ArrayAdapter<CharSequence>(getActivity(),
                android.R.layout.simple_spinner_item);
        final int spinnerDdItem = android.R.layout.simple_spinner_dropdown_item;
        categoriesOfFood = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).loadCategoriesOfFood();
        int currentPosition = 0;
        for (final CategoryFood categoryFood : categoriesOfFood) {
            if (categoryId != null && categoryFood.getId() == categoryId) {
                selectedIndex = currentPosition;
            }
            categoryAdapter.add(categoryFood.getName());
            currentPosition++;
        }
        categoryAdapter.setDropDownViewResource(spinnerDdItem);
        newFoodCategorySpinner.setAdapter(categoryAdapter);
        if (categoryId != null && selectedIndex != null) {
            newFoodCategorySpinner.setSelection(selectedIndex);
        }
    }

    private void updateFieldText(EditText editText, String text) {
        editText.setText(text);
    }

    private long getFoodId() {
        return getArguments().getLong(FOOD_ID_PARAMETER, FAKE_DEFAULT_LONG_ID);
    }

    private String getFoodName() {
        return getArguments().getString(FOOD_NAME_PARAMETER);
    }

    private float getFoodCarbonHydrate() {
        return getArguments().getFloat(FOOD_CARBONHYDRATE_PARAMETER, FAKE_DEFAULT_FLOAT_ID);
    }

    private float getFoodQuantity() {
        return getArguments().getFloat(FOOD_QUANTITY_PARAMETER, FAKE_DEFAULT_FLOAT_ID);
    }

    private String getFoodUnit() {
        return getArguments().getString(FOOD_UNIT_PARAMETER);
    }

    private long getFoodCategoryId() {
        return getArguments().getLong(FOOD_CATEGORY_ID_PARAMETER, FAKE_DEFAULT_LONG_ID);
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

    private void saveNewFood(Food newFood) {
        if (getFoodCategoryId() == FAKE_DEFAULT_LONG_ID) {
            final long id = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).store(newFood);
            propagateResultForCreation(id);
        } else {
            GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).updateFood(newFood);
            propagateResultForEdition();
        }
    }

    private Food initFoodFromFields() {
        final Food newFood = new Food();
        if (getFoodId() != FAKE_DEFAULT_LONG_ID) {
            newFood.setId(getFoodId());
        }
        newFood.setName(newFoodName.getText().toString());

        final String newFoodCarbonHydrateText = newFoodCarbonHydrate.getText().toString();
        try {
            final Float newFoodCarbonHydrateAsFloat = Float.valueOf(newFoodCarbonHydrateText);
            newFood.setCarbonhydrate(newFoodCarbonHydrateAsFloat);
        } catch (final NumberFormatException nfe) {
        }

        final String newFoodQuantityText = newFoodQuantity.getText().toString();
        try {
            final Float newFoodQuantityTextAsFloat = Float.valueOf(newFoodQuantityText);
            newFood.setQuantity(newFoodQuantityTextAsFloat);
        } catch (final NumberFormatException nfe) {
        }
        newFood.setUnit(newFoodUnit.getText().toString());
        final int selectedItemPosition = newFoodCategorySpinner.getSelectedItemPosition();
        long categoryIdSelected = -1;
        int i = 0;
        for (final CategoryFood category : categoriesOfFood) {
            if (i == selectedItemPosition) {
                categoryIdSelected = category.getId();
                break;
            }
            i++;
        }
        newFood.setCategoryId(categoryIdSelected);
        return newFood;
    }

    private void propagateResultForCreation(long id) {
        final Intent intent = new Intent();
        intent.putExtra(FoodConstants.CREATED_ID_RESULT, id);
        //setResult(FoodConstants.RESULT_CODE_CREATED, intent);
    }

    private void propagateResultForEdition() {
        final Intent intent = new Intent();
        intent.putExtra(FoodConstants.MODIFIED_ID_RESULT, getFoodId());
//        setResult(FoodConstants.RESULT_CODE_EDITED, intent);
    }

}
