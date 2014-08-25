package ch.glucalc.food;

import ch.glucalc.food.category.CategoryFoodTable;

public class FoodTable {

  public static final String TABLE_NAME = "food";

  public static final String COLUMN_ID = "id";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_QUANTITY = "quantity";
  public static final String COLUMN_UNIT = "unit";
  public static final String COLUMN_CARBONHYDRATE = "carbonhydrate";
  public static final String COLUMN_FK_CATEGORY = "fk_category";

  public static final String[] COLUMNS = { COLUMN_ID, COLUMN_NAME, COLUMN_QUANTITY, COLUMN_UNIT, COLUMN_CARBONHYDRATE,
      COLUMN_FK_CATEGORY };

  // Database creation sql statement
  public static final String TABLE_CREATE = "create table " + TABLE_NAME
      + "(" //
      + COLUMN_ID
      + " integer primary key autoincrement, " //
      + COLUMN_NAME
      + " text not null, " //
      + COLUMN_QUANTITY
      + " real not null, " //
      + COLUMN_UNIT
      + " text not null, " //
      + COLUMN_CARBONHYDRATE
      + " real not null," //
      + COLUMN_FK_CATEGORY
      + " integer not null," //
      + "foreign key (" + COLUMN_FK_CATEGORY + ") REFERENCES " + CategoryFoodTable.TABLE_NAME + "("
      + CategoryFoodTable.COLUMN_ID + ")" //
      + ");";

  public static final String TABLE_DROP = "drop table " + TABLE_NAME + ";";
}
