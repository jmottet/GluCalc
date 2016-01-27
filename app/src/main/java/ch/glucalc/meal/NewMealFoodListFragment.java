package ch.glucalc.meal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.glucalc.GestureHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.NavigationBackAndNext;
import ch.glucalc.R;
import ch.glucalc.beans.SelectionBean;
import ch.glucalc.meal.diary.FoodDiary;

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

    private NavigationBackAndNext navigationBackAndNext;

    // Container Activity must implement this interface
    public interface OnNewMealFoodEdition {

        // public void openFavouriteFoodListSelectionFragment(long mealTypeId);

        void openEditNewMealFoodFragment(FoodDiary newMealFood);

    }

    public void setNewMealFoods(List<FoodDiary> newMealFoods) {
        this.newMealFoods = newMealFoods;
    }

    public void setNavigationBackAndNext(NavigationBackAndNext navigationBackAndNext) {
        this.navigationBackAndNext = navigationBackAndNext;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (navigationBackAndNext != null) {
            initializeGestureDetector(getListView());
        }
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

    private void initializeGestureDetector(View layout) {
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        log("NewMealFragment.onFling");
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > GestureHelper.SWIPE_MAX_OFF_PATH)
                                return false;
                            if (GestureHelper.isGestureRightToLeft(e1, e2, velocityX)) {
                                navigationBackAndNext.goToNextPage();

                            } else if (GestureHelper.isGestureLeftToRight(e1, e2, velocityX)) {
                                navigationBackAndNext.goToPreviousPage();
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
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
