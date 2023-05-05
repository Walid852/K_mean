package com.example.k_mean;


import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ExcelReader {

    public List<List<Double>> readExcelFile(String filePath) {
        try {
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            List<List<Double>> transactions=new LinkedList<>();
            int x=0;
            for (Row row : sheet) {
                List<Double> transaction=new LinkedList<>();
                if(x!=0) {
                    for (Cell cell : row) {
                        switch (cell.getCellType()) {
                            /*case STRING -> {
                                String[] L=cell.getStringCellValue().split(" ");
                                transaction.add(Double.parseDouble(L[1]));
                            }*/
                            case NUMERIC -> {
                                transaction.add(cell.getNumericCellValue());
                            }
                        }
                    }
                    transactions.add(transaction);
                }
                x++;
            }
            /*for (List<Double> transactionR : transactions) {
                for (Double item : transactionR) {
                    System.out.print(item + "\t");
                }
                System.out.println();
            }*/
            workbook.close();
            inputStream.close();
            return transactions;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
