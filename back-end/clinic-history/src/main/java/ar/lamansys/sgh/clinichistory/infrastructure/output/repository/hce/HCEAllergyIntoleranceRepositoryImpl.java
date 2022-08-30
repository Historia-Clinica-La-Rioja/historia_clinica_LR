package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEAllergyVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.AllergyIntoleranceVerificationStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HCEAllergyIntoleranceRepositoryImpl implements HCEAllergyIntoleranceRepository {

    private final EntityManager entityManager;

    public HCEAllergyIntoleranceRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<HCEAllergyVo> findActiveAllergiesByPatient(Integer patientId) {

        String sqlString = "with temporal as (" +
                "SELECT DISTINCT " +
                "ai.id, " +
                "ai.snomed_id, " +
                "ai.status_id, " +
                "ai.verification_status_id, " +
                "ai.category_id, " +
                "ai.criticality, " +
                "ai.start_date, " +
                "ai.updated_on, " +
                "row_number() over (partition by ai.snomed_id order by ai.updated_on desc) as rw " +
                "FROM {h-schema}document d " +
                "JOIN {h-schema}document_allergy_intolerance dai ON d.id = dai.document_id " +
                "JOIN {h-schema}allergy_intolerance ai ON dai.allergy_intolerance_id = ai.id " +
                "WHERE d.type_id IN (:documentTypes) "+
                "AND d.status_id IN (:documentStatusId) " +
                "AND ai.patient_id = :patientId " +
                ") " +
                "SELECT t.id AS id, s.sctid AS sctid, s.pt, t.status_id, t.verification_status_id, t.category_id, t.criticality, t.start_date " +
                "FROM temporal t " +
                "JOIN {h-schema}snomed s ON t.snomed_id = s.id " +
                "WHERE rw = 1 AND NOT status_id = :allergyIntoleranceStatus " +
                "ORDER BY t.updated_on desc ";

        List<Object[]> queryResult = entityManager.createNativeQuery(sqlString)
                .setParameter("patientId", patientId)
                .setParameter("documentStatusId", List.of(DocumentStatus.FINAL, DocumentStatus.DRAFT))
                .setParameter("documentTypes", List.of(DocumentType.OUTPATIENT, DocumentType.COUNTER_REFERENCE, DocumentType.ODONTOLOGY))
                .setParameter("allergyIntoleranceStatus", AllergyIntoleranceVerificationStatus.ERROR)
                .getResultList();
        List<HCEAllergyVo> result = new ArrayList<>();
        queryResult.forEach(a ->
                result.add(new HCEAllergyVo(
                        (Integer) a[0],
                        new Snomed((String) a[1], (String) a[2], null, null),
                        (String) a[3],
                        (String) a[4],
                        (Short) a[5],
                        (Short) a[6],
                        a[7] != null ? ((Date) a[7]).toLocalDate() : null
                ))
        );
        return result;
    }

	@Override
	@Transactional(readOnly = true)
	public HCEAllergyVo findHCEAllergy(Integer allergyId) {

		String sqlString = "with temporal as (" +
				"SELECT DISTINCT " +
				"ai.id, " +
				"ai.snomed_id, " +
				"ai.status_id, " +
				"ai.verification_status_id, " +
				"ai.category_id, " +
				"ai.criticality, " +
				"ai.start_date, " +
				"ai.updated_on, " +
				"row_number() over (partition by ai.snomed_id order by ai.updated_on desc) as rw " +
				"FROM {h-schema}document d " +
				"JOIN {h-schema}document_allergy_intolerance dai ON d.id = dai.document_id " +
				"JOIN {h-schema}allergy_intolerance ai ON dai.allergy_intolerance_id = ai.id " +
				"WHERE ai.id IN (:allergyId) "+
				") " +
				"SELECT t.id AS id, s.sctid AS sctid, s.pt, t.status_id, t.verification_status_id, t.category_id, t.criticality, t.start_date " +
				"FROM temporal t " +
				"JOIN {h-schema}snomed s ON t.snomed_id = s.id " +
				"WHERE rw = 1 AND NOT status_id = :allergyIntoleranceStatus " +
				"ORDER BY t.updated_on desc ";

		Object[] queryResult =  (Object[]) entityManager.createNativeQuery(sqlString)
				.setParameter("allergyId", allergyId)
				.setParameter("allergyIntoleranceStatus", AllergyIntoleranceVerificationStatus.ERROR)
				.getSingleResult();

		return new HCEAllergyVo(
						(Integer) queryResult[0],
						new Snomed((String) queryResult[1], (String) queryResult[2], null, null),
						(String) queryResult[3],
						(String) queryResult[4],
						(Short) queryResult[5],
						(Short) queryResult[6],
				queryResult[7] != null ? ((Date) queryResult[7]).toLocalDate() : null);
	}
}
