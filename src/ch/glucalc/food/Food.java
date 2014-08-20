package ch.glucalc.food;

import android.text.TextUtils;

public class Food {

  private long id;
  private String name;
  private Float quantity;
  private Float carbonhydrate;
  private String unit;

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

  public boolean areSomeMandatoryFieldsMissing() {
    if (TextUtils.isEmpty(unit) || TextUtils.isEmpty(name) || carbonhydrate == null || quantity == null) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return name;
  }

}
