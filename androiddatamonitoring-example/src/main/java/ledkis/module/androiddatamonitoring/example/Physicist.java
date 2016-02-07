package ledkis.module.androiddatamonitoring.example;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ledkis on 07/02/2016.
 */
public class Physicist {

    public static final String TAG = "Physicist";

    private String name;
    private int age;
    private float phi;
    private DateTime birthday;
    private Object object;
    private List<Object> things;

    public Physicist(String name, int age, float phi, DateTime birthday, Object object, List<Object> things) {
        this.name = name;
        this.age = age;
        this.phi = phi;
        this.birthday = birthday;
        this.object = object;
        this.things = things;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public float getPhi() {
        return phi;
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

    @Override
    public String toString() {
        return name;
    }
}
