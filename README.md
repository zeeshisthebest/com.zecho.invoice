# com.zecho.invoice
A simple app developed to generate invoices for the client.
I converted my python program (https://github.com/zeeshisthebest/ShopPOS-python-) in java to support Android platform.

I used **Apache Poi** to generate the output in excel which can be printed to pdf using MS Office mobile app. The jar used as the library is modified version of Apache POI original lib because there's is a bug in netbeans that throws some duplication error when building the android app using gradle.

## Screenshots
- This is the main screen of the app.
![Main screen](/snapshots/1.png)
- Dummy data
![data](/snapshots/2.png)

- Confirmation screen.
![confirm](/snapshots/3.png)

- After the file has been saved.
![saving](/snapshots/4.png)

- The excel produced by the app.
![excel](/snapshots/excel.jpg)

- PDF after "print-to-pdf" from MS Excel.
![pdf](/snapshots/pdf.jpg)
