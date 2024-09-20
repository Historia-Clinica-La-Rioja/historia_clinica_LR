package net.pladema.reports.infrastructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgx.shared.reports.util.CellContent;
import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.application.hierarchicalunits.FetchDescendantsByHierarchicalUnitId;
import net.pladema.reports.application.ports.NominalAppointmentDetailStorage;
import net.pladema.reports.domain.NominalAppointmentDetailBo;

import net.pladema.reports.domain.ReportSearchFilterBo;
import net.pladema.reports.service.NominalDetailExcelService;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class NominalAppointmentDetailStorageImpl implements NominalAppointmentDetailStorage {

	private final EntityManager entityManager;

	private final SharedPersonPort sharedPersonPort;

	private final NominalDetailExcelService nominalDetailExcelService;

	private final FetchDescendantsByHierarchicalUnitId fetchDescendantsByHierarchicalUnitId;

	@Override
	public IWorkbook buildNominalAppointmentsDetailExcelReport(String title, ReportSearchFilterBo filter) {

		var appointmentsDetail = this.fetchNominalAppointmentsDetail(filter);

		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		ISheet sheet = wb.createSheet(title);
		nominalDetailExcelService.fillRow(sheet, getNominalAppointmentsHeaderData(wb));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle styleDataRow = nominalDetailExcelService.createDataRowStyle(wb);

		appointmentsDetail.forEach(
				resultData -> {
					IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
					fillNominalAppointmentsDetailRow(newDataRow, resultData, styleDataRow);
				}
		);

		nominalDetailExcelService.setDimensions(sheet);
		return wb;
	}

	public List<NominalAppointmentDetailBo> fetchNominalAppointmentsDetail(ReportSearchFilterBo filter) {
		String sqlQuery = "SELECT NEW net.pladema.reports.domain.NominalAppointmentDetailBo(hut.description, hu.alias, a.dateTypeId, a.hour, as1.description, " +
				"a.phoneNumber, a.phonePrefix, a.patientEmail, mc.name, pmc.affiliateNumber, cs.name, a.patientId, a.creationable.createdBy, " +
				"d.healthcareProfessionalId, a.id, da.pk.documentId) " +
				"FROM DoctorsOffice dof " +
				"JOIN Diary d ON (d.doctorsOfficeId = dof.id) " +
				"JOIN AppointmentAssn aa ON (d.id = aa.pk.diaryId) " +
				"JOIN Appointment a ON (aa.pk.appointmentId = a.id) " +
				"LEFT JOIN HierarchicalUnit hu ON (hu.id = d.hierarchicalUnitId) " +
				"LEFT JOIN HierarchicalUnitType hut ON (hut.id = hu.typeId) " +
				"JOIN AppointmentState as1 ON (as1.id = a.appointmentStateId) " +
				"LEFT JOIN PatientMedicalCoverageAssn pmc ON (pmc.id = a.patientMedicalCoverageId) " +
				"LEFT JOIN MedicalCoverage mc ON (mc.id = pmc.medicalCoverageId) " +
				"LEFT JOIN ClinicalSpecialty cs ON (cs.id = d.clinicalSpecialtyId) " +
				"LEFT JOIN DocumentAppointment da ON (da.pk.appointmentId = a.id) " +
 				"WHERE dof.institutionId = :institutionId " +
				"AND a.dateTypeId BETWEEN :startDate AND :endDate " +
				"AND a.patientId IS NOT NULL " +
				(filter.getHierarchicalUnitId() != null ? filter.isIncludeHierarchicalUnitDescendants() ? " AND hu.id IN (:hierarchicalUnitIds) " : " AND hu.id = " + filter.getHierarchicalUnitId() : "");

		if (filter.getHierarchicalUnitTypeId() != null)
			sqlQuery += " AND hu.typeId = " + filter.getHierarchicalUnitTypeId();

		if (filter.getClinicalSpecialtyId() != null)
			sqlQuery += " AND d.clinicalSpecialtyId = " + filter.getClinicalSpecialtyId();

		if (filter.getDoctorId() != null)
			sqlQuery += " AND d.healthcareProfessionalId = " + filter.getDoctorId();

		if (filter.getAppointmentStateId() != null)
			sqlQuery += " AND a.appointmentStateId = " + filter.getAppointmentStateId();

		sqlQuery += " ORDER BY a.dateTypeId, a.hour ";

		Query nativeQuery = entityManager.createQuery(sqlQuery)
				.setParameter("institutionId", filter.getInstitutionId())
				.setParameter("startDate", filter.getFromDate())
				.setParameter("endDate", filter.getToDate());

		if (filter.getHierarchicalUnitId() != null && filter.isIncludeHierarchicalUnitDescendants())
			nativeQuery.setParameter("hierarchicalUnitIds", getHierarchicalUnitIds(filter.getHierarchicalUnitId()));

		List<NominalAppointmentDetailBo> result = nativeQuery.getResultList();
		if (!result.isEmpty())
			fetchAndCompleteNominalAppointmentDetails(result, filter.getInstitutionId());
		log.debug("Nominal appointments detail size -> {} ", result.size());
		return result;
	}

	private void fetchAndCompleteNominalAppointmentDetails(List<NominalAppointmentDetailBo> result, Integer institutionId) {
		String queryString = "SELECT p.description AS province, d.description AS department, i.sisa_code, i.name AS institution " +
				"FROM {h-schema}institution i " +
				"JOIN {h-schema}address a ON (a.id = i.address_id) " +
				"JOIN {h-schema}city c ON (c.id = a.city_id) " +
				"JOIN {h-schema}department d ON (d.id = c.department_id) " +
				"JOIN {h-schema}province p ON (p.id = d.province_id) " +
				"where i.id = :institutionId";
		Object[] institutionData = (Object[]) entityManager.createNativeQuery(queryString)
				.setParameter("institutionId", institutionId)
				.getSingleResult();

		List<Integer> patientIds = result.stream()
				.map(NominalAppointmentDetailBo::getPatientId)
				.collect(Collectors.toList());
		queryString = "SELECT p.id, CONCAT(p2.first_name, ' ', p2.middle_names) AS patientNames, CONCAT(p2.last_name, ' ', p2.other_last_names) AS patientSurnames, " +
				"pe.name_self_determination, it.description AS identification_type, p2.identification_number, p2.birth_date, spg.description, " +
				"CONCAT(a.street, ' ', a.number, ' ', a.floor, ' ', a.apartment, ' ', c.description) AS address, pe.phone_prefix, pe.phone_number, " +
				"pe.email " +
				"FROM {h-schema}patient p " +
				"JOIN {h-schema}person p2 ON (p2.id = p.person_id) " +
				"JOIN {h-schema}person_extended pe ON (pe.person_id = p2.id) " +
				"LEFT JOIN {h-schema}identification_type it ON (it.id = p2.identification_type_id) " +
				"LEFT JOIN {h-schema}self_perceived_gender spg ON (spg.id = pe.gender_self_determination) " +
				"JOIN {h-schema}address a ON (a.id = pe.address_id) " +
				"LEFT JOIN {h-schema}city c ON (c.id = a.city_id) " +
				"WHERE p.id IN :patientIds";
		List<Object[]> patientsData = entityManager.createNativeQuery(queryString)
				.setParameter("patientIds", patientIds)
				.getResultList();

		List<Integer> diaryHealthcareProfessionalIds = result.stream().map(NominalAppointmentDetailBo::getDiaryHealthcareProfessionalId).collect(Collectors.toList());
		queryString = "SELECT hp.id, p.first_name, p.middle_names, p.last_name, p.other_last_names, pe.name_self_determination " +
				"FROM {h-schema}healthcare_professional hp " +
				"JOIN {h-schema}person p ON (p.id = hp.person_id) " +
				"JOIN {h-schema}person_extended pe ON (pe.person_id = p.id) " +
				"WHERE hp.id IN :diaryHealthcareProfessionalIds";
		List<Object[]> diaryHealthcareProfessionalsData = entityManager.createNativeQuery(queryString)
				.setParameter("diaryHealthcareProfessionalIds", diaryHealthcareProfessionalIds)
				.getResultList();

		List<Integer> appointmentHealthcareProfessionalIds = result.stream().map(NominalAppointmentDetailBo::getAppointmentHealthcareProfessionalUserId).collect(Collectors.toList());
		queryString = "SELECT up.user_id, p.first_name, p.middle_names, p.last_name, p.other_last_names, pe.name_self_determination " +
				"FROM {h-schema}user_person up " +
				"JOIN {h-schema}person p ON (p.id = up.person_id) " +
				"JOIN {h-schema}person_extended pe ON (pe.person_id = p.id) " +
				"WHERE up.user_id IN :appointmentHealthcareProfessionalIds";
		List<Object[]> appointmentHealthcareProfessionalsData = entityManager.createNativeQuery(queryString)
				.setParameter("appointmentHealthcareProfessionalIds", appointmentHealthcareProfessionalIds)
				.getResultList();

		List<Long> appointmentDocumentIds = result.stream()
				.map(NominalAppointmentDetailBo::getDocumentId)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		queryString = "SELECT dhc.document_id, STRING_AGG(s.pt, ', ') AS description " +
				"FROM {h-schema}document_health_condition dhc " +
				"JOIN {h-schema}health_condition hc ON (hc.id = dhc.health_condition_id) " +
				"JOIN {h-schema}snomed s ON (s.id = hc.snomed_id) " +
				"WHERE dhc.document_id IN :appointmentDocumentIds " +
				"GROUP BY dhc.document_id";
		List<Object[]> documentsProblems = entityManager.createNativeQuery(queryString)
				.setParameter("appointmentDocumentIds", appointmentDocumentIds)
				.getResultList();

		result.forEach(appointment -> completeNominalAppointmentDetails(appointment, institutionData, patientsData, diaryHealthcareProfessionalsData, appointmentHealthcareProfessionalsData, documentsProblems));
	}

	private void completeNominalAppointmentDetails(NominalAppointmentDetailBo appointment, Object[] institutionData,
												   List<Object[]> patientsData, List<Object[]> diaryHealthcareProfessionalsData,
												   List<Object[]> appointmentHealthcareProfessionalsData, List<Object[]> documentsProblems) {
		appointment.setInstitutionData(institutionData);
		Optional<Object[]> patientData = patientsData.stream()
				.filter(patient -> appointment.getPatientId().equals(patient[0]))
				.findFirst();
		patientData.ifPresent(appointment::setPatientData);
		Optional<Object[]> diaryHealthcareProfessionalData = diaryHealthcareProfessionalsData.stream()
				.filter(diaryProfessional -> appointment.getDiaryHealthcareProfessionalId().equals(diaryProfessional[0]))
				.findFirst();
		diaryHealthcareProfessionalData.ifPresent(professional -> setDiaryProfessionalName(appointment, professional));
		Optional<Object[]> appointmentHealthcareProfessionalData = appointmentHealthcareProfessionalsData.stream()
				.filter(appointmentProfessional -> appointment.getAppointmentHealthcareProfessionalUserId().equals(appointmentProfessional[0]))
				.findFirst();
		appointmentHealthcareProfessionalData.ifPresent(professional -> setAppointmentProfessionalName(appointment, professional));
		Optional<Object[]> documentProblems = documentsProblems.stream()
				.filter(document -> appointment.getDocumentId() != null && appointment.getDocumentId().equals(((BigInteger)document[0]).longValue()))
				.findFirst();
		documentProblems.ifPresent(document -> appointment.setDiagnoses((String) document[1]));
	}

	private void setAppointmentProfessionalName(NominalAppointmentDetailBo appointment, Object[] professional) {
		String professionalName = sharedPersonPort.parseCompletePersonName((String) professional[1], (String) professional[2], (String) professional[3], (String) professional[4], (String) professional[5]);
		appointment.setIssuerAppointmentFullName(professionalName);
	}

	private void setDiaryProfessionalName(NominalAppointmentDetailBo appointment, Object[] professional) {
		String professionalName = sharedPersonPort.parseCompletePersonName((String) professional[1], (String) professional[2], (String) professional[3], (String) professional[4], (String) professional[5]);
		appointment.setProfessionalName(professionalName);
	}

	private List<Integer> getHierarchicalUnitIds(Integer hierarchicalUnitId) {
		var result = fetchDescendantsByHierarchicalUnitId.run(hierarchicalUnitId);
		result.add(hierarchicalUnitId);
		return result;
	}

	private List<String> getNominalAppointmentsDetailColumnNames () {
		return List.of("Provincia", "Municipio", "Cod_Estable", "Establecimiento", "Tipo de unidad jerárquica", "Unidad jerárquica", "Apellidos paciente", "Nombres paciente", "Nombre autopercibido", "Tipo documento",
				"Nro documento", "Fecha de nacimiento", "Género autopercibido", "Fecha_turno", "Hora_turno", "Estado_turno", "Domicilio", "Teléfono", "Mail", "Obra social/Prepaga", "Nro de afiliado",
				"Especialidad", "Profesional", "Diagnóstico", "Usuario que otorgo el turno");
	}

	private List<CellContent> getNominalAppointmentsHeaderData(IWorkbook wb) {
		List<CellContent> data = new ArrayList<>();

		var basicStyle = nominalDetailExcelService.getBasicStyle(wb);
		var titleStyle = nominalDetailExcelService.getTitleStyle(wb);
		var fieldStyle = nominalDetailExcelService.getFieldStyle(wb);
		var subTitleStyle = nominalDetailExcelService.getSubTitleStyle(wb);

		int nRow = 0;

		data.add(new CellContent(nRow, 0, 1, 2, "",  basicStyle));
		data.add(new CellContent(nRow, 2, 2, 1, "2", titleStyle));
		data.add(new CellContent(nRow, 3, 2, 16,
				"Detalle nominal de Turnos", titleStyle));
		data.add(new CellContent(nRow, 19, 1, 3, "1. Hoja N°", fieldStyle));
		data.add(new CellContent(nRow, 22, 1, 3, "", basicStyle));

		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "", basicStyle));
		data.add(new CellContent(nRow, 19, 1, 3, "", basicStyle));
		data.add(new CellContent(nRow, 22, 1, 3, "", basicStyle));
		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "2. ESTABLECIMIENTO", fieldStyle));
		data.add(new CellContent(nRow, 2, 1, 14, "", basicStyle));
		data.add(new CellContent(nRow, 16, 1, 1, "3. MES", fieldStyle));
		data.add(new CellContent(nRow, 17, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 18, 1, 1, "4. AÑO", fieldStyle));
		data.add(new CellContent(nRow, 19, 1, 3, "", basicStyle));
		data.add(new CellContent(nRow, 22, 1, 3, "", basicStyle));
		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "5. PARTIDO", fieldStyle));
		data.add(new CellContent(nRow, 2, 1, 8, "", basicStyle));
		data.add(new CellContent(nRow, 10, 1, 1, "6. REGIÓN SANITARIA", fieldStyle));
		data.add(new CellContent(nRow, 11, 1, 5, "", basicStyle));
		data.add(new CellContent(nRow, 16, 1, 3, "7. SERVICIO", fieldStyle));
		data.add(new CellContent(nRow, 19, 1, 3, "", basicStyle));
		data.add(new CellContent(nRow, 22, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 23, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 24, 1, 1, "", basicStyle));

		nRow++;
		int column = 0;
		List<String> subtitles = getNominalAppointmentsDetailColumnNames();
		for(String subtitle : subtitles)
			data.add(new CellContent(nRow, column++, 1, 1, subtitle, subTitleStyle));

		return data;
	}

	private void fillNominalAppointmentsDetailRow(IRow row, NominalAppointmentDetailBo content, ICellStyle style){
		AtomicInteger rowNumber = new AtomicInteger(0);
		ICell cell = row.createCell(rowNumber.getAndIncrement());
		cell.setCellValue(content.getProvince());
		cell.setCellStyle(style);
		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(content.getDepartment());
		cell2.setCellStyle(style);
		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(content.getSisaCode());
		cell3.setCellStyle(style);
		ICell cell4 = row.createCell(rowNumber.getAndIncrement());
		cell4.setCellValue(content.getInstitution());
		cell4.setCellStyle(style);
		ICell cell5 = row.createCell(rowNumber.getAndIncrement());
		cell5.setCellValue(content.getHierarchicalUnitType());
		cell5.setCellStyle(style);
		ICell cell6 = row.createCell(rowNumber.getAndIncrement());
		cell6.setCellValue(content.getHierarchicalUnitAlias());
		cell6.setCellStyle(style);

		ICell cell7 = row.createCell(rowNumber.getAndIncrement());
		cell7.setCellValue(content.getPatientSurname());
		cell7.setCellStyle(style);
		ICell cell8 = row.createCell(rowNumber.getAndIncrement());
		cell8.setCellValue(content.getPatientNames());
		cell8.setCellStyle(style);
		ICell cell9 = row.createCell(rowNumber.getAndIncrement());
		cell9.setCellValue(content.getPatientSelfPerceivedName());
		cell9.setCellStyle(style);
		ICell cell10 = row.createCell(rowNumber.getAndIncrement());
		cell10.setCellValue(content.getIdentificationType());
		cell10.setCellStyle(style);
		ICell cell11 = row.createCell(rowNumber.getAndIncrement());
		cell11.setCellValue(content.getIdentificationNumber());
		cell11.setCellStyle(style);
		ICell cell12 = row.createCell(rowNumber.getAndIncrement());
		cell12.setCellValue(content.getBirthDate() != null ? content.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null);
		cell12.setCellStyle(style);
		ICell cell13 = row.createCell(rowNumber.getAndIncrement());
		cell13.setCellValue(content.getSelfPerceivedGender());
		cell13.setCellStyle(style);

		ICell cell14 = row.createCell(rowNumber.getAndIncrement());
		cell14.setCellValue(content.getAppointmentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		cell14.setCellStyle(style);
		ICell cell15 = row.createCell(rowNumber.getAndIncrement());
		cell15.setCellValue(content.getAppointmentHour().format(DateTimeFormatter.ofPattern("HH:mm")));
		cell15.setCellStyle(style);
		ICell cell16 = row.createCell(rowNumber.getAndIncrement());
		cell16.setCellValue(content.getAppointmentState());
		cell16.setCellStyle(style);

		ICell cell17 = row.createCell(rowNumber.getAndIncrement());
		cell17.setCellValue(content.getAddress());
		cell17.setCellStyle(style);
		ICell cell18 = row.createCell(rowNumber.getAndIncrement());
		cell18.setCellValue(content.getPhoneNumber());
		cell18.setCellStyle(style);
		ICell cell19 = row.createCell(rowNumber.getAndIncrement());
		cell19.setCellValue(content.getEmail());
		cell19.setCellStyle(style);

		ICell cell20 = row.createCell(rowNumber.getAndIncrement());
		cell20.setCellValue(content.getCoverageName());
		cell20.setCellStyle(style);
		ICell cell21 = row.createCell(rowNumber.getAndIncrement());
		cell21.setCellValue(content.getAffiliateNumber());
		cell21.setCellStyle(style);

		ICell cell22 = row.createCell(rowNumber.getAndIncrement());
		cell22.setCellValue(content.getClinicalSpecialty());
		cell22.setCellStyle(style);
		ICell cell23 = row.createCell(rowNumber.getAndIncrement());
		cell23.setCellValue(content.getProfessionalName());
		cell23.setCellStyle(style);
		ICell cell24 = row.createCell(rowNumber.getAndIncrement());
		cell24.setCellValue(content.getDiagnoses());
		cell24.setCellStyle(style);
		ICell cell25 = row.createCell(rowNumber.getAndIncrement());
		cell25.setCellValue(content.getIssuerAppointmentFullName());
		cell25.setCellStyle(style);
	}
}
