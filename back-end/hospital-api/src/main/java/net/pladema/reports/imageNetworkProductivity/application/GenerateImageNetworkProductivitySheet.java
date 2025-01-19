package net.pladema.reports.imageNetworkProductivity.application;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.person.service.PersonService;
import net.pladema.reports.domain.InstitutionReportType;
import net.pladema.reports.imageNetworkProductivity.application.exception.WrongDateException;
import net.pladema.reports.imageNetworkProductivity.application.port.ImageNetworkProductivityReportInstitutionStorage;
import net.pladema.reports.imageNetworkProductivity.application.port.ImageNetworkProductivityReportStorage;
import net.pladema.reports.imageNetworkProductivity.domain.CellDataBo;
import net.pladema.reports.imageNetworkProductivity.domain.ImageNetworkProductivityFilterBo;

import net.pladema.reports.imageNetworkProductivity.domain.InstitutionBo;

import net.pladema.reports.imageNetworkProductivity.domain.exception.EWrongDateException;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenerateImageNetworkProductivitySheet {

	private final ImageNetworkProductivityReportInstitutionStorage imageNetworkProductivityReportInstitutionStorage;

	private final ImageNetworkProductivityReportStorage imageNetworkProductivityReportStorage;

	private final PersonService personService;

	private final String EMPTY_CELL = "";

	private ICellStyle basicStyle;

	private ICellStyle titleStyle;

	private ICellStyle columnAttributeStyle;

	public IWorkbook run(ImageNetworkProductivityFilterBo filter) {
		log.debug("Input parameters -> filter {}", filter);
		assertValidFilter(filter);
		IWorkbook result = createWorkBook(filter);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertValidFilter(ImageNetworkProductivityFilterBo filter) {
		if (!filter.getFrom().getMonth().equals(filter.getTo().getMonth()))
			throw new WrongDateException(EWrongDateException.NON_MATCHING_FILTER_MONTHS, "El mes de ambas fechas de búsqueda no coinciden");
		Month currentMonth = LocalDate.now().getMonth();
		if (filter.getFrom().getMonth().equals(currentMonth))
			throw new WrongDateException(EWrongDateException.FILTER_CURRENT_MONTH, "No puede realizarse una búsqueda sobre el corriente mes");
	}

	private IWorkbook createWorkBook(ImageNetworkProductivityFilterBo filter) {
		IWorkbook result = WorkbookCreator.createExcelWorkbook();
		setWorkBookStyle(result);
		createWorkBookSheet(result, filter);
		return result;
	}

	private void createWorkBookSheet(IWorkbook workbook, ImageNetworkProductivityFilterBo filter) {
		final String SHEET_NAME = InstitutionReportType.ImageNetworkProductivity.getDescription();
		InstitutionBo institutionData = imageNetworkProductivityReportInstitutionStorage.fetchInstitutionData(filter.getInstitutionId());
		ISheet sheet = workbook.createSheet(SHEET_NAME);
		addHeaders(sheet, filter, institutionData);
		addData(sheet, filter, institutionData);
	}

	private void addData(ISheet sheet, ImageNetworkProductivityFilterBo filter, InstitutionBo institutionData) {
		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		List<CellDataBo> cellData = imageNetworkProductivityReportStorage.fetchCellData(filter);
		cellData.forEach(data -> writeCellData(sheet, data, rowNumber, institutionData));
	}

	private void writeCellData(ISheet sheet, CellDataBo data, AtomicInteger rowNumber, InstitutionBo institutionData) {
		List<CellContent> content = getSheetData(data, rowNumber, institutionData);
		fillRow(sheet, content);
		rowNumber.getAndIncrement();
	}

	private List<CellContent> getSheetData(CellDataBo data, AtomicInteger rowNumber, InstitutionBo institutionData) {
		List<CellContent> result = new ArrayList<>();

		String technicianFullName = personService.getCompletePersonNameById(data.getTechnicianPersonId()).toUpperCase();
		String informerFullName = data.getInformerPersonId() != null ? personService.getCompletePersonNameById(data.getInformerPersonId()).toUpperCase() : EMPTY_CELL;
		String requestProfessionalFullName = getRequestProfessionalFullName(data);

		result.add(new CellContent(rowNumber.get(), 0,1, 1, parseNullableAttribute(institutionData.getStateName()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 1,1, 1, parseNullableAttribute(institutionData.getDepartmentName()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 2,1, 1, parseNullableAttribute(institutionData.getSisaCode()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 3,1, 1, parseNullableAttribute(institutionData.getName()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 4,1, 1, EMPTY_CELL, basicStyle)); //TIPO DE UNIDAD JERÁRQUICA
		result.add(new CellContent(rowNumber.get(), 5,1, 1, EMPTY_CELL, basicStyle)); //UNIDAD JERÁRQUICA
		result.add(new CellContent(rowNumber.get(), 6,1, 1, parseNullableAttribute(data.getPatientLastName()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 7,1, 1, parseNullableAttribute(data.getPatientFirstName()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 8,1, 1, parseNullableAttribute(data.getPatientSelfDeterminationName()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 9,1, 1, parseNullableAttribute(data.getIdentificationType()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 10,1, 1, parseNullableAttribute(data.getIdentificationNumber()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 11,1, 1, data.getPatientBirthDate() != null ? data.getPatientBirthDate().toString() : EMPTY_CELL, basicStyle));
		result.add(new CellContent(rowNumber.get(), 12,1, 1, parseNullableAttribute(data.getSelfDeterminationGender()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 13,1, 1, parseSpaceSeparatedData(data.getPatientStreetName(), data.getPatientStreetNumber()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 14,1, 1, parsePhoneData(data.getPhonePrefix(), data.getPhoneNumber()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 15,1, 1, parseNullableAttribute(data.getPatientEmail()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 16,1, 1, parseNullableAttribute(data.getMedicalCoverageName()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 17,1, 1, parseNullableAttribute(data.getAffiliateNumber()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 18,1, 1, data.getAppointmentDate().toString(), basicStyle));
		result.add(new CellContent(rowNumber.get(), 19,1, 1, parseNullableAttribute(data.getProblem()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 20,1, 1, parseNullableAttribute(data.getModality()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 21,1, 1, parseNullableAttribute(data.getPractice()), basicStyle));
		result.add(new CellContent(rowNumber.get(), 22,1, 1, EMPTY_CELL, basicStyle)); //CANTIDAD DE IMÁGENES
		result.add(new CellContent(rowNumber.get(), 23,1, 1, technicianFullName, basicStyle));
		result.add(new CellContent(rowNumber.get(), 24,1, 1, informerFullName, basicStyle));
		result.add(new CellContent(rowNumber.get(), 25,1, 1, requestProfessionalFullName, basicStyle));
		result.add(new CellContent(rowNumber.get(), 26,1, 1, EMPTY_CELL, basicStyle)); //SERVICIO SOLICITANTE
		result.add(new CellContent(rowNumber.get(), 27,1, 1, parseNullableAttribute(data.getSourceType()), basicStyle));
		return result;
	}

	private String getRequestProfessionalFullName(CellDataBo data) {
		return data.getNonTranscribedRequestProfessionalPersonId() != null ? personService.getCompletePersonNameById(data.getNonTranscribedRequestProfessionalPersonId()).toUpperCase() : data.getTranscribedRequestProfessional() != null ? data.getTranscribedRequestProfessional().toUpperCase() : EMPTY_CELL;
	}

	private String parsePhoneData(String prefix, String number) {
		return number != null ? prefix != null ? prefix + number : number : EMPTY_CELL;
	}

	private String parseSpaceSeparatedData(String firstElement, String secondElement) {
		return firstElement != null && secondElement != null ? (firstElement + " " + secondElement).toUpperCase() : EMPTY_CELL;
	}

	private String parseNullableAttribute(String data) {
		return data != null ? data.toUpperCase() : EMPTY_CELL;
	}

	private void addHeaders(ISheet sheet, ImageNetworkProductivityFilterBo filter, InstitutionBo institutionData) {
		List<CellContent> sheetHeaders = getSheetHeader(filter, institutionData);
		fillRow(sheet, sheetHeaders);
	}

	//Mover a servicio - Compratido (ConsultationSummaryReport.java)
	private void fillRow(ISheet sheet, List<CellContent> data){
		Map<Integer, List<CellContent>> cellByRow = data.stream().collect(Collectors.groupingBy(CellContent::getRow));
		for(Map.Entry<Integer, List<CellContent>> entry : cellByRow.entrySet())
			processRow(sheet, entry);
	}

	private void processRow(ISheet sheet, Map.Entry<Integer, List<CellContent>> entry) {
		int nRow = entry.getKey();
		IRow row = sheet.createRow(nRow);
		entry.getValue().forEach(cell -> createCell(sheet, row, nRow, cell));
	}

	//Mover a servicio - Compartido (ConsultationSummaryReport.java)
	private void createCell(ISheet sheet, IRow row, int nRow, CellContent data){
		int nColumn = data.getColumn();

		ICell cell = row.createCell(data.getColumn());
		cell.setCellStyle(data.getStyle());
		cell.setCellValue(data);

		if(data.isCellRange())
			sheet.addMergedRegion(nRow, data.lastRow(), nColumn, data.lastCol(), true);
	}

	private List<CellContent> getSheetHeader(ImageNetworkProductivityFilterBo filter, InstitutionBo institutionData) {
		List<CellContent> result = new ArrayList<>();

		int rowNumber = 0;

		result.add(new CellContent(rowNumber, 0,1, 2, EMPTY_CELL, basicStyle));
		result.add(new CellContent(rowNumber, 2,2, 1, "X", titleStyle));
		result.add(new CellContent(rowNumber, 3,2, 16, "Detalle nominal de prestaciones", titleStyle));
		result.add(new CellContent(rowNumber, 19,1, 2, "1. Hoja N°", basicStyle));
		result.add(new CellContent(rowNumber, 21,1, 7, EMPTY_CELL, basicStyle));

		rowNumber++;

		result.add(new CellContent(rowNumber, 0,1, 2, EMPTY_CELL, basicStyle));
		result.add(new CellContent(rowNumber, 19,1, 2, EMPTY_CELL, basicStyle));
		result.add(new CellContent(rowNumber, 21,1, 7, EMPTY_CELL, basicStyle));

		rowNumber++;

		Locale argentinianLocale = new Locale("es", "AR"); //Ésto se tendría que revisar y meterlo en algún lugar mas global
		String monthName = filter.getFrom().getMonth().getDisplayName(TextStyle.FULL, argentinianLocale).toUpperCase();
		String year = Integer.toString(filter.getFrom().getYear());

		result.add(new CellContent(rowNumber, 0,1, 2, "2. ESTABLECIMIENTO", basicStyle));
		result.add(new CellContent(rowNumber, 2,1, 14, institutionData.getName(), basicStyle));
		result.add(new CellContent(rowNumber, 16,1, 1, "3. MES", basicStyle));
		result.add(new CellContent(rowNumber, 17,1, 1, monthName, basicStyle));
		result.add(new CellContent(rowNumber, 18,1, 1, "4. AÑO", basicStyle));
		result.add(new CellContent(rowNumber, 19,1, 2, year, basicStyle));
		result.add(new CellContent(rowNumber, 21,1, 7, EMPTY_CELL, basicStyle));

		rowNumber++;

		result.add(new CellContent(rowNumber, 0,1, 2, "5. PARTIDO", basicStyle));
		result.add(new CellContent(rowNumber, 2,1, 8, institutionData.getDepartmentName(), basicStyle));
		result.add(new CellContent(rowNumber, 10,1, 1, "6. REGIÓN SANITARIA", basicStyle));
		result.add(new CellContent(rowNumber, 11,1, 5, EMPTY_CELL, basicStyle));
		result.add(new CellContent(rowNumber, 16,1, 3, "7. SERVICIO", basicStyle));
		result.add(new CellContent(rowNumber, 19,1, 2, EMPTY_CELL, basicStyle));
		result.add(new CellContent(rowNumber, 21,1, 7, EMPTY_CELL, basicStyle));

		rowNumber ++;

		result.add(new CellContent(rowNumber, 0,1, 1, "PROVINCIA", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 1,1, 1, "MUNICIPIO", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 2,1, 1, "CÓDIGO DE ESTABLECIMIENTO", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 3,1, 1, "ESTABLECIMIENTO", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 4,1, 1, "TIPO DE UNIDAD JERÁRQUICA", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 5,1, 1, "UNIDAD JERÁRQUICA", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 6,1, 1, "APELLIDOS PACIENTE", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 7,1, 1, "NOMBRES PACIENTE", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 8,1, 1, "NOMBRE AUTOPERCIBIDO", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 9,1, 1, "TIPO DOCUMENTO", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 10,1, 1, "NRO. DOCUMENTO", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 11,1, 1, "FECHA DE NACIMIENTO", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 12,1, 1, "GÉNERO AUTOPERCIBIDO", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 13,1, 1, "DOMICILIO", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 14,1, 1, "TELÉFONO", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 15,1, 1, "MAIL", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 16,1, 1, "OBRA SOCIAL/PREPAGA", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 17,1, 1, "NRO. AFILIADO", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 18,1, 1, "FECHA DE ATENCIÓN", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 19,1, 1, "DIAGNÓSTICO", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 20,1, 1, "MODALIDAD", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 21,1, 1, "PRÁCTICA", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 22,1, 1, "CANTIDAD DE IMÁGENES", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 23,1, 1, "PROFESIONAL TÉCNICO", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 24,1, 1, "PROFESIONAL INFORMADOR", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 25,1, 1, "PROFESIONAL SOLICITANTE", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 26,1, 1, "SERVICIO SOLICITANTE", columnAttributeStyle));
		result.add(new CellContent(rowNumber, 27,1, 1, "ÁMBITO", columnAttributeStyle));

		return result;
	}

	private void setWorkBookStyle(IWorkbook workbook) {
		basicStyle = workbook.createStyle();
		basicStyle.setFontSize((short)10);
		basicStyle.setBold(false);
		basicStyle.setWrap(false);
		basicStyle.setBorders(true);
		basicStyle.setHAlign(ICellStyle.HALIGNMENT.LEFT);
		basicStyle.setVAlign(ICellStyle.VALIGNMENT.CENTER);

		titleStyle = workbook.createStyle();
		titleStyle.setFontSize((short)25);
		titleStyle.setBold(true);
		titleStyle.setWrap(false);
		titleStyle.setBorders(true);
		titleStyle.setHAlign(ICellStyle.HALIGNMENT.CENTER);
		titleStyle.setVAlign(ICellStyle.VALIGNMENT.CENTER);

		columnAttributeStyle = workbook.createStyle();
		columnAttributeStyle.setFontSize((short)12);
		columnAttributeStyle.setBold(true);
		columnAttributeStyle.setWrap(false);
		columnAttributeStyle.setBorders(true);
		columnAttributeStyle.setHAlign(ICellStyle.HALIGNMENT.LEFT);
		columnAttributeStyle.setVAlign(ICellStyle.VALIGNMENT.CENTER);
	}

}
