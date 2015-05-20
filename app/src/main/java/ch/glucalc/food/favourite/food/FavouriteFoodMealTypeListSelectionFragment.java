package ch.glucalc.food.favourite.food;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.meal.type.MealType;

@SuppressLint("DefaultLocale")
public class FavouriteFoodMealTypeListSelectionFragment extends ListFragment {

    private static String TAG = "GluCalc";


    private List<MealType> mealTypes;

    private FavouriteFoodMealTypeAdapter mealTypeAdapter;

    private OnMealTypeFavouriteFood mCallback;


    // Container Activity must implement this interface
    public interface OnMealTypeFavouriteFood {

        public void openFavouriteFoodListFragment(long mealTypeId);

    }

    public FavouriteFoodMealTypeListSelectionFragment() {
        log("FavouriteFoodMealTypeListSelectionFragment.new");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMealTypeFavouriteFood) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMealTypeFavouriteFood");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("FavouriteFoodMealTypeListSelectionFragment.onCreate");
        super.onCreate(savedInstanceState);

        // Load the mealTypes from the Database
        mealTypes = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadMealTypes();

        // Set the list adapter for this ListFragment
        mealTypeAdapter = new FavouriteFoodMealTypeAdapter(getActivity(), mealTypes);
        setListAdapter(mealTypeAdapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        log("FavouriteFoodMealTypeListSelectionFragment.onListItemClick");
        final MealType currentMealType = (MealType) getListView().getItemAtPosition(position);

        super.onListItemClick(l, v, position, id);
        mCallback.openFavouriteFoodListFragment(currentMealType.getId());
//        if (!deleteBean.isModeMultiSelection()) {
//            mCallback.openEditMealTypeFragment(currentMealType);
//        } else {
//            if (!currentMealType.isSelected()) {
//                v.setBackgroundColor(getResources().getColor(R.color.lightSkyBlue));
//                currentMealType.setSelected(true);
//                deleteBean.addOneToNumberItemSelected();
//                if (deleteBean.getNumberItemSelected() == 1) {
//                    deleteBean.getmMenu().findItem(R.id.delete).setVisible(true);
//                }
//            } else {
//                v.setBackground(null);
//                currentMealType.setSelected(false);
//                deleteBean.substractOneToNumberItemSelected();
//                if (deleteBean.getNumberItemSelected() == 0) {
//                    deleteBean.getmMenu().findItem(R.id.delete).setVisible(false);
//                }
//            }
//        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        log("FavouriteFoodMealTypeListSelectionFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        log("FavouriteFoodMealTypeListSelectionFragment.onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        log("FavouriteFoodMealTypeListSelectionFragment.onDetach");
        super.onDetach();
    }

    @Override
    public void onPause() {
        log("FavouriteFoodMealTypeListSelectionFragment.onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        log("FavouriteFoodMealTypeListSelectionFragment.onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        log("FavouriteFoodMealTypeListSelectionFragment.onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        log("FavouriteFoodMealTypeListSelectionFragment.onStop");
        super.onStart();
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }

    private void addAction() {

        //final Intent createMealTypeIntent = new Intent(getActivity().getApplicationContext(), EditMealTypeFragment.class);
        // Un resultat est attendu pour savoir si la categorie a ete cree
        //startActivityForResult(createMealTypeIntent, REQUEST_CREATE_CODE);
    }


}
