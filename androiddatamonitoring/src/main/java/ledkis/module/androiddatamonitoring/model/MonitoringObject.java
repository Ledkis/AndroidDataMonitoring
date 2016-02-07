package ledkis.module.androiddatamonitoring.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * TODO not used
 * stackoverflow.com/questions/6061745/how-to-force-a-generic-type-parameter-to-be-an-interface
 */

public class MonitoringObject<V extends Parcelable> implements Parcelable {

    public static final int LOG_TYPE = 1;
    public static final int MAP_TYPE = 2;

    private int type;
    private V value;

    public MonitoringObject(int type, V value) {
        this.type = type;
        this.value = value;
    }

    /**
     * http://stackoverflow.com/questions/18255117/how-do-i-get-the-class-attribute-from-a-generic-type-parameter
     */
    private MonitoringObject(Parcel in, Class<V> cls) {
        this.type = in.readInt();
        this.value = in.readParcelable(cls.getClassLoader());
    }

    public int getType() {
        return type;
    }

    public V getValue() {
        return value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeParcelable(this.value, flags);
    }

//    public static Parcelable.Creator<MonitoringObject<?>> CREATOR = new Parcelable.Creator<MonitoringObject>() {
//        public MonitoringObject createFromParcel(Parcel source) {
//            return new MonitoringObject(cls);
//        }
//
//
//
//        public MonitoringObject[] newArray(int size) {
//            return new MonitoringObject[size];
//        }
//    };
//
//    public static String getLogFilter(MonitoringObject monitoringObject) throws ClassCastException {
//        if(monitoringObject.getType() == LOG_TYPE){
//            return (String) monitoringObject.getValue();
//        } else {
//            return null;
//        }
//    }
//
//    public static List<Map.Entry<String, Object>> getMapEntries(MonitoringObject monitoringObject) throws ClassCastException {
//        if(monitoringObject.getType() == MAP_TYPE ){
//            try {
//                return (List<Map.Entry<String, Object>>) monitoringObject.getValue();
//            } catch (ClassCastException e) {
//                throw e;
//            }
//        } else {
//            return null;
//        }
//    }


}
