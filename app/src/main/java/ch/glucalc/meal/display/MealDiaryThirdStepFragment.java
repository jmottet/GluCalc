package ch.glucalc.meal.display;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ch.glucalc.EnumColor;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.MainActivity;
import ch.glucalc.R;
import ch.glucalc.meal.NewMealConstants;
import ch.glucalc.meal.NewMealFoodListFragment;
import ch.glucalc.meal.diary.FoodDiary;
import ch.glucalc.meal.diary.MealDiary;
import ch.glucalc.meal.type.MealType;

public class MealDiaryThirdStepFragment extends Fragment {

    private static String TAG = "GluCalc";

    private MealType mealType = null;
    private MealDiary mealDiary = null;
    private String dateFormatted = null;

    private LinearLayout glycemiaContainer;
    private LinearLayout carbohydrateContainer;
    private TextView carbohydrateTextView;
    private TextView carbohydrateUnitTextView;
    private TextView insulinTextView;
    private NewMealFoodListFragment newMealFoodListFragment;
    private TextView bolusGivenTextView;

    private static int ORANGE_COLOR = Color.parseColor("#FF6600");
    private static int GREEN_COLOR = Color.parseColor("#669900");
    private static int RED_COLOR = Color.parseColor("#FF0000");

    private static SimpleDateFormat inputSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat outputSdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mealDiary = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadMealDiary(getMealDiaryId());
        mealType =  GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadMealType(mealDiary.getMealTypeId());

        try {
            Date dateParsed = inputSdf.parse(mealDiary.getMealDate());
            dateFormatted = outputSdf.format(dateParsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("MealDiaryThirdStepFragment.onCreate");
        View layout = inflater.inflate(R.layout.meal_diary_third_step_view, container, false);
        newMealFoodListFragment = new NewMealFoodListFragment();
        initFields(layout);

        android.support.v4.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        List<FoodDiary> foodDiaries = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadFoodDiaries(mealDiary.getId());
        newMealFoodListFragment.setNewMealFoods(foodDiaries);

        Bundle arguments = new Bundle();
        arguments.putLong(NewMealConstants.NEW_MEAL_TYPE_ID_PARAMETER, mealDiary.getMealTypeId());
        newMealFoodListFragment.setArguments(arguments);

        fragmentTransaction.replace(R.id.meal_diary_third_step_food_container, newMealFoodListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

        return layout;
    }

    @Override
    public void onDestroy() {
        log("MealDiaryThirdStepFragment.onDestroy");
        super.onDestroy();
    }

    private void initFields(View layout) {
        log("MealDiaryThirdStepFragment.initFieldsAndButtonForEdition");
        TextView title = (TextView) layout.findViewById(R.id.meal_diary_third_step_meal_type_textview);
        title.setText(mealType.getName());

        carbohydrateContainer = (LinearLayout) layout.findViewById(R.id.meal_diary_third_step_carbohydrate_container);
        if (mealDiary.getCarbohydrateTotal() > mealType.getFoodTarget() + 5) {
            carbohydrateContainer.setBackgroundColor(ORANGE_COLOR);
        } else {
            carbohydrateContainer.setBackgroundColor(GREEN_COLOR);
        }

        carbohydrateTextView = (TextView) layout.findViewById(R.id.meal_diary_third_step_carbohydrate_value_textview);
        carbohydrateTextView.setText(format(mealDiary.getCarbohydrateTotal()));

        carbohydrateUnitTextView = (TextView) layout.findViewById(R.id.meal_diary_third_step_carbohydrate_unit_textview);
        carbohydrateUnitTextView.setText("[g]");

        insulinTextView = (TextView) layout.findViewById(R.id.meal_diary_third_step_insulin_value_textview);
        insulinTextView.setText(format(mealDiary.getBolusCalculated()));
        insulinTextView.setTypeface(null, Typeface.BOLD);

        TextView insulinUnitTextView = (TextView) layout.findViewById(R.id.meal_diary_third_step_insulin_unit_textview);
        insulinUnitTextView.setText("[UI]");


        TextView glycemiaTextView = (TextView) layout.findViewById(R.id.meal_diary_third_step_glycemia_value_textview);
        glycemiaTextView.setText(format(mealDiary.getGlycemiaMeasured()));

        glycemiaContainer = (LinearLayout) layout.findViewById(R.id.meal_diary_third_step_glycemia_container);
        EnumColor color = MainActivity.GLOBAL_BLOOD_GLUCOSE.getColor(mealDiary.getGlycemiaMeasured());
        switch (color) {
            case GREEN:
                glycemiaContainer.setBackgroundColor(GREEN_COLOR);
                break;
            case RED:
                glycemiaContainer.setBackgroundColor(RED_COLOR);
                break;
            case ORANGE:
                glycemiaContainer.setBackgroundColor(ORANGE_COLOR);
                break;
        }

        TextView glycemiaUnitTextView = (TextView) layout.findViewById(R.id.meal_diary_third_step_glycemia_unit_textview);
        glycemiaUnitTextView.setText(MainActivity.GLOBAL_BLOOD_GLUCOSE.getLabel());

        bolusGivenTextView = (TextView) layout.findViewById(R.id.meal_diary_third_step_bolus_given_textview);
        bolusGivenTextView.setText(format(mealDiary.getBolusGiven()));

        TextView dateTextView = (TextView) layout.findViewById(R.id.meal_diary_third_step_date_textview);
        dateTextView.setText(dateFormatted);
    }

    private Long getMealDiaryId() {
        return getArguments().getLong(MealDiaryConstants.MEAL_DIARY_ID_PARAMETER, MealDiaryConstants.MEAL_DIARY_ID_DEFAULT);
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

    private String format(float number) {
        return String.format("%.2f", number).replaceAll(",", ".");
    }
}
