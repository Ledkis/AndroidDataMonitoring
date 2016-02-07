package ledkis.module.androiddatamonitoring;

import android.util.Log;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static final String TAG = "Utils";

    public static final String NULL_ERROR_MESSAGE = "NULL";
    public static final String FULL_DATE_SIMPLE_FORMAT = "dd-MM-yyy HH:mm";

    public static boolean isHeader(String key) {
        return key.startsWith("_");
    }

    public static long valueOrDefault(DateTime date, long defaultDateMillis) {
        return date != null ? date.getMillis() : defaultDateMillis;
    }

    public static String valueOrDefaultString(DateTime date, String format, String defaultDateString) {
        return date != null ? date.toString(format) : defaultDateString;
    }

    public static boolean isNullOrEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    public static String valueOrDefault(String string, String defaultString) {
        return isNullOrEmpty(string) ? defaultString : string;
    }

    public static String valueOrDefault(Object object, String defaultString) {
        if (object == null)
            return "";
        else
            return valueOrDefault(object.toString(), defaultString);
    }

    public static List<String> jsonArrayToStringList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        try {
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    // TODO moche : toString
                    list.add((String) jsonArray.get(i).toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String concatStrings(List<String> stringList, String separator) {
        String concatString = "";

        int i = 0;
        for (String string : stringList) {

            concatString += string;
            if (i != stringList.size() - 1)
                concatString += separator;
            i++;
        }
        return concatString;
    }

    public static List<String> listToStringList(List list) {
        List<String> stringList = new ArrayList<>();
        for (Object object : list) {
            stringList.add(object.toString());
        }
        return stringList;
    }

    public static LinkedHashMap<String, String> getDisplayHashMap(LinkedHashMap<String, Object> map) {
        LinkedHashMap<String, String> displayableMap = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();

            if (value instanceof String) {
                displayableMap.put(entry.getKey(), valueOrDefault((String) entry.getValue(), NULL_ERROR_MESSAGE));
            } else if (value instanceof DateTime) {
                displayableMap.put(entry.getKey(), valueOrDefaultString((DateTime) entry.getValue(), FULL_DATE_SIMPLE_FORMAT, NULL_ERROR_MESSAGE));
            } else if (value instanceof JSONArray) {
                displayableMap.put(entry.getKey(), concatStrings(jsonArrayToStringList((JSONArray) entry.getValue()), " "));
            } else if (value instanceof List) {
                displayableMap.put(entry.getKey(), concatStrings((listToStringList((List) entry.getValue())), " "));
            } else {
                displayableMap.put(entry.getKey(), valueOrDefault(entry.getValue(), NULL_ERROR_MESSAGE));
            }
        }
        return displayableMap;
    }


    public static LinkedHashMap<String, String> getLoggingMap(String[] keys) {
        LinkedHashMap<String, String> loggingMap = new LinkedHashMap<>();
        if (null == keys || keys.length == 0)
            return loggingMap;
        if (keys.length % 2 != 0) {
            Log.e(TAG, "getLoggingMap: keys not a multiple of 2: " + keys.length);
            return loggingMap;
        }
        for (int i = 0; i < keys.length; i += 2) {
            loggingMap.put(keys[i], keys[i + 1]);
        }
        return loggingMap;
    }
}
