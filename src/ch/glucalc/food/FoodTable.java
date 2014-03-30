package ch.glucalc.food;

public class FoodTable {

	public static final String TABLE_NAME = "food";

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_QUANTITY = "quantity";
	public static final String COLUMN_UNIT = "unit";
	public static final String COLUMN_CARBONHYDRATE = "carbonhydrate";

	public static final String[] COLUMNS = { COLUMN_ID, COLUMN_NAME,
			COLUMN_QUANTITY, COLUMN_UNIT, COLUMN_CARBONHYDRATE };

	// Database creation sql statement
	public static final String TABLE_CREATE = "create table " + TABLE_NAME
			+ "(" //
			+ COLUMN_ID + " integer primary key autoincrement, " //
			+ COLUMN_NAME + " text not null, " //
			+ COLUMN_QUANTITY + " real not null, " //
			+ COLUMN_UNIT + " text not null, " //
			+ COLUMN_CARBONHYDRATE + " real not null" //
			+ ");";
}
