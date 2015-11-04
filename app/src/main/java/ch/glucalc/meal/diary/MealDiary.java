package ch.glucalc.meal.diary;

import java.text.SimpleDateFormat;
import java.util.Date;

import ch.glucalc.food.FoodConstants;

public class MealDiary {

    private long id;
    private String mealDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    private Float glycemiaMeasured = 0F;
    private Float carbohydrateTotal = 0F;
    private Float bolusCalculated = 0F;
    private Float bolusGiven = 0F;
    private Boolean temporary = true;
    private long mealTypeId;

    public MealDiary() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Float getBolusGiven() {
        return bolusGiven;
    }

    public void setBolusGiven(Float bolusGiven) {
        this.bolusGiven = bolusGiven;
    }

    public Float getCarbohydrateTotal() {
        return carbohydrateTotal;
    }

    public void setCarbohydrateTotal(Float carbohydrateTotal) {
        this.carbohydrateTotal = carbohydrateTotal;
    }

    public Float getGlycemiaMeasured() {
        return glycemiaMeasured;
    }

    public void setGlycemiaMeasured(Float glycemiaMeasured) {
        this.glycemiaMeasured = glycemiaMeasured;
    }

    public String getMealDate() {
        return mealDate;
    }

    public void setMealDate(String mealDate) {
        this.mealDate = mealDate;
    }

    public long getMealTypeId() {
        return mealTypeId;
    }

    public void setMealTypeId(long mealTypeId) {
        this.mealTypeId = mealTypeId;
    }

    public Boolean getTemporary() {
        return temporary;
    }

    public void setTemporary(Boolean temporary) {
        this.temporary = temporary;
    }

    public Float getBolusCalculated() {
        return bolusCalculated;
    }

    public void setBolusCalculated(Float bolusCalculated) {
        this.bolusCalculated = bolusCalculated;
    }

    public boolean areSomeMandatoryFieldsMissing() {
        return false;
    }

}
