package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEMapClinicalObservationVo;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEMapHistoricClinicalObservationVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class HCEClinicalObservationRepositoryImpl implements HCEClinicalObservationRepository {

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public HCEMapClinicalObservationVo getGeneralState(Integer patientId, List<Short> invalidDocumentTypes) {
        log.debug("Input parameters -> patientId {}, invalidDocumentTypes {}", patientId, invalidDocumentTypes);

		String queryString = getQueryString(invalidDocumentTypes);

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
                    (String) o[3], timeStamp != null ? timeStamp.toLocalDateTime() : null, null));
        }
        HCEMapClinicalObservationVo result = new HCEMapClinicalObservationVo(clinicalObservationVos);
        log.debug("Output -> {}", result);
        return result;
    }

	@Transactional(readOnly = true)
	@Override
	public HCEMapHistoricClinicalObservationVo getHistoricData(Integer patientId, List<Short> invalidDocumentTypes) {
		log.debug("Input parameters -> patientId {}, invalidDocumentTypes {}", patientId, invalidDocumentTypes);

		String queryString = getQueryString(invalidDocumentTypes);

		Query query = entityManager.createNativeQuery(queryString);
		query.setParameter("patientId", patientId);
		query.setParameter("docStatusId", DocumentStatus.FINAL);
		if(!invalidDocumentTypes.isEmpty())
			query.setParameter("invalidDocumentTypes", invalidDocumentTypes);


		List<Object[]> queryResult = query.getResultList();
		List<HCEClinicalObservationVo> clinicalObservationVos = new ArrayList<>();
		for (Object[] o : queryResult) {
			Timestamp timeStamp = (Timestamp) o[4];
			BigInteger documentId = (BigInteger) o[5];
			clinicalObservationVos.add(new HCEClinicalObservationVo((int) o[0], (String) o[1], (String) o[2],
					(String) o[3], timeStamp != null ? timeStamp.toLocalDateTime() : null, documentId.longValue()));
		}
		HCEMapHistoricClinicalObservationVo result = new HCEMapHistoricClinicalObservationVo(clinicalObservationVos);
		log.debug("Output -> {}", result);
		return result;
	}

	private String getQueryString(List<Short> invalidDocumentTypes) {
		String invalidDocumentCondition = (invalidDocumentTypes.isEmpty()) ? "" : "AND d.type_id NOT IN :invalidDocumentTypes ";
		String queryString =
				"   (SELECT  ovs.id, " +
						"            s.sctid, " +
						"            ovs.status_id, " +
						"            ovs.value, " +
						"            ovs.effective_time, " +
						"			 d.id as document_id " +
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
						"            ovs.effective_time, " +
						"			 d.id as document_id " +
						"    FROM {h-schema}document d " +
						"    JOIN {h-schema}document_lab dl ON (d.id = dl.document_id) " +
						"    JOIN {h-schema}observation_lab ovs ON (dl.observation_lab_id = ovs.id) " +
						"    JOIN {h-schema}snomed s ON (ovs.snomed_id = s.id) " +
						"    WHERE d.status_id IN (:docStatusId) " +
						invalidDocumentCondition +
						"       AND ovs.patient_id = :patientId " +
						")" +
						"    ORDER BY effective_time DESC ";

		return queryString;
	}

}
