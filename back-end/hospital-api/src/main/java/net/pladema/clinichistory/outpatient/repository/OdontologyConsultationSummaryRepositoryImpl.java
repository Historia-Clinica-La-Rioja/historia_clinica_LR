package net.pladema.clinichistory.outpatient.repository;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ReferenceSummaryVo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReasonSummaryBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import net.pladema.clinichistory.outpatient.repository.domain.OdontologyEvolutionSummaryVo;
import net.pladema.person.repository.entity.Person;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OdontologyConsultationSummaryRepositoryImpl implements OdontologyConsultationSummaryRepository {

    private final EntityManager entityManager;

    public OdontologyConsultationSummaryRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<OdontologyEvolutionSummaryVo> getAllOdontologyEvolutionSummary(Integer patientId) {
        String sqlString =" SELECT oc.id, oc.performedDate, cs, hp, p, n.description"
                + " FROM OdontologyConsultation oc"
                + " LEFT JOIN ClinicalSpecialty cs ON (oc.clinicalSpecialtyId = cs.id)"
                + " JOIN Document doc ON (doc.sourceId = oc.id)"
                + " LEFT JOIN Note n ON (n.id = doc.otherNoteId)"
                + " JOIN HealthcareProfessional hp ON (hp.id = oc.doctorId)"
                + " JOIN Person p ON (p.id = hp.personId)"
                + " WHERE oc.billable = TRUE "
                + " AND oc.patientId = :patientId"
                + " AND doc.sourceTypeId = " + SourceType.ODONTOLOGY
                + " ORDER BY oc.performedDate DESC";

        List<Object[]> queryResult = entityManager.createQuery(sqlString)
                .setParameter("patientId", patientId)
                .getResultList();
        List<OdontologyEvolutionSummaryVo> result = new ArrayList<>();
        queryResult.forEach(a ->
                result.add(new OdontologyEvolutionSummaryVo(
                        (Integer)a[0],
                        a[1] != null ? (LocalDate)a[1] : null,
                        (ClinicalSpecialty) a[2],
                        (HealthcareProfessional) a[3],
                        (Person) a[4],
                        (String)a[5]))
        );
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HealthConditionSummaryVo> getHealthConditionsByPatient(Integer patientId, List<Integer> odontologyConsultationIds) {
        String sqlString ="SELECT hc.id, s.sctid, s.pt, d.statusId, hc.startDate, hc.inactivationDate, hc.main, hc.problemId, oc.id"
                +"  FROM OdontologyConsultation oc"
                +"  JOIN Document d ON (d.sourceId = oc.id)"
                +"  JOIN DocumentHealthCondition dhc ON (d.id = dhc.pk.documentId)"
                +"  JOIN HealthCondition hc ON (dhc.pk.healthConditionId = hc.id)"
                +"  JOIN Snomed s ON (s.id = hc.snomedId)"
                +"  WHERE d.statusId = '" + DocumentStatus.FINAL + "'"
                +"  AND d.sourceTypeId =" + SourceType.ODONTOLOGY
                +"  AND d.typeId = "+ DocumentType.ODONTOLOGY
                +"  AND hc.patientId = :patientId "
                +"  AND hc.problemId IN ('"+ ProblemType.PROBLEM+"', '"+ ProblemType.CHRONIC+ "')"
                +"  AND oc.id IN (:odontologyConsultationIds) ";

        List<Object[]> queryResult = entityManager.createQuery(sqlString)
                .setParameter("patientId", patientId)
                .setParameter("odontologyConsultationIds", odontologyConsultationIds)
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
    public List<ReasonSummaryBo> getReasonsByPatient(Integer patientId, List<Integer> odontologyConsultationIds) {
        String sqlString = "SELECT r.id, r.description, oc.id"
                +"  FROM OdontologyConsultation oc"
                +"  JOIN OdontologyConsultationReason ocr ON (ocr.pk.odontologyConsultationId = oc.id)"
                +"  JOIN OdontologyReason r ON (r.id = ocr.pk.reasonId)"
                +"  WHERE oc.patientId = :patientId"
                +"  AND oc.id IN (:odontologyConsultationIds) ";

        List<Object[]> queryResult = entityManager.createQuery(sqlString)
                .setParameter("patientId", patientId)
                .setParameter("odontologyConsultationIds", odontologyConsultationIds)
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
    public List<ProcedureSummaryBo> getProceduresByPatient(Integer patientId, List<Integer> odontologyConsultationIds) {
        String sqlString = "SELECT p.id, s.id, s.sctid, s.pt, p.performedDate, oc.id"
                +"  FROM OdontologyConsultation oc"
                +"  JOIN Document d ON (d.sourceId = oc.id)"
                +"  JOIN DocumentProcedure dp ON (d.id = dp.pk.documentId)"
                +"  JOIN Procedure p ON (dp.pk.procedureId = p.id)"
                +"  JOIN Snomed s ON (p.snomedId = s.id) "
                +"  WHERE oc.patientId = :patientId"
                +"  AND d.statusId = '" + DocumentStatus.FINAL + "'"
                +"  AND d.typeId = "+ DocumentType.ODONTOLOGY
                +"  AND d.sourceTypeId =" + SourceType.ODONTOLOGY
                +"  AND oc.id IN (:odontologyConsultationIds) ";
        
        List<Object[]> queryResult = entityManager.createQuery(sqlString)
                .setParameter("patientId", patientId)
                .setParameter("odontologyConsultationIds", odontologyConsultationIds)
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
    public List<ReferenceSummaryVo> getReferenceByHealthCondition(Integer healthConditionId, Integer consultationId) {
        String sqlString = "SELECT r.id, cl.description , cs.name, rn.description"
                +"  FROM Reference r"
                +"  JOIN OdontologyConsultation oc ON (r.encounterId = oc.id)"
                +"  LEFT JOIN CareLine cl ON (r.careLineId = cl.id)"
                +"  JOIN ClinicalSpecialty cs ON (r.clinicalSpecialtyId = cs.id)"
                +"  JOIN ReferenceHealthCondition rhc ON (r.id = rhc.pk.referenceId)"
                +"  LEFT JOIN ReferenceNote rn ON (r.referenceNoteId = rn.id)"
                +"  WHERE rhc.pk.healthConditionId = :healthConditionId"
                +"  AND r.sourceTypeId= " + SourceType.ODONTOLOGY
                +"  AND oc.id = :consultationId ";

        List<Object[]> queryResult = entityManager.createQuery(sqlString)
                .setParameter("healthConditionId", healthConditionId)
                .setParameter("consultationId", consultationId)
                .getResultList();
        List<ReferenceSummaryVo> result = new ArrayList<>();
        queryResult.forEach(a ->
                result.add(new ReferenceSummaryVo(
                        (Integer)a[0],
                        (String) a[1],
                        (String) a[2],
                        a[3] != null ? (String) a[3] : null))
        );
        return result;
    }

}
