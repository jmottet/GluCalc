package ch.glucalc.meal.type;


public class MealTypeTable {

  public static final String TABLE_NAME = "meal_type";

  public static final String COLUMN_ID = "id";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_FOOD_TARGET = "food_target";
  public static final String COLUMN_GLYCEMIA_TARGET = "glycemia_target";
  public static final String COLUMN_INSULIN_SENSITIVITY = "insulin_sensitivity";
  public static final String COLUMN_INSULIN = "insulin";

  public static final String[] COLUMNS = { COLUMN_ID, COLUMN_NAME, COLUMN_FOOD_TARGET, COLUMN_GLYCEMIA_TARGET,
      COLUMN_INSULIN_SENSITIVITY, COLUMN_INSULIN };

  // Database creation sql statement
  public static final String TABLE_CREATE = "create table " + TABLE_NAME + "(" //
      + COLUMN_ID + " integer primary key autoincrement, " //
      + COLUMN_NAME + " text not null, " //
      + COLUMN_FOOD_TARGET + " real not null, " //
      + COLUMN_GLYCEMIA_TARGET + " real not null, " //
      + COLUMN_INSULIN_SENSITIVITY + " real not null," //
      + COLUMN_INSULIN + " real not null);";

  public static final String TABLE_DROP = "drop table " + TABLE_NAME + ";";
}
