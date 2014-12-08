package ch.glucalc.food.category;

public class CategoryFood {

  private long id;
  private String name;
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

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public boolean isSelected() {
    return selected;
  }

}
