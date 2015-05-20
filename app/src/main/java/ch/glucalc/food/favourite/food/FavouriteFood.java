package ch.glucalc.food.favourite.food;

import android.text.TextUtils;

import ch.glucalc.food.FoodConstants;
import ch.glucalc.meal.type.MealTypeConstants;

public class FavouriteFood {

  private long id;
  private String name;
  private Float quantity = 0F;
  private Float carbonhydrate = 0F;
  private long foodId;
  private long mealTypeId;

  private boolean selected = false;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Float getQuantity() {
    return quantity;
  }

  public void setQuantity(Float quantity) {
    this.quantity = quantity;
  }

  public Float getCarbonhydrate() {
    return carbonhydrate;
  }

  public void setCarbonhydrate(Float carbonhydrate) {
    this.carbonhydrate = carbonhydrate;
  }

  public long getFoodId() {
        return foodId;
    }

  public void setFoodId(long foodId) {
        this.foodId = foodId;
    }

  public long getMealTypeId() {
        return mealTypeId;
    }

  public void setMealTypeId(long mealTypeId) {
        this.mealTypeId = mealTypeId;
    }

  public String getName() {
        return name;
    }

  public void setName(String name) {
        this.name = name;
    }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public boolean areSomeMandatoryFieldsMissing() {
    if (carbonhydrate == null || quantity == null
        || mealTypeId == MealTypeConstants.FAKE_DEFAULT_ID
        || foodId == FoodConstants.FAKE_DEFAULT_LONG_ID) {
      return true;
    }
    return false;
  }
}
