package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.healthcondition;

import ar.lamansys.sgh.clinichistory.application.healthCondition.HealthConditionStorage;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthConditionStorageImpl implements HealthConditionStorage {

    private final EntityManager entityManager;

    @Override
    public List<Integer> getHealthConditionIdByEncounterAndSnomedConcept(Integer encounterId, Integer sourceTypeId, String sctid, String pt) {
        log.debug("Input parameters encounterId {}, sourceTypeId {} , sctid {} , pt {}", encounterId, sourceTypeId, sctid, pt);

        String sqlString = "SELECT hc.id as id "
                +"  FROM Document d"
                +"  JOIN DocumentHealthCondition dhc ON (d.id = dhc.pk.documentId)"
                +"  JOIN HealthCondition hc ON (dhc.pk.healthConditionId = hc.id)"
                +"  JOIN Snomed s ON (s.id = hc.snomedId)"
                +"  WHERE d.sourceId = :encounterId"
                +"  AND d.statusId = '" + DocumentStatus.FINAL + "'"
                +"  AND d.sourceTypeId = :sourceTypeId"
                +"  AND hc.problemId IN ('"+ ProblemType.PROBLEM+"', '"+ ProblemType.CHRONIC+ "')"
                +"  AND s.sctid = :sctid"
                +"  AND s.pt = :pt";

        List<Integer> result = entityManager.createQuery(sqlString)
                .setParameter("encounterId", encounterId)
                .setParameter("sourceTypeId", sourceTypeId.shortValue())
                .setParameter("sctid", sctid)
                .setParameter("pt", pt)
				.getResultList();

        log.debug("Output parameter -> result {}", result);
        return result;
    }

	@Override
	public List<Integer> getHealthConditionIdByDocumentIdAndSnomedConcept(Long documentId, String sctid, String pt) {
		log.debug("Input parameters documentId {}, sctid {} , pt {}", documentId, sctid, pt);

		String sqlString = "SELECT hc.id as id "
				+"  FROM Document d"
				+"  JOIN DocumentHealthCondition dhc ON (d.id = dhc.pk.documentId)"
				+"  JOIN HealthCondition hc ON (dhc.pk.healthConditionId = hc.id)"
				+"  JOIN Snomed s ON (s.id = hc.snomedId)"
				+"  WHERE d.id = :documentId"
				+"  AND s.sctid = :sctid"
				+"  AND s.pt = :pt"
				+"  ORDER BY hc.updateable.updatedOn DESC"
				;

		List<Integer> result = entityManager.createQuery(sqlString)
				.setParameter("documentId", documentId)
				.setParameter("sctid", sctid)
				.setParameter("pt", pt)
				.getResultList();

		log.debug("Output parameter -> result {}", result);
		return result;
	}

}
