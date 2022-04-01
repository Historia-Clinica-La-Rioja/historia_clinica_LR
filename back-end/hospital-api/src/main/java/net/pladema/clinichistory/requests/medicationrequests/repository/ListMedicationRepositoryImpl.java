package net.pladema.clinichistory.requests.medicationrequests.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import net.pladema.clinichistory.requests.medicationrequests.repository.domain.MedicationFilterVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ListMedicationRepositoryImpl implements ListMedicationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ListMedicationRepositoryImpl.class);

    private final EntityManager entityManager;

    public ListMedicationRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Object[]> execute(MedicationFilterVo filter) {
        LOG.debug("Input parameters -> filter {}", filter);

        String sqlString = "with temporal as (" +
                "SELECT DISTINCT " +
                "ms.id, ms.snomed_id, ms.status_id, ms.health_condition_id, ms.note_id, ms.dosage_id, ms.created_on, d.source_id, d.source_type_id, d.created_by, ms.updated_on, " +
                "row_number() OVER (PARTITION by ms.snomed_id, ms.health_condition_id ORDER BY ms.updated_on desc) AS rw " +
                "FROM document d " +
                "JOIN document_medicamention_statement dms ON d.id = dms.document_id " +
                "JOIN medication_statement ms ON dms.medication_statement_id = ms.id " +
                "WHERE ms.patient_id = :patientId  " +
                "AND d.type_id IN :documentType "+
                "AND d.status_id = :documentStatusId " +
                ") " +
                "SELECT t.id AS id, s.id AS m_s_id, s.sctid AS m_sctid, s.pt AS m_pt " +
                ", mss.id AS statusId, mss.description AS status " +
                ", h.id AS hid, h.s_id AS h_s_id, h.sctid_id AS h_sctid, h.pt AS h_pt, n.description AS note " +
                ", d.id AS d_id, d.duration AS duration, d.frequency, d.period_unit, d.chronic, d.start_date, d.end_date " +
                ", d.suspended_start_date, d.suspended_end_date " +
                ", mr.id AS mr_id, CASE WHEN mr.has_recipe IS NULL THEN false ELSE mr.has_recipe END, t.created_by AS user_id " +
                ", t.created_on AS m_created_on " +
                "FROM temporal t " +
                "JOIN {h-schema}snomed s ON (t.snomed_id = s.id) " +
                "LEFT JOIN {h-schema}medication_request mr ON (mr.id = t.source_id AND t.source_type_id = "+ SourceType.RECIPE + ") " +
                "LEFT JOIN {h-schema}medication_statement_status mss ON (mss.id = t.status_id)" +
                "LEFT JOIN {h-schema}note n ON (t.note_id = n.id) " +
                "LEFT JOIN {h-schema}dosage d ON (t.dosage_id = d.id) " +
                "LEFT JOIN ( SELECT h1.id, s1.id as s_id, s1.sctid as sctid_id, s1.pt " +
                "            FROM {h-schema}health_condition h1 " +
                "            JOIN {h-schema}snomed s1 ON (h1.snomed_id = s1.id) " +
                "          ) AS h ON (h.id = t.health_condition_id) " +
                "WHERE rw = 1 " +
                (filter.getMedicationStatement() != null ? "AND UPPER(s.pt) LIKE :medication " : "") +
                (filter.getHealthCondition() != null ? "AND UPPER(h.pt) LIKE :healthCondition " : "") +
                "ORDER BY t.updated_on";
        Query query = entityManager.createNativeQuery(sqlString);

        query.setParameter("documentStatusId", DocumentStatus.FINAL)
             .setParameter("patientId", filter.getPatientId())
             .setParameter("documentType", List.of(DocumentType.RECIPE, DocumentType.OUTPATIENT, DocumentType.EPICRISIS, DocumentType.COUNTER_REFERENCE));

        if (filter.getMedicationStatement() != null)
            query.setParameter("medication", "%"+filter.getMedicationStatement().toUpperCase()+"%");

        if (filter.getHealthCondition() != null)
            query.setParameter("healthCondition", "%"+filter.getHealthCondition().toUpperCase()+"%");

        List<Object[]> result = query.getResultList();
        return result;
    }
}
