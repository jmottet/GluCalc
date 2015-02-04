package ch.glucalc.food.category;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ch.glucalc.DialogHelper;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;

import static ch.glucalc.food.category.CategoryFoodConstants.CATEGORY_ID_PARAMETER;
import static ch.glucalc.food.category.CategoryFoodConstants.CATEGORY_NAME_PARAMETER;
import static ch.glucalc.food.category.CategoryFoodConstants.FAKE_DEFAULT_ID;

public class EditCategoryFoodFragment extends Fragment {

    private static String TAG = "GluCalc";

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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        View layout = inflater.inflate(R.layout.edit_category_food, container, false);
        final EditText newCategory = (EditText) layout.findViewById(R.id.category_edittext);
        final Button saveButton = (Button) layout.findViewById(R.id.category_save_button);

        if (getCategoryId() != FAKE_DEFAULT_ID) {
            // Initaliser les champs avec les valeurs
            final String categoryName = getCategoryName();
            newCategory.setText(categoryName);
        }

        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                log("EditCategoryFoodFragment.onClick : START");
                final String newCategoryFoodName = newCategory.getText().toString();
                if (TextUtils.isEmpty(newCategoryFoodName)) {
                    DialogHelper.displayErrorMessageAllFieldsMissing(getActivity());
                } else {
                    saveNewCategory(newCategoryFoodName);
                    log("EditCategoryFoodFragment.onClick : DONE");
                    mCallback.openCategoryFoodListFragment();
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

        });

        return layout;
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
