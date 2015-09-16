package ch.glucalc.meal.diary;

import ch.glucalc.food.FoodConstants;
import ch.glucalc.food.favourite.food.FavouriteFood;

public class FoodDiary {

    private long id;
    private String foodName;
    private Float quantity = 0F;
    private String unit;
    private Float carbohydrate = 0F;
    private long mealDiaryId;
    private boolean selected = false;

    public FoodDiary() {
    }

    public FoodDiary(long newMealDiaryId, FavouriteFood favouriteFood) {
        foodName = favouriteFood.getName();
        quantity = favouriteFood.getQuantity();
        unit = "g";
        carbohydrate = favouriteFood.getCarbonhydrate();
        mealDiaryId = newMealDiaryId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Float getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(Float carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public long getMealDiaryId() {
        return mealDiaryId;
    }

    public void setMealDiaryId(long mealDiaryId) {
        this.mealDiaryId = mealDiaryId;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean areSomeMandatoryFieldsMissing() {
        return false;
    }
}
