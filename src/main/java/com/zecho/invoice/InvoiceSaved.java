package com.zecho.invoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class InvoiceSaved extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saving_done);

        File file = (File) getIntent().getExtras().get("file");
        String name = (String) getIntent().getExtras().get("name");

        TextView tv_saved = findViewById(R.id.textView3);

        tv_saved.setText(String.format(getResources().getString(R.string.saved), name));

        Button btn_exit = findViewById(R.id.btn_exit);
        Button btn_new_invoice = findViewById(R.id.btn_create_new);
        Button btn_open = findViewById(R.id.btn_open);

        btn_exit.setOnClickListener(view -> {this.finish(); System.exit(0);});

        btn_new_invoice.setOnClickListener(view -> {
            Intent reopen = new Intent(this, InvoiceActivity.class);
            reopen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(reopen);
            this.finish();
        });

        btn_open.setOnClickListener(view -> {
            startActivity(getExcelFileIntent(file));
        });

    }

    public Intent getExcelFileIntent(File file) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = FileProvider.getUriForFile(InvoiceSaved.this, BuildConfig.APPLICATION_ID + ".provider",file);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }
}