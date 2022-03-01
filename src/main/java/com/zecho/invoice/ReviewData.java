package com.zecho.invoice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zecho.pdfgenerator.Item;
import com.zecho.pdfgenerator.MAIN;

import java.io.File;
import java.util.ArrayList;

public class ReviewData extends AppCompatActivity {
    private AlertBox loadingdialog;
    private ReviewData context;
    private File file;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_data);

        DataStorage ds = DataStorage.getInstance();
        TextView tv = findViewById(R.id.tv_customer_info);
        TextView tv2 = findViewById(R.id.rv_tv_invoice);
        TextView tv_grand_total = findViewById(R.id.tv_grand_total);
        loadingdialog = new AlertBox(ReviewData.this);
        Button btn_save = findViewById(R.id.btn_confirm);
        Button btn_back = findViewById(R.id.btn_back);

        final SharedPreferences storage = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = storage.edit();

        float grandTotal = 0;
        ArrayList<Item> items = new ArrayList<>(ds.getItems());
        items.add(new Item(ds.getDelivery(), 1, "Delivery / Packaging", "-"));

        //Calculating the Total
        for (int i = 0; i < items.size(); i++) {
            grandTotal += items.get(i).getTotal();
        }

        //Setting Invoice Number
        tv2.setText(String.format(getResources().getString(R.string.invoice), ds.getInvoice()));

        //Setting Customer Info
        String tmp = String.format(getResources().getString(R.string.review_customer_info), ds.getCustomer()[0],
                ds.getCustomer()[1], ds.getCustomer()[2]);

        tv.setText(tmp);

        //Recyclerview for Showing items
        RecyclerView rv = findViewById(R.id.rv_items_confirm);

        ReviewAdapter reviewAdapter = new ReviewAdapter(items);

        rv.setAdapter(reviewAdapter);

        rv.setLayoutManager(new LinearLayoutManager(this));

        //Showing the total Amount
        tv_grand_total.setText(String.format(getResources().getString(R.string.grand_total), String.valueOf(grandTotal)));

        //Back Button
        btn_back.setOnClickListener(view -> this.finish());

        //Saving Button
        btn_save.setOnClickListener(view -> {
            loadingdialog.startLoadingdialog();
            ds.setItems(items);
            ds.getParent().finish();
            context = this;
            MAIN main = new MAIN(ds.getCustomer(), ds.getInvoice(), ds.getItems());

            editor.putString("INVOICE", ds.getInvoice());
            editor.commit();

            new SavingTask().execute(main);
        });
    }

    private class SavingTask extends AsyncTask<MAIN, Void, Intent> {
        protected Intent doInBackground(MAIN... main) {
            try {
                main[0].makeExcel(ReviewData.this);
                fileName = main[0].getFileName();
                main[0].setPath(getDocStorage(context, fileName));

                MediaScannerConnection.scanFile(context,
                            new String[] { file.toString() }, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> uri=" + uri);
                                }
                            });
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent i = new Intent(ReviewData.this, InvoiceSaved.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return i;
        }

        protected void onProgressUpdate(Void... voids) {

        }

        protected void onPostExecute(Intent intent) {
            intent.putExtra("file", file);
            intent.putExtra("name", fileName);
            loadingdialog.dismissdialog();
            startActivity(intent);
            context.finish();
        }

    }

    public File getDocStorage(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        file = new File(path, albumName);
        if (!path.mkdirs()) {
            Log.e("log", "Directory not created");
        }
        return file;
    }

}