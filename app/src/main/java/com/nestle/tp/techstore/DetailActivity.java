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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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
    private TextView mTitle;
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
        mTitle = findViewById(R.id.textTitle);
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
            //mPlant.setText(savedInstanceState.getString(EXTRA_PLANT, ""));
            //mStorageLocation.setText(savedInstanceState.getString(EXTRA_STORAGE_LOCATION, ""));
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
        if (data != null) if (!data.isEmpty()) mMaterial.setText(data);
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
        // initialize visibility and edibility
        ViewGroup viewGroup;
        switch (mOption) {
            case OPTION_GOODS_ISSUE:
                mTitle.setText(R.string.activity_goods_issue);
                break;
            case OPTION_GOODS_RETURN:
                mTitle.setText(R.string.activity_goods_return);
                break;
            case OPTION_INVENTORY_WO_DOCUMENT:
            case OPTION_INVENTORY_WITH_DOCUMENT:
                mTitle.setText(R.string.activity_inventory);
        }
        if (mOption.equals(OPTION_GOODS_ISSUE) || mOption.equals(OPTION_GOODS_RETURN)) {
            // hide and clear inventory document number
            viewGroup = findViewById(R.id.layoutInventory);
            if (viewGroup != null) viewGroup.setVisibility(View.GONE);
            mInventory.setText("");
            if (mWorkOrder.length() > 0) {
                // show work order and disable, hide and clear cost center
                viewGroup = findViewById(R.id.layoutWorkOrder);
                if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                mWorkOrder.setEnabled(false);
                viewGroup = findViewById(R.id.layoutCostCenter);
                if (viewGroup != null) viewGroup.setVisibility(View.GONE);
                mCostCenter.setText("");
            } else if (mCostCenter.length() > 0) {
                // show cost center and disable, hide and clear work order
                viewGroup = findViewById(R.id.layoutCostCenter);
                if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                mCostCenter.setEnabled(false);
                viewGroup = findViewById(R.id.layoutWorkOrder);
                if (viewGroup != null) viewGroup.setVisibility(View.GONE);
                mWorkOrder.setText("");
            } else {
                // show and enable both
                viewGroup = findViewById(R.id.layoutCostCenter);
                if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                mWorkOrder.setEnabled(true);
                viewGroup = findViewById(R.id.layoutWorkOrder);
                if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                mCostCenter.setEnabled(true);
            }
        }
        if (mOption.equals(OPTION_INVENTORY_WO_DOCUMENT) || mOption.equals(OPTION_INVENTORY_WITH_DOCUMENT)) {
            // show inventory document, hide and clear work order and cost center
            viewGroup = findViewById(R.id.layoutInventory);
            if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
            viewGroup = findViewById(R.id.layoutWorkOrder);
            if (viewGroup != null) viewGroup.setVisibility(View.GONE);
            mWorkOrder.setText("");
            viewGroup = findViewById(R.id.layoutCostCenter);
            if (viewGroup != null) viewGroup.setVisibility(View.GONE);
            mCostCenter.setText("");
            if (mInventory.length() > 0) {
                mInventory.setEnabled(false);
            } else {
                mInventory.setEnabled(true);
            }
        }
        if (mPlant.length() > 0) {
            // plant provided, disable field
            mPlant.setEnabled(false);
        } else {
            // plant not provided, enable field
            mPlant.setEnabled(true);
        }
        if (mStorageLocation.length() > 0) {
            // storage location provided, disable field
            mStorageLocation.setEnabled(false);
        } else {
            // storage location not provided, enable field
            mStorageLocation.setEnabled(true);
        }
        if (mBin.length() > 0) {
            // bin provided, show disabled
            viewGroup = findViewById(R.id.layoutBin);
            if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
            mBin.setEnabled(false);
        } else {
            // bin not provided, hide
            viewGroup = findViewById(R.id.layoutBin);
            if (viewGroup != null) viewGroup.setVisibility(View.GONE);
        }
        if (mVendor.length() > 0) {
            // vendor provided, show
            viewGroup = findViewById(R.id.layoutVendor);
            if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
            mVendor.setEnabled(false);
        } else {
            // vendor not provided, hide
            viewGroup = findViewById(R.id.layoutVendor);
            if (viewGroup != null) viewGroup.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_WORK_ORDER, mWorkOrder.getText().toString());
        outState.putString(EXTRA_COST_CENTER, mCostCenter.getText().toString());
        //outState.putString(EXTRA_PLANT, mPlant.getText().toString());
        //outState.putString(EXTRA_STORAGE_LOCATION, mStorageLocation.getText().toString());
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
        ViewGroup viewGroup;
        if (splitData[0].matches("\\d+")) {
            // starts with a digit
            switch (splitData[0].length()) {
                case 10:
                    // this is a work order
                    unknown = false;
                    if (mOption.equals(OPTION_GOODS_ISSUE) || mOption.equals(OPTION_GOODS_RETURN)) {
                        // ignore work order by inventory
                        // show work order, hide and clear cost center
                        viewGroup = findViewById(R.id.layoutWorkOrder);
                        if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                        mWorkOrder.setText(splitData[0]);
                        mWorkOrder.setEnabled(false);
                        viewGroup = findViewById(R.id.layoutCostCenter);
                        if (viewGroup != null) viewGroup.setVisibility(View.GONE);
                        mCostCenter.setText("");
                    }
                    break;
                case 9:
                    // this is a material
                    unknown = false;
                    mMaterial.setText(splitData[0]);
                    mMaterial.setEnabled(false);
                    if (splitData.length > 1) {
                        mPlant.setText(splitData[1]);
                        // plant provided, disable field
                        mPlant.setEnabled(false);
                        if (splitData.length > 2) {
                            mStorageLocation.setText(splitData[2]);
                            // storage location provided, disable field
                            mStorageLocation.setEnabled(false);
                            if (splitData.length > 3) {
                                mBin.setText(splitData[3]);
                                viewGroup = findViewById(R.id.layoutBin);
                                if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                            }
                        }
                    }
            }
        } else if (splitData[0].matches("E\\d{9}")
                || splitData[0].matches("U\\d{9}")) {
            // this is an ERSA or UNBW material
            unknown = false;
            mMaterial.setText(splitData[0].substring(1, 10));
            mMaterial.setEnabled(false);
            if (splitData.length > 1) {
                mPlant.setText(splitData[1]);
                // plant provided, disable field
                mPlant.setEnabled(false);
                if (splitData.length > 2) {
                    mStorageLocation.setText(splitData[2]);
                    // storage location provided, disable field
                    mStorageLocation.setEnabled(false);
                    if (splitData.length > 3) {
                        mBin.setText(splitData[3]);
                        mBin.setEnabled(false);
                        viewGroup = findViewById(R.id.layoutBin);
                        if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else if (splitData[0].matches("K\\d{9}")) {
            // this is a vendor consignment material
            unknown = false;
            mMaterial.setText(splitData[0].substring(1, 10));
            mMaterial.setEnabled(false);
            if (splitData.length > 1) {
                if (splitData[1].matches("\\d{9}")) {
                    // this is a vendor code
                    mVendor.setText(splitData[1]);
                    viewGroup = findViewById(R.id.layoutVendor);
                    if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                    mVendor.setEnabled(false);
                } else {
                    mPlant.setText(splitData[1]);
                    // plant provided, disable field
                    mPlant.setEnabled(false);
                    if (splitData.length > 2) {
                        mStorageLocation.setText(splitData[2]);
                        // storage location provided, disable field
                        mStorageLocation.setEnabled(false);
                        if (splitData.length > 3) {
                            mVendor.setText(splitData[3]);
                            viewGroup = findViewById(R.id.layoutVendor);
                            if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                            mVendor.setEnabled(false);
                            if (splitData.length > 4) {
                                mBin.setText(splitData[4]);
                                mBin.setEnabled(false);
                                viewGroup = findViewById(R.id.layoutBin);
                                if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        } else if (splitData[0].startsWith("C")) {
            // this is a cost center
            unknown = false;
            if (mOption.equals(OPTION_GOODS_ISSUE) || mOption.equals(OPTION_GOODS_RETURN)) {
                // ignore cost center by inventory
                // show cost center, hide and clear work order
                viewGroup = findViewById(R.id.layoutCostCenter);
                if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                mCostCenter.setText(splitData[0].substring(1));
                viewGroup = findViewById(R.id.layoutWorkOrder);
                if (viewGroup != null) viewGroup.setVisibility(View.GONE);
                mWorkOrder.setText("");
            }
        } else if (splitData[0].matches("V\\d{9}")) {
            // this is a vendor
            unknown = false;
            // display vendor
            viewGroup = findViewById(R.id.layoutVendor);
            if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
            mVendor.setEnabled(false);
            mVendor.setText(splitData[0].substring(1));
        } else if (splitData[0].matches("I\\d{9}")) {
            // this is an inventory document
            unknown = false;
            if (mOption.equals(OPTION_INVENTORY_WO_DOCUMENT) || mOption.equals(OPTION_INVENTORY_WITH_DOCUMENT)) {
                // ignore inventory number at goods issue
                mInventory.setText(splitData[0].substring(1));
                mInventory.setEnabled(false);
            }
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
