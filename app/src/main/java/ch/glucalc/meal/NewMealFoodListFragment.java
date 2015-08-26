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
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;
import ch.glucalc.beans.SelectionBean;
import ch.glucalc.food.favourite.food.FavouriteFood;
import ch.glucalc.food.favourite.food.FavouriteFoodAdapter;
import ch.glucalc.meal.type.MealTypeConstants;

import static ch.glucalc.food.category.CategoryFoodConstants.FAKE_DEFAULT_ID;

@SuppressLint("DefaultLocale")
public class NewMealFoodListFragment extends ListFragment {

    private static String TAG = "GluCalc";

    private  FavouriteFoodAdapter favouriteFoodAdapter;
    private List<FavouriteFood> favouriteFoods;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMealTypeEdition");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("NewMealFoodListFragment.onCreate");
        super.onCreate(savedInstanceState);

        // Load the mealTypes from the Database
        favouriteFoods = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadFavouriteFoods(getMealTypeId());

        // Set the list adapter for this ListFragment
        favouriteFoodAdapter = new FavouriteFoodAdapter(getActivity(), favouriteFoods);
        setListAdapter(favouriteFoodAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        log("NewMealFoodListFragment.onListItemClick");
        final FavouriteFood currentFavouriteFood = (FavouriteFood) getListView().getItemAtPosition(position);

        super.onListItemClick(l, v, position, id);

        Toast.makeText(getActivity(), "Position clicked : " + position, Toast.LENGTH_LONG).show();
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


    private long getMealTypeId() {
        return getArguments().getLong(NewMealConstants.NEW_MEAL_TYPE_ID_PARAMETER, FAKE_DEFAULT_ID);
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }


    private void refreshFavouriteFoods() {
        // Reload the favourite foods from the Database
        favouriteFoods.clear();
        favouriteFoods.addAll(GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadFavouriteFoods(getMealTypeId()));
        favouriteFoodAdapter.notifyDataSetChanged();
    }

}
