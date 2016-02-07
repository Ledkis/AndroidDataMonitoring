package ledkis.module.androiddatamonitoring;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import ledkis.module.androiddatamonitoring.fragment.KeyValueFragment;
import ledkis.module.androiddatamonitoring.fragment.ListKeyValueFragment;
import ledkis.module.androiddatamonitoring.logcat.LogcatFragment;
import ledkis.module.androiddatamonitoring.model.KeyValueObject;
import ledkis.module.androiddatamonitoring.model.ListKeyValueObject;

public class MonitoringActivity extends Activity {

    public static final String TAG = "MonitoringActivity";

    public static final String KEY_VALUE_OBJECT = "KEY_VALUE_OBJECT";
    public static final String LIST_KEY_VALUE_OBJECT = "LIST_KEY_VALUE_OBJECT";
    public static final String LOGGING_OBJECT = "LOGGING_OBJECT";

    private ViewPager mPager;
    MonitoringPagerAdapter monitoringPagerAdapter;

    public static Intent getIntent(Context context, ArrayList<KeyValueObject> keyValueObjects, ArrayList<ListKeyValueObject> listKeyValueObjects, KeyValueObject loggingObjects) {
        Intent intent = new Intent(context, MonitoringActivity.class);

        if (null != keyValueObjects && keyValueObjects.size() != 0)
            intent.putParcelableArrayListExtra(KEY_VALUE_OBJECT, keyValueObjects);

        if (null != listKeyValueObjects && listKeyValueObjects.size() != 0)
            intent.putParcelableArrayListExtra(LIST_KEY_VALUE_OBJECT, listKeyValueObjects);

        if (null != loggingObjects && loggingObjects.size() != 0)
            intent.putExtra(LOGGING_OBJECT, loggingObjects);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_monitoring);

        // http://stackoverflow.com/questions/5621132/passing-custom-objects-between-activities
        Bundle extras = getIntent().getExtras();
        Intent intentIncoming = getIntent();
        if (extras != null) {

            ArrayList<KeyValueObject> keyValueObjects = intentIncoming.getParcelableArrayListExtra(KEY_VALUE_OBJECT);
            ArrayList<ListKeyValueObject> listKeyValueObjects = intentIncoming.getParcelableArrayListExtra(LIST_KEY_VALUE_OBJECT);
            KeyValueObject loggingObjects = intentIncoming.getParcelableExtra(LOGGING_OBJECT);

            monitoringPagerAdapter = new MonitoringPagerAdapter(super.getFragmentManager(), keyValueObjects, listKeyValueObjects, loggingObjects);

            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setAdapter(this.monitoringPagerAdapter);
            mPager.setOffscreenPageLimit(0);
            mPager.setCurrentItem(0);
            mPager.setPageMargin(10);
        }
    }

    public class MonitoringPagerAdapter extends FragmentPagerAdapter {

        ArrayList<KeyValueObject> keyValueObjects;
        ArrayList<ListKeyValueObject> listKeyValueObjects;
        KeyValueObject loggingObjects;

        public MonitoringPagerAdapter(FragmentManager fragmentManager, ArrayList<KeyValueObject> keyValueObjects, ArrayList<ListKeyValueObject> listKeyValueObjects, KeyValueObject loggingObjects) {
            super(fragmentManager);

            this.keyValueObjects = keyValueObjects != null ? keyValueObjects : new ArrayList<KeyValueObject>();
            this.listKeyValueObjects = listKeyValueObjects != null ? listKeyValueObjects : new ArrayList<ListKeyValueObject>();
            this.loggingObjects = loggingObjects != null ? loggingObjects : new KeyValueObject();
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return keyValueObjects.size() + listKeyValueObjects.size() + loggingObjects.size();
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            if (position < keyValueObjects.size())
                return KeyValueFragment.newInstance(keyValueObjects.get(position));
            else if (position < keyValueObjects.size() + listKeyValueObjects.size()) {
                return ListKeyValueFragment.newInstance(listKeyValueObjects.get(position - keyValueObjects.size()));
            } else {
                int pos = position - listKeyValueObjects.size() - keyValueObjects.size();
                String logName = loggingObjects.getEntries().get(pos).getKey();
                String logFilter = loggingObjects.getEntries().get(pos).getValue();
                return LogcatFragment.newInstance(logName, logFilter);
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

}
