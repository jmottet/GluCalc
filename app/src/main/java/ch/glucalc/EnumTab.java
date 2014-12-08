package ch.glucalc;

public enum EnumTab {
  TAB_NEW_MEAL(0), TAB_FOOD(1), TAB_MENU(2);

  private int index;

  EnumTab(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }

  public static EnumTab getInstanceOf(int index) {
    for (final EnumTab enumTab : EnumTab.values()) {
      if (index == enumTab.getIndex()) {
        return enumTab;
      }
    }
    throw new IllegalArgumentException();
  }
}
