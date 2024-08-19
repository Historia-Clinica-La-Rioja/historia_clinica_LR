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

import java.sql.Date;
import java.sql.Time;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

		String sqlQuery = "SELECT p.description as province, " +
				"dp.description as department, " +
				"i.sisa_code, " +
				"i.name AS institution, " +
				"hut.description, " +
				"hu.alias, " +
				"CONCAT(p3.first_name, ' ', p3.middle_names) AS patientNames, " +
				"CONCAT(p3.last_name, ' ', p3.other_last_names) AS patientSurnames, " +
				"pe.name_self_determination AS patientNameSelfDetermination, " +
				"it.description AS identificationType, " +
				"p3.identification_number, " +
				"p3.birth_date, " +
				"spg.description AS selfPerceivedGender, " +
				"apt.date_type_id, " +
				"apt.hour, " +
				"as2.description AS appointmentState, " +
				"CONCAT(a2.street, ' ', a2.number, ' ', a2.floor, ' ', a2.apartment, ' ', c.description) AS address, " +
				"CASE WHEN (apt.phone_prefix IS NOT NULL AND apt.phone_number IS NOT NULL) THEN CONCAT(apt.phone_prefix, apt.phone_number) " +
				"ELSE CONCAT(pe.phone_prefix, pe.phone_number) END AS phone, " +
				"CASE WHEN apt.patient_email IS NOT NULL THEN apt.patient_email ELSE pe.email END, " +
				"mc.name AS coverageName, " +
				"pmc.affiliate_number AS affiliateNumber, " +
				"cs.name AS clinicalSpecialty, " +
				"p4.first_name AS professionalFirstName, " +
				"p4.middle_names AS professionalMiddleNames, " +
				"p4.last_name AS professionalLastName, " +
				"p4.other_last_names AS professionalOtherLastNames, " +
				"pe2.name_self_determination AS professionalNameSelfDetermination, " +
				"problems.description AS problems, " +
				"p5.first_name, p5.middle_names, p5.last_name, p5.other_last_names, pe3.name_self_determination " +
				"FROM {h-schema}institution i " +
				"JOIN {h-schema}address a ON (i.address_id = a.id) " +
				"JOIN {h-schema}city c ON (a.city_id = c.id)" +
				"JOIN {h-schema}department dp ON (c.department_id = dp.id) " +
				"JOIN {h-schema}province p ON (dp.province_id = p.id) " +
				"JOIN {h-schema}doctors_office dof ON (i.id = dof.institution_id) " +
				"JOIN {h-schema}diary d2 ON (dof.id = d2.doctors_office_id) " +
				"JOIN {h-schema}appointment_assn aa ON (d2.id = aa.diary_id) " +
				"JOIN {h-schema}appointment apt ON (aa.appointment_id = apt.id) " +
				"LEFT JOIN {h-schema}hierarchical_unit hu ON (d2.hierarchical_unit_id = hu.id) " +
				"LEFT JOIN {h-schema}hierarchical_unit_type hut ON (hu.type_id = hut.id) " +
				"JOIN {h-schema}patient p2 ON (apt.patient_id = p2.id) " +
				"JOIN {h-schema}person p3 ON (p2.person_id = p3.id) " +
				"JOIN {h-schema}person_extended pe ON (p3.id = pe.person_id) " +
				"JOIN {h-schema}address a2 ON (pe.address_id = a2.id) " +
				"LEFT JOIN {h-schema}self_perceived_gender spg ON (pe.gender_self_determination = spg.id) " +
				"LEFT JOIN {h-schema}identification_type it ON (p3.identification_type_id = it.id) " +
				"JOIN {h-schema}appointment_state as2 ON (apt.appointment_state_id = as2.id) " +
				"LEFT JOIN {h-schema}patient_medical_coverage pmc ON (apt.patient_medical_coverage_id  = pmc.id) " +
				"LEFT JOIN {h-schema}medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
				"LEFT JOIN {h-schema}clinical_specialty cs ON (d2.clinical_specialty_id = cs.id) " +
				"JOIN {h-schema}healthcare_professional hp ON (hp.id = d2.healthcare_professional_id) " +
				"JOIN {h-schema}person p4 ON (hp.person_id = p4.id) " +
				"JOIN {h-schema}person_extended pe2 ON (p4.id = pe2.person_id) " +
				"LEFT JOIN {h-schema}document_appointment da ON (da.appointment_id = apt.id) " +
				"LEFT JOIN {h-schema}document d ON (da.document_id  = d.id) " +
				"LEFT JOIN (SELECT dhc.document_id, STRING_AGG(s.pt, ', ') as description " +
							"FROM {h-schema}document_health_condition dhc " +
							"JOIN {h-schema}health_condition hc ON (hc.id = dhc.health_condition_id) " +
							"JOIN {h-schema}snomed s ON (s.id = hc.snomed_id) " +
							"GROUP BY dhc.document_id) AS problems ON (problems.document_id = d.id) " +
				"LEFT JOIN {h-schema}user_person up ON (apt.created_by = up.user_id) " +
				"LEFT JOIN {h-schema}person p5 ON (up.person_id = p5.id) " +
				"JOIN {h-schema}person_extended pe3 ON (p5.id = pe3.person_id) " +
 				"WHERE i.id = :institutionId  " +
				"AND apt.date_type_id BETWEEN :startDate AND :endDate ";

		if (filter.getHierarchicalUnitTypeId() != null)
			sqlQuery += " AND hu.type_id = " + filter.getHierarchicalUnitTypeId();

		sqlQuery += filter.getHierarchicalUnitId() != null
				? filter.isIncludeHierarchicalUnitDescendants()
					? " AND hu.id IN (:hierarchicalUnitIds) "
					: " AND hu.id = " + filter.getHierarchicalUnitId()
				: "";

		if (filter.getClinicalSpecialtyId() != null)
			sqlQuery += " AND d2.clinical_specialty_id = " + filter.getClinicalSpecialtyId();

		if (filter.getDoctorId() != null)
			sqlQuery += " AND hp.id = " + filter.getDoctorId();

		if (filter.getAppointmentStateId() != null)
			sqlQuery += " AND apt.appointment_state_id = " + filter.getAppointmentStateId();

		var nativeQuery = entityManager.createNativeQuery(sqlQuery)
				.setParameter("institutionId", filter.getInstitutionId())
				.setParameter("startDate", filter.getFromDate())
				.setParameter("endDate", filter.getToDate());

		if (filter.getHierarchicalUnitId() != null && filter.isIncludeHierarchicalUnitDescendants())
			nativeQuery.setParameter("hierarchicalUnitIds", getHierarchicalUnitIds(filter.getHierarchicalUnitId()));

		List<Object[]> appointmentsData = nativeQuery.getResultList();

		List<NominalAppointmentDetailBo> result = new ArrayList<>();
		appointmentsData.forEach(a ->
				result.add(new NominalAppointmentDetailBo(
						(String) a[0],
						(String) a[1],
						(String) a[2],
						(String) a[3],
						(String) a[4],
						(String) a[5],
						(String) a[6],
						(String) a[7],
						(String) a[8],
						(String) a[9],
						(String) a[10],
						a[11] != null ? ((Date)a[11]).toLocalDate() : null,
						(String) a[12],
						((Date)a[13]).toLocalDate(),
						((Time)a[14]).toLocalTime(),
						(String) a[15],
						(String) a[16],
						(String) a[17],
						(String) a[18],
						(String) a[19],
						(String) a[20],
						(String) a[21],
						sharedPersonPort.parseCompletePersonName((String) a[22], (String) a[23],
								(String) a[24], (String) a[25], (String) a[26]),
						(String) a[27],
						sharedPersonPort.parseCompletePersonName((String) a[28], (String) a[29],
								(String) a[30], (String) a[31], (String) a[32]))
				)
		);
		log.debug("Nominal appointments detail size -> {} ", result.size());
		result.sort(Comparator.comparing(NominalAppointmentDetailBo::getAppointmentDate).thenComparing(NominalAppointmentDetailBo::getAppointmentHour));
		return result;
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
