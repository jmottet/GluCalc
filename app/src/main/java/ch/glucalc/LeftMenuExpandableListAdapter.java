package ch.glucalc;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LeftMenuExpandableListAdapter extends BaseExpandableListAdapter {

  private final Context mContext;
  private final List<MenuGroupItem> parentRecord;
  private final HashMap<String, List<MenuChildItem>> childRecord;
  private final LayoutInflater inflater;

  public LeftMenuExpandableListAdapter(Context context, List<MenuGroupItem> listDataHeader,
      HashMap<String, List<MenuChildItem>> listChildData) {
    this.mContext = context;
    this.parentRecord = listDataHeader;
    this.childRecord = listChildData;
    this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public Object getChild(int groupPosition, int childPosititon) {
    return this.childRecord.get(this.parentRecord.get(groupPosition).getTitle()).get(childPosititon);
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override
  public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView,
      ViewGroup parent) {

    final MenuChildItem childItem = (MenuChildItem) getChild(groupPosition, childPosition);

    if (convertView == null) {
      convertView = inflater.inflate(R.layout.navigation_drawer_sublist_item, null);
    }

    final TextView txtListChild = (TextView) convertView.findViewById(R.id.left_submenu_textview);

    txtListChild.setText(childItem.getTitle());

    if (childItem.getImageResource() != null) {
      final ImageView imageView = (ImageView) convertView.findViewById(R.id.left_submenu_imageview);
      imageView.setImageResource(childItem.getImageResource());
    }

    return convertView;
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return this.childRecord.get(this.parentRecord.get(groupPosition).getTitle()).size();
  }

  @Override
  public Object getGroup(int groupPosition) {
    return this.parentRecord.get(groupPosition);
  }

  @Override
  public int getGroupCount() {
    return this.parentRecord.size();
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    MenuGroupItem menuGroupItem = (MenuGroupItem) getGroup(groupPosition);

    if (convertView == null) {
      convertView = inflater.inflate(R.layout.navigation_drawer_list_item, null);
    }

    final TextView lblListHeader = (TextView) convertView.findViewById(R.id.left_menu_textview);
    lblListHeader.setTypeface(null, Typeface.BOLD);
    lblListHeader.setText(menuGroupItem.getTitle());

    if (menuGroupItem.getImageResource() != null) {
      final ImageView imageView = (ImageView) convertView.findViewById(R.id.left_menu_imageview);
      imageView.setImageResource(menuGroupItem.getImageResource());
    }
    return convertView;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }
}
