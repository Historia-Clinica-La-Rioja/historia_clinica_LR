package net.pladema.provincialreports.nursingreports.service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.nursingreports.repository.NursingEmergencyConsultationDetail;
import net.pladema.provincialreports.nursingreports.repository.NursingHospitalizationConsultationDetail;
import net.pladema.provincialreports.nursingreports.repository.NursingOutpatientConsultationDetail;
import net.pladema.provincialreports.nursingreports.repository.NursingProceduresConsultationDetail;
import net.pladema.provincialreports.nursingreports.repository.NursingVaccineConsultationDetail;
import net.pladema.provincialreports.reportformat.DateFormat;
import net.pladema.provincialreports.reportformat.domain.service.ReportExcelUtilsService;

@Service
public class NursingReportsExcelService {
	private static final String NURSING_EMERGENCY_OBSERVATION = "Observación: este reporte contiene información recolectada durante las últimas 24 horas, incluyendo los datos más recientes hasta la fecha de emisión";

	private final DateFormat dateTools;

	private final ReportExcelUtilsService excelUtilsService;

	public NursingReportsExcelService(DateFormat dateTools, ReportExcelUtilsService excelUtilsService) {
		this.dateTools = dateTools;
		this.excelUtilsService = excelUtilsService;
	}

	public IWorkbook buildNursingEmergencyExcel(String title, String[] headers, List<NursingEmergencyConsultationDetail> result, Integer institutionId) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.createHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 26, 0, null, institutionId, NURSING_EMERGENCY_OBSERVATION));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillNursingEmergencyRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildNursingOutpatientExcel(String title, String[] headers, List<NursingOutpatientConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.createHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 24, 0, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId, null));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillNursingOutpatientRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildNursingHospitalizationExcel(String title, String[] headers, List<NursingHospitalizationConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.createHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 12, 0, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId, null));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillNursingHospitalizationRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildNursingProceduresExcel(String title, String[] headers, List<NursingProceduresConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.createHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 25, 0, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId, null));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillNursingProceduresRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildNursingVaccinesExcel(String title, String[] headers, List<NursingVaccineConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.createHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 12, 0, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId, null));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillNursingVaccinesRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public void fillNursingEmergencyRow(IRow row, NursingEmergencyConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getAmbulance());
		excelUtilsService.setCellValue(row, 1, style, content.getOffice());
		excelUtilsService.setCellValue(row, 2, style, content.getSector());
		excelUtilsService.setCellValue(row, 3, style, content.getPoliceIntervention());
		excelUtilsService.setCellValue(row, 4, style, dateTools.newReformatDate(content.getAttentionDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 5, style, dateTools.standardizeTime(content.getAttentionHour()));
		excelUtilsService.setCellValue(row, 6, style, content.getProfessionalRegistering());
		excelUtilsService.setCellValue(row, 7, style, content.getProfessionalAttention());
		excelUtilsService.setCellValue(row, 8, style, content.getIdentification());
		excelUtilsService.setCellValue(row, 9, style, content.getLastName());
		excelUtilsService.setCellValue(row, 10, style, content.getNames());
		excelUtilsService.setCellValue(row, 11, style, content.getGender());
		excelUtilsService.setCellValue(row, 12, style, content.getSelfPerceivedGender());
		excelUtilsService.setCellValue(row, 13, style, dateTools.newReformatDate(content.getBirthDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 14, style, content.getAgeTurn());
		excelUtilsService.setCellValue(row, 15, style, content.getAgeToday());
		excelUtilsService.setCellValue(row, 16, style, content.getEthnicity());
		excelUtilsService.setCellValue(row, 17, style, content.getAddressPatient());
		excelUtilsService.setCellValue(row, 18, style, content.getLocationPatient());
		excelUtilsService.setCellValue(row, 19, style, content.getMedicalCoverage());
		excelUtilsService.setCellValue(row, 20, style, content.getEmergencyCareEntrance());
		excelUtilsService.setCellValue(row, 21, style, content.getStatePatient());
		excelUtilsService.setCellValue(row, 22, style, content.getAttentionType());
		excelUtilsService.setCellValue(row, 23, style, content.getTriageNote());
		excelUtilsService.setCellValue(row, 24, style, content.getTriageLevel());
		excelUtilsService.setCellValue(row, 25, style, dateTools.newReformatDate(content.getDateDischarge(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 26, style, content.getAmbulanceDischarge());
		excelUtilsService.setCellValue(row, 27, style, content.getTypeDischarge());
		excelUtilsService.setCellValue(row, 28, style, content.getPatientExit());
	}

	public void fillNursingOutpatientRow(IRow row, NursingOutpatientConsultationDetail content, ICellStyle style) {
		// no glycosilated hemoglobin, blood sugar, cardiovascular risk
		excelUtilsService.setCellValue(row, 0, style, content.getPatientProvider());
		excelUtilsService.setCellValue(row, 1, style, content.getProviderDni());
		excelUtilsService.setCellValue(row, 2, style, dateTools.newReformatDate(content.getAttentionDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 3, style, dateTools.standardizeTime(content.getHour()));
		excelUtilsService.setCellValue(row, 4, style, content.getConsultationNumber());
		excelUtilsService.setCellValue(row, 5, style, content.getPatientDni());
		excelUtilsService.setCellValue(row, 6, style, content.getPatientName());
		excelUtilsService.setCellValue(row, 7, style, content.getGender());
		excelUtilsService.setCellValue(row, 8, style, dateTools.newReformatDate(content.getBirthDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 9, style, content.getAgeTurn());
		excelUtilsService.setCellValue(row, 10, style, content.getAgeToday());
		excelUtilsService.setCellValue(row, 11, style, content.getEthnicity());
		excelUtilsService.setCellValue(row, 12, style, content.getMedicalCoverage());
		excelUtilsService.setCellValue(row, 13, style, content.getPatientAddress());
		excelUtilsService.setCellValue(row, 14, style, content.getPatientLocation());
		excelUtilsService.setCellValue(row, 15, style, content.getHeartRate());
		excelUtilsService.setCellValue(row, 16, style, content.getRespiratoryRate());
		excelUtilsService.setCellValue(row, 17, style, content.getTemperature());
		excelUtilsService.setCellValue(row, 18, style, content.getArterialOxygenSaturation());
		excelUtilsService.setCellValue(row, 19, style, content.getSystolicPressure());
		excelUtilsService.setCellValue(row, 20, style, content.getDiastolicPressure());
		excelUtilsService.setCellValue(row, 21, style, content.getMeanArterialPressure());
		excelUtilsService.setCellValue(row, 22, style, content.getHeight());
		excelUtilsService.setCellValue(row, 23, style, content.getWeight());
		excelUtilsService.setCellValue(row, 24, style, content.getBmi());
		excelUtilsService.setCellValue(row, 25, style, content.getProcedures());
		excelUtilsService.setCellValue(row, 26, style, content.getEvolution());
	}

	public void fillNursingHospitalizationRow(IRow row, NursingHospitalizationConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getLastName());
		excelUtilsService.setCellValue(row, 1, style, content.getCompleteName());
		excelUtilsService.setCellValue(row, 2, style, content.getGender());
		excelUtilsService.setCellValue(row, 3, style, content.getIdentification());
		excelUtilsService.setCellValue(row, 4, style, content.getProfessional());
		excelUtilsService.setCellValue(row, 5, style, content.getLicenseNumber());
		excelUtilsService.setCellValue(row, 6, style, dateTools.newReformatDate(content.getEntryDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 7, style, dateTools.newReformatDate(content.getProbableDischargeDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 8, style, content.getBed());
		excelUtilsService.setCellValue(row, 9, style, content.getCategoryBed());
		excelUtilsService.setCellValue(row, 10, style, content.getRoomName());
		excelUtilsService.setCellValue(row, 11, style, content.getSector());
		excelUtilsService.setCellValue(row, 12, style, dateTools.newReformatDate(content.getDischargeDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 13, style, content.getProcedures());
		excelUtilsService.setCellValue(row, 14, style, content.getVitalSign());
	}

	public void fillNursingProceduresRow(IRow row, NursingProceduresConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getLender());
		excelUtilsService.setCellValue(row, 1, style, content.getLenderDni());
		excelUtilsService.setCellValue(row, 2, style, dateTools.newReformatDate(content.getAttentionDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 3, style, dateTools.standardizeTime(content.getHour()));
		excelUtilsService.setCellValue(row, 4, style, content.getPatientDni());
		excelUtilsService.setCellValue(row, 5, style, content.getPatientName());
		excelUtilsService.setCellValue(row, 6, style, content.getGender());
		excelUtilsService.setCellValue(row, 7, style, dateTools.newReformatDate(content.getBirthday(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 8, style, content.getAgeTurn());
		excelUtilsService.setCellValue(row, 9, style, content.getAgeToday());
		excelUtilsService.setCellValue(row, 10, style, content.getEthnicity());
		excelUtilsService.setCellValue(row, 11, style, content.getMedicalCoverage());
		excelUtilsService.setCellValue(row, 12, style, content.getAddress());
		excelUtilsService.setCellValue(row, 13, style, content.getLocation());
		excelUtilsService.setCellValue(row, 14, style, content.getHeartRate());
		excelUtilsService.setCellValue(row, 15, style, content.getRespirationRate());
		excelUtilsService.setCellValue(row, 16, style, content.getTemperature());
		excelUtilsService.setCellValue(row, 17, style, content.getOxygenSaturationHemoglobin());
		excelUtilsService.setCellValue(row, 18, style, content.getSystolicBloodPressure());
		excelUtilsService.setCellValue(row, 19, style, content.getDiastolicBloodPressure());
		excelUtilsService.setCellValue(row, 20, style, content.getMeanArterialPressure());
		excelUtilsService.setCellValue(row, 21, style, content.getHeight());
		excelUtilsService.setCellValue(row, 22, style, content.getWeight());
		excelUtilsService.setCellValue(row, 23, style, content.getBmi());
		excelUtilsService.setCellValue(row, 24, style, content.getProcedures());
		excelUtilsService.setCellValue(row, 25, style, content.getProblems());
		excelUtilsService.setCellValue(row, 26, style, content.getMedication());
		excelUtilsService.setCellValue(row, 27, style, content.getEvolution());
	}

	public void fillNursingVaccinesRow(IRow row, NursingVaccineConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getProvider());
		excelUtilsService.setCellValue(row, 1, style, content.getProviderIdentificationNumber());
		excelUtilsService.setCellValue(row, 2, style, dateTools.newReformatDate(content.getAttentionDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 3, style, content.getPatientIdentificationNumber());
		excelUtilsService.setCellValue(row, 4, style, content.getPatientName());
		excelUtilsService.setCellValue(row, 5, style, content.getPatientGender());
		excelUtilsService.setCellValue(row, 6, style, dateTools.newReformatDate(content.getPatientBirthDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 7, style, content.getAgeOnAppointmentDate());
		excelUtilsService.setCellValue(row, 8, style, content.getVaccine());
		excelUtilsService.setCellValue(row, 9, style, content.getSctid());
		excelUtilsService.setCellValue(row, 10, style, content.getStatus());
		excelUtilsService.setCellValue(row, 11, style, content.getCondition());
		excelUtilsService.setCellValue(row, 12, style, content.getVaccinationSchedule());
		excelUtilsService.setCellValue(row, 13, style, content.getVaccinationDosage());
		excelUtilsService.setCellValue(row, 14, style, content.getVaccinationLot());
	}
}
