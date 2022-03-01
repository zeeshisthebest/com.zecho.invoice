package com.zecho.invoice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zecho.pdfgenerator.Item;

import java.util.ArrayList;
import java.util.Locale;

public class InvoiceActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ArrayList<Item> items;
    private AdapterItem adapterItem;
    private TextView tv_total_items;
    private TextView tv_previous_invoice;
    private EditText et_invoice, et_customer, et_phone, et_add, et_delivery, et_item_name,
            et_unit, et_quantity;
    private Spinner sp_unit;
    private String measuredUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        bindViews();

        final SharedPreferences sp = getSharedPreferences("DATA", MODE_PRIVATE);
        String inv = sp.getString("INVOICE", "00000");
        tv_previous_invoice.setText(String.format(getString(R.string.previous_invoice), inv));

        et_invoice.setText(String.format(Locale.US,"%06d", Integer.parseInt(inv) + 1));


        items = new ArrayList<>();
        updateTotal();

        RecyclerView recyclerView = findViewById(R.id.rv_items);

        adapterItem = new AdapterItem(items, this);

        recyclerView.setAdapter(adapterItem);

        recyclerView.setLayoutManager(new LinearLayoutManager(InvoiceActivity.this));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.measured_unit, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sp_unit.setAdapter(adapter);


    }

    /**
     * @param item The type Item to be removed
     */
    public void removeItem(Item item) {
        int index = items.indexOf(item);
        items.remove(index);
        adapterItem.notifyItemRemoved(index);
        updateTotal();
    }


    /**
     * Adds a new item to the Array
     */
    public void addItem() {
        String name = String.valueOf(et_item_name.getText()).trim();
        String unitString = String.valueOf(et_unit.getText()).trim();
        String quantityString = String.valueOf(et_quantity.getText()).trim();

        if (name.equals("")) {
            et_item_name.setError("please enter name");
        } else if (unitString.equals("")){
            et_unit.setError("Enter Unit Amount");
        } else if (quantityString.equals("")){
            et_quantity.setError("Enter Unit Amount");
        } else {
            int unit = Integer.parseInt(unitString);
            float quantity = Float.parseFloat(quantityString);
            items.add(new Item(unit, quantity, name, measuredUnit));
            adapterItem.notifyItemInserted(items.size());
            reset();
        }
    }

    /**
     * Binding the variable with their respective views
     */

    public void bindViews() {
        Button btn_reset_all = findViewById(R.id.btn_reset);
        btn_reset_all.setOnClickListener(this);

        Button btn_proceed = findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(this);

        et_add = findViewById(R.id.et_customer_address);

        et_customer = findViewById(R.id.et_customer_name);

        et_phone = findViewById(R.id.et_customer_phone);

        et_invoice = findViewById(R.id.et_invoice_no);

        et_delivery = findViewById(R.id.et_delivery);

        TextView tv_date = findViewById(R.id.tv_date);
        tv_date.setText(String.format(getResources().getString(R.string.date), java.time.LocalDate.now()));

        tv_previous_invoice = findViewById(R.id.tv_previous_invoice_number);


        tv_total_items = findViewById(R.id.tv_total_items);

        View includeLayout = findViewById(R.id.included);

        et_item_name = includeLayout.findViewById(R.id.et_item);
        et_unit = includeLayout.findViewById(R.id.et_unit);
        et_quantity = includeLayout.findViewById(R.id.et_quantity);

        sp_unit = includeLayout.findViewById(R.id.unit_spinner);
        sp_unit.setOnItemSelectedListener(this);

        Button btn_add_more = includeLayout.findViewById(R.id.btn_add_item);
        btn_add_more.setOnClickListener(this);

    }

    /**
     * @param view Implementation of ClickListener
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_add_item) {

            addItem();
            updateTotal();

        } else if (view.getId() == R.id.btn_proceed) {

            DataStorage dataStorage = DataStorage.getInstance();

            if (setCustomerInfo(dataStorage)) {
                dataStorage.setParent(this);
                Intent intent = new Intent(InvoiceActivity.this, ReviewData.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else if (view.getId() == R.id.btn_reset) {

            Intent intent = new Intent(InvoiceActivity.this, InvoiceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Upadtes the count of total items
     */
    private void updateTotal() {
        tv_total_items.setText(String.format(getResources().getString(R.string.tv_total_items), items.size()));
    }


    /**
     * Saves all the data to the class
     *
     * @param ds singleton of DataStorage
     */
    public boolean setCustomerInfo(DataStorage ds) {
        String name = String.valueOf(et_customer.getText()).trim();
        String invoice = String.valueOf(et_invoice.getText()).trim();

        if (name.equals("")) {
            et_customer.setError("Enter name please");
            return false;
        } else if (invoice.equals("")) {
            et_invoice.setError("Enter invoice please");
            return false;
        } else {
            String phone = String.valueOf(et_phone.getText());
            String address = String.valueOf((et_add.getText()));
            invoice = String.format(Locale.US, "%06d", Integer.parseInt(invoice));
            ds.setCustomer(name, phone, address, invoice);

            setItems(ds);
        }
        return true;
    }

    /**
     * @param dataStorage the singleton of dataStorage class
     */
    public void setItems(DataStorage dataStorage) {
        dataStorage.setItems(items);
        String tm = String.valueOf(et_delivery.getText()).trim();
        dataStorage.setDelivery( (tm.equals("")) ? 0 : Integer.parseInt(tm));
    }

    //Listener for the spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        measuredUnit = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        measuredUnit = "mg";
    }

    /**
     * Resets the Data Entry form
     */
    public void reset() {
        et_item_name.setText("");
        et_unit.setText("");
        et_quantity.setText("");
        sp_unit.setSelection(0);
    }

}