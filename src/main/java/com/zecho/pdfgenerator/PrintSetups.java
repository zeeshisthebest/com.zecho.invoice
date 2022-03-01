package com.zecho.pdfgenerator;

import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;

public class PrintSetups {
    private final String invoice;
    private final Sheet sheet;

    public PrintSetups(String invoice, Sheet sheet) {
        this.invoice = "# " + invoice;
        this.sheet = sheet;
    }

    public void setProperties(){
        sheet.setFitToPage(true);
        sheet.getPrintSetup().setFitWidth((short)1);
        sheet.getPrintSetup().setPaperSize(PrintSetup.A4_PAPERSIZE);

        Header header = sheet.getHeader();
        header.setLeft(invoice);
        header.setCenter("&C&K000000&18INVOICE");

        Footer footer = sheet.getFooter();
        footer.setLeft("&L&K8A8A8A&10www.fb.com/SaleemChemicals");
        footer.setCenter("&C&K8A8A8A&10Receipt required for return and exchange\nTerms and Conditions applied.");
        footer.setRight( "&R&K8A8A8A&10Page " + HeaderFooter.page() + " of " + HeaderFooter.numPages() );

    }
}
