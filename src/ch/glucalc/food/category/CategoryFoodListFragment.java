package ch.glucalc.food.category;

import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import ch.glucalc.GluCalcSQLiteHelper;

public class CategoryFoodListFragment extends ListFragment {

  private static String TAG = "GluCalc";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    log("CategoryFoodListFragment.onCreate");
    super.onCreate(savedInstanceState);

    final List<CategoryFood> categories = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(
        getActivity().getApplicationContext()).loadCategoriesOfFood();

    // Set the list adapter for this ListFragment
    final CategoryFoodAdapter categoryFoodAdapter = new CategoryFoodAdapter(getActivity(), categories);
    setListAdapter(categoryFoodAdapter);

  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    log("CategoryFoodListFragment.onListItemClick");
    final CategoryFood currentCategory = (CategoryFood) getListView().getItemAtPosition(position);

    super.onListItemClick(l, v, position, id);

    final Intent editCategoryFoodIntent = new Intent(getActivity().getApplicationContext(),
        EditCategoryFoodActivity.class);

    editCategoryFoodIntent.putExtra("categoryId", currentCategory.getId());
    editCategoryFoodIntent.putExtra("categoryName", currentCategory.getName());

    startActivity(editCategoryFoodIntent);
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
