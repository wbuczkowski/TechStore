package com.nestle.tp.techstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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

    private String mOption = "";
    private TextView mTitle;
    private ImageButton mSwitch;
    private EditText mWorkOrder;
    private EditText mCostCenter;
    private EditText mMaterial;
    private EditText mPlant;
    private EditText mStorageLocation;
    private EditText mBin;
    private EditText mQuantity;
    private EditText mInventory;
    private EditText mVendor;
    private TextWatcher WorkOrderWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            validateWorkOrder(s);
        }
    };
    private TextWatcher CostCenterWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            validateCostCenter(s);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    private TextWatcher InventoryWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            validateInventory(s);
        }
    };
    private TextWatcher MaterialWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            validateMaterial(s);
        }
    };
    private TextWatcher PlantWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            validatePlant(s);
        }
    };
    private TextWatcher StorageLocationWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            validateStorageLocation(s);
        }
    };
    private TextWatcher BinWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            validateBin(s);
        }
    };
    private TextWatcher VendorWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            validateVendor(s);
        }
    };
    private TextWatcher QuantityWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            validateQuantity(s);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(fabListener);

        // get title and switch button
        mTitle = findViewById(R.id.textTitle);
        mSwitch = findViewById(R.id.button_switch);
        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mOption) {
                    case OPTION_GOODS_ISSUE:
                        mOption = OPTION_GOODS_RETURN;
                        mTitle.setText(R.string.activity_goods_return);
                        break;
                    case OPTION_GOODS_RETURN:
                        mOption = OPTION_GOODS_ISSUE;
                        mTitle.setText(R.string.activity_goods_issue);
                }
            }
        });

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

        // TODO: data validation
        // set validation watchers
        mWorkOrder.addTextChangedListener(WorkOrderWatcher);
        mCostCenter.addTextChangedListener(CostCenterWatcher);
        mInventory.addTextChangedListener(InventoryWatcher);
        mMaterial.addTextChangedListener(MaterialWatcher);
        mPlant.addTextChangedListener(PlantWatcher);
        mStorageLocation.addTextChangedListener(StorageLocationWatcher);
        mBin.addTextChangedListener(BinWatcher);
        mVendor.addTextChangedListener(VendorWatcher);
        mQuantity.addTextChangedListener(QuantityWatcher);

        // get option from intent, if any
        Intent intent = getIntent();
        String data = intent.getStringExtra(EXTRA_OPTION);
        mOption = (data != null && !data.isEmpty()) ? data : OPTION_GOODS_ISSUE;

        // load last used values first
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        mWorkOrder.setText(sharedPref.getString(EXTRA_WORK_ORDER, ""));
        mCostCenter.setText(sharedPref.getString(EXTRA_COST_CENTER, ""));
        mPlant.setText(sharedPref.getString(EXTRA_PLANT, ""));
        mStorageLocation.setText(sharedPref.getString(EXTRA_STORAGE_LOCATION, ""));
        mInventory.setText(sharedPref.getString(EXTRA_INVENTORY, ""));
        mVendor.setText(sharedPref.getString(EXTRA_VENDOR, ""));

        // overwrite with the values from intent, if provided
        data = intent.getStringExtra(EXTRA_WORK_ORDER);
        if (data != null && !data.isEmpty()) mWorkOrder.setText(data);
        data = intent.getStringExtra(EXTRA_COST_CENTER);
        if (data != null && !data.isEmpty()) mCostCenter.setText(data);
        data = intent.getStringExtra(EXTRA_MATERIAL);
        if (data != null && !data.isEmpty()) mMaterial.setText(data);
        data = intent.getStringExtra(EXTRA_PLANT);
        if (data != null && !data.isEmpty()) mPlant.setText(data);
        data = intent.getStringExtra(EXTRA_STORAGE_LOCATION);
        if (data != null && !data.isEmpty()) mStorageLocation.setText(data);
        data = intent.getStringExtra(EXTRA_BIN);
        if (data != null && !data.isEmpty()) mBin.setText(data);
        data = intent.getStringExtra(EXTRA_INVENTORY);
        if (data != null && !data.isEmpty()) mInventory.setText(data);
        data = intent.getStringExtra(EXTRA_VENDOR);
        if (data != null && !data.isEmpty()) mVendor.setText(data);

        // get defaults from preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (mPlant.length() == 0) {
            mPlant.setText(sharedPref.getString("pref_default_plant", ""));
            // if read from defaults: disable field
            mPlant.setEnabled(mPlant.length() == 0);
        }
        if (mStorageLocation.length() == 0) {
            mStorageLocation.setText(sharedPref.getString("pref_default_storage_location", ""));
            // if read from defaults: disable field
            mStorageLocation.setEnabled(mStorageLocation.length() == 0);
        }

        // initialize visibility and errors
        initializeVisibility();
    }

    private void initializeVisibility() {
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
            mSwitch.setVisibility(View.VISIBLE);
            viewGroup = findViewById(R.id.layoutInventory);
            if (viewGroup != null) viewGroup.setVisibility(View.GONE);
            mInventory.setText("");
            if (mWorkOrder.length() > 0) {
                // show work order and disable, hide and clear cost center
                viewGroup = findViewById(R.id.layoutWorkOrder);
                if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                mWorkOrder.setError(null);
                viewGroup = findViewById(R.id.layoutCostCenter);
                if (viewGroup != null) viewGroup.setVisibility(View.GONE);
                mCostCenter.setText("");
            } else if (mCostCenter.length() > 0) {
                // show cost center and disable, hide and clear work order
                viewGroup = findViewById(R.id.layoutCostCenter);
                if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                mCostCenter.setError(null);
                viewGroup = findViewById(R.id.layoutWorkOrder);
                if (viewGroup != null) viewGroup.setVisibility(View.GONE);
                mWorkOrder.setText("");
            } else {
                // show and enable both
                viewGroup = findViewById(R.id.layoutCostCenter);
                if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
//                mWorkOrder.setError("Enter either Work Order or Cost Center");
                viewGroup = findViewById(R.id.layoutWorkOrder);
                if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
//                mWorkOrder.setError("Enter either Work Order or Cost Center");
            }

        }
        if (mOption.equals(OPTION_INVENTORY_WO_DOCUMENT) || mOption.equals(OPTION_INVENTORY_WITH_DOCUMENT)) {
            // show inventory document, hide and clear work order and cost center
            mSwitch.setVisibility(View.INVISIBLE);
            viewGroup = findViewById(R.id.layoutInventory);
            if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
            viewGroup = findViewById(R.id.layoutWorkOrder);
            if (viewGroup != null) viewGroup.setVisibility(View.GONE);
            mWorkOrder.setText("");
            viewGroup = findViewById(R.id.layoutCostCenter);
            if (viewGroup != null) viewGroup.setVisibility(View.GONE);
            mCostCenter.setText("");
        }

