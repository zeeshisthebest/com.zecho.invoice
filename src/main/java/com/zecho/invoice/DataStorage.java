package com.zecho.invoice;

import com.zecho.pdfgenerator.Item;

import java.util.ArrayList;

public class DataStorage {
    private final String[] customer;
    private String invoice;
    private ArrayList<Item> items;
    private static DataStorage ds;
    private InvoiceActivity parent;
    private int delivery;

    public InvoiceActivity getParent() {
        return parent;
    }

    public void setParent(InvoiceActivity parent) {
        this.parent = parent;
    }

    protected DataStorage() {
        items = new ArrayList<>();
        customer = new String[3];
    }

    /**
     *
     * @return customer info
     */
    public String[] getCustomer(){
        return customer;
    }

    /**
     *
     * @return the list of items
     */
    public ArrayList<Item> getItems(){
        return items;
    }

    /**
     *
     * @param name Customer name
     * @param phone Customer Phone
     * @param add Customer Address
     * @param invoice Invoice Number
     */
    public void setCustomer(String name, String phone, String add, String invoice){
        customer[0] = name;
        customer[1] = phone;
        customer[2] = add;
        this.invoice = invoice;
    }

    /**
     *
     * @param items the list of items ArrayList<Item>
     */
    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    /**
     *
     * @return Returns the singleton
     */
    public static DataStorage getInstance(){
        if (ds == null){
            ds = new DataStorage();
        }
        return ds;
    }

    /**
     *Use this method to get the invoice number in string
     * @return The invoice number
     */
    public String getInvoice(){
        return invoice;
    }

    public void setDelivery(int delivery){ this.delivery = delivery;}

    public int getDelivery(){return delivery;}

}
