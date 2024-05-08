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
import net.pladema.reports.application.ports.InstitutionReportStorage;
import net.pladema.reports.application.ports.NominalECEpisodeDetailStorage;
import net.pladema.reports.domain.NominalECEpisodeDetailBo;
import net.pladema.reports.domain.ReportSearchFilterBo;
import net.pladema.reports.repository.InstitutionInfo;
import net.pladema.reports.service.NominalDetailExcelService;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Slf4j
@Service
public class NominalECEpisodeDetailStorageImpl implements NominalECEpisodeDetailStorage {

	private final EntityManager entityManager;

	private final SharedPersonPort sharedPersonPort;

	private final NominalDetailExcelService nominalDetailExcelService;

	private final FetchDescendantsByHierarchicalUnitId fetchDescendantsByHierarchicalUnitId;

	private final InstitutionReportStorage institutionReportStorage;

	@Override
	public IWorkbook buildNominalECEpisodeDetailExcelReport(String title, ReportSearchFilterBo filter) {

		InstitutionInfo institutionInfo = institutionReportStorage.getInstitutionInfo(filter.getInstitutionId());

		var emergencyCareEpisodesDetail = this.fetchNominalEmergencyCareEpisodeDetail(filter, institutionInfo);

		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		ISheet sheet = wb.createSheet(title);
		nominalDetailExcelService.fillRow(sheet, getNominalECEpisodeHeaderData(wb, institutionInfo, filter.getFromDate()));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle styleDataRow = nominalDetailExcelService.createDataRowStyle(wb);

		emergencyCareEpisodesDetail.forEach(
				resultData -> {
					IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
					fillNominalECEpisodeDetailRow(newDataRow, styleDataRow, resultData, institutionInfo);
				}
		);

		nominalDetailExcelService.setDimensions(sheet);
		return wb;
	}

