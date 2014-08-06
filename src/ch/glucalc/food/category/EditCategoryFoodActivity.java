package ch.glucalc.food.category;

import android.app.Activity;
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
    final Button loginButton = (Button) findViewById(R.id.category_save_button);

    final long categoryId = getIntent().getLongExtra("categoryId", -1L);
    final String categoryName = getIntent().getStringExtra("categoryName");
    log("EditCategoryFoodActivity.onCreate - editing category : " + categoryId + " -> " + categoryName);
    newCategory.setText(categoryName);
    loginButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        log("EditCategoryFoodActivity.onClickSave : START");
        final CategoryFood categoryFood = new CategoryFood();
        categoryFood.setId(categoryId);
        categoryFood.setName(newCategory.getText().toString());
        GluCalcSQLiteHelper.getGluCalcSQLiteHelper(EditCategoryFoodActivity.this).updateCategory(categoryFood);
        log("EditCategoryFoodActivity.onClickSave : DONE");
        finish();
      }
    });
  }

  private void log(String msg) {
    Log.i(TAG, msg);
  }
}
