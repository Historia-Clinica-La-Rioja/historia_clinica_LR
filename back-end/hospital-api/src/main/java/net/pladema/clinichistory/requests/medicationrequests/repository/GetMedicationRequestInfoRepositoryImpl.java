package net.pladema.clinichistory.requests.medicationrequests.repository;

import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class GetMedicationRequestInfoRepositoryImpl implements GetMedicationRequestInfoRepository {

    private static final Logger LOG = LoggerFactory.getLogger(GetMedicationRequestInfoRepositoryImpl.class);

    private final EntityManager entityManager;

    public GetMedicationRequestInfoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> execute(Integer medicationRequestId) {
        LOG.debug("Input parameters -> medicationRequestId {}", medicationRequestId);

        String sqlString = "" +
                "SELECT mr.id AS mr_id, mr.doctor_id AS doctor_id, mr.request_date AS request_date, mr.medical_coverage_id AS medical_coverage_id " +
                ", ms.id AS ms_id, s.id AS m_s_id, s.sctid AS m_s_sctid, s.pt AS m_s_pt, n.description AS note, mss.id AS statusId, mss.description AS status " +
                ", h.id AS hid, h.s_id AS h_s_id, h.sctid_id AS h_sctid, h.pt AS h_pt, h.cie10_codes AS cie10_codes " +
                "FROM medication_request mr " +
                "JOIN document d ON (mr.id = d.source_id AND d.source_type_id = "+ SourceType.RECIPE + ") " +
                "JOIN document_medicamention_statement dms ON (d.id = dms.document_id) " +
                "JOIN medication_statement ms ON (dms.medication_statement_id = ms.id)" +
                "JOIN snomed s ON (ms.snomed_id = s.id) " +
                "JOIN medication_statement_status mss ON (mss.id = ms.status_id) " +
                "LEFT JOIN note n ON (ms.note_id = n.id) " +
                "JOIN ( SELECT h1.id, s1.id as s_id, s1.sctid as sctid_id, s1.pt, h1.cie10_codes " +
                "            FROM health_condition h1 " +
                "            JOIN snomed s1 ON (h1.snomed_id = s1.id) " +
                "          ) AS h ON (h.id = ms.health_condition_id) " +
                "WHERE mr.id = :medicationRequestId ";
        Query query = entityManager.createNativeQuery(sqlString);

        query.setParameter("medicationRequestId", medicationRequestId);

        List<Object[]> result = query.getResultList();
        LOG.trace("execute result query -> {}", result);
        return result;
    }
}
