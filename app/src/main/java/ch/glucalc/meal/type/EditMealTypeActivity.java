package ch.glucalc.meal.type;

import static ch.glucalc.meal.type.MealTypeConstants.CREATED_ID_RESULT;
import static ch.glucalc.meal.type.MealTypeConstants.FAKE_DEFAULT_FLOAT_ID;
import static ch.glucalc.meal.type.MealTypeConstants.FAKE_DEFAULT_ID;
import static ch.glucalc.meal.type.MealTypeConstants.MEAL_TYPE_FOOD_TARGET_PARAMETER;
import static ch.glucalc.meal.type.MealTypeConstants.MEAL_TYPE_GLYCEMIA_TARGET_PARAMETER;
import static ch.glucalc.meal.type.MealTypeConstants.MEAL_TYPE_ID_PARAMETER;
import static ch.glucalc.meal.type.MealTypeConstants.MEAL_TYPE_INSULIN_PARAMETER;
import static ch.glucalc.meal.type.MealTypeConstants.MEAL_TYPE_INSULIN_SENSITIVITY_PARAMETER;
import static ch.glucalc.meal.type.MealTypeConstants.MEAL_TYPE_NAME_PARAMETER;
import static ch.glucalc.meal.type.MealTypeConstants.MODIFIED_ID_RESULT;
import static ch.glucalc.meal.type.MealTypeConstants.RESULT_CODE_CREATED;
import static ch.glucalc.meal.type.MealTypeConstants.RESULT_CODE_EDITED;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import ch.glucalc.DialogHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;

public class EditMealTypeActivity extends Activity {

  private static String TAG = "GluCalc";

