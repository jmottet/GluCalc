package ch.glucalc.food;

import static ch.glucalc.food.FoodConstants.FAKE_DEFAULT_FLOAT_ID;
import static ch.glucalc.food.FoodConstants.FAKE_DEFAULT_LONG_ID;
import static ch.glucalc.food.FoodConstants.FOOD_CARBONHYDRATE_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_ID_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_NAME_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_QUANTITY_PARAMETER;
import static ch.glucalc.food.FoodConstants.FOOD_UNIT_PARAMETER;
import static ch.glucalc.food.FoodConstants.MODIFIED_ID_RESULT;
import static ch.glucalc.food.FoodConstants.RESULT_CODE_EDITED;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;

public class EditFoodActivity extends Activity {

  private static String TAG = "GluCalc";

  private EditText newFoodName;
  private EditText newFoodCarbonHydrate;
  private EditText newFoodQuantity;
  private EditText newFoodUnit;
  private Button saveButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.edit_food);

    initFieldsAndButton();

    saveButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        log("EditFoodActivity.onClick : START");
        saveNewFood();
        propagateResult();
        log("EditFoodActivity.onClick : DONE");
        finish();
      }

      private void saveNewFood() {
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
        GluCalcSQLiteHelper.getGluCalcSQLiteHelper(EditFoodActivity.this).updateFood(foodToUpdate);
      }

      private void propagateResult() {
        final Intent intent = new Intent();
        intent.putExtra(MODIFIED_ID_RESULT, getFoodId());
        setResult(RESULT_CODE_EDITED, intent);
      }
    });
  }

  private void initFieldsAndButton() {
    newFoodName = (EditText) findViewById(R.id.food_edittext);
    newFoodCarbonHydrate = (EditText) findViewById(R.id.food_carbonhydrate_edittext);
    newFoodQuantity = (EditText) findViewById(R.id.food_quantity_edittext);
    newFoodUnit = (EditText) findViewById(R.id.food_unit_edittext);
    saveButton = (Button) findViewById(R.id.food_save_button);

    updateFieldText(newFoodName, getFoodName());
    updateFieldText(newFoodCarbonHydrate, String.valueOf(getFoodCarbonHydrate()));
    updateFieldText(newFoodQuantity, String.valueOf(getFoodQuantity()));
    updateFieldText(newFoodUnit, getFoodUnit());
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
}
