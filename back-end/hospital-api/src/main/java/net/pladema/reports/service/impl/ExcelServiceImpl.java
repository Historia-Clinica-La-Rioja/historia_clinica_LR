package net.pladema.reports.service.impl;

import net.pladema.reports.service.ExcelService;
import net.pladema.reports.util.manager.WorkbookCreator;
import net.pladema.reports.util.struct.CellStyle;
import net.pladema.reports.util.struct.ICell;
import net.pladema.reports.util.struct.IRow;
import net.pladema.reports.util.struct.ISheet;
import net.pladema.reports.util.struct.IWorkbook;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExcelServiceImpl implements ExcelService {

    private static short FONT_SIZE = 12;

    @Override
    public IWorkbook buildExcelFromQuery(String title, String[] headers, Query query) {
        // creo el workbook de excel
        IWorkbook wb = WorkbookCreator.createExcelWorkbook();

        // obtengo los resultados desde la query
        List<Object> result = query.getResultList();

        // creo la hoja con el titulo pasado como parametro
        ISheet sheet = wb.createSheet(title);

        AtomicInteger rowNumber = new AtomicInteger(0);
        // creo la columna de headers
        IRow headerRow = sheet.createRow(rowNumber.getAndIncrement());

        CellStyle styleHeader = createHeaderStyle();

        fillRow(headerRow, headers, styleHeader);

        CellStyle styleDataRow = createDataRowStyle();

        // itero el resultado creando una row para cada uno
        result.stream().forEach(
                resultData -> {
                    IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
                    fillRow(newDataRow, (Object[]) resultData, styleDataRow);
                }
        );

        sheet.autoSizeColumns();

        return wb;
    }

    private CellStyle createHeaderStyle(){
        CellStyle cellStyle = new CellStyle();
        cellStyle.setBold(true);
        cellStyle.setFontSize(FONT_SIZE);

        return cellStyle;
    }

    private CellStyle createDataRowStyle(){
        CellStyle cellStyle = new CellStyle();
        cellStyle.setFontSize(FONT_SIZE);

        return cellStyle;
    }

    private void fillRow(IRow row, Object[] content, CellStyle style){
        AtomicInteger rowNumber = new AtomicInteger(0);
        Arrays.stream(content).forEach(
                item -> {
                    ICell cell = row.createCell(rowNumber.getAndIncrement());
                    cell.setCellValue(item != null ? item.toString() : "");
                    cell.setCellStyle(style);
                }
        );
    }
}
