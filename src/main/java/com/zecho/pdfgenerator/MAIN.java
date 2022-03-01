package com.zecho.pdfgenerator;

import android.content.Context;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_LEFT;
import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_RIGHT;
import static org.apache.poi.ss.usermodel.CellStyle.BORDER_DOUBLE;
import static org.apache.poi.ss.usermodel.CellStyle.BORDER_THIN;
import static org.apache.poi.ss.usermodel.CellStyle.VERTICAL_TOP;

public class MAIN {
    public static int INVOICE_WIDTH_IN_CELLS = 10;
    private final String[] customer;
    private final String invoice;
    private final ArrayList<Item> items;
    private String name;
    protected Workbook workbook;

    /**
     *
     * @param customer Array of string containing customer info
     * @param invoice Invoice number
     * @param items Array of items list
     */
    public MAIN(String[] customer, String invoice, ArrayList<Item> items){
        this.customer = customer;
        this.invoice = invoice;
        this.items = items;
    }

    public void makeExcel(Context context) throws Exception {
        int nextRowLevel = 0; //To move down the rows easily.

        // Creating a PdfWriter
        workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("INVOICE");

        Font font = workbook.createFont();
        font.setFontName("Verdana");
        font.setBold(false);
        font.setFontHeightInPoints((short) 10);
        font.setColor(IndexedColors.BLACK.getIndex());

        //Cell Style for whole Invoice:
        CellStyle cellStyleRight = workbook.createCellStyle();
        cellStyleRight.setFont(font);
        cellStyleRight.setAlignment(ALIGN_RIGHT);
        cellStyleRight.setWrapText(true);


        CellStyle cellStyleCenter = workbook.createCellStyle();
        cellStyleCenter.setFont(font);
        cellStyleCenter.setAlignment(ALIGN_CENTER);


        CellStyle cellStyleLeft = workbook.createCellStyle();
        cellStyleLeft.setFont(font);
        cellStyleLeft.setAlignment(ALIGN_LEFT);
        cellStyleLeft.setWrapText(true);
        cellStyleLeft.setVerticalAlignment(VERTICAL_TOP);


        CellStyle cellStyleLeftB = workbook.createCellStyle();
        cellStyleLeftB.setFont(font);
        cellStyleLeftB.setAlignment(ALIGN_LEFT);
        cellStyleLeftB.setBorderBottom(BORDER_THIN);
        cellStyleLeftB.setBorderTop(BORDER_THIN);
        cellStyleLeftB.setBorderRight(BORDER_THIN);
        cellStyleLeftB.setBorderLeft(BORDER_THIN);


        CellStyle cellStyleCenterB = workbook.createCellStyle();
        cellStyleCenterB.setFont(font);
        cellStyleCenterB.setAlignment(ALIGN_CENTER);
        cellStyleCenterB.setBorderBottom(BORDER_THIN);
        cellStyleCenterB.setBorderTop(BORDER_THIN);
        cellStyleCenterB.setBorderRight(BORDER_THIN);
        cellStyleCenterB.setBorderLeft(BORDER_THIN);


        CellStyle cellStyleRightB = workbook.createCellStyle();
        cellStyleRightB.setFont(font);
        cellStyleRightB.setAlignment(ALIGN_RIGHT);
        cellStyleRightB.setBorderBottom(BORDER_THIN);
        cellStyleRightB.setBorderTop(BORDER_THIN);
        cellStyleRightB.setBorderRight(BORDER_THIN);
        cellStyleRightB.setBorderLeft(BORDER_THIN);

        CellStyle cellStyleRightD = workbook.createCellStyle();
        cellStyleRightD.setFont(font);
        cellStyleRightD.setAlignment(ALIGN_RIGHT);
        cellStyleRightD.setBorderTop(BORDER_DOUBLE);

        //Writing BioData
        BioDataWriter bioDataWriter = new BioDataWriter(sheet, 0, 3, 0, 9, cellStyleRight, workbook, context);
        nextRowLevel += bioDataWriter.writeNow(); //Increment to jump to customer area

        sheet.createRow(nextRowLevel++).createCell(0); //An Empty row

        //Writing Customer info
        CustomerInfoCreate customerInfoCreate = new CustomerInfoCreate(customer, cellStyleLeft, cellStyleRight, nextRowLevel, sheet);
        nextRowLevel = customerInfoCreate.writeNow();

        sheet.createRow(nextRowLevel++).createCell(0);  // Extra row after address

        HashMap<String, CellStyle> styles = new HashMap<>();
        styles.put("centerBordered", cellStyleCenterB);
        styles.put("rightBordered", cellStyleRightB);
        styles.put("leftBordered", cellStyleLeftB);
        styles.put("rightTop", cellStyleRightD);

        ItemsCreate itemsCreate = new ItemsCreate(sheet, nextRowLevel, styles, items);
        nextRowLevel = itemsCreate.writeNow();

        sheet.createRow(nextRowLevel++).createCell(0);                // Extra row before total

        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 12 * 256);
        for (int i = 2; i < 10; i++) {
            sheet.setColumnWidth(i, 9 * 256);
        }

        PrintSetups ps = new PrintSetups(invoice, sheet);
        ps.setProperties();
        name = "Invoice " + invoice + " " + java.time.LocalDate.now() + ".xlsx";

//        FileOutputStream fileOut = new FileOutputStream(path);
//        workbook.write(fileOut);
//        fileOut.close();
    }

    public String getFileName(){
        return name;
    }

    public void setPath(File path) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(path);
        workbook.write(fileOut);
        fileOut.close();
    }
}