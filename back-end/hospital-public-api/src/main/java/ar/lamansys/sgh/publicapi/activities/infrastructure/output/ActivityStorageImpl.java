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

	private static final String JOIN_PATIENT_MEDICAL_COVERAGE = "LEFT JOIN {h-schema}patient_medical_coverage pmc ON (pmc.id = event.patient_medical_coverage_id)";

	private static final String JOIN_HOSPITALIZATION = "LEFT JOIN {h-schema}internment_episode event ON event.id = va.encounter_id " + JOIN_PATIENT_MEDICAL_COVERAGE;
	private static final String JOIN_OUTPATIENT = "LEFT JOIN {h-schema}outpatient_consultation event ON event.id = va.encounter_id " + JOIN_PATIENT_MEDICAL_COVERAGE;
	private static final String JOIN_ODONTOLOGY = "LEFT JOIN {h-schema}odontology_consultation event ON event.id = va.encounter_id " + JOIN_PATIENT_MEDICAL_COVERAGE;
	private static final String JOIN_EMERGENCY_CARE = "LEFT JOIN {h-schema}emergency_care_episode event ON event.id = va.encounter_id " + JOIN_PATIENT_MEDICAL_COVERAGE;

	private static final Integer HOSPITALIZATION = 0;
	private static final Integer OUTPATIENT_CONSULTATION = 1;
	private static final Integer ODONTOLOGY = 6;
	private static final Integer EMERGENCY_CARE = 4;

	private static final String JOIN_ANY_EVENT = "LEFT JOIN {h-schema}internment_episode ie ON ie.id = va.encounter_id AND va.scope_id =" + HOSPITALIZATION + " " +
			"LEFT JOIN {h-schema} outpatient_consultation oc ON oc.id = va.encounter_id and va.scope_id =" + OUTPATIENT_CONSULTATION + " " +
			"LEFT JOIN {h-schema} odontology_consultation odc ON odc.id = va.encounter_id and va.scope_id = " + ODONTOLOGY + " " +
			"LEFT JOIN {h-schema} emergency_care_episode ece ON ece.id = va.encounter_id and va.scope_id = " + EMERGENCY_CARE + " " +
			"LEFT JOIN {h-schema} patient_medical_coverage pmc ON (" +
			"(ie.id IS NOT NULL AND pmc.id = ie.patient_medical_coverage_id) " +
			"OR (oc.id IS NOT NULL AND pmc.id = oc.patient_medical_coverage_id) " +
			"OR (odc.id IS NOT NULL AND pmc.id = odc.patient_medical_coverage_id) " +
			"OR (ece.id IS NOT NULL AND pmc.id = ece.patient_medical_coverage_id) " +
			")";

	private static final String SQL_STRING =
			"SELECT va.id as attention_id, va.performed_date as attention_date, " +
					"snm.name , snm.sctid_code, " +
					"p.first_name as person_name, p.last_name as person_last_name, p.identification_number as person_identification_number, p.gender_id as person_gender_id, p.birth_date as person_birth_date, " +
					"pmc.affiliate_number, " +
					"va.scope_id, " +
					"pd.administrative_discharge_date, " +
					"d.doctor_id, d.first_name as doctor_name, d.last_name as doctor_last_name, d.license_number, d.identification_number as doctor_identification_number, " +
					"mc.cuit, " +
					"va.encounter_id AS encounter_id, " +
					"s3.sctid, " +
					"s3.pt, " +
					"hc.main, " +
					"hc.problem_id, " +
					"hc.verification_status_id, " +
					"hc.updated_on, " +
					"mcp.plan, " +
					"hc.cie10_codes, " +
					"va.created_on, " +
					"p.middle_names, " +
					"p.other_last_names, " +
					"p.email, " +
					"p.name_self_determination, " +
					"p.gender_self_determination, " +
					"ecd.administrative_discharge_on " +
					"FROM {h-schema}v_attention va " +
					"LEFT JOIN {h-schema}attention_reads ar ON (ar.attention_id = va.id) " +
					"JOIN {h-schema}institution i ON (i.sisa_code = :refsetCode AND va.institution_id = i.id) " +
					"JOIN (SELECT pat.id as patient_id, pp.first_name, pp.last_name, pp.identification_number, pp.gender_id, pp.birth_date, " +
					"pp.middle_names, pp.other_last_names, pe.email, pe.name_self_determination, pe.gender_self_determination " +
					"FROM {h-schema}patient pat " +
					"JOIN {h-schema}person pp on pp.id = pat.person_id " +
					"LEFT JOIN {h-schema}person_extended pe on pe.person_id = pp.id) AS p ON (p.patient_id = va.patient_id) " +
					"JOIN (SELECT hp.id as doctor_id, dp.first_name, dp.last_name, hp.license_number, dp.identification_number " +
					"FROM {h-schema}healthcare_professional hp " +
					"JOIN {h-schema}person dp on hp.person_id = dp.id) AS d ON (d.doctor_id = va.doctor_id) " +
					"LEFT JOIN (SELECT cs.id, cs.name, cs.sctid_code " +
					"FROM {h-schema}clinical_specialty cs ) snm ON (va.clinical_speciality_id = snm.id) " +
					"LEFT JOIN {h-schema}patient_discharge pd ON (va.scope_id = 0 AND pd.internment_episode_id = va.encounter_id) " +
					" %s " +
					"LEFT JOIN {h-schema}medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
					"LEFT JOIN {h-schema}medical_coverage_plan mcp ON (mc.id = mcp.medical_coverage_id) " +
					"LEFT JOIN {h-schema}document_health_condition dhc on dhc.document_id = va.id " +
					"LEFT JOIN {h-schema}health_condition hc on hc.id = dhc.health_condition_id " +
					"LEFT JOIN {h-schema}snomed s3 on s3.id = hc.snomed_id " +
					"LEFT JOIN {h-schema}emergency_care_discharge ecd ON (va.scope_id = 4 AND ecd.emergency_care_episode_id = va.encounter_id) " +
					"WHERE %s " +
					"ORDER BY encounter_id DESC";

	private static final String SQL_STRING_ODONTOLOGY =
			"SELECT va.id as attention_id, va.performed_date as attention_date, " +
					"snm.name , snm.sctid_code, " +
					"p.first_name as person_name, p.last_name as person_last_name, p.identification_number as person_identification_number, p.gender_id as person_gender_id, p.birth_date as person_birth_date, " +
					"pmc.affiliate_number, " +
					"va.scope_id, " +
					"pd.administrative_discharge_date, " +
					"d.doctor_id, d.first_name as doctor_name, d.last_name as doctor_last_name, d.license_number, d.identification_number as doctor_identification_number, " +
					"mc.cuit, " +
					"va.encounter_id AS encounter_id, " +
					"s3.sctid, " +
					"s3.pt, " +
					"hc.main, " +
					"hc.problem_id, " +
					"hc.verification_status_id, " +
					"hc.updated_on, " +
					"mcp.plan, " +
					"hc.cie10_codes, " +
					"va.created_on, " +
					"p.middle_names, " +
					"p.other_last_names, " +
					"p.email, " +
					"p.name_self_determination, " +
					"p.gender_self_determination, " +
					"ecd.administrative_discharge_on " +
					"FROM {h-schema}v_attention va " +
					"LEFT JOIN {h-schema}attention_reads ar ON (ar.attention_id = va.id) " +
					"JOIN {h-schema}institution i ON (i.sisa_code = :refsetCode AND va.institution_id = i.id) " +
					"JOIN (SELECT pat.id as patient_id, pp.first_name, pp.last_name, pp.identification_number, pp.gender_id, pp.birth_date, " +
					"pp.middle_names, pp.other_last_names, pe.email, pe.name_self_determination, pe.gender_self_determination " +
					"FROM {h-schema} patient pat " +
					"JOIN {h-schema}person pp on pp.id = pat.person_id " +
					"LEFT JOIN {h-schema}person_extended pe on pe.person_id = pp.id) AS p ON (p.patient_id = va.patient_id) " +
					"JOIN (SELECT hp.id as doctor_id, dp.first_name, dp.last_name, hp.license_number, dp.identification_number " +
					"FROM {h-schema}healthcare_professional hp " +
					"JOIN {h-schema}person dp on hp.person_id = dp.id) AS d ON (d.doctor_id = va.doctor_id) " +
					"LEFT JOIN (SELECT cs.id, cs.name, cs.sctid_code " +
					"FROM {h-schema} clinical_specialty cs) snm ON (va.clinical_speciality_id = snm.id) " +
					"LEFT JOIN {h-schema} patient_discharge pd ON (va.scope_id = 0 AND pd.internment_episode_id = va.encounter_id) " +
					"LEFT JOIN {h-schema} document_odontology_diagnostic dod ON (dod.document_id = va.id) " +
					"LEFT JOIN {h-schema} odontology_diagnostic od ON od.id = (dod.odontology_diagnostic_id) " +
					" %s " +
					"LEFT JOIN {h-schema} medical_coverage mc ON (pmc.medical_coverage_id = mc.id) " +
					"LEFT JOIN {h-schema}medical_coverage_plan mcp ON (mc.id = mcp.medical_coverage_id) " +
					"LEFT JOIN {h-schema} document_health_condition dhc ON (dhc.document_id = va.id) " +
					"LEFT JOIN {h-schema} health_condition hc ON (hc.id = dhc.health_condition_id) " +
					"LEFT JOIN {h-schema} snomed s3 ON (s3.id = od.snomed_id) " +
					"LEFT JOIN {h-schema}emergency_care_discharge ecd ON (va.scope_id = 4 AND ecd.emergency_care_episode_id = va.encounter_id) " +
					"WHERE %s " +
					"ORDER BY encounter_id DESC";

	@Override
	public Optional<AttentionInfoBo> getActivityById(String refsetCode, Long activityId) {
		LOG.debug("getActivityById ActivityStorage -> refsetCode {}, activityId {}", refsetCode, activityId);

		Query query = entityManager.createNativeQuery(String.format(SQL_STRING, JOIN_ANY_EVENT, "va.id = :activityId "))
				.setParameter("refsetCode", refsetCode)
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

		String whereClause = "cast(va.updated_on as date) BETWEEN :fromDate AND :toDate AND " +
				"(ar.attention_id IS NULL OR NOT ar.processed OR ar.processed AND :reprocessing )" +
				"AND va.scope_id = ";
		String finalQuery = "("+String.format(SQL_STRING, JOIN_HOSPITALIZATION, whereClause + HOSPITALIZATION) +")"
				+ " UNION ALL " +
				"("+String.format(SQL_STRING, JOIN_OUTPATIENT, whereClause + OUTPATIENT_CONSULTATION) +")"
				+ " UNION ALL " +
				"("+String.format(SQL_STRING_ODONTOLOGY, JOIN_ODONTOLOGY, whereClause + ODONTOLOGY) +")"
				+ " UNION ALL " +
				"("+String.format(SQL_STRING, JOIN_EMERGENCY_CARE, whereClause + EMERGENCY_CARE) +")";


		Query query = entityManager.createNativeQuery(finalQuery)
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

		String whereClause = "cast(va.updated_on as date) BETWEEN :fromDate AND :toDate AND " +
				"p.identification_number = :identificationNumber AND " +
				"(ar.attention_id IS NULL OR NOT ar.processed OR ar.processed AND :reprocessing) " +
				"AND va.scope_id = ";

		String finalQuery = "("+String.format(SQL_STRING, JOIN_HOSPITALIZATION, whereClause + HOSPITALIZATION) +")"
				+ " UNION ALL " +
				"("+String.format(SQL_STRING, JOIN_OUTPATIENT, whereClause + OUTPATIENT_CONSULTATION) +")"
				+ " UNION ALL " +
				"("+String.format(SQL_STRING, JOIN_EMERGENCY_CARE, whereClause + EMERGENCY_CARE) +")";
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

		String whereClause = "cast(va.updated_on as date) BETWEEN :fromDate AND :toDate AND " +
				"mc.cuit = :coverageCuit AND " +
				"(ar.attention_id IS NULL OR NOT ar.processed OR ar.processed AND :reprocessing)" +
				"AND va.scope_id = ";

		String finalQuery = "("+String.format(SQL_STRING, JOIN_HOSPITALIZATION, whereClause + HOSPITALIZATION) +")"
				+ " UNION ALL " +
				"("+String.format(SQL_STRING, JOIN_OUTPATIENT, whereClause + OUTPATIENT_CONSULTATION) +")"
				+ " UNION ALL " +
				"("+String.format(SQL_STRING, JOIN_EMERGENCY_CARE, whereClause + EMERGENCY_CARE) +")";
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