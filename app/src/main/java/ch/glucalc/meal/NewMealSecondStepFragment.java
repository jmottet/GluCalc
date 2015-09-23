package ch.glucalc.meal;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;
import ch.glucalc.food.favourite.food.FavouriteFood;
import ch.glucalc.meal.diary.FoodDiary;
import ch.glucalc.meal.diary.MealDiary;
import ch.glucalc.meal.type.MealType;

public class NewMealSecondStepFragment extends Fragment {

    private static String TAG = "GluCalc";

    private MealType mealType;
    private MealDiary mealDiary = null;
    private boolean alreadyExists = true;
    private OnNewMealFoodDiaryAddition mCallback;

    private TextView carbohydrateTextView;

    private NewMealFoodListFragment newMealFoodListFragment;

    // Container Activity must implement this interface
    public interface OnNewMealFoodDiaryAddition {

        void openNewMealFoodDiaryListSelectionFragment(long mealDiaryId);
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
            mealDiary.setGlycemiaMeasured(getCarbohydrate());

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
        carbohydrateTextView.setText("0.00");

        TextView carbohydrateUnitTextView = (TextView) layout.findViewById(R.id.new_meal_second_step_carbohydrate_unit_textview);
        carbohydrateUnitTextView.setText("[g]");

        TextView insulinTextView = (TextView) layout.findViewById(R.id.new_meal_second_step_insulin_value_textview);
        insulinTextView.setText("-0.17");
        insulinTextView.setTypeface(null, Typeface.BOLD);

        TextView insulinUnitTextView = (TextView) layout.findViewById(R.id.new_meal_second_step_insulin_unit_textview);
        insulinUnitTextView.setText("[UI]");


        TextView glycemiaTextView = (TextView) layout.findViewById(R.id.new_meal_second_step_glycemia_value_textview);
        glycemiaTextView.setText(format(mealDiary.getGlycemiaMeasured()));
        TextView glycemiaUnitTextView = (TextView) layout.findViewById(R.id.new_meal_second_step_glycemia_unit_textview);
        glycemiaUnitTextView.setText("[mmol/l]");

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
                unselectButton.setVisibility(View.INVISIBLE);
                deleteButton.setVisibility(View.INVISIBLE);
                selectButton.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.VISIBLE);
            }
        });
        deleteButton.setVisibility(View.INVISIBLE);
        newMealFoodListFragment.setDeleteButton(deleteButton);

        selectButton.setVisibility(View.VISIBLE);

        unselectButton.setVisibility(View.INVISIBLE);

        selectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newMealFoodListFragment.setSectionMode(true);
                selectButton.setVisibility(View.INVISIBLE);
                unselectButton.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.INVISIBLE);
            }
        });

        unselectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newMealFoodListFragment.setSectionMode(false);
                unselectButton.setVisibility(View.INVISIBLE);
                deleteButton.setVisibility(View.INVISIBLE);
                selectButton.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.VISIBLE);
                newMealFoodListFragment.initList();
            }
        });

    }

    private void computeCarbohydrateTotal() {
        carbohydrateTextView.setText(format(newMealFoodListFragment.getCarbohydrateTotal()));

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

    private float getCarbohydrate() {
        return getArguments().getFloat(NewMealConstants.NEW_MEAL_CARBOHYDRATE_PARAMETER);
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

    private String format(float number) {
        return String.format("%.2f", number).replaceAll(",", ".");
    }
}
