package ar.lamansys.sgh.publicapi.activities.infrastructure.output;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ar.lamansys.sgh.publicapi.activities.domain.PersonInfoExtendedBo;
import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.DateBo;
import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.DateTimeBo;

import ar.lamansys.sgh.publicapi.activities.domain.datetimeutils.TimeBo;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.activities.application.port.out.ActivityStorage;
import ar.lamansys.sgh.publicapi.activities.domain.AttentionInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.CoverageActivityInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.GenderEnum;
import ar.lamansys.sgh.publicapi.activities.domain.InternmentBo;
import ar.lamansys.sgh.publicapi.activities.domain.PersonInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.ProfessionalBo;
import ar.lamansys.sgh.publicapi.activities.domain.ScopeEnum;
import ar.lamansys.sgh.publicapi.activities.domain.SingleDiagnosticBo;
import ar.lamansys.sgh.publicapi.activities.domain.SnomedBo;
import ar.lamansys.sgh.publicapi.activities.domain.SnomedCIE10Bo;

@RequiredArgsConstructor
@Service
public class ActivityStorageImpl implements ActivityStorage {

	private static final Logger LOG = LoggerFactory.getLogger(ActivityStorageImpl.class);

	private final EntityManager entityManager;

	private final LocalDateMapper localDateMapper;

	private final String V_ATTENTION_FILTERED =
			"WITH v_attention_filtered AS ( " +
					"   SELECT va.id, va.performed_date, va.scope_id, va.encounter_id, va.created_on, " +
					"          va.patient_id, va.doctor_id, va.clinical_speciality_id " +
					"   FROM v_attention va " +
					"   JOIN institution i ON va.institution_id = i.id " +
					"   LEFT JOIN attention_reads ar ON (ar.attention_id = va.id ) " +
					"   %1$s " +
					") ";

	String QUERY =
			V_ATTENTION_FILTERED +
					" ( " +
					"   SELECT va.id AS attention_id, va.performed_date AS attention_date, snm.name, snm.sctid_code, " +
					"          ppat.first_name AS person_name, ppat.last_name AS person_last_name, " +
					"          ppat.identification_number AS person_identification_number, " +
					"          ppat.gender_id AS person_gender_id, ppat.birth_date AS person_birth_date, " +
					"          pmc.affiliate_number, va.scope_id, pd.administrative_discharge_date, hp.id, " +
					"          pprof.first_name AS doctor_name, pprof.last_name AS doctor_last_name, hp.license_number, " +
					"          pprof.identification_number AS doctor_identification_number, mc.cuit, " +
					"          va.encounter_id AS encounter_id, s3.sctid, s3.pt, hc.main, hc.problem_id, " +
					"          hc.verification_status_id, hc.updated_on, mcp.plan, hc.cie10_codes, va.created_on, " +
					"          ppat.middle_names, ppat.other_last_names, pepat.email, pepat.name_self_determination, " +
					"          pepat.gender_self_determination, CAST(NULL AS TIMESTAMP) " +
					"   FROM v_attention_filtered va " +
					"   JOIN patient pat ON (pat.id = va.patient_id) " +
					"   JOIN person ppat ON ppat.id = pat.person_id " +
					"   JOIN person_extended pepat ON pepat.person_id = ppat.id " +
					"   JOIN healthcare_professional hp ON (hp.id = va.doctor_id) " +
					"   JOIN person pprof ON hp.person_id = pprof.id " +
					"   LEFT JOIN clinical_specialty snm ON (va.clinical_speciality_id = snm.id) " +
					"   LEFT JOIN patient_discharge pd ON (pd.internment_episode_id = va.encounter_id) " +
					"   LEFT JOIN internment_episode event ON event.id = va.encounter_id " +
					"   LEFT JOIN patient_medical_coverage pmc ON (pmc.id = event.patient_medical_coverage_id) " +
					"   LEFT JOIN medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
					"   LEFT JOIN medical_coverage_plan mcp ON (mc.id = mcp.medical_coverage_id) " +
					"   LEFT JOIN document_health_condition dhc ON dhc.document_id = va.id " +
					"   LEFT JOIN health_condition hc ON hc.id = dhc.health_condition_id " +
					"   LEFT JOIN snomed s3 ON s3.id = hc.snomed_id " +
					"   WHERE va.scope_id = 0 %2$s" +
					" ) " +

