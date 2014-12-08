package ch.glucalc.food.category;

public class CategoryFoodTable {

  public static final String TABLE_NAME = "category_food";

  public static final String COLUMN_ID = "id";
  public static final String COLUMN_NAME = "name";

  public static final String[] COLUMNS = { COLUMN_ID, COLUMN_NAME };

  // Database creation sql statement
  public static final String TABLE_CREATE = "create table " + TABLE_NAME + "(" //
      + COLUMN_ID + " integer primary key autoincrement, " //
      + COLUMN_NAME + " text not null" //
      + ");";

  public static final String TABLE_DROP = "drop table " + TABLE_NAME + ";";
}
