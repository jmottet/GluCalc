package ch.glucalc.configuration;

import ch.glucalc.food.category.CategoryFoodTable;

public class ParametersTable {

  public static final String TABLE_NAME = "parameters";

  public static final String COLUMN_KEY = "key";
  public static final String COLUMN_VALUE = "value";

  public static final String[] COLUMNS = { COLUMN_KEY, COLUMN_VALUE};

  // Database creation sql statement
  public static final String TABLE_CREATE = "create table " + TABLE_NAME
      + "(" //
      + COLUMN_KEY
      + " text primary key, " //
      + COLUMN_VALUE
      + " text not null );";

  public static final String TABLE_DROP = "drop table " + TABLE_NAME + ";";
}
