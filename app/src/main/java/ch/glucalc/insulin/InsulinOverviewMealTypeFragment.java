package ch.glucalc.insulin;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;
import ch.glucalc.meal.type.MealType;

import ch.glucalc.meal.type.MealTypeConstants;

public class InsulinOverviewMealTypeFragment extends Fragment {

    private static String TAG = "GluCalc";

    private MealType mealType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mealType = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).loadMealType(getMealTypeId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("InsulinOverviewMealTypeFragment.onCreate");
        View layout = inflater.inflate(R.layout.insulin_overview, container, false);
        initFields(layout);
        return layout;
    }

    @Override
    public void onDestroy() {
        log("InsulinOverviewMealTypeFragment.onDestroy");
        super.onDestroy();
    }

    private void initFields(View layout) {
        log("InsulinOverviewMealTypeFragment.initFieldsAndButtonForEdition");

        TextView mealTypeTextView = (TextView) layout.findViewById(R.id.insulin_overview_meal_type_textview);
        mealTypeTextView.setText(mealType.getName());

        TextView targetFoodValue = (TextView) layout.findViewById(R.id.insulin_overview_target_food_value_textview);
        targetFoodValue.setText(String.valueOf(mealType.getFoodTarget()));

        TextView sensitivityValue = (TextView) layout.findViewById(R.id.insulin_overview_insulin_sensitivity_value_textview);
        sensitivityValue.setText(String.valueOf(mealType.getInsulinSensitivity()));

        TextView glycemiaTargetValue = (TextView) layout.findViewById(R.id.insulin_overview_blood_glucose_target_value_textview);
        glycemiaTargetValue.setText(String.valueOf(mealType.getGlycemiaTarget()));

        TextView insulinValue = (TextView) layout.findViewById(R.id.insulin_overview_insulin_value_textview);
        insulinValue.setText(String.valueOf(mealType.getInsulin()));

        float glycemiaMeasured = 0;
        TextView glycemiaFirstValue = (TextView) layout.findViewById(R.id.insulin_overview_recap_blood_glucose_first_textview);
        glycemiaFirstValue.setText(format(glycemiaMeasured));

        TextView insulinFirstValue = (TextView) layout.findViewById(R.id.insulin_overview_recap_insulin_first_textview);
        float bolus = getBolus(glycemiaMeasured, mealType.getGlycemiaTarget(), mealType.getInsulin(), mealType.getFoodTarget(), mealType.getInsulinSensitivity());
        insulinFirstValue.setText(format(bolus));

        glycemiaMeasured += 3;
        TextView glycemiaSecondValue = (TextView) layout.findViewById(R.id.insulin_overview_recap_blood_glucose_second_textview);
        glycemiaSecondValue.setText(format(glycemiaMeasured));

        TextView insulinSecondValue = (TextView) layout.findViewById(R.id.insulin_overview_recap_insulin_second_textview);
        bolus = getBolus(glycemiaMeasured, mealType.getGlycemiaTarget(), mealType.getInsulin(), mealType.getFoodTarget(), mealType.getInsulinSensitivity());
        insulinSecondValue.setText(format(bolus));

        glycemiaMeasured += 3;
        TextView glycemiaThirdValue = (TextView) layout.findViewById(R.id.insulin_overview_recap_blood_glucose_third_textview);
        bolus = getBolus(glycemiaMeasured, mealType.getGlycemiaTarget(), mealType.getInsulin(), mealType.getFoodTarget(), mealType.getInsulinSensitivity());
        glycemiaThirdValue.setText(format(glycemiaMeasured));

        TextView insulinThirdValue = (TextView) layout.findViewById(R.id.insulin_overview_recap_insulin_third_textview);
        bolus = getBolus(glycemiaMeasured, mealType.getGlycemiaTarget(), mealType.getInsulin(), mealType.getFoodTarget(), mealType.getInsulinSensitivity());
        insulinThirdValue.setText(format(bolus));

        glycemiaMeasured += 3;
        TextView glycemiaFourthValue = (TextView) layout.findViewById(R.id.insulin_overview_recap_blood_glucose_fourth_textview);
        glycemiaFourthValue.setText(format(glycemiaMeasured));

        TextView insulinFourthValue = (TextView) layout.findViewById(R.id.insulin_overview_recap_insulin_fourth_textview);
        bolus = getBolus(glycemiaMeasured, mealType.getGlycemiaTarget(), mealType.getInsulin(), mealType.getFoodTarget(), mealType.getInsulinSensitivity());
        insulinFourthValue.setText(format(bolus));

        glycemiaMeasured += 3;
        TextView glycemiaFifthValue = (TextView) layout.findViewById(R.id.insulin_overview_recap_blood_glucose_fifth_textview);
        glycemiaFifthValue.setText(format(glycemiaMeasured));

        TextView insulinFifthValue = (TextView) layout.findViewById(R.id.insulin_overview_recap_insulin_fifth_textview);
        bolus = getBolus(glycemiaMeasured, mealType.getGlycemiaTarget(), mealType.getInsulin(), mealType.getFoodTarget(), mealType.getInsulinSensitivity());
        insulinFifthValue.setText(format(bolus));

        glycemiaMeasured += 3;
        TextView glycemiaSixthValue = (TextView) layout.findViewById(R.id.insulin_overview_recap_blood_glucose_sixth_textview);
        glycemiaSixthValue.setText(format(glycemiaMeasured));

        TextView insulinSixthValue = (TextView) layout.findViewById(R.id.insulin_overview_recap_insulin_sixth_textview);
        bolus = getBolus(glycemiaMeasured, mealType.getGlycemiaTarget(), mealType.getInsulin(), mealType.getFoodTarget(), mealType.getInsulinSensitivity());
        insulinSixthValue.setText(format(bolus));
    }

    private float getBolus(float glycemiaMeasured, float glycemiaTarget, float insulinPerTen, float carbohydrate, float sensitivityFactor) {
        return (insulinPerTen * carbohydrate / 10) + (glycemiaMeasured - glycemiaTarget) / sensitivityFactor;

    }
    private String format(float number) {
        return String.format("%.2f", number).replaceAll(",",".");
    }

    private LinearLayout getNewHorizontalLayout() {
        LinearLayout result = new LinearLayout(getActivity());
        result.setOrientation(LinearLayout.HORIZONTAL);
        result.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return result;
    }

    private TextView getTextView(int paddingRight, ViewGroup.LayoutParams layoutParams, String text) {
        TextView result = new TextView(getActivity());
        result.setPadding(0, 0, 120, 0);
        result.setText(text);
        result.setGravity(Gravity.LEFT);
        return result;
    }

    private long getMealTypeId() {
        return getArguments().getLong(MealTypeConstants.MEAL_TYPE_ID_PARAMETER);
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

}
