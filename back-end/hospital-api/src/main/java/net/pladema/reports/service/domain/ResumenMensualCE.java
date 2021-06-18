package net.pladema.reports.service.domain;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.CellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResumenMensualCE {

    private IWorkbook workbook;

    private CellStyle basicStyle;
    private CellStyle titleStyle;
    private CellStyle wrapStyle;
    private CellStyle fieldStyle;

    public ResumenMensualCE(){
        workbook = WorkbookCreator.createExcelWorkbook();
        createCellStyle();
    }

    public IWorkbook build() {
        try {
            ISheet sheet = workbook.createSheet("Hoja 2.1");

            /* Fill Data*/
            fillRow(sheet, getHeaderData());
            int lastRow = sheet.getCantRows();
            fillRow(sheet, getFooterData(lastRow));

            setDimensions(sheet);
            return workbook;
        }
        catch(Exception e){
            return null;
        }
    }


    private List<CellContent> getHeaderData(){
        List<CellContent> data = new ArrayList<>();

        int nRow=0;
        data.add(new CellContent(nRow, 0,1, 4,
                "PROVINCIA DE BUENOS AIRES MINISTERIO DE SALUD", wrapStyle));
        data.add(new CellContent(nRow, 4,2, 2, "2,1", titleStyle));
        data.add(new CellContent(nRow, 6,2, 16,
                "Resumen Mensual de Consultorio Externo", titleStyle));
        data.add(new CellContent(nRow, 22,1, 1, "1. Hoja N°", basicStyle));
        data.add(new CellContent(nRow, 23,1, 1, "", basicStyle));
        data.add(new CellContent(nRow, 24,1, 1, "", basicStyle));

        nRow++;

        data.add(new CellContent(nRow, 0, 1, 4,
                "Dirección de Información Sistematizada", basicStyle));
        data.add(new CellContent(nRow, 22, 1, 3, "", basicStyle));

        nRow++;
        data.add(new CellContent(nRow, 0, 1, 4, "2. ESTABLECIMIENTO", fieldStyle));
        data.add(new CellContent(nRow, 4, 1, 14, "", basicStyle));
        data.add(new CellContent(nRow, 18, 1, 2, "3. MES", fieldStyle));
        data.add(new CellContent(nRow, 20, 1, 1, "", basicStyle));
        data.add(new CellContent(nRow, 21, 1, 1, "", basicStyle));
        data.add(new CellContent(nRow, 22, 1, 1, "4. AÑO", fieldStyle));
        data.add(new CellContent(nRow, 23, 1, 2, "", basicStyle));

        nRow++;

        data.add(new CellContent(nRow, 0, 1, 4, "5. PARTIDO", fieldStyle));
        data.add(new CellContent(nRow, 4, 1, 8, "", basicStyle));
        data.add(new CellContent(nRow, 12, 1, 8,
                "6. DEPENDENCIA ADMINISTRATIVA", fieldStyle));
        data.add(new CellContent(nRow, 20, 1, 2, "", basicStyle));
        data.add(new CellContent(nRow, 22, 1, 1, "7. REGIÓN SANITARIA", fieldStyle));
        data.add(new CellContent(nRow, 23, 1, 1, "", basicStyle));
        data.add(new CellContent(nRow, 24, 1, 1, "", basicStyle));

        nRow++;
        data.add(new CellContent(nRow, 0, 3, 1, "8. SERVICIO", basicStyle));
        data.add(new CellContent(nRow, 1, 3, 3, "9. CÓDIGO", basicStyle));
        data.add(new CellContent(nRow, 4, 1, 18, "10. EDAD Y SEXO", basicStyle));
        data.add(new CellContent(nRow, 22, 3, 1, "11. TOTAL", basicStyle));
        data.add(new CellContent(nRow, 23, 2, 2, "12. OBRA SOCIAL", basicStyle));

        nRow++;
        data.add(new CellContent(nRow, 4, 1, 2, "< 1 año", basicStyle));
        data.add(new CellContent(nRow, 6, 1, 2, "1 a 4", basicStyle));
        data.add(new CellContent(nRow, 8, 1, 2, "5 a 9", basicStyle));
        data.add(new CellContent(nRow, 10, 1, 2, "10 a 14", basicStyle));
        data.add(new CellContent(nRow, 12, 1, 2, "15 a 19", basicStyle));
        data.add(new CellContent(nRow, 14, 1, 2, "20 a 34", basicStyle));
        data.add(new CellContent(nRow, 16, 1, 2, "35 a 49", basicStyle));
        data.add(new CellContent(nRow, 18, 1, 2, "50 a 64", basicStyle));
        data.add(new CellContent(nRow, 20, 1, 2, "> 65", basicStyle));

        nRow++;
        String[] sexo = new String[]{"V","M"};
        for(int i=4; i<22; i++)
            data.add(new CellContent(nRow, i, 1, 1, sexo[i%2], basicStyle));
        data.add(new CellContent(nRow, 23, 1, 1, "SI", basicStyle));
        data.add(new CellContent(nRow, 24, 1, 1, "NO", basicStyle));

        return data;
    }

    private List<CellContent> getFooterData(int nRow) {
        List<CellContent> data = new ArrayList<>();
        int nColumn = 0;
        data.add(new CellContent(nRow, nColumn,1, 4, "13. TOTALES", fieldStyle));
        nColumn+=4;
        while(nColumn < 25)
            data.add(new CellContent(nRow, nColumn++, 1, 1, "", basicStyle));
        return data;
    }

    private void createCellStyle(){
        basicStyle = new CellStyle();
        basicStyle.setFontSize((short)10);
        basicStyle.setBold(false);
        basicStyle.setWrap(false);
        basicStyle.setBorders(true);
        basicStyle.setHalign(CellStyle.HALIGNMENT.CENTER);
        basicStyle.setVAlign(CellStyle.VALIGNMENT.CENTER);


        titleStyle = new CellStyle();
        titleStyle.setFontSize((short)25);
        titleStyle.setBold(true);
        titleStyle.setWrap(false);
        titleStyle.setBorders(true);
        titleStyle.setHalign(CellStyle.HALIGNMENT.CENTER);
        titleStyle.setVAlign(CellStyle.VALIGNMENT.CENTER);

        wrapStyle = new CellStyle();
        wrapStyle.setFontSize((short)10);
        wrapStyle.setBold(false);
        wrapStyle.setWrap(true);
        wrapStyle.setBorders(true);
        wrapStyle.setHalign(CellStyle.HALIGNMENT.CENTER);
        wrapStyle.setVAlign(CellStyle.VALIGNMENT.CENTER);

        fieldStyle = new CellStyle();
        fieldStyle.setFontSize((short)10);
        fieldStyle.setBold(false);
        fieldStyle.setWrap(true);
        fieldStyle.setBorders(true);
        fieldStyle.setHalign(CellStyle.HALIGNMENT.LEFT);
        fieldStyle.setVAlign(CellStyle.VALIGNMENT.BOTTOM);

    }

    private void setDimensions(ISheet sheet){
        //Column's width
        sheet.setColumnWidth(0, 150);
        for (int i=1; i<22; i++)
            sheet.setColumnWidth(i, 50);
        sheet.setColumnWidth(22, 150);
        sheet.setColumnWidth(23, 60);
        sheet.setColumnWidth(24, 60);

        //Row's height
        int nRow=0;
        sheet.setRowHeight(nRow++, 50);
        sheet.setRowHeight(nRow++, 40);
        sheet.setRowHeight(nRow++, 35);
        while(nRow < 6)
            sheet.setRowHeight(nRow++, 21);
    }

    private void fillRow(ISheet sheet, List<CellContent> data){
        Map<Integer, List<CellContent>> cellByRow = data.stream()
                .collect(Collectors.groupingBy(CellContent::getRow));

        for(Map.Entry<Integer, List<CellContent>> entry : cellByRow.entrySet()){
            int nRow = entry.getKey();
            IRow row = sheet.createRow(nRow);
            entry.getValue().forEach(cell -> createCell(sheet, row, nRow, cell));
        }
    }

    private void createCell(ISheet sheet, IRow row, int nRow, CellContent data){

        int nColumn = data.getColumn();

        ICell cell = row.createCell(data.getColumn());
        cell.setCellStyle(data.getStyle());
        cell.setCellValue(data.getValue());

        CellRangeAddress combined = new CellRangeAddress(nRow, data.lastRow(), nColumn, data.lastCol());
        if(combined.getNumberOfCells() > 1)
            sheet.addMergedRegion(nRow, data.lastRow(), nColumn, data.lastCol());
    }
}
