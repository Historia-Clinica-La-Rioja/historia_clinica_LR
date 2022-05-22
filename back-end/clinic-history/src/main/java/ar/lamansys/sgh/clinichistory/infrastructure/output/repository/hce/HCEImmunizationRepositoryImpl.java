package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEImmunizationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.InmunizationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HCEImmunizationRepositoryImpl implements HCEImmunizationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(HCEImmunizationRepositoryImpl.class);

    private final EntityManager entityManager;

    public HCEImmunizationRepositoryImpl(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<HCEImmunizationVo> getImmunization(Integer patientId) {
        LOG.debug("Input parameters patientId {}", patientId);
        String sqlString = "WITH t AS (" +
                "   SELECT inm.id, inm.snomed_id, inm.status_id, inm.administration_date, inm.expiration_date,  " +
                "   inm.condition_id, inm.scheme_id, inm.dose, inm.dose_order, inm.institution_id, inm.lot_number, " +
                "   inm.note_id, inm.institution_info, inm.doctor_info, inm.updated_on, inm.created_by, inm.billable, " +
                "   row_number() over (partition by inm.snomed_id, inm.condition_id, inm.scheme_id, inm.dose, inm.dose_order, inm.administration_date order by inm.updated_on desc) as rw  " +
                "   FROM {h-schema}document d " +
                "   JOIN {h-schema}document_inmunization di on d.id = di.document_id " +
                "   JOIN {h-schema}inmunization inm on di.inmunization_id = inm.id " +
                "   WHERE d.status_id = :docStatusId " +
                "   AND d.type_id IN :documentType " +
                "   AND inm.patient_id = :patientId " +
                "   " +
                ") " +
                "SELECT t.id as id, s.sctid as sctid, s.pt, status_id, administration_date, expiration_date, " +
                "t.institution_id, t.condition_id, t.scheme_id, t.dose, t.dose_order,  t.lot_number, n.description, t.created_by," +
                "t.institution_info, t.doctor_info, t.billable " +
                "FROM t " +
                "JOIN {h-schema}snomed s ON snomed_id = s.id " +
                "LEFT JOIN note n ON (n.id = t.note_id)" +
                "WHERE rw = 1 " +
                "AND status_id <> :immunizationStatusId " +
                "ORDER BY t.updated_on DESC";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("docStatusId", DocumentStatus.FINAL)
                .setParameter("immunizationStatusId", InmunizationStatus.ERROR)
                .setParameter("patientId", patientId)
                .setParameter("documentType", List.of(DocumentType.OUTPATIENT, DocumentType.IMMUNIZATION))
                .getResultList();

        List<HCEImmunizationVo> result = new ArrayList<>();

        queryResult.forEach(h ->
                result.add(
                        new HCEImmunizationVo(
                                (Integer)h[0],
                                new Snomed((String)h[1], (String)h[2], null, null),
                                (String)h[3],
                                h[4] != null ? ((Date)h[4]).toLocalDate() : null,
                                h[5] != null ? ((Date)h[5]).toLocalDate() : null,
                                patientId,
                                (Integer) h[6],
                                (Short) h[7],
                                (Short) h[8],
                                (String) h[9],
                                (Short) h[10],
                                (String) h[11],
                                (String) h[12],
                                (Integer) h[13],
                                (String) h[14],
                                (String) h[15],
                                (Boolean) h[16]
                        )
                )
        );
        return result;
    }
}
