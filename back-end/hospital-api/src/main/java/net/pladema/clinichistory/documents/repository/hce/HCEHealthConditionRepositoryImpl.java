package net.pladema.clinichistory.documents.repository.hce;

import net.pladema.clinichistory.documents.repository.hce.domain.HCEHealthConditionVo;
import net.pladema.clinichistory.documents.repository.hce.domain.HCEHospitalizationVo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.*;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class HCEHealthConditionRepositoryImpl implements HCEHealthConditionRepository {


    private static final Logger LOG = LoggerFactory.getLogger(HCEHealthConditionRepositoryImpl.class);
    public static final String INPUT_PARAMETERS_PATIENT_ID = "Input parameters patientId {}";

    private final EntityManager entityManager;

    public HCEHealthConditionRepositoryImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<HCEHealthConditionVo> getPersonalHistories(Integer patientId) {
        LOG.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);
        String sqlString = "WITH t AS (" +
                "   SELECT hc.id, snomed_id, hc.status_id, hc.main, verification_status_id, problem_id, start_date, inactivation_date, hc.note_id, hc.updated_on, hc.patient_id, " +
                "   row_number() over (partition by snomed_id order by hc.updated_on desc) as rw  " +
                "   FROM document d " +
                "   JOIN document_health_condition dhc on d.id = dhc.document_id " +
                "   JOIN health_condition hc on dhc.health_condition_id = hc.id " +
                "   WHERE d.status_id = :docStatusId " +
                "   AND d.type_id = :documentType "+
                "   AND hc.patient_id = :patientId " +
                "   AND hc.problem_id IN (:validProblemTypes) " +
                ") " +
                "SELECT t.id as id, s.sctid as sctid, s.pt, status_id, t.main, verification_status_id, problem_id," +
                "start_date, inactivation_date, patient_id " +
                "FROM t " +
                "JOIN snomed s ON snomed_id = s.id " +
                "WHERE rw = 1 " +
                "AND NOT verification_status_id = :verificationId  " +
                "ORDER BY t.updated_on DESC";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("docStatusId", DocumentStatus.FINAL)
                .setParameter("verificationId", ConditionVerificationStatus.ERROR)
                .setParameter("patientId", patientId)
                .setParameter("validProblemTypes", Arrays.asList(ProblemType.PROBLEM, ProblemType.CHRONIC))
                .setParameter("documentType", DocumentType.OUTPATIENT)
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
                        h[7] != null ? ((Date)h[7]).toLocalDate() : null,
                        h[8] != null ? ((Date)h[8]).toLocalDate() : null,
                        (Integer) h[9]))
        );
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<HCEHealthConditionVo> getFamilyHistories(Integer patientId) {
        LOG.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);
        String sqlString = "WITH t AS (" +
                "   SELECT hc.id, snomed_id, hc.status_id, hc.main, verification_status_id, problem_id, start_date, hc.note_id, hc.updated_on, hc.patient_id, " +
                "   row_number() over (partition by snomed_id order by hc.updated_on desc) as rw  " +
                "   FROM document d " +
                "   JOIN document_health_condition dhc on d.id = dhc.document_id " +
                "   JOIN health_condition hc on dhc.health_condition_id = hc.id " +
                "   WHERE d.status_id = :docStatusId " +
                "   AND d.type_id NOT IN :invalidDocumentTypes "+
                "   AND hc.patient_id = :patientId " +
                "   AND hc.problem_id = :problemType " +
                "   " +
                ") " +
                "SELECT t.id as id, s.sctid as sctid, s.pt, status_id, t.main, verification_status_id, problem_id," +
                "start_date, patient_id " +
                "FROM t " +
                "JOIN snomed s ON snomed_id = s.id " +
                "WHERE rw = 1 " +
                "AND NOT verification_status_id = :verificationId  " +
                "AND status_id = :hcStatusId " +
                "ORDER BY t.updated_on DESC";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("docStatusId", DocumentStatus.FINAL)
                .setParameter("verificationId", ConditionVerificationStatus.ERROR)
                .setParameter("hcStatusId", ConditionClinicalStatus.ACTIVE)
                .setParameter("patientId", patientId)
                .setParameter("problemType", ProblemType.HISTORY)
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
                            h[7] != null ? ((Date)h[7]).toLocalDate() : null,
                            null,
                            (Integer) h[8]))
        );
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<HCEHospitalizationVo> getHospitalizationHistory(Integer patientId) {
        LOG.debug(INPUT_PARAMETERS_PATIENT_ID, patientId);
        String sqlString = "WITH t AS (" +
                "   SELECT hc.id, snomed_id, hc.status_id, d.source_id, hc.main, verification_status_id, ie.entry_date, pd.administrative_discharge_date, hc.updated_on, hc.patient_id, " +
                "   row_number() over (partition by snomed_id, source_id order by hc.updated_on desc) as rw  " +
                "   FROM document d " +
                "   JOIN document_health_condition dhc ON d.id = dhc.document_id " +
                "   JOIN health_condition hc ON dhc.health_condition_id = hc.id " +
                "   JOIN internment_episode ie ON ie.id = d.source_id " +
                "   LEFT JOIN patient_discharge pd ON pd.internment_episode_id = ie.id " +
                "   WHERE d.status_id = :docStatusId " +
                "   AND d.source_type_id = :sourceType " +
                "   AND d.type_id = :documentType "+
                "   AND hc.patient_id = :patientId " +
                "   AND hc.problem_id = :problemType " +
                ") " +
                "SELECT t.id as id, s.sctid as sctid, s.pt, status_id, t.main, source_id," +
                "entry_date, administrative_discharge_date, patient_id " +
                "FROM t " +
                "JOIN snomed s ON snomed_id = s.id " +
                "WHERE rw = 1 " +
                "AND NOT verification_status_id = :verificationId  " +
                "ORDER BY t.updated_on DESC";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("docStatusId", DocumentStatus.FINAL)
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
                                h[6] != null ? ((Date)h[6]).toLocalDate() : null,
                                h[7] != null ? ((Date)h[7]).toLocalDate() : null,
                                (Integer) h[8]))

        );
        return result;
    }
}
