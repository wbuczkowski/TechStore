package com.nestle.tp.techstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppActivity {

//    static final String SOFT_SCAN_TRIGGER = "com.symbol.datawedge.api.SOFT_SCAN_TRIGGER";
//    static final String START_SCANNING = "START_SCANNING";

    static final String KEY_PREF_ENABLE_DATAWEDGE = "pref_enable_datawedge";
    static final String KEY_PREF_USE_DATAWEDGE = "pref_use_datawedge";
    static final String KEY_PREF_ENABLE_CAMERA = "pref_enable_camera";
    static final String KEY_PREF_USE_CAMERA = "pref_use_camera";
//    static final String KEY_PREF_USE_AUTO_FOCUS = "pref_use_auto_focus";
//    static final String KEY_PREF_USE_FLASH = "pref_use_flash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(fabListener);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view ){
                EditText editText = findViewById(R.id.editUserName);
                String data = editText.getText().toString();
                if (Character.isLetter(data.charAt(0)) && Character.isLetter(data.charAt(1))) {
                    // first two characters are letters
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, data);
                    startActivity(intent);
                } else {
                    Snackbar.make(findViewById(R.id.fab),
                            "Not a user ID!",
                            Snackbar.LENGTH_LONG).show();
                }

            }
        });

        initDataWedge();
        initCamera();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    private void initDataWedge() {
        //All the DataWedge version does not support creating the profile using the DataWedge intent API.
        //To avoid crashes on the device, make sure to check the DtaaWedge version before creating the profile.
        final String DW_PKG_NAME = "com.symbol.datawedge";
        final String DW_INTENT_SUPPORT_VERSION = "6.3";

        int result = -1;
        // Find out current DW version, if the version is 6.3 or higher then we know it support intent config
        // Then we can send CartScan profile via intent
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(DW_PKG_NAME, PackageManager.GET_META_DATA);
            String versionCurrent = pInfo.versionName;
//            Log.i(TAG, "createProfileInDW: versionCurrent=" + versionCurrent);
            if (versionCurrent != null)
                result = compareVersion(versionCurrent, DW_INTENT_SUPPORT_VERSION);
//            Log.i(TAG, "onCreate: result=" + result);
        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e(TAG, "onCreate: NameNotFoundException:", e1);
        }
        if (result >= 0) {
            createDataWedgeProfile();
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(KEY_PREF_ENABLE_DATAWEDGE, true);
//            editor.putBoolean(KEY_PREF_USE_DATAWEDGE, true);
            editor.apply();
        } else {
//            dataTextView.append("DataWedge version is " + versionCurrent + ", " +
//                    "but the current Sample is only supported with DataWedge version 6.3 or greater.");
            // Disable DataWedge in preferences
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(KEY_PREF_ENABLE_DATAWEDGE, false);
            editor.putBoolean(KEY_PREF_USE_DATAWEDGE, false);
            editor.apply();
        }
    }

    private static int compareVersion(String v1, String v2) {
        List<String> l1 = new ArrayList<>(Arrays.asList(
                v1.replaceAll("\\s", "").split("\\.")));
        List<String> l2 = new ArrayList<>(Arrays.asList(
                v2.replaceAll("\\s", "").split("\\.")));
        int i = 0;
        while (i < l1.size() && i < l2.size() && l1.get(i).equals(l2.get(i))) {
            i++;
        }
        if (i < l1.size() && i < l2.size()) {
            int diff = Integer.valueOf(l1.get(i)).compareTo(Integer.valueOf(l2.get(i)));
            return Integer.signum(diff);
        } else {
            return Integer.signum(l1.size() - l2.size());
        }
    }

    private void initCamera() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(KEY_PREF_ENABLE_CAMERA, true);
//            editor.putBoolean(KEY_PREF_USE_CAMERA, false);
            editor.apply();
        } else {
            // no camera on this device
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(KEY_PREF_ENABLE_CAMERA, false);
            editor.putBoolean(KEY_PREF_USE_CAMERA, false);
            editor.apply();
        }
    }

    /**
     * This code demonstrates how to create the DataWedge programatically and modify the settings.
     * This code can be skipped if the profile is created on the DataWedge manaually and pushed to different device though MDM
     */
    public void createDataWedgeProfile() {
        //Create profile if doesn't exit and update the required settings
        final String ACTION = "com.symbol.datawedge.api.ACTION";
        final String SET_CONFIG = "com.symbol.datawedge.api.SET_CONFIG";
//        final String CREATE_PROFILE = "com.symbol.datawedge.api.CREATE_PROFILE";
//        final String SWITCH = "com.symbol.datawedge.api.SWITCH_TO_PROFILE";
        String packageName = getPackageName();
        String profileName = getString(R.string.app_name) + "App";
        {
            Bundle bConfig = new Bundle();
            Bundle bParams = new Bundle();
            Bundle configBundle = new Bundle();
            Bundle bundleApp1 = new Bundle();

            bParams.putString("scanner_selection", "auto");
            bParams.putString("intent_output_enabled", "true");
            bParams.putString("intent_action", packageName + ".SCAN");
            bParams.putString("intent_category", Intent.CATEGORY_DEFAULT);
            bParams.putString("intent_delivery", "2");

            configBundle.putString("PROFILE_NAME", profileName);
            configBundle.putString("PROFILE_ENABLED", "true");
            configBundle.putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST");

            bundleApp1.putString("PACKAGE_NAME", packageName);
            bundleApp1.putStringArray("ACTIVITY_LIST", new String[]{packageName + ".SCAN"});

            configBundle.putParcelableArray("APP_LIST", new Bundle[]{bundleApp1});

            bConfig.putString("PLUGIN_NAME", "INTENT");
            bConfig.putString("RESET_CONFIG", "false");

            bConfig.putBundle("PARAM_LIST", bParams);
            configBundle.putBundle("PLUGIN_CONFIG", bConfig);

            Intent i = new Intent();
            i.setAction(ACTION);
            i.putExtra(SET_CONFIG, configBundle);
            this.sendBroadcast(i);
        }

        //TO recieve the scanned via intent, the keystroke must disabled.
        {
            Bundle bConfig = new Bundle();
            Bundle bParams = new Bundle();
            Bundle configBundle = new Bundle();

            bParams.putString("keystroke_output_enabled", "false");

            configBundle.putString("PROFILE_NAME", profileName);
            configBundle.putString("PROFILE_ENABLED", "true");
            configBundle.putString("CONFIG_MODE", "UPDATE");

            bConfig.putString("PLUGIN_NAME", "KEYSTROKE");
            bConfig.putString("RESET_CONFIG", "false");

            bConfig.putBundle("PARAM_LIST", bParams);
            configBundle.putBundle("PLUGIN_CONFIG", bConfig);

            Intent i = new Intent();
            i.setAction(ACTION);
            i.putExtra(SET_CONFIG, configBundle);
            this.sendBroadcast(i);
        }
    }

    @Override
    public void processBarcode(String data) {
        if (data.matches("[a-zA-Z]{3}.*")) {
            if (data.equals("GETMEOUT")) {
                finishAffinity();
                System.exit(0);
            }
            // first three characters are letters
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, data);
            startActivity(intent);
        } else {
            Snackbar.make(findViewById(R.id.fab),
                    "Not a user ID!",
                    Snackbar.LENGTH_LONG).show();
        }
    }
}
