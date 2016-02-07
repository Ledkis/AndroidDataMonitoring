package ledkis.module.androiddatamonitoring.example;

import android.app.Activity;
import android.content.Intent;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ledkis.module.androiddatamonitoring.MonitoringActivity;
import ledkis.module.androiddatamonitoring.Utils;
import ledkis.module.androiddatamonitoring.model.KeyValueObject;
import ledkis.module.androiddatamonitoring.model.ListKeyValueObject;


public class MonitoringUtils {

    private static LinkedHashMap<String, Object> getUserPropertyMap(Physicist physicist) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        if (physicist == null)
            return map;

        map.put("name", physicist.getName());
        map.put("age", physicist.getAge());
        map.put("phi", physicist.getPhi());
        map.put("birthday", physicist.getBirthday());
        map.put("object", physicist.getObject());
        map.put("things", physicist.getThings());


        return map;
    }



    private static KeyValueObject getKeyValueObjectFrom(Physicist physicist) {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put(Physicist.TAG.toUpperCase(), physicist.toString());

        map.putAll(Utils.getDisplayHashMap(getUserPropertyMap(physicist)));


        return KeyValueObject.getKeyValueObjectFromMap(map);
    }

    private static ListKeyValueObject getListKeyValueObjectFromPhysicists(List<Physicist> physicists) {
        List<KeyValueObject> listKeyValueObject = new ArrayList<>();


        KeyValueObject header = new KeyValueObject("LIST USER (" + physicists.size() + ")");
        listKeyValueObject.add(header);

        for (Physicist physicist : physicists) {
            if (null != physicist)
                listKeyValueObject.add(getKeyValueObjectFrom(physicist));
        }

        return new ListKeyValueObject(listKeyValueObject);
    }

    private static KeyValueObject getLoggingObjects(Map<String, String> map) {
        return KeyValueObject.getKeyValueObjectFromMap(map);
    }


    public static void startMonitoringActivity(final Activity activity,
                                                final Physicist physicist,
                                                final List<Physicist> physicists,
                                                final String[] keys) {

        final LinkedHashMap<String, String> loggingMap = Utils.getLoggingMap(keys);

        ArrayList<KeyValueObject> keyValueObjects = new ArrayList<>();
        ArrayList<ListKeyValueObject> listKeyValueObjects = new ArrayList<>();
        KeyValueObject loggingObjects = null;

        if (null != physicist)
            keyValueObjects.add(getKeyValueObjectFrom(physicist));

        if (null != physicists)
            listKeyValueObjects.add(getListKeyValueObjectFromPhysicists(physicists));

        // Logging
        if (null != loggingMap)
            loggingObjects = getLoggingObjects(loggingMap);

        Intent intent = MonitoringActivity.getIntent(activity, keyValueObjects, listKeyValueObjects, loggingObjects);

        activity.startActivity(intent);

    }


}
