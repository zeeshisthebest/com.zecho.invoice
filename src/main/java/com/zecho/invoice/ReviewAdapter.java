package com.zecho.invoice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zecho.pdfgenerator.Item;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private ArrayList<Item> items;

    ReviewAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item currentItem = items.get(position);
        holder.et_item_name.setText(currentItem.getItemName());
        holder.et_item_name.setEnabled(false);
        holder.et_item_name.setFocusable(false);

        holder.et_unit_price.setText(currentItem.getUnitPrice());
        holder.et_unit_price.setEnabled(false);
        holder.et_unit_price.setFocusable(false);

        holder.et_quantity.setText(currentItem.getQuantity());
        holder.et_quantity.setEnabled(false);
        holder.et_quantity.setFocusable(false);

        holder.tv_total.setText(currentItem.getTotalString());

        holder.tv_measured.setText("Measured in: " + currentItem.getMeasuringUnit());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_total, tv_measured;
        EditText et_item_name, et_unit_price, et_quantity;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            tv_total = view.findViewById(R.id.tv_total_items);
            et_item_name = view.findViewById(R.id.et_item);
            et_unit_price = view.findViewById(R.id.et_unit);
            et_quantity = view.findViewById(R.id.et_quantity);
            tv_measured = view.findViewById(R.id.tv_measured);
        }
    }
}

