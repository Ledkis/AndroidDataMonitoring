package ledkis.module.androiddatamonitoring.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * http://stackoverflow.com/questions/5566921/androidpassing-a-hash-map-between-activities
 */
public class KeyValueObject implements Parcelable {

    private static final String TAG = "KeyValueData";

    // http://stackoverflow.com/questions/10710193/how-to-preserve-insertion-order-in-hashmap
    LinkedHashMap<String, String> keyValueObjects;

    public KeyValueObject() {
        keyValueObjects = new LinkedHashMap<>();
    }

    public KeyValueObject(String header) {
        keyValueObjects = new LinkedHashMap<>();
        keyValueObjects.put(header, header);
    }

    public KeyValueObject(LinkedHashMap<String, String> keyValueObjects) {
        this.keyValueObjects = keyValueObjects;
    }

    public List<Map.Entry<String, String>> getEntries() {
        // http://stackoverflow.com/questions/8892360/convert-set-to-list-without-creating-new-list

        List<Map.Entry<String, String>> list = new ArrayList<>();
        list.addAll(keyValueObjects.entrySet());
        return list;
    }


    public KeyValueObject(Parcel source) {
        keyValueObjects = new LinkedHashMap<>();
        final int length = source.readInt();
        for (int i = 0; i < length; i++) {
            String key = source.readString();
            String value = source.readString();
            keyValueObjects.put(key, value);
        }
    }


    public void writeToParcel(Parcel dest, int parcelableFlags) {
        final int length = keyValueObjects.size();
        dest.writeInt(length);
        if (length > 0) {
            for (Map.Entry<String, String> entry : keyValueObjects.entrySet()) {
                dest.writeString(entry.getKey());
                String dat = entry.getValue();
                dest.writeString(dat);
            }
        }
    }

    public int size() {
        return keyValueObjects.size();
    }

    public static KeyValueObject getKeyValueObjectFromMap(Map<String, ?> map) {

        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            data.put(entry.getKey(), entry.getValue().toString());
        }

        return new KeyValueObject(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KeyValueObject> CREATOR = new Creator<KeyValueObject>() {
        public KeyValueObject createFromParcel(Parcel source) {
            return new KeyValueObject(source);
        }

        public KeyValueObject[] newArray(int size) {
            return new KeyValueObject[size];
        }
    };
}
