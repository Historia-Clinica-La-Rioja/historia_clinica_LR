package net.pladema.clinichistory.requests.medicationrequests.repository;

import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import net.pladema.clinichistory.requests.medicationrequests.repository.domain.MedicationFilterVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
                "ms.id, ms.sctid_code, ms.status_id, ms.health_condition_id, ms.note_id, ms.dosage_id, d.source_id, d.source_type_id, ms.updated_on, " +
                "row_number() OVER (PARTITION by ms.sctid_code, ms.health_condition_id ORDER BY ms.updated_on desc) AS rw " +
                "FROM document d " +
                "JOIN document_medicamention_statement dms ON d.id = dms.document_id " +
                "JOIN medication_statement ms ON dms.medication_statement_id = ms.id " +
                "WHERE ms.patient_id = :patientId  " +
                "AND d.type_id IN :documentType "+
                "AND d.status_id = :documentStatusId " +
                ") " +
                "SELECT t.id AS id, s.id AS m_sctid, s.pt AS m_pt " +
                ", mss.id AS statusId, mss.description AS status " +
                ", h.id AS hid, h.sctid_id AS h_sctid, h.pt AS h_pt, n.description AS note " +
                ", d.id AS d_id, d.duration AS duration, d.frequency, d.period_unit, d.chronic, d.start_date " +
                ", d.suspended_start_date, d.suspended_end_date " +
                ", mr.id AS mr_id, ISNULL(mr.has_recipe, false) " +
                "FROM temporal t " +
                "JOIN snomed s ON (t.sctid_code = s.id) " +
                "LEFT JOIN medication_request mr ON (mr.id = t.source_id AND t.source_type_id = "+ SourceType.RECIPE + ") " +
                "LEFT JOIN medication_statement_status mss ON (mss.id = t.status_id)" +
                "LEFT JOIN note n ON (t.note_id = n.id) " +
                "LEFT JOIN dosage d ON (t.dosage_id = d.id) " +
                "LEFT JOIN ( SELECT h1.id, s1.id as sctid_id, s1.pt " +
                "            FROM health_condition h1 " +
                "            JOIN snomed s1 ON (h1.sctid_code = s1.id) " +
                "          ) AS h ON (h.id = t.health_condition_id) " +
                "WHERE rw = 1 " +
                "ORDER BY t.updated_on";

        List<Object[]> result = entityManager.createNativeQuery(sqlString)
                .setParameter("documentStatusId", DocumentStatus.FINAL)
                .setParameter("patientId", filter.getPatientId())
                .setParameter("documentType", List.of(DocumentType.RECIPE, DocumentType.OUTPATIENT, DocumentType.EPICRISIS))
                .getResultList();
        return result;
    }
}
