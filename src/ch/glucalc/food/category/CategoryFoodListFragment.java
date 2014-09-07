package ch.glucalc.food.category;

import static ch.glucalc.food.category.CategoryFoodConstants.CREATED_ID_RESULT;
import static ch.glucalc.food.category.CategoryFoodConstants.MODIFIED_ID_RESULT;
import static ch.glucalc.food.category.CategoryFoodConstants.REQUEST_CREATE_CODE;
import static ch.glucalc.food.category.CategoryFoodConstants.REQUEST_EDIT_CODE;
import static ch.glucalc.food.category.CategoryFoodConstants.RESULT_CODE_CREATED;
import static ch.glucalc.food.category.CategoryFoodConstants.RESULT_CODE_EDITED;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;

@SuppressLint("DefaultLocale")
public class CategoryFoodListFragment extends ListFragment {

  private static String TAG = "GluCalc";

  private List<CategoryFood> categories;

  private CategoryFoodAdapter categoryFoodAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    log("CategoryFoodListFragment.onCreate");
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    // Load the categories from the Database
    categories = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext())
        .loadCategoriesOfFood();

    // Set the list adapter for this ListFragment
    categoryFoodAdapter = new CategoryFoodAdapter(getActivity(), categories);
    setListAdapter(categoryFoodAdapter);

  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.add_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    log("CategoryFoodListFragment.onOptionsItemSelected");
    switch (item.getItemId()) {
    case R.id.add:
      final Intent createCategoryFoodIntent = new Intent(getActivity().getApplicationContext(),
          EditCategoryFoodActivity.class);
      // Un résultat est attendu pour savoir si la catégorie a été crée
      startActivityForResult(createCategoryFoodIntent, REQUEST_CREATE_CODE);
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    log("CategoryFoodListFragment.onListItemClick");
    final CategoryFood currentCategory = (CategoryFood) getListView().getItemAtPosition(position);

    super.onListItemClick(l, v, position, id);

    final Intent editCategoryFoodIntent = new Intent(getActivity().getApplicationContext(),
        EditCategoryFoodActivity.class);

    editCategoryFoodIntent.putExtra(CategoryFoodConstants.CATEGORY_ID_PARAMETER, currentCategory.getId());
    editCategoryFoodIntent.putExtra(CategoryFoodConstants.CATEGORY_NAME_PARAMETER, currentCategory.getName());

    // Un résultat est attendu pour savoir si la catégorie a été modifiée
    startActivityForResult(editCategoryFoodIntent, REQUEST_EDIT_CODE);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Check which request we're responding to
    if (requestCode == REQUEST_EDIT_CODE) {
      // Make sure the request was successful
      if (resultCode == RESULT_CODE_EDITED) {
        final long modifiedId = data.getExtras().getLong(MODIFIED_ID_RESULT);
        final CategoryFood modifiedCategory = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(
            getActivity().getApplicationContext()).loadCategoryOfFood(modifiedId);
        updateCategoryList(modifiedCategory);
      }
    } else if (requestCode == REQUEST_CREATE_CODE) {
      // Make sure the request was successful
      if (resultCode == RESULT_CODE_CREATED) {
        final long createdId = data.getExtras().getLong(CREATED_ID_RESULT);
        final CategoryFood createdCategory = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(
            getActivity().getApplicationContext()).loadCategoryOfFood(createdId);
        updateCategoryList(createdCategory);
      }
    }
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

  /**
   * Modify the corresponding category of the list if it already exists,
   * otherwise add the category to the list
   * 
   * @param aCategory
   *          - the category to be added to the list
   */
  private void updateCategoryList(CategoryFood aCategory) {
    boolean itemHasBeenReplace = false;
    final ListIterator<CategoryFood> listIterator = categories.listIterator();
    while (listIterator.hasNext() && !itemHasBeenReplace) {
      final CategoryFood currentCategory = listIterator.next();
      if (currentCategory.getId() == aCategory.getId()) {
        listIterator.set(aCategory);
        itemHasBeenReplace = true;
      }
    }
    if (!itemHasBeenReplace) {
      categories.add(aCategory);
    }
    sortCategories();
    categoryFoodAdapter.notifyDataSetChanged();
  }

  private void sortCategories() {
    Collections.sort(categories, new Comparator<CategoryFood>() {

      @Override
      public int compare(CategoryFood lhs, CategoryFood rhs) {
        return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
      }
    });
  }

}
