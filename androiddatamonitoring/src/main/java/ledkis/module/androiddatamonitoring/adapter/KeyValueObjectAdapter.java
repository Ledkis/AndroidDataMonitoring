package ledkis.module.androiddatamonitoring.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import ledkis.module.androiddatamonitoring.R;
import ledkis.module.androiddatamonitoring.Util;

public class KeyValueObjectAdapter extends ArrayAdapter<Map.Entry<String, String>> {

    private Activity activity;
    List<Map.Entry<String, String>> entries;

    public KeyValueObjectAdapter(Activity activity, int resourceId, List<Map.Entry<String, String>> entries) {


        super(activity, resourceId, entries);
        this.activity = activity;
        this.entries = entries;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map.Entry<String, String> entry = entries.get(position);
        View rootView;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
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

    public void remove(int position) {
        Map.Entry<String, String> entry = entries.get(position);
        remove(entry);
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    public boolean isEnabled(int position) {
        return false;
    }

    public Map.Entry<String, String> get(int position) {
        return entries.get(position);
    }

    public List<Map.Entry<String, String>> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}