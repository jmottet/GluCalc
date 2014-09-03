package ch.glucalc.food.category;

import static ch.glucalc.food.category.CategoryFoodConstants.CATEGORY_ID_PARAMETER;
import static ch.glucalc.food.category.CategoryFoodConstants.CATEGORY_NAME_PARAMETER;
import static ch.glucalc.food.category.CategoryFoodConstants.CREATED_ID_RESULT;
import static ch.glucalc.food.category.CategoryFoodConstants.FAKE_DEFAULT_ID;
import static ch.glucalc.food.category.CategoryFoodConstants.MODIFIED_ID_RESULT;
import static ch.glucalc.food.category.CategoryFoodConstants.RESULT_CODE_CREATED;
import static ch.glucalc.food.category.CategoryFoodConstants.RESULT_CODE_EDITED;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import ch.glucalc.GluCalcSQLiteHelper;
import ch.glucalc.R;

public class EditCategoryFoodActivity extends Activity {

  private static String TAG = "GluCalc";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.edit_category_food);

    final EditText newCategory = (EditText) findViewById(R.id.category_edittext);
    final Button saveButton = (Button) findViewById(R.id.category_save_button);

    if (getCategoryId() != FAKE_DEFAULT_ID) {
      // Initaliser les champs avec les valeurs
      final String categoryName = getCategoryName();
      newCategory.setText(categoryName);
    }

    saveButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        log("EditCategoryFoodActivity.onClick : START");
        final String newCategoryFoodName = newCategory.getText().toString();
        saveNewCategory(newCategoryFoodName);
        log("EditCategoryFoodActivity.onClick : DONE");
        finish();
      }

      private void saveNewCategory(final String newCategoryFoodName) {
        if (getCategoryId() != FAKE_DEFAULT_ID) {
          final CategoryFood categoryFoodToUpdate = new CategoryFood();
          categoryFoodToUpdate.setId(getCategoryId());
          categoryFoodToUpdate.setName(newCategoryFoodName);
          GluCalcSQLiteHelper.getGluCalcSQLiteHelper(EditCategoryFoodActivity.this)
              .updateCategory(categoryFoodToUpdate);
          propagateModifiedResult();
        } else {
          final CategoryFood categoryFoodToCreate = new CategoryFood();
          categoryFoodToCreate.setName(newCategoryFoodName);
          final long id = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(EditCategoryFoodActivity.this).storeCategory(
              categoryFoodToCreate);
          propagateCreatedResult(id);
        }
      }

      private void propagateCreatedResult(long newCategoryId) {
        final Intent intent = new Intent();
        intent.putExtra(CREATED_ID_RESULT, newCategoryId);
        setResult(RESULT_CODE_CREATED, intent);
      }

      private void propagateModifiedResult() {
        final Intent intent = new Intent();
        intent.putExtra(MODIFIED_ID_RESULT, getCategoryId());
        setResult(RESULT_CODE_EDITED, intent);
      }
    });
  }

  private long getCategoryId() {
    return getIntent().getLongExtra(CATEGORY_ID_PARAMETER, FAKE_DEFAULT_ID);
  }

  private String getCategoryName() {
    return getIntent().getStringExtra(CATEGORY_NAME_PARAMETER);
  }

  private void log(String msg) {
    Log.i(TAG, msg);
  }
}
