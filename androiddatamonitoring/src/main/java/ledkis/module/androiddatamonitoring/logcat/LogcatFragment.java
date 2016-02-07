package ledkis.module.androiddatamonitoring.logcat;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ledkis.module.androiddatamonitoring.R;

public class LogcatFragment extends Fragment {

    public static final String TAG = "LogcatFragment";
    public static final String LOGGING_NAME_KEY = "LOGGING_NAME_KEY";
    public static final String LOGGING_FILTER_KEY = "LOGGING_FILTER_KEY";

    static final SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat(
            "MMM d, yyyy HH:mm:ss ZZZZ");
    private static final Executor EX = Executors.newCachedThreadPool();

    private static final int MENU_JUMP_TOP = 11;
    private static final int MENU_JUMP_BOTTOM = 12;

    static final int WINDOW_SIZE = 1000;

    static final int CAT_WHAT = 0;
    static final int CLEAR_WHAT = 2;

    private AlertDialog mFilterDialog;
    static final SimpleDateFormat LOG_FILE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ssZ");

    private LogEntryAdapter mLogEntryAdapter;

    private Level mLastLevel = Level.V;
    private Logcat mLogcat;
    private Prefs mPrefs;
    private String logName;
    private String logFilter;
    private boolean mPlay = true;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // http://stackoverflow.com/questions/10919240/fragment-myfragment-not-attached-to-activity
            if (!isAdded())
                return;
            switch (msg.what) {
                case CAT_WHAT:
                    final List<String> lines = (List<String>) msg.obj;
                    cat(lines);
                    break;
                case CLEAR_WHAT:
                    mLogEntryAdapter.clear();
                    break;
            }
        }
    };

    public LogcatFragment() {
    }

    public static LogcatFragment newInstance() {
        LogcatFragment f = new LogcatFragment();
        return f;
    }

    public static LogcatFragment newInstance(String logName, String logFilter) {
        LogcatFragment f = new LogcatFragment();
        Bundle b = new Bundle();
        b.putString(LOGGING_NAME_KEY, logName);
        b.putString(LOGGING_FILTER_KEY, logFilter);
        f.setArguments(b);
        return f;
    }

    private void jumpTop() {
        pauseLog();
        if (viewHolder.mLogList != null) {
            viewHolder.mLogList.post(new Runnable() {
                public void run() {
                    viewHolder.mLogList.setSelection(0);
                }
            });
        }
    }

    private void jumpBottom() {
        playLog();
        if (viewHolder.mLogList != null) {
            viewHolder.mLogList.setSelection(mLogEntryAdapter.getCount() - 1);
        }
    }

    private void cat(final String s) {
        if (mLogEntryAdapter.getCount() > WINDOW_SIZE) {
            mLogEntryAdapter.remove(0);
        }

        Format format = mLogcat.mFormat;
        Level level = format.getLevel(s);
        if (level == null) {
            level = mLastLevel;
        } else {
            mLastLevel = level;
        }

        final LogEntry entry = new LogEntry(s, level);
        mLogEntryAdapter.add(entry);
    }

    private void cat(List<String> lines) {
        for (String line : lines) {
            cat(line);
        }
        jumpBottom();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = new Prefs(getActivity());

        if (getArguments() != null) {
            logName = getArguments().getString(LOGGING_NAME_KEY);
            logFilter = getArguments().getString(LOGGING_FILTER_KEY);
        } else {
            logName = "LogCat";
            logFilter = "";
        }

    }

    ViewHolder viewHolder;

    public static class ViewHolder {

        View container;
        ListView mLogList;
        View actionbar;
        TextView actionTitleTextView;
        ImageView menuPlayIcon;
        ImageView menuFilterIcon;
        ImageView menuClearIcon;
        ImageView menuShareIcon;
        ImageView menuSaveIcon;

        public ViewHolder(View itemView) {
            container = itemView;
            mLogList = (ListView) itemView.findViewById(R.id.entryListView);
            actionTitleTextView = (TextView) itemView.findViewById(R.id.actionTitleTextView);
            menuPlayIcon = (ImageView) itemView.findViewById(R.id.menuPlayIcon);
            menuFilterIcon = (ImageView) itemView.findViewById(R.id.menuFilterIcon);
            menuClearIcon = (ImageView) itemView.findViewById(R.id.menuClearIcon);
            menuShareIcon = (ImageView) itemView.findViewById(R.id.menuShareIcon);
            menuSaveIcon = (ImageView) itemView.findViewById(R.id.menuSaveIcon);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_logcat, container, false);

        viewHolder = new ViewHolder(rootView);

        // Action bar

        viewHolder.actionTitleTextView.setText(logName);

        setPlayMenu();
        viewHolder.menuPlayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlay) {
                    pauseLog();
                } else {
                    jumpBottom();
                }
            }
        });

        setFilterMenu();
        viewHolder.menuFilterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilterDialog = new FilterDialog(getActivity(), LogcatFragment.this);
                mFilterDialog.show();
            }
        });
        viewHolder.menuClearIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                reset();
            }
        });
        viewHolder.menuShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        viewHolder.menuSaveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        // LogList
        viewHolder.mLogList.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenuInfo menuInfo) {
                MenuItem jumpTopItem = menu.add(0, MENU_JUMP_TOP, 0,
                        R.string.jump_start_menu);
                jumpTopItem.setIcon(android.R.drawable.ic_media_previous);

                MenuItem jumpBottomItem = menu.add(0, MENU_JUMP_BOTTOM, 0,
                        R.string.jump_end_menu);
                jumpBottomItem.setIcon(android.R.drawable.ic_media_next);
            }
        });

        viewHolder.mLogList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //pauseLog();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

        return rootView;
    }

    private void init() {
        mLogEntryAdapter = new LogEntryAdapter(getActivity(), R.layout.logcat_entry, new ArrayList<LogEntry>(WINDOW_SIZE));
        viewHolder.mLogList.setAdapter(mLogEntryAdapter);
        reset();
        setKeepScreenOn();
    }

    @Override
    public void onResume() {
        //Debug.startMethodTracing("alogcat");
        super.onResume();
//        onNewIntent(getIntent());
        init();
        // Log.v("alogcat", "resumed");
    }

    @Override
    public void onPause() {
        super.onPause();
        // Log.v("alogcat", "paused");

        //Debug.stopMethodTracing();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mLogcat != null) {
            mLogcat.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pauseLog();
        // Log.v("alogcat", "destroyed");
    }


    public void reset() {
        mLastLevel = Level.V;

        if (mLogcat != null) {
            mLogcat.stop();
        }

        mPlay = true;

        EX.execute(new Runnable() {
            public void run() {
                if (!"".equals(logFilter))
                    mLogcat = new Logcat(getActivity(), mHandler, logFilter);
                else
                    mLogcat = new Logcat(getActivity(), mHandler);
                mLogcat.start();
            }
        });
    }

    public void setPlayMenu() {
        if (viewHolder == null) {
            return;
        }
        if (mPlay) {
            // mPlayItem.setTitle(R.string.pause_menu);
            viewHolder.menuPlayIcon.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_media_pause));
        } else {
            // mPlayItem.setTitle(R.string.play_menu);
            viewHolder.menuPlayIcon.setImageDrawable(getActivity().getResources().getDrawable(android.R.drawable.ic_media_play));
        }
    }

    void setFilterMenu() {
        if (viewHolder == null) {
            return;
        }
        int filterMenuId;
        String filter = mPrefs.getFilter();
        if (filter == null || filter.length() == 0) {
            filterMenuId = R.string.filter_menu_empty;
        } else {
            filterMenuId = R.string.filter_menu;
        }
        // mFilterItem.setTitle(getResources().getString(filterMenuId, filter));
    }

    private void setKeepScreenOn() {
//        if (mPrefs.isKeepScreenOn()) {
//            getWindow()
//                    .addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        } else {
//            getWindow().clearFlags(
//                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case MENU_JUMP_TOP:
//                Toast.makeText(this, "Jumping to top of log ...",
//                        Toast.LENGTH_SHORT).show();
//                jumpTop();
//                return true;
//            case MENU_JUMP_BOTTOM:
//                Toast.makeText(this, "Jumping to bottom of log ...",
//                        Toast.LENGTH_SHORT).show();
//                jumpBottom();
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
        return super.onContextItemSelected(item);

    }

    private void clear() {
        try {
            Runtime.getRuntime().exec(new String[]{"logcat", "-c"});
        } catch (IOException e) {
            Log.e("alogcat", "error clearing log", e);
        } finally {
        }
    }

    private String dump(boolean html) {
        StringBuilder sb = new StringBuilder();
        Level lastLevel = Level.V;

        // make copy to avoid CME
        List<LogEntry> entries = new ArrayList<LogEntry>(
                mLogEntryAdapter.getEntries());

        for (LogEntry le : entries) {
            if (!html) {
                sb.append(le.getText());
                sb.append('\n');
            } else {
                Level level = le.getLevel();
                if (level == null) {
                    level = lastLevel;
                } else {
                    lastLevel = level;
                }
                sb.append("<font color=\"");
                sb.append(level.getHexColor());
                sb.append("\" face=\"sans-serif\"><b>");
                sb.append(TextUtils.htmlEncode(le.getText()));
                sb.append("</b></font><br/>\n");
            }
        }

        return sb.toString();
    }

    private void share(File file) {
        final Uri uri = Uri.fromFile(file);

        EX.execute(new Runnable() {
            public void run() {
                boolean html = mPrefs.isShareHtml();
                String content = dump(html);

                Intent shareIntent = new Intent(
                        Intent.ACTION_SEND);

                // emailIntent.setType("message/rfc822");
                if (html) {
                    shareIntent.setType("text/html");
                } else {
                    shareIntent.setType("text/plain");
                }

                shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                        "Android Log: " + LOG_DATE_FORMAT.format(new Date()));
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "* Android debug logs");
                shareIntent.putExtra(Intent.EXTRA_EMAIL, "android@leftshift.io");
                startActivity(Intent.createChooser(shareIntent,
                        "Share Android Log ..."));
            }
        });

    }


    private File save() {
        final File path = new File(Environment.getExternalStorageDirectory(),
                "alogcat");
        final File file = new File(path + File.separator + "alogcat."
                + LOG_FILE_FORMAT.format(new Date()) + ".txt");

        // String msg = "saving log to: " + file.toString();
        // Log.d("alogcat", msg);

        EX.execute(new Runnable() {
            public void run() {
                String content = dump(false);

                if (!path.exists()) {
                    path.mkdir();
                }

                BufferedWriter bw = null;
                try {
                    file.createNewFile();
                    bw = new BufferedWriter(new FileWriter(file), 1024);
                    bw.write(content);
                } catch (IOException e) {
                    Log.e("alogcat", "error saving log", e);
                } finally {
                    if (bw != null) {
                        try {
                            bw.close();
                        } catch (IOException e) {
                            Log.e("alogcat", "error closing log", e);
                        }
                    }
                }
                share(file);
            }
        });

        return file;
    }


    private void pauseLog() {
        if (!mPlay) {
            return;
        }

        if (viewHolder != null) {
            viewHolder.actionTitleTextView.setText(getResources().getString(R.string.app_name_paused));
            if (mLogcat != null) {
                mLogcat.setPlay(false);
                mPlay = false;
            }
        }
        setPlayMenu();
    }

    private void playLog() {
        if (mPlay) {
            return;
        }
        if (viewHolder != null) {
            viewHolder.actionTitleTextView.setText(getResources().getString(R.string.app_name));
            if (mLogcat != null) {
                mLogcat.setPlay(true);
                mPlay = true;
            } else {
                reset();
            }
        }
        setPlayMenu();
    }
}
