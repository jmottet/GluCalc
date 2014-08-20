package ch.glucalc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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

    return rootView;
  }

  private void generateFoods() {

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
      foods.add(food);
    }
    Log.i(TAG, "" + size + " foods generated");

    GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).store(foods);

    Log.i(TAG, "" + size + " foods stored");
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
    GluCalcSQLiteHelper.getGluCalcSQLiteHelper(context).deleteCategoriesOfFood();
  }

}
