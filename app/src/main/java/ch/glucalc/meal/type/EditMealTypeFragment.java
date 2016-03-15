package ch.glucalc.meal.type;

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
import android.widget.ImageButton;
import android.widget.TextView;

import ch.glucalc.DialogHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.KeyboardHelper;
import ch.glucalc.MainActivity;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        log("EditMealTypeFragment.onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.accept_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        log("EditCategoryFoodFragment.onOptionsItemSelected : START");

        log("EditFoodFragment.onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.save:
                log("EditMealTypeFragment.onClick : START");
                final MealType newMealType = initMealTypeFromFields();
                if (newMealType.areSomeMandatoryFieldsMissing()) {
                    DialogHelper.displayErrorMessageAllFieldsMissing(getActivity());
                } else {
                    KeyboardHelper.hideKeyboard(getActivity());
                    saveNewMealType(newMealType);
                    log("EditMealTypeFragment.onClick : DONE");
                    mCallback.openMealTypeListFragment();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View layout = inflater.inflate(R.layout.edit_meal_type, container, false);

        String globalBloodGlucose = MainActivity.GLOBAL_BLOOD_GLUCOSE.getLabel();

        TextView unitTextView = (TextView) layout.findViewById((R.id.meal_type_glycemia_target_unit_textview));
        unitTextView.setText(globalBloodGlucose);

        unitTextView = (TextView) layout.findViewById((R.id.meal_type_insulin_sensitivity_unit_textview));
        unitTextView.setText(globalBloodGlucose);


        if (getMealTypeId() == FAKE_DEFAULT_ID) {
            initFieldsAndButtonForCreation(layout);
        } else {
            initFieldsAndButtonForEdition(layout);
        }

        ImageButton mealTypeNameInfoButton =(ImageButton) layout.findViewById(R.id.meal_type_name_info_button);
        mealTypeNameInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogHelper.getDialogInfo(getActivity(), getString(R.string.edit_meal_type_name), getString(R.string.edit_meal_type_name_info)).show();
            }
        });

        ImageButton mealTypeFoodTargetInfoButton =(ImageButton) layout.findViewById(R.id.meal_type_food_target_info_button);
        mealTypeFoodTargetInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogHelper.getDialogInfo(getActivity(), getString(R.string.edit_meal_type_food_target), getString(R.string.edit_meal_type_food_target_info)).show();
            }
        });

        ImageButton mealTypeGlycemiaTargetInfoButton =(ImageButton) layout.findViewById(R.id.meal_type_glycemia_target_info_button);
        mealTypeGlycemiaTargetInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogHelper.getDialogInfo(getActivity(), getString(R.string.edit_meal_type_blood_glucose_target), getString(R.string.edit_meal_type_blood_glucose_target_info)).show();
            }
        });

        ImageButton mealTypeInsulinSensitivityInfoButton =(ImageButton) layout.findViewById(R.id.meal_type_insulin_sensitivity_info_button);
        mealTypeInsulinSensitivityInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogHelper.getDialogInfo(getActivity(), getString(R.string.edit_meal_type_insulin_sensitivity), getString(R.string.edit_meal_type_insulin_sensitivity_info)).show();
            }
        });

        ImageButton mealTypeInsulinInfoButton =(ImageButton) layout.findViewById(R.id.meal_type_insulin_info_button);
        mealTypeInsulinInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogHelper.getDialogInfo(getActivity(), getString(R.string.edit_meal_type_insulin), getString(R.string.edit_meal_type_insulin_info)).show();
            }
        });

        return layout;
    }

    private void saveNewMealType(MealType newMealType) {
        if (getMealTypeId() == FAKE_DEFAULT_ID) {
            final long id = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).storeMealType(
                    newMealType);
        } else {
            GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).updateMealType(newMealType);
        }
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
    }

    private void initFieldsAndButtonForEdition(View layout) {
        log("EditMealTypeFragment.initFieldsAndButtonForEdition");
        newMealTypeName = (EditText) layout.findViewById(R.id.meal_type_name_edittext);
        newMealTypeFoodTarget = (EditText) layout.findViewById(R.id.meal_type_food_target_edittext);
        newMealTypeGlycemiaTarget = (EditText) layout.findViewById(R.id.meal_type_glycemia_target_edittext);
        newMealTypeInsulinSensitivity = (EditText) layout.findViewById(R.id.meal_type_insulin_sensitivity_edittext);
        newMealTypeInsulin = (EditText) layout.findViewById(R.id.meal_type_insulin_edittext);

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
