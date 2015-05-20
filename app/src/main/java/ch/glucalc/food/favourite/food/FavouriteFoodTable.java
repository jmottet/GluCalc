package ch.glucalc.food.favourite.food;

import ch.glucalc.food.FoodTable;
import ch.glucalc.food.category.CategoryFoodTable;
import ch.glucalc.meal.type.MealType;
import ch.glucalc.meal.type.MealTypeTable;

public class FavouriteFoodTable {

  public static final String TABLE_NAME = "favourite_food";

  public static final String COLUMN_ID = "id";
  public static final String COLUMN_FK_MEAL_TYPE = "fk_meal_type";
  public static final String COLUMN_FK_FOOD = "fk_food";
  public static final String COLUMN_QUANTITY = "quantity";
  public static final String COLUMN_CARBONHYDRATE = "carbonhydrate";


  public static final String[] COLUMNS = { COLUMN_ID, COLUMN_FK_MEAL_TYPE, COLUMN_FK_FOOD, COLUMN_QUANTITY, COLUMN_CARBONHYDRATE};

  // Database creation sql statement
  public static final String TABLE_CREATE = "create table " + TABLE_NAME
      + "(" //
      + COLUMN_ID
      + " integer primary key autoincrement, " //
      + COLUMN_FK_MEAL_TYPE
      + " integer not null," //
      + COLUMN_FK_FOOD
      + " integer not null," //
      + COLUMN_QUANTITY
      + " real not null, " //
      + COLUMN_CARBONHYDRATE
      + " real not null," //
      + "foreign key (" + COLUMN_FK_MEAL_TYPE + ") REFERENCES " + MealTypeTable.TABLE_NAME + "("
      + MealTypeTable.COLUMN_ID + ")," //
      + "foreign key (" + COLUMN_FK_FOOD + ") REFERENCES " + FoodTable.TABLE_NAME + "("
      + FoodTable.COLUMN_ID + ")" //
      + ");";

  public static final String TABLE_DROP = "drop table " + TABLE_NAME + ";";
}
