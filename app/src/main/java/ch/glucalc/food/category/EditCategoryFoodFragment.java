package ch.glucalc.food.category;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ch.glucalc.DialogHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;
import ch.glucalc.food.Food;

import static ch.glucalc.food.category.CategoryFoodConstants.CATEGORY_ID_PARAMETER;
import static ch.glucalc.food.category.CategoryFoodConstants.CATEGORY_NAME_PARAMETER;
import static ch.glucalc.food.category.CategoryFoodConstants.FAKE_DEFAULT_ID;

public class EditCategoryFoodFragment extends Fragment {

    private static String TAG = "GluCalc";

    private EditText newCategory;

    private OnCategoryFoodSaved mCallback;

    // Container Activity must implement this interface
    public interface OnCategoryFoodSaved {

        public void openCategoryFoodListFragment();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnCategoryFoodSaved) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCategoryFoodSaved");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        log("FoodListFragment.onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.accept_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        View layout = inflater.inflate(R.layout.edit_category_food, container, false);
        newCategory = (EditText) layout.findViewById(R.id.category_edittext);

        if (getCategoryId() != FAKE_DEFAULT_ID) {
            // Initaliser les champs avec les valeurs
            final String categoryName = getCategoryName();
            newCategory.setText(categoryName);
        }

        return layout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        log("EditCategoryFoodFragment.onOptionsItemSelected : START");

        log("EditFoodFragment.onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.save:
                final String newCategoryFoodName = newCategory.getText().toString();
                if (TextUtils.isEmpty(newCategoryFoodName)) {
                    DialogHelper.displayErrorMessageAllFieldsMissing(getActivity());
                } else {
                    saveNewCategory(newCategoryFoodName);
                    log("EditCategoryFoodFragment.onClick : DONE");
                    mCallback.openCategoryFoodListFragment();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNewCategory(final String newCategoryFoodName) {
        if (getCategoryId() != FAKE_DEFAULT_ID) {
            final CategoryFood categoryFoodToUpdate = new CategoryFood();
            categoryFoodToUpdate.setId(getCategoryId());
            categoryFoodToUpdate.setName(newCategoryFoodName);
            GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity())
                    .updateCategory(categoryFoodToUpdate);
        } else {
            final CategoryFood categoryFoodToCreate = new CategoryFood();
            categoryFoodToCreate.setName(newCategoryFoodName);
            final long id = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(getActivity()).storeCategory(
                    categoryFoodToCreate);
        }
    }

    private long getCategoryId() {
        return getArguments().getLong(CATEGORY_ID_PARAMETER, FAKE_DEFAULT_ID);
    }

    private String getCategoryName() {
        return getArguments().getString(CATEGORY_NAME_PARAMETER);
    }

    private void log(String msg) {
        Log.i(TAG, msg);
    }
}
