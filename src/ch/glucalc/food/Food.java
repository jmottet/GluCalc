package ch.glucalc.food;

import android.text.TextUtils;

public class Food {

  private long id;
  private String name;
  private Float quantity;
  private Float carbonhydrate;
  private String unit;
  private long categoryId;
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

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(long categoryId) {
    this.categoryId = categoryId;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public boolean areSomeMandatoryFieldsMissing() {
    if (TextUtils.isEmpty(unit) || TextUtils.isEmpty(name) || carbonhydrate == null || quantity == null
        || categoryId == FoodConstants.FAKE_DEFAULT_LONG_ID) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return name;
  }

}
