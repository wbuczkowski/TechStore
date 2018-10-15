package com.nestle.tp.techstore;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppActivity implements View.OnClickListener {

    private static final int RC_GET_DATA = 9101;

    private String mUserName;
    private TextView mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
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
        mUserName = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        mStatus = findViewById(R.id.text_status);
        setStatusText();
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
                    option = OPTION_GOODS_ISSUE;
                    workOrder = splitData[0];
                    break;
                case 9:
                    // this is a material
                    option = OPTION_GOODS_ISSUE;
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
                option = OPTION_GOODS_ISSUE;
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
                option = OPTION_GOODS_ISSUE;
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
            option = OPTION_GOODS_ISSUE;
            costCenter = splitData[0].substring(1);
        } else if (splitData[0].matches("V\\d{9}")) {
            // this is a vendor
            option = OPTION_GOODS_ISSUE;
            vendor = splitData[0].substring(1);
        } else if (splitData[0].matches("I\\d{9}")) {
            // this is an inventory document
            option = OPTION_INVENTORY_WITH_DOCUMENT;
            inventory = splitData[0].substring(1);
        }
        if (option.isEmpty()) {
            Snackbar.make(findViewById(R.id.fab),
                    "Unknown barcode",
                    Snackbar.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(EXTRA_OPTION, option);
            intent.putExtra(EXTRA_WORK_ORDER, workOrder);
            intent.putExtra(EXTRA_COST_CENTER, costCenter);
            intent.putExtra(EXTRA_MATERIAL, materialNumber);
            intent.putExtra(EXTRA_PLANT, plant);
            intent.putExtra(EXTRA_STORAGE_LOCATION, storageLocation);
            intent.putExtra(EXTRA_BIN, bin);
            intent.putExtra(EXTRA_VENDOR, vendor);
            intent.putExtra(EXTRA_INVENTORY, inventory);
            startActivityForResult(intent, RC_GET_DATA);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button_goods_issue:
                intent = new Intent(this, DetailActivity.class);
                intent.putExtra(EXTRA_OPTION, OPTION_GOODS_ISSUE);
                startActivityForResult(intent, RC_GET_DATA);
                break;
            case R.id.button_goods_return:
                intent = new Intent(this, DetailActivity.class);
                intent.putExtra(EXTRA_OPTION, OPTION_GOODS_RETURN);
                startActivityForResult(intent, RC_GET_DATA);
                break;
            case R.id.button_inventory:
                intent = new Intent(this, DetailActivity.class);
                intent.putExtra(EXTRA_OPTION, OPTION_INVENTORY_WO_DOCUMENT);
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
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    Snackbar.make(findViewById(R.id.fab),
                            "Success",
                            Snackbar.LENGTH_LONG).show();
                    if (writeFile(data)) setStatusText();
                } else {
                    Snackbar.make(findViewById(R.id.fab),
                            "Cancelled",
                            Snackbar.LENGTH_LONG).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean writeFile(Intent intent) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File file = (Build.VERSION.SDK_INT >= 19) ?
                    new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                            getString(R.string.app_name)) :
                    new File(Environment.getExternalStorageDirectory(),
                            "Documents/" + getString(R.string.app_name));

            if (!file.exists() && !file.mkdirs()) {
                Snackbar.make(findViewById(R.id.fab), "Directory not created", Snackbar.LENGTH_LONG).show();
                return false;
            }
            file = new File(file.getPath() + "/" + getString(R.string.file_name));
            if (!file.exists()) {
                try {
                    if (!file.createNewFile()) {
                        Snackbar.make(findViewById(R.id.fab), "Cannot create file", Snackbar.LENGTH_LONG).show();
                        return false;
                    }
                } catch (IOException e) {
                    Snackbar.make(findViewById(R.id.fab), e.getMessage(), Snackbar.LENGTH_LONG).show();
                    return false;
                }
            }
            BufferedWriter bw = null;
            FileWriter fw = null;

            String dataLine = prepareData(intent);

            try {
                fw = new FileWriter(file, true);
                bw = new BufferedWriter(fw);
                if (dataLine != null) bw.write(dataLine);
                return true;
            } catch (IOException e) {
                Snackbar.make(findViewById(R.id.fab), e.getMessage(), Snackbar.LENGTH_LONG).show();
                return false;
            } finally {
                try {
                    if (bw != null) bw.close();
                    if (fw != null) fw.close();
                } catch (IOException e) {
                    Snackbar.make(findViewById(R.id.fab), e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        } else {
            Snackbar.make(findViewById(R.id.fab), "External storage not writable", Snackbar.LENGTH_LONG).show();
            return false;
        }
    }

    @Nullable
    private String prepareData(Intent intent) {
        String data = intent.getStringExtra(DetailActivity.EXTRA_OPTION);
        if (data == null || data.isEmpty()) {
            // wrong intent?
            return null;
        }
        // write option
        String dataLine = data;

        switch (data) {
            case OPTION_GOODS_ISSUE:
            case OPTION_GOODS_RETURN:
                data = intent.getStringExtra(EXTRA_WORK_ORDER);
                if (data != null && !data.isEmpty()) {
                    // write work order
                    dataLine = dataLine.concat("\t" + data);
                } else {
                    // no work order, take cost center
                    data = intent.getStringExtra(EXTRA_COST_CENTER);
                    if (data != null && !data.isEmpty()) {
                        // write cost center
                        dataLine = dataLine.concat("\t" + data);
                    } else {
                        // no work order and no cost center
                        return null;
                    }
                }
                break;
            case OPTION_INVENTORY_WO_DOCUMENT:
            case OPTION_INVENTORY_WITH_DOCUMENT:
                // write spacer
                dataLine = dataLine.concat("\t");
                break;
            default:
                // unknown option
                return null;
        }

        // write material number etc.
        String[][] ii = {
                {EXTRA_MATERIAL, "true"},
                {EXTRA_PLANT, "true"},
                {EXTRA_STORAGE_LOCATION, "true"},
                {EXTRA_BIN, "false"},
                {EXTRA_QUANTITY, "true"}
        };
        // for (int i = 0; i<map.length;i++){
        for (String[] i : ii) {
            data = intent.getStringExtra(i[0]);
            if (data != null && !data.isEmpty()) { // write data
                dataLine = dataLine.concat("\t" + data);
            } else { // no data, quit if mandatory
                if (Boolean.parseBoolean(i[1])) { // mandatory: quit
                    return null;
                } else { // not mandatory: write spacer
                    dataLine = dataLine.concat("\t");
                }
            }
        }
//        HashMap<String, Boolean> map = new HashMap<>();
//        map.put(EXTRA_MATERIAL, true);
//        map.put(EXTRA_PLANT, true);
//        map.put(EXTRA_STORAGE_LOCATION, true);
//        map.put(EXTRA_BIN, false);
//        map.put(EXTRA_QUANTITY, true);
//        for (String key : map.keySet()) {
//            data = intent.getStringExtra(key);
//            if (data != null && !data.isEmpty()) { // write data
//                dataLine = dataLine.concat("\t" + data);
//            } else { // no data, quit if mandatory
//                Boolean b = map.get(key);
//                if (b != null && b) { // mandatory: quit
//                    return null;
//                } else { // not mandatory: write spacer
//                    dataLine = dataLine.concat("\t");
//                }
//            }
//        }

        // write user name
        dataLine = dataLine.concat("\t" + mUserName);

        //write date
        data = intent.getStringExtra(EXTRA_DATE);
        if (data != null && !data.isEmpty()) { // write data
            dataLine = dataLine.concat("\t" + data);
        } else {
            return null;
        }

        // write inventory number, if provided
        if (data.equals(OPTION_INVENTORY_WITH_DOCUMENT)) {
            // write inventory document
            data = intent.getStringExtra(EXTRA_INVENTORY);
            if (data != null && !data.isEmpty()) { // write data
                dataLine = dataLine.concat("\t" + data);
            } else {
                return null;
            }
        } else {
            // write spacer
            // TODO: check if needed
            dataLine = dataLine.concat("\t");
        }
        // write vendor
        data = intent.getStringExtra(EXTRA_VENDOR);
        if (data != null && !data.isEmpty()) {
            // write vendor
            dataLine = dataLine.concat("\t" + data);
        }

        // success: write end of line
        dataLine = dataLine.concat("\n");

        return dataLine;
    }

    private void setStatusText() {
        String state = Environment.getExternalStorageState();
        String statusText = getString(R.string.text_status);
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File file = (Build.VERSION.SDK_INT >= 19) ?
                    new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                            getString(R.string.app_name)
                                    + "/" + getString(R.string.file_name)) :
                    new File(Environment.getExternalStorageDirectory(),
                            "Documents/" + getString(R.string.app_name)
                                    + "/" + getString(R.string.file_name));
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            int count = 0;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);

                byte[] c = new byte[1024];

                int readChars = bis.read(c);
                if (readChars == -1) {
                    // bail out if nothing to read
                    statusText = statusText.concat("0");
                    mStatus.setText(statusText);
                    return;
                }

                // make it easy for the optimizer to tune this loop
                while (readChars == 1024) {
                    for (int i = 0; i < 1024; ) {
                        if (c[i++] == '\n') {
                            ++count;
                        }
                    }
                    readChars = bis.read(c);
                }

                // count remaining characters
                while (readChars != -1) {
                    System.out.println(readChars);
                    for (int i = 0; i < readChars; ++i) {
                        if (c[i] == '\n') {
                            ++count;
                        }
                    }
                    readChars = bis.read(c);
                }
            } catch (IOException e) {
                Snackbar.make(findViewById(R.id.fab), e.getMessage(), Snackbar.LENGTH_LONG).show();
                statusText = statusText.concat("0");
                mStatus.setText(statusText);
                return;
            } finally {
                try {
                    if (bis != null) bis.close();
                    if (fis != null) fis.close();
                } catch (IOException e) {
                    Snackbar.make(findViewById(R.id.fab), e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
            statusText = statusText.concat(String.valueOf(count));
            mStatus.setText(statusText);
        }
    }
}
