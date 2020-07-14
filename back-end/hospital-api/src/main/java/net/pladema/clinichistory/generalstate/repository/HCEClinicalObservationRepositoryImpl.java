package net.pladema.clinichistory.generalstate.repository;

import net.pladema.clinichistory.generalstate.repository.domain.HCEClinicalObservationVo;
import net.pladema.clinichistory.generalstate.repository.domain.HCEMapClinicalObservationVo;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class HCEClinicalObservationRepositoryImpl implements HCEClinicalObservationRepository {

    private static final Logger LOG = LoggerFactory.getLogger(HCEClinicalObservationRepositoryImpl.class);

    private final EntityManager entityManager;

    public HCEClinicalObservationRepositoryImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public HCEMapClinicalObservationVo getGeneralState(Integer patientId) {
        LOG.debug("Input parameters -> patientId {}", patientId);
        Query query = entityManager.createNativeQuery(
                "   (SELECT  ovs.id, " +
                        "            ovs.sctid_code, " +
                        "            ovs.status_id, " +
                        "            ovs.value, " +
                        "            ovs.effective_time " +
                        "    FROM document d " +
                        "    JOIN document_vital_sign dvs ON (d.id = dvs.document_id) " +
                        "    JOIN observation_vital_sign ovs ON (dvs.observation_vital_sign_id = ovs.id) " +
                        "    WHERE d.status_id = :docStatusId " +
                        "       AND d.type_id NOT IN :invalidDocumentTypes "+
                        "       AND ovs.patient_id = :patientId " +
                        " )UNION( " +
                        "   SELECT  ovs.id, " +
                        "            ovs.sctid_code, " +
                        "            ovs.status_id, " +
                        "            ovs.value, " +
                        "            ovs.effective_time " +
                        "    FROM document d " +
                        "    JOIN document_lab dl ON (d.id = dl.document_id) " +
                        "    JOIN observation_lab ovs ON (dl.observation_lab_id = ovs.id) " +
                        "    WHERE d.status_id = :docStatusId " +
                        "       AND d.type_id NOT IN :invalidDocumentTypes "+
                        "       AND ovs.patient_id = :patientId " +
                        ")" +
                        "    ORDER BY effective_time DESC " );
        query.setParameter("patientId", patientId);
        query.setParameter("docStatusId", DocumentStatus.FINAL);
        query.setParameter("invalidDocumentTypes", Arrays.asList(DocumentType.ANAMNESIS, DocumentType.EVALUATION_NOTE, DocumentType.EPICRISIS));


        List<Object[]> queryResult = query.getResultList();
        List<HCEClinicalObservationVo> clinicalObservationVos = new ArrayList<>();
        for (Object[] o : queryResult) {
            Timestamp timeStamp = (Timestamp) o[4];
            clinicalObservationVos.add(new HCEClinicalObservationVo((int) o[0], (String) o[1], (String) o[2],
                    (String) o[3], timeStamp != null ? timeStamp.toLocalDateTime() : null));
        }
        HCEMapClinicalObservationVo result = new HCEMapClinicalObservationVo(clinicalObservationVos);
        LOG.debug("Output -> {}", result);
        return result;
    }
}
