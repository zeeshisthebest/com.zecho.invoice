package com.zecho.pdfgenerator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class CustomerInfoCreate implements MethodsControll {
    private final String[] customer;
    private final CellStyle cellStyle;
    private final CellStyle right;
    private int startRow;
    private final Sheet sheet;
    private Row nameRow, phoneRow, addressRow;
    private final Cell[][] cells;


    /**
     *
     * @param customer String[3] array with name, phone, address.
     * @param cellStyle WorkBook.CellStyle object
     * @param startRow  starting Row index (nextRowLevel)
     * @param sheet Sheet object
     */
    protected CustomerInfoCreate(String[] customer, CellStyle cellStyle, CellStyle right, int startRow, Sheet sheet){
        this.customer = customer;
        this.cellStyle = cellStyle;
        this.startRow = startRow;
        this.right = right;
        this.sheet = sheet;
        cells = new Cell[3][MAIN.INVOICE_WIDTH_IN_CELLS];
    }

    public int writeData() {
        this.cellStyle.setWrapText(true);

        String date = "Date: " + java.time.LocalDate.now();
        cells[0][7].setCellValue(date);
        cells[0][7].setCellStyle(right);

        customer[0] = "Name: " + customer[0];
        customer[1] = "Phone: " + customer[1];
        customer[2] = "Address: " + customer[2];

        for(int i = 0; i < 3; i++){
            cells[i][0].setCellValue(customer[i]);
            cells[i][0].setCellStyle(this.cellStyle);
            ++startRow;
        }
        return startRow;
    }

    public int writeNow() {
        createRows();
        createCells();
        return writeData();
    }

    @Override
    public void createCells() {
        for (int i = 0; i < MAIN.INVOICE_WIDTH_IN_CELLS; i++){
            cells[0][i] = nameRow.createCell(i);
            cells[1][i] = phoneRow.createCell(i);
            cells[2][i] = addressRow.createCell(i);
        }

        for(int i = 0; i < 3; i++) {
            sheet.addMergedRegion(new CellRangeAddress(startRow + i, startRow + i, 0, 6));
        }
        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 7, 9));
    }

    @Override
    public void createRows() {
        nameRow = sheet.createRow(startRow);
        phoneRow = sheet.createRow(startRow + 1);
        addressRow = sheet.createRow(startRow + 2);
        int height = (int) Math.ceil( (double) customer[2].length() / 40 );
        addressRow.setHeight((short) (addressRow.getHeight() * height));

    }
}
