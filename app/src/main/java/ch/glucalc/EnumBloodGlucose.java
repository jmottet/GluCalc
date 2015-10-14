package ch.glucalc;

public enum EnumBloodGlucose {
  MMOL_L("[mmol/l]"), MG_DL("[mg/dl]"), G_L("[g/l]");

  private String label;

    EnumBloodGlucose(String aLabel) {
        label = aLabel;
    }

    public String getLabel() {
        return label;
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
