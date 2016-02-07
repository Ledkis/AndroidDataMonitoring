package ledkis.module.androiddatamonitoring.example;

import android.app.Activity;
import android.content.Intent;


import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import ledkis.module.androiddatamonitoring.MonitoringActivity;
import ledkis.module.androiddatamonitoring.Utils;
import ledkis.module.androiddatamonitoring.model.KeyValueObject;
import ledkis.module.androiddatamonitoring.model.ListKeyValueObject;


public class MonitoringUtils {

    private static LinkedHashMap<String, Object> getUserPropertyMap(User user) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();

        if (user == null)
            return map;

        map.put("name", user.getName());
        map.put("age", user.getAge());
        map.put("weight", user.getWeight());
        map.put("birthday", user.getBirthday());
        map.put("object", user.getObject());
        map.put("things", user.getThings());


        return map;
    }

    private static KeyValueObject getKeyValueObjectFrom(User user) {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put(User.TAG.toUpperCase(), user.toString());

        map.putAll(Utils.getDisplayHashMap(getUserPropertyMap(user)));


        return KeyValueObject.getKeyValueObjectFromMap(map);
    }

    private static KeyValueObject getLoggingObjects(Map<String, String> map) {
        return KeyValueObject.getKeyValueObjectFromMap(map);
    }


    public static void startMonitoringActivity(final Activity activity,
                                                final User user,
                                                final String[] keys) {

        final LinkedHashMap<String, String> loggingMap = Utils.getLoggingMap(keys);

        ArrayList<KeyValueObject> keyValueObjects = new ArrayList<>();
        ArrayList<ListKeyValueObject> listKeyValueObjects = new ArrayList<>();
        KeyValueObject loggingObjects = null;

        if (null != user)
            keyValueObjects.add(getKeyValueObjectFrom(user));

        // Logging
        if (null != loggingMap)
            loggingObjects = getLoggingObjects(loggingMap);

        Intent intent = MonitoringActivity.getIntent(activity, keyValueObjects, listKeyValueObjects, loggingObjects);

        activity.startActivity(intent);

    }


}
