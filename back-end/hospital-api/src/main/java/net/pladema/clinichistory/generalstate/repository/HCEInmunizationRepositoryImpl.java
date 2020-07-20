package net.pladema.clinichistory.generalstate.repository;

import net.pladema.clinichistory.generalstate.repository.domain.HCEInmunizationVo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HCEInmunizationRepositoryImpl implements  HCEInmunizationRepository{

    private static final Logger LOG = LoggerFactory.getLogger(HCEHealthConditionRepositoryImpl.class);

    private final EntityManager entityManager;

    public HCEInmunizationRepositoryImpl(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<HCEInmunizationVo> getInmunization(Integer patientId) {
        LOG.debug("Input parameters patientId {}", patientId);
        String sqlString = "WITH t AS (" +
                "   SELECT inm.id, sctid_code, inm.status_id, administration_date, expiration_date, inm.updated_on, " +
                "   row_number() over (partition by sctid_code, administration_date order by inm.updated_on desc) as rw  " +
                "   FROM document d " +
                "   JOIN document_inmunization di on d.id = di.document_id " +
                "   JOIN inmunization inm on di.inmunization_id = inm.id " +
                "   WHERE d.status_id = :docStatusId " +
                "   AND d.type_id = :documentType "+
                "   AND inm.patient_id = :patientId " +
                "   " +
                ") " +
                "SELECT t.id as id, s.id as sctid, s.pt, status_id, administration_date, expiration_date " +
                "FROM t " +
                "JOIN snomed s ON sctid_code = s.id " +
                "WHERE rw = 1 " +
                "AND status_id <> :inmunizationStatusId " +
                "ORDER BY t.updated_on DESC";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("docStatusId", DocumentStatus.FINAL)
                .setParameter("inmunizationStatusId", InmunizationStatus.ERROR)
                .setParameter("patientId", patientId)
                .setParameter("documentType", DocumentType.OUTPATIENT)
                .getResultList();

        List<HCEInmunizationVo> result = new ArrayList<>();

        queryResult.forEach(h ->
                result.add(
                        new HCEInmunizationVo(
                                (Integer)h[0],
                                new Snomed((String)h[1], (String)h[2], null, null),
                                (String)h[3],
                                h[4] != null ? ((Date)h[4]).toLocalDate() : null,
                                h[5] != null ? ((Date)h[5]).toLocalDate() : null,
                                patientId
                        )
                )
        );
        return result;
    }
}
