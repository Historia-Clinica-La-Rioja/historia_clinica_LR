package ar.lamansys.sgh.publicapi.activities.infrastructure.output;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.activities.application.port.out.ActivityInfoStorage;
import ar.lamansys.sgh.publicapi.activities.domain.BedRelocationInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.DocumentInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.ProcedureInformationBo;
import ar.lamansys.sgh.publicapi.activities.domain.SnomedBo;
import ar.lamansys.sgh.publicapi.activities.domain.SupplyInformationBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.EIndicationStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.ENursingRecordStatus;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ActivityInfoStorageImpl implements ActivityInfoStorage {

	private static final Logger LOG = LoggerFactory.getLogger(ActivityInfoStorageImpl.class);
	private static final int HOSPITALIZATION_SOURCE_ID = 0;
	private static final short PARENTERAL = 0;
	private static final short PHARMACO = 1;
	private static final short VACCINE = 2;

	private final EntityManager entityManager;
	private final LocalDateMapper localDateMapper;

	@Override
	public List<ProcedureInformationBo> getProceduresByActivity(String refsetCode, Long activityId) {
		LOG.debug("getBedRelocationsByActivity ActivityInfoStorage -> refsetCode {}, activityId {}", refsetCode, activityId);

		String sqlString = "SELECT s2.pt, s2.sctid, p.updated_on " +
				"FROM {h-schema}v_attention va " +
				"JOIN {h-schema}institution i ON (i.sisa_code = :refsetCode AND va.institution_id = i.id) " +
				"JOIN {h-schema}document d ON (d.source_id = va.encounter_id AND d.source_type_id = va.scope_id) " +
				"JOIN {h-schema}document_procedure dp ON dp.document_id = d.id " +
				"JOIN {h-schema}procedures p ON p.id = dp.procedure_id " +
				"JOIN {h-schema}snomed s2 ON p.snomed_id = s2.id " +
				"WHERE va.id = :activityId AND (p.status_id = " + "CAST(385651009 AS VARCHAR)" +
				"OR p.status_id = " + " CAST(255594003 AS VARCHAR)) AND p.created_on >= va.performed_date ";

		Query query = entityManager.createNativeQuery(sqlString)
				.setParameter("refsetCode", refsetCode)
				.setParameter("activityId", activityId);

		List<Object[]> queryResult = query.getResultList();

		List<ProcedureInformationBo> result = queryResult
				.stream()
				.map(procedure ->
						ProcedureInformationBo.builder()
								.snomedBo(new SnomedBo((String) procedure[1], (String) procedure[0]))
								.administrationTime(localDateMapper
										.fromLocalDateTimeToZonedDateTime(((Timestamp) procedure[2]).toLocalDateTime())
										.toLocalDateTime())
								.build())
				.collect(Collectors.toList());

		LOG.trace("Output -> {}", result);
		return result;
	}

	@Override
	public List<SupplyInformationBo> getSuppliesByActivity(String refsetCode, Long activityId) {
		LOG.debug("getBedRelocationsByActivity ActivityInfoStorage -> refsetCode {}, activityId {}", refsetCode, activityId);

		String pharmacoQueryString = "SELECT " + PHARMACO + " AS type, nr.status_id, s2.pt, s2.sctid, " +
				"CASE WHEN nr.scheduled_administration_time IS NULL THEN nr.created_on ELSE nr.scheduled_administration_time END AS admin_time " +
				"FROM {h-schema}v_attention va " +
				"JOIN {h-schema}institution i ON (i.sisa_code = :refsetCode AND va.institution_id = i.id) " +
				"JOIN {h-schema}document d ON (d.source_id = va.encounter_id AND d.source_type_id = va.scope_id) " +
				"JOIN {h-schema}document_indication di ON di.document_id = d.id " +
				"JOIN {h-schema}indication i2 ON i2.id = di.indication_id " +
				"JOIN {h-schema}nursing_record nr ON nr.indication_id = i2.id " +
				"JOIN {h-schema}pharmaco p4 ON p4.id = i2.id " +
				"JOIN {h-schema}snomed s2 ON s2.id = p4.snomed_id " +
				"WHERE va.id = :activityId AND " +
				"p4.patient_provided <> TRUE AND " +
				"nr.status_id = " + ENursingRecordStatus.COMPLETED.getId();

		String parenteralQueryString = "SELECT " + PARENTERAL + " AS type, nr.status_id, s2.pt, s2.sctid, " +
				"CASE WHEN nr.scheduled_administration_time IS NULL THEN nr.created_on ELSE nr.scheduled_administration_time END AS admin_time " +
				"FROM {h-schema}v_attention va " +
				"JOIN {h-schema}institution i ON (i.sisa_code = :refsetCode AND va.institution_id = i.id) " +
				"JOIN {h-schema}document d ON (d.source_id = va.encounter_id AND d.source_type_id = va.scope_id) " +
				"JOIN {h-schema}document_indication di ON di.document_id = d.id " +
				"JOIN {h-schema}indication i2 ON i2.id = di.indication_id " +
				"JOIN {h-schema}nursing_record nr ON nr.indication_id = i2.id " +
				"JOIN {h-schema}parenteral_plan p4 ON p4.id = i2.id " +
				"JOIN {h-schema}snomed s2 ON s2.id = p4.snomed_id " +
				"WHERE va.id = :activityId AND nr.status_id = " + ENursingRecordStatus.COMPLETED.getId();

		String vaccineQueryString = "SELECT " + VACCINE + " AS type, " + EIndicationStatus.COMPLETED.getId() + " , s2.pt, s2.sctid, i2.created_on " +
				"FROM {h-schema}v_attention va " +
				"JOIN {h-schema}institution i ON (i.sisa_code = :refsetCode AND va.institution_id = i.id) " +
				"JOIN {h-schema}document d ON (d.source_id = va.encounter_id AND d.source_type_id = va.scope_id) " +
				"JOIN {h-schema}document_inmunization di ON di.document_id = d.id " +
				"JOIN {h-schema}inmunization i2 ON i2.id = di.inmunization_id " +
				"JOIN {h-schema}snomed s2 ON s2.id = i2.snomed_id " +
				"WHERE va.id = :activityId AND i2.status_id = CAST(255594003 AS VARCHAR)";

		String suppliesQuery = parenteralQueryString + " UNION ALL " + pharmacoQueryString;

		Query query = entityManager.createNativeQuery(suppliesQuery)
				.setParameter("refsetCode", refsetCode)
				.setParameter("activityId", activityId);

		List<Object[]> queryResult = query.getResultList();

		List<SupplyInformationBo> result = queryResult.stream()
				.map(this::parseSupplyInformationBo)
				.collect(Collectors.toList());

		LOG.trace("Output -> {}", result);
		return result;
	}

	@Override
	public List<BedRelocationInfoBo> getBedRelocationsByActivity(String refsetCode, Long activityId) {
		LOG.debug("getBedRelocationsByActivity ActivityInfoStorage -> refsetCode {}, activityId {}", refsetCode, activityId);

		final String CARE_TYPE_ID_SUBQUERY = " (SELECT ct.description " +
				"FROM {h-schema}bed b " +
				"JOIN {h-schema}room r ON b.room_id = r.id " +
				"JOIN {h-schema}sector s ON r.sector_id = s.id " +
				"JOIN {h-schema}care_type ct ON s.care_type_id = ct.id " +
				"WHERE b.id = hpbr.destination_bed_id) ";

		final String FILTERED_ATTENTION_VIEW = " WITH filtered_v_attention AS ( " +
				" SELECT va.id, va.encounter_id, va.institution_id, va.clinical_speciality_id " +
				" FROM v_attention va " +
				" WHERE va.id = :activityId ) ";

		final String SQL_STRING = FILTERED_ATTENTION_VIEW + "(SELECT hpbr.destination_bed_id, b.bed_number, bc.description AS bed_category, hpbr.relocation_date, snm.sctid, snm.pt, " + CARE_TYPE_ID_SUBQUERY +
			" FROM filtered_v_attention va " +
			" JOIN historic_patient_bed_relocation hpbr ON (hpbr.internment_episode_id = va.encounter_id) " +
			" JOIN institution i ON (i.sisa_code = :refsetCode AND va.institution_id = i.id) " +
			" JOIN bed b ON b.id = hpbr.destination_bed_id " +
			" LEFT JOIN bed_category bc ON bc.id = b.bed_category_id " +
			" LEFT JOIN (SELECT cs.clinical_specialty_type_id, s.sctid, s.pt " +
			" FROM clinical_specialty cs " +
			" JOIN snomed s ON s.sctid = cs.sctid_code) snm ON (va.clinical_speciality_id = snm.clinical_specialty_type_id) " +

			" UNION ALL " +

			" SELECT b.id, b.bed_number, bc.description as bed_category, ie.entry_date, snm.sctid, snm.pt, ct.description " +
			" FROM filtered_v_attention va " +
			" JOIN institution i ON (i.sisa_code = :refsetCode AND va.institution_id = i.id) " +
			" JOIN internment_episode ie ON ie.id = va.encounter_id " +
			" JOIN bed b ON b.id = ie.bed_id " +
			" left join bed_category bc on bc.id = b.bed_category_id " +
			" JOIN room r ON b.room_id = r.id " +
			" JOIN sector s ON r.sector_id = s.id " +
			" JOIN care_type ct ON s.care_type_id = ct.id " +
			" LEFT JOIN (SELECT cs.id, s.sctid, s.pt " +
			" FROM clinical_specialty cs " +
			" JOIN snomed s ON s.sctid = cs.sctid_code) snm ON (va.clinical_speciality_id = snm.id) " +
			" WHERE not exists (SELECT 1 FROM historic_patient_bed_relocation hpbr2 WHERE hpbr2.internment_episode_id = ie.id) " +

			" UNION ALL " +

			" ( WITH oldest_origin_bed AS (  " +
			"    SELECT hpbr.origin_bed_id,  " +
			"    ( ROW_NUMBER() OVER (PARTITION BY internment_episode_id ORDER BY relocation_date ASC)) AS seqnum, " +
			"    va.encounter_id " +
			"    FROM historic_patient_bed_relocation hpbr " +
			"    JOIN filtered_v_attention va ON va.encounter_id = hpbr.internment_episode_id )  " +
			" (SELECT oob.origin_bed_id, b.bed_number, bc.description AS bed_category, ie.entry_date, snm.sctid, snm.pt, ct.description " +
			" FROM filtered_v_attention va " +
			" JOIN institution i ON (i.sisa_code = :refsetCode AND va.institution_id = i.id) " +
			" JOIN internment_episode ie ON ie.id = va.encounter_id " +
			" JOIN oldest_origin_bed oob ON oob.encounter_id = va.encounter_id " +
			" JOIN bed b ON b.id = oob.origin_bed_id " +
			" LEFT JOIN bed_category bc ON bc.id = b.bed_category_id " +
			" JOIN room r ON b.room_id = r.id " +
			" JOIN sector s ON r.sector_id = s.id " +
			" JOIN care_type ct ON s.care_type_id = ct.id " +
			" LEFT JOIN (SELECT cs.id, s.sctid, s.pt FROM clinical_specialty cs JOIN snomed s ON s.sctid = cs.sctid_code) snm ON (va.clinical_speciality_id = snm.id) " +
			" WHERE EXISTS (SELECT 1 FROM historic_patient_bed_relocation hpbr2 WHERE hpbr2.internment_episode_id = ie.id) " +
			" AND oob.seqnum = 1 ))) " +
			" ORDER BY relocation_date ASC";

		Query query = entityManager.createNativeQuery(SQL_STRING)
				.setParameter("refsetCode", refsetCode)
				.setParameter("activityId", activityId);

		List<Object[]> queryResult = query.getResultList();

		List<BedRelocationInfoBo> result = queryResult
				.stream()
				.map(this::parseToBedRelocationInfoBo)
				.collect(Collectors.toList());

		LOG.trace("Output -> {}", result);
		return result;
	}

	@Override
	public List<DocumentInfoBo> getDocumentsByActivity(String refsetCode, Long activityId) {
		LOG.debug("getDocumentsByActivity ActivityInfoStorage -> refsetCode {}, activityId {}", refsetCode, activityId);

		String sqlString = "SELECT df.id, df.file_path, df.file_name, df.updated_on, dt.description " +
				"FROM {h-schema}v_attention va " +
				"JOIN {h-schema}institution i ON (i.sisa_code = :refsetCode AND va.institution_id = i.id) " +
				"JOIN {h-schema}document_file df ON df.source_id = va.encounter_id AND df.source_type_id = va.scope_id " +
				"JOIN {h-schema}document_type dt ON df.type_id = dt.id  " +
				"WHERE va.id = :activityId " ;


		Query query = entityManager.createNativeQuery(sqlString)
				.setParameter("refsetCode", refsetCode)
				.setParameter("activityId", activityId);

		List<Object[]> queryResult = query.getResultList();

		List<DocumentInfoBo> result = queryResult
				.stream()
				.map(row ->
						DocumentInfoBo.builder()
								.id(((BigInteger) row[0]).longValue())
								.filePath((String) row[1])
								.fileName((String) row[2])
								.updateOn(((Timestamp)row[3]).toLocalDateTime())
								.type((String) row[4])
								.build())
				.collect(Collectors.toList());

		LOG.trace("Output -> {}", result);
		return result;
	}

	private BedRelocationInfoBo parseToBedRelocationInfoBo(Object[] rawBedRelocation) {
		return new BedRelocationInfoBo(
				(Integer) rawBedRelocation[0],
				((String) rawBedRelocation[1]),
				((String) rawBedRelocation[2]),
				((Timestamp) rawBedRelocation[3]).toLocalDateTime(),
				((String) rawBedRelocation[6]),
				new SnomedBo((String) rawBedRelocation[4], (String) rawBedRelocation[5])
		);
	}

	private SupplyInformationBo parseSupplyInformationBo(Object[] supplyInformation) {
		return SupplyInformationBo.builder()
				.supplyType(mapSupplyTypes((Integer) supplyInformation[0]))
				.status(EIndicationStatus.map(((Integer)supplyInformation[1]).shortValue()).getValue())
				.snomedBo(new SnomedBo((String) supplyInformation[3], (String) supplyInformation[2]))
				.administrationTime(((Timestamp)supplyInformation[4]).toLocalDateTime())
				.build();
	}

	private String mapSupplyTypes(Integer type) {
		if(type == PHARMACO) return "Fármaco";
		if(type == PARENTERAL) return "Parenteral";
		if(type == VACCINE) return "Vacuna";
		return "Tipo desconocido";
	}


}