	public List<NominalECEpisodeDetailBo> fetchNominalEmergencyCareEpisodeDetail(ReportSearchFilterBo filter, InstitutionInfo institutionInfo) {

		LocalDateTime startDate = filter.getFromDate().atTime(3, 0);
		LocalDateTime endDate = filter.getToDate().atTime(3, 0).plusDays(1);

		String sqlQuery = "SELECT " +
				"hut.description as hierarchical_unit_type, " +
				"hu.alias as hierarchical_unit, " +
				"patient_data.given_name as p_given_name, " +
				"patient_data.family_names as p_family_name, " +
				"patient_data.name_self_determination as p_name_self_determination, " +
				"pa.id as patient_id, " +
				"patient_data.identification_type as p_identification_type, " +
				"patient_data.identification_number as p_identitication_number, " +
				"patient_data.birth_date as p_birth_date, " +
				"patient_data.gender as p_gender, " +
				"patient_data.self_perceived_gender as p_self_perceived_gender, " +
				"a.quarter as p_quarter, " +
				"CONCAT(a.street, ' ', a.number, ' ', a.floor, ' ', a.apartment, ' ', c.description) as p_address, " +
				"CONCAT(pex.phone_prefix, pex.phone_number) as p_phone, " +
				"pex.email as p_email, " +
				"mc.name ece_mc, " +
				"pmc.affiliate_number as ece_mc_affiliate_number, " +
				"patient_coverages.description as p_coverages, " +
				"ece.id as ece_id, " +
				"ece.created_on as ece_created_on, " +
				"author_episode.given_name as author_episode_given_name, " +
				"author_episode.family_names as author_episode_family_names, " +
				"author_episode.name_self_determination as author_episode_name_self_determination, " +
				"ect.description as ece_type, " +
				"last_triage.created_on as last_trage_created_on, " +
				"triages.quantity as triages_quantity, " +
				"tc.name as last_triage_catory, " +
				"ecs.description as ece_state," +
				"last_attention.created_on as last_attention_created_on, " +
				"do2.description as last_attention_doctor_office, " +
				"s2.description as last_attention_shockroom, " +
				"r.description as last_attention_room, " +
				"b.bed_number as last_attention_bed, " +
				"reasons.description as reasons, " +
				"diagnosis.description as diagnosis, " +
				"ecd.medical_discharge_on as ece_medical_discharge_on, " +
				"CONCAT_WS('/split/', proced.description, order_proced.description) as procedures, " +
				"author_last_triage.given_name as author_last_triage_given_name, " +
				"author_last_triage.family_names as author_last_triage_family_names, " +
				"author_last_triage.name_self_determination as author_last_triage_name_self_determination, " +
				"author_last_attention.given_name as author_last_attention_given_name, " +
				"author_last_attention.family_names as author_last_attention_family_names, " +
				"author_last_attention.name_self_determination as author_last_attention_name_self_determination, " +
				"author_medical_discharge.given_name as author_medical_discharge_given_name, " +
				"author_medical_discharge.family_names as author_medical_discharge_family_names, " +
				"author_medical_discharge.name_self_determination as author_medical_discharge_name_self_determination, " +
				"attentions.quantity as attentions_quantity, "+
				"dt.description as ece_discharge_type," +
				"pa.person_id as p_person_id, " +
				"hp.id as author_last_attention_professional_id " +
				"FROM {h-schema}emergency_care_episode ece " +
				"JOIN {h-schema}patient pa ON (ece.patient_id = pa.id) " +
				"LEFT JOIN {h-schema}v_user_person_complete_data AS patient_data ON (pa.person_id = patient_data.person_id) " +
				"LEFT JOIN {h-schema}person_extended pex ON (pa.person_id = pex.person_id) " +
				"LEFT JOIN {h-schema}address a ON (pex.address_id = a.id) " +
				"LEFT JOIN {h-schema}city c ON (a.city_id = c.id) " +
				"LEFT JOIN {h-schema}patient_medical_coverage pmc ON (ece.patient_medical_coverage_id = pmc.id) " +
				"LEFT JOIN {h-schema}medical_coverage mc ON (pmc.medical_coverage_id = mc.id)  " +
				"LEFT JOIN (SELECT pmc.patient_id, STRING_AGG(mc.name, ', ') AS description " +
				"			FROM {h-schema}patient_medical_coverage pmc " +
				"			JOIN {h-schema}medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
				"			WHERE pmc.active IS TRUE " +
				"			GROUP BY pmc.patient_id " +
				"			) AS patient_coverages ON (patient_coverages.patient_id = ece.patient_id) " +
				"JOIN {h-schema}v_user_person_complete_data AS author_episode ON (ece.created_by = author_episode.user_id) " +
				"LEFT JOIN {h-schema}emergency_care_type ect ON (ece.emergency_care_type_id = ect.id) " +
				"LEFT JOIN {h-schema}emergency_care_state ecs ON (ece.emergency_care_state_id = ecs.id) " +
				"LEFT JOIN (SELECT t.emergency_care_episode_id AS ece_id, COUNT(t.id) AS quantity " +
				"			FROM {h-schema}triage t " +
				"			GROUP BY t.emergency_care_episode_id " +
				"			) AS triages ON (ece.id = triages.ece_id) " +
				"LEFT JOIN (SELECT ecer.emergency_care_episode_id AS ece_id , STRING_AGG(r.description, ', ') AS description " +
				"			FROM {h-schema}emergency_care_episode_reason ecer " +
				"			JOIN {h-schema}reasons r ON (r.id = ecer.reason_id) " +
				"			GROUP BY ecer.emergency_care_episode_id" +
				"			) AS reasons ON (reasons.ece_id = ece.id) " +
				"LEFT JOIN {h-schema}document doc ON (ece.id = doc.source_id AND doc.type_id = 16 AND doc.status_id = '445665009') " +
				"LEFT JOIN (SELECT dhc.document_id AS document_id,  STRING_AGG(s.pt, ', ') AS description " +
				"			FROM {h-schema}document_health_condition dhc " +
				"			JOIN {h-schema}health_condition hc ON (dhc.health_condition_id = hc.id) " +
				"			JOIN {h-schema}snomed s ON (s.id = hc.snomed_id) " +
				"			GROUP BY dhc.document_id " +
				"			) AS diagnosis ON (diagnosis.document_id = doc.id) " +
				"LEFT JOIN (SELECT dp.document_id AS document_id, STRING_AGG(s.pt, ', ') AS description " +
				"			FROM {h-schema}document_procedure dp " +
				"			JOIN {h-schema}procedures p ON (dp.procedure_id = p.id) " +
				"			JOIN {h-schema}snomed s ON (p.snomed_id = s.id) " +
				"			GROUP BY dp.document_id " +
				"			) AS proced ON (proced.document_id = doc.id) " +
				"LEFT JOIN (SELECT sr.source_id AS ece_id, STRING_AGG(s.pt, ', ') AS description " +
				"			FROM {h-schema}service_request sr " +
				"			JOIN {h-schema}document doc ON (sr.id = doc.source_id AND doc.source_type_id = 3) " +
				"			JOIN {h-schema}document_diagnostic_report ddr ON (doc.id = ddr.document_id) " +
				"			JOIN {h-schema}diagnostic_report dr ON (ddr.diagnostic_report_id = dr.id AND dr.status_id = '261782000') " +
				"			JOIN {h-schema}snomed s ON (dr.snomed_id = s.id) " +
				"			WHERE sr.source_type_id = 4 " +
				"			GROUP BY sr.source_id " +
				"			) AS order_proced ON (order_proced.ece_id = ece.id) " +
				"LEFT JOIN {h-schema}emergency_care_discharge ecd ON (ece.id = ecd.emergency_care_episode_id) " +
				"LEFT JOIN {h-schema}discharge_type dt ON (ecd.discharge_type_id = dt.id) " +
				"LEFT JOIN {h-schema}v_user_person_complete_data AS author_medical_discharge ON (ecd.medical_discharge_by_professional = author_medical_discharge.user_id) " +
				"LEFT JOIN (SELECT DISTINCT ON(t.emergency_care_episode_id)  t.emergency_care_episode_id AS ece_id, t.created_on, t.triage_category_id, t.created_by " +
				"			FROM {h-schema}triage AS t " +
				"			ORDER BY t.emergency_care_episode_id, t.created_on DESC " +
				"			) AS last_triage ON (last_triage.ece_id = ece.id) " +
				"LEFT JOIN {h-schema}triage_category AS tc ON (last_triage.triage_category_id = tc.id)" +
				"LEFT JOIN {h-schema}v_user_person_complete_data AS author_last_triage ON (last_triage.created_by = author_last_triage.user_id) " +
				"LEFT JOIN (SELECT DISTINCT ON(hee.emergency_care_episode_id) hee.emergency_care_episode_id AS ece_id, hee.created_on, hee.created_by, hee.doctors_office_id, hee.bed_id, hee.shockroom_id " +
				"			FROM {h-schema}historic_emergency_episode hee " +
				"			WHERE hee.emergency_care_state_id = 1 " +
				"			ORDER BY hee.emergency_care_episode_id, hee.created_on DESC " +
				"			) AS last_attention ON (ece.id = last_attention.ece_id) " +
				"LEFT JOIN {h-schema}doctors_office do2 ON (last_attention.doctors_office_id = do2.id) " +
				"LEFT JOIN {h-schema}shockroom s2 ON (last_attention.shockroom_id = s2.id) " +
				"LEFT JOIN {h-schema}bed b ON (last_attention.bed_id = b.id) " +
				"LEFT JOIN {h-schema}room r ON (b.room_id = r.id) " +
				"LEFT JOIN {h-schema}doctors_office AS bed_sector ON (r.sector_id = bed_sector.id) " +
				"LEFT JOIN {h-schema}hierarchical_unit_sector hus ON (do2.sector_id = hus.sector_id OR s2.sector_id = hus.sector_id OR r.sector_id = hus.sector_id) " +
				"LEFT JOIN {h-schema}hierarchical_unit hu ON (hus.hierarchical_unit_id = hu.id) " +
				"LEFT JOIN {h-schema}hierarchical_unit_type hut ON (hu.type_id = hut.id) " +
				"LEFT JOIN {h-schema}v_user_person_complete_data AS author_last_attention ON (last_attention.created_by = author_last_attention.user_id ) " +
				"LEFT JOIN (SELECT hee.emergency_care_episode_id AS ece_id, COUNT(hee.emergency_care_episode_id) AS quantity " +
				"			FROM {h-schema}historic_emergency_episode hee " +
				"			WHERE hee.emergency_care_state_id = 1 " +
				"			GROUP BY hee.emergency_care_episode_id " +
				"			) AS attentions ON (ece.id = attentions.ece_id) " +
				"LEFT JOIN healthcare_professional hp ON (author_last_attention.person_id = hp.person_id) " +
				"WHERE ece.institution_id = :institutionId " +
				"AND ece.created_on BETWEEN :startDate AND :endDate ";

		if (filter.getHierarchicalUnitTypeId() != null)
			sqlQuery += " AND hu.type_id = " + filter.getHierarchicalUnitTypeId();

		sqlQuery += filter.getHierarchicalUnitId() != null
				? filter.isIncludeHierarchicalUnitDescendants()
				? " AND hu.id IN (:hierarchicalUnitIds) "
				: " AND hu.id = " + filter.getHierarchicalUnitId()
				: "";

		if (filter.getDoctorId() != null)
			sqlQuery += " AND hp.id = " + filter.getDoctorId();

		var query = entityManager.createNativeQuery(sqlQuery);

		if (filter.getHierarchicalUnitId() != null && filter.isIncludeHierarchicalUnitDescendants())
			query.setParameter("hierarchicalUnitIds", getHierarchicalUnitIds(filter.getHierarchicalUnitId()));

		query.setParameter("institutionId", filter.getInstitutionId());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);


