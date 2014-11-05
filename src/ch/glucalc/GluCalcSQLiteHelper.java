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
import ch.glucalc.meal.type.MealType;
import ch.glucalc.meal.type.MealTypeTable;

public class GluCalcSQLiteHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "glucalc.db";
  private static final int DATABASE_VERSION = 6;

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
    db.execSQL(MealTypeTable.TABLE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL(FoodTable.TABLE_DROP);
    db.execSQL(CategoryFoodTable.TABLE_DROP);

    if (DATABASE_VERSION > 6) {
      db.execSQL(MealTypeTable.TABLE_DROP);
    }

    onCreate(db);
  }

  @Override
  public void onConfigure(SQLiteDatabase db) {
    db.setForeignKeyConstraintsEnabled(true);
  }

  /****** MEAL TYPE SECTION ******/

  public void deleteMealType(Long mealTypeId) {
    getWritableDatabase().delete(MealTypeTable.TABLE_NAME, MealTypeTable.COLUMN_ID + " = ?",
        new String[] { String.valueOf(mealTypeId) });
  }

  public void deleteMealTypes() {
    getWritableDatabase().delete(MealTypeTable.TABLE_NAME, null, null);
  }

  public List<MealType> loadMealTypes() {
    final Cursor cursor = getReadableDatabase().query(MealTypeTable.TABLE_NAME, MealTypeTable.COLUMNS, null, null,
        null, null, MealTypeTable.COLUMN_NAME);
    final List<MealType> mealTypes = new ArrayList<MealType>(cursor.getCount());
    while (cursor.moveToNext()) {
      final MealType mealType = new MealType();
      mealType.setId(cursor.getLong(0));
      mealType.setName(cursor.getString(1));
      mealType.setFoodTarget(cursor.getFloat(2));
      mealType.setGlycemiaTarget(cursor.getFloat(3));
      mealType.setInsulinSensitivity(cursor.getFloat(4));
      mealType.setInsulin(cursor.getFloat(5));
      mealTypes.add(mealType);
    }
    return mealTypes;
  }

  public MealType loadMealType(long mealTypeId) {
    MealType result = null;

    final String whereClause = MealTypeTable.COLUMN_ID + "=?";
    final String[] whereArgs = { String.valueOf(mealTypeId) };

    final Cursor cursor = getReadableDatabase().query(MealTypeTable.TABLE_NAME, MealTypeTable.COLUMNS, whereClause,
        whereArgs, null, null, null);
    if (cursor.moveToNext()) {
      result = new MealType();
      result.setId(cursor.getLong(0));
      result.setName(cursor.getString(1));
      result.setFoodTarget(cursor.getFloat(2));
      result.setGlycemiaTarget(cursor.getFloat(3));
      result.setInsulinSensitivity(cursor.getFloat(4));
      result.setInsulin(cursor.getFloat(5));
    }
    return result;
  }

  public void storeMealTypes(List<MealType> mealTypes) {

    final SQLiteDatabase db = getWritableDatabase();

    final ContentValues values = new ContentValues();
    try {
      db.beginTransaction();

      for (final MealType mealType : mealTypes) {
        values.put(MealTypeTable.COLUMN_NAME, mealType.getName());
        values.put(MealTypeTable.COLUMN_FOOD_TARGET, mealType.getFoodTarget());
        values.put(MealTypeTable.COLUMN_GLYCEMIA_TARGET, mealType.getGlycemiaTarget());
        values.put(MealTypeTable.COLUMN_INSULIN_SENSITIVITY, mealType.getInsulinSensitivity());
        values.put(MealTypeTable.COLUMN_INSULIN, mealType.getInsulin());
        final long id = db.insert(FoodTable.TABLE_NAME, "", values);
        mealType.setId(id);
        values.clear();
      }
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }
  }

  public long storeMealType(MealType mealType) {
    long id = -1;
    final SQLiteDatabase db = getWritableDatabase();

    final ContentValues values = new ContentValues();
    try {
      db.beginTransaction();

      values.put(MealTypeTable.COLUMN_NAME, mealType.getName());
      values.put(MealTypeTable.COLUMN_FOOD_TARGET, mealType.getFoodTarget());
      values.put(MealTypeTable.COLUMN_GLYCEMIA_TARGET, mealType.getGlycemiaTarget());
      values.put(MealTypeTable.COLUMN_INSULIN_SENSITIVITY, mealType.getInsulinSensitivity());
      values.put(MealTypeTable.COLUMN_INSULIN, mealType.getInsulin());
      id = db.insert(MealTypeTable.TABLE_NAME, "", values);
      mealType.setId(id);
      values.clear();

      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }
    return id;
  }

  public void updateMealType(MealType mealType) {

    final SQLiteDatabase db = getWritableDatabase();
    db.setForeignKeyConstraintsEnabled(true);

    final ContentValues values = new ContentValues();
    try {
      db.beginTransaction();

      values.put(MealTypeTable.COLUMN_NAME, mealType.getName());
      values.put(MealTypeTable.COLUMN_FOOD_TARGET, mealType.getFoodTarget());
      values.put(MealTypeTable.COLUMN_GLYCEMIA_TARGET, mealType.getGlycemiaTarget());
      values.put(MealTypeTable.COLUMN_INSULIN_SENSITIVITY, mealType.getInsulinSensitivity());
      values.put(MealTypeTable.COLUMN_INSULIN, mealType.getInsulin());
      db.update(MealTypeTable.TABLE_NAME, values, MealTypeTable.COLUMN_ID + " = ?",
          new String[] { String.valueOf(mealType.getId()) });

      values.clear();
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }
  }

  /****** FOOD SECTION ******/

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

  public List<Food> loadFoodsFilteredByName(String name) {
    final Cursor cursor = getReadableDatabase().query(FoodTable.TABLE_NAME, FoodTable.COLUMNS,
        FoodTable.COLUMN_NAME + " LIKE ?", new String[] { "%" + name + "%" }, null, null, null);
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

  public boolean existFoodFromCategory(Long categoryId) {
    final Cursor cursor = getReadableDatabase().query(FoodTable.TABLE_NAME, FoodTable.COLUMNS,
        FoodTable.COLUMN_FK_CATEGORY + " = ?", new String[] { String.valueOf(categoryId) }, null, null, null);
    return cursor.getCount() > 0;
  }

  public void deleteFoods() {
    getWritableDatabase().delete(FoodTable.TABLE_NAME, null, null);
  }

  public void deleteFoodsFromSameCategory(Long withCategoryId) {
    getWritableDatabase().delete(FoodTable.TABLE_NAME, FoodTable.COLUMN_FK_CATEGORY + " = ?",
        new String[] { String.valueOf(withCategoryId) });
  }

  public void deleteFood(Long foodId) {
    getWritableDatabase().delete(FoodTable.TABLE_NAME, FoodTable.COLUMN_ID + " = ?",
        new String[] { String.valueOf(foodId) });
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

  /****** CATEGORIES OF FOOD SECTION ******/

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

  public boolean isCategoryOfFoodEmpty() {
    final Cursor cursor = getReadableDatabase().query(CategoryFoodTable.TABLE_NAME, CategoryFoodTable.COLUMNS, null,
        null, null, null, CategoryFoodTable.COLUMN_NAME);
    return cursor.getCount() == 0;
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

  public void deleteCategory(Long categoryId) {
    getWritableDatabase().delete(CategoryFoodTable.TABLE_NAME, CategoryFoodTable.COLUMN_ID + " = ?",
        new String[] { String.valueOf(categoryId) });
  }
}