  private EditText newMealTypeName;
  private EditText newMealTypeFoodTarget;
  private EditText newMealTypeGlycemiaTarget;
  private EditText newMealTypeInsulinSensitivity;
  private EditText newMealTypeInsulin;
  private Button saveButton;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.edit_meal_type);

    if (getMealTypeId() == FAKE_DEFAULT_ID) {
      initFieldsAndButtonForCreation();
    } else {
      initFieldsAndButtonForEdition();
    }

    saveButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        log("EditMealTypeActivity.onClick : START");
        final MealType newMealType = initMealTypeFromFields();
        if (newMealType.areSomeMandatoryFieldsMissing()) {
          DialogHelper.displayErrorMessageAllFieldsMissing(EditMealTypeActivity.this);
        } else {
          saveNewMealType(newMealType);
          log("EditMealTypeActivity.onClick : DONE");
          finish();
        }
      }

      private void saveNewMealType(MealType newMealType) {
        if (getMealTypeId() == FAKE_DEFAULT_ID) {
          final long id = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(EditMealTypeActivity.this).storeMealType(
              newMealType);
          propagateResultForCreation(id);
        } else {
          GluCalcSQLiteHelper.getGluCalcSQLiteHelper(EditMealTypeActivity.this).updateMealType(newMealType);
          propagateResultForEdition();
        }
      }

      private void propagateResultForCreation(long newMealTypeId) {
        final Intent intent = new Intent();
        intent.putExtra(CREATED_ID_RESULT, newMealTypeId);
        setResult(RESULT_CODE_CREATED, intent);
      }

      private void propagateResultForEdition() {
        final Intent intent = new Intent();
        intent.putExtra(MODIFIED_ID_RESULT, getMealTypeId());
        setResult(RESULT_CODE_EDITED, intent);
      }
    });
  }

  private MealType initMealTypeFromFields() {
    final MealType newMealType = new MealType();
    if (getMealTypeId() != FAKE_DEFAULT_ID) {
      newMealType.setId(getMealTypeId());
    }
    newMealType.setName(newMealTypeName.getText().toString());

    final String newMealTypeFoodTargetText = newMealTypeFoodTarget.getText().toString();
    try {
      final Float newMealTypeFoodTargetAsFloat = Float.valueOf(newMealTypeFoodTargetText);
      newMealType.setFoodTarget(newMealTypeFoodTargetAsFloat);
    } catch (final NumberFormatException nfe) {
    }

    final String newMealTypeGlycemiaTargetText = newMealTypeGlycemiaTarget.getText().toString();
    try {
      final Float newMealTypeGlycemiaTargetAsFloat = Float.valueOf(newMealTypeGlycemiaTargetText);
      newMealType.setGlycemiaTarget(newMealTypeGlycemiaTargetAsFloat);
    } catch (final NumberFormatException nfe) {
    }

    final String newMealTypeInsulinSensitivityText = newMealTypeInsulinSensitivity.getText().toString();
    try {
      final Float newMealTypeInsulinSensitivityAsFloat = Float.valueOf(newMealTypeInsulinSensitivityText);
      newMealType.setInsulinSensitivity(newMealTypeInsulinSensitivityAsFloat);
    } catch (final NumberFormatException nfe) {
    }

    final String newMealTypeInsulinText = newMealTypeInsulin.getText().toString();
    try {
      final Float newMealTypeInsulinAsFloat = Float.valueOf(newMealTypeInsulinText);
      newMealType.setInsulin(newMealTypeInsulinAsFloat);
    } catch (final NumberFormatException nfe) {
    }
    return newMealType;
  }

  private void initFieldsAndButtonForCreation() {
    log("EditMealTypeActivity.initFieldsAndButtonForCreation");
    newMealTypeName = (EditText) findViewById(R.id.meal_type_name_edittext);
    newMealTypeFoodTarget = (EditText) findViewById(R.id.meal_type_food_target_edittext);
    newMealTypeGlycemiaTarget = (EditText) findViewById(R.id.meal_type_glycemia_target_edittext);
    newMealTypeInsulinSensitivity = (EditText) findViewById(R.id.meal_type_insulin_sensitivity_edittext);
    newMealTypeInsulin = (EditText) findViewById(R.id.meal_type_insulin_edittext);
    saveButton = (Button) findViewById(R.id.meal_type_save_button);
  }

  private void initFieldsAndButtonForEdition() {
    log("EditMealTypeActivity.initFieldsAndButtonForEdition");
    newMealTypeName = (EditText) findViewById(R.id.meal_type_name_edittext);
    newMealTypeFoodTarget = (EditText) findViewById(R.id.meal_type_food_target_edittext);
    newMealTypeGlycemiaTarget = (EditText) findViewById(R.id.meal_type_glycemia_target_edittext);
    newMealTypeInsulinSensitivity = (EditText) findViewById(R.id.meal_type_insulin_sensitivity_edittext);
    newMealTypeInsulin = (EditText) findViewById(R.id.meal_type_insulin_edittext);
    saveButton = (Button) findViewById(R.id.meal_type_save_button);

    updateFieldText(newMealTypeName, getMealTypeName());
    updateFieldText(newMealTypeFoodTarget, String.valueOf(getMealTypeFoodTarget()));
    updateFieldText(newMealTypeGlycemiaTarget, String.valueOf(getMealTypeGlycemiaTarget()));
    updateFieldText(newMealTypeInsulinSensitivity, String.valueOf(getMealTypeInsulinSensitivity()));
    updateFieldText(newMealTypeInsulin, String.valueOf(getMealTypeInsulin()));

  }

  private long getMealTypeId() {
    return getIntent().getLongExtra(MEAL_TYPE_ID_PARAMETER, FAKE_DEFAULT_ID);
  }

  private String getMealTypeName() {
    return getIntent().getStringExtra(MEAL_TYPE_NAME_PARAMETER);
  }

  private float getMealTypeFoodTarget() {
    return getIntent().getFloatExtra(MEAL_TYPE_FOOD_TARGET_PARAMETER, FAKE_DEFAULT_FLOAT_ID);
  }

  private float getMealTypeGlycemiaTarget() {
    return getIntent().getFloatExtra(MEAL_TYPE_GLYCEMIA_TARGET_PARAMETER, FAKE_DEFAULT_FLOAT_ID);
  }

  private float getMealTypeInsulinSensitivity() {
    return getIntent().getFloatExtra(MEAL_TYPE_INSULIN_SENSITIVITY_PARAMETER, FAKE_DEFAULT_FLOAT_ID);
  }

  private float getMealTypeInsulin() {
    return getIntent().getFloatExtra(MEAL_TYPE_INSULIN_PARAMETER, FAKE_DEFAULT_FLOAT_ID);
  }

  private void updateFieldText(EditText editText, String text) {
    editText.setText(text);
  }

  private void log(String msg) {
    Log.i(TAG, msg);
  }
}
