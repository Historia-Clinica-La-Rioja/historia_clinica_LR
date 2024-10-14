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
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CipresOutpatientConsultationSummaryStorageImpl implements CipresOutpatientConsultationSummaryStorage {

	private final EntityManager entityManager;

	@Transactional(readOnly = true)
	@Override
	public List<CipresOutpatientBasicDataBo> getOutpatientConsultationsForSend(Integer limit, LocalDateTime start, LocalDateTime end) {
		 String sqlString =" SELECT NEW net.pladema.clinichistory.cipres.domain.CipresOutpatientBasicDataBo(oc.id, d.id, oc.creationable.createdOn, " +
				"cs.id, cs.sctidCode, i.id, i.sisaCode, oc.patientId, pe.id, pe.identificationTypeId, pe.identificationNumber, pe.genderId) " +
				"FROM OutpatientConsultation oc " +
				"JOIN ClinicalSpecialty cs ON (oc.clinicalSpecialtyId = cs.id) " +
				"JOIN Institution i ON (oc.institutionId = i.id) " +
				"JOIN Document d ON (oc.id = d.sourceId) " +
				"JOIN Patient p ON (oc.patientId = p.id) " +
				"JOIN Person pe ON (p.personId = pe.id) " +
				"LEFT JOIN CipresEncounter ce ON (oc.id = ce.encounterId) " +
				"WHERE oc.billable = TRUE " +
				"AND d.statusId = '" + DocumentStatus.FINAL + "' " +
				"AND d.typeId = " + DocumentType.OUTPATIENT +
				"AND d.sourceTypeId = " + SourceType.OUTPATIENT +
				"AND ce.encounterId IS NULL " +
				"AND (oc.creationable.createdOn BETWEEN :start AND :end) " +
				"ORDER BY oc.creationable.createdOn DESC";

        return (List<CipresOutpatientBasicDataBo>) entityManager.createQuery(sqlString)
				.setParameter("end", end)
				.setParameter("start", start)
				.setMaxResults(limit)
				.getResultList();
	}

	@Transactional(readOnly = true)
	@Override
	public List<CipresOutpatientBasicDataBo> getOutpatientConsultationsForSendOrResend(Integer limit, LocalDateTime start, LocalDateTime end) {

		String sqlString =" SELECT NEW net.pladema.clinichistory.cipres.domain.CipresOutpatientBasicDataBo(oc.id, d.id, oc.creationable.createdOn, " +
				"cs.id, cs.sctidCode, i.id, i.sisaCode, oc.patientId, pe.id, pe.identificationTypeId, pe.identificationNumber, pe.genderId, ce.id) " +
				"FROM OutpatientConsultation oc " +
				"JOIN ClinicalSpecialty cs ON (oc.clinicalSpecialtyId = cs.id) " +
				"JOIN Institution i ON (oc.institutionId = i.id) " +
				"JOIN Document d ON (oc.id = d.sourceId) " +
				"JOIN Patient p ON (oc.patientId = p.id) " +
				"JOIN Person pe ON (p.personId = pe.id) " +
				"LEFT JOIN CipresEncounter ce ON (oc.id = ce.encounterId) " +
				"WHERE oc.billable = TRUE " +
				"AND d.statusId = '" + DocumentStatus.FINAL + "' " +
				"AND d.typeId = " + DocumentType.OUTPATIENT +
				"AND d.sourceTypeId = " + SourceType.OUTPATIENT +
				"AND (ce.responseCode IN (500, 503) OR ce.encounterId IS NULL)" +
				"AND (oc.creationable.createdOn BETWEEN :start AND :end) " +
				"ORDER BY oc.creationable.createdOn DESC";

		return (List<CipresOutpatientBasicDataBo>) entityManager.createQuery(sqlString)
				.setParameter("end", end)
				.setParameter("start", start)
				.setMaxResults(limit)
				.getResultList();
	}
	@Override
	public CipresOutpatientBasicDataBo getOutpatientConsultationByCipresEncounterId(Integer cipresEncounterId) {
		String sqlString =" SELECT NEW net.pladema.clinichistory.cipres.domain.CipresOutpatientBasicDataBo(oc.id, d.id, oc.creationable.createdOn, " +
				"cs.id, cs.sctidCode, i.id, i.sisaCode, oc.patientId, pe.id, pe.identificationTypeId, pe.identificationNumber, pe.genderId) " +
				"FROM OutpatientConsultation oc " +
				"JOIN ClinicalSpecialty cs ON (oc.clinicalSpecialtyId = cs.id) " +
				"JOIN Institution i ON (oc.institutionId = i.id) " +
				"JOIN Document d ON (oc.id = d.sourceId) " +
				"JOIN Patient p ON (oc.patientId = p.id) " +
				"JOIN Person pe ON (p.personId = pe.id) " +
				"JOIN CipresEncounter ce ON (oc.id = ce.encounterId)" +
				"AND ce.id = :cipresEncounterId";

        return (CipresOutpatientBasicDataBo) entityManager.createQuery(sqlString)
				.setParameter("cipresEncounterId", cipresEncounterId)
				.getSingleResult();
	}

}