		List<Object[]> emergencyCareEpisodesData = query.getResultList();

		List<NominalECEpisodeDetailBo> result = new ArrayList<>();
		emergencyCareEpisodesData.forEach(e ->
				result.add(new NominalECEpisodeDetailBo(
						(String) e[0],
						(String) e[1],
						(String) e[2],
						(String) e[3],
						(String) e[4],
						(Integer) e[5],
						(String) e[6],
						(String) e[7],
						e[8] != null ? ((Date)e[8]).toLocalDate() : null,
						(String) e[9],
						(String) e[10],
						(String) e[11],
						(String) e[12],
						(String) e[13],
						(String) e[14],
						(String) e[15],
						(String) e[16],
						(String) e[17],
						(Integer) e[18],
						e[19] != null ? ((Timestamp)e[19]).toLocalDateTime() : null,
						(String) e[20],
						(String) e[21],
						(String) e[22],
						(String) e[23],
						e[24] != null ? ((Timestamp)e[24]).toLocalDateTime() : null,
						e[25] != null ? ((BigInteger)e[25]).intValue() : null,
						(String) e[26],
						(String) e[27],
						e[28] != null ? ((Timestamp)e[28]).toLocalDateTime() : null,
						(String) e[29],
						(String) e[30],
						(String) e[31],
						(String) e[32],
						(String) e[33],
						(String) e[34],
						e[35] != null ? ((Timestamp)e[35]).toLocalDateTime() : null,
						(String) e[36],
						(String) e[37],
						(String) e[38],
						(String) e[39],
						(String) e[40],
						(String) e[41],
						(String) e[42],
						(String) e[43],
						(String) e[44],
						(String) e[45],
						e[46] != null ? ((BigInteger)e[46]).intValue() : null,
						(String) e[47],
						(Integer) e[48],
						(Integer) e[49])
				)
		);
		processData(result);
		result.sort(Comparator.comparing(NominalECEpisodeDetailBo::getEpisodeCreatedOn));
		log.debug("Nominal appointments detail size -> {} ", result.size());
		return result;
	}

	private void processData(List<NominalECEpisodeDetailBo> result) {
		result.forEach( ece -> {
			if (ece.getPersonId() == null)
				ece.setPatientNames("Paciente temporal de guardia");

			ece.setTriageAuthor(sharedPersonPort.parseCompletePersonName(ece.getTriageAuthorNames(), ece.getTriageAuthorSurnames(), ece.getTriageAuthorSelfPerceivedNames()));
			ece.setEncounterProfessional(sharedPersonPort.parseCompletePersonName(ece.getEncounterProfessionalNames(), ece.getEncounterProfessionalSurnames(), ece.getEncounterProfessionalSelfPerceivedNames()));
			ece.setMedicalDischargeProfessional(sharedPersonPort.parseCompletePersonName(ece.getMedicalDischargeAuthorNames(), ece.getMedicalDischargeAuthorSurnames(), ece.getMedicalDischargeAuthorSelfPerceivedNames()));
			ece.setEpisodeAuthor(sharedPersonPort.parseCompletePersonName(ece.getEpisodeAuthorNames(), ece.getEpisodeAuthorSurnames(), ece.getEpisodeAuthorSelfPerceivedNames()));
			ece.setAge(processAge(ece.getBirthDate(), ece.getEpisodeCreatedOn()));
			ece.setAttentionSite(identifyAttentionSite(ece.getAttentionDoctorOffice(), ece.getAttentionShockroom(), ece.getAttentionRoom(), ece.getAttentionBed()));
		});
	}

	private String processAge(LocalDate birthDate, LocalDateTime eceDate) {
		if (birthDate == null)
			return null;
		Period age = Period.between(birthDate, eceDate.toLocalDate());
		int years = age.getYears();
		int months = age.getMonths();

		StringBuilder result = new StringBuilder();
		if (years >= 1)
			result.append(years).append(years == 1 ? " año" : " años");
		else
			result.append(months).append(months == 1 ? " mes" : " meses");
		return result.toString();
	}

	private String identifyAttentionSite(String doctorOffice, String shockroom, String room, String bed) {
		if (doctorOffice != null)
			return doctorOffice;
		if (shockroom != null)
			return shockroom;
		if (room != null && bed != null)
			return "Habitación: " + room + " - Cama: " + bed;
		return null;
	}

	private List<Integer> getHierarchicalUnitIds(Integer hierarchicalUnitId) {
		var result = fetchDescendantsByHierarchicalUnitId.run(hierarchicalUnitId);
		result.add(hierarchicalUnitId);
		return result;
	}

	private List<String> getNominalECEpisodeDetailColumnNames () {
		return List.of("Provincia", "Municipio", "Cod_Estable", "Establecimiento", "Tipo de unidad jerárquica", "Unidad jerárquica", "Apellidos paciente", "Nombres paciente", "Nombre autopercibido", "ID Paciente HSI",
				"Tipo documento", "Nro documento", "Fecha de nacimiento", "Edad", "Sexo documento", "Género autopercibido", "Barrio", "Domicilio", "Teléfono", "Mail", "Obra social/Prepaga asociada episodio", "Nro de afiliado", "Obra social/Prepaga del paciente",
				"Id episodio", "Hora de apertura del episodio", "Usuario creador episodio", "Tipo de guardia", "Fecha y hora ultimo triage", "Cantidad triages episodio",
				"Tipo de triage", "Estado del episodio", "Fecha y hora de atención", "Lugar atención", "Motivo", "Diagnósticos",
				"Fecha y hora alta médica", "Práctica/Procedimientos", "Usuario triage", "Profesional consulta", "Profesional alta médica", "Pases de", "Tipo egreso");
	}

	private List<CellContent> getNominalECEpisodeHeaderData(IWorkbook wb, InstitutionInfo institutionInfo, LocalDate date) {
		List<CellContent> data = new ArrayList<>();
		Locale argentinianLocale = new Locale("es", "AR");
		String month =  date.getMonth().getDisplayName(TextStyle.FULL, argentinianLocale).toUpperCase();
		String year = String.valueOf(date.getYear());

		var basicStyle = nominalDetailExcelService.getBasicStyle(wb);
		var titleStyle = nominalDetailExcelService.getTitleStyle(wb);
		var fieldStyle = nominalDetailExcelService.getFieldStyle(wb);
		var subTitleStyle = nominalDetailExcelService.getSubTitleStyle(wb);

		int nRow = 0;

		data.add(new CellContent(nRow, 0, 1, 2, "",  basicStyle));
		data.add(new CellContent(nRow, 2, 2, 1, "2", titleStyle));
		data.add(new CellContent(nRow, 3, 2, 16,
				"Reporte detalle nominal de atenciones de guardia", titleStyle));
		data.add(new CellContent(nRow, 19, 1, 3, "1. Hoja N°", fieldStyle));
		data.add(new CellContent(nRow, 22, 1, 20, "", basicStyle));

		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "", basicStyle));
		data.add(new CellContent(nRow, 19, 1, 3, "", basicStyle));
		data.add(new CellContent(nRow, 22, 1, 20, "", basicStyle));
		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "2. ESTABLECIMIENTO", fieldStyle));
		data.add(new CellContent(nRow, 2, 1, 14, institutionInfo.getInstitution().toUpperCase(), fieldStyle));
		data.add(new CellContent(nRow, 16, 1, 1, "3. MES", fieldStyle));
		data.add(new CellContent(nRow, 17, 1, 1, month, fieldStyle));
		data.add(new CellContent(nRow, 18, 1, 1, "4. AÑO", fieldStyle));
		data.add(new CellContent(nRow, 19, 1, 3, year, fieldStyle));
		data.add(new CellContent(nRow, 22, 1, 20, "", basicStyle));
		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "5. PARTIDO", fieldStyle));
		data.add(new CellContent(nRow, 2, 1, 8, institutionInfo.getDepartment().toUpperCase(), fieldStyle));
		data.add(new CellContent(nRow, 10, 1, 1, "6. REGIÓN SANITARIA", fieldStyle));
		data.add(new CellContent(nRow, 11, 1, 5, "", basicStyle));
		data.add(new CellContent(nRow, 16, 1, 3, "7. SERVICIO", fieldStyle));
		data.add(new CellContent(nRow, 19, 1, 3, "", basicStyle));
		data.add(new CellContent(nRow, 22, 1, 20, "", basicStyle));
		data.add(new CellContent(nRow, 23, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 24, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 25, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 26, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 27, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 28, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 29, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 30, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 31, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 32, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 33, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 34, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 35, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 36, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 37, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 38, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 39, 1, 1, "", basicStyle));

		nRow++;
		int column = 0;
		List<String> subtitles = getNominalECEpisodeDetailColumnNames();
		for(String subtitle : subtitles)
			data.add(new CellContent(nRow, column++, 1, 1, subtitle, subTitleStyle));

		return data;
	}

	private void fillNominalECEpisodeDetailRow(IRow row, ICellStyle style, NominalECEpisodeDetailBo content, InstitutionInfo institutionInfo) {
		AtomicInteger rowNumber = new AtomicInteger(0);
		nominalDetailExcelService.fillNominalDetailCommonColumns(row, rowNumber, style, institutionInfo, content.getHierarchicalUnitType(), content.getHierarchicalUnitAlias());

		ICell cell7 = row.createCell(rowNumber.getAndIncrement());
		cell7.setCellValue(content.getPatientSurnames());
		cell7.setCellStyle(style);
		ICell cell8 = row.createCell(rowNumber.getAndIncrement());
		cell8.setCellValue(content.getPatientNames());
		cell8.setCellStyle(style);
		ICell cell9 = row.createCell(rowNumber.getAndIncrement());
		cell9.setCellValue(content.getPatientSelfPerceivedName());
		cell9.setCellStyle(style);
		ICell cell10 = row.createCell(rowNumber.getAndIncrement());
		cell10.setCellValue(String.valueOf(content.getPatientId()));
		cell10.setCellStyle(style);
		ICell cell11 = row.createCell(rowNumber.getAndIncrement());
		cell11.setCellValue(content.getIdentificationType());
		cell11.setCellStyle(style);
		ICell cell12 = row.createCell(rowNumber.getAndIncrement());
		cell12.setCellValue(content.getIdentificationNumber());
		cell12.setCellStyle(style);
		ICell cell13 = row.createCell(rowNumber.getAndIncrement());
		cell13.setCellValue(content.getBirthDate() != null ? content.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null);
		cell13.setCellStyle(style);
		ICell cell14 = row.createCell(rowNumber.getAndIncrement());
		cell14.setCellValue(content.getAge());
		cell14.setCellStyle(style);
		ICell cell15 = row.createCell(rowNumber.getAndIncrement());
		cell15.setCellValue(content.getGender());
		cell15.setCellStyle(style);
		ICell cell16 = row.createCell(rowNumber.getAndIncrement());
		cell16.setCellValue(content.getSelfPerceivedGender());
		cell16.setCellStyle(style);

		ICell cell17 = row.createCell(rowNumber.getAndIncrement());
		cell17.setCellValue(content.getQuarter());
		cell17.setCellStyle(style);


		ICell cell18 = row.createCell(rowNumber.getAndIncrement());
		cell18.setCellValue(content.getAddress());
		cell18.setCellStyle(style);
		ICell cell19 = row.createCell(rowNumber.getAndIncrement());
		cell19.setCellValue(content.getPhone());
		cell19.setCellStyle(style);
		ICell cell20 = row.createCell(rowNumber.getAndIncrement());
		cell20.setCellValue(content.getEmail());
		cell20.setCellStyle(style);
		ICell cell21 = row.createCell(rowNumber.getAndIncrement());
		cell21.setCellValue(content.getEpisodeCoverageName());
		cell21.setCellStyle(style);
		ICell cell22 = row.createCell(rowNumber.getAndIncrement());
		cell22.setCellValue(content.getEpisodeCoverageAffiliateNumber());
		cell22.setCellStyle(style);
		ICell cell23 = row.createCell(rowNumber.getAndIncrement());
		cell23.setCellValue(content.getPatientMedicalCoverages());
		cell23.setCellStyle(style);

		ICell cell24 = row.createCell(rowNumber.getAndIncrement());
		cell24.setCellValue(String.valueOf(content.getEpisodeId()));
		cell24.setCellStyle(style);
		ICell cell25 = row.createCell(rowNumber.getAndIncrement());
		cell25.setCellValue(parseAndNormalizeDate(content.getEpisodeCreatedOn()));
		cell25.setCellStyle(style);
		ICell cell26 = row.createCell(rowNumber.getAndIncrement());
		cell26.setCellValue(content.getEpisodeAuthor());
		cell26.setCellStyle(style);
		ICell cell27 = row.createCell(rowNumber.getAndIncrement());
		cell27.setCellValue(content.getEpisodeType());
		cell27.setCellStyle(style);
		ICell cell28 = row.createCell(rowNumber.getAndIncrement());
		cell28.setCellValue(parseAndNormalizeDate(content.getTriageDate()));
		cell28.setCellStyle(style);
		ICell cell29 = row.createCell(rowNumber.getAndIncrement());
		cell29.setCellValue(content.getTriageQuantity() != null ? String.valueOf(content.getTriageQuantity()) : null);
		cell29.setCellStyle(style);
		ICell cell30 = row.createCell(rowNumber.getAndIncrement());
		cell30.setCellValue(content.getTriageType());
		cell30.setCellStyle(style);
		ICell cell31 = row.createCell(rowNumber.getAndIncrement());
		cell31.setCellValue(content.getEpisodeState());
		cell31.setCellStyle(style);
		ICell cell32 = row.createCell(rowNumber.getAndIncrement());
		cell32.setCellValue(parseAndNormalizeDate(content.getLastAttentionCreatedOn()));
		cell32.setCellStyle(style);
		ICell cell33 = row.createCell(rowNumber.getAndIncrement());
		cell33.setCellValue(content.getAttentionSite());
		cell33.setCellStyle(style);
		ICell cell34 = row.createCell(rowNumber.getAndIncrement());
		cell34.setCellValue(content.getReasons());
		cell34.setCellStyle(style);
		ICell cell35 = row.createCell(rowNumber.getAndIncrement());
		cell35.setCellValue(content.getDiagnosis());
		cell35.setCellStyle(style);
		ICell cell36 = row.createCell(rowNumber.getAndIncrement());
		cell36.setCellValue(parseAndNormalizeDate(content.getMedicalDischargeOn()));
		cell36.setCellStyle(style);
		ICell cell37 = row.createCell(rowNumber.getAndIncrement());
		cell37.setCellValue(content.getProcedures());
		cell37.setCellStyle(style);
		ICell cell38 = row.createCell(rowNumber.getAndIncrement());
		cell38.setCellValue(content.getTriageAuthor());
		cell38.setCellStyle(style);
		ICell cell39 = row.createCell(rowNumber.getAndIncrement());
		cell39.setCellValue(content.getEncounterProfessional());
		cell39.setCellStyle(style);
		ICell cell40 = row.createCell(rowNumber.getAndIncrement());
		cell40.setCellValue(content.getMedicalDischargeProfessional());
		cell40.setCellStyle(style);
		ICell cell41 = row.createCell(rowNumber.getAndIncrement());
		cell41.setCellValue(content.getAttentionsQuantity() != null ? String.valueOf(content.getAttentionsQuantity()) : null);
		cell41.setCellStyle(style);
		ICell cell42 = row.createCell(rowNumber.getAndIncrement());
		cell42.setCellValue(content.getDischargeType());
		cell42.setCellStyle(style);

	}

	private String parseAndNormalizeDate(LocalDateTime date) {
		if (date == null)
			return null;
		return date.atZone(ZoneId.of("UTC"))
				.withZoneSameInstant(ZoneId.of("UTC-3"))
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}

}
