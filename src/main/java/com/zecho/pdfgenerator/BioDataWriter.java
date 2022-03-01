package com.zecho.pdfgenerator;

import android.content.Context;
import android.content.res.Resources;

import com.zecho.invoice.R;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;

import java.io.IOException;
import java.io.InputStream;

public class BioDataWriter implements MethodsControll {
    private final Sheet sheet;
    int iniRow, iniCol, lastRow, lastCol;
    private final Row[] rows;
    private final Cell[][] cells;
    private final Workbook wb;
    Context context;
    private final CellStyle cellStyle;

    /**
     * @param sheet     The sheet to be worked
     * @param iniRow    Staring Row
     * @param lastRow   Ending Row
     * @param iniCol    Starting Column
     * @param lastCol   Ending Column
     * @param cellStyle Workbook.CellStyle() object
     */
    BioDataWriter(Sheet sheet, int iniRow, int lastRow, int iniCol, int lastCol, CellStyle cellStyle, Workbook wb, Context context) {
        this.sheet = sheet;
        this.iniRow = iniRow;
        this.iniCol = iniCol;
        this.lastCol = lastCol;
        this.context = context;
        this.wb = wb;
        this.lastRow = lastRow;
        this.cellStyle = cellStyle;
        rows = new Row[lastRow - iniRow + 1];
        cells = new Cell[rows.length][lastCol - iniCol + 1];
    }

    /*
    invokes the writing of header
     */
    public int writeNow() throws IOException {
        this.createRows();
        this.createCells();
        sheet.addMergedRegion(new CellRangeAddress(iniRow, lastRow, iniCol, lastCol));
        return writeData();
    }

    /*
    Creates the rows
     */
    public void createRows() {
        for (int i = 0; i < lastRow - iniRow + 1; i++) {
            rows[i] = sheet.createRow(i);
        }
    }

    /*
    Creates the cells
     */
    public void createCells() {
        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < lastCol - iniCol; j++) {
                cells[i][j] = rows[i].createCell(j);
            }
        }
    }

    /*
    Writes in the header
     */
    public int writeData() throws IOException {

//        InputStream logo = BioDataWriter.class.getClassLoader().getResourceAsStream("logo.png");
        InputStream logo = context.getResources().openRawResource(R.raw.logo);
        byte[] logoByte = IOUtils.toByteArray(logo);
        int inputImagePictureID1 = wb.addPicture(logoByte, Workbook.PICTURE_TYPE_PNG);
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
        XSSFClientAnchor logoAnchor = new XSSFClientAnchor();
        logoAnchor.setCol1(0); // Sets the column (0 based) of the first cell.
        logoAnchor.setCol2(2); // Sets the column (0 based) of the Second cell.
        logoAnchor.setRow1(0); // Sets the row (0 based) of the first cell.
        logoAnchor.setRow2(4); // Sets the row (0 based) of the Second cell.
        drawing.createPicture(logoAnchor, inputImagePictureID1);

        String str1 = "0333 - 2384042";
        String str2 = "0332 - 2569183";
        String str3 = "Office# 17, B - Road";
        String str4 = "Liaquatabad, Karachi";
//        String strFinal = String.format("%s\n%s\n%s\n%s",
//                str1, str2, str3, str4);
        String strFinal = str1 + "\n" + str2 + "\n" + str3 + "\n" + str4;
        cells[0][0].setCellValue(strFinal);
        cells[0][0].setCellStyle(this.cellStyle);

        return 4;

    }

}
