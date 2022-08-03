package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEMapClinicalObservationVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
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
    public HCEMapClinicalObservationVo getGeneralState(Integer patientId, List<Short> invalidDocumentTypes) {
        LOG.debug("Input parameters -> patientId {}, invalidDocumentTypes {}", patientId, invalidDocumentTypes);

		String invalidDocumentCondition = (invalidDocumentTypes.isEmpty()) ? "" : "AND d.type_id NOT IN :invalidDocumentTypes ";
		String queryString =
                "   (SELECT  ovs.id, " +
                        "            s.sctid, " +
                        "            ovs.status_id, " +
                        "            ovs.value, " +
                        "            ovs.effective_time " +
                        "    FROM {h-schema}document d " +
                        "    JOIN {h-schema}document_vital_sign dvs ON (d.id = dvs.document_id) " +
                        "    JOIN {h-schema}observation_vital_sign ovs ON (dvs.observation_vital_sign_id = ovs.id) " +
                        "    JOIN {h-schema}snomed s ON (ovs.snomed_id = s.id) " +
                        "    WHERE d.status_id IN (:docStatusId) " +
								invalidDocumentCondition +
                        "       AND ovs.patient_id = :patientId " +
                        " )UNION( " +
                        "   SELECT  ovs.id, " +
                        "            s.sctid, " +
                        "            ovs.status_id, " +
                        "            ovs.value, " +
                        "            ovs.effective_time " +
                        "    FROM {h-schema}document d " +
                        "    JOIN {h-schema}document_lab dl ON (d.id = dl.document_id) " +
                        "    JOIN {h-schema}observation_lab ovs ON (dl.observation_lab_id = ovs.id) " +
                        "    JOIN {h-schema}snomed s ON (ovs.snomed_id = s.id) " +
                        "    WHERE d.status_id IN (:docStatusId) " +
								invalidDocumentCondition +
                        "       AND ovs.patient_id = :patientId " +
                        ")" +
                        "    ORDER BY effective_time DESC ";

		Query query = entityManager.createNativeQuery(queryString);
        query.setParameter("patientId", patientId);
        query.setParameter("docStatusId", DocumentStatus.FINAL);
        if(!invalidDocumentTypes.isEmpty())
        	query.setParameter("invalidDocumentTypes", invalidDocumentTypes);


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
