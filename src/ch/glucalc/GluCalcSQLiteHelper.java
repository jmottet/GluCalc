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

public class GluCalcSQLiteHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "glucalc.db";
  private static final int DATABASE_VERSION = 1;

  public GluCalcSQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(FoodTable.TABLE_CREATE);

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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

        final long id = db.insert(FoodTable.TABLE_NAME, "", values);
        food.setId(id);
        values.clear();
      }
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }

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
      foods.add(food);
    }
    return foods;
  }

  public void deleteFoods() {
    getWritableDatabase().delete(FoodTable.TABLE_NAME, null, null);
  }
}
