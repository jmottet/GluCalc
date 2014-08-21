package ch.glucalc.food;

import static ch.glucalc.food.FoodConstants.FAKE_DEFAULT_FLOAT_ID;
import static ch.glucalc.food.FoodConstants.FAKE_DEFAULT_LONG_ID;
import static ch.glucalc.food.FoodConstants.FOOD_CARBONHYDRATE_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_ID_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_NAME_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_QUANTITY_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_UNIT_PARAMETER;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;
import ch.glucalc.food.category.CategoryFood;
import ch.glucalc.food.category.CategoryFoodConstants;

public class EditFoodActivity extends Activity implements OnClickListener {

  private static String TAG = "GluCalc";

  private EditText newFoodName;
  private Spinner newFoodCategory;
  private EditText newFoodCarbonHydrate;
  private EditText newFoodQuantity;
  private EditText newFoodUnit;
  private Button saveButton;

  private AlertDialog alertDialog;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.edit_food);

    initFieldsAndButton();
    saveButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    log("EditFoodActivity.onClick : START");
    final Food foodToUpdate = initFoodFromFields();
    if (foodToUpdate.areSomeMandatoryFieldsMissing()) {
      displayErrorMessage();
    } else {
      saveNewFood(foodToUpdate);
      propagateResult();
      log("EditFoodActivity.onClick : DONE");
      finish();
    }
  }

  @Override
  public void onStop() {
    super.onStop();

    if (alertDialog != null) {
      alertDialog.dismiss();
    }
  }

  @Override
  public void onDestroy() {
    log("EditFoodActivity.onDestroy");
    super.onDestroy();
  }

  @Override
  public void onPause() {
    log("EditFoodActivity.onPause");
    super.onPause();

    if (alertDialog != null) {
      alertDialog.dismiss();
    }
  }

  @Override
  public void onResume() {
    log("EditFoodActivity.onResume");
    super.onResume();
  }

  private void initFieldsAndButton() {
    newFoodName = (EditText) findViewById(R.id.food_edittext);
    newFoodCategory = (Spinner) findViewById(R.id.food_category_spinner);
    newFoodCarbonHydrate = (EditText) findViewById(R.id.food_carbonhydrate_edittext);
    newFoodQuantity = (EditText) findViewById(R.id.food_quantity_edittext);
    newFoodUnit = (EditText) findViewById(R.id.food_unit_edittext);
    saveButton = (Button) findViewById(R.id.food_save_button);

    populateSpinner();
    updateFieldText(newFoodName, getFoodName());
    updateFieldText(newFoodCarbonHydrate, String.valueOf(getFoodCarbonHydrate()));
    updateFieldText(newFoodQuantity, String.valueOf(getFoodQuantity()));
    updateFieldText(newFoodUnit, getFoodUnit());
  }

  private void populateSpinner() {
    final ArrayAdapter<CharSequence> categoryAdapter = new ArrayAdapter<CharSequence>(this,
        android.R.layout.simple_spinner_item);
    final int spinnerDdItem = android.R.layout.simple_spinner_dropdown_item;
    final List<CategoryFood> categoriesOfFood = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(EditFoodActivity.this)
        .loadCategoriesOfFood();
    for (final CategoryFood categoryFood : categoriesOfFood) {
      categoryAdapter.add(categoryFood.getName());
    }
    categoryAdapter.setDropDownViewResource(spinnerDdItem);
    newFoodCategory.setAdapter(categoryAdapter);
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

  private void log(String msg) {
    Log.i(TAG, msg);
  }

  private void saveNewFood(Food foodToUpdate) {
    GluCalcSQLiteHelper.getGluCalcSQLiteHelper(EditFoodActivity.this).updateFood(foodToUpdate);
  }

  private Food initFoodFromFields() {
    final Food foodToUpdate = new Food();
    foodToUpdate.setId(getFoodId());
    foodToUpdate.setName(newFoodName.getText().toString());

    final String newFoodCarbonHydrateText = newFoodCarbonHydrate.getText().toString();
    try {
      final Float newFoodCarbonHydrateAsFloat = Float.valueOf(newFoodCarbonHydrateText);
      foodToUpdate.setCarbonhydrate(newFoodCarbonHydrateAsFloat);
    } catch (final NumberFormatException nfe) {
    }

    final String newFoodQuantityText = newFoodQuantity.getText().toString();
    try {
      final Float newFoodQuantityTextAsFloat = Float.valueOf(newFoodQuantityText);
      foodToUpdate.setQuantity(newFoodQuantityTextAsFloat);
    } catch (final NumberFormatException nfe) {
    }
    foodToUpdate.setUnit(newFoodUnit.getText().toString());
    return foodToUpdate;
  }

  private void propagateResult() {
    final Intent intent = new Intent();
    intent.putExtra(CategoryFoodConstants.MODIFIED_ID_RESULT, getFoodId());
    setResult(CategoryFoodConstants.RESULT_CODE_EDITED, intent);
  }

  private void displayErrorMessage() {
    final Builder builder = new AlertDialog.Builder(EditFoodActivity.this);

    // Setting Dialog Title
    builder.setTitle("Error");

    // Setting Dialog Message
    builder.setMessage("Please complete all fields");

    // Setting Icon to Dialog if needed
    // alertDialog.setIcon(R.drawable.)

    // Setting OK Button
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        // Toast.makeText(getApplicationContext(), "You clicked on OK",
        // Toast.LENGTH_SHORT).show();
        log("EditFoodActivity - Alert clicked");
      }
    });

    alertDialog = builder.create();
    alertDialog.show();
  }

}
