package ledkis.monitoring.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ledkis.monitoring.MonitoringActivity;
import ledkis.monitoring.R;

public class ParseControlFragmentDebug extends Fragment {

    String userId;

    public ParseControlFragmentDebug() {
    }

    public static ParseControlFragmentDebug newInstance(String userId) {
        ParseControlFragmentDebug f = new ParseControlFragmentDebug();
        Bundle b = new Bundle();
        b.putString(MonitoringActivity.USER_ID, userId);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            userId = getArguments().getString(MonitoringActivity.USER_ID);

            userId = userId != null ? userId : "";

            // Already initialized in MainApp
//            Parse.initialize(getActivity(), parseAppId, parseClientId);
        } else {
            userId = "";
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_parse_control_debug, container, false);

        Button deleteUserButton = (Button) rootView.findViewById(R.id.deleteUserButton);
        Button deleteAllUsersButton = (Button) rootView.findViewById(R.id.deleteAllUsersButton);
        Button deleteShouldIsButton = (Button) rootView.findViewById(R.id.deleteShouldIsButton);

        // the parse delete function does not exist anymore
//        deleteUserButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case DialogInterface.BUTTON_POSITIVE:
//                                if (userId.length() == 0) {
//                                    Toast.makeText(getActivity(), "Can't delete user : userId is null", Toast.LENGTH_LONG).show();
//                                    break;
//                                }
//
//                                HashMap<String, Object> params = new HashMap<>();
//                                params.put("userId", userId);
//
//                                ParseCloud.callFunctionInBackground("deleteUsers", params, new FunctionCallback<Object>() {
//                                    @Override
//                                    public void done(Object o, ParseException e) {
//                                        if (e != null) {
//                                            Toast.makeText(getActivity(), "Error while delete Current user: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                });
//                                break;
//
//                            case DialogInterface.BUTTON_NEGATIVE:
//                                break;
//                        }
//                    }
//                };
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setMessage("Delete Current user")
//                        .setPositiveButton("YES", dialogClickListener)
//                        .setNegativeButton("NO", dialogClickListener)
//                        .setTitle("Are you sure MOTHA FUCKERS ?")
//                        .show();
//            }
//        });
//
//        deleteAllUsersButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case DialogInterface.BUTTON_POSITIVE:
//                                HashMap<String, Object> params = new HashMap<>();
//                                params.put("userId", "");
//
//                                ParseCloud.callFunctionInBackground("deleteUsers", params, new FunctionCallback<Object>() {
//                                    @Override
//                                    public void done(Object o, ParseException e) {
//                                        if (e != null) {
//                                            Toast.makeText(getActivity(), "Error while delete All users: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                });
//                                break;
//
//                            case DialogInterface.BUTTON_NEGATIVE:
//                                break;
//                        }
//                    }
//                };
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setMessage("Delete All users")
//                        .setPositiveButton("YES", dialogClickListener)
//                        .setNegativeButton("NO", dialogClickListener)
//                        .setTitle("Are you sure MOTHA FUCKERS ?")
//                        .show();
//            }
//        });
//
//        deleteShouldIsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case DialogInterface.BUTTON_POSITIVE:
//                                HashMap<String, Object> params = new HashMap<>();
//
//                                ParseCloud.callFunctionInBackground("deleteAllShouldIs", params, new FunctionCallback<Object>() {
//                                    @Override
//                                    public void done(Object o, ParseException e) {
//                                        if (e != null) {
//                                            Toast.makeText(getActivity(), "Error while delete all shouldIs: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                });
//                                break;
//
//                            case DialogInterface.BUTTON_NEGATIVE:
//                                break;
//                        }
//                    }
//                };
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setMessage("Delete all shouldIs")
//                        .setPositiveButton("YES", dialogClickListener)
//                        .setNegativeButton("NO", dialogClickListener)
//                        .setTitle("Are you sure MOTHA FUCKERS ?")
//                        .show();
//            }
//        });

        return rootView;

    }

}
