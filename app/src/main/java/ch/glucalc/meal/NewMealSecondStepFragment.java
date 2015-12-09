package ch.glucalc.meal;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.glucalc.EnumColor;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.MainActivity;
import ch.glucalc.R;
import ch.glucalc.food.favourite.food.FavouriteFood;
import ch.glucalc.meal.diary.FoodDiary;
import ch.glucalc.meal.diary.MealDiary;
import ch.glucalc.meal.type.MealType;
import ch.glucalc.util.ColorHelper;

public class NewMealSecondStepFragment extends Fragment {

    private static String TAG = "GluCalc";

    private MealType mealType;
    private MealDiary mealDiary = null;
    private boolean alreadyExists = true;
    private OnNewMealFoodDiaryAddition mCallback;
    private OnNewMealThirdStep mCallback2;

    private TextView carbohydrateTextView;
    private TextView insulinTextView;

    private NewMealFoodListFragment newMealFoodListFragment;

    private static int ORANGE_COLOR = Color.parseColor("#FF6600");
    private static int GREEN_COLOR = Color.parseColor("#669900");
    private static int RED_COLOR = Color.parseColor("#FF0000");

    public String getCarbohydrateUnit() {
        return "[g]";
    }

    public String getInsulinUnit() {
        return "[UI]";
    }

    public String getBloodGlucoseUnit() {
        return MainActivity.GLOBAL_BLOOD_GLUCOSE.getLabel();
    }

    // Container Activity must implement this interface
    public interface OnNewMealFoodDiaryAddition {

        void openNewMealFoodDiaryListSelectionFragment(long mealDiaryId);
    }

