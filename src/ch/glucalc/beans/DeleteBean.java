package ch.glucalc.beans;

import java.util.HashSet;
import java.util.Set;

import android.view.Menu;

public class DeleteBean {

  private boolean modeMultiSelection = false;

  private int numberItemSelected = 0;

  private Menu mMenu;

  private final Set<Long> idsToDelete = new HashSet<Long>();

  public boolean isModeMultiSelection() {
    return modeMultiSelection;
  }

  public void setModeMultiSelection(boolean modeMultiSelection) {
    this.modeMultiSelection = modeMultiSelection;
  }

  public int getNumberItemSelected() {
    return numberItemSelected;
  }

  public void setNumberItemSelected(int numberItemSelected) {
    this.numberItemSelected = numberItemSelected;
  }

  public void addOneToNumberItemSelected() {
    this.numberItemSelected++;
  }

  public void substractOneToNumberItemSelected() {
    this.numberItemSelected--;
  }

  public Menu getmMenu() {
    return mMenu;
  }

  public void setmMenu(Menu mMenu) {
    this.mMenu = mMenu;
  }

  public void addIdToDelete(Long aNewId) {
    idsToDelete.add(aNewId);
  }

  public void resetIdsToDelete() {
    idsToDelete.clear();
  }

  public Set<Long> getIdsToDelete() {
    return idsToDelete;
  }

}
