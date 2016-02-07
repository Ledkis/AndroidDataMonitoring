package ledkis.module.androiddatamonitoring.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import ledkis.module.androiddatamonitoring.R;
import ledkis.module.androiddatamonitoring.Util;
import ledkis.module.androiddatamonitoring.model.KeyValueObject;


public class ListKeyValueObjectAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    List<KeyValueObject> entries;

    LayoutInflater inflater;

    public ListKeyValueObjectAdapter(Activity activity, List<KeyValueObject> entries) {
        this.activity = activity;
        this.entries = entries;

        inflater = activity.getLayoutInflater();
    }

    @Override
    public Map.Entry<String, String> getChild(int groupPosition, int childPosititon) {
        return entries.get(groupPosition).getEntries().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Map.Entry<String, String> entry = getChild(groupPosition, childPosition);
        View rootView;
        if (convertView == null) {
            rootView = inflater.inflate(R.layout.row_key_value, null);
        } else {
            rootView = convertView;
        }

        if (Util.isHeader(entry.getKey())) {
            rootView.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_orange_dark));
        } else {
            rootView.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_orange_light));
        }

        ((TextView) rootView.findViewById(R.id.keyTextView)).setText(entry.getKey());
        ((TextView) rootView.findViewById(R.id.valueTextView)).setText(entry.getValue());

        return rootView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return entries.get(groupPosition).getEntries().size();
    }

    @Override
    public List<Map.Entry<String, String>> getGroup(int groupPosition) {
        return entries.get(groupPosition).getEntries();
    }

    @Override
    public int getGroupCount() {
        return entries.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        KeyValueObject keyValueObject = entries.get(groupPosition);
        View rootView;
        if (convertView == null) {
            rootView = inflater.inflate(R.layout.row_list_key_value, null);
        } else {
            rootView = convertView;
        }

        rootView.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_red_light));

        TextView textView = (TextView) rootView.findViewById(R.id.headerTextView);

        textView.setText(keyValueObject.getEntries().get(0).getValue());

        return rootView;
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
