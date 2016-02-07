package ledkis.monitoring.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.List;

import ledkis.monitoring.MonitoringActivity;
import ledkis.monitoring.R;
import ledkis.monitoring.adapter.ListKeyValueObjectAdapter;
import ledkis.monitoring.model.ListKeyValueObject;

public class ListKeyValueFragment extends Fragment {

    ListKeyValueObject listKeyValueObject;
    ExpandableListView entryListView;
    ListKeyValueObjectAdapter listKeyValueObjectAdapter;

    public ListKeyValueFragment() {
    }

    public static ListKeyValueFragment newInstance(ListKeyValueObject listKeyValueObject) {
        ListKeyValueFragment f = new ListKeyValueFragment();
        Bundle b = new Bundle();
        b.putParcelable(MonitoringActivity.KEY_VALUE_OBJECT, listKeyValueObject);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            listKeyValueObject = getArguments().getParcelable(MonitoringActivity.KEY_VALUE_OBJECT);
        else
            listKeyValueObject = new ListKeyValueObject();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_list_key_value, container, false);

        entryListView = (ExpandableListView) rootView.findViewById(R.id.entryListView);

        listKeyValueObjectAdapter = new ListKeyValueObjectAdapter(getActivity(), listKeyValueObject.getEntries());
        entryListView.setAdapter(listKeyValueObjectAdapter);

        collapseAll();

        return rootView;

    }

    private void expandAll() {
        int count = listKeyValueObjectAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            entryListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listKeyValueObjectAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            entryListView.collapseGroup(i);
        }
    }


    public void refresh(List<ListKeyValueObject> entries) {
        entries.clear();
        entries.addAll(entries);
        listKeyValueObjectAdapter.notifyDataSetChanged();
    }

}