    public interface OnNewMealThirdStep {
        void openNewMealThirdStepFragment(long mealDiaryId);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnNewMealFoodDiaryAddition) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNewMealFoodDiaryAddition");
        }

        try {
            mCallback2 = (OnNewMealThirdStep) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNewMealThirdStep");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getNewMealDiaryId() == NewMealConstants.NEW_MEAL_DIARY_ID_DEFAULT) {
            mealType = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).loadMealType(getMealTypeId());
            mealDiary = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadMealDiaryTemporary();
            if (mealDiary == null) {
                mealDiary = new MealDiary();
                alreadyExists = false;
            }

            mealDiary.setMealTypeId(getMealTypeId());
            mealDiary.setGlycemiaMeasured(getBloodGlucose());

            if (alreadyExists) {
                GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).updateMealDiary(mealDiary);
            } else {
                long mealDiaryId = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).storeMealDiary(mealDiary);
                mealDiary.setId(mealDiaryId);
            }
        } else {
            mealDiary = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadMealDiary(getNewMealDiaryId());
            mealType = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadMealType(mealDiary.getMealTypeId());
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        log("NewMealSecondStepFragment.onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.next_page_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        log("NewMealSecondStepFragment.onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.next:
                // On sauve les données et on passe à l'écran 3 de la prise de repas
                GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).updateMealDiary(mealDiary);
                mCallback2.openNewMealThirdStepFragment(mealDiary.getId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("NewMealSecondStepFragment.onCreate");
        View layout = inflater.inflate(R.layout.new_meal_second_step_view, container, false);
        newMealFoodListFragment = new NewMealFoodListFragment();

        initFields(layout);

        android.support.v4.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        List<FoodDiary> foodDiaries;
        if (!alreadyExists) {
            if (isFavouriteFoodIncluded()) {

                List<FavouriteFood> favouriteFoods = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadFavouriteFoods(mealDiary.getMealTypeId());
                foodDiaries = new ArrayList<>(favouriteFoods.size());
                for (FavouriteFood favouriteFood : favouriteFoods) {
                    FoodDiary foodDiary = new FoodDiary(mealDiary.getId(), favouriteFood);
                    GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).storeFoodDiary(foodDiary);
                    foodDiaries.add(foodDiary);
                }
            } else {
                foodDiaries = new ArrayList<>();
            }
        } else {
            foodDiaries = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadFoodDiaries(mealDiary.getId());
        }
        newMealFoodListFragment.setNewMealFoods(foodDiaries);
        Bundle arguments = new Bundle();
        arguments.putLong(NewMealConstants.NEW_MEAL_TYPE_ID_PARAMETER, mealDiary.getMealTypeId());
        newMealFoodListFragment.setArguments(arguments);

        fragmentTransaction.replace(R.id.new_meal_second_step_food_container, newMealFoodListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

        computeCarbohydrateTotal();
        computeBolus();

        return layout;
    }

    @Override
    public void onDestroy() {
        log("NewMealSecondStepFragment.onDestroy");
        super.onDestroy();
    }

    private void initFields(View layout) {
        log("NewMealSecondStepFragment.initFieldsAndButtonForEdition");

        TextView mealTypeTextView = (TextView) layout.findViewById(R.id.new_meal_second_step_meal_type_textview);
        mealTypeTextView.setText(mealType.getName());

        carbohydrateTextView = (TextView) layout.findViewById(R.id.new_meal_second_step_carbohydrate_value_textview);
        carbohydrateTextView.setText("0.00" + " " + getCarbohydrateUnit());

        insulinTextView = (TextView) layout.findViewById(R.id.new_meal_second_step_insulin_value_textview);
        insulinTextView.setText("0.00" + " " + getInsulinUnit());
        insulinTextView.setTypeface(null, Typeface.BOLD);

        TextView bloodGlucoseTextView = (TextView) layout.findViewById(R.id.new_meal_second_step_blood_glucose_value_textview);
        bloodGlucoseTextView.setText(format(mealDiary.getGlycemiaMeasured()) + " " + getBloodGlucoseUnit());

        EnumColor color = MainActivity.GLOBAL_BLOOD_GLUCOSE.getColor(mealDiary.getGlycemiaMeasured());
        switch (color) {
            case GREEN:
                bloodGlucoseTextView.setBackgroundColor(GREEN_COLOR);
                break;
            case RED:
                bloodGlucoseTextView.setBackgroundColor(RED_COLOR);
                break;
            case ORANGE:
                bloodGlucoseTextView.setBackgroundColor(ORANGE_COLOR);
                break;
        }

        final ImageButton addButton = (ImageButton) layout.findViewById(R.id.new_meal_second_step_add_button);
        final ImageButton deleteButton = (ImageButton) layout.findViewById(R.id.new_meal_second_step_delete_button);
        final Button selectButton = (Button) layout.findViewById(R.id.new_meal_second_step_select_button);
        final ImageButton unselectButton = (ImageButton) layout.findViewById(R.id.new_meal_second_step_unselect_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCallback.openNewMealFoodDiaryListSelectionFragment(mealDiary.getId());
                computeCarbohydrateTotal();
            }
        });
        addButton.setVisibility(View.VISIBLE);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newMealFoodListFragment.deleteAction();
                computeCarbohydrateTotal();

                newMealFoodListFragment.setSectionMode(false);
                unselectButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                selectButton.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.VISIBLE);
            }
        });
        deleteButton.setVisibility(View.GONE);
        newMealFoodListFragment.setDeleteButton(deleteButton);

        selectButton.setVisibility(View.VISIBLE);

        unselectButton.setVisibility(View.GONE);

        selectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newMealFoodListFragment.setSectionMode(true);
                selectButton.setVisibility(View.GONE);
                unselectButton.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.GONE);
            }
        });

        unselectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newMealFoodListFragment.setSectionMode(false);
                unselectButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                selectButton.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.VISIBLE);
                newMealFoodListFragment.initList();
            }
        });

    }

    private void computeCarbohydrateTotal() {
        float carbohydrateTotal = newMealFoodListFragment.getCarbohydrateTotal();
        mealDiary.setCarbohydrateTotal(carbohydrateTotal);

        if (carbohydrateTotal > mealType.getFoodTarget() + 5) {
            carbohydrateTextView.setBackgroundColor(ORANGE_COLOR);
            String warningMessage = "La cible de glucose est dépassée de plus de 5 [g]";
            Toast.makeText(getActivity(), warningMessage, Toast.LENGTH_LONG).show();
        } else {
            carbohydrateTextView.setBackgroundColor(GREEN_COLOR);
        }
        carbohydrateTextView.setText(format(newMealFoodListFragment.getCarbohydrateTotal()) + " " + getCarbohydrateUnit());
    }

    private void computeBolus() {
        float bolus = getBolus(mealDiary.getGlycemiaMeasured(), mealType.getGlycemiaTarget(), mealType.getInsulin(), newMealFoodListFragment.getCarbohydrateTotal(), mealType.getInsulinSensitivity());
        mealDiary.setBolusCalculated(bolus);
        insulinTextView.setText(format(bolus) + " " + getInsulinUnit());
    }

    private float getBolus(float glycemiaMeasured, float glycemiaTarget, float insulinPerTen, float carbohydrate, float sensitivityFactor) {
        return (insulinPerTen * carbohydrate / 10) + (glycemiaMeasured - glycemiaTarget) / sensitivityFactor;

    }

    private Long getNewMealDiaryId() {
        return getArguments().getLong(NewMealConstants.NEW_MEAL_DIARY_ID_PARAMETER, NewMealConstants.NEW_MEAL_DIARY_ID_DEFAULT);
    }

    private long getMealTypeId() {
        return getArguments().getLong(NewMealConstants.NEW_MEAL_TYPE_ID_PARAMETER);
    }

    private boolean isFavouriteFoodIncluded() {
        return getArguments().getBoolean(NewMealConstants.NEW_MEAL_WITH_FAVOURITE_FOOD_PARAMETER);
    }

    private float getBloodGlucose() {
        return getArguments().getFloat(NewMealConstants.NEW_MEAL_BLOOD_GLUCOSE_PARAMETER);
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

    private String format(float number) {
        return String.format("%.2f", number).replaceAll(",", ".");
    }
}
