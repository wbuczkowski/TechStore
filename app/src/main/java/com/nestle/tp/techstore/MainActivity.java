package com.nestle.tp.techstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

public class MainActivity extends AppActivity implements View.OnClickListener {

    private static final int RC_GET_DATA = 9101;

    private String mUserName;
    private TextView mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(fabListener);
        Button button = findViewById(R.id.button_goods_issue);
        button.setOnClickListener(this);
        button = findViewById(R.id.button_goods_return);
        button.setOnClickListener(this);
        button = findViewById(R.id.button_inventory);
        button.setOnClickListener(this);
        button = findViewById(R.id.button_display);
        button.setOnClickListener(this);
        mStatus = findViewById(R.id.text_status);
        mUserName = getIntent().getStringExtra(Intent.EXTRA_TEXT);
    }

    @Override
    public void processBarcode(String data) {
        String option = "", materialNumber = "",
                workOrder = "", costCenter = "",
                plant = "", storageLocation = "", bin = "",
                inventory = "", vendor = "";
        String[] splitData = data.split(" ");
        if (splitData[0].matches("\\d+")) {
            // starts with a digit
            switch (splitData[0].length()) {
                case 10:
                    // this is a work order
                    option = DetailActivity.OPTION_GOODS_ISSUE;
                    workOrder = splitData[0];
                    break;
                case 9:
                    // this is a material
                    option = DetailActivity.OPTION_GOODS_ISSUE;
                    materialNumber = splitData[0];
                    if (splitData.length > 1) {
                        plant = splitData[1];
                        if (splitData.length > 2) {
                            storageLocation = splitData[2];
                            if (splitData.length > 3) {
                                bin = splitData[3];
                            }
                        }
                    }
            }
        } else if (splitData[0].startsWith("E")
                || splitData[0].startsWith("U")) {
            // this is an ERSA or UNBW material
            materialNumber = splitData[0].substring(1, 10);
            if (materialNumber.matches("\\d{9}")) {
                // material number correct
                option = DetailActivity.OPTION_GOODS_ISSUE;
                if (splitData.length > 1) {
                    plant = splitData[1];
                    if (splitData.length > 2) {
                        storageLocation = splitData[2];
                        if (splitData.length > 3) {
                            bin = splitData[3];
                        }
                    }
                }
            }
        } else if (splitData[0].startsWith("K")) {
            // this is a vendor consignment material
            materialNumber = splitData[0].substring(1, 10);
            if (materialNumber.matches("\\d{9}")) {
                // material number correct
                option = DetailActivity.OPTION_GOODS_ISSUE;
                if (splitData.length > 1) {
                    if (splitData[1].matches("\\d{9}")) {
                        // this is a vendor code
                        vendor = splitData[1];
                    } else {
                        plant = splitData[1];
                        if (splitData.length > 2) {
                            storageLocation = splitData[2];
                            if (splitData.length > 3) {
                                vendor = splitData[3];
                                if (splitData.length > 4) {
                                    bin = splitData[4];
                                }
                            }
                        }
                    }
                }
            }
        } else if (splitData[0].startsWith("C")) {
            // this is a cost center
            option = DetailActivity.OPTION_GOODS_ISSUE;
            costCenter = splitData[0].substring(1);
        } else if (splitData[0].matches("V\\d{9}")) {
            // this is a vendor
            option = DetailActivity.OPTION_GOODS_ISSUE;
            vendor = splitData[0].substring(1);
        } else if (splitData[0].matches("I\\d{9}")) {
            // this is an inventory document
            option = DetailActivity.OPTION_INVENTORY_WITH_DOCUMENT;
            inventory = splitData[0].substring(1);
        }
        if (option.isEmpty()) {
            Snackbar.make(findViewById(R.id.fab),
                    "Unknown barcode",
                    Snackbar.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_OPTION, option);
            intent.putExtra(DetailActivity.EXTRA_WORK_ORDER, workOrder);
            intent.putExtra(DetailActivity.EXTRA_COST_CENTER, costCenter);
            intent.putExtra(DetailActivity.EXTRA_MATERIAL, materialNumber);
            intent.putExtra(DetailActivity.EXTRA_PLANT, plant);
            intent.putExtra(DetailActivity.EXTRA_STORAGE_LOCATION, storageLocation);
            intent.putExtra(DetailActivity.EXTRA_BIN, bin);
            intent.putExtra(DetailActivity.EXTRA_VENDOR, vendor);
            intent.putExtra(DetailActivity.EXTRA_INVENTORY, inventory);
            startActivityForResult(intent, RC_GET_DATA);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button_goods_issue:
                intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_OPTION, DetailActivity.OPTION_GOODS_ISSUE);
                startActivityForResult(intent, RC_GET_DATA);
                break;
            case R.id.button_goods_return:
                intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_OPTION, DetailActivity.OPTION_GOODS_RETURN);
                startActivityForResult(intent, RC_GET_DATA);
                break;
            case R.id.button_inventory:
                intent = new Intent(this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_OPTION, DetailActivity.OPTION_INVENTORY_WO_DOCUMENT);
                startActivityForResult(intent, RC_GET_DATA);
                break;
            case R.id.button_display:
                // TODO display activity
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_GET_DATA:
                // TODO
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    Snackbar.make(findViewById(R.id.fab),
                            "Success",
                            Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(findViewById(R.id.fab),
                            "Something else",
                            Snackbar.LENGTH_LONG).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
