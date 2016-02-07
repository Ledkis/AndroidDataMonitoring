package ledkis.monitoring.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

import ledkis.monitoring.MonitoringActivity;
import ledkis.monitoring.R;
import ledkis.monitoring.adapter.KeyValueObjectAdapter;
import ledkis.monitoring.model.KeyValueObject;

public class KeyValueFragment extends Fragment {

    KeyValueObject keyValueObject;
    ListView entryListView;
    KeyValueObjectAdapter keyValueObjectAdapter;

    public KeyValueFragment() {
    }

    public static KeyValueFragment newInstance(KeyValueObject data) {
        KeyValueFragment f = new KeyValueFragment();
        Bundle b = new Bundle();
        b.putParcelable(MonitoringActivity.KEY_VALUE_OBJECT, data);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            keyValueObject = getArguments().getParcelable(MonitoringActivity.KEY_VALUE_OBJECT);
        else
            keyValueObject = new KeyValueObject();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_key_value, container, false);

        entryListView = (ListView) rootView.findViewById(R.id.entryListView);

        keyValueObjectAdapter = new KeyValueObjectAdapter(getActivity(), R.layout.row_key_value, keyValueObject.getEntries());
        entryListView.setAdapter(keyValueObjectAdapter);

        return rootView;

    }


    public void refresh(List<Map.Entry<String, Object>> entries) {
        entries.clear();
        entries.addAll(entries);
        keyValueObjectAdapter.notifyDataSetChanged();
    }


}

