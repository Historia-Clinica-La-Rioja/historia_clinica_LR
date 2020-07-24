package net.pladema.clinichistory.generalstate.repository;

import net.pladema.clinichistory.generalstate.repository.domain.HCEHealthConditionVo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.*;
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

    private final EntityManager entityManager;

    public HCEHealthConditionRepositoryImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<HCEHealthConditionVo> getPersonalHistories(Integer patientId) {
        LOG.debug("Input parameters patientId {}", patientId);
        String sqlString = "WITH t AS (" +
                "   SELECT hc.id, sctid_code, hc.status_id, hc.main, verification_status_id, problem_id, start_date, hc.note_id, hc.updated_on, hc.patient_id, " +
                "   row_number() over (partition by sctid_code, problem_id order by hc.updated_on desc) as rw  " +
                "   FROM document d " +
                "   JOIN document_health_condition dhc on d.id = dhc.document_id " +
                "   JOIN health_condition hc on dhc.health_condition_id = hc.id " +
                "   WHERE d.status_id = :docStatusId " +
                "   AND d.type_id = :documentType "+
                "   AND hc.patient_id = :patientId " +
                "   AND hc.problem_id IN (:validProblemTypes) " +
                ") " +
                "SELECT t.id as id, s.id as sctid, s.pt, status_id, t.main, verification_status_id, problem_id," +
                "start_date, patient_id " +
                "FROM t " +
                "JOIN snomed s ON sctid_code = s.id " +
                "WHERE rw = 1 " +
                "AND NOT verification_status_id = :verificationId  " +
                "ORDER BY t.updated_on DESC";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("docStatusId", DocumentStatus.FINAL)
                .setParameter("verificationId", ConditionVerificationStatus.ERROR)
                .setParameter("patientId", patientId)
                .setParameter("validProblemTypes", Arrays.asList(ProblemType.PROBLEMA, ProblemType.CHRONIC))
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
                                (Integer)h[8]
                        )
                )
        );
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<HCEHealthConditionVo> getFamilyHistories(Integer patientId) {
        LOG.debug("Input parameters patientId {}", patientId);
        String sqlString = "WITH t AS (" +
                "   SELECT hc.id, sctid_code, hc.status_id, hc.main, verification_status_id, problem_id, start_date, hc.note_id, hc.updated_on, hc.patient_id, " +
                "   row_number() over (partition by sctid_code, problem_id order by hc.updated_on desc) as rw  " +
                "   FROM document d " +
                "   JOIN document_health_condition dhc on d.id = dhc.document_id " +
                "   JOIN health_condition hc on dhc.health_condition_id = hc.id " +
                "   WHERE d.status_id = :docStatusId " +
                "   AND d.type_id NOT IN :invalidDocumentTypes "+
                "   AND hc.patient_id = :patientId " +
                "   AND hc.problem_id = :problemType " +
                "   " +
                ") " +
                "SELECT t.id as id, s.id as sctid, s.pt, status_id, t.main, verification_status_id, problem_id," +
                "start_date, patient_id " +
                "FROM t " +
                "JOIN snomed s ON sctid_code = s.id " +
                "WHERE rw = 1 " +
                "AND NOT verification_status_id = :verificationId  " +
                "AND status_id = :hcStatusId " +
                "ORDER BY t.updated_on DESC";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("docStatusId", DocumentStatus.FINAL)
                .setParameter("verificationId", ConditionVerificationStatus.ERROR)
                .setParameter("hcStatusId", ConditionClinicalStatus.ACTIVE)
                .setParameter("patientId", patientId)
                .setParameter("problemType", ProblemType.ANTECEDENTE)
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
                                (Integer)h[8]
                        )
                )
        );
        return result;
    }
}
