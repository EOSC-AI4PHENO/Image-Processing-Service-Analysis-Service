package com.siseth.analysis.component.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public final class ExcelCreator extends FileReader {

    private Map<String, Object> header;

    private List<Map<String,Object>> data;

    public ExcelCreator(List<Map<String,Object>> data) {
        this.data = data;
    }
    @Override
    public File createFile(){
        if(data.size() == 0)
            return null;

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("sheet1");

        int initialValue = createHeader(workbook, sheet);

        AtomicInteger i = new AtomicInteger(initialValue);

        Map<String,Object> header = data.get(0);

        createHeaderRow(workbook, sheet, header, i.getAndIncrement());
        for (Map<String, Object> stringObjectMap : data) {
            createValueRow(sheet, stringObjectMap, i.getAndIncrement());
        }

        autoSizeColumn(sheet, initialValue);
        return createFileByWorkBook(workbook, "file");
    }


    private int createHeader(Workbook workbook, Sheet sheet) {
         int i = 0;
        if(this.header == null)
            return i;

        createHeaderRow(workbook, sheet, this.header, i);
        createValueRow(sheet, this.header, ++i);
        return i + 2;
    }
    private void createHeaderRow(Workbook workbook, Sheet sheet, Map<String, Object> map, int rowInt) {
        int j = 0;
        Cell cell;
        Row row = sheet.createRow(rowInt);
        for (String key : map.keySet()) {
            if(!isExcluded(key)){
                cell = row.createCell(j++);
                cell.setCellValue(key);
                cell.setCellStyle(getHeaderCellStyle(workbook));
            }
        }
    }
    private void createValueRow(Sheet sheet, Map<String, Object> map, int rowInt) {
        int j = 0;
        Cell cell;
        Row row = sheet.createRow(rowInt);
        for (String key : map.keySet()) {
            if(!isExcluded(key)){
                Object value = map.get(key);
                cell = row.createCell(j++);
                cell.setCellValue(tryCast(value));
            }
        }
    }


    @SneakyThrows
    private File createFileByWorkBook(Workbook workbook,String fileName){
        File file =  File.createTempFile(fileName,".xlsx");
        file.deleteOnExit();
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        return file;
    }

    private void autoSizeColumn(Sheet sheet, int rownum){
        for(int j = 0 ; j< sheet.getRow(rownum).getLastCellNum(); j++ )
            sheet.autoSizeColumn(j);
    }

    private CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }


}
