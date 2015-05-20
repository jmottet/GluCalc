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
import ch.glucalc.beans.SelectionBean;

@SuppressLint("DefaultLocale")
public class MealTypeListFragment extends ListFragment implements OnClickListener {

    private static String TAG = "GluCalc";

    private List<MealType> mealTypes;

    private MealTypeAdapter mealTypeAdapter;

    private final SelectionBean selectionBean = new SelectionBean();

    private OnMealTypeEdition mCallback;

    // Container Activity must implement this interface
    public interface OnMealTypeEdition {

        public void openEditMealTypeFragment();

        public void openEditMealTypeFragment(MealType mealType);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnMealTypeEdition) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMealTypeEdition");
        }
    }

    public MealTypeListFragment() {
        log("MealTypeListFragment.new");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("MealTypeListFragment.onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        selectionBean.setNumberItemSelected(0);
        selectionBean.setModeMultiSelection(false);

        // Load the mealTypes from the Database
        mealTypes = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).loadMealTypes();

        // Set the list adapter for this ListFragment
        mealTypeAdapter = new MealTypeAdapter(getActivity(), mealTypes);
        setListAdapter(mealTypeAdapter);

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
        log("MealTypeListFragment.onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.add:
                mCallback.openEditMealTypeFragment();
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

        if (!selectionBean.isModeMultiSelection()) {
            mCallback.openEditMealTypeFragment(currentMealType);
        } else {
            if (!currentMealType.isSelected()) {
                v.setBackgroundColor(getResources().getColor(R.color.lightSkyBlue));
                currentMealType.setSelected(true);
                selectionBean.addOneToNumberItemSelected();
                if (selectionBean.getNumberItemSelected() == 1) {
                    selectionBean.getmMenu().findItem(R.id.delete).setVisible(true);
                }
            } else {
                v.setBackground(null);
                currentMealType.setSelected(false);
                selectionBean.substractOneToNumberItemSelected();
                if (selectionBean.getNumberItemSelected() == 0) {
                    selectionBean.getmMenu().findItem(R.id.delete).setVisible(false);
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
     * @param aMealType - the mealType to be added to the list
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

    private void addAction() {

        final Intent createMealTypeIntent = new Intent(getActivity().getApplicationContext(), EditMealTypeFragment.class);
        // Un resultat est attendu pour savoir si la categorie a ete cree
        startActivityForResult(createMealTypeIntent, REQUEST_CREATE_CODE);
    }

    private void deleteAction() {
        // show dialog confirmation if needed
        boolean isSomeFoodLinked = false;
        selectionBean.resetIdsToDelete();
        for (final MealType mealType : mealTypes) {
            if (mealType.isSelected()) {
                if (!isSomeFoodLinked
                        && GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).existFoodFromCategory(
                        mealType.getId())) {
                    isSomeFoodLinked = true;
                }
                selectionBean.addIdToDelete(mealType.getId());
            }
        }
        deleteMealTypes();
        refreshMealTypes();
    }

    private void deleteMealTypes() {
        for (final Long mealTypeId : selectionBean.getIdsToDelete()) {
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
