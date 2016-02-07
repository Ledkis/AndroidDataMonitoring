package ledkis.monitoring.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * http://stackoverflow.com/questions/5566921/androidpassing-a-hash-map-between-activities
 */
public class ListKeyValueObject implements Parcelable {

    private static final String TAG = "ListKeyValueObject";


    List<KeyValueObject> listKeyValueObject;

    public ListKeyValueObject() {
        listKeyValueObject = new ArrayList<>();
    }

    public ListKeyValueObject(List<KeyValueObject> listKeyValueObject) {
        this.listKeyValueObject = listKeyValueObject;
    }

    public List<KeyValueObject> getEntries() {
        return listKeyValueObject;
    }


    public ListKeyValueObject(Parcel source) {
        listKeyValueObject = new ArrayList<>();
        final int length = source.readInt();
        for (int i = 0; i < length; i++) {
            KeyValueObject keyValueObject = source.readParcelable(KeyValueObject.class.getClassLoader());
            listKeyValueObject.add(keyValueObject);
        }
    }


    public void writeToParcel(Parcel dest, int parcelableFlags) {
        final int length = listKeyValueObject.size();
        dest.writeInt(length);
        if (length > 0) {
            for (KeyValueObject keyValueObject : listKeyValueObject) {
                dest.writeParcelable(keyValueObject, parcelableFlags);
            }
        }
    }

    public int size() {
        return listKeyValueObject.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListKeyValueObject> CREATOR = new Creator<ListKeyValueObject>() {
        public ListKeyValueObject createFromParcel(Parcel source) {
            return new ListKeyValueObject(source);
        }

        public ListKeyValueObject[] newArray(int size) {
            return new ListKeyValueObject[size];
        }
    };
}
