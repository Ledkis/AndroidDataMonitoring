package ledkis.module.androiddatamonitoring.logcat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ledkis.module.androiddatamonitoring.R;

public class LogcatActivity extends Activity {

    public static final String TAG = "LogcatActivity";

    LogcatFragment logcatFragment;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, LogcatActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_logcat);

        if (savedInstanceState == null) {
            logcatFragment = LogcatFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, logcatFragment)
                    .commit();
        }
    }


}
