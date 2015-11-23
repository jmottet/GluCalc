package ch.glucalc.meal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import ch.glucalc.DialogHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.MainActivity;
import ch.glucalc.R;
import ch.glucalc.meal.type.MealType;


@SuppressLint("DefaultLocale")
public class NewMealFragment extends Fragment {

    private static String TAG = "GluCalc";

    private EditText newMealBloodGlucose;
    private TextView favouriteFoodStatus;
    private Switch favouriteFoodSwitch;

    private Spinner mealTypeSpinner;
    private OnMealTypeInsulinSecondStep mCallback;

    private List<MealType> mealTypes = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).loadMealTypes();

    // Container Activity must implement this interface
    public interface OnMealTypeInsulinSecondStep {

        void openNewMealSecondStepFragment(long mealTypeId, float newMealBloodGlucose, boolean favouriteFood);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("NewMealFragment.onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        log("NewMealFragment.onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.next_page_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        log("NewMealFragment.onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.next:

                if (areSomeMandatoryFieldsMissing()) {
                    DialogHelper.displayErrorMessageAllFieldsMissing(getActivity());
                } else {
                    final String newFoodBloodGlucoseText = newMealBloodGlucose.getText().toString();
                    try {
                        final Float newMealBloodGlucoseAsFloat = Float.valueOf(newFoodBloodGlucoseText);

                        final int selectedItemPosition = mealTypeSpinner.getSelectedItemPosition();
                        long mealTypeIdSelected = -1;
                        int i = 1;
                        for (final MealType mealType : mealTypes) {
                            if (i == selectedItemPosition) {
                                mealTypeIdSelected = mealType.getId();
                                break;
                            }
                            i++;
                        }

                        mCallback.openNewMealSecondStepFragment(mealTypeIdSelected, newMealBloodGlucoseAsFloat, favouriteFoodSwitch.isChecked());
                    } catch (final NumberFormatException nfe) {
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.new_meal_view, container, false);

        newMealBloodGlucose = (EditText) layout.findViewById(R.id.new_meal_blood_glucose_edittext);
        TextView newMealBloodGlucoseUnit = (TextView) layout.findViewById(R.id.new_meal_blood_glucose_unit_textview);
        newMealBloodGlucoseUnit.setText(MainActivity.GLOBAL_BLOOD_GLUCOSE.getLabel());

        mealTypeSpinner = (Spinner) layout.findViewById(R.id.new_meal_type_spinner);
        populateSpinner(null);


        favouriteFoodStatus = (TextView) layout.findViewById(R.id.favourite_food_status);
        favouriteFoodSwitch = (Switch) layout.findViewById(R.id.favourite_food_switch);

        //set the switch to ON
        favouriteFoodSwitch.setChecked(true);
        //attach a listener to check for changes in state
        favouriteFoodSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    favouriteFoodStatus.setText("ON");
                } else {
                    favouriteFoodStatus.setText("OFF");
                }
            }
        });

        if (favouriteFoodSwitch.isChecked()) {
            favouriteFoodStatus.setText("ON");
        } else {
            favouriteFoodStatus.setText("OFF");
        }

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        log("NewMealFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMealTypeInsulinSecondStep) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMealTypeInsulinSecondStep");
        }
    }

    @Override
    public void onDestroy() {
        log("NewMealFragment.onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        log("NewMealFragment.onDetach");
        super.onDetach();
    }

    @Override
    public void onPause() {
        log("NewMealFragment.onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        log("NewMealFragment.onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        log("NewMealFragment.onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        log("NewMealFragment.onStop");
        super.onStart();
    }


    private void populateSpinner(Long mealTypeId) {
        log("NewMealFragment.populateSpinner");
        Integer selectedIndex = null;
        final ArrayAdapter<CharSequence> mealTypeAdapter = new ArrayAdapter<CharSequence>(getActivity(),
                android.R.layout.simple_spinner_item);
        final int spinnerDdItem = android.R.layout.simple_spinner_dropdown_item;

        int currentPosition = 0;
        // The first value is "empty"
        mealTypeAdapter.add("");
        currentPosition++;
        for (final MealType mealType : mealTypes) {
            if (mealTypeId != null && mealType.getId() == mealTypeId) {
                selectedIndex = currentPosition;
            }
            mealTypeAdapter.add(mealType.getName());
            currentPosition++;
        }
        mealTypeAdapter.setDropDownViewResource(spinnerDdItem);
        mealTypeSpinner.setAdapter(mealTypeAdapter);
        if (mealTypeId != null && selectedIndex != null) {
            mealTypeSpinner.setSelection(selectedIndex);
        }
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }


    private boolean areSomeMandatoryFieldsMissing() {
        if (TextUtils.isEmpty(newMealBloodGlucose.getText()) || mealTypeSpinner.getSelectedItemId() == 0) {
            return true;
        }
        return false;
    }


}
