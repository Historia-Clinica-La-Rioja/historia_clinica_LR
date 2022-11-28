package ar.lamansys.sgh.publicapi.infrastructure.output;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ar.lamansys.sgh.publicapi.domain.DocumentInfoBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.application.port.out.ActivityInfoStorage;
import ar.lamansys.sgh.publicapi.domain.BedRelocationInfoBo;
import ar.lamansys.sgh.publicapi.domain.ProcedureInformationBo;
import ar.lamansys.sgh.publicapi.domain.SnomedBo;
import ar.lamansys.sgh.publicapi.domain.SupplyInformationBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.EIndicationStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.ENursingRecordStatus;

@Service
public class ActivityInfoStorageImpl implements ActivityInfoStorage {

	private static final Logger LOG = LoggerFactory.getLogger(ActivityInfoStorageImpl.class);
	private static final int HOSPITALIZATION_SOURCE_ID = 0;
	private static final short PARENTERAL = 0;
	private static final short PHARMACO = 1;
	private static final short VACCINE = 2;

	private final EntityManager entityManager;
	private final AttentionReadsRepository attentionReadsRepository;

	public ActivityInfoStorageImpl(EntityManager entityManager, AttentionReadsRepository attentionReadsRepository) {
		this.entityManager = entityManager;
		this.attentionReadsRepository = attentionReadsRepository;
	}

	@Override
	public void processActivity(String refsetCode, Long activityId) {
		LOG.debug("update ActivityInfoStorage -> refsetCode {}, activityId {}", refsetCode, activityId);
		List<AttentionReads> toUpdate = attentionReadsRepository.findByAttentionId(activityId);
		toUpdate.forEach(ar -> ar.setProcessed(true));
		LOG.trace("Updated -> {}", attentionReadsRepository.saveAll(toUpdate));
	}