//        mPlant.setError(mPlant.length() == 0 ? "Enter plant code" : null);
//        mStorageLocation.setError(mStorageLocation.length() == 0 ? "Enter storage location code" : null);
//        mMaterial.setError(mMaterial.length() == 0 ? "Enter material number" : null);

        if (mBin.length() > 0) {
            // bin provided, show disabled
            viewGroup = findViewById(R.id.layoutBin);
            if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
        } else {
            // bin not provided, hide
            viewGroup = findViewById(R.id.layoutBin);
            if (viewGroup != null) viewGroup.setVisibility(View.GONE);
        }
        if (mVendor.length() > 0) {
            // vendor provided, show
            viewGroup = findViewById(R.id.layoutVendor);
            if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
        } else {
            // vendor not provided, hide
            viewGroup = findViewById(R.id.layoutVendor);
            if (viewGroup != null) viewGroup.setVisibility(View.GONE);
        }
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
                    // save data for next use
                    SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(EXTRA_WORK_ORDER, mWorkOrder.getText().toString());
                    editor.putString(EXTRA_COST_CENTER, mCostCenter.getText().toString());
                    editor.putString(EXTRA_INVENTORY, mInventory.getText().toString());
                    editor.putString(EXTRA_PLANT, mPlant.getText().toString());
                    editor.putString(EXTRA_STORAGE_LOCATION, mStorageLocation.getText().toString());
                    editor.putString(EXTRA_VENDOR, mVendor.getText().toString());
                    editor.apply();
                    // finish activity
                    finish();
                } else {
                    Snackbar.make(findViewById(R.id.fab),
                            "Please complete data entry",
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
                        //mWorkOrder.setEnabled(false);
                        viewGroup = findViewById(R.id.layoutCostCenter);
                        if (viewGroup != null) viewGroup.setVisibility(View.GONE);
                        mCostCenter.setText("");
                    }
                    break;
                case 9:
                    // this is a material
                    unknown = false;
                    mMaterial.setText(splitData[0]);
                    //mMaterial.setEnabled(false);
                    if (splitData.length > 1) {
                        mPlant.setText(splitData[1]);
                        // plant provided, disable field
                        //mPlant.setEnabled(false);
                        if (splitData.length > 2) {
                            mStorageLocation.setText(splitData[2]);
                            // storage location provided, disable field
                            //mStorageLocation.setEnabled(false);
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
            //mMaterial.setEnabled(false);
            if (splitData.length > 1) {
                mPlant.setText(splitData[1]);
                // plant provided, disable field
                //mPlant.setEnabled(false);
                if (splitData.length > 2) {
                    mStorageLocation.setText(splitData[2]);
                    // storage location provided, disable field
                    //mStorageLocation.setEnabled(false);
                    if (splitData.length > 3) {
                        mBin.setText(splitData[3]);
                        //mBin.setEnabled(false);
                        viewGroup = findViewById(R.id.layoutBin);
                        if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else if (splitData[0].matches("K\\d{9}")) {
            // this is a vendor consignment material
            unknown = false;
            mMaterial.setText(splitData[0].substring(1, 10));
            //mMaterial.setEnabled(false);
            if (splitData.length > 1) {
                if (splitData[1].matches("\\d{9}")) {
                    // this is a vendor code
                    mVendor.setText(splitData[1]);
                    viewGroup = findViewById(R.id.layoutVendor);
                    if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                    //mVendor.setEnabled(false);
                } else {
                    mPlant.setText(splitData[1]);
                    // plant provided, disable field
                    //mPlant.setEnabled(false);
                    if (splitData.length > 2) {
                        mStorageLocation.setText(splitData[2]);
                        // storage location provided, disable field
                        //mStorageLocation.setEnabled(false);
                        if (splitData.length > 3) {
                            mVendor.setText(splitData[3]);
                            viewGroup = findViewById(R.id.layoutVendor);
                            if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                            //mVendor.setEnabled(false);
                            if (splitData.length > 4) {
                                mBin.setText(splitData[4]);
                                //mBin.setEnabled(false);
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
            //mVendor.setEnabled(false);
            mVendor.setText(splitData[0].substring(1));
        } else if (splitData[0].matches("I\\d{9}")) {
            // this is an inventory document
            unknown = false;
            if (mOption.equals(OPTION_INVENTORY_WO_DOCUMENT)) {
                // switch option from 3 to 4
                mOption = OPTION_INVENTORY_WITH_DOCUMENT;
            }
            if (mOption.equals(OPTION_INVENTORY_WITH_DOCUMENT)) {
                // ignore inventory number at goods issue
                mInventory.setText(splitData[0].substring(1));
                //mInventory.setEnabled(false);
            }
        }
        if (unknown) Snackbar.make(findViewById(R.id.fab),
                "Unknown barcode",
                Snackbar.LENGTH_LONG).show();
    }

    private boolean validateData() {
        return validateWorkOrder(mWorkOrder.getText())
                && validateCostCenter(mCostCenter.getText())
                && validateInventory(mInventory.getText())
                && validateMaterial(mMaterial.getText())
                && validatePlant(mPlant.getText())
                && validateStorageLocation(mStorageLocation.getText())
                && validateBin(mBin.getText())
                && validateVendor(mVendor.getText())
                && validateQuantity(mQuantity.getText());
    }

    private boolean validateWorkOrder(Editable s) {
        if (mOption.equals(OPTION_GOODS_ISSUE) || mOption.equals(OPTION_GOODS_RETURN)) {
            if (s == null || s.length() == 0) {
                if (mCostCenter.length() == 0) {
                    // both work order and cost center are empty
                    // show up cost center, if hidden before
                    ViewGroup viewGroup = findViewById(R.id.layoutCostCenter);
                    if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                    mWorkOrder.setError("Enter either Work Order or Cost Center");
                    mCostCenter.setError("Enter either Work Order or Cost Center");
                    return false;
                } else {
                    // work order is empty, but cost center is not
                    // should not be here, as the work order should be hidden
                    ViewGroup viewGroup = findViewById(R.id.layoutWorkOrder);
                    if (viewGroup != null) viewGroup.setVisibility(View.GONE);
                    mWorkOrder.setError(null);
                    return true;
                }
            } else {
                // work order entered, clear and hide cost center
                ViewGroup viewGroup = findViewById(R.id.layoutCostCenter);
                if (viewGroup != null) viewGroup.setVisibility(View.GONE);
                if (mCostCenter.length() > 0) mCostCenter.setText("");
                // check the length
                if (s.length() == 10) {
                    mWorkOrder.setError(null);
                    return true;
                } else {
                    mWorkOrder.setError("Work Order should be 10 digits");
                    return false;
                }
            }
        } else {
            mWorkOrder.setError(null);
            return true;
        }
    }

    private boolean validateCostCenter(Editable s) {
        if (mOption.equals(OPTION_GOODS_ISSUE) || mOption.equals(OPTION_GOODS_RETURN)) {
            if (s == null || s.length() == 0) {
                if (mWorkOrder.length() == 0) {
                    // both work order and cost center are empty
                    // show up work order, if hidden before
                    ViewGroup viewGroup = findViewById(R.id.layoutWorkOrder);
                    if (viewGroup != null) viewGroup.setVisibility(View.VISIBLE);
                    mCostCenter.setError("Enter either Work Order or Cost Center");
                    mWorkOrder.setError("Enter either Work Order or Cost Center");
                    return false;
                } else {
                    // cost center is empty, but work order is not
                    // should not be here, as the cost center should be hidden
                    ViewGroup viewGroup = findViewById(R.id.layoutCostCenter);
                    if (viewGroup != null) viewGroup.setVisibility(View.GONE);
                    mCostCenter.setError(null);
                    return true;
                }
            } else {
                // cost center entered, clear and hide work order
                ViewGroup viewGroup = findViewById(R.id.layoutWorkOrder);
                if (viewGroup != null) viewGroup.setVisibility(View.GONE);
                if (mWorkOrder.length() > 0) mWorkOrder.setText("");
                // check the length
                if (s.length() >= 7 && s.length() <= 10) {
                    mCostCenter.setError(null);
                    return true;
                } else {
                    mCostCenter.setError("Cost Center should be 7 to 10 characters");
                    return false;
                }
            }
        } else {
            mCostCenter.setError(null);
            return true;
        }
    }

    private boolean validateInventory(Editable s) {
        if (mOption.equals(OPTION_INVENTORY_WO_DOCUMENT) || mOption.equals(OPTION_INVENTORY_WITH_DOCUMENT)) {
            if (s == null || s.length() == 0) {
                mInventory.setError(null);
                if (mOption.equals(OPTION_INVENTORY_WITH_DOCUMENT))
                    mOption = OPTION_INVENTORY_WO_DOCUMENT;
                return true;
            } else {
                if (s.length() == 9) {
                    mInventory.setError(null);
                    if (mOption.equals(OPTION_INVENTORY_WO_DOCUMENT))
                        mOption = OPTION_INVENTORY_WITH_DOCUMENT;
                    return true;
                }
                mInventory.setError("Inventory document number should be 9 digits");
                return false;
            }
        } else {
            mInventory.setError(null);
            return true;
        }
    }

    private boolean validateMaterial(Editable s) {
        if (s == null || s.length() == 0) {
            mMaterial.setError("Enter material number");
            return false;
        } else if (s.length() == 9) {
            mMaterial.setError(null);
            return true;
        } else {
            mMaterial.setError("Material number should be 9 digits");
            return false;
        }
    }

    private boolean validatePlant(Editable s) {
        if (s == null || s.length() == 0) {
            mPlant.setError("Enter plant code");
            return false;
        } else if (s.length() == 4) {
            mPlant.setError(null);
            return true;
        } else {
            mPlant.setError("Plant code should be 4 characters");
            return false;
        }
    }

    private boolean validateStorageLocation(Editable s) {
        if (s == null || s.length() == 0) {
            mStorageLocation.setError("Enter storage location code");
            return false;
        } else if (s.length() == 4) {
            mStorageLocation.setError(null);
            return true;
        } else {
            mStorageLocation.setError("Storage location code should be 4 characters");
            return false;
        }
    }

    private boolean validateBin(Editable s) {
        if (s == null || s.length() == 0) {
            mBin.setError(null);
            return true;
        } else {
            if (s.length() <= 10) {
                mBin.setError(null);
                return true;
            }
            mBin.setError("Bin code should be up to 10 characters");
            return false;
        }
    }

    private boolean validateVendor(Editable s) {
        if (s == null || s.length() == 0) {
            mVendor.setError(null);
            return true;
        } else {
            if (s.length() == 9) {
                mVendor.setError(null);
                return true;
            }
            mVendor.setError("Vendor code should be 9 digits");
            return false;
        }
    }

    private boolean validateQuantity(Editable s) {
        if (s == null || s.length() == 0) {
            mQuantity.setError("Enter quantity");
            return false;
        } else {
            Double q = Double.parseDouble(s.toString());
            if (q == 0.0) {
                mQuantity.setError("Quantity cannot be zero");
                return false;
            } else if (Math.round(q * 1000.0) < q * 1000.0) {
                mQuantity.setError("Up to 3 decimals allowed");
                return false;
            } else if (Math.round(q / 10000000000.0) > 0) {
                mQuantity.setError("Up to 10 integers allowed");
                return false;
            } else {
                return true;
            }
        }
    }
}

