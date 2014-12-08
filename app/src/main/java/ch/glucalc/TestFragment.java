package ch.glucalc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import ch.glucalc.food.Food;
import ch.glucalc.food.category.CategoryFood;

public class TestFragment extends Fragment {

  private static String TAG = "GluCalc";

  private static int NUMBER_OF_ITEMS_TO_GENERATE = 1000;

  private Context context;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    context = container.getContext();

    final View rootView = inflater.inflate(R.layout.fragment_test, container, false);

    final Button generateFoodsBtn = (Button) rootView.findViewById(R.id.generate_foods);
    generateFoodsBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        generateFoods();
      }
    });

    final Button deleteFoodsBtn = (Button) rootView.findViewById(R.id.delete_foods);
    deleteFoodsBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        deleteFoods();
      }
    });

    final Button generateFoodCategoriesBtn = (Button) rootView.findViewById(R.id.generate_categories_food);
    generateFoodCategoriesBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        generateCategoriesOfFood();
      }
    });

    final Button deleteFoodCategoriesBtn = (Button) rootView.findViewById(R.id.delete_categories_food);
    deleteFoodCategoriesBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        deleteCategoriesOfFood();
      }
    });

    final Button exportFoodBtn = (Button) rootView.findViewById(R.id.export_food);
    exportFoodBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        startExportActivity();
      }
    });

    final Button importFoodBtn = (Button) rootView.findViewById(R.id.import_food);
    importFoodBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        try {
          importFoodToCsvFile();
        } catch (final IOException e) {
          // TODO: Display an error message
          e.printStackTrace();
        }
      }
    });

    return rootView;
  }

  private void startExportActivity() {
    final Intent startIntent = new Intent(getActivity(), ExportActivity.class);
    startActivity(startIntent);
  }

  @Deprecated
  private void importFoodToCsvFile() throws IOException {
    final Map<String, CategoryFood> categories = new HashMap<String, CategoryFood>();
    final List<Food> foods = new ArrayList<Food>();

    if (isExternalStorageReadable()) {

      GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).deleteFoods();
      GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).deleteCategoriesOfFood();

      final File importGlucalcFile = getExportGlucalcFile(context);
      final BufferedReader inputReader = new BufferedReader(new InputStreamReader(
          new FileInputStream(importGlucalcFile)));
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
      inputReader.close();
    }
  }

  /* Checks if external storage is available for read and write */
  @Deprecated
  private boolean isExternalStorageWritable() {
    final String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state)) {
      return true;
    }
    return false;
  }

  /* Checks if external storage is available to at least read */
  @Deprecated
  private boolean isExternalStorageReadable() {
    final String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
      return true;
    }
    return false;
  }

  @Deprecated
  private File getExportGlucalcFile(Context context) {
    // Get the directory for the app's private download directory.
    final File file = new File(context.getExternalFilesDir(null), "GluCalc_Foods.csv.glucalc");
    return file;
  }

  private void generateFoods() {
    final List<CategoryFood> categories = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).loadCategoriesOfFood();
    if (categories.isEmpty()) {
      displayErrorMessageGenerateFoodCategories();
    } else {
      final int size = NUMBER_OF_ITEMS_TO_GENERATE;

      Log.i(TAG, "Generating " + size + " food");

      final List<Food> foods = new ArrayList<Food>(size);
      final Random random = new Random();

      for (int i = 0; i < size; i++) {
        final Food food = new Food();
        food.setName(generateName(random));
        food.setQuantity((float) Math.round(random.nextFloat() * 100 * 1000) / 1000);
        food.setCarbonhydrate((float) Math.round(random.nextFloat() * 10 * 1000) / 1000);
        food.setUnit(generateUnit(random));
        final int index = random.nextInt(categories.size());
        food.setCategoryId(categories.get(index).getId());
        foods.add(food);
      }
      Log.i(TAG, "" + size + " foods generated");

      GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).store(foods);

      Log.i(TAG, "" + size + " foods stored");
    }
  }

  private String generateName(Random random) {
    final StringBuilder name = new StringBuilder();
    final int size = random.nextInt(5) + 2;
    for (int i = 0; i < size; i++) {
      name.append((char) (random.nextInt(26) + 'a'));
    }
    name.append(" blabla food ");
    return name.toString();
  }

  private String generateUnit(Random random) {
    switch (random.nextInt(5)) {
    case 0:
      return "g";
    case 1:
      return "kg";
    case 2:
      return "l";
    case 3:
      return "ml";
    case 4:
      return "pce";
    default:
      return "xxx";
    }

  }

  private void deleteFoods() {
    GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).deleteFoods();
  }

  private void generateCategoriesOfFood() {

    final int size = NUMBER_OF_ITEMS_TO_GENERATE;

    Log.i(TAG, "Generating " + size + " categories of food");

    final List<CategoryFood> categories = new ArrayList<CategoryFood>(size);
    final Random random = new Random();

    for (int i = 0; i < size; i++) {
      final CategoryFood categoryFood = new CategoryFood();
      categoryFood.setName("Category " + Math.abs(random.nextInt()));
      categories.add(categoryFood);
    }
    Log.i(TAG, "" + size + " categories of food generated");

    GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).storeCategories(categories);

    Log.i(TAG, "" + size + " categories of food stored");
  }

  private void deleteCategoriesOfFood() {
    final List<Food> foods = GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).loadFoods();
    if (foods.isEmpty()) {
      GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).deleteCategoriesOfFood();
    } else {
      displayErrorMessageDeleteCategories();
    }
  }

  private void displayErrorMessageGenerateFoodCategories() {
    final Builder builder = new AlertDialog.Builder(getActivity());

    // Setting Dialog Title
    builder.setTitle("Error");

    // Setting Dialog Message
    builder.setMessage("Please generate the categories of food first!");

    // Setting OK Button
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {

      }
    });
    final AlertDialog dialog = builder.create();
    dialog.show();
  }

  private void displayErrorMessageDeleteCategories() {
    final Builder builder = new AlertDialog.Builder(getActivity());

    // Setting Dialog Title
    builder.setTitle("Error");

    // Setting Dialog Message
    builder.setMessage("Please delete the foods first!");

    // Setting OK Button
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {

      }
    });
    final AlertDialog dialog = builder.create();
    dialog.show();
  }

}