					" UNION ALL " +

					" ( " +
					"   SELECT va.id AS attention_id, va.performed_date AS attention_date, snm.name, snm.sctid_code, " +
					"          ppat.first_name AS person_name, ppat.last_name AS person_last_name, " +
					"          ppat.identification_number AS person_identification_number, " +
					"          ppat.gender_id AS person_gender_id, ppat.birth_date AS person_birth_date, " +
					"          pmc.affiliate_number, va.scope_id, CAST(NULL AS TIMESTAMP), hp.id, " +
					"          pprof.first_name AS doctor_name, pprof.last_name AS doctor_last_name, hp.license_number, " +
					"          pprof.identification_number AS doctor_identification_number, mc.cuit, " +
					"          va.encounter_id AS encounter_id, s3.sctid, s3.pt, hc.main, hc.problem_id, " +
					"          hc.verification_status_id, hc.updated_on, mcp.plan, hc.cie10_codes, va.created_on, " +
					"          ppat.middle_names, ppat.other_last_names, pepat.email, pepat.name_self_determination, " +
					"          pepat.gender_self_determination, CAST(NULL AS TIMESTAMP) " +
					"   FROM v_attention_filtered va " +
					"   JOIN patient pat ON (pat.id = va.patient_id) " +
					"   JOIN person ppat ON ppat.id = pat.person_id " +
					"   JOIN person_extended pepat ON pepat.person_id = ppat.id " +
					"   JOIN healthcare_professional hp ON (hp.id = va.doctor_id) " +
					"   JOIN person pprof ON hp.person_id = pprof.id " +
					"   LEFT JOIN clinical_specialty snm ON (va.clinical_speciality_id = snm.id) " +
					"   LEFT JOIN outpatient_consultation event ON event.id = va.encounter_id " +
					"   LEFT JOIN patient_medical_coverage pmc ON (pmc.id = event.patient_medical_coverage_id) " +
					"   LEFT JOIN medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
					"   LEFT JOIN medical_coverage_plan mcp ON (mc.id = mcp.medical_coverage_id) " +
					"   LEFT JOIN document_health_condition dhc ON dhc.document_id = va.id " +
					"   LEFT JOIN health_condition hc ON hc.id = dhc.health_condition_id " +
					"   LEFT JOIN snomed s3 ON s3.id = hc.snomed_id " +
					"   WHERE va.scope_id = 1 %2$s " +
					" ) " +

					" UNION ALL " +

