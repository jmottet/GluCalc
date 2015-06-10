package ch.glucalc.food.favourite.food;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;
import ch.glucalc.beans.SelectionBean;
import ch.glucalc.meal.type.MealType;
import ch.glucalc.meal.type.MealTypeConstants;

import static ch.glucalc.food.category.CategoryFoodConstants.FAKE_DEFAULT_ID;

@SuppressLint("DefaultLocale")
public class FavouriteFoodListFragment extends ListFragment {

    private static String TAG = "GluCalc";

    private  FavouriteFoodAdapter favouriteFoodAdapter;
    private List<FavouriteFood> favouriteFoods;

    private final SelectionBean selectionBean = new SelectionBean();

    private OnFavouriteFoodAddition mCallback;

    // Container Activity must implement this interface
    public interface OnFavouriteFoodAddition {

        public void openFavouriteFoodListSelectionFragment(long mealTypeId);

        public void openEditFavouriteFoodFragment(FavouriteFood favouriteFood);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnFavouriteFoodAddition) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMealTypeEdition");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("FavouriteFoodListFragment.onCreate");
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        selectionBean.setNumberItemSelected(0);
        selectionBean.setModeMultiSelection(false);

        // Load the mealTypes from the Database
        favouriteFoods = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadFavouriteFoods(getMealTypeId());

        // Set the list adapter for this ListFragment
        favouriteFoodAdapter = new FavouriteFoodAdapter(getActivity(), favouriteFoods);
        setListAdapter(favouriteFoodAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        selectionBean.setmMenu(menu);
        inflater.inflate(R.menu.add_menu, menu);
        inflater.inflate(R.menu.selection_menu, menu);
        initMenuVisibility();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        log("FavouriteFoodListFragment.onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.add:
                mCallback.openFavouriteFoodListSelectionFragment(getMealTypeId());
                return true;
            case R.id.delete:
                deleteAction();
                return true;
            case R.id.selection:
                selectionBean.setModeMultiSelection(true);
                initMenuVisibility();
                return true;
            case R.id.selection_performed:
                selectionBean.setModeMultiSelection(false);
                initMenuVisibility();
                favouriteFoodAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        log("FavouriteFoodListFragment.onListItemClick");
        final FavouriteFood currentFavouriteFood = (FavouriteFood) getListView().getItemAtPosition(position);

        super.onListItemClick(l, v, position, id);

        if (!selectionBean.isModeMultiSelection()) {
            mCallback.openEditFavouriteFoodFragment(currentFavouriteFood);
        } else {
            if (!currentFavouriteFood.isSelected()) {
                v.setBackgroundColor(getResources().getColor(R.color.lightSkyBlue));
                currentFavouriteFood.setSelected(true);
                selectionBean.addOneToNumberItemSelected();
                if (selectionBean.getNumberItemSelected() == 1) {
                    selectionBean.getmMenu().findItem(R.id.delete).setVisible(true);
                }
            } else {
                v.setBackground(null);
                currentFavouriteFood.setSelected(false);
                selectionBean.substractOneToNumberItemSelected();
                if (selectionBean.getNumberItemSelected() == 0) {
                    selectionBean.getmMenu().findItem(R.id.delete).setVisible(false);
                }
            }
        }
    }
    @Override
    public void onDetach() {
        log("FavouriteFoodListFragment.onDetach");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        log("FavouriteFoodListFragment.onDestroy");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        log("FavouriteFoodListFragment.onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        log("FavouriteFoodListFragment.onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        log("FavouriteFoodListFragment.onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        log("FavouriteFoodListFragment.onStop");
        super.onStart();
    }


    private long getMealTypeId() {
        return getArguments().getLong(MealTypeConstants.MEAL_TYPE_ID_PARAMETER, FAKE_DEFAULT_ID);
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

    private void initMenuVisibility() {
        if (!selectionBean.isModeMultiSelection()) {
            selectionBean.getmMenu().findItem(R.id.add).setVisible(true);
            selectionBean.getmMenu().findItem(R.id.delete).setVisible(false);
            selectionBean.getmMenu().findItem(R.id.selection).setVisible(true);
            selectionBean.getmMenu().findItem(R.id.selection_performed).setVisible(false);
        } else {
            selectionBean.getmMenu().findItem(R.id.add).setVisible(false);
            selectionBean.getmMenu().findItem(R.id.selection).setVisible(false);
            selectionBean.getmMenu().findItem(R.id.selection_performed).setVisible(true);
            if (selectionBean.getNumberItemSelected() == 0) {
                selectionBean.getmMenu().findItem(R.id.delete).setVisible(false);
            } else {
                selectionBean.getmMenu().findItem(R.id.delete).setVisible(true);
            }
        }
    }


    private void deleteAction() {
        selectionBean.resetIdsToDelete();
        for (final FavouriteFood favouriteFood : favouriteFoods) {
            if (favouriteFood.isSelected()) {
                selectionBean.addIdToDelete(favouriteFood.getId());
            }
        }
        deleteFavouriteFoods();
        refreshFavouriteFoods();
    }

    private void deleteFavouriteFoods() {
        for (final Long favouriteFoodId : selectionBean.getIdsToDelete()) {
            GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).deleteFavouriteFood(favouriteFoodId);
        }
    }

    private void refreshFavouriteFoods() {
        // Reload the favourite foods from the Database
        favouriteFoods.clear();
        favouriteFoods.addAll(GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadFavouriteFoods(getMealTypeId()));
        favouriteFoodAdapter.notifyDataSetChanged();
    }

}
