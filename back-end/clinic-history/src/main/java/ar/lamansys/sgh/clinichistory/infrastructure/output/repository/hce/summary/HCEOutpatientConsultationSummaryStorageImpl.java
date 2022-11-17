package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.summary;

import ar.lamansys.sgh.clinichistory.application.ports.HCEOutpatientConsultationSummaryStorage;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ClinicalSpecialtyBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.DocumentDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.HealthConditionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.HealthcareProfessionalBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.OutpatientEvolutionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReasonSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReferenceSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HCEOutpatientConsultationSummaryStorageImpl implements HCEOutpatientConsultationSummaryStorage {

    private final EntityManager entityManager;

    public HCEOutpatientConsultationSummaryStorageImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<OutpatientEvolutionSummaryBo> getAllOutpatientEvolutionSummary(Integer patientId) {
        String sqlString =" SELECT oc.id, oc.startDate, "
                + "hp.id AS healthcarProfessionalId, hp.licenseNumber, hp.personId, "
                + "p.firstName, p.lastName, p.identificationNumber, pe.nameSelfDetermination, "
                + "cs.id AS clinicalSpecialtyId, cs.name AS clinicalSpecialtyName, cs.clinicalSpecialtyTypeId, "
                + "n.description, docFile.id, docFile.filename "
                + " FROM OutpatientConsultation oc"
                + " LEFT JOIN ClinicalSpecialty cs ON (oc.clinicalSpecialtyId = cs.id)"
                + " JOIN Document doc ON (doc.sourceId = oc.id)"
                + " LEFT JOIN Note n ON (n.id = doc.otherNoteId)"
                + " JOIN HealthcareProfessional hp ON (hp.id = oc.doctorId)"
                + " JOIN Person p ON (p.id = hp.personId)"
		+ " JOIN PersonExtended pe ON (p.id = pe.id)"
                + " LEFT JOIN DocumentFile docFile ON (doc.id = docFile.id)"
                + " WHERE oc.billable = TRUE "
                + " AND oc.patientId = :patientId"
                + " AND doc.sourceTypeId = " + SourceType.OUTPATIENT
                + " ORDER BY oc.startDate DESC";

        List<Object[]> queryResult = entityManager.createQuery(sqlString)
                .setParameter("patientId", patientId)
                .getResultList();
        List<OutpatientEvolutionSummaryBo> result = new ArrayList<>();
        queryResult.forEach(a ->
                 result.add(new OutpatientEvolutionSummaryBo(
                        (Integer)a[0],
                        a[1] != null ? (LocalDate)a[1] : null,
                        new HealthcareProfessionalBo((Integer) a[2], (String) a[3], (Integer) a[4], (String) a[5], (String) a[6], (String) a[7], (String) a[8]),
                         a[9] != null ? new ClinicalSpecialtyBo((Integer)a[9], (String)a[10], (Short) a[11]) : null,
                        (String)a[12],
                         a[13] != null ? new DocumentDataBo((Long)a[13], (String)a[14]) : null))
        );
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HealthConditionSummaryBo> getHealthConditionsByPatient(Integer patientId, List<Integer> outpatientIds) {
        String sqlString ="SELECT hc.id, hc.patientId, "
                +"  s.sctid, s.pt, "
                +"  d.statusId, "
                +"  hc.startDate, hc.inactivationDate, "
                +"  hc.main, hc.problemId, oc.id "
                +"  FROM OutpatientConsultation oc "
                +"  JOIN Document d ON (d.sourceId = oc.id) "
                +"  JOIN DocumentHealthCondition dhc ON (d.id = dhc.pk.documentId) "
                +"  JOIN HealthCondition hc ON (dhc.pk.healthConditionId = hc.id) "
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
        List<HealthConditionSummaryBo> result = new ArrayList<>();
        queryResult.forEach(a ->
                result.add(new HealthConditionSummaryBo(
                        (Integer)a[0],
                        (Integer)a[1],
                        new SnomedBo((String) a[2], (String) a[3]),
                        (String)a[4], null, null,
                        a[5] != null ? (LocalDate)a[5] : null,
                        a[6] != null ? (LocalDate)a[6] : null,
                        (boolean)a[7],
                        (String)a[8],
                        (Integer) a[9]))
        );
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ReasonSummaryBo> getReasonsByPatient(Integer patientId, List<Integer> outpatientIds) {
        String sqlString = "SELECT r.id, r.description, oc.id"
                +"  FROM OutpatientConsultation oc"
                +"  JOIN OutpatientConsultationReasons ocr ON (ocr.pk.outpatientConsultationId = oc.id)"
                +"  JOIN Reason r ON (r.id = ocr.pk.reasonID)"
                +"  WHERE oc.patientId = :patientId"
                +"  AND oc.id IN (:outpatientIds) ";

        List<Object[]> queryResult = entityManager.createQuery(sqlString)
                .setParameter("patientId", patientId)
                .setParameter("outpatientIds", outpatientIds)
                .getResultList();
        List<ReasonSummaryBo> result = new ArrayList<>();
        queryResult.forEach(a ->
                result.add(new ReasonSummaryBo(
                        new SnomedBo((String) a[0],(String) a[1]),
                        (Integer)a[2]))
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

    @Override
    public List<ReferenceSummaryBo> getReferencesByHealthCondition(Integer healthConditionId, Integer outpatientId) {
        String sqlString = "SELECT r.id, cl.description , cs.name, rn.description, i.name"
                +"  FROM Reference r"
                +"  JOIN OutpatientConsultation oc ON (r.encounterId = oc.id)"
				+"  JOIN Institution i ON (oc.institutionId = i.id)"
                +"  LEFT JOIN CareLine cl ON (r.careLineId = cl.id)"
                +"  JOIN ClinicalSpecialty cs ON (r.clinicalSpecialtyId = cs.id)"
                +"  JOIN ReferenceHealthCondition rhc ON (r.id = rhc.pk.referenceId)"
                +"  LEFT JOIN ReferenceNote rn ON (r.referenceNoteId = rn.id)"
                +"  WHERE rhc.pk.healthConditionId = :healthConditionId"
                +"  AND r.sourceTypeId= " + SourceType.OUTPATIENT
                +"  AND oc.id = :outpatientId ";

        List<Object[]> queryResult = entityManager.createQuery(sqlString)
                .setParameter("healthConditionId", healthConditionId)
                .setParameter("outpatientId", outpatientId)
                .getResultList();
        List<ReferenceSummaryBo> result = new ArrayList<>();
        queryResult.forEach(a ->
                result.add(new ReferenceSummaryBo(
                        (Integer)a[0],
                        (String) a[1],
                        (String) a[2],
                        a[3] != null ? (String) a[3] : null,
						(String) a[4]))
        );
        return result;
    }
}