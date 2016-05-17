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
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.widget.Toast;
import ch.glucalc.food.Food;
import ch.glucalc.food.category.CategoryFood;

public class ImportActivity extends Activity {

  private static final String COM_GOOGLE_ANDROID_EMAIL_ATTACHMENTPROVIDER = "com.google.android.email.attachmentprovider";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final Uri data = getIntent().getData();
    if (data != null) {
      getIntent().setData(null);

      final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImportActivity.this);

      // set title
      alertDialogBuilder.setTitle(R.string.dialog_confirm_title);

      // set dialog message
      alertDialogBuilder.setMessage(R.string.dialog_confirm_food_import).setCancelable(false)
          .setPositiveButton(R.string.dialog_button_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
              try {
                final int importedSize = importFoodToCsvFile(data);
                final Object[] messageArgs = { importedSize };
                displayNumberOfImportedFood(messageArgs);
                startMainActivity();
              } catch (final Exception e) {
                Toast.makeText(getApplicationContext(), R.string.imported_food_failed, Toast.LENGTH_LONG).show();
                e.printStackTrace();
                finish();
                return;
              }
            }
          }).setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
              ImportActivity.this.finish();
            }
          });

      // create alert dialog
      final AlertDialog alertDialog = alertDialogBuilder.create();
      // show it
      alertDialog.show();
    }
  }

  private void startMainActivity() {
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

    if (data != null) {

      GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).deleteFoods();
      GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).deleteCategoriesOfFood();

      final ContentResolver cr = context.getContentResolver();
      Uri uri = data;
      if (data.toString().contains(COM_GOOGLE_ANDROID_EMAIL_ATTACHMENTPROVIDER)) {
        uri = Uri.parse(getPath(data));
      }
      final InputStream is = cr.openInputStream(uri);

      result = ImportActivity.ImportDatas(context, is);
    }
    return result;
  }

  public static int ImportDatas(Context context, InputStream is) throws IOException {
    final Map<String, CategoryFood> categories = new HashMap<String, CategoryFood>();
    final List<Food> foods = new ArrayList<Food>();
    int result;
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
    return result;
  }

  private String getPath(Uri uri) {

    final String[] projection = { MediaColumns.DATA };
    final Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
    final int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
    cursor.moveToFirst();
    return cursor.getString(column_index);
  }

}
