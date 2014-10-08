package ch.glucalc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import ch.glucalc.food.Food;
import ch.glucalc.food.category.CategoryFood;

public class ImportActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final Uri data = getIntent().getData();
    if (data != null) {
      getIntent().setData(null);
      try {
        final int importedSize = importFoodToCsvFile(data);
        final Object[] messageArgs = { importedSize };

        displayNumberOfImportedFood(messageArgs);
      } catch (final Exception e) {
        // warn user about bad data here
        finish();
        return;
      }
    }

    final Intent startIntent = new Intent(this, MainActivity.class);
    startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(startIntent);
  }

  private void displayNumberOfImportedFood(final Object[] messageArgs) {
    String message = getString(R.string.imported_food);
    final MessageFormat form = new MessageFormat(message);
    message = form.format(messageArgs);
    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  }

  private int importFoodToCsvFile(Uri data) throws IOException {
    int result = 0;
    final Context context = getApplicationContext();

    final Map<String, CategoryFood> categories = new HashMap<String, CategoryFood>();
    final List<Food> foods = new ArrayList<Food>();

    if (data != null) {

      GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).deleteFoods();
      GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).deleteCategoriesOfFood();

      final ContentResolver cr = context.getContentResolver();
      final InputStream is = cr.openInputStream(data);

      final BufferedReader inputReader = new BufferedReader(new InputStreamReader(is));
      String buffer;
      if ((buffer = inputReader.readLine()) != null) {
        // Ignore the first line
      }

      while ((buffer = inputReader.readLine()) != null) {
        final StringTokenizer tokenizer = new StringTokenizer(buffer, ";");
        final String categoryName = tokenizer.nextToken();
        final String foodName = tokenizer.nextToken();
        final String foodCarbohydrate = tokenizer.nextToken();
        final String foodQuantity = tokenizer.nextToken();
        final String foodUnit = tokenizer.nextToken();

        CategoryFood categoryFood = categories.get(categoryName.toLowerCase());
        if (categoryFood == null) {
          categoryFood = new CategoryFood();
          categoryFood.setName(categoryName);
          final long categoryId = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).storeCategory(categoryFood);
          categoryFood = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).loadCategoryOfFood(categoryId);
          categories.put(categoryFood.getName().toLowerCase(), categoryFood);
        }

        final Food newFood = new Food();
        newFood.setCategoryId(categoryFood.getId());
        newFood.setName(foodName);
        newFood.setCarbonhydrate(Float.valueOf(foodCarbohydrate));
        newFood.setQuantity(Float.valueOf(foodQuantity));
        newFood.setUnit(foodUnit);

        foods.add(newFood);

      }
      GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).store(foods);
      result = foods.size();
      is.close();
    }
    return result;
  }

}