					" ( " +
					"   SELECT va.id AS attention_id, va.performed_date AS attention_date, snm.name, snm.sctid_code, " +
					"          ppat.first_name AS person_name, ppat.last_name AS person_last_name, " +
					"          ppat.identification_number AS person_identification_number, " +
					"          ppat.gender_id AS person_gender_id, ppat.birth_date AS person_birth_date, " +
					"          pmc.affiliate_number, va.scope_id, CAST(NULL AS TIMESTAMP), hp.id, " +
					"          pprof.first_name AS doctor_name, pprof.last_name AS doctor_last_name, hp.license_number, " +
					"          pprof.identification_number AS doctor_identification_number, mc.cuit, " +
					"          va.encounter_id AS encounter_id, s3.sctid, s3.pt, hc.main, hc.problem_id, " +
					"          hc.verification_status_id, hc.updated_on, mcp.plan, hc.cie10_codes, va.created_on, " +
					"          ppat.middle_names, ppat.other_last_names, pepat.email, pepat.name_self_determination, " +
					"          pepat.gender_self_determination, CAST(NULL AS TIMESTAMP) " +
					"   FROM v_attention_filtered va " +
					"   JOIN patient pat ON (pat.id = va.patient_id) " +
					"   JOIN person ppat ON ppat.id = pat.person_id " +
					"   JOIN person_extended pepat ON pepat.person_id = ppat.id " +
					"   JOIN healthcare_professional hp ON (hp.id = va.doctor_id) " +
					"   JOIN person pprof ON hp.person_id = pprof.id " +
					"   LEFT JOIN clinical_specialty snm ON (va.clinical_speciality_id = snm.id) " +
					"   LEFT JOIN document_odontology_diagnostic dod ON (dod.document_id = va.id) " +
					"   LEFT JOIN odontology_diagnostic od ON od.id = (dod.odontology_diagnostic_id) " +
					"   LEFT JOIN odontology_consultation event ON event.id = va.encounter_id " +
					"   LEFT JOIN patient_medical_coverage pmc ON (pmc.id = event.patient_medical_coverage_id) " +
					"   LEFT JOIN medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
					"   LEFT JOIN medical_coverage_plan mcp ON (mc.id = mcp.medical_coverage_id) " +
					"   LEFT JOIN document_health_condition dhc ON dhc.document_id = va.id " +
					"   LEFT JOIN health_condition hc ON hc.id = dhc.health_condition_id " +
					"   LEFT JOIN snomed s3 ON s3.id = od.snomed_id " +
					"   WHERE va.scope_id = 6 %2$s " +
					" ) " +

					" UNION ALL " +

					" ( " +
					"   SELECT va.id AS attention_id, va.performed_date AS attention_date, snm.name, snm.sctid_code, " +
					"          ppat.first_name AS person_name, ppat.last_name AS person_last_name, " +
					"          ppat.identification_number AS person_identification_number, " +
					"          ppat.gender_id AS person_gender_id, ppat.birth_date AS person_birth_date, " +
					"          pmc.affiliate_number, va.scope_id, CAST(NULL AS TIMESTAMP), hp.id, " +
					"          pprof.first_name AS doctor_name, pprof.last_name AS doctor_last_name, hp.license_number, " +
					"          pprof.identification_number AS doctor_identification_number, mc.cuit, " +
					"          va.encounter_id AS encounter_id, s3.sctid, s3.pt, hc.main, hc.problem_id, " +
					"          hc.verification_status_id, hc.updated_on, mcp.plan, hc.cie10_codes, va.created_on, " +
					"          ppat.middle_names, ppat.other_last_names, pepat.email, pepat.name_self_determination, " +
					"          pepat.gender_self_determination, ecd.administrative_discharge_on " +
					"   FROM v_attention_filtered va " +
					"   JOIN patient pat ON (pat.id = va.patient_id) " +
					"   JOIN person ppat ON ppat.id = pat.person_id " +
					"   JOIN person_extended pepat ON pepat.person_id = ppat.id " +
					"   JOIN healthcare_professional hp ON (hp.id = va.doctor_id) " +
					"   JOIN person pprof ON hp.person_id = pprof.id " +
					"   LEFT JOIN clinical_specialty snm ON (va.clinical_speciality_id = snm.id) " +
					"   LEFT JOIN emergency_care_episode event ON event.id = va.encounter_id " +
					"   LEFT JOIN patient_medical_coverage pmc ON (pmc.id = event.patient_medical_coverage_id) " +
					"   LEFT JOIN medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
					"   LEFT JOIN medical_coverage_plan mcp ON (mc.id = mcp.medical_coverage_id) " +
					"   LEFT JOIN document_health_condition dhc ON dhc.document_id = va.id " +
					"   LEFT JOIN health_condition hc ON hc.id = dhc.health_condition_id " +
					"   LEFT JOIN snomed s3 ON s3.id = hc.snomed_id " +
					"   LEFT JOIN emergency_care_discharge ecd ON (ecd.emergency_care_episode_id = va.encounter_id) " +
					"   WHERE va.scope_id = 4 %2$s " +
					" ) ";

	private final String WHERE_CLAUSE = " WHERE CAST(va.updated_on AS DATE) BETWEEN :fromDate AND :toDate " +
			" AND (ar.attention_id IS NULL OR NOT ar.processed OR ar.processed AND :reprocessing) " +
			" AND i.sisa_code = :refsetCode ";

