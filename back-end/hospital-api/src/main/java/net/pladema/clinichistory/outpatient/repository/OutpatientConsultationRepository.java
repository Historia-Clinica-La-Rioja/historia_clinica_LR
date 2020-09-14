package net.pladema.clinichistory.outpatient.repository;

import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentType;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ProblemType;
import net.pladema.clinichistory.outpatient.repository.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OutpatientConsultationRepository extends JpaRepository<OutpatientConsultation, Integer> {

    @Transactional(readOnly = true)
    @Query(value = " SELECT NEW net.pladema.clinichistory.outpatient.repository.domain.OutpatientEvolutionSummaryVo("
            + " oc.id, oc.startDate, hp, p, n.description)"
            + " FROM OutpatientConsultation oc"
            + " JOIN Document doc ON (doc.sourceId = oc.id)"
            + " LEFT JOIN Note n ON (n.id = doc.otherNoteId)"
            + " JOIN HealthcareProfessional hp ON (hp.id = oc.doctorId)"
            + " JOIN Person p ON (p.id = hp.personId)"
            + " WHERE oc.billable = TRUE "
            + " AND oc.institutionId = :institutionId"
            + " AND oc.patientId = :patientId"
            + " AND doc.sourceTypeId = " + SourceType.OUTPATIENT)
    List<OutpatientEvolutionSummaryVo> getAllOutpatientEvolutionSummary(@Param("institutionId") Integer institutionId, @Param("patientId") Integer patientId);

    @Transactional(readOnly = true)
    @Query(value =  "   SELECT NEW net.pladema.clinichistory.outpatient.repository.domain.HealthConditionSummaryVo(hc.id, s.id, s.pt, d.statusId, hc.startDate, hc.inactivationDate, hc.main, hc.problemId, oc.id)"
                    +"  FROM OutpatientConsultation oc"
                    +"  JOIN Document d ON (d.sourceId = oc.id)"
                    +"  JOIN DocumentHealthCondition dhc ON (d.id = dhc.pk.documentId)"
                    +"  JOIN HealthCondition hc ON (dhc.pk.healthConditionId = hc.id)"
                    +"  JOIN Snomed s ON (s.id = hc.sctidCode)"
                    +"  WHERE d.statusId = '" + DocumentStatus.FINAL + "'"
                    +"  AND d.sourceTypeId =" + SourceType.OUTPATIENT
                    +"  AND d.typeId = "+ DocumentType.OUTPATIENT
                    +"  AND hc.patientId = :patientId "
                    +"  AND hc.problemId IN ('"+ProblemType.PROBLEM+"', '"+ProblemType.CHRONIC+ "')")
    List<HealthConditionSummaryVo> getHealthConditionsByPatient(@Param("patientId") Integer patientId);

    @Transactional(readOnly = true)
    @Query(value =   "  SELECT NEW net.pladema.clinichistory.outpatient.repository.domain.ReasonSummaryVo(r.id, r.description, oc.id)"
                    +"  FROM OutpatientConsultation oc"
                    +"  JOIN OutpatientConsultationReasons ocr ON (ocr.pk.outpatientConsultationId = oc.id)"
                    +"  JOIN Reason r ON (r.id = ocr.pk.reasonID)"
                    +"  WHERE oc.patientId = :patientId")
    List<ReasonSummaryVo> getReasonsByPatient(@Param("patientId") Integer patientId);

    @Transactional(readOnly = true)
    @Query(value =   "  SELECT NEW net.pladema.clinichistory.outpatient.repository.domain.ProcedureSummaryVo(p.id, s, p.performedDate, oc.id)"
                    +"  FROM OutpatientConsultation oc"
                    +"  JOIN Document d ON (d.sourceId = oc.id)"
                    +"  JOIN DocumentProcedure dp ON (d.id = dp.pk.documentId)"
                    +"  JOIN Procedure p ON (dp.pk.procedureId = p.id)"
                    +"  JOIN Snomed s ON (p.sctidCode = s.id) "
                    +"  WHERE oc.patientId = :patientId"
                    +"  AND d.statusId = '" + DocumentStatus.FINAL + "'"
                    +"  AND d.typeId = "+ DocumentType.OUTPATIENT
                    +"  AND d.sourceTypeId =" + SourceType.OUTPATIENT)
    List<ProcedureSummaryVo> getProceduresByPatient(@Param("patientId") Integer patientId);
}