package ch.glucalc.food.category;

import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import ch.glucalc.GluCalcSQLiteHelper;

public class CategoryFoodListFragment extends ListFragment {

  private static String TAG = "GluCalc";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    log("CategoryFoodListFragment.onCreate");
    super.onCreate(savedInstanceState);

    final List<CategoryFood> categories = new GluCalcSQLiteHelper(getActivity().getApplicationContext())
        .loadCategoriesOfFood();

    // Set the list adapter for this ListFragment
    setListAdapter(new CategoryFoodAdapter(getActivity(), categories));

  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    log("CategoryFoodListFragment.onActivityCreated");
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onAttach(Activity activity) {
    log("CategoryFoodListFragment.onAttach");
    super.onAttach(activity);
  }

  @Override
  public void onDestroy() {
    log("CategoryFoodListFragment.onDestroy");
    super.onDestroy();
  }

  @Override
  public void onDetach() {
    log("CategoryFoodListFragment.onDetach");
    super.onDetach();
  }

  @Override
  public void onPause() {
    log("CategoryFoodListFragment.onPause");
    super.onPause();
  }

  @Override
  public void onResume() {
    log("CategoryFoodListFragment.onResume");
    super.onResume();
  }

  @Override
  public void onStart() {
    log("CategoryFoodListFragment.onStart");
    super.onStart();
  }

  @Override
  public void onStop() {
    log("CategoryFoodListFragment.onStop");
    super.onStart();
  }

  private void log(String msg) {
    Log.i(TAG, msg);
  }

}
