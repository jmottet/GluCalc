package ch.glucalc;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ch.glucalc.food.Food;
import ch.glucalc.food.FoodTable;
import ch.glucalc.food.category.CategoryFood;
import ch.glucalc.food.category.CategoryFoodTable;

public class GluCalcSQLiteHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "glucalc.db";
  private static final int DATABASE_VERSION = 4;

  private static GluCalcSQLiteHelper singleInstance;

  private GluCalcSQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public static synchronized GluCalcSQLiteHelper getGluCalcSQLiteHelper(Context context) {
    if (singleInstance == null) {
      singleInstance = new GluCalcSQLiteHelper(context);
    }
    return singleInstance;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(FoodTable.TABLE_CREATE);
    db.execSQL(CategoryFoodTable.TABLE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL(FoodTable.TABLE_DROP);
    db.execSQL(CategoryFoodTable.TABLE_DROP);
    onCreate(db);
  }

  @Override
  public void onConfigure(SQLiteDatabase db) {
    db.setForeignKeyConstraintsEnabled(true);
  }

  public void store(List<Food> foods) {

    final SQLiteDatabase db = getWritableDatabase();

    final ContentValues values = new ContentValues();
    try {
      db.beginTransaction();

      for (final Food food : foods) {
        values.put(FoodTable.COLUMN_NAME, food.getName());
        values.put(FoodTable.COLUMN_QUANTITY, food.getQuantity());
        values.put(FoodTable.COLUMN_UNIT, food.getUnit());
        values.put(FoodTable.COLUMN_CARBONHYDRATE, food.getCarbonhydrate());
        values.put(FoodTable.COLUMN_FK_CATEGORY, food.getCategoryId());
        final long id = db.insert(FoodTable.TABLE_NAME, "", values);
        food.setId(id);
        values.clear();
      }
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }
  }

  public long store(Food food) {
    long id = -1;
    final SQLiteDatabase db = getWritableDatabase();

    final ContentValues values = new ContentValues();
    try {
      db.beginTransaction();

      values.put(FoodTable.COLUMN_NAME, food.getName());
      values.put(FoodTable.COLUMN_QUANTITY, food.getQuantity());
      values.put(FoodTable.COLUMN_UNIT, food.getUnit());
      values.put(FoodTable.COLUMN_CARBONHYDRATE, food.getCarbonhydrate());
      values.put(FoodTable.COLUMN_FK_CATEGORY, food.getCategoryId());
      id = db.insert(FoodTable.TABLE_NAME, "", values);
      food.setId(id);
      values.clear();

      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }
    return id;
  }

  public List<Food> loadFoods() {
    final Cursor cursor = getReadableDatabase().query(FoodTable.TABLE_NAME, FoodTable.COLUMNS, null, null, null, null,
        null);
    final List<Food> foods = new ArrayList<Food>(cursor.getCount());
    while (cursor.moveToNext()) {
      final Food food = new Food();
      food.setId(cursor.getLong(0));
      food.setName(cursor.getString(1));
      food.setQuantity(cursor.getFloat(2));
      food.setUnit(cursor.getString(3));
      food.setCarbonhydrate(cursor.getFloat(4));
      food.setCategoryId(cursor.getLong(5));
      foods.add(food);
    }
    return foods;
  }

  public void deleteFoods() {
    getWritableDatabase().delete(FoodTable.TABLE_NAME, null, null);
  }

  public void updateFood(Food food) {

    final SQLiteDatabase db = getWritableDatabase();
    db.setForeignKeyConstraintsEnabled(true);

    final ContentValues values = new ContentValues();
    try {
      db.beginTransaction();

      values.put(FoodTable.COLUMN_NAME, food.getName());
      values.put(FoodTable.COLUMN_CARBONHYDRATE, food.getCarbonhydrate());
      values.put(FoodTable.COLUMN_QUANTITY, food.getQuantity());
      values.put(FoodTable.COLUMN_UNIT, food.getUnit());
      values.put(FoodTable.COLUMN_FK_CATEGORY, food.getCategoryId());
      db.update(FoodTable.TABLE_NAME, values, FoodTable.COLUMN_ID + " = ?",
          new String[] { String.valueOf(food.getId()) });

      values.clear();
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }
  }

  public Food loadFood(long foodId) {
    Food result = null;

    final String whereClause = FoodTable.COLUMN_ID + "=?";
    final String[] whereArgs = { String.valueOf(foodId) };

    final Cursor cursor = getReadableDatabase().query(FoodTable.TABLE_NAME, FoodTable.COLUMNS, whereClause, whereArgs,
        null, null, null);
    if (cursor.moveToNext()) {
      result = new Food();
      result.setId(cursor.getLong(0));
      result.setName(cursor.getString(1));
      result.setQuantity(cursor.getFloat(2));
      result.setUnit(cursor.getString(3));
      result.setCarbonhydrate(cursor.getFloat(4));
      result.setCategoryId(cursor.getLong(5));
    }
    return result;
  }

  /* Section : Categories of Food */

  public void storeCategories(List<CategoryFood> categories) {

    final SQLiteDatabase db = getWritableDatabase();

    final ContentValues values = new ContentValues();
    try {
      db.beginTransaction();

      for (final CategoryFood categoryFood : categories) {
        values.put(CategoryFoodTable.COLUMN_NAME, categoryFood.getName());

        final long id = db.insert(CategoryFoodTable.TABLE_NAME, "", values);
        categoryFood.setId(id);
        values.clear();
      }
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }
  }

  public long storeCategory(CategoryFood categoryFood) {
    long id = -1;
    final SQLiteDatabase db = getWritableDatabase();
    final ContentValues values = new ContentValues();
    try {
      db.beginTransaction();

      values.put(CategoryFoodTable.COLUMN_NAME, categoryFood.getName());
      id = db.insert(CategoryFoodTable.TABLE_NAME, "", values);
      categoryFood.setId(id);
      values.clear();

      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }
    return id;
  }

  public void updateCategory(CategoryFood categoryFood) {

    final SQLiteDatabase db = getWritableDatabase();

    final ContentValues values = new ContentValues();
    try {
      db.beginTransaction();

      values.put(CategoryFoodTable.COLUMN_NAME, categoryFood.getName());
      db.update(CategoryFoodTable.TABLE_NAME, values, CategoryFoodTable.COLUMN_ID + " = ?",
          new String[] { String.valueOf(categoryFood.getId()) });

      values.clear();
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }
  }

  public List<CategoryFood> loadCategoriesOfFood() {
    final Cursor cursor = getReadableDatabase().query(CategoryFoodTable.TABLE_NAME, CategoryFoodTable.COLUMNS, null,
        null, null, null, CategoryFoodTable.COLUMN_NAME);
    final List<CategoryFood> categories = new ArrayList<CategoryFood>(cursor.getCount());
    while (cursor.moveToNext()) {
      final CategoryFood categoryFood = new CategoryFood();
      categoryFood.setId(cursor.getLong(0));
      categoryFood.setName(cursor.getString(1));
      categories.add(categoryFood);
    }
    return categories;
  }

  public CategoryFood loadCategoryOfFood(long categoryFoodId) {
    CategoryFood result = null;

    final String whereClause = CategoryFoodTable.COLUMN_ID + "=?";
    final String[] whereArgs = { String.valueOf(categoryFoodId) };

    final Cursor cursor = getReadableDatabase().query(CategoryFoodTable.TABLE_NAME, CategoryFoodTable.COLUMNS,
        whereClause, whereArgs, null, null, null);
    if (cursor.moveToNext()) {
      result = new CategoryFood();
      result.setId(cursor.getLong(0));
      result.setName(cursor.getString(1));
    }
    return result;
  }

  public void deleteCategoriesOfFood() {
    getWritableDatabase().delete(CategoryFoodTable.TABLE_NAME, null, null);
  }
}
