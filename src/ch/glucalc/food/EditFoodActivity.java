package ch.glucalc.food;

import static ch.glucalc.food.FoodConstants.FAKE_DEFAULT_FLOAT_ID;
import static ch.glucalc.food.FoodConstants.FAKE_DEFAULT_LONG_ID;
import static ch.glucalc.food.FoodConstants.FOOD_CARBONHYDRATE_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_CATEGORY_ID_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_ID_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_NAME_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_QUANTITY_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_UNIT_PARAMETER;
import static ch.glucalc.food.category.CategoryFoodConstants.FAKE_DEFAULT_ID;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import ch.glucalc.DialogHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;
import ch.glucalc.food.category.CategoryFood;

public class EditFoodActivity extends Activity implements OnClickListener {

  private static String TAG = "GluCalc";

  private EditText newFoodName;
  private Spinner newFoodCategorySpinner;
  private EditText newFoodCarbonHydrate;
  private EditText newFoodQuantity;
  private EditText newFoodUnit;
  private Button saveButton;
  private List<CategoryFood> categoriesOfFood;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    log("EditFoodActivity.onCreate");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.edit_food);

    if (getFoodCategoryId() == FAKE_DEFAULT_ID) {
      initFieldsAndButtonForCreation();
    } else {
      initFieldsAndButtonForEdition();
    }
    saveButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    log("EditFoodActivity.onClick : START");
    final Food newFood = initFoodFromFields();
    if (newFood.areSomeMandatoryFieldsMissing()) {
      DialogHelper.displayErrorMessageAllFieldsMissing(EditFoodActivity.this);
    } else {
      saveNewFood(newFood);
      log("EditFoodActivity.onClick : DONE");
      finish();
    }
  }

  @Override
  public void onDestroy() {
    log("EditFoodActivity.onDestroy");
    super.onDestroy();
  }

  @Override
  public void onResume() {
    log("EditFoodActivity.onResume");
    super.onResume();
  }

  private void initFieldsAndButtonForCreation() {
    log("EditFoodActivity.initFieldsAndButtonForCreation");
    newFoodName = (EditText) findViewById(R.id.food_edittext);
    newFoodCategorySpinner = (Spinner) findViewById(R.id.food_category_spinner);
    newFoodCarbonHydrate = (EditText) findViewById(R.id.food_carbonhydrate_edittext);
    newFoodQuantity = (EditText) findViewById(R.id.food_quantity_edittext);
    newFoodUnit = (EditText) findViewById(R.id.food_unit_edittext);
    saveButton = (Button) findViewById(R.id.food_save_button);
    populateSpinner(null);
  }

  private void initFieldsAndButtonForEdition() {
    log("EditFoodActivity.initFieldsAndButtonForEdition");
    newFoodName = (EditText) findViewById(R.id.food_edittext);
    newFoodCategorySpinner = (Spinner) findViewById(R.id.food_category_spinner);
    newFoodCarbonHydrate = (EditText) findViewById(R.id.food_carbonhydrate_edittext);
    newFoodQuantity = (EditText) findViewById(R.id.food_quantity_edittext);
    newFoodUnit = (EditText) findViewById(R.id.food_unit_edittext);
    saveButton = (Button) findViewById(R.id.food_save_button);

    populateSpinner(getFoodCategoryId());

    updateFieldText(newFoodName, getFoodName());
    updateFieldText(newFoodCarbonHydrate, String.valueOf(getFoodCarbonHydrate()));
    updateFieldText(newFoodQuantity, String.valueOf(getFoodQuantity()));
    updateFieldText(newFoodUnit, getFoodUnit());
  }

  private void populateSpinner(Long categoryId) {
    log("EditFoodActivity.populateSpinner");
    Integer selectedIndex = null;
    final ArrayAdapter<CharSequence> categoryAdapter = new ArrayAdapter<CharSequence>(this,
        android.R.layout.simple_spinner_item);
    final int spinnerDdItem = android.R.layout.simple_spinner_dropdown_item;
    categoriesOfFood = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(EditFoodActivity.this).loadCategoriesOfFood();
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
    return getIntent().getLongExtra(FOOD_ID_PARAMETER, FAKE_DEFAULT_LONG_ID);
  }

  private String getFoodName() {
    return getIntent().getStringExtra(FOOD_NAME_PARAMETER);
  }

  private float getFoodCarbonHydrate() {
    return getIntent().getFloatExtra(FOOD_CARBONHYDRATE_PARAMETER, FAKE_DEFAULT_FLOAT_ID);
  }

  private float getFoodQuantity() {
    return getIntent().getFloatExtra(FOOD_QUANTITY_PARAMETER, FAKE_DEFAULT_FLOAT_ID);
  }

  private String getFoodUnit() {
    return getIntent().getStringExtra(FOOD_UNIT_PARAMETER);
  }

  private long getFoodCategoryId() {
    return getIntent().getLongExtra(FOOD_CATEGORY_ID_PARAMETER, FAKE_DEFAULT_LONG_ID);
  }

  private void log(String msg) {
    Log.i(TAG, msg);
  }

  private void saveNewFood(Food newFood) {
    if (getFoodCategoryId() == FAKE_DEFAULT_LONG_ID) {
      final long id = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(EditFoodActivity.this).store(newFood);
      propagateResultForCreation(id);
    } else {
      GluCalcSQLiteHelper.getGluCalcSQLiteHelper(EditFoodActivity.this).updateFood(newFood);
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
    setResult(FoodConstants.RESULT_CODE_CREATED, intent);
  }

  private void propagateResultForEdition() {
    final Intent intent = new Intent();
    intent.putExtra(FoodConstants.MODIFIED_ID_RESULT, getFoodId());
    setResult(FoodConstants.RESULT_CODE_EDITED, intent);
  }

}