	@Override
	public Optional<AttentionInfoBo> getActivityById(String refsetCode, Long activityId) {
		LOG.debug("getActivityById ActivityStorage -> refsetCode {}, activityId {}", refsetCode, activityId);

		Query query = entityManager.createNativeQuery(String.format(QUERY, " WHERE va.id = :activityId ", " "))
				.setParameter("activityId", activityId);

		var queryResult = query.getResultList();

		Object[] resultSearch = queryResult.size() == 1 ? (Object[]) queryResult.get(0) : null;

		Optional<AttentionInfoBo> result = Optional.ofNullable(resultSearch).map(this::parseToAttentionInfoBo);

		LOG.trace("Output -> {}", result);
		return result;
	}

	@Override
	public List<AttentionInfoBo> getActivitiesByInstitution(String refsetCode, LocalDate fromDate, LocalDate toDate, Boolean reprocessing) {
		LOG.debug("getActivitiesByInstitution ActivityStorage -> refsetCode {}, fromDate {}, toDate {}, reprocessing{}",
				refsetCode, fromDate, toDate, reprocessing);

		Query query = entityManager.createNativeQuery(String.format(QUERY, WHERE_CLAUSE, ""))
				.setParameter("refsetCode", refsetCode)
				.setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate)
				.setParameter("reprocessing", reprocessing);

		List<Object[]> queryResult = query.getResultList();

		List<AttentionInfoBo> result = queryResult
				.stream()
				.map(this::parseToAttentionInfoBo)
				.collect(Collectors.toList());

