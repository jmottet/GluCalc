package ch.glucalc.meal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;
import ch.glucalc.beans.SelectionBean;
import ch.glucalc.food.category.CategoryFood;
import ch.glucalc.food.favourite.food.FavouriteFood;
import ch.glucalc.food.favourite.food.FavouriteFoodAdapter;
import ch.glucalc.meal.diary.FoodDiary;
import ch.glucalc.meal.diary.MealDiary;
import ch.glucalc.meal.type.MealTypeConstants;

import static ch.glucalc.food.category.CategoryFoodConstants.FAKE_DEFAULT_ID;

@SuppressLint("DefaultLocale")
public class NewMealFoodListFragment extends ListFragment {

    private static String TAG = "GluCalc";

    private  NewMealFoodAdapter newMealFoodAdapter;

    private List<FoodDiary> newMealFoods = new ArrayList<>();

    private OnNewMealFoodEdition mCallback;

    private final SelectionBean selectionBean = new SelectionBean();

    private ImageButton deleteButton;

    private boolean itemClickDisabled = false;

    // Container Activity must implement this interface
    public interface OnNewMealFoodEdition {

        // public void openFavouriteFoodListSelectionFragment(long mealTypeId);

        void openEditNewMealFoodFragment(FoodDiary newMealFood);

    }

    public void setNewMealFoods(List<FoodDiary> newMealFoods) {
        this.newMealFoods = newMealFoods;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnNewMealFoodEdition) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNewMealFoodEdition");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("NewMealFoodListFragment.onCreate");
        super.onCreate(savedInstanceState);

        // Set the list adapter for this ListFragment
        newMealFoodAdapter = new NewMealFoodAdapter(getActivity(), newMealFoods);
        setListAdapter(newMealFoodAdapter);
        selectionBean.setNumberItemSelected(0);
        selectionBean.setModeMultiSelection(false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        log("NewMealFoodListFragment.onListItemClick");

        super.onListItemClick(l, v, position, id);
        if (!itemClickDisabled) {
            final FoodDiary currentNewMealFood = (FoodDiary) getListView().getItemAtPosition(position);

            if (!selectionBean.isModeMultiSelection()) {
                mCallback.openEditNewMealFoodFragment(currentNewMealFood);
            } else {
                if (!currentNewMealFood.isSelected()) {
                    v.setBackgroundColor(getResources().getColor(R.color.lightSkyBlue));
                    currentNewMealFood.setSelected(true);
                    selectionBean.addOneToNumberItemSelected();
                    if (selectionBean.getNumberItemSelected() == 1) {
                        deleteButton.setVisibility(View.VISIBLE);
                    }
                } else {
                    v.setBackground(null);
                    currentNewMealFood.setSelected(false);
                    selectionBean.substractOneToNumberItemSelected();
                    if (selectionBean.getNumberItemSelected() == 0) {
                        deleteButton.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    public void setSectionMode(boolean mode) {
        if (mode) {
            selectionBean.setModeMultiSelection(true);
        } else {
            selectionBean.setModeMultiSelection(false);
            selectionBean.setNumberItemSelected(0);
        }
    }

    public void setItemClickDisabled(boolean itemClickDisabled) {
        this.itemClickDisabled = itemClickDisabled;
    }

    public void setDeleteButton(ImageButton aDeleteButton) {
        deleteButton = aDeleteButton;
    }

    @Override
    public void onDetach() {
        log("NewMealFoodListFragment.onDetach");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        log("NewMealFoodListFragment.onDestroy");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        log("NewMealFoodListFragment.onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        log("NewMealFoodListFragment.onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        log("NewMealFoodListFragment.onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        log("NewMealFoodListFragment.onStop");
        super.onStart();
    }

    public void deleteAction() {
        selectionBean.resetIdsToDelete();
        for (final FoodDiary foodDiary : newMealFoods) {
            if (foodDiary.isSelected()) {
                selectionBean.addIdToDelete(foodDiary.getId());
            }
        }

        for (final Long foodDiaryId : selectionBean.getIdsToDelete()) {
            GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).deleteFoodDiary(foodDiaryId);
        }

        refreshFoodDiaries();
    }

    private void refreshFoodDiaries() {
        // Reload the categories from the Database
        newMealFoods.clear();
        newMealFoods.addAll(GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadFoodDiaries(getMealDiaryId()));
        newMealFoodAdapter.notifyDataSetChanged();
    }

    public void initList() {
        selectionBean.resetIdsToDelete();
        selectionBean.setNumberItemSelected(0);
        newMealFoodAdapter.notifyDataSetChanged();
    }

    public float getCarbohydrateTotal() {
        float result = 0;
        for (final FoodDiary foodDiary : newMealFoods) {
            result += foodDiary.getCarbohydrate();
        }
        return result;
    }

    private long getMealDiaryId() {
        return getArguments().getLong(NewMealConstants.NEW_MEAL_DIARY_ID_PARAMETER, FAKE_DEFAULT_ID);
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }


}
