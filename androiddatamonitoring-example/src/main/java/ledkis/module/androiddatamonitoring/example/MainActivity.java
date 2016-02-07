package ledkis.module.androiddatamonitoring.example;

import android.app.Activity;
import android.os.Bundle;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Object barilDePoudre = new Object();
        Object modelBohr = new Object();
        Object cat = new Object();

        List<Object> albertThings = new ArrayList<>();
        albertThings.add("e=mc2");
        albertThings.add("Dieu ne joue pas au dés");
        albertThings.add(1905);

        Physicist albert = new Physicist("albert", 76, 299792458f, new DateTime(1879, 3, 14, 0, 0), barilDePoudre, albertThings);

        List<Object> nielsThings = new ArrayList<>();
        nielsThings.add("qui êtes vous pour dire à Dieu se qu'il doit faire");
        nielsThings.add(1922);

        Physicist niels = new Physicist("niels", 77, 9.109E-31f, new DateTime(1885, 10, 7, 0, 0), modelBohr, nielsThings);

        List<Object> erwinThings = new ArrayList<>();
        erwinThings.add("Anny");
        erwinThings.add(1933);

        Physicist erwin = new Physicist("erwin", 73, 6.62606957E-34f, new DateTime(1887, 8, 12, 0, 0), cat, erwinThings);


        List<Physicist> physicists = new ArrayList<>();
        physicists.add(albert);
        physicists.add(niels);
        physicists.add(erwin);

        MonitoringUtils.startMonitoringActivity(this, albert, physicists, new String[]{"ledkis.module.picturecomparator.example", ""});
    }
}
