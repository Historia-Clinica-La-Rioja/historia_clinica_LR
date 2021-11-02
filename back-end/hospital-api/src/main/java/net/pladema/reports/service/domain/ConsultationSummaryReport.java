package net.pladema.reports.service.domain;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.person.repository.entity.Gender;
import net.pladema.reports.repository.ConsultationSummary;
import net.pladema.reports.repository.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConsultationSummaryReport {

    private static final Logger LOG = LoggerFactory.getLogger(ConsultationSummaryReport.class);

    private final QueryFactory queryFactory;

    private ICellStyle basicStyle;
    private ICellStyle titleStyle;
    private ICellStyle wrapStyle;
    private ICellStyle fieldStyle;

    private static final int SINGLECELL = 1;

    //specific range for age and gender data
    private static final int FIRST_COLUMN_DATA =4;
    private static final int LAST_COLUMN_DATA =22;

    public ConsultationSummaryReport(QueryFactory queryFactory){
        this.queryFactory = queryFactory;
    }

    public IWorkbook build(Integer institutionId, LocalDate from, LocalDate to,
                           Integer professionalId, Integer specialtyId) {

        //The report must show summary information of the outpatient—consultations
        // organized by specialty, sex and age.
        IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
        try {

            createCellStyle(workbook);
            ISheet sheet = workbook.createSheet("RMCE-Hoja 2.1");

            /* Fill Data*/
            //Header
            fillRow(sheet, getHeaderData());

            //Content
            int firstDataRow = sheet.getCantRows();
            Map<Integer, List<ConsultationSummary>> data = processData(institutionId, from, to, professionalId, specialtyId);
            fillRow(sheet, convertDataIntoContent(sheet, data, firstDataRow));
            int lastDataRow = sheet.getCantRows();

            //Footer
            fillRow(sheet, getFooterData(sheet, firstDataRow, lastDataRow));

            /* Set Column-Width and Row-Height for whole sheet*/
            setDimensions(sheet);
        }
        catch(Exception e){
            LOG.debug("OutpatientSummaryReport -> {}", e.getMessage());
        }
        return workbook;
    }

    private Map<Integer, List<ConsultationSummary>> processData(Integer institutionId, LocalDate from, LocalDate to,
                                                                Integer professionalId, Integer specialtyId) {
        //Data from database
        List<ConsultationSummary> data = queryFactory.fetchConsultationSummaryData(institutionId, from, to);

        //Required filter: discard records without gender or date of birth
        //Optional filter: by specialty or professional if specified
        List<ConsultationSummary> validData = data.stream().filter(c -> c.hasGender() && c.hasBirthdate())
                .filter(professionalId != null ? c -> c.getProfessionalId().equals(professionalId) : c -> true)
                .filter(specialtyId != null ? c -> c.getSpecialtyId().equals(specialtyId) : c -> true)
                .collect(Collectors.toList());

        return validData.stream()
                .collect(Collectors.groupingBy(ConsultationSummary::getSpecialtyId));
    }

    private List<CellContent> convertDataIntoContent(ISheet sheet, Map<Integer, List<ConsultationSummary>> data, int row){
        List<CellContent> content = new ArrayList<>();
        int column = 0;

        for(Map.Entry<Integer, List<ConsultationSummary>> dataEntry : data.entrySet()){

            List<ConsultationSummary> consultations = dataEntry.getValue();

            //Register Clinical-Specialty
            String specialty = consultations.get(0).getSpecialty();
            content.add(new CellContent(row, column++, SINGLECELL, SINGLECELL, specialty, fieldStyle));

            //Register Empty codes
            while(column < FIRST_COLUMN_DATA)
                content.add(new CellContent(row, column++, SINGLECELL, SINGLECELL, null, basicStyle));

            Map<Integer, List<ConsultationSummary>> consultationsByAgeRange = consultations.stream()
                    .collect(Collectors.groupingBy(ConsultationSummary::getAgeRange));

            //Register outpatient—consultations by sex and age for the specific clinical—specialty
            while(column < LAST_COLUMN_DATA) {

                Object nWomen = null;
                Object nMen = null;
                CellContent.DATAFORMAT dataFormat;

                if(!consultationsByAgeRange.containsKey(column))
                    //There are no outpatients—consultations for the age range.
                    dataFormat = CellContent.DATAFORMAT.STRING;
                else {
                    List<ConsultationSummary> consultationForAgeRange = consultationsByAgeRange.get(column);

                    nWomen = getNumberOfWomen(consultationForAgeRange);
                    nMen = getNumberOfMen(consultationForAgeRange, nWomen);
                    dataFormat = CellContent.DATAFORMAT.DOUBLE;
                }

                content.add(new CellContent(row, column++, SINGLECELL, SINGLECELL, nMen, basicStyle, dataFormat));
                content.add(new CellContent(row, column++, SINGLECELL, SINGLECELL, nWomen, basicStyle, dataFormat));
            }


            String formula = getSumFunction(sheet, row, row, FIRST_COLUMN_DATA, column-1);

            content.add(new CellContent(row, column++, SINGLECELL, SINGLECELL, formula, basicStyle));


            //Register total number of outpatient and with/out medical coverage for the specific clinical—specialty
            int count = consultations.size();

            double withCoverage = consultations.stream().filter(ConsultationSummary::isCoverage).count();
            double withoutCoverage = count - withCoverage;

            content.add(new CellContent(row, column++, SINGLECELL, SINGLECELL, withCoverage,
                    basicStyle, CellContent.DATAFORMAT.DOUBLE));
            content.add(new CellContent(row, column, SINGLECELL, SINGLECELL, withoutCoverage,
                    basicStyle, CellContent.DATAFORMAT.DOUBLE));

            row++;
            column=0;
        }
        return content;
    }

    private Object getNumberOfMen(List<ConsultationSummary> consultationForAgeRange, Object nWomen) {
        double size = consultationForAgeRange.size();
        if(nWomen != null) {
            double nMen = size - (double) nWomen;
            return nMen > 0 ? nMen : null;
        }
        return (double) consultationForAgeRange.size();

    }

    private Double getNumberOfWomen(List<ConsultationSummary> consultations){
        double women = (double) consultations.stream()
                .filter(oc -> oc.getGenderId().equals(Gender.FEMALE))
                .count();
        return women > 0 ? women : null;
    }

    private List<CellContent> getHeaderData(){
        List<CellContent> data = new ArrayList<>();

        int nRow=0;

        data.add(new CellContent(nRow, 0,1, 4, "", wrapStyle));
        data.add(new CellContent(nRow, 4,2, 2, "2,1", titleStyle));
        data.add(new CellContent(nRow, 6,2, 16,
                "Resumen Mensual de Consultorio Externo", titleStyle));
        data.add(new CellContent(nRow, 22,1, 1, "1. Hoja N°", basicStyle));
        data.add(new CellContent(nRow, 23,1, 1, "", basicStyle));
        data.add(new CellContent(nRow, 24,1, 1, "", basicStyle));

        nRow++;

        data.add(new CellContent(nRow, 0, 1, 4,
                "", basicStyle));
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
        for(int i=FIRST_COLUMN_DATA; i<LAST_COLUMN_DATA; i++)
            data.add(new CellContent(nRow, i, 1, 1, sexo[i%2], basicStyle));
        data.add(new CellContent(nRow, LAST_COLUMN_DATA+1, SINGLECELL, SINGLECELL, "SI", basicStyle));
        data.add(new CellContent(nRow, LAST_COLUMN_DATA+2, SINGLECELL, SINGLECELL, "NO", basicStyle));

        return data;
    }

    private List<CellContent> getFooterData(ISheet sheet, int firstRow, int lastRow) {
        List<CellContent> data = new ArrayList<>();
        int nColumn = 0;
        data.add(new CellContent(lastRow, nColumn,SINGLECELL, 4, "13. TOTALES", fieldStyle));
        nColumn=FIRST_COLUMN_DATA;
        while(nColumn < 25) {
            String formula = getSumFunction(sheet, firstRow, lastRow-1, nColumn, nColumn);
            data.add(new CellContent(lastRow, nColumn++, SINGLECELL, SINGLECELL, formula, basicStyle));
        }
        return data;
    }

    private void createCellStyle(IWorkbook workbook){
        basicStyle = workbook.createStyle();
        basicStyle.setFontSize((short)10);
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

        wrapStyle = workbook.createStyle();
        wrapStyle.setFontSize((short)10);
        wrapStyle.setBold(false);
        wrapStyle.setWrap(true);
        wrapStyle.setBorders(true);
        wrapStyle.setHAlign(ICellStyle.HALIGNMENT.CENTER);
        wrapStyle.setVAlign(ICellStyle.VALIGNMENT.CENTER);

        fieldStyle = workbook.createStyle();
        fieldStyle.setFontSize((short)10);
        fieldStyle.setBold(false);
        fieldStyle.setWrap(true);
        fieldStyle.setBorders(true);
        fieldStyle.setHAlign(ICellStyle.HALIGNMENT.LEFT);
        fieldStyle.setVAlign(ICellStyle.VALIGNMENT.BOTTOM);

    }

    private void setDimensions(ISheet sheet){
        //Column's width
        sheet.setColumnWidth(0, 150);
        for (int i=1; i<LAST_COLUMN_DATA; i++)
            sheet.setColumnWidth(i, 50);
        sheet.setColumnWidth(LAST_COLUMN_DATA, 150);
        sheet.setColumnWidth(LAST_COLUMN_DATA+1, 60);
        sheet.setColumnWidth(LAST_COLUMN_DATA+2, 60);

        //Row's height
        int nRow=0;
        sheet.setRowHeight(nRow++, 50);
        sheet.setRowHeight(nRow++, 40);
        sheet.setRowHeight(nRow++, 35);

        while(nRow < 7)
            sheet.setRowHeight(nRow++, 21);

        int footerRow = sheet.getCantRows() - 1;
        sheet.setRowHeight(footerRow, 21);
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

    private String getSumFunction(ISheet sheet, int row, int lastRow, int column, int lastCol){
        String range = sheet.getCellRangeAsString(row, lastRow, column, lastCol);
        return range != null ? "=sum(" + range + ")" : null;
    }
}
