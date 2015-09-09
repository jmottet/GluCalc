package ch.glucalc.meal.diary;

import ch.glucalc.meal.type.MealTypeTable;

public class FoodDiaryTable {

    public static final String TABLE_NAME = "food_diary";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FOOD_NAME = "food_name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_CARBOHYDRATE = "carbonhydrate";
    public static final String COLUMN_FK_MEAL_DIARY = "fk_meal_diary";

    public static final String[] COLUMNS = {COLUMN_ID, COLUMN_FOOD_NAME, COLUMN_QUANTITY, COLUMN_UNIT, COLUMN_CARBOHYDRATE,
            COLUMN_FK_MEAL_DIARY};

    // Database creation sql statement
    public static final String TABLE_CREATE = "create table " + TABLE_NAME
            + "(" //
            + COLUMN_ID
            + " integer primary key autoincrement, " //
            + COLUMN_FOOD_NAME
            + " text not null, " //
            + COLUMN_QUANTITY
            + " real not null, " //
            + COLUMN_UNIT
            + " text not null, " //
            + COLUMN_CARBOHYDRATE
            + " real not null," //
            + COLUMN_FK_MEAL_DIARY
            + " integer not null," //
            + " foreign key (" + COLUMN_FK_MEAL_DIARY + ") REFERENCES " + MealDiaryTable.TABLE_NAME + "("
            + MealDiaryTable.COLUMN_ID + ")" //
            + ");";

    public static final String TABLE_DROP = "drop table " + TABLE_NAME + ";";
}