	@Override
	public List<ProcedureInformationBo> getProceduresByActivity(String refsetCode, Long activityId) {
		LOG.debug("getBedRelocationsByActivity ActivityInfoStorage -> refsetCode {}, activityId {}", refsetCode, activityId);

		String sqlString = "SELECT s2.pt, s2.sctid, p.updated_on " +
				"FROM {h-schema}v_attention va " +
				"JOIN {h-schema}institution i ON (i.sisa_code = :refsetCode AND va.institution_id = i.id) " +
				"JOIN {h-schema}document d ON d.source_id = va.encounter_id " +
				"JOIN {h-schema}document_procedure dp ON dp.document_id = d.id " +
				"JOIN {h-schema}procedures p ON p.id = dp.procedure_id " +
				"JOIN {h-schema}snomed s2 ON p.snomed_id = s2.id " +
				"WHERE va.id = :activityId AND (p.status_id = " + "CAST(385651009 AS VARCHAR)" +
				"OR p.status_id = " + " CAST(255594003 AS VARCHAR)) AND p.created_on >= va.performed_date " +

				" UNION ALL " +

				"SELECT s2.pt, s2.sctid, dr.effective_time " +
				"FROM {h-schema}v_attention va " +
				"JOIN {h-schema}institution i ON (i.sisa_code = :refsetCode AND va.institution_id = i.id) " +
				"JOIN {h-schema}diagnostic_report dr ON dr.patient_id = va.patient_id " +
				"JOIN {h-schema}snomed s2 ON dr.snomed_id = s2.id " +
				"WHERE va.id = :activityId AND dr.status_id = CAST(261782000 AS VARCHAR) AND dr.created_on >= va.performed_date";


		Query query = entityManager.createNativeQuery(sqlString)
				.setParameter("refsetCode", refsetCode)
				.setParameter("activityId", activityId);

		List<Object[]> queryResult = query.getResultList();

		List<ProcedureInformationBo> result = queryResult
				.stream()
				.map(procedure ->
						ProcedureInformationBo.builder()
								.snomedBo(new SnomedBo((String) procedure[1], (String) procedure[0]))
								.administrationTime(((Timestamp)procedure[2]).toLocalDateTime())
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
				"JOIN {h-schema}document d ON d.source_id = va.encounter_id " +
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
				"JOIN {h-schema}document d ON d.source_id = va.encounter_id " +
				"JOIN {h-schema}document_indication di ON di.document_id = d.id " +
				"JOIN {h-schema}indication i2 ON i2.id = di.indication_id " +
				"JOIN {h-schema}nursing_record nr ON nr.indication_id = i2.id " +
				"JOIN {h-schema}parenteral_plan p4 ON p4.id = i2.id " +
				"JOIN {h-schema}snomed s2 ON s2.id = p4.snomed_id " +
				"WHERE va.id = :activityId AND nr.status_id = " + ENursingRecordStatus.COMPLETED.getId();

		String vaccineQueryString = "SELECT " + VACCINE + " AS type, " + EIndicationStatus.COMPLETED.getId() + " , s2.pt, s2.sctid, i2.created_on " +
				"FROM {h-schema}v_attention va " +
				"JOIN {h-schema}institution i ON (i.sisa_code = :refsetCode AND va.institution_id = i.id) " +
				"JOIN {h-schema}document d ON d.source_id = va.encounter_id " +
				"JOIN {h-schema}document_inmunization di ON di.document_id = d.id " +
				"JOIN {h-schema}inmunization i2 ON i2.id = di.inmunization_id " +
				"JOIN {h-schema}snomed s2 ON s2.id = i2.snomed_id " +
				"WHERE va.id = :activityId AND i2.status_id = CAST(255594003 AS VARCHAR)";

		String suppliesQuery = parenteralQueryString + " UNION ALL " + pharmacoQueryString + " UNION ALL " + vaccineQueryString;

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

		String careTypeIdSubQuery = "SELECT ct.description " +
				"FROM {h-schema}bed b " +
				"JOIN {h-schema}room r ON b.room_id = r.id " +
				"JOIN {h-schema}sector s ON r.sector_id = s.id " +
				"JOIN {h-schema}care_type ct ON s.care_type_id = ct.id " +
				"WHERE b.id = hpbr.destination_bed_id ";

		String sqlString = "SELECT hpbr.destination_bed_id, hpbr.relocation_date, snm.sctid, snm.pt, ("+careTypeIdSubQuery+")" +
				"FROM {h-schema}v_attention va " +
				"JOIN {h-schema}historic_patient_bed_relocation hpbr ON (hpbr.internment_episode_id = va.encounter_id) " +
				"JOIN {h-schema}institution i ON (i.sisa_code = :refsetCode AND va.institution_id = i.id) " +
				"LEFT JOIN (SELECT cs.clinical_specialty_type_id, s.sctid, s.pt " +
				"FROM {h-schema}clinical_specialty cs " +
				"JOIN {h-schema}snomed s ON s.sctid = cs.sctid_code) snm ON (va.clinical_speciality_id = snm.clinical_specialty_type_id) " +
				"WHERE va.id = :activityId AND va.scope_id = " + HOSPITALIZATION_SOURCE_ID +
				" UNION ALL " +
				"SELECT b.id, ie.entry_date, snm.sctid, snm.pt, ct.description " +
				"FROM {h-schema}v_attention va " +
				"JOIN {h-schema}institution i ON (i.sisa_code = :refsetCode AND va.institution_id = i.id) " +
				"JOIN {h-schema}internment_episode ie ON ie.id = va.encounter_id " +
				"JOIN {h-schema}bed b ON b.id = ie.bed_id " +
				"JOIN {h-schema}room r ON b.room_id = r.id " +
				"JOIN {h-schema}sector s ON r.sector_id = s.id " +
				"JOIN {h-schema}care_type ct ON s.care_type_id = ct.id " +
				"LEFT JOIN (SELECT cs.id, s.sctid, s.pt " +
				"FROM {h-schema}clinical_specialty cs " +
				"JOIN {h-schema}snomed s ON s.sctid = cs.sctid_code) snm ON (va.clinical_speciality_id = snm.id) " +
				"WHERE va.id = :activityId AND va.scope_id = " + HOSPITALIZATION_SOURCE_ID;

		Query query = entityManager.createNativeQuery(sqlString)
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
				((Timestamp) rawBedRelocation[1]).toLocalDateTime(),
				((String) rawBedRelocation[4]),
				new SnomedBo((String) rawBedRelocation[2], (String) rawBedRelocation[3])
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