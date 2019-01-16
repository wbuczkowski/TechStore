package com.nestle.tp.techstore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

public abstract class AppActivity extends AppCompatActivity implements LogoutTimerUtility.LogOutListener {

    static final String EXTRA_OPTION = "OPTION";
    static final String EXTRA_WORK_ORDER = "WORK_ORDER";
    static final String EXTRA_COST_CENTER = "COST_CENTER";
    static final String EXTRA_MATERIAL = "MATERIAL";
    static final String EXTRA_PLANT = "PLANT";
    static final String EXTRA_STORAGE_LOCATION = "STORAGE_LOCATION";
    static final String EXTRA_BIN = "BIN";
    static final String EXTRA_QUANTITY = "QUANTITY";
    static final String EXTRA_DATE = "DATE";
    static final String EXTRA_INVENTORY = "INVENTORY";
    static final String EXTRA_VENDOR = "VENDOR";

    static final String OPTION_GOODS_ISSUE = "1";
    static final String OPTION_GOODS_RETURN = "2";
    static final String OPTION_INVENTORY_WITH_DOCUMENT = "4";
    static final String OPTION_INVENTORY_WO_DOCUMENT = "3";

    private static final String ACTION = "com.symbol.datawedge.api.ACTION";
    private static final String SOFT_SCAN_TRIGGER = "com.symbol.datawedge.api.SOFT_SCAN_TRIGGER";
    private static final String START_SCANNING = "START_SCANNING";

    private static final int RC_BARCODE_CAPTURE_GOOGLE_VISION = 9001;
    private static final int RC_BARCODE_CAPTURE_ZBAR_LIB = 9002;
    private static final int RC_BARCODE_CAPTURE_ZXING_LIB = 9003;

    //    static final String KEY_PREF_USE_DATAWEDGE = "pref_use_datawedge";
//    static final String KEY_PREF_USE_CAMERA = "pref_use_camera";
    private static final String KEY_PREF_USE_AUTO_FOCUS = "pref_use_auto_focus";
    private static final String KEY_PREF_USE_FLASH = "pref_use_flash";

    private static final String KEY_PREF_TIMEOUT = "pref_timeout";

    private static final String VALUE_PREF_TIMEOUT_DEFAULT = "300000";

    private static final String KEY_PREF_SCAN_TECHNOLOGY = "pref_scan_technology";

    private static final String VALUE_PREF_SCAN_NONE = "";
    private static final String VALUE_PREF_SCAN_DATAWEDGE = "DataWedge";
    private static final String VALUE_PREF_SCAN_ZXING = "ZXing";
    private static final String VALUE_PREF_SCAN_GOOGLEVISION = "GoogleVision";
    private static final String VALUE_PREF_SCAN_ZBAR_LIB = "ZBarLib";
    private static final String VALUE_PREF_SCAN_ZXING_LIB = "ZXingLib";

    //    private boolean useDataWedge = false;
//    private boolean useCamera = false;
    private boolean useAutoFocus = false;
    private boolean useFlash = false;
    private int logoutTime = 300000;

