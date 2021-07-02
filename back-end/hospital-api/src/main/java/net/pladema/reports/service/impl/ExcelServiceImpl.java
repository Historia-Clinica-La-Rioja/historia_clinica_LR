package net.pladema.reports.service.impl;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.reports.service.ExcelService;
import net.pladema.reports.service.domain.InformeMensualOC;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ExcelServiceImpl implements ExcelService {

    private ICellStyle basicStyle;
    private ICellStyle titleStyle;
    private ICellStyle fieldStyle;
    private ICellStyle subTitleStyle;

    @Override
    public IWorkbook buildExcelFromQuery(String title, String[] headers, Query query) {
        // creo el workbook de excel
        IWorkbook wb = WorkbookCreator.createExcelWorkbook();
        createCellStyle(wb);

        // obtengo los resultados desde la query
        List<Object[]> result = query.getResultList();

        // creo la hoja con el titulo pasado como parametro
        ISheet sheet = wb.createSheet(title);

        // Header
        fillRow(sheet, getHeaderData(headers));

        AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

        ICellStyle styleDataRow = createDataRowStyle(wb);

        List<InformeMensualOC> formatedContent = new ArrayList<>();
        result.forEach(r ->
                formatedContent.add(new InformeMensualOC((String) r[0], (String) r[1], (String) r[2], (String) r[3], (String) r[4],
                        (String) r[5], (String) r[6], (String) r[7], (String) r[8], (String) r[9], (String) r[10],
                        (String) r[11], (String) r[12], (String) r[13], (String) r[14], (String) r[15],
                        (String) r[16], (String) r[17], (String) r[18], (String) r[19], (String) r[20], (String) r[21]))
        );

        // itero el resultado creando una row para cada uno
        formatedContent.stream().forEach(
                resultData -> {
                    IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
                    fillRowContent(newDataRow, resultData, styleDataRow);
                }
        );

        setDimensions(sheet);
        return wb;
    }

    private void createCellStyle(IWorkbook workbook) {
        basicStyle = workbook.createStyle();
        basicStyle.setFontSize((short)12);
        basicStyle.setBold(false);
        basicStyle.setWrap(false);
        basicStyle.setBorders(true);
        basicStyle.setHAlign(ICellStyle.HALIGNMENT.CENTER);
        basicStyle.setVAlign(ICellStyle.VALIGNMENT.CENTER);

        titleStyle = workbook.createStyle();
        titleStyle.setFontSize((short)25);
        titleStyle.setBold(true);
        titleStyle.setWrap(false);
        titleStyle.setBorders(true);
        titleStyle.setHAlign(ICellStyle.HALIGNMENT.CENTER);
        titleStyle.setVAlign(ICellStyle.VALIGNMENT.CENTER);

        fieldStyle = workbook.createStyle();
        fieldStyle.setFontSize((short)10);
        fieldStyle.setBold(false);
        fieldStyle.setWrap(false);
        fieldStyle.setBorders(true);
        fieldStyle.setHAlign(ICellStyle.HALIGNMENT.LEFT);
        fieldStyle.setVAlign(ICellStyle.VALIGNMENT.BOTTOM);

        subTitleStyle = workbook.createStyle();
        subTitleStyle.setFontSize((short)12);
        subTitleStyle.setBold(true);
        subTitleStyle.setWrap(false);
        subTitleStyle.setBorders(true);
        subTitleStyle.setHAlign(ICellStyle.HALIGNMENT.LEFT);
        subTitleStyle.setVAlign(ICellStyle.VALIGNMENT.BOTTOM);
    }

    private List<CellContent> getHeaderData(String[] subtitles) {
        List<CellContent> data = new ArrayList<>();

        int nRow = 0;

        data.add(new CellContent(nRow, 0, 1, 2, "", basicStyle));
        data.add(new CellContent(nRow, 2, 2, 1, "2", titleStyle));
        data.add(new CellContent(nRow, 3, 2, 14,
                "Detalle nominal de consultorios externos", titleStyle));
        data.add(new CellContent(nRow, 17, 1, 3, "1. Hoja N°", fieldStyle));
        data.add(new CellContent(nRow, 20, 1, 1, "", basicStyle));
        data.add(new CellContent(nRow, 21, 1, 1, "", basicStyle));

        nRow++;
        data.add(new CellContent(nRow, 0, 1, 2, "", basicStyle));
        data.add(new CellContent(nRow, 17, 1, 5, "", basicStyle));

        nRow++;
        data.add(new CellContent(nRow, 0, 1, 2, "2. ESTABLECIMIENTO", fieldStyle));
        data.add(new CellContent(nRow, 2, 1, 12, "", basicStyle));
        data.add(new CellContent(nRow, 14, 1, 1, "3. MES", fieldStyle));
        data.add(new CellContent(nRow, 15, 1, 1, "", basicStyle));
        data.add(new CellContent(nRow, 16, 1, 1, "4. AÑO", fieldStyle));
        data.add(new CellContent(nRow, 17, 1, 5, "", basicStyle));

        nRow++;
        data.add(new CellContent(nRow, 0, 1, 2, "5. PARTIDO", fieldStyle));
        data.add(new CellContent(nRow, 2, 1, 8, "", basicStyle));
        data.add(new CellContent(nRow, 10, 1, 1, "6. REGIÓN SANITARIA", fieldStyle));
        data.add(new CellContent(nRow, 11, 1, 3, "", basicStyle));
        data.add(new CellContent(nRow, 14, 1, 3, "7. SERVICIO", fieldStyle));
        data.add(new CellContent(nRow, 17, 1, 3, "", basicStyle));
        data.add(new CellContent(nRow, 20, 1, 1, "", basicStyle));
        data.add(new CellContent(nRow, 21, 1, 1, "", basicStyle));

        nRow++;
        int column = 0;
        for(String subtitle : subtitles)
            data.add(new CellContent(nRow, column++, 1, 1, subtitle, subTitleStyle));

        return data;
    }

    private void setDimensions(ISheet sheet){
        //Column's width
        sheet.autoSizeColumns();
        //Row's height
        int nRow=0;
        sheet.setRowHeight(nRow++, 50);
        sheet.setRowHeight(nRow++, 40);
        sheet.setRowHeight(nRow++, 35);

        while(nRow < sheet.getCantRows())
            sheet.setRowHeight(nRow++, 21);
    }

    private ICellStyle createDataRowStyle(IWorkbook workbook){
        ICellStyle cellStyle = workbook.createStyle();
        cellStyle.setFontSize((short)12);
        cellStyle.setBorders(true);
        return cellStyle;
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
        cell.setCellValue(data);

        if(data.isCellRange())
            sheet.addMergedRegion(nRow, data.lastRow(), nColumn, data.lastCol(), true);
    }

    private void fillRowContent(IRow row, InformeMensualOC content, ICellStyle style){
        AtomicInteger rowNumber = new AtomicInteger(0);
            ICell cell = row.createCell(rowNumber.getAndIncrement());
            cell.setCellValue(content.getProvincia());
            cell.setCellStyle(style);
            ICell cell2 = row.createCell(rowNumber.getAndIncrement());
            cell2.setCellValue(content.getMunicipio());
            cell2.setCellStyle(style);
            ICell cell3 = row.createCell(rowNumber.getAndIncrement());
            cell3.setCellValue(content.getCodEstable());
            cell3.setCellStyle(style);
            ICell cell4 = row.createCell(rowNumber.getAndIncrement());
            cell4.setCellValue(content.getInstitucion());
            cell4.setCellStyle(style);
            ICell cell5 = row.createCell(rowNumber.getAndIncrement());
            cell5.setCellValue(content.getApellidosPaciente());
            cell5.setCellStyle(style);
            ICell cell6 = row.createCell(rowNumber.getAndIncrement());
            cell6.setCellValue(content.getNombresPaciente());
            cell6.setCellStyle(style);
            ICell cell7 = row.createCell(rowNumber.getAndIncrement());
            cell7.setCellValue(content.getTipoDocumento());
            cell7.setCellStyle(style);
            ICell cell8 = row.createCell(rowNumber.getAndIncrement());
            cell8.setCellValue(content.getNumeroDocumento());
            cell8.setCellStyle(style);
            ICell cell9 = row.createCell(rowNumber.getAndIncrement());
            cell9.setCellValue(content.getFechaNacimiento());
            cell9.setCellStyle(style);
            ICell cell10 = row.createCell(rowNumber.getAndIncrement());
            cell10.setCellValue(content.getGenero());
            cell10.setCellStyle(style);
            ICell cell11 = row.createCell(rowNumber.getAndIncrement());
            cell11.setCellValue(content.getDomicilio());
            cell11.setCellStyle(style);
            ICell cell12 = row.createCell(rowNumber.getAndIncrement());
            cell12.setCellValue(content.getNumeroTelefono());
            cell12.setCellStyle(style);
            ICell cell13 = row.createCell(rowNumber.getAndIncrement());
            cell13.setCellValue(content.getEmail());
            cell13.setCellStyle(style);
            ICell cell14 = row.createCell(rowNumber.getAndIncrement());
            cell14.setCellValue(content.getFechaInicio());
            cell14.setCellStyle(style);
            ICell cell15 = row.createCell(rowNumber.getAndIncrement());
            cell15.setCellValue(content.getEspecialidad());
            cell15.setCellStyle(style);
            ICell cell16 = row.createCell(rowNumber.getAndIncrement());
            cell16.setCellValue(content.getApellidosProfesional());
            cell16.setCellStyle(style);
            ICell cell17 = row.createCell(rowNumber.getAndIncrement());
            cell17.setCellValue(content.getRazonesConsulta());
            cell17.setCellStyle(style);
            ICell cell18 = row.createCell(rowNumber.getAndIncrement());
            cell18.setCellValue(content.getProblemas());
            cell18.setCellStyle(style);
            ICell cell19 = row.createCell(rowNumber.getAndIncrement());
            cell19.setCellValue(content.getPeso());
            cell19.setCellStyle(style);
            ICell cell20 = row.createCell(rowNumber.getAndIncrement());
            cell20.setCellValue(content.getTalla());
            cell20.setCellStyle(style);
            ICell cell21 = row.createCell(rowNumber.getAndIncrement());
            cell21.setCellValue(content.getTensionSistolica());
            cell21.setCellStyle(style);
            ICell cell22 = row.createCell(rowNumber.getAndIncrement());
            cell22.setCellValue(content.getTensionDiastolica());
            cell22.setCellStyle(style);

        }
}
