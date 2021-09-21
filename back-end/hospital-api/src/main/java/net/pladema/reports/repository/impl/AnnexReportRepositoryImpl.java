package net.pladema.reports.repository.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import net.pladema.reports.repository.AnnexReportRepository;
import net.pladema.reports.repository.entity.AnnexIIAppointmentVo;
import net.pladema.reports.repository.entity.AnnexIIOutpatientVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class AnnexReportRepositoryImpl implements AnnexReportRepository {

    private final EntityManager entityManager;

    public AnnexReportRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnnexIIAppointmentVo> getAppointmentAnnexInfo(Integer appointmentId) {
        String query = "SELECT NEW net.pladema.reports.repository.entity.AnnexIIAppointmentVo(i.name, pe.firstName, pe.middleNames, " +
                "           pe.lastName, pe.otherLastNames, g.description, pe.birthDate, it.description, pe.identificationNumber, " +
                "           aps.description, a.dateTypeId, mc.name, pmca.affiliateNumber, i.sisaCode) " +
                "       FROM Appointment AS a " +
                "           JOIN AppointmentAssn AS assn ON (a.id = assn.pk.appointmentId) " +
                "           JOIN Diary AS d ON (assn.pk.diaryId = d.id) " +
                "           JOIN DoctorsOffice AS doff ON (d.doctorsOfficeId = doff.id) " +
                "           JOIN Institution AS i ON (doff.institutionId = i.id) " +
                "           JOIN AppointmentState AS aps ON (a.appointmentStateId = aps.id) " +
                "           LEFT JOIN PatientMedicalCoverageAssn AS pmca ON (a.patientMedicalCoverageId = pmca.id) " +
                "           LEFT JOIN MedicalCoverage AS mc ON (pmca.medicalCoverageId = mc.id) " +
                "           JOIN Patient AS pa ON (a.patientId = pa.id) " +
                "           LEFT JOIN Person AS pe ON (pe.id = pa.personId) " +
                "           LEFT JOIN IdentificationType AS it ON (it.id = pe.identificationTypeId) " +
                "           LEFT JOIN Gender AS g ON (pe.genderId = g.id) " +
                "       WHERE a.id = :appointmentId ";

        return entityManager.createQuery(query)
                .setParameter("appointmentId", appointmentId)
                .setMaxResults(1)
                .getResultList().stream().findFirst();
    }

    @Override
    public Optional<AnnexIIOutpatientVo> getConsultationAnnexInfo(Long documentId) {
        String query = "WITH t AS (" +
                "       SELECT d.id as doc_id, oc.start_date, oc.institution_id, oc.patient_id, oc.clinical_specialty_id " +
                "       FROM document AS d " +
                "       JOIN outpatient_consultation AS oc ON (d.source_id = oc.id  AND d.source_type_id = 1)" +
                "       WHERE d.id = :documentId " +
                "       UNION ALL " +
                "       SELECT d.id as doc_id, vc.performed_date as start_date, vc.institution_id, vc.patient_id, vc.clinical_specialty_id " +
                "       FROM document AS d " +
                "       JOIN vaccine_consultation AS vc ON (d.source_id = vc.id  AND d.source_type_id = 5)" +
                "       WHERE d.id = :documentId " +
                "       )" +
                "       SELECT i.name as institution, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names, g.description, " +
                "               pe.birth_date, it.description as idType, pe.identification_number, t.start_date, pr.proced as hasProcedures, " +
                "               cs.name, i.sisa_code, prob.descriptions as problems  " +
                "       FROM t " +
                "           JOIN Institution AS i ON (t.institution_id = i.id) " +
                "           JOIN Patient AS pa ON (t.patient_id = pa.id) " +
                "           LEFT JOIN Person AS pe ON (pe.id = pa.person_id) " +
                "           LEFT JOIN Identification_type AS it ON (it.id = pe.identification_type_id)" +
                "           LEFT JOIN Gender AS g ON (pe.gender_id = g.id) " +
                "           LEFT JOIN clinical_specialty AS cs ON (t.clinical_specialty_id = cs.id)" +
                "           LEFT JOIN ( " +
                "               SELECT dp.document_id, CAST(1 AS BIT) as proced " +
                "               FROM document_procedure dp " +
                "               GROUP BY proced, dp.document_id " +
                "           ) pr ON (t.doc_id = pr.document_id) " +
                "           LEFT JOIN ( " +
                "           SELECT dhc.document_id, STRING_AGG(( " +
                "               CASE WHEN hc.cie10_codes IS NULL THEN sno.pt ELSE CONCAT(sno.pt, ' (',hc.cie10_codes, ')') END), '| '" +
                "           ) as descriptions  " +
                "           FROM document_health_condition dhc " +
                "           JOIN health_condition hc ON (dhc.health_condition_id = hc.id) " +
                "           JOIN snomed sno ON (hc.snomed_id = sno.id) " +
                "           WHERE hc.problem_id IN (:problemTypes) " +
                "           GROUP BY dhc.document_id " +
                "           ) prob ON (t.doc_id = prob.document_id) ";
        Optional<Object[]> queryResult =  entityManager.createNativeQuery(query)
                .setParameter("documentId", documentId)
                .setParameter("problemTypes", List.of(ProblemType.PROBLEM, ProblemType.CHRONIC))
                .setMaxResults(1)
                .getResultList().stream().findFirst();

        Optional<AnnexIIOutpatientVo> result = queryResult.map(a -> new AnnexIIOutpatientVo(
                (String) a[0],
                (String) a[1],
                (String) a[2],
                (String) a[3],
                (String) a[4],
                (String) a[5],
                a[6] != null ? ((Date) a[6]).toLocalDate() : null,
                (String) a[7],
                (String) a[8],
                a[9] != null ? ((Date) a[9]).toLocalDate() : null,
                (Boolean) a[10],
                (String) a[11],
                (String) a[12],
                (String) a[13]
        ));
        return result;
    }
}
