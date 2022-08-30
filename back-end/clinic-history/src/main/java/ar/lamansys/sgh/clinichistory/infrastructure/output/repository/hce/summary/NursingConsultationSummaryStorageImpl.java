package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.summary;

import ar.lamansys.sgh.clinichistory.application.ports.NursingConsultationSummaryStorage;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ClinicalSpecialtyBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.DocumentDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.HealthConditionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.HealthcareProfessionalBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.NursingEvolutionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
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
public class NursingConsultationSummaryStorageImpl implements NursingConsultationSummaryStorage {
    private final EntityManager entityManager;

    public NursingConsultationSummaryStorageImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NursingEvolutionSummaryBo> getAllNursingEvolutionSummary(Integer patientId) {
        String sqlString = " SELECT nc.id, nc.performedDate, "
                + "hp.id AS healthcarProfessionalId, hp.licenseNumber, hp.personId, "
                + "p.firstName, p.lastName, p.identificationNumber, pe.nameSelfDetermination, "
                + "cs.id AS clinicalSpecialtyId, cs.name AS clinicalSpecialtyName, cs.clinicalSpecialtyTypeId, "
                + "n.description, docFile.id, docFile.filename "
                + " FROM NursingConsultation nc"
                + " LEFT JOIN ClinicalSpecialty cs ON (nc.clinicalSpecialtyId = cs.id)"
                + " JOIN Document doc ON (doc.sourceId = nc.id)"
                + " LEFT JOIN Note n ON (n.id = doc.evolutionNoteId)"
                + " JOIN HealthcareProfessional hp ON (hp.id = nc.doctorId)"
                + " JOIN Person p ON (p.id = hp.personId)"
		+ " LEFT JOIN PersonExtended pe ON (p.id = pe.id)"
                + " LEFT JOIN DocumentFile docFile ON (doc.id = docFile.id)"
                + " WHERE nc.patientId = :patientId"
                + " AND doc.sourceTypeId = " + SourceType.NURSING
                + " ORDER BY nc.performedDate DESC";

        List<Object[]> queryResult = entityManager.createQuery(sqlString)
                .setParameter("patientId", patientId)
                .getResultList();
        List<NursingEvolutionSummaryBo> result = new ArrayList<>();
        queryResult.forEach(a ->
                result.add(new NursingEvolutionSummaryBo(
                        (Integer)a[0],
                        a[1] != null ? (LocalDate)a[1] : null,
						new HealthcareProfessionalBo((Integer) a[2], (String) a[3], (Integer) a[4], (String) a[5], (String) a[6], (String) a[7], (String) a[8]),
						a[9] != null ? new ClinicalSpecialtyBo((Integer)a[9], (String)a[10], (Short) a[11]) : null,
						(String)a[12],
						a[13] != null ? new DocumentDataBo((Long)a[13], (String)a[14]) : null))
        );
        return result;
    }

    @Override
    public List<HealthConditionSummaryBo> getHealthConditionsByPatient(Integer patientId, List<Integer> nursingConsultationIds) {
        String sqlString = "SELECT hc.id, hc.patientId, "
                + "  s.sctid, s.pt, "
                + "  d.statusId, "
                + "  hc.startDate, hc.inactivationDate, "
                + "  hc.main, hc.problemId, nc.id "
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
