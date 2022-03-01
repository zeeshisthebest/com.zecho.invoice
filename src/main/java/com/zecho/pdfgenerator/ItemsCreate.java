package com.zecho.pdfgenerator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemsCreate implements MethodsControll{
    private final Sheet sheet;
    private int startRow;
    private final HashMap<String, CellStyle> styles;
    private final Row[] rows;
    private final Cell[][] cells;
    private final ArrayList<Item> items;
    private float total = 0;

    /**
     *
     * @param sheet sheet object
     * @param startRow starting row index (nextRowLevel)
     * @param styles HashMap<String, Cellstyle> Object
     * @param items list of all the items
     */
    public ItemsCreate(Sheet sheet, int startRow, HashMap<String, CellStyle> styles, ArrayList<Item> items) {
        this.sheet = sheet;
        this.startRow = startRow;
        this.styles = styles;
        this.items = items;
        rows = new Row[items.size() + 1];
        cells = new Cell[rows.length][MAIN.INVOICE_WIDTH_IN_CELLS];
    }


    @Override
    public void createRows() {

        for (int i = 0; i < rows.length ; i++){
            rows[i] = sheet.createRow(startRow + i);
        }
    }

    @Override
    public void createCells() {
        for (int i = 0; i < rows.length; i++){
            for (int j = 0; j < MAIN.INVOICE_WIDTH_IN_CELLS; j++){
                cells[i][j] = rows[i].createCell(j);
            }
        }

        for(int i = 0; i < rows.length; i++) {

            for(int j = 0; j < cells[i].length; j++){
                cells[i][j].setCellStyle(styles.get("centerBordered"));
            }
            sheet.addMergedRegion(new CellRangeAddress(startRow + i, startRow + i, 1, 6));
        }

    }

    public int writeData() {
        //To Write Item Title
        String[] itemsTitle = {"#", "Item", "Unit", "Quantity", "Total"};

        for (int i = 0 ; i < 5; i++){
            if (i > 1) {
                cells[0][i + 5].setCellValue(itemsTitle[i]);
                cells[0][i + 5].setCellStyle(styles.get("centerBordered"));
            } else {
                cells[0][i].setCellValue(itemsTitle[i]);
                cells[0][i].setCellStyle(styles.get("centerBordered"));
            }

        }

        ++startRow;

        //Item listing
        for (int i = 1; i <= items.size(); i++){
            cells[i][0].setCellValue(i);
            cells[i][0].setCellStyle(styles.get("centerBordered"));

            cells[i][1].setCellValue(" " + items.get(i-1).getItemName());
            cells[i][1].setCellStyle(styles.get("leftBordered"));

            cells[i][7].setCellValue(items.get(i-1).getUnitPrice() + " ");
            cells[i][7].setCellStyle(styles.get("rightBordered"));

            cells[i][8].setCellValue(items.get(i-1).getQuantity() + " ");
            cells[i][8].setCellStyle(styles.get("rightBordered"));

            cells[i][9].setCellValue(items.get(i-1).getTotal() + " ");
            cells[i][9].setCellStyle(styles.get("rightBordered"));

            total += items.get(i-1).getTotal();

            ++startRow;
        }

        sheet.createRow(startRow++);

        Row totalRow = sheet.createRow(startRow);

        Cell totalCell = totalRow.createCell(0);

        for (int i = 1; i < MAIN.INVOICE_WIDTH_IN_CELLS; i++){
            totalRow.createCell(i).setCellStyle(styles.get("rightTop"));
        }

        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 0 ,9));

        totalCell.setCellValue("TOTAL: Rs/=" + total + " ");

        totalCell.setCellStyle(styles.get("rightTop"));

        ++startRow;

        return startRow;
    }

    public int writeNow() {
        createRows();
        createCells();
        return writeData();
    }
}
