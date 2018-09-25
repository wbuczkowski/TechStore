package com.nestle.tp.techstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
    public static final String EXTRA_USERNAME = "USERNAME";
    public static final String EXTRA_DATE = "DATE";
    public static final String EXTRA_INVENTORY = "INVENTORY";
    public static final String EXTRA_VENDOR = "VENDOR";

    private String mOption;
    private String mWorkOrder;
    private String mCostCenter;
    private String mMaterial;
    private String mPlant;
    private String mStorageLocation;
    private String mBin;
    private Double mQuantity;
    private String mUser;
    private String mInventory;
    private String mVendor;

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

        Intent intent = getIntent();
        mOption = intent.getStringExtra(EXTRA_OPTION);
        mWorkOrder = intent.getStringExtra(EXTRA_WORK_ORDER);
        mCostCenter = intent.getStringExtra(EXTRA_COST_CENTER);
        mMaterial = intent.getStringExtra(EXTRA_MATERIAL);
        mPlant = intent.getStringExtra(EXTRA_PLANT);
        mStorageLocation = intent.getStringExtra(EXTRA_STORAGE_LOCATION);
        mBin = intent.getStringExtra(EXTRA_BIN);
        mQuantity = intent.getDoubleExtra(EXTRA_QUANTITY, 0.0);
        mUser = intent.getStringExtra(EXTRA_USERNAME);
        mInventory = intent.getStringExtra(EXTRA_INVENTORY);
        mVendor = intent.getStringExtra(EXTRA_VENDOR);
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
                    data.putExtra(EXTRA_WORK_ORDER, mWorkOrder);
                    data.putExtra(EXTRA_COST_CENTER, mCostCenter);
                    data.putExtra(EXTRA_MATERIAL, mMaterial);
                    data.putExtra(EXTRA_PLANT, mPlant);
                    data.putExtra(EXTRA_STORAGE_LOCATION, mStorageLocation);
                    data.putExtra(EXTRA_BIN, mBin);
                    data.putExtra(EXTRA_QUANTITY, mQuantity);
                    data.putExtra(EXTRA_USERNAME, mUser);
                    SimpleDateFormat ft = new SimpleDateFormat("DD/MM/YY HH:mm:ss", Locale.UK);
                    data.putExtra(EXTRA_DATE, ft.format(new Date()));
                    data.putExtra(EXTRA_INVENTORY, mInventory);
                    data.putExtra(EXTRA_VENDOR, mVendor);
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
        // TODO
    }

    private boolean validateData() {
        // TODO
        return true;
    }
}
