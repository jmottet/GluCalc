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

        TextView glycemiaTargetValue = (TextView) layout.findViewById(R.id.insulin_overview_glycemia_target_value_textview);
        glycemiaTargetValue.setText(String.valueOf(mealType.getGlycemiaTarget()));

        TextView insulinValue = (TextView) layout.findViewById(R.id.insulin_overview_insulin_value_textview);
        insulinValue.setText(String.valueOf(mealType.getInsulin()));

        LinearLayout verticalLayout = (LinearLayout) layout.findViewById(R.id.insulin_overview_vertical_layout);
        LinearLayout horizontalLayout = getNewHorizontalLayout();
        verticalLayout.addView(horizontalLayout);

        TextView leftTextViewRef = (TextView) layout.findViewById(R.id.insulin_overview_recap_glycemia_unit_title_textview);
        int paddingRight = leftTextViewRef.getPaddingRight();
        ViewGroup.LayoutParams leftLayoutParams = leftTextViewRef.getLayoutParams();
        TextView textViewLeft = getTextView(paddingRight, leftLayoutParams, "0.00");
        horizontalLayout.addView(textViewLeft);


    }

    private LinearLayout getNewHorizontalLayout() {
        LinearLayout result = new LinearLayout(getActivity());
        result.setOrientation(LinearLayout.HORIZONTAL);
        result.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return result;
    }

    private TextView getTextView(int paddingRight, ViewGroup.LayoutParams layoutParams, String text) {
        TextView result = new TextView(getActivity());
        result.setPadding(0, 0, paddingRight, 0);
        result.setText(text);
        result.setGravity(Gravity.RIGHT);
        return result;
    }

    private long getMealTypeId() {
        return getArguments().getLong(MealTypeConstants.MEAL_TYPE_ID_PARAMETER);
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

}
