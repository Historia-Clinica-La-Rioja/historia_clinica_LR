package net.pladema.clinichistory.cipres.infrastructure.output;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.cipres.application.port.CipresOutpatientConsultationSummaryStorage;
import net.pladema.clinichistory.cipres.domain.CipresOutpatientBasicDataBo;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CipresOutpatientConsultationSummaryStorageImpl implements CipresOutpatientConsultationSummaryStorage {

	private final EntityManager entityManager;

	@Transactional(readOnly = true)
	@Override
	public List<CipresOutpatientBasicDataBo> getOutpatientConsultations(Integer limit, LocalDateTime start, LocalDateTime end) {
		String sqlString =" SELECT oc.id, d.id, oc.creationable.createdOn, cs.id, cs.sctidCode, i.sisaCode, oc.patientId, pe.id, " +
				"pe.identificationTypeId, pe.identificationNumber, pe.genderId " +
				"FROM OutpatientConsultation oc " +
				"JOIN ClinicalSpecialty cs ON (oc.clinicalSpecialtyId = cs.id) " +
				"JOIN Institution i ON (oc.institutionId = i.id) " +
				"JOIN Document d ON (oc.id = d.sourceId) " +
				"JOIN Patient p ON (oc.patientId = p.id) " +
				"JOIN Person pe ON (p.personId = pe.id) " +
				"WHERE oc.billable = TRUE " +
				"AND d.statusId = '" + DocumentStatus.FINAL + "' " +
				"AND d.typeId = " + DocumentType.OUTPATIENT +
				"AND d.sourceTypeId = " + SourceType.OUTPATIENT +
				"AND oc.id NOT IN (SELECT ce.encounterId FROM CipresEncounter ce) " +
				"AND (oc.creationable.createdOn BETWEEN :start AND :end) " +
				"ORDER BY oc.creationable.createdOn DESC";

		List<Object[]> queryResult = entityManager.createQuery(sqlString)
				.setParameter("end", end)
				.setParameter("start", start)
				.setMaxResults(limit)
				.getResultList();
		List<CipresOutpatientBasicDataBo> result = new ArrayList<>();
		queryResult.forEach(a ->
				result.add(new CipresOutpatientBasicDataBo(
						(Integer)a[0],
						(Long)a[1],
						(LocalDateTime)a[2],
						(Integer) a[3],
						(String) a[4],
						(String) a[5],
						(Integer) a[6],
						(Integer)a[7],
						(Short)a[8],
						(String)a[9],
						(Short)a[10])
				)
		);
		return result;
	}

}
