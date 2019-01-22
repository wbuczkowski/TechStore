package com.nestle.tp.techstore;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.ViewTreeObserver;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ResultsActivity extends AppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        final TableLayout tl = findViewById(R.id.tablelayout_contents);
        TableRow tr;
        TextView tv;

        ArrayList<String[]> data = readData();
        for (String[] row : data) {
            tr = new TableRow(this);
            for (String cell : row) {
                tv = new TextView(this);
                tv.setBackground(ContextCompat.getDrawable(this, android.R.drawable.editbox_background));
                tv.setText(cell);
                tr.addView(tv);

            }
            tl.addView(tr);
        }

        String[] header = getResources().getStringArray(R.array.results_headers);
        tr = findViewById(R.id.tablerow_header);
        for (String cell : header) {
            tv = new TextView(this);
            tv.setBackground(ContextCompat.getDrawable(this, android.R.drawable.editbox_background));
            tv.setText(cell);
            tr.addView(tv);
        }

        tl.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (tl.getChildCount() > 0) {
                            TableRow tr = (TableRow) tl.getChildAt(0);
                            TableRow trH = findViewById(R.id.tablerow_header);
                            TextView tv, tvH;
                            for (int j = 0; j < tr.getChildCount(); j++) {
                                tv = (TextView) tr.getChildAt(j);
                                tvH = (TextView) trH.getChildAt(j);
                                tvH.setWidth(tv.getWidth());
                            }
                        }
                    }
                });
    }

    @Override
    public void processBarcode(String data) {
        // dummy
    }

    private ArrayList<String[]> readData() {
        ArrayList<String[]> data = new ArrayList<>();
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File file = (Build.VERSION.SDK_INT >= 19) ?
                    new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                            getString(R.string.app_name)
                                    + "/" + getString(R.string.file_name)) :
                    new File(Environment.getExternalStorageDirectory(),
                            "Documents/" + getString(R.string.app_name)
                                    + "/" + getString(R.string.file_name));
            FileReader fr = null;
            BufferedReader br = null;
            String line;
            try {
                fr = new FileReader(file);
                br = new BufferedReader(fr);

                while ((line = br.readLine()) != null) {
                    // use tab as separator
                    String[] row = line.split("\t");
                    data.add(row);
                }

            } catch (FileNotFoundException e) {
                Snackbar.make(findViewById(R.id.fab), e.getMessage(), Snackbar.LENGTH_LONG).show();
            } catch (IOException e) {
                Snackbar.make(findViewById(R.id.fab), e.getMessage(), Snackbar.LENGTH_LONG).show();
            } finally {
                try {
                    if (br != null) br.close();
                    if (fr != null) fr.close();
                } catch (IOException e) {
                    Snackbar.make(findViewById(R.id.fab), e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        }
        return data;
    }
}
