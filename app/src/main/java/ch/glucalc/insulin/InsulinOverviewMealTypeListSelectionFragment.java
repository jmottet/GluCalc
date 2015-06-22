package ch.glucalc.insulin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.meal.type.MealType;

@SuppressLint("DefaultLocale")
public class InsulinOverviewMealTypeListSelectionFragment extends ListFragment {

    private static String TAG = "GluCalc";


    private List<MealType> mealTypes;

    private InsulinOverviewMealTypeAdapter mealTypeAdapter;

    private OnMealTypeInsulinOverview mCallback;


    // Container Activity must implement this interface
    public interface OnMealTypeInsulinOverview {

        public void openInsulinOverviewMealTypeFragment(long mealTypeId);

    }

    public InsulinOverviewMealTypeListSelectionFragment() {
        log("InsulinOverviewMealTypeListSelectionFragment.new");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMealTypeInsulinOverview) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMealTypeFavouriteFood");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("InsulinOverviewMealTypeListSelectionFragment.onCreate");
        super.onCreate(savedInstanceState);

        // Load the mealTypes from the Database
        mealTypes = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadMealTypes();

        // Set the list adapter for this ListFragment
        mealTypeAdapter = new InsulinOverviewMealTypeAdapter(getActivity(), mealTypes);
        setListAdapter(mealTypeAdapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        log("InsulinOverviewMealTypeListSelectionFragment.onListItemClick");
        final MealType currentMealType = (MealType) getListView().getItemAtPosition(position);

        super.onListItemClick(l, v, position, id);
        mCallback.openInsulinOverviewMealTypeFragment(currentMealType.getId());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        log("InsulinOverviewMealTypeListSelectionFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        log("InsulinOverviewMealTypeListSelectionFragment.onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        log("InsulinOverviewMealTypeListSelectionFragment.onDetach");
        super.onDetach();
    }

    @Override
    public void onPause() {
        log("InsulinOverviewMealTypeListSelectionFragment.onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        log("InsulinOverviewMealTypeListSelectionFragment.onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        log("InsulinOverviewMealTypeListSelectionFragment.onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        log("InsulinOverviewMealTypeListSelectionFragment.onStop");
        super.onStart();
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

}
