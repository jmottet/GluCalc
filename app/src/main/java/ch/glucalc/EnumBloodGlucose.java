package ch.glucalc;

public enum EnumBloodGlucose {
  MMOL_L("[mmol/l]", R.string.blood_glucose_millimoles_per_liter), MG_DL("[mg/dl]", R.string.blood_glucose_milligrams_per_liter), G_L("[g/l]", R.string.blood_glucose_grams_per_liter);

  private String label;

  private int descriptionKey;

  EnumBloodGlucose(String aLabel, int aDescriptionKey) {
    label = aLabel;
    descriptionKey = aDescriptionKey;
  }

  public String getLabel() {
      return label;
  }

  public int getDescriptionKey() {
  return descriptionKey;
}

  public EnumColor getColor(float value) {

    switch (this) {
      case MMOL_L:
        if (value >= 4 && value <= 10) {
          return EnumColor.GREEN;
        } else if (value > 10 && value < 15) {
          return EnumColor.ORANGE;
        } else {
          return EnumColor.RED;
        }
      case MG_DL:
        if (value >= 72 && value <= 180) {
          return EnumColor.GREEN;
        } else if (value > 180 && value < 270) {
          return EnumColor.ORANGE;
        } else {
          return EnumColor.RED;
        }
      case G_L:
        if (value >= 0.7 && value <= 1.8) {
          return EnumColor.GREEN;
        } else if (value > 1.8 && value < 2.7) {
          return EnumColor.ORANGE;
        } else {
          return EnumColor.RED;
        }
    }
    return EnumColor.GREEN;
  }
}
