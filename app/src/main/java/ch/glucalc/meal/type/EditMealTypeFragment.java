package ch.glucalc.meal.type;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ch.glucalc.DialogHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;

import static ch.glucalc.meal.type.MealTypeConstants.FAKE_DEFAULT_FLOAT_ID;
import static ch.glucalc.meal.type.MealTypeConstants.FAKE_DEFAULT_ID;
import static ch.glucalc.meal.type.MealTypeConstants.MEAL_TYPE_FOOD_TARGET_PARAMETER;
import static ch.glucalc.meal.type.MealTypeConstants.MEAL_TYPE_GLYCEMIA_TARGET_PARAMETER;
import static ch.glucalc.meal.type.MealTypeConstants.MEAL_TYPE_ID_PARAMETER;
import static ch.glucalc.meal.type.MealTypeConstants.MEAL_TYPE_INSULIN_PARAMETER;
import static ch.glucalc.meal.type.MealTypeConstants.MEAL_TYPE_INSULIN_SENSITIVITY_PARAMETER;
import static ch.glucalc.meal.type.MealTypeConstants.MEAL_TYPE_NAME_PARAMETER;

public class EditMealTypeFragment extends Fragment {

    private static String TAG = "GluCalc";

    private EditText newMealTypeName;
    private EditText newMealTypeFoodTarget;
    private EditText newMealTypeGlycemiaTarget;
    private EditText newMealTypeInsulinSensitivity;
    private EditText newMealTypeInsulin;
    private Button saveButton;

    private OnMealTypeSaved mCallback;

    // Container Activity must implement this interface
    public interface OnMealTypeSaved {

        public void openMealTypeListFragment();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMealTypeSaved) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMealTypeSaved");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        View layout = inflater.inflate(R.layout.edit_meal_type, container, false);

        if (getMealTypeId() == FAKE_DEFAULT_ID) {
            initFieldsAndButtonForCreation(layout);
        } else {
            initFieldsAndButtonForEdition(layout);
        }

        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                log("EditMealTypeFragment.onClick : START");
                final MealType newMealType = initMealTypeFromFields();
                if (newMealType.areSomeMandatoryFieldsMissing()) {
                    DialogHelper.displayErrorMessageAllFieldsMissing(getActivity());
                } else {
                    saveNewMealType(newMealType);
                    log("EditMealTypeFragment.onClick : DONE");
                    mCallback.openMealTypeListFragment();
                }
            }

            private void saveNewMealType(MealType newMealType) {
                if (getMealTypeId() == FAKE_DEFAULT_ID) {
                    final long id = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).storeMealType(
                            newMealType);
                } else {
                    GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).updateMealType(newMealType);
                }
            }
        });
        return layout;
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

    private void initFieldsAndButtonForCreation(View layout) {
        log("EditMealTypeFragment.initFieldsAndButtonForCreation");
        newMealTypeName = (EditText) layout.findViewById(R.id.meal_type_name_edittext);
        newMealTypeFoodTarget = (EditText) layout.findViewById(R.id.meal_type_food_target_edittext);
        newMealTypeGlycemiaTarget = (EditText) layout.findViewById(R.id.meal_type_glycemia_target_edittext);
        newMealTypeInsulinSensitivity = (EditText) layout.findViewById(R.id.meal_type_insulin_sensitivity_edittext);
        newMealTypeInsulin = (EditText) layout.findViewById(R.id.meal_type_insulin_edittext);
        saveButton = (Button) layout.findViewById(R.id.meal_type_save_button);
    }

    private void initFieldsAndButtonForEdition(View layout) {
        log("EditMealTypeFragment.initFieldsAndButtonForEdition");
        newMealTypeName = (EditText) layout.findViewById(R.id.meal_type_name_edittext);
        newMealTypeFoodTarget = (EditText) layout.findViewById(R.id.meal_type_food_target_edittext);
        newMealTypeGlycemiaTarget = (EditText) layout.findViewById(R.id.meal_type_glycemia_target_edittext);
        newMealTypeInsulinSensitivity = (EditText) layout.findViewById(R.id.meal_type_insulin_sensitivity_edittext);
        newMealTypeInsulin = (EditText) layout.findViewById(R.id.meal_type_insulin_edittext);
        saveButton = (Button) layout.findViewById(R.id.meal_type_save_button);

        updateFieldText(newMealTypeName, getMealTypeName());
        updateFieldText(newMealTypeFoodTarget, String.valueOf(getMealTypeFoodTarget()));
        updateFieldText(newMealTypeGlycemiaTarget, String.valueOf(getMealTypeGlycemiaTarget()));
        updateFieldText(newMealTypeInsulinSensitivity, String.valueOf(getMealTypeInsulinSensitivity()));
        updateFieldText(newMealTypeInsulin, String.valueOf(getMealTypeInsulin()));

    }

    private long getMealTypeId() {
        return getArguments().getLong(MEAL_TYPE_ID_PARAMETER, FAKE_DEFAULT_ID);
    }

    private String getMealTypeName() {
        return getArguments().getString(MEAL_TYPE_NAME_PARAMETER);
    }

    private float getMealTypeFoodTarget() {
        return getArguments().getFloat(MEAL_TYPE_FOOD_TARGET_PARAMETER, FAKE_DEFAULT_FLOAT_ID);
    }

    private float getMealTypeGlycemiaTarget() {
        return getArguments().getFloat(MEAL_TYPE_GLYCEMIA_TARGET_PARAMETER, FAKE_DEFAULT_FLOAT_ID);
    }

    private float getMealTypeInsulinSensitivity() {
        return getArguments().getFloat(MEAL_TYPE_INSULIN_SENSITIVITY_PARAMETER, FAKE_DEFAULT_FLOAT_ID);
    }

    private float getMealTypeInsulin() {
        return getArguments().getFloat(MEAL_TYPE_INSULIN_PARAMETER, FAKE_DEFAULT_FLOAT_ID);
    }

    private void updateFieldText(EditText editText, String text) {
        editText.setText(text);
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }
}
