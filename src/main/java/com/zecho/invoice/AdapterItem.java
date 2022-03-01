package com.zecho.invoice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zecho.pdfgenerator.Item;

import java.util.ArrayList;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ViewHolder> {

    private ArrayList<Item> items;
    private final InvoiceActivity ia;


    public AdapterItem(ArrayList<Item> items, InvoiceActivity ia) {
        this.items = items;
        this.ia = ia;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Item currentItem = items.get(position);
        viewHolder.et_item_name.setText(currentItem.getItemName());
        viewHolder.et_item_name.setEnabled(false);
        viewHolder.et_item_name.setFocusable(false);

        viewHolder.et_unit_price.setText(currentItem.getUnitPrice());
        viewHolder.et_unit_price.setEnabled(false);
        viewHolder.et_unit_price.setFocusable(false);

        viewHolder.et_quantity.setText(currentItem.getQuantity());
        viewHolder.et_quantity.setEnabled(false);
        viewHolder.et_quantity.setFocusable(false);

        viewHolder.tv_total.setText(currentItem.getTotalString());

        viewHolder.btn_remove.setOnClickListener(view -> ia.removeItem(currentItem));

        viewHolder.tv_measured.setText("Measured in: " + currentItem.getMeasuringUnit());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_total, tv_measured;
        EditText et_item_name, et_unit_price, et_quantity;
        Button btn_remove;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            tv_total = view.findViewById(R.id.ll_tv_total);
            et_item_name = view.findViewById(R.id.ll_et_item);
            et_unit_price = view.findViewById(R.id.ll_et_unit);
            et_quantity = view.findViewById(R.id.ll_et_quantity);
            btn_remove = view.findViewById(R.id.ll_btn_remove);
            tv_measured = view.findViewById(R.id.ll_tv_measured);
        }

    }
}
