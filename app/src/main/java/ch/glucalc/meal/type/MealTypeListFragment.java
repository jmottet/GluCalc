package ch.glucalc.meal.type;

import static ch.glucalc.meal.type.MealTypeConstants.CREATED_ID_RESULT;
import static ch.glucalc.meal.type.MealTypeConstants.MODIFIED_ID_RESULT;
import static ch.glucalc.meal.type.MealTypeConstants.REQUEST_CREATE_CODE;
import static ch.glucalc.meal.type.MealTypeConstants.REQUEST_EDIT_CODE;
import static ch.glucalc.meal.type.MealTypeConstants.RESULT_CODE_CREATED;
import static ch.glucalc.meal.type.MealTypeConstants.RESULT_CODE_EDITED;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import ch.glucalc.beans.DeleteBean;

@SuppressLint("DefaultLocale")
public class MealTypeListFragment extends ListFragment implements OnClickListener {

  private static String TAG = "GluCalc";

  private List<MealType> mealTypes;

  private MealTypeAdapter mealTypeAdapter;

  private final DeleteBean deleteBean = new DeleteBean();


  public MealTypeListFragment () {
      log("MealTypeListFragment.new");
  }
  @Override
  public void onCreate(Bundle savedInstanceState) {
    log("MealTypeListFragment.onCreate");
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    deleteBean.setNumberItemSelected(0);
    deleteBean.setModeMultiSelection(false);

    // Load the mealTypes from the Database
    mealTypes = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadMealTypes();

    // Set the list adapter for this ListFragment
    mealTypeAdapter = new MealTypeAdapter(getActivity(), mealTypes);
    setListAdapter(mealTypeAdapter);

  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    deleteBean.setmMenu(menu);
    inflater.inflate(R.menu.add_menu, menu);
    inflater.inflate(R.menu.selection_menu, menu);
    initMenuVisibility();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    log("MealTypeListFragment.onOptionsItemSelected");
    switch (item.getItemId()) {
    case R.id.add:
      addAction();
      return true;
    case R.id.delete:
      deleteAction();
      return true;
    case R.id.selection:
      deleteBean.setModeMultiSelection(true);
      initMenuVisibility();
      return true;
    case R.id.selection_performed:
      deleteBean.setModeMultiSelection(false);
      initMenuVisibility();
      mealTypeAdapter.notifyDataSetChanged();
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    log("MealTypeListFragment.onListItemClick");
    final MealType currentMealType = (MealType) getListView().getItemAtPosition(position);

    super.onListItemClick(l, v, position, id);

    if (!deleteBean.isModeMultiSelection()) {
      final Intent editMealTypeIntent = new Intent(getActivity().getApplicationContext(), EditMealTypeActivity.class);

      editMealTypeIntent.putExtra(MealTypeConstants.MEAL_TYPE_ID_PARAMETER, currentMealType.getId());
      editMealTypeIntent.putExtra(MealTypeConstants.MEAL_TYPE_NAME_PARAMETER, currentMealType.getName());
      editMealTypeIntent.putExtra(MealTypeConstants.MEAL_TYPE_FOOD_TARGET_PARAMETER, currentMealType.getFoodTarget());
      editMealTypeIntent.putExtra(MealTypeConstants.MEAL_TYPE_GLYCEMIA_TARGET_PARAMETER,
          currentMealType.getGlycemiaTarget());
      editMealTypeIntent.putExtra(MealTypeConstants.MEAL_TYPE_INSULIN_SENSITIVITY_PARAMETER,
          currentMealType.getInsulinSensitivity());
      editMealTypeIntent.putExtra(MealTypeConstants.MEAL_TYPE_INSULIN_PARAMETER, currentMealType.getInsulin());

      // Un resultat est attendu pour savoir si la categorie a ete modifiee
      startActivityForResult(editMealTypeIntent, REQUEST_EDIT_CODE);
    } else {
      if (!currentMealType.isSelected()) {
        v.setBackgroundColor(getResources().getColor(R.color.lightSkyBlue));
        currentMealType.setSelected(true);
        deleteBean.addOneToNumberItemSelected();
        if (deleteBean.getNumberItemSelected() == 1) {
          deleteBean.getmMenu().findItem(R.id.delete).setVisible(true);
        }
      } else {
        v.setBackground(null);
        currentMealType.setSelected(false);
        deleteBean.substractOneToNumberItemSelected();
        if (deleteBean.getNumberItemSelected() == 0) {
          deleteBean.getmMenu().findItem(R.id.delete).setVisible(false);
        }
      }
    }
  }

  @Override
  public void onClick(DialogInterface dialog, int which) {
    deleteMealTypes();
    refreshMealTypes();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    // Check which request we're responding to
    if (requestCode == REQUEST_EDIT_CODE) {
      // Make sure the request was successful
      if (resultCode == RESULT_CODE_EDITED) {
        final long modifiedId = data.getExtras().getLong(MODIFIED_ID_RESULT);
        final MealType modifiedMealType = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(
            getActivity().getApplicationContext()).loadMealType(modifiedId);
        updateMealTypeList(modifiedMealType);
      }
    } else if (requestCode == REQUEST_CREATE_CODE) {
      // Make sure the request was successful
      if (resultCode == RESULT_CODE_CREATED) {
        final long createdId = data.getExtras().getLong(CREATED_ID_RESULT);
        final MealType createdMealType = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(
            getActivity().getApplicationContext()).loadMealType(createdId);
        updateMealTypeList(createdMealType);
      }
    }
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    log("MealTypeListFragment.onActivityCreated");
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onAttach(Activity activity) {
    log("MealTypeListFragment.onAttach");
    super.onAttach(activity);
  }

  @Override
  public void onDestroy() {
    log("MealTypeListFragment.onDestroy");
    super.onDestroy();
  }

  @Override
  public void onDetach() {
    log("MealTypeListFragment.onDetach");
    super.onDetach();
  }

  @Override
  public void onPause() {
    log("MealTypeListFragment.onPause");
    super.onPause();
  }

  @Override
  public void onResume() {
    log("MealTypeListFragment.onResume");
    super.onResume();
  }

  @Override
  public void onStart() {
    log("MealTypeListFragment.onStart");
    super.onStart();
  }

  @Override
  public void onStop() {
    log("MealTypeListFragment.onStop");
    super.onStart();
  }

  private void log(String msg) {
    Log.i(TAG, msg);
  }

  /**
   * Modify the corresponding mealType of the list if it already exists,
   * otherwise add the mealType to the list
   * 
   * @param aMealType
   *          - the mealType to be added to the list
   */
  private void updateMealTypeList(MealType aMealType) {
    boolean itemHasBeenReplace = false;
    final ListIterator<MealType> listIterator = mealTypes.listIterator();
    while (listIterator.hasNext() && !itemHasBeenReplace) {
      final MealType currentMealType = listIterator.next();
      if (currentMealType.getId() == aMealType.getId()) {
        listIterator.set(aMealType);
        itemHasBeenReplace = true;
      }
    }
    if (!itemHasBeenReplace) {
      mealTypes.add(aMealType);
    }
    sortMealTypes();
    mealTypeAdapter.notifyDataSetChanged();
  }

  private void sortMealTypes() {
    Collections.sort(mealTypes, new Comparator<MealType>() {

      @Override
      public int compare(MealType lhs, MealType rhs) {
        return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
      }
    });
  }

  private void initMenuVisibility() {
    if (!deleteBean.isModeMultiSelection()) {
      deleteBean.getmMenu().findItem(R.id.add).setVisible(true);
      deleteBean.getmMenu().findItem(R.id.delete).setVisible(false);
      deleteBean.getmMenu().findItem(R.id.selection).setVisible(true);
      deleteBean.getmMenu().findItem(R.id.selection_performed).setVisible(false);
    } else {
      deleteBean.getmMenu().findItem(R.id.add).setVisible(false);
      deleteBean.getmMenu().findItem(R.id.selection).setVisible(false);
      deleteBean.getmMenu().findItem(R.id.selection_performed).setVisible(true);
      if (deleteBean.getNumberItemSelected() == 0) {
        deleteBean.getmMenu().findItem(R.id.delete).setVisible(false);
      } else {
        deleteBean.getmMenu().findItem(R.id.delete).setVisible(true);
      }
    }
  }

  private void addAction() {
    final Intent createMealTypeIntent = new Intent(getActivity().getApplicationContext(), EditMealTypeActivity.class);
    // Un resultat est attendu pour savoir si la categorie a ete cree
    startActivityForResult(createMealTypeIntent, REQUEST_CREATE_CODE);
  }

  private void deleteAction() {
    // show dialog confirmation if needed
    boolean isSomeFoodLinked = false;
    deleteBean.resetIdsToDelete();
    for (final MealType mealType : mealTypes) {
      if (mealType.isSelected()) {
        if (!isSomeFoodLinked
            && GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).existFoodFromCategory(
                mealType.getId())) {
          isSomeFoodLinked = true;
        }
        deleteBean.addIdToDelete(mealType.getId());
      }
    }
    deleteMealTypes();
    refreshMealTypes();
  }

  private void deleteMealTypes() {
    for (final Long mealTypeId : deleteBean.getIdsToDelete()) {
      GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).deleteMealType(mealTypeId);
    }
  }

  private void refreshMealTypes() {
    // Reload the mealTypes from the Database
    mealTypes.clear();
    mealTypes.addAll(GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadMealTypes());
    mealTypeAdapter.notifyDataSetChanged();
  }

}
