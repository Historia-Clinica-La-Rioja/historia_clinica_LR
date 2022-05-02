package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEToothRecordBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEToothRecordStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class HCEToothRecordStorageImpl implements HCEToothRecordStorage {

    private static final Logger LOG = LoggerFactory.getLogger(HCEToothRecordStorageImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final EntityManager entityManager;

    public HCEToothRecordStorageImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public List<HCEToothRecordBo> getToothRecords(Integer patientId, String toothSctid) {
        LOG.debug("Input parameters -> patientId {}, toothSctid {}", patientId, toothSctid);
        Query query = entityManager.createNativeQuery(
                "     (SELECT snomed_diagnostic.sctid as snomed_diagnostic_sctid , snomed_diagnostic.pt , " +
                        "           snomed_surface.sctid as snomed_surface_sctid , od.performed_date , od.updated_on " +
                        "FROM {h-schema}tooth t  " +
                        "JOIN {h-schema}body_part bp ON (t.sctid = bp.sctid) " +
                        "JOIN {h-schema}snomed snomed_tooth ON (bp.sctid = snomed_tooth.sctid AND bp.pt = snomed_tooth.pt) " +
                        "JOIN {h-schema}odontology_diagnostic od ON (snomed_tooth.id = od.tooth_id) " +
                        "JOIN {h-schema}snomed snomed_diagnostic ON (od.snomed_id = snomed_diagnostic.id) " +
                        "LEFT JOIN {h-schema}snomed snomed_surface ON (od.surface_id = snomed_surface.id) " +
                        "WHERE t.sctid = :toothSctid " +
                        "   AND od.patient_id = :patientId ) " +
                        "UNION " +
                        "(SELECT snomed_procedure.sctid as snomed_procedure_sctid , snomed_procedure.pt , " +
                        "           snomed_surface.sctid as snomed_surface_sctid , op.performed_date , op.updated_on " +
                        "FROM {h-schema}tooth t  " +
                        "JOIN {h-schema}body_part bp ON (t.sctid = bp.sctid) " +
                        "JOIN {h-schema}snomed snomed_tooth ON (bp.sctid = snomed_tooth.sctid AND bp.pt = snomed_tooth.pt) " +
                        "JOIN {h-schema}odontology_procedure op ON (snomed_tooth.id = op.tooth_id) " +
                        "JOIN {h-schema}snomed snomed_procedure ON (op.snomed_id = snomed_procedure.id) " +
                        "LEFT JOIN {h-schema}snomed snomed_surface ON (op.surface_id = snomed_surface.id) " +
                        "WHERE t.sctid = :toothSctid " +
                        "   AND op.patient_id = :patientId ) " +
                        "ORDER BY updated_on DESC " );
        query.setParameter("toothSctid", toothSctid);
        query.setParameter("patientId", patientId);
        List<Object[]> queryResult = query.getResultList();

        List<HCEToothRecordBo> result = new ArrayList<>();
        for (Object[] o : queryResult) {
            result.add(new HCEToothRecordBo((String) o[0],
                    (String) o[1],
                    (String) o[2],
                    ((Date) o[3]).toLocalDate()));
        }
        LOG.debug("Output size -> {}", result.size());
        LOG.trace(OUTPUT, result);
        return result;
    }

}
