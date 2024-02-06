package net.pladema.clinichistory.outpatient.repository;

import ar.lamansys.sgh.clinichistory.domain.hce.summary.MedicationSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientBasicDataBo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OutpatientConsultationSummaryStorageImpl implements OutpatientConsultationSummaryStorage {

    private final EntityManager entityManager;

    public OutpatientConsultationSummaryStorageImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HealthConditionSummaryVo> getHealthConditionsByPatient(Integer patientId, List<Integer> outpatientIds) {
        String sqlString ="SELECT hc.id, s.sctid, s.pt, d.statusId, hc.startDate, hc.inactivationDate, hc.main, hc.problemId, oc.id"
                +"  FROM OutpatientConsultation oc"
                +"  JOIN Document d ON (d.sourceId = oc.id)"
                +"  JOIN DocumentHealthCondition dhc ON (d.id = dhc.pk.documentId)"
                +"  JOIN HealthCondition hc ON (dhc.pk.healthConditionId = hc.id)"
                +"  JOIN Snomed s ON (s.id = hc.snomedId)"
                +"  WHERE d.statusId = '" + DocumentStatus.FINAL + "'"
                +"  AND d.sourceTypeId =" + SourceType.OUTPATIENT
                +"  AND d.typeId = "+ DocumentType.OUTPATIENT
                +"  AND hc.patientId = :patientId "
                +"  AND hc.problemId IN ('"+ ProblemType.PROBLEM+"', '"+ ProblemType.CHRONIC+ "')"
                +"  AND oc.id IN (:outpatientIds) ";

        List<Object[]> queryResult = entityManager.createQuery(sqlString)
                .setParameter("patientId", patientId)
                .setParameter("outpatientIds", outpatientIds)
                .getResultList();
        List<HealthConditionSummaryVo> result = new ArrayList<>();
        queryResult.forEach(a ->
                result.add(new HealthConditionSummaryVo(
                        (Integer)a[0],
                        (String) a[1],
                        (String) a[2],
                        (String)a[3],
                        a[4] != null ? (LocalDate)a[4] : null,
                        a[5] != null ? (LocalDate)a[5] : null,
                        (boolean)a[6],
                        (String)a[7],
                        (Integer) a[8]))
        );
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProcedureSummaryBo> getProceduresByPatient(Integer patientId, List<Integer> outpatientIds) {
        String sqlString = "SELECT p.id, s.id, s.sctid, s.pt, p.performedDate, oc.id"
                +"  FROM OutpatientConsultation oc"
                +"  JOIN Document d ON (d.sourceId = oc.id)"
                +"  JOIN DocumentProcedure dp ON (d.id = dp.pk.documentId)"
                +"  JOIN Procedure p ON (dp.pk.procedureId = p.id)"
                +"  JOIN Snomed s ON (p.snomedId = s.id) "
                +"  WHERE oc.patientId = :patientId"
                +"  AND d.statusId = '" + DocumentStatus.FINAL + "'"
                +"  AND d.typeId = "+ DocumentType.OUTPATIENT
                +"  AND d.sourceTypeId =" + SourceType.OUTPATIENT
                +"  AND oc.id IN (:outpatientIds) ";
        
        List<Object[]> queryResult = entityManager.createQuery(sqlString)
                .setParameter("patientId", patientId)
                .setParameter("outpatientIds", outpatientIds)
                .getResultList();
        List<ProcedureSummaryBo> result = new ArrayList<>();
        queryResult.forEach(a ->
                result.add(new ProcedureSummaryBo(
                        (Integer)a[0],
                        new SnomedBo((Integer) a[1],(String) a[2],(String) a[3]),
                        a[4] != null ? (LocalDate)a[4] : null,
                        (Integer) a[5]))
        );
        return result;
    }

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<OutpatientBasicDataBo> getOutpatientConsultationsToCipres(Integer limit, LocalDateTime start, LocalDateTime end) {
		String sqlString =" SELECT oc.id, d.id, oc.creationable.createdOn, cs.sctidCode, i.sisaCode, oc.patientId, pe.id, " +
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
				"ORDER BY oc.creationable.createdOn ";

		List<Object[]> queryResult = entityManager.createQuery(sqlString)
				.setParameter("end", end)
				.setParameter("start", start)
				.setMaxResults(limit)
				.getResultList();
		List<OutpatientBasicDataBo> result = new ArrayList<>();
		queryResult.forEach(a ->
				result.add(new OutpatientBasicDataBo(
						(Integer)a[0],
						(Long)a[1],
						(LocalDateTime)a[2],
						(String) a[3],
						(String) a[4],
						(Integer) a[5],
						(Integer)a[6],
						(Short)a[7],
						(String)a[8],
						(Short)a[9])
				)
		);
		return result;
	}

	@Override
	public List<ProcedureSummaryBo> getProceduresByOutpatientIds(List<Integer> outpatientIds) {
		String sqlString = "SELECT p.id, s.id, s.sctid, s.pt, p.performedDate, oc.id"
				+"  FROM OutpatientConsultation oc"
				+"  JOIN Document d ON (d.sourceId = oc.id)"
				+"  JOIN DocumentProcedure dp ON (d.id = dp.pk.documentId)"
				+"  JOIN Procedure p ON (dp.pk.procedureId = p.id)"
				+"  JOIN Snomed s ON (p.snomedId = s.id) "
				+"  WHERE d.statusId = '" + DocumentStatus.FINAL + "'"
				+"  AND d.typeId = "+ DocumentType.OUTPATIENT
				+"  AND d.sourceTypeId =" + SourceType.OUTPATIENT
				+"  AND oc.id IN (:outpatientIds) ";

		List<Object[]> queryResult = entityManager.createQuery(sqlString)
				.setParameter("outpatientIds", outpatientIds)
				.getResultList();
		List<ProcedureSummaryBo> result = new ArrayList<>();
		queryResult.forEach(a ->
				result.add(new ProcedureSummaryBo(
						(Integer)a[0],
						new SnomedBo((Integer) a[1],(String) a[2],(String) a[3]),
						a[4] != null ? (LocalDate)a[4] : null,
						(Integer) a[5])
				)
		);
		return result;
	}

	@Override
	public List<HealthConditionSummaryVo> getHealthConditionsByOutpatientIds(List<Integer> outpatientIds) {
		String sqlString ="SELECT hc.id, s.sctid, s.pt, d.statusId, hc.startDate, hc.inactivationDate, hc.main, hc.problemId, oc.id"
				+"  FROM OutpatientConsultation oc"
				+"  JOIN Document d ON (d.sourceId = oc.id)"
				+"  JOIN DocumentHealthCondition dhc ON (d.id = dhc.pk.documentId)"
				+"  JOIN HealthCondition hc ON (dhc.pk.healthConditionId = hc.id)"
				+"  JOIN Snomed s ON (s.id = hc.snomedId)"
				+"  WHERE d.statusId = '" + DocumentStatus.FINAL + "'"
				+"  AND d.sourceTypeId =" + SourceType.OUTPATIENT
				+"  AND d.typeId = "+ DocumentType.OUTPATIENT
				+"  AND hc.problemId IN ('"+ ProblemType.PROBLEM+"', '"+ ProblemType.CHRONIC+ "')"
				+"  AND oc.id IN (:outpatientIds) ";

		List<Object[]> queryResult = entityManager.createQuery(sqlString)
				.setParameter("outpatientIds", outpatientIds)
				.getResultList();
		List<HealthConditionSummaryVo> result = new ArrayList<>();
		queryResult.forEach(a ->
				result.add(new HealthConditionSummaryVo(
						(Integer)a[0],
						(String) a[1],
						(String) a[2],
						(String)a[3],
						a[4] != null ? (LocalDate)a[4] : null,
						a[5] != null ? (LocalDate)a[5] : null,
						(boolean)a[6],
						(String)a[7],
						(Integer) a[8])
				)
		);
		return result;
	}

	@Override
	public List<MedicationSummaryBo> getMedicationsByOutpatientIds(List<Integer> outpatientIds) {
		String sqlString ="SELECT ms.id, oc.id, s.id, s.sctid, s.pt"
				+"  FROM OutpatientConsultation oc"
				+"  JOIN Document d ON (d.sourceId = oc.id) "
				+"  JOIN DocumentMedicamentionStatement dms ON (d.id = dms.pk.documentId) "
				+"  JOIN MedicationStatement ms ON (dms.pk.medicationStatementId = ms.id ) "
				+"  JOIN Snomed s ON (s.id = ms.snomedId) "
				+"  WHERE d.statusId = '" + DocumentStatus.FINAL + "'"
				+"  AND d.sourceTypeId =" + SourceType.OUTPATIENT
				+"  AND d.typeId = "+ DocumentType.OUTPATIENT
				+"  AND oc.id IN (:outpatientIds)";

		List<Object[]> queryResult = entityManager.createQuery(sqlString)
				.setParameter("outpatientIds", outpatientIds)
				.getResultList();
		List<MedicationSummaryBo> result = new ArrayList<>();
		queryResult.forEach(a ->
				result.add(new MedicationSummaryBo(
						(Integer)a[0],
						(Integer) a[1],
						(Integer) a[2],
						(String) a[3],
						(String) a[4])
				)
		);
		return result;
	}

}