    private String scanTech;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(fabListener);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        // read preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        // useDataWedge = sharedPref.getBoolean(KEY_PREF_USE_DATAWEDGE, true);
        scanTech = String.valueOf(sharedPref.getString(KEY_PREF_SCAN_TECHNOLOGY, VALUE_PREF_SCAN_NONE));
        if (scanTech.equals(VALUE_PREF_SCAN_NONE)) {
            Snackbar.make(findViewById(R.id.fab),
                    "Please select scanning technology!", Snackbar.LENGTH_LONG)
                    .setAction(R.string.action_settings, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent;
                            intent = new Intent(view.getContext(),
                                    SettingsActivity.class);
                            intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT,
                                    SettingsActivity.ScannerPreferenceFragment.class.getName());
                            intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
        //if (useDataWedge) {
        else if (scanTech.equals(VALUE_PREF_SCAN_DATAWEDGE)) {
            //Register for the intent to receiev the scanned data using intent callabck.
            //The action and category name used must be same as the names usied in the profile creation.
            IntentFilter filter = new IntentFilter();
            filter.addAction(getPackageName() + ".SCAN");
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            registerReceiver(broadcastReceiver, filter);
        }
//        useCamera = sharedPref.getBoolean(KEY_PREF_USE_CAMERA, !useDataWedge);
        useAutoFocus = sharedPref.getBoolean(KEY_PREF_USE_AUTO_FOCUS, false);
        useFlash = sharedPref.getBoolean(KEY_PREF_USE_FLASH, false);
        logoutTime = Integer.parseInt(String.valueOf(sharedPref.getString(KEY_PREF_TIMEOUT, VALUE_PREF_TIMEOUT_DEFAULT)));
        LogoutTimerUtility.startLogoutTimer(this, this, logoutTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Unregister the intent reciever when the app goes to background.
        if ( /*useDataWedge*/ scanTech.equals(VALUE_PREF_SCAN_DATAWEDGE)) {
            unregisterReceiver(broadcastReceiver);
        }
        LogoutTimerUtility.stopLogoutTimer();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        LogoutTimerUtility.startLogoutTimer(this, this, logoutTime);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_settings) {
            // launch settings activity
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param intent      An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case RC_BARCODE_CAPTURE_GOOGLE_VISION:
                //Google Vision
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    if (intent != null) {
                        Barcode barcode = intent.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
//                        Snackbar.make(findViewById(R.id.fab),
//                                R.string.barcode_success + "\n" + barcode.displayValue,
//                                Snackbar.LENGTH_LONG).show();
                        processBarcode(barcode.displayValue);
                    } else {
                        Snackbar.make(findViewById(R.id.fab), R.string.barcode_failure,
                                Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(findViewById(R.id.fab), String.format(getString(R.string.barcode_error),
                            CommonStatusCodes.getStatusCodeString(resultCode)),
                            Snackbar.LENGTH_LONG).show();
                }
                break;
            case RC_BARCODE_CAPTURE_ZBAR_LIB:
            case RC_BARCODE_CAPTURE_ZXING_LIB:
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    if (intent != null) {
                        String barcode = intent.getStringExtra("Contents");
//                        Snackbar.make(findViewById(R.id.fab),
//                                R.string.barcode_success + "\n" + barcode,
//                                Snackbar.LENGTH_LONG).show();
                        processBarcode(barcode);
                    } else {
                        Snackbar.make(findViewById(R.id.fab), R.string.barcode_failure,
                                Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(findViewById(R.id.fab), String.format(getString(R.string.barcode_error),
                            CommonStatusCodes.getStatusCodeString(resultCode)),
                            Snackbar.LENGTH_LONG).show();
                }
                break;
            case IntentIntegrator.REQUEST_CODE:
                //ZXing
                if (resultCode == CommonStatusCodes.SUCCESS
                        || resultCode == CommonStatusCodes.SUCCESS_CACHE) {
                    if (intent != null) {
                        //retrieve scan result
                        IntentResult scanningResult =
                                IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                        if (scanningResult != null) {
                            //we have a result
                            processBarcode(scanningResult.getContents());
                        } else {
                            Snackbar.make(findViewById(R.id.fab), R.string.barcode_failure,
                                    Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Snackbar.make(findViewById(R.id.fab), R.string.barcode_failure,
                                Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(findViewById(R.id.fab), String.format(getString(R.string.barcode_error),
                            CommonStatusCodes.getStatusCodeString(resultCode)),
                            Snackbar.LENGTH_LONG).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    final View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (scanTech) {
                case VALUE_PREF_SCAN_DATAWEDGE:
                    // start DataWedge soft-scanning
                    intent = new Intent();
                    intent.setAction(ACTION);
                    intent.putExtra(SOFT_SCAN_TRIGGER, START_SCANNING);
                    if (findDataWedgePackage(intent)) {
                        sendBroadcast(intent);
                    } else {
                        Snackbar.make(view, "DataWedge is not installed.\n"
                                + "Please select another scanning technology", Snackbar.LENGTH_LONG)
                                .setAction(R.string.action_settings, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent;
                                        intent = new Intent(AppActivity.this,
                                                SettingsActivity.class);
                                        intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT,
                                                SettingsActivity.ScannerPreferenceFragment.class.getName());
                                        intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }
                    break;
                case VALUE_PREF_SCAN_ZXING:
                    IntentIntegrator scanIntegrator = new IntentIntegrator(AppActivity.this);
                    scanIntegrator.initiateScan();
                    break;
                case VALUE_PREF_SCAN_GOOGLEVISION:
                    // launch barcode capture activity
                    intent = new Intent(AppActivity.this, BarcodeCaptureActivity.class);
                    intent.putExtra(BarcodeCaptureActivity.AutoFocus, useAutoFocus);
                    intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash);
                    startActivityForResult(intent, RC_BARCODE_CAPTURE_GOOGLE_VISION);
                    break;
                case VALUE_PREF_SCAN_ZBAR_LIB:
                    intent = new Intent(AppActivity.this, ZBarFullScannerActivity.class);
                    startActivityForResult(intent, RC_BARCODE_CAPTURE_ZBAR_LIB);
                    break;
                case VALUE_PREF_SCAN_ZXING_LIB:
                    intent = new Intent(AppActivity.this, ZXingFullScannerActivity.class);
                    startActivityForResult(intent, RC_BARCODE_CAPTURE_ZXING_LIB);
                    break;
                case VALUE_PREF_SCAN_NONE:
                    Snackbar.make(view, "Please select scanning technology", Snackbar.LENGTH_LONG)
                            .setAction(R.string.action_settings, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent;
                                    intent = new Intent(view.getContext(),
                                            SettingsActivity.class);
                                    intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT,
                                            SettingsActivity.ScannerPreferenceFragment.class.getName());
                                    intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
                                    startActivity(intent);
                                }
                            })
                            .show();
            }
        }
    };

    private boolean findDataWedgePackage(Intent intent) {
        PackageManager pm = getPackageManager();
        List<ResolveInfo> availableApps = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (availableApps != null) {
            for (ResolveInfo resolveInfo : availableApps) {
                if (resolveInfo.resolvePackageName.equals("com.symbol.datawedge")) {
                    return true;
                }
            }
        }
        return false;
    }

    //Broadcast Receiver for receiving the intents back from DataWedge
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        final String LABEL_TYPE_TAG = "com.symbol.datawedge.label_type";
        final String DATA_STRING_TAG = "com.symbol.datawedge.data_string";
        //  final String DECODE_DATA_TAG = "com.symbol.datawedge.decode_data";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(getPackageName() + ".SCAN")) {
                String labelType = intent.getStringExtra(LABEL_TYPE_TAG);
                String decodeString = intent.getStringExtra(DATA_STRING_TAG);
                Snackbar.make(findViewById(R.id.fab), R.string.barcode_success +
                                "\nType:\t" + labelType +
                                "\nValue:\t" + decodeString,
                        Snackbar.LENGTH_LONG).show();
                processBarcode(decodeString);
            }
        }
    };

    /**
     * Performing idle time logout
     */
    @Override
    public void doLogout() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    protected abstract void processBarcode(String data);
}
