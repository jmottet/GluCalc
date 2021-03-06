package ch.glucalc.food.category;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import ch.glucalc.DialogHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;
import ch.glucalc.beans.SelectionBean;

@SuppressLint("DefaultLocale")
public class CategoryFoodListFragment extends ListFragment implements OnClickListener {

    private static String TAG = "GluCalc";

    private List<CategoryFood> categories;

    private CategoryFoodAdapter categoryFoodAdapter;

    private final SelectionBean selectionBean = new SelectionBean();

    private OnCategoryFoodEdition mCallback;

    // Container Activity must implement this interface
    public interface OnCategoryFoodEdition {

        public void openEditCategoryFoodFragment();

        public void openEditCategoryFoodFragment(CategoryFood categoryFood);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnCategoryFoodEdition) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCategoryFoodEdition");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log("CategoryFoodListFragment.onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        selectionBean.setNumberItemSelected(0);
        selectionBean.setModeMultiSelection(false);

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
        selectionBean.setmMenu(menu);
        inflater.inflate(R.menu.add_menu, menu);
        inflater.inflate(R.menu.selection_menu, menu);
        initMenuVisibility();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        log("CategoryFoodListFragment.onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.add:
                mCallback.openEditCategoryFoodFragment();
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
                categoryFoodAdapter.notifyDataSetChanged();
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

        if (!selectionBean.isModeMultiSelection()) {
            mCallback.openEditCategoryFoodFragment(currentCategory);
        } else {
            if (!currentCategory.isSelected()) {
                v.setBackgroundColor(getResources().getColor(R.color.lightSkyBlue));
                currentCategory.setSelected(true);
                selectionBean.addOneToNumberItemSelected();
                if (selectionBean.getNumberItemSelected() == 1) {
                    selectionBean.getmMenu().findItem(R.id.delete).setVisible(true);
                }
            } else {
                v.setBackground(null);
                currentCategory.setSelected(false);
                selectionBean.substractOneToNumberItemSelected();
                if (selectionBean.getNumberItemSelected() == 0) {
                    selectionBean.getmMenu().findItem(R.id.delete).setVisible(false);
                }
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        deleteFoodsFirst();
        deleteCategories();
        refreshCategories();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check which request we're responding to
//        if (requestCode == REQUEST_EDIT_CODE) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_CODE_EDITED) {
//                final long modifiedId = data.getExtras().getLong(MODIFIED_ID_RESULT);
//                final CategoryFood modifiedCategory = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(
//                        getActivity().getApplicationContext()).loadCategoryOfFood(modifiedId);
//                updateCategoryList(modifiedCategory);
//            }
//        } else if (requestCode == REQUEST_CREATE_CODE) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_CODE_CREATED) {
//                final long createdId = data.getExtras().getLong(CREATED_ID_RESULT);
//                final CategoryFood createdCategory = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(
//                        getActivity().getApplicationContext()).loadCategoryOfFood(createdId);
//                updateCategoryList(createdCategory);
//            }
//        }
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        log("CategoryFoodListFragment.onActivityCreated");
        super.onActivityCreated(savedInstanceState);
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
     * @param aCategory - the category to be added to the list
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
        // show dialog confirmation if needed
        boolean isSomeFoodLinked = false;
        selectionBean.resetIdsToDelete();
        for (final CategoryFood category : categories) {
            if (category.isSelected()) {
                if (!isSomeFoodLinked
                        && GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).existFoodFromCategory(
                        category.getId())) {
                    isSomeFoodLinked = true;
                }
                selectionBean.addIdToDelete(category.getId());
            }
        }
        if (isSomeFoodLinked) {
            // show dialog
            final DialogWarningDeleteCascade dialog = DialogWarningDeleteCascade.newInstance(this);
            dialog.show(getFragmentManager(), "warningDeleteCascadeCategoriesDialog");
        } else {
            deleteCategories();
            refreshCategories();
        }
    }

    private void deleteFoodsFirst() {
        // This click means that the user confirmed that he wants to delete the
        // related food
        for (final Long categoryId : selectionBean.getIdsToDelete()) {
            GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).deleteFoodsFromSameCategory(categoryId);
        }
    }

    private void deleteCategories() {
        for (final Long categoryId : selectionBean.getIdsToDelete()) {
            GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext()).deleteCategory(categoryId);
        }
    }

    private void refreshCategories() {
        // Reload the categories from the Database
        categories.clear();
        categories.addAll(GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity().getApplicationContext())
                .loadCategoriesOfFood());
        categoryFoodAdapter.notifyDataSetChanged();
    }

    public static class DialogWarningDeleteCascade extends DialogFragment {

        private OnClickListener positiveOnClickListener;

        public static DialogWarningDeleteCascade newInstance(OnClickListener positiveOnClickListener) {
            final DialogWarningDeleteCascade frag = new DialogWarningDeleteCascade();
            frag.positiveOnClickListener = positiveOnClickListener;
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return DialogHelper.getDialogWarningDeleteCategoryWithFood(getActivity(), positiveOnClickListener);
        }

    }

}
