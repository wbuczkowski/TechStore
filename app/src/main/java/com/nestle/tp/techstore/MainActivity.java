package com.nestle.tp.techstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

public class MainActivity extends AppActivity implements View.OnClickListener{

    private static final int RC_GET_DATA = 9101;

    private String mUserName;

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
        mUserName = getIntent().getStringExtra(Intent.EXTRA_TEXT);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button_goods_issue:
                break;
            case R.id.button_goods_return:
                break;
            case R.id.button_inventory:
                break;
            case R.id.button_display:
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
                    // TODO
                } else {

                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void processBarcode(String data) {
        // TODO
        Intent intent = new Intent(this,
                BarcodeCaptureActivity.class);
        intent.putExtra(DetailActivity.EXTRA_OPTION, "1");
        intent.putExtra(DetailActivity.EXTRA_WORK_ORDER, "");
        intent.putExtra(DetailActivity.EXTRA_COST_CENTER, "");
        startActivityForResult(intent, RC_GET_DATA);
    }

}