		LOG.debug("Output size -> {}", result.size());
		LOG.trace("Output -> {}", result);
		return result;
	}

	@Override
	public List<AttentionInfoBo> getActivitiesByInstitutionAndPatient(
			String refsetCode, String identificationNumber, LocalDate fromDate, LocalDate toDate, Boolean reprocessing) {
		LOG.debug("getActivitiesByInstitutionAndPatient ActivityStorage -> refsetCode {}, identificationNumber {}, fromDate {}, toDate {}, reprocessing{}",
				refsetCode, identificationNumber, fromDate, toDate, reprocessing);

		String finalQuery = String.format(QUERY, WHERE_CLAUSE, " AND ppat.identification_number = :identificationNumber ");

		Query query = entityManager.createNativeQuery(finalQuery)
				.setParameter("refsetCode", refsetCode)
				.setParameter("identificationNumber", identificationNumber)
				.setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate)
				.setParameter("reprocessing", reprocessing);
		List<Object[]> queryResult = query.getResultList();

		List<AttentionInfoBo> result = queryResult
				.stream()
				.map(this::parseToAttentionInfoBo)
				.collect(Collectors.toList());

		LOG.debug("Output size -> {}", result.size());
		LOG.trace("Output -> {}", result);
		return result;
	}

	@Override
	public List<AttentionInfoBo> getActivitiesByInstitutionAndCoverage(
			String refsetCode, String coverageCuit, LocalDate fromDate, LocalDate toDate, Boolean reprocessing) {

		LOG.debug("getActivitiesByInstitutionAndCoverage ActivityStorage -> refsetCode {}, coverageCuit {}, fromDate {}, toDate {}, reprocessing{}",
				refsetCode, coverageCuit, fromDate, toDate, reprocessing);

		String finalQuery = String.format(QUERY, WHERE_CLAUSE, " AND mc.cuit = :coverageCuit ");

		Query query = entityManager.createNativeQuery(finalQuery)
				.setParameter("refsetCode", refsetCode)
				.setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate)
				.setParameter("reprocessing", reprocessing)
				.setParameter("coverageCuit", coverageCuit);
		List<Object[]> queryResult = query.getResultList();

		List<AttentionInfoBo> result = queryResult
				.stream()
				.map(this::parseToAttentionInfoBo)
				.collect(Collectors.toList());

		LOG.debug("Output size -> {}", result.size());
		LOG.trace("Output -> {}", result);
		return result;
	}

	private AttentionInfoBo parseToAttentionInfoBo(Object[] rawAttention) {
		var attentionDate = localDateMapper.fromLocalDateTime(((Timestamp) rawAttention[1]).toLocalDateTime());
		return new AttentionInfoBo(
				((BigInteger) rawAttention[0]).longValue(),
				((Integer)rawAttention[18]).longValue(),
				attentionDate,
				new SnomedBo((String) rawAttention[3], (String) rawAttention[2]),
				buildPersonInfoBo(rawAttention),
				buildCoverageInfoBo(rawAttention),
				ScopeEnum.map((Short) rawAttention[10]),
				buildInternmentBo(rawAttention),
				buildProfessionalBo(rawAttention),
				buildDiagnoses(rawAttention),
				buildDateTimeBo(rawAttention, 27),
				buildPersonExtendedInfoBo(rawAttention),
				buildDateTimeBo(rawAttention, 33)
		);
	}

	private PersonInfoExtendedBo buildPersonExtendedInfoBo(Object[] rawAttention) {
		return new PersonInfoExtendedBo(
				(String)rawAttention[28],
				(String)rawAttention[29],
				(String)rawAttention[30],
				(String)rawAttention[31],
				(Short)rawAttention[32]
		);
	}

	private DateTimeBo buildDateTimeBo(Object[] rawAttention, int arrayIndex) {
		if (rawAttention[arrayIndex] != null)
			return getDateTimeBo(rawAttention[arrayIndex]);
		return null;
	}

	private DateTimeBo getDateTimeBo(Object rawAttention) {
		var localDate = localDateMapper.fromLocalDateTimeToZonedDateTime(((Timestamp) rawAttention).toLocalDateTime());
		return new DateTimeBo(new DateBo(localDate.toLocalDate().getYear(), localDate.toLocalDate().getMonthValue(), localDate.toLocalDate().getDayOfMonth()), new TimeBo(localDate.getHour(), localDate.getMinute(), localDate.getSecond()));
	}

	private SingleDiagnosticBo buildDiagnoses(Object[] rawAttention) {
		return new SingleDiagnosticBo(
				new SnomedCIE10Bo((String) rawAttention[19], (String) rawAttention[20], (String) rawAttention[26]),
				(Boolean) rawAttention[21],
				(String) rawAttention[22],
				(String) rawAttention[23],
				rawAttention[24] != null ? ((Timestamp) rawAttention[1]).toLocalDateTime() : null
		);
	}

	private CoverageActivityInfoBo buildCoverageInfoBo(Object[] rawAttention) {
		return CoverageActivityInfoBo.builder()
				.affiliateNumber((String) rawAttention[9])
				.cuit((String) rawAttention[17])
				.plan(rawAttention[25] !=  null ? (String) rawAttention[25] : null)
				.build();
	}

	private PersonInfoBo buildPersonInfoBo(Object[] rawAttention) {
		return new PersonInfoBo((String) rawAttention[6],
				(String) rawAttention[4],
				(String) rawAttention[5],
				rawAttention[8] == null ? null : ((Date) rawAttention[8]).toLocalDate(),
				rawAttention[7] == null ? null : GenderEnum.map((Short) rawAttention[7]));
	}

	private ProfessionalBo buildProfessionalBo(Object[] rawAttention) {
		return new ProfessionalBo((Integer) rawAttention[12],
				(String) rawAttention[13],
				(String) rawAttention[14],
				(String) rawAttention[15],
				(String) rawAttention[16]);
	}

	private InternmentBo buildInternmentBo(Object[] rawAttention) {
		return ScopeEnum.map((Short) rawAttention[10]).equals(ScopeEnum.INTERNACION) ?
				new InternmentBo(rawAttention[0].toString(),
						rawAttention[1] != null ? ((Timestamp) rawAttention[1]).toLocalDateTime() : null,
						rawAttention[11] != null ? ((Timestamp) rawAttention[11]).toLocalDateTime() : null) :
				new InternmentBo();
	}

}