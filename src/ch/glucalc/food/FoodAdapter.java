package ch.glucalc.food;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ch.glucalc.R;

public class FoodAdapter extends BaseAdapter {

  // public static abstract class Row {
  // }

  public static final class Section {
    public final String text;

    public Section(String text) {
      this.text = text;
    }
  }

  private List<Object> rows;

  public void setRows(List<Object> rows) {
    this.rows = rows;
  }

  @Override
  public int getCount() {
    return rows.size();
  }

  @Override
  public Object getItem(int position) {
    return rows.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public int getViewTypeCount() {
    return 2;
  }

  @Override
  public int getItemViewType(int position) {
    if (getItem(position) instanceof Section) {
      return 1;
    } else {
      return 0;
    }
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    if (getItemViewType(position) == 0) {
      // Item
      if (view == null) {
        // final LayoutInflater inflater = (LayoutInflater)
        // parent.getContext().getSystemService(
        // Context.LAYOUT_INFLATER_SERVICE);
        // view = inflater.inflate(R.layout.row_item, parent, false);
        final LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(
            Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.row_food_view, parent, false);

      }

      final Food food = (Food) getItem(position);

      final TextView textView = (TextView) view.findViewById(R.id.name);
      textView.setText(food.getName());

      final TextView textView2 = (TextView) view.findViewById(R.id.quantity);
      textView2.setText("" + food.getQuantity() + " " + food.getUnit());

      final TextView textView3 = (TextView) view.findViewById(R.id.carbonhydrate);
      textView3.setText("" + food.getCarbonhydrate());

    } else {
      // Section
      if (view == null) {
        final LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(
            Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.row_section_view, parent, false);
      }
      final Section section = (Section) getItem(position);
      final TextView textView = (TextView) view.findViewById(R.id.textView1);
      textView.setText(section.text);
    }
    return view;
  }
}
