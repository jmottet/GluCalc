package ch.glucalc.meal.type;

import android.text.TextUtils;

public class MealType {

  private long id;
  private String name;
  private Float foodTarget;
  private Float glycemiaTarget;
  private Float insulinSensitivity;
  private Float insulin;
  private boolean selected = false;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Float getFoodTarget() {
    return foodTarget;
  }

  public void setFoodTarget(Float foodTarget) {
    this.foodTarget = foodTarget;
  }

  public Float getGlycemiaTarget() {
    return glycemiaTarget;
  }

  public void setGlycemiaTarget(Float glycemiaTarget) {
    this.glycemiaTarget = glycemiaTarget;
  }

  public Float getInsulinSensitivity() {
    return insulinSensitivity;
  }

  public void setInsulinSensitivity(Float insulinSensitivity) {
    this.insulinSensitivity = insulinSensitivity;
  }

  public Float getInsulin() {
    return insulin;
  }

  public void setInsulin(Float insulin) {
    this.insulin = insulin;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public boolean areSomeMandatoryFieldsMissing() {
    if (TextUtils.isEmpty(name) || foodTarget == null || glycemiaTarget == null || insulinSensitivity == null
        || insulin == null) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return name;
  }

}
