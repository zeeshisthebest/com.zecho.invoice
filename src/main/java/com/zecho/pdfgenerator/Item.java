package com.zecho.pdfgenerator;

public class Item {
    private final int unitPrice;
    private final float quantity;
    private final String itemName;
    private final String measuringUnit;

    /**
     * @param unitPrice     unit price
     * @param quantity      quantity of the items
     * @param itemName      Item name
     * @param measuringUnit unit used to quote unit price
     */
    public Item(int unitPrice, float quantity, String itemName, String measuringUnit) {
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.itemName = itemName;
        this.measuringUnit = measuringUnit;
    }

    public String getUnitPrice() {
        return String.valueOf(unitPrice);
    }

    public String getQuantity() { return String.valueOf(quantity);}

    public String getTotalString() {
        return "Total rs/=" + quantity * unitPrice;
    }

    public int getTotal(){
        return (int) (quantity * unitPrice);
    }

    public String getItemName() {
        return itemName;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }
}
