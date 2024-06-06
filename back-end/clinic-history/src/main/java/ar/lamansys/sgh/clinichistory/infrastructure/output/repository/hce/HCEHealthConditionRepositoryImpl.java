package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEHealthConditionVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEHospitalizationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEPersonalHistoryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class HCEHealthConditionRepositoryImpl implements HCEHealthConditionRepository {
	
    public static final String INPUT_PARAMETERS_PATIENT_ID = "Input parameters patientId {}";

    private final EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<HCEHealthConditionVo> getSummaryProblems(Integer patientId) {
        log.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);
        String sqlString = "WITH t AS (" +
                "   SELECT hc.id, snomed_id, hc.status_id, hc.main, verification_status_id, problem_id, severity, start_date, inactivation_date, hc.note_id, hc.updated_on, hc.patient_id, " +
                "   row_number() over (partition by snomed_id order by hc.updated_on desc) as rw  " +
                "   FROM {h-schema}document d " +
                "   JOIN {h-schema}document_health_condition dhc on d.id = dhc.document_id " +
                "   JOIN {h-schema}health_condition hc on dhc.health_condition_id = hc.id " +
                "   WHERE d.status_id IN (:docStatusId) " +
				"   AND NOT hc.verification_status_id = :verificationId " +
                "   AND d.type_id in (:documentTypes) "+
                "   AND hc.patient_id = :patientId " +
                "   AND hc.problem_id IN (:validProblemTypes) " +
                ") " +
                "SELECT t.id as id, s.sctid as sctid, s.pt, status_id, t.main, verification_status_id, problem_id," +
                "severity, start_date, inactivation_date, patient_id " +
                "FROM t " +
                "JOIN {h-schema}snomed s ON snomed_id = s.id " +
                "WHERE rw = 1 " +
                "ORDER BY t.updated_on DESC";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("docStatusId", List.of(DocumentStatus.FINAL, DocumentStatus.DRAFT))
				.setParameter("verificationId", ConditionVerificationStatus.ERROR)
                .setParameter("patientId", patientId)
                .setParameter("validProblemTypes", Arrays.asList(ProblemType.PROBLEM, ProblemType.CHRONIC))
                .setParameter("documentTypes", List.of(DocumentType.OUTPATIENT, DocumentType.ODONTOLOGY))
                .getResultList();

        List<HCEHealthConditionVo> result = new ArrayList<>();

        queryResult.forEach(h ->
                result.add(
                    new HCEHealthConditionVo(
                        (Integer)h[0],
                        new Snomed((String)h[1], (String)h[2], null, null),
                        (String)h[3],
                        (boolean)h[4],
                        (String)h[5],
                        (String)h[6],
                        (String)h[7],
                        h[8] != null ? ((Date)h[8]).toLocalDate() : null,
                        h[9] != null ? ((Date)h[9]).toLocalDate() : null,
                        (Integer) h[10],
							null,
							null,
							null,
							null))
        );
        return result;
    }

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<HCEHealthConditionVo> getSummaryProblemsByUser(Integer patientId, Integer userId) {
		log.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);
		String sqlString = "WITH t AS (" +
				"   SELECT hc.id, snomed_id, hc.status_id, hc.main, verification_status_id, problem_id, severity, hc.start_date, inactivation_date, hc.note_id, hc.updated_on, hc.patient_id, " +
				"   row_number() over (partition by snomed_id order by hc.updated_on desc) as rw  " +
				"   FROM {h-schema}document d " +
				"   JOIN {h-schema}document_health_condition dhc on d.id = dhc.document_id " +
				"   JOIN {h-schema}health_condition hc on dhc.health_condition_id = hc.id " +
				"	JOIN {h-schema}outpatient_consultation oc on d.id = oc.document_id " +
				"	JOIN {h-schema}healthcare_professional hcp on oc.doctor_id = hcp.id " +
				"	JOIN {h-schema}person p on hcp.person_id = p.id " +
				"	JOIN {h-schema}user_person up on up.person_id = p.id " +
				"   WHERE d.status_id IN (:docStatusId) " +
				"   AND NOT hc.verification_status_id = :verificationId " +
				"   AND d.type_id in (:documentTypes) "+
				"   AND hc.patient_id = :patientId " +
				"   AND hc.problem_id IN (:validProblemTypes) " +
				"	AND up.user_id = (:userId) "+
				") " +
				"SELECT t.id as id, s.sctid as sctid, s.pt, status_id, t.main, verification_status_id, problem_id," +
				"severity, start_date, inactivation_date, patient_id " +
				"FROM t " +
				"JOIN {h-schema}snomed s ON snomed_id = s.id " +
				"WHERE rw = 1 " +
				"ORDER BY t.updated_on DESC";

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
				.setParameter("docStatusId", List.of(DocumentStatus.FINAL, DocumentStatus.DRAFT))
				.setParameter("verificationId", ConditionVerificationStatus.ERROR)
				.setParameter("patientId", patientId)
				.setParameter("validProblemTypes", Arrays.asList(ProblemType.PROBLEM, ProblemType.CHRONIC))
				.setParameter("documentTypes", List.of(DocumentType.OUTPATIENT, DocumentType.ODONTOLOGY))
				.setParameter("userId", userId)
				.getResultList();

		List<HCEHealthConditionVo> result = new ArrayList<>();

		queryResult.forEach(h ->
				result.add(
						new HCEHealthConditionVo(
								(Integer)h[0],
								new Snomed((String)h[1], (String)h[2], null, null),
								(String)h[3],
								(boolean)h[4],
								(String)h[5],
								(String)h[6],
								(String)h[7],
								h[8] != null ? ((Date)h[8]).toLocalDate() : null,
								h[9] != null ? ((Date)h[9]).toLocalDate() : null,
								(Integer) h[10],
								null,
								null,
								null,
								null))
		);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<HCEHealthConditionVo> getProblemsMarkedAsError(Integer patientId) {
		log.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);
		String sqlString =
				"   SELECT hc.id, s.sctid, s.pt, hc.status_id, hc.main, hc.verification_status_id, hc.problem_id," +
				"          hc.severity, hc.start_date, hc.inactivation_date, hc.patient_id " +
				"   FROM {h-schema}document d " +
				"   JOIN {h-schema}document_health_condition dhc on d.id = dhc.document_id " +
				"   JOIN {h-schema}health_condition hc on dhc.health_condition_id = hc.id " +
				"   JOIN {h-schema}snomed s ON hc.snomed_id = s.id " +
				"   WHERE d.status_id IN (:docStatusId) " +
				"   AND hc.verification_status_id = :verificationId " +
				"   AND d.type_id in (:documentTypes) "+
				"   AND hc.patient_id = :patientId " +
				"   AND hc.problem_id IN (:validProblemTypes) " +
				"   ORDER BY hc.updated_on DESC";

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
				.setParameter("docStatusId", List.of(DocumentStatus.FINAL, DocumentStatus.DRAFT))
				.setParameter("verificationId", ConditionVerificationStatus.ERROR)
				.setParameter("patientId", patientId)
				.setParameter("validProblemTypes", Arrays.asList(ProblemType.PROBLEM, ProblemType.CHRONIC))
				.setParameter("documentTypes", List.of(DocumentType.OUTPATIENT, DocumentType.ODONTOLOGY))
				.getResultList();

		List<HCEHealthConditionVo> result = new ArrayList<>();

		queryResult.forEach(h ->
				result.add(
						new HCEHealthConditionVo(
								(Integer)h[0],
								new Snomed((String)h[1], (String)h[2], null, null),
								(String)h[3],
								(boolean)h[4],
								(String)h[5],
								(String)h[6],
								(String)h[7],
								h[8] != null ? ((Date)h[8]).toLocalDate() : null,
								h[9] != null ? ((Date)h[9]).toLocalDate() : null,
								(Integer) h[10],
								null,
								null,
								null,
								null))
		);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<HCEPersonalHistoryVo> getPersonalHistories(Integer patientId) {
		log.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);
		String sqlString = "WITH t AS (" +
				"   SELECT hc.id, snomed_id, hc.status_id, hc.main, verification_status_id, problem_id, severity, start_date, inactivation_date, hc.note_id, hc.updated_on, hc.patient_id, " +
				"   i.name as institution_name, up.person_id, d.created_on " +
				"   FROM {h-schema}document d " +
				"   JOIN {h-schema}document_health_condition dhc on d.id = dhc.document_id " +
				"   JOIN {h-schema}health_condition hc on dhc.health_condition_id = hc.id " +
				"   JOIN {h-schema}institution i ON i.id = d.institution_id " +
				"   JOIN {h-schema}user_person up ON d.created_by = up.user_id " +
				"   WHERE d.status_id IN (:docStatusId) " +
				"   AND hc.patient_id = :patientId " +
				"   AND hc.problem_id = :problemType " +
				"   " +
				") " +
				"SELECT t.id as id, s.sctid as sctid, s.pt, status_id, t.main, verification_status_id, problem_id, " +
				"severity, start_date, inactivation_date, patient_id, " +
				"institution_name, person_id as professional, created_on, n.description as observations, pht.description " +
				"FROM t " +
				"JOIN {h-schema}snomed s ON snomed_id = s.id " +
				"LEFT JOIN {h-schema}note n ON note_id = n.id " +
				"LEFT JOIN {h-schema}personal_history ph ON t.id = ph.health_condition_id " +
				"LEFT JOIN {h-schema}personal_history_type pht ON ph.type_id = pht.id " +
				"AND NOT verification_status_id = :verificationId  " +
				"AND status_id = :hcStatusId " +
				"ORDER BY t.updated_on DESC";

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
				.setParameter("docStatusId", List.of(DocumentStatus.FINAL, DocumentStatus.DRAFT))
				.setParameter("verificationId", ConditionVerificationStatus.ERROR)
				.setParameter("hcStatusId", ConditionClinicalStatus.ACTIVE)
				.setParameter("patientId", patientId)
				.setParameter("problemType", ProblemType.PERSONAL_HISTORY)
				.getResultList();

		List<HCEPersonalHistoryVo> result = new ArrayList<>();

		queryResult.forEach(h ->
				result.add(
						new HCEPersonalHistoryVo(
								(Integer)h[0],
								new Snomed((String)h[1], (String)h[2], null, null),
								(String)h[3],
								(boolean)h[4],
								(String)h[5],
								(String)h[6],
								(String)h[7],
								h[8] != null ? ((Date)h[8]).toLocalDate() : null,
								h[9] != null ? ((Date)h[9]).toLocalDate() : null,
								(Integer) h[10],
								(String) h[11],
								(Integer) h[12],
								((Timestamp) h[13]).toLocalDateTime(),
								(String) h[14],
								(String) h[15]))
		);
		return result;
	}

	@SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<HCEHealthConditionVo> getFamilyHistories(Integer patientId) {
        log.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);
        String sqlString = "WITH t AS (" +
                "   SELECT hc.id, snomed_id, hc.status_id, hc.main, verification_status_id, problem_id, severity, start_date, hc.note_id, hc.updated_on, hc.patient_id, " +
                "   row_number() over (partition by snomed_id order by hc.updated_on desc) as rw  " +
                "   FROM {h-schema}document d " +
                "   JOIN {h-schema}document_health_condition dhc on d.id = dhc.document_id " +
                "   JOIN {h-schema}health_condition hc on dhc.health_condition_id = hc.id " +
                "   WHERE d.status_id IN (:docStatusId) " +
                "   AND d.type_id NOT IN :invalidDocumentTypes "+
                "   AND hc.patient_id = :patientId " +
                "   AND hc.problem_id = :problemType " +
                "   " +
                ") " +
                "SELECT t.id as id, s.sctid as sctid, s.pt, status_id, t.main, verification_status_id, problem_id," +
                "severity, start_date, patient_id " +
                "FROM t " +
                "JOIN {h-schema}snomed s ON snomed_id = s.id " +
                "WHERE rw = 1 " +
                "AND NOT verification_status_id = :verificationId  " +
                "AND status_id = :hcStatusId " +
                "ORDER BY t.updated_on DESC";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("docStatusId", List.of(DocumentStatus.FINAL, DocumentStatus.DRAFT))
                .setParameter("verificationId", ConditionVerificationStatus.ERROR)
                .setParameter("hcStatusId", ConditionClinicalStatus.ACTIVE)
                .setParameter("patientId", patientId)
                .setParameter("problemType", ProblemType.FAMILY_HISTORY)
                .setParameter("invalidDocumentTypes", Arrays.asList(DocumentType.ANAMNESIS, DocumentType.EVALUATION_NOTE, DocumentType.EPICRISIS))
                .getResultList();

        List<HCEHealthConditionVo> result = new ArrayList<>();

        queryResult.forEach(h ->
                result.add(
                        new HCEHealthConditionVo(
                            (Integer)h[0],
                            new Snomed((String)h[1], (String)h[2], null, null),
                            (String)h[3],
                            (boolean)h[4],
                            (String)h[5],
                            (String)h[6],
                            (String)h[7],
                            h[8] != null ? ((Date)h[8]).toLocalDate() : null,
                            null,
                            (Integer) h[9],
							null,
								null,
								null,
								null))
        );
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<HCEHospitalizationVo> getHospitalizationHistory(Integer patientId) {
        log.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);
        String sqlString = "WITH t AS (" +
                "   SELECT hc.id, snomed_id, hc.status_id, d.source_id, hc.main, verification_status_id, ie.entry_date, pd.administrative_discharge_date, hc.updated_on, hc.patient_id, " +
                "   row_number() over (partition by snomed_id, source_id order by hc.updated_on desc) as rw  " +
                "   FROM {h-schema}document d " +
                "   JOIN {h-schema}document_health_condition dhc ON d.id = dhc.document_id " +
                "   JOIN {h-schema}health_condition hc ON dhc.health_condition_id = hc.id " +
                "   JOIN {h-schema}internment_episode ie ON ie.id = d.source_id " +
                "   LEFT JOIN {h-schema}patient_discharge pd ON pd.internment_episode_id = ie.id " +
                "   WHERE d.status_id IN (:docStatusId) " +
                "   AND d.source_type_id = :sourceType " +
                "   AND d.type_id = :documentType "+
                "   AND hc.patient_id = :patientId " +
                "   AND hc.problem_id = :problemType " +
                ") " +
                "SELECT t.id as id, s.sctid as sctid, s.pt, status_id, t.main, source_id," +
                "entry_date, administrative_discharge_date, patient_id " +
                "FROM t " +
                "JOIN {h-schema}snomed s ON snomed_id = s.id " +
                "WHERE rw = 1 " +
                "AND NOT verification_status_id = :verificationId  " +
                "ORDER BY t.updated_on DESC";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("docStatusId", List.of(DocumentStatus.FINAL, DocumentStatus.DRAFT))
                .setParameter("verificationId", ConditionVerificationStatus.ERROR)
                .setParameter("sourceType", SourceType.HOSPITALIZATION)
                .setParameter("patientId", patientId)
                .setParameter("problemType", ProblemType.DIAGNOSIS)
                .setParameter("documentType", DocumentType.EPICRISIS)
                .getResultList();

        List<HCEHospitalizationVo> result = new ArrayList<>();

        queryResult.forEach(h ->
                result.add(
                        new HCEHospitalizationVo(
                                (Integer)h[0],
                                new Snomed((String)h[1], (String)h[2], null, null),
                                (String)h[3],
                                (boolean)h[4],
                                (Integer)h[5],
                                h[6] != null ? ((Timestamp)h[6]).toLocalDateTime() : null,
                                h[7] != null ? ((Timestamp)h[7]).toLocalDateTime() : null,
                                (Integer) h[8]))

        );
        return result;
    }

	@Override
	@Transactional(readOnly = true)
	public List<HCEHospitalizationVo> getEmergencyCareHistory(Integer patientId) {
		log.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);
		String sqlString = "WITH t AS (" +
				"   SELECT hc.id, snomed_id, hc.status_id, d.source_id, hc.main, verification_status_id, ece.created_on, ecd.administrative_discharge_on, hc.updated_on, hc.patient_id, " +
				"   row_number() over (partition by snomed_id, source_id order by hc.updated_on desc) as rw  " +
				"   FROM {h-schema}document d " +
				"   JOIN {h-schema}document_health_condition dhc ON d.id = dhc.document_id " +
				"   JOIN {h-schema}health_condition hc ON dhc.health_condition_id = hc.id " +
				"   JOIN {h-schema}emergency_care_episode ece ON ece.id = d.source_id " +
				"   LEFT JOIN {h-schema}emergency_care_discharge ecd ON ecd.emergency_care_episode_id = ece.id " +
				"   WHERE d.status_id IN (:docStatusId) " +
				"   AND d.source_type_id = :sourceType " +
				"   AND d.type_id = :documentType "+
				"   AND hc.patient_id = :patientId " +
				"   AND hc.problem_id = :problemType " +
				"	AND ecd.medical_discharge_on IS NOT NULL" +
				") " +
				"SELECT t.id as id, s.sctid as sctid, s.pt, status_id, t.main, source_id," +
				"t.created_on, administrative_discharge_on, patient_id " +
				"FROM t " +
				"JOIN {h-schema}snomed s ON snomed_id = s.id " +
				"WHERE rw = 1 " +
				"AND NOT verification_status_id = :verificationId  " +
				"ORDER BY t.updated_on DESC";

		List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
				.setParameter("docStatusId", List.of(DocumentStatus.FINAL, DocumentStatus.DRAFT))
				.setParameter("verificationId", ConditionVerificationStatus.ERROR)
				.setParameter("sourceType", SourceType.EMERGENCY_CARE)
				.setParameter("patientId", patientId)
				.setParameter("problemType", ProblemType.DIAGNOSIS)
				.setParameter("documentType", DocumentType.EMERGENCY_CARE_EVOLUTION_NOTE)
				.getResultList();

		List<HCEHospitalizationVo> result = new ArrayList<>();

		queryResult.forEach(h ->
				result.add(
						new HCEHospitalizationVo(
								(Integer)h[0],
								new Snomed((String)h[1], (String)h[2], null, null),
								(String)h[3],
								(boolean)h[4],
								(Integer)h[5],
								h[6] != null ? ((Timestamp)h[6]).toLocalDateTime() : null,
								h[7] != null ? ((Timestamp)h[7]).toLocalDateTime() : null,
								(Integer) h[8]))

		);
		return result;
	}
}
