package ch.glucalc.meal.diary;

import ch.glucalc.meal.type.MealTypeTable;

public class MealDiaryTable {

  public static final String TABLE_NAME = "meal_diary";

  public static final String COLUMN_ID = "id";
  public static final String COLUMN_MEAL_DATE = "meal_date";
  public static final String COLUMN_GLYCEMIA_MEASURED = "glycemia_measured";
  public static final String COLUMN_CARBOHYDRATE_TOTAL = "carbohydrate_total";
  public static final String COLUMN_BOLUS_CALCULATED = "bolus_calculated";
  public static final String COLUMN_BOLUS_GIVEN = "bolus_given";
  public static final String COLUMN_TEMPORARY = "temporary";
  public static final String COLUMN_FK_MEAL_TYPE = "fk_meal_type";

  public static final String[] COLUMNS = { COLUMN_ID, COLUMN_MEAL_DATE, COLUMN_GLYCEMIA_MEASURED, COLUMN_CARBOHYDRATE_TOTAL,
          COLUMN_BOLUS_CALCULATED, COLUMN_BOLUS_GIVEN, COLUMN_TEMPORARY, COLUMN_FK_MEAL_TYPE};

  // Database creation sql statement
  public static final String TABLE_CREATE = "create table " + TABLE_NAME
      + "(" //
      + COLUMN_ID
      + " integer primary key autoincrement, " //
      + COLUMN_MEAL_DATE
      + " text, " //
      + COLUMN_GLYCEMIA_MEASURED
      + " real, " //
      + COLUMN_CARBOHYDRATE_TOTAL
      + " real, " //
      + COLUMN_BOLUS_CALCULATED
      + " real," //
      + COLUMN_BOLUS_GIVEN
      + " real," //
      + COLUMN_TEMPORARY
      + " integer default 1," //
      + COLUMN_FK_MEAL_TYPE
      + " integer not null," //
      + "foreign key (" + COLUMN_FK_MEAL_TYPE + ") REFERENCES " + MealTypeTable.TABLE_NAME + "("
      + MealTypeTable.COLUMN_ID + ")" //
      + ");";

  public static final String TABLE_DROP = "drop table " + TABLE_NAME + ";";
}
