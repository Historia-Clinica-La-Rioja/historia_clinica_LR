package net.pladema.clinichistory.outpatient.repository;

import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import net.pladema.clinichistory.outpatient.repository.domain.NursingEvolutionSummaryVo;
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
public class NursingConsultationSummaryRepositoryImpl implements NursingConsultationSummaryRepository {

    private final EntityManager entityManager;

    public NursingConsultationSummaryRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NursingEvolutionSummaryVo> getAllNursingEvolutionSummary(Integer patientId) {
        String sqlString = " SELECT nc.id, nc.performedDate, cs, hp, p, n.description"
                + " FROM NursingConsultation nc"
                + " LEFT JOIN ClinicalSpecialty cs ON (nc.clinicalSpecialtyId = cs.id)"
                + " JOIN Document doc ON (doc.sourceId = nc.id)"
                + " LEFT JOIN Note n ON (n.id = doc.evolutionNoteId)"
                + " JOIN HealthcareProfessional hp ON (hp.id = nc.doctorId)"
                + " JOIN Person p ON (p.id = hp.personId)"
                + " WHERE nc.patientId = :patientId"
                + " AND doc.sourceTypeId = " + SourceType.NURSING
                + " ORDER BY nc.performedDate DESC";

        List<Object[]> queryResult = entityManager.createQuery(sqlString)
                .setParameter("patientId", patientId)
                .getResultList();
        List<NursingEvolutionSummaryVo> result = new ArrayList<>();
        queryResult.forEach(a ->
                result.add(new NursingEvolutionSummaryVo(
                        (Integer) a[0],
                        a[1] != null ? (LocalDate) a[1] : null,
                        (ClinicalSpecialty) a[2],
                        (HealthcareProfessional) a[3],
                        (Person) a[4],
                        (String) a[5]))
        );
        return result;
    }

    @Override
    public List<HealthConditionSummaryVo> getHealthConditionsByPatient(Integer patientId, List<Integer> nursingConsultationIds) {
        String sqlString = "SELECT hc.id, s.sctid, s.pt, d.statusId, hc.startDate, hc.inactivationDate, hc.main, hc.problemId, nc.id"
                + "  FROM NursingConsultation nc"
                + "  JOIN Document d ON (d.sourceId = nc.id)"
                + "  JOIN DocumentHealthCondition dhc ON (d.id = dhc.pk.documentId)"
                + "  JOIN HealthCondition hc ON (dhc.pk.healthConditionId = hc.id)"
                + "  JOIN Snomed s ON (s.id = hc.snomedId)"
                + "  WHERE d.statusId = '" + DocumentStatus.FINAL + "'"
                + "  AND d.sourceTypeId =" + SourceType.NURSING
                + "  AND d.typeId = " + DocumentType.NURSING
                + "  AND hc.patientId = :patientId "
                + "  AND hc.problemId IN ('" + ProblemType.PROBLEM + "', '" + ProblemType.CHRONIC + "')"
                + "  AND nc.id IN (:nursingConsultationIds) ";

        List<Object[]> queryResult = entityManager.createQuery(sqlString)
                .setParameter("patientId", patientId)
                .setParameter("nursingConsultationIds", nursingConsultationIds)
                .getResultList();
        List<HealthConditionSummaryVo> result = new ArrayList<>();
        queryResult.forEach(a ->
                result.add(new HealthConditionSummaryVo(
                        (Integer) a[0],
                        (String) a[1],
                        (String) a[2],
                        (String) a[3],
                        a[4] != null ? (LocalDate) a[4] : null,
                        a[5] != null ? (LocalDate) a[5] : null,
                        (boolean) a[6],
                        (String) a[7],
                        (Integer) a[8]))
        );
        return result;
    }

    @Override
    public List<ProcedureSummaryBo> getProceduresByPatient(Integer patientId, List<Integer> nursingConsultationIds) {
        String sqlString = "SELECT p.id, s.id, s.sctid, s.pt, p.performedDate, nc.id"
                + "  FROM NursingConsultation nc"
                + "  JOIN Document d ON (d.sourceId = nc.id)"
                + "  JOIN DocumentProcedure dp ON (d.id = dp.pk.documentId)"
                + "  JOIN Procedure p ON (dp.pk.procedureId = p.id)"
                + "  JOIN Snomed s ON (p.snomedId = s.id) "
                + "  WHERE nc.patientId = :patientId"
                + "  AND d.statusId = '" + DocumentStatus.FINAL + "'"
                + "  AND d.typeId = " + DocumentType.NURSING
                + "  AND d.sourceTypeId =" + SourceType.NURSING
                + "  AND nc.id IN (:nursingConsultationIds) ";

        List<Object[]> queryResult = entityManager.createQuery(sqlString)
                .setParameter("patientId", patientId)
                .setParameter("nursingConsultationIds", nursingConsultationIds)
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

}
