package ch.glucalc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.glucalc.food.Food;
import ch.glucalc.food.FoodTable;
import ch.glucalc.food.category.CategoryFood;
import ch.glucalc.food.category.CategoryFoodTable;
import ch.glucalc.food.favourite.food.FavouriteFood;
import ch.glucalc.food.favourite.food.FavouriteFoodTable;
import ch.glucalc.meal.diary.FoodDiary;
import ch.glucalc.meal.diary.FoodDiaryTable;
import ch.glucalc.meal.diary.MealDiary;
import ch.glucalc.meal.diary.MealDiaryTable;
import ch.glucalc.meal.type.MealType;
import ch.glucalc.meal.type.MealTypeTable;

public class GluCalcSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "glucalc.db";
    private static final int DATABASE_VERSION = 11;

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
        db.execSQL(FavouriteFoodTable.TABLE_CREATE);
        db.execSQL(MealDiaryTable.TABLE_CREATE);
        db.execSQL(FoodDiaryTable.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (DATABASE_VERSION >= 10) {
            db.execSQL(FoodDiaryTable.TABLE_DROP);
            db.execSQL(MealDiaryTable.TABLE_DROP);
            db.execSQL(FavouriteFoodTable.TABLE_DROP);
            db.execSQL(FoodTable.TABLE_DROP);
            db.execSQL(CategoryFoodTable.TABLE_DROP);
            db.execSQL(MealTypeTable.TABLE_DROP);
            onCreate(db);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    /******
     * MEAL TYPE SECTION
     ******/

    public void deleteMealType(Long mealTypeId) {
        final SQLiteDatabase db = getWritableDatabase();
        db.setForeignKeyConstraintsEnabled(true);

        final ContentValues values = new ContentValues();
        try {
            db.beginTransaction();

            values.put(MealTypeTable.COLUMN_DELETED, 1);
            db.update(MealTypeTable.TABLE_NAME, values, MealTypeTable.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(mealTypeId)});

            values.clear();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteMealTypes() {
        getWritableDatabase().delete(MealTypeTable.TABLE_NAME, null, null);
    }

    public List<MealType> loadMealTypes() {
        Cursor cursor = null;
        List<MealType> mealTypes = null;
        try {
            final String whereClause = MealTypeTable.COLUMN_DELETED + "=0";
            cursor = getReadableDatabase().query(MealTypeTable.TABLE_NAME, MealTypeTable.COLUMNS, whereClause, null,
                    null, null, MealTypeTable.COLUMN_NAME);
            mealTypes = new ArrayList<MealType>(cursor.getCount());
            while (cursor.moveToNext()) {
                final MealType mealType = new MealType();
                mealType.setId(cursor.getLong(0));
                mealType.setName(cursor.getString(1));
                mealType.setFoodTarget(cursor.getFloat(2));
                mealType.setGlycemiaTarget(cursor.getFloat(3));
                mealType.setInsulinSensitivity(cursor.getFloat(4));
                mealType.setInsulin(cursor.getFloat(5));

                int deleted = cursor.getInt(6);

                if (deleted == 0) {
                    mealType.setDeleted(false);
                } else if (deleted == 1) {
                    mealType.setDeleted(true);
                }
                mealTypes.add(mealType);
                System.out.println("Meal Type Id : " + mealType.getId());
            }
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return mealTypes;
    }

    public MealType loadMealType(long mealTypeId) {
        MealType result = null;

        final String whereClause = MealTypeTable.COLUMN_ID + "=?";
        final String[] whereArgs = {String.valueOf(mealTypeId)};

        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(MealTypeTable.TABLE_NAME, MealTypeTable.COLUMNS, whereClause,
                    whereArgs, null, null, null);
            if (cursor.moveToNext()) {
                result = new MealType();
                result.setId(cursor.getLong(0));
                result.setName(cursor.getString(1));
                result.setFoodTarget(cursor.getFloat(2));
                result.setGlycemiaTarget(cursor.getFloat(3));
                result.setInsulinSensitivity(cursor.getFloat(4));
                result.setInsulin(cursor.getFloat(5));

                int deleted = cursor.getInt(6);

                if (deleted == 0) {
                    result.setDeleted(false);
                } else if (deleted == 1) {
                    result.setDeleted(true);
                }
            }
        } finally {
            if(cursor != null)
                cursor.close();

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
                    new String[]{String.valueOf(mealType.getId())});

            values.clear();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /******
     * FOOD SECTION
     ******/

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
        Cursor cursor = null;
        List<Food> foods = null;
        try {
            cursor = getReadableDatabase().query(FoodTable.TABLE_NAME, FoodTable.COLUMNS, null, null, null, null,
                    null);
            foods = new ArrayList<Food>(cursor.getCount());
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
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return foods;
    }

    public List<Food> loadFoodsFilteredByName(String name) {
        Cursor cursor = null;
        List<Food> foods = null;
        try {
            cursor = getReadableDatabase().query(FoodTable.TABLE_NAME, FoodTable.COLUMNS,
                    FoodTable.COLUMN_NAME + " LIKE ?", new String[]{"%" + name + "%"}, null, null, null);
            foods = new ArrayList<Food>(cursor.getCount());
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
        } finally {
            if(cursor != null)
                cursor.close();

        }
        return foods;
    }

    public List<Food> loadFoodsWhichAreNotFavourite(List<Long> foodsToExclude) {
        StringBuilder clauseIn = new StringBuilder();
        boolean first = true;
        for (Long foodToExclude : foodsToExclude) {
            if (!first) {
                clauseIn.append(",");
            } else {
                first = false;
            }
            clauseIn.append(foodToExclude);
        }

        Cursor cursor = null;
        List<Food> foods = null;
        try {
            cursor = getReadableDatabase().query(FoodTable.TABLE_NAME, FoodTable.COLUMNS,
                    FoodTable.COLUMN_ID + " NOT IN (" + clauseIn.toString() + ")", null, null, null, null);
            foods = new ArrayList<Food>(cursor.getCount());
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
        } finally {
            if(cursor != null)
                cursor.close();

        }

        return foods;
    }

    public boolean existFoodFromCategory(Long categoryId) {
        final Cursor cursor = getReadableDatabase().query(FoodTable.TABLE_NAME, FoodTable.COLUMNS,
                FoodTable.COLUMN_FK_CATEGORY + " = ?", new String[]{String.valueOf(categoryId)}, null, null, null);
        return cursor.getCount() > 0;
    }

    public void deleteFoods() {
        getWritableDatabase().delete(FoodTable.TABLE_NAME, null, null);
    }

    public void deleteFoodsFromSameCategory(Long withCategoryId) {
        getWritableDatabase().delete(FoodTable.TABLE_NAME, FoodTable.COLUMN_FK_CATEGORY + " = ?",
                new String[]{String.valueOf(withCategoryId)});
    }

    public void deleteFood(Long foodId) {
        getWritableDatabase().delete(FoodTable.TABLE_NAME, FoodTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(foodId)});
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
                    new String[]{String.valueOf(food.getId())});

            values.clear();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public Food loadFood(long foodId) {
        Food result = null;

        final String whereClause = FoodTable.COLUMN_ID + "=?";
        final String[] whereArgs = {String.valueOf(foodId)};

        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(FoodTable.TABLE_NAME, FoodTable.COLUMNS, whereClause, whereArgs,
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
        } finally {
            if(cursor != null)
                cursor.close();

        }

        return result;
    }

    public Food loadFoodByName(String foodName) {
        Food result = null;

        final String whereClause = FoodTable.COLUMN_NAME + "=?";
        final String[] whereArgs = {foodName};

        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(FoodTable.TABLE_NAME, FoodTable.COLUMNS, whereClause, whereArgs,
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
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return result;
    }

    /******
     * CATEGORIES OF FOOD SECTION
     ******/

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
                    new String[]{String.valueOf(categoryFood.getId())});

            values.clear();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public boolean isCategoryOfFoodEmpty() {
        Cursor cursor = null;
        boolean result = true;
        try {
            cursor = getReadableDatabase().query(CategoryFoodTable.TABLE_NAME, CategoryFoodTable.COLUMNS, null,
                    null, null, null, CategoryFoodTable.COLUMN_NAME);
            result = cursor.getCount() == 0;
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return result;
    }

    public List<CategoryFood> loadCategoriesOfFood() {
        Cursor cursor = null;
        List<CategoryFood> categories = null;
        try {

            cursor = getReadableDatabase().query(CategoryFoodTable.TABLE_NAME, CategoryFoodTable.COLUMNS, null,
                    null, null, null, CategoryFoodTable.COLUMN_NAME);
            categories = new ArrayList<CategoryFood>(cursor.getCount());
            while (cursor.moveToNext()) {
                final CategoryFood categoryFood = new CategoryFood();
                categoryFood.setId(cursor.getLong(0));
                categoryFood.setName(cursor.getString(1));
                categories.add(categoryFood);
            }
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return categories;
    }

    public CategoryFood loadCategoryOfFood(long categoryFoodId) {
        CategoryFood result = null;

        final String whereClause = CategoryFoodTable.COLUMN_ID + "=?";
        final String[] whereArgs = {String.valueOf(categoryFoodId)};

        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(CategoryFoodTable.TABLE_NAME, CategoryFoodTable.COLUMNS,
                    whereClause, whereArgs, null, null, null);
            if (cursor.moveToNext()) {
                result = new CategoryFood();
                result.setId(cursor.getLong(0));
                result.setName(cursor.getString(1));
            }
        } finally {
            if(cursor != null)
                cursor.close();

        }
        return result;
    }

    public void deleteCategoriesOfFood() {
        getWritableDatabase().delete(CategoryFoodTable.TABLE_NAME, null, null);
    }

    public void deleteCategory(Long categoryId) {
        getWritableDatabase().delete(CategoryFoodTable.TABLE_NAME, CategoryFoodTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(categoryId)});
    }


    /******
     * FAVOURITE FOOD SECTION
     ******/

// addFavouriteFood
// removeFavouriteFood
// List<Food> loadFavouriteFoods
    public void deleteFavouriteFoods() {
        getWritableDatabase().delete(FavouriteFoodTable.TABLE_NAME, null, null);
    }

    public long storeFavouriteFood(FavouriteFood favouriteFood) {
        long id = -1;
        final SQLiteDatabase db = getWritableDatabase();
        final ContentValues values = new ContentValues();
        try {
            db.beginTransaction();

            values.put(FavouriteFoodTable.COLUMN_FK_MEAL_TYPE, favouriteFood.getMealTypeId());
            values.put(FavouriteFoodTable.COLUMN_FK_FOOD, favouriteFood.getFoodId());
            values.put(FavouriteFoodTable.COLUMN_CARBONHYDRATE, favouriteFood.getQuantity());
            values.put(FavouriteFoodTable.COLUMN_QUANTITY, favouriteFood.getQuantity());
            id = db.insert(FavouriteFoodTable.TABLE_NAME, "", values);
            favouriteFood.setId(id);
            values.clear();

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    public void updateFavouriteFood(FavouriteFood favouriteFood) {

        final SQLiteDatabase db = getWritableDatabase();

        final ContentValues values = new ContentValues();
        try {
            db.beginTransaction();

            values.put(FavouriteFoodTable.COLUMN_FK_MEAL_TYPE, favouriteFood.getMealTypeId());
            values.put(FavouriteFoodTable.COLUMN_FK_FOOD, favouriteFood.getFoodId());
            values.put(FavouriteFoodTable.COLUMN_CARBONHYDRATE, favouriteFood.getCarbonhydrate());
            values.put(FavouriteFoodTable.COLUMN_QUANTITY, favouriteFood.getQuantity());
            db.update(FavouriteFoodTable.TABLE_NAME, values, FavouriteFoodTable.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(favouriteFood.getId())});
            values.clear();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteFavouriteFood(Long favouriteFoodId) {
        getWritableDatabase().delete(FavouriteFoodTable.TABLE_NAME, FavouriteFoodTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(favouriteFoodId)});
    }

    public List<FavouriteFood> loadFavouriteFoods(long mealTypeId) {
        final String whereClause = FavouriteFoodTable.COLUMN_FK_MEAL_TYPE + "=?";
        final String[] whereArgs = {String.valueOf(mealTypeId)};

        final Cursor cursor = getReadableDatabase().query(FavouriteFoodTable.TABLE_NAME, FavouriteFoodTable.COLUMNS,
                whereClause, whereArgs, null, null, null);

        final List<FavouriteFood> favouriteFoods = new ArrayList<FavouriteFood>(cursor.getCount());
        while (cursor.moveToNext()) {
            final FavouriteFood favouriteFood = new FavouriteFood();
            favouriteFood.setId(cursor.getLong(0));
            favouriteFood.setMealTypeId(cursor.getLong(1));
            favouriteFood.setFoodId(cursor.getLong(2));
            favouriteFood.setQuantity(cursor.getFloat(3));
            favouriteFood.setCarbonhydrate(cursor.getFloat(4));

            String foodName = loadFood(favouriteFood.getFoodId()).getName();
            favouriteFood.setName(foodName);
            favouriteFoods.add(favouriteFood);
        }
        sortFavouriteFoods(favouriteFoods);
        return favouriteFoods;
    }

    public FavouriteFood loadFavouriteFood(long favouriteFoodId) {
        FavouriteFood result = null;

        final String whereClause = FavouriteFoodTable.COLUMN_ID + "=?";
        final String[] whereArgs = {String.valueOf(favouriteFoodId)};
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(FavouriteFoodTable.TABLE_NAME, FavouriteFoodTable.COLUMNS,
                    whereClause, whereArgs, null, null, null);
            if (cursor.moveToNext()) {
                result = new FavouriteFood();
                result.setId(cursor.getLong(0));
                result.setMealTypeId(cursor.getLong(1));
                result.setFoodId(cursor.getLong(2));
                result.setQuantity(cursor.getFloat(3));
                result.setCarbonhydrate(cursor.getFloat(4));
            }
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return result;
    }

    public List<FavouriteFood> loadFavouriteFoods() {
        Cursor cursor = null;
        List<FavouriteFood> favouriteFoods = null;
        try {
            cursor = getReadableDatabase().query(FavouriteFoodTable.TABLE_NAME, FavouriteFoodTable.COLUMNS,
                    null, null, null, null, null);

            favouriteFoods = new ArrayList<FavouriteFood>(cursor.getCount());
            while (cursor.moveToNext()) {
                final FavouriteFood favouriteFood = new FavouriteFood();
                favouriteFood.setId(cursor.getLong(0));
                favouriteFood.setMealTypeId(cursor.getLong(1));
                favouriteFood.setFoodId(cursor.getLong(2));
                favouriteFood.setQuantity(cursor.getFloat(3));
                favouriteFood.setCarbonhydrate(cursor.getFloat(4));

                String foodName = loadFood(favouriteFood.getFoodId()).getName();
                favouriteFood.setName(foodName);
                favouriteFoods.add(favouriteFood);
            }
            sortFavouriteFoods(favouriteFoods);
        } finally {
            if(cursor != null)
                cursor.close();
        }

        return favouriteFoods;
    }

    private void sortFavouriteFoods(List<FavouriteFood> favouriteFoods) {
        Collections.sort(favouriteFoods, new Comparator<FavouriteFood>() {

            @Override
            public int compare(FavouriteFood lhs, FavouriteFood rhs) {
                return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
            }
        });
    }

    /******
     * MEAL DIARY SECTION
     ******/

    public long storeMealDiary(MealDiary mealDiary) {
        long id = -1;
        final SQLiteDatabase db = getWritableDatabase();
        final ContentValues values = new ContentValues();
        try {
            db.beginTransaction();

            values.put(MealDiaryTable.COLUMN_MEAL_DATE, mealDiary.getMealDate());
            values.put(MealDiaryTable.COLUMN_GLYCEMIA_MEASURED, mealDiary.getGlycemiaMeasured());
            values.put(MealDiaryTable.COLUMN_CARBOHYDRATE_TOTAL, mealDiary.getCarbohydrateTotal());
            values.put(MealDiaryTable.COLUMN_BOLUS_CALCULATED, mealDiary.getBolusCalculated());
            values.put(MealDiaryTable.COLUMN_BOLUS_GIVEN, mealDiary.getBolusGiven());
            int temporary = 0;
            if (mealDiary.getTemporary()) {
                temporary = 1;
            }
            values.put(MealDiaryTable.COLUMN_TEMPORARY, temporary);
            values.put(MealDiaryTable.COLUMN_FK_MEAL_TYPE, mealDiary.getMealTypeId());

            id = db.insert(MealDiaryTable.TABLE_NAME, "", values);
            mealDiary.setId(id);
            values.clear();

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    public void updateMealDiary(MealDiary mealDiary) {

        final SQLiteDatabase db = getWritableDatabase();

        final ContentValues values = new ContentValues();
        try {
            db.beginTransaction();

            values.put(MealDiaryTable.COLUMN_MEAL_DATE, mealDiary.getMealDate());
            values.put(MealDiaryTable.COLUMN_GLYCEMIA_MEASURED, mealDiary.getGlycemiaMeasured());
            values.put(MealDiaryTable.COLUMN_CARBOHYDRATE_TOTAL, mealDiary.getCarbohydrateTotal());
            values.put(MealDiaryTable.COLUMN_BOLUS_CALCULATED, mealDiary.getBolusCalculated());
            values.put(MealDiaryTable.COLUMN_BOLUS_GIVEN, mealDiary.getBolusGiven());
            int temporary = 0;
            if (mealDiary.getTemporary()) {
                temporary = 1;
            }
            values.put(MealDiaryTable.COLUMN_TEMPORARY, temporary);
            values.put(MealDiaryTable.COLUMN_FK_MEAL_TYPE, mealDiary.getMealTypeId());

            db.update(MealDiaryTable.TABLE_NAME, values, MealDiaryTable.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(mealDiary.getId())});
            values.clear();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public MealDiary loadMealDiaryTemporary() {
        MealDiary result = null;

        final String whereClause = MealDiaryTable.COLUMN_TEMPORARY + "=?";
        final String[] whereArgs = {"1"};

        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(MealDiaryTable.TABLE_NAME, MealDiaryTable.COLUMNS,
                    whereClause, whereArgs, null, null, null);

            if (cursor.moveToNext()) {
                result = new MealDiary();
                result.setId(cursor.getLong(0));
                result.setMealDate(cursor.getString(1));
                result.setGlycemiaMeasured(cursor.getFloat(2));
                result.setCarbohydrateTotal(cursor.getFloat(3));
                result.setBolusCalculated(cursor.getFloat(4));
                result.setBolusGiven(cursor.getFloat(5));
                int temporary = cursor.getInt(6);

                if (temporary == 0) {
                    result.setTemporary(false);
                } else if (temporary == 1) {
                    result.setTemporary(true);
                }
                result.setMealTypeId(cursor.getLong(7));
            }
        } finally {
            if(cursor != null)
                cursor.close();
        }

        return result;
    }

    public MealDiary loadMealDiary(long mealDiaryId) {
        MealDiary result = null;

        final String whereClause = MealDiaryTable.COLUMN_ID + "=?";
        final String[] whereArgs = { String.valueOf(mealDiaryId)};

        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(MealDiaryTable.TABLE_NAME, MealDiaryTable.COLUMNS,
                    whereClause, whereArgs, null, null, null);

            if (cursor.moveToNext()) {
                result = new MealDiary();
                result.setId(cursor.getLong(0));
                result.setMealDate(cursor.getString(1));
                result.setGlycemiaMeasured(cursor.getFloat(2));
                result.setCarbohydrateTotal(cursor.getFloat(3));
                result.setBolusCalculated(cursor.getFloat(4));
                result.setBolusGiven(cursor.getFloat(5));
                int temporary = cursor.getInt(6);

                if (temporary == 0) {
                    result.setTemporary(false);
                } else if (temporary == 1) {
                    result.setTemporary(true);
                }
                result.setMealTypeId(cursor.getLong(7));
            }
        } finally {
            if(cursor != null)
                cursor.close();
        }

        return result;
    }

    public List<MealDiary> loadMealDiariesStartingWith(String date) {

        final String whereClause = MealDiaryTable.COLUMN_TEMPORARY + "=? and " + MealDiaryTable.COLUMN_MEAL_DATE + " LIKE ?";
        final String[] whereArgs = { String.valueOf(0), date + "%"};
        List<MealDiary> mealDiaries = null;
        Cursor cursor = null;

        try {
            cursor = getReadableDatabase().query(MealDiaryTable.TABLE_NAME, MealDiaryTable.COLUMNS,
                    whereClause, whereArgs, null, null, MealDiaryTable.COLUMN_MEAL_DATE);

            mealDiaries = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                final MealDiary mealDiary = new MealDiary();
                mealDiary.setId(cursor.getLong(0));
                mealDiary.setMealDate(cursor.getString(1));
                mealDiary.setGlycemiaMeasured(cursor.getFloat(2));
                mealDiary.setCarbohydrateTotal(cursor.getFloat(3));
                mealDiary.setBolusCalculated(cursor.getFloat(4));
                mealDiary.setBolusGiven(cursor.getFloat(5));
                int temporary = cursor.getInt(6);

                if (temporary == 0) {
                    mealDiary.setTemporary(false);
                } else if (temporary == 1) {
                    mealDiary.setTemporary(true);
                }
                mealDiary.setMealTypeId(cursor.getLong(7));
                mealDiaries.add(mealDiary);
            }
            sortMealDiaries(mealDiaries);
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return mealDiaries;
    }

    public List<MealDiary> loadMealDiaries() {

        final String whereClause = MealDiaryTable.COLUMN_TEMPORARY + "=?";
        final String[] whereArgs = { String.valueOf(0)};

        Cursor cursor = null;
        List<MealDiary> mealDiaries = null;
        try {
            cursor = getReadableDatabase().query(MealDiaryTable.TABLE_NAME, MealDiaryTable.COLUMNS,
                    whereClause, whereArgs, null, null, null);

            mealDiaries = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                final MealDiary mealDiary = new MealDiary();
                mealDiary.setId(cursor.getLong(0));
                mealDiary.setMealDate(cursor.getString(1));
                mealDiary.setGlycemiaMeasured(cursor.getFloat(2));
                mealDiary.setCarbohydrateTotal(cursor.getFloat(3));
                mealDiary.setBolusCalculated(cursor.getFloat(4));
                mealDiary.setBolusGiven(cursor.getFloat(5));
                int temporary = cursor.getInt(6);

                if (temporary == 0) {
                    mealDiary.setTemporary(false);
                } else if (temporary == 1) {
                    mealDiary.setTemporary(true);
                }
                mealDiary.setMealTypeId(cursor.getLong(7));
                mealDiaries.add(mealDiary);
            }
            sortMealDiaries(mealDiaries);
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return mealDiaries;
    }

    private void sortMealDiaries(List<MealDiary> mealDiaries) {
        Collections.sort(mealDiaries, new Comparator<MealDiary>() {

            @Override
            public int compare(MealDiary lhs, MealDiary rhs) {
                return lhs.getMealDate().toLowerCase().compareTo(rhs.getMealDate().toLowerCase());
            }
        });
    }

    /******
     * FOOD DIARY SECTION
     ******/

    public long storeFoodDiary(FoodDiary foodDiary) {
        long id = -1;
        final SQLiteDatabase db = getWritableDatabase();
        final ContentValues values = new ContentValues();
        try {
            db.beginTransaction();

            values.put(FoodDiaryTable.COLUMN_FOOD_NAME, foodDiary.getFoodName());
            values.put(FoodDiaryTable.COLUMN_QUANTITY, foodDiary.getQuantity());
            values.put(FoodDiaryTable.COLUMN_UNIT, foodDiary.getUnit());
            values.put(FoodDiaryTable.COLUMN_CARBOHYDRATE, foodDiary.getCarbohydrate());
            values.put(FoodDiaryTable.COLUMN_FK_MEAL_DIARY, foodDiary.getMealDiaryId());

            id = db.insert(FoodDiaryTable.TABLE_NAME, "", values);
            foodDiary.setId(id);
            values.clear();

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    public void updateFoodDiary(FoodDiary foodDiary) {

        final SQLiteDatabase db = getWritableDatabase();

        final ContentValues values = new ContentValues();
        try {
            db.beginTransaction();
            values.put(FoodDiaryTable.COLUMN_FK_MEAL_DIARY, foodDiary.getMealDiaryId());
            values.put(FoodDiaryTable.COLUMN_FOOD_NAME, foodDiary.getFoodName());
            values.put(FoodDiaryTable.COLUMN_CARBOHYDRATE, foodDiary.getCarbohydrate());
            values.put(FoodDiaryTable.COLUMN_QUANTITY, foodDiary.getQuantity());
            db.update(FoodDiaryTable.TABLE_NAME, values, FoodDiaryTable.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(foodDiary.getId())});
            values.clear();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<FoodDiary> loadFoodDiaries(long mealDiaryId) {

        final String whereClause = FoodDiaryTable.COLUMN_FK_MEAL_DIARY + "=?";
        final String[] whereArgs = {String.valueOf(mealDiaryId)};
        List<FoodDiary> foodDiaries = null;
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(FoodDiaryTable.TABLE_NAME, FoodDiaryTable.COLUMNS,
                    whereClause, whereArgs, null, null, null);

            foodDiaries = new ArrayList<FoodDiary>(cursor.getCount());
            while (cursor.moveToNext()) {
                final FoodDiary foodDiary = new FoodDiary();

                //COLUMN_ID, COLUMN_FOOD_NAME, COLUMN_QUANTITY, COLUMN_UNIT, COLUMN_CARBOHYDRATE,
                //COLUMN_FK_MEAL_DIARY};
                foodDiary.setId(cursor.getLong(0));
                foodDiary.setFoodName(cursor.getString(1));
                foodDiary.setQuantity(cursor.getFloat(2));
                foodDiary.setUnit(cursor.getString(3));
                foodDiary.setCarbohydrate(cursor.getFloat(4));
                foodDiary.setMealDiaryId(cursor.getInt(5));

                foodDiaries.add(foodDiary);
            }
            sortFoodDiary(foodDiaries);
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return foodDiaries;
    }

    public List<FoodDiary> loadFoodDiaries() {

        Cursor cursor = null;
        List<FoodDiary> foodDiaries = null;
        try {
            cursor = getReadableDatabase().query(FoodDiaryTable.TABLE_NAME, FoodDiaryTable.COLUMNS,
                    null, null, null, null, null);

            foodDiaries = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                final FoodDiary foodDiary = new FoodDiary();

                foodDiary.setId(cursor.getLong(0));
                foodDiary.setFoodName(cursor.getString(1));
                foodDiary.setQuantity(cursor.getFloat(2));
                foodDiary.setUnit(cursor.getString(3));
                foodDiary.setCarbohydrate(cursor.getFloat(4));
                foodDiary.setMealDiaryId(cursor.getInt(5));

                foodDiaries.add(foodDiary);
            }
            sortFoodDiary(foodDiaries);
        } finally {
            if(cursor != null)
                cursor.close();
        }

        return foodDiaries;
    }

    public FoodDiary loadFoodDiary(Long foodDiaryId) {
        FoodDiary result = null;

        final String whereClause = FoodDiaryTable.COLUMN_ID + "=?";
        final String[] whereArgs = {String.valueOf(foodDiaryId)};

        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(FoodDiaryTable.TABLE_NAME, FoodDiaryTable.COLUMNS,
                    whereClause, whereArgs, null, null, null);

            if (cursor.moveToNext()) {
                result = new FoodDiary();
                result.setId(cursor.getLong(0));
                result.setFoodName(cursor.getString(1));
                result.setQuantity(cursor.getFloat(2));
                result.setUnit(cursor.getString(3));
                result.setCarbohydrate(cursor.getFloat(4));
                result.setMealDiaryId(cursor.getInt(5));
            }
        } finally {
            if(cursor != null)
                cursor.close();
        }

        return result;
    }

    public void deleteFoodDiary(Long foodDiaryId) {
        getWritableDatabase().delete(FoodDiaryTable.TABLE_NAME, FoodDiaryTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(foodDiaryId)});
    }

    private void sortFoodDiary(List<FoodDiary> foodDiaries) {
        Collections.sort(foodDiaries, new Comparator<FoodDiary>() {

            @Override
            public int compare(FoodDiary lhs, FoodDiary rhs) {
                return lhs.getFoodName().toLowerCase().compareTo(rhs.getFoodName().toLowerCase());
            }
        });
    }

}