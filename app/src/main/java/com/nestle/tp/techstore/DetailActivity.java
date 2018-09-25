package com.nestle.tp.techstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppActivity {

    public static final String EXTRA_OPTION = "OPTION";
    public static final String EXTRA_WORK_ORDER = "WORK_ORDER";
    public static final String EXTRA_COST_CENTER = "COST_CENTER";
    public static final String EXTRA_MATERIAL = "MATERIAL";
    public static final String EXTRA_PLANT = "PLANT";
    public static final String EXTRA_STORAGE_LOCATION = "STORAGE_LOCATION";
    public static final String EXTRA_BIN = "BIN";
    public static final String EXTRA_QUANTITY = "QUANTITY";
    public static final String EXTRA_DATE = "DATE";
    public static final String EXTRA_INVENTORY = "INVENTORY";
    public static final String EXTRA_VENDOR = "VENDOR";

    public static final String OPTION_GOODS_ISSUE = "1";
    public static final String OPTION_GOODS_RETURN = "2";
    public static final String OPTION_INVENTORY_WO_DOCUMENT = "3";
    public static final String OPTION_INVENTORY_WITH_DOCUMENT = "4";

    private String mOption;
    private EditText mWorkOrder;
    private EditText mCostCenter;
    private EditText mMaterial;
    private EditText mPlant;
    private EditText mStorageLocation;
    private EditText mBin;
    private EditText mQuantity;
    private EditText mInventory;
    private EditText mVendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(fabListener);
        // get edit text controls
        mWorkOrder = findViewById(R.id.editWorkOrder);
        mCostCenter = findViewById(R.id.editCostCenter);
        mMaterial = findViewById(R.id.editMaterial);
        mPlant = findViewById(R.id.editPlant);
        mStorageLocation = findViewById(R.id.editStorageLocation);
        mBin = findViewById(R.id.editBin);
        mQuantity = findViewById(R.id.editQuantity);
        mInventory = findViewById(R.id.editInventory);
        mVendor = findViewById(R.id.editVendor);
        // load last used values first
        if (savedInstanceState != null) {
            mWorkOrder.setText(savedInstanceState.getString(EXTRA_WORK_ORDER, ""));
            mCostCenter.setText(savedInstanceState.getString(EXTRA_COST_CENTER, ""));
            mPlant.setText(savedInstanceState.getString(EXTRA_PLANT, ""));
            mStorageLocation.setText(savedInstanceState.getString(EXTRA_STORAGE_LOCATION, ""));
            mInventory.setText(savedInstanceState.getString(EXTRA_INVENTORY, ""));
            mVendor.setText(savedInstanceState.getString(EXTRA_VENDOR, ""));
        }
        // replace with the values from intent, if provided
        Intent intent = getIntent();
        String data;
        data = intent.getStringExtra(EXTRA_OPTION);
        mOption = (data.isEmpty()) ? OPTION_GOODS_ISSUE : data;
        data = intent.getStringExtra(EXTRA_WORK_ORDER);
        if (data != null) if (!data.isEmpty()) mWorkOrder.setText(data);
        data = intent.getStringExtra(EXTRA_COST_CENTER);
        if (data != null) if (!data.isEmpty()) mCostCenter.setText(data);
        data = intent.getStringExtra(EXTRA_MATERIAL);
        if (data != null) if (!data.isEmpty()) mCostCenter.setText(data);
        data = intent.getStringExtra(EXTRA_PLANT);
        if (data != null) if (!data.isEmpty()) mPlant.setText(data);
        data = intent.getStringExtra(EXTRA_STORAGE_LOCATION);
        if (data != null) if (!data.isEmpty()) mStorageLocation.setText(data);
        data = intent.getStringExtra(EXTRA_BIN);
        if (data != null) if (!data.isEmpty()) mBin.setText(data);
        data = intent.getStringExtra(EXTRA_INVENTORY);
        if (data != null) if (!data.isEmpty()) mInventory.setText(data);
        data = intent.getStringExtra(EXTRA_VENDOR);
        if (data != null) if (!data.isEmpty()) mVendor.setText(data);
        // get defaults from preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (mPlant.length() == 0)
            mPlant.setText(sharedPref.getString("pref_default_plant", ""));
        if (mStorageLocation.length() == 0)
            mStorageLocation.setText(sharedPref.getString("pref_default_storage_location", ""));
        // TODO: initialize fields labels, visibility and clear unused
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_WORK_ORDER, mWorkOrder.getText().toString());
        outState.putString(EXTRA_COST_CENTER, mCostCenter.getText().toString());
        outState.putString(EXTRA_PLANT, mPlant.getText().toString());
        outState.putString(EXTRA_STORAGE_LOCATION, mStorageLocation.getText().toString());
        outState.putString(EXTRA_INVENTORY, mInventory.getText().toString());
        outState.putString(EXTRA_VENDOR, mVendor.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_save:
                if (validateData()) {
                    Intent data = new Intent();
                    data.putExtra(EXTRA_OPTION, mOption);
                    data.putExtra(EXTRA_WORK_ORDER, mWorkOrder.getText().toString());
                    data.putExtra(EXTRA_COST_CENTER, mCostCenter.getText().toString());
                    data.putExtra(EXTRA_MATERIAL, mMaterial.getText().toString());
                    data.putExtra(EXTRA_PLANT, mPlant.getText().toString());
                    data.putExtra(EXTRA_STORAGE_LOCATION, mStorageLocation.getText().toString());
                    data.putExtra(EXTRA_BIN, mBin.getText().toString());
                    // TODO: manage format conversion for quantity as string
                    data.putExtra(EXTRA_QUANTITY, mQuantity.getText().toString());
                    SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.US);
                    data.putExtra(EXTRA_DATE, ft.format(new Date()));
                    data.putExtra(EXTRA_INVENTORY, mInventory.getText().toString());
                    data.putExtra(EXTRA_VENDOR, mVendor.getText().toString());
                    setResult(CommonStatusCodes.SUCCESS, data);
                    finish();
                } else {
                    Snackbar.make(findViewById(R.id.fab),
                            "Please complete the data",
                            Snackbar.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processBarcode(String data) {
        String[] splitData = data.split(" ");
        boolean unknown = true;
        if (splitData[0].matches("\\d+")) {
            // starts with a digit
            switch (splitData[0].length()) {
                case 10:
                    // this is a work order
                    unknown = false;
                    mWorkOrder.setText(splitData[0]);
                    break;
                case 9:
                    // this is a material
                    unknown = false;
                    mMaterial.setText(splitData[0]);
                    if (splitData.length > 1) {
                        mPlant.setText(splitData[1]);
                        if (splitData.length > 2) {
                            mStorageLocation.setText(splitData[2]);
                            if (splitData.length > 3) {
                                mBin.setText(splitData[3]);
                            }
                        }
                    }
            }
        } else if (splitData[0].matches("E\\d{9}")
                || splitData[0].matches("U\\d{9}")) {
            // this is an ERSA or UNBW material
            unknown = false;
            mMaterial.setText(splitData[0].substring(1, 10));
            if (splitData.length > 1) {
                mPlant.setText(splitData[1]);
                if (splitData.length > 2) {
                    mStorageLocation.setText(splitData[2]);
                    if (splitData.length > 3) {
                        mBin.setText(splitData[3]);
                    }
                }
            }
        } else if (splitData[0].matches("K\\d{9}")) {
            // this is a vendor consignment material
            unknown = false;
            mMaterial.setText(splitData[0].substring(1, 10));
            if (splitData.length > 1) {
                if (splitData[1].matches("\\d{9}")) {
                    // this is a vendor code
                    mVendor.setText(splitData[1]);
                } else {
                    mPlant.setText(splitData[1]);
                    if (splitData.length > 2) {
                        mStorageLocation.setText(splitData[2]);
                        if (splitData.length > 3) {
                            mVendor.setText(splitData[3]);
                            if (splitData.length > 4) {
                                mBin.setText(splitData[4]);
                            }
                        }
                    }
                }
            }
        } else if (splitData[0].startsWith("C")) {
            // this is a cost center
            unknown = false;
            mCostCenter.setText(splitData[0].substring(1));
        } else if (splitData[0].matches("V\\d{9}")) {
            // this is a vendor
            unknown = false;
            mVendor.setText(splitData[0].substring(1));
        } else if (splitData[0].matches("I\\d{9}")) {
            // this is an inventory document
            unknown = false;
            mInventory.setText(splitData[0].substring(1));
        }
        if (unknown) Snackbar.make(findViewById(R.id.fab),
                "Unknown barcode",
                Snackbar.LENGTH_LONG).show();
    }

    private boolean validateData() {
        // TODO
        return true;
    }
}
