package net.pladema.reports.service.impl;

import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.reports.service.ExcelService;
import net.pladema.reports.service.domain.RowVo;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ExcelServiceImpl implements ExcelService {

    private static short FONT_SIZE = 12;

    @Override
    public IWorkbook buildExcelFromQuery(String title, String[] headers, Query query) {
        // creo el workbook de excel
        IWorkbook wb = WorkbookCreator.createExcelWorkbook();

        // obtengo los resultados desde la query
        List<Object[]> result = query.getResultList();

        // creo la hoja con el titulo pasado como parametro
        ISheet sheet = wb.createSheet(title);

        AtomicInteger rowNumber = new AtomicInteger(0);
        // creo la columna de headers
        IRow headerRow = sheet.createRow(rowNumber.getAndIncrement());

        ICellStyle styleHeader = createHeaderStyle(wb);

        fillRow(headerRow, headers, styleHeader);

        ICellStyle styleDataRow = createDataRowStyle(wb);

        /*provincia,municipio,codEstable,institucion,apellidosPaciente,nombresPaciente,tipoDocumento,numeroDocumento,fechaNacimiento,genero,
        domicilio,numeroTelefono,email,fechaInicio,especialidad,apellidosProfesional,List<String> razonesConsulta;*/

        List<RowVo> formatedContent = new ArrayList<>();
        result.forEach(r ->
                formatedContent.add(new RowVo((String) r[0], (String) r[1], (String) r[2], (String) r[3], (String) r[4],
                        (String) r[5], (String) r[6], (String) r[7], (String) r[8], (String) r[9], (String) r[10],
                        (String) r[11], (String) r[12], (String) r[13], (String) r[14], (String) r[15],
                        ListToString(r[16])))
        );

        // itero el resultado creando una row para cada uno
        formatedContent.stream().forEach(
                resultData -> {
                    IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
                    fillRowContent(newDataRow, resultData, styleDataRow);
                }
        );

        sheet.autoSizeColumns();

        return wb;
    }

    private String ListToString(Object obj) {
        if (obj != null) {
            Object[] array = (Object[]) obj;
            return Arrays.stream(array)
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
        }
        return null;
    }

    private ICellStyle createHeaderStyle(IWorkbook workbook){
        ICellStyle cellStyle = workbook.createStyle();
        cellStyle.setBold(true);
        cellStyle.setFontSize(FONT_SIZE);

        return cellStyle;
    }

    private ICellStyle createDataRowStyle(IWorkbook workbook){
        ICellStyle cellStyle = workbook.createStyle();
        cellStyle.setFontSize(FONT_SIZE);

        return cellStyle;
    }

    private void fillRow(IRow row, Object[] content, ICellStyle style){
        AtomicInteger rowNumber = new AtomicInteger(0);
        Arrays.stream(content).forEach(
                item -> {
                    ICell cell = row.createCell(rowNumber.getAndIncrement());
                    cell.setCellValue(item != null ? item.toString() : "");
                    cell.setCellStyle(style);
                }
        );
    }

    private void fillRowContent(IRow row, RowVo content, ICellStyle style){
        AtomicInteger rowNumber = new AtomicInteger(0);
            ICell cell = row.createCell(rowNumber.getAndIncrement());
            cell.setCellValue(content != null ? content.getProvincia() : "");
            cell.setCellStyle(style);
            ICell cell2 = row.createCell(rowNumber.getAndIncrement());
            cell2.setCellValue(content != null ? content.getMunicipio() : "");
            cell2.setCellStyle(style);
            ICell cell3 = row.createCell(rowNumber.getAndIncrement());
            cell3.setCellValue(content != null ? content.getCodEstable() : "");
            cell3.setCellStyle(style);
            ICell cell4 = row.createCell(rowNumber.getAndIncrement());
            cell4.setCellValue(content != null ? content.getInstitucion() : "");
            cell4.setCellStyle(style);
            ICell cell5 = row.createCell(rowNumber.getAndIncrement());
            cell5.setCellValue(content != null ? content.getApellidosPaciente() : "");
            cell5.setCellStyle(style);
            ICell cell6 = row.createCell(rowNumber.getAndIncrement());
            cell6.setCellValue(content != null ? content.getNombresPaciente() : "");
            cell6.setCellStyle(style);
            ICell cell7 = row.createCell(rowNumber.getAndIncrement());
            cell7.setCellValue(content != null ? content.getTipoDocumento() : "");
            cell7.setCellStyle(style);
            ICell cell8 = row.createCell(rowNumber.getAndIncrement());
            cell8.setCellValue(content != null ? content.getNumeroDocumento() : "");
            cell8.setCellStyle(style);
            ICell cell9 = row.createCell(rowNumber.getAndIncrement());
            cell9.setCellValue(content != null ? content.getFechaNacimiento() : "");
            cell9.setCellStyle(style);
            ICell cell10 = row.createCell(rowNumber.getAndIncrement());
            cell10.setCellValue(content != null ? content.getGenero() : "");
            cell10.setCellStyle(style);
            ICell cell11 = row.createCell(rowNumber.getAndIncrement());
            cell11.setCellValue(content != null ? content.getDomicilio() : "");
            cell11.setCellStyle(style);
            ICell cell12 = row.createCell(rowNumber.getAndIncrement());
            cell12.setCellValue(content != null ? content.getNumeroTelefono() : "");
            cell12.setCellStyle(style);
            ICell cell13 = row.createCell(rowNumber.getAndIncrement());
            cell13.setCellValue(content != null ? content.getEmail() : "");
            cell13.setCellStyle(style);
            ICell cell14 = row.createCell(rowNumber.getAndIncrement());
            cell14.setCellValue(content != null ? content.getFechaInicio() : "");
            cell14.setCellStyle(style);
            ICell cell15 = row.createCell(rowNumber.getAndIncrement());
            cell15.setCellValue(content != null ? content.getEspecialidad() : "");
            cell15.setCellStyle(style);
            ICell cell16 = row.createCell(rowNumber.getAndIncrement());
            cell16.setCellValue(content != null ? content.getApellidosProfesional() : "");
            cell16.setCellStyle(style);
            ICell cell17 = row.createCell(rowNumber.getAndIncrement());
            cell17.setCellValue(content != null ? content.getRazonesConsulta() : "");
            cell17.setCellStyle(style);
        }
}
