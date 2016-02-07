package ledkis.module.androiddatamonitoring.example;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by ledkis on 07/02/2016.
 */
public class User {

    public static final String TAG = "User";

    private String name;
    private int age;
    private float weight;
    private DateTime birthday;
    private Object object;
    private List<Object> things;

    public User() {
        name = "albert";
        age = 50;
        weight = 43.2f;
        birthday = DateTime.now();
        object = new Object();

        things = new ArrayList<>();
        things.add("car");
        things.add(13);
        things.add(3.14f);
        things.add(DateTime.now());
        things.add(new Object());
    }

    public static String getTAG() {
        return TAG;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public float getWeight() {
        return weight;
    }

    public DateTime getBirthday() {
        return birthday;
    }

    public Object getObject() {
        return object;
    }

    public List<Object> getThings() {
        return things;
    }
}
