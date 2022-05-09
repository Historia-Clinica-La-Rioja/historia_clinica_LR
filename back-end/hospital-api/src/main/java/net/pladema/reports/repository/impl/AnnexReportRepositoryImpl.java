package net.pladema.reports.repository.impl;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import net.pladema.reports.repository.AnnexReportRepository;
import net.pladema.reports.repository.entity.AnnexIIAppointmentVo;
import net.pladema.reports.repository.entity.AnnexIIOdontologyVo;
import net.pladema.reports.repository.entity.AnnexIIOutpatientVo;

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
                "       FROM {h-schema}document AS d " +
                "       JOIN {h-schema}outpatient_consultation AS oc ON (d.source_id = oc.id  AND d.source_type_id = 1)" +
                "       WHERE d.id = :documentId " +
                "       UNION ALL " +
                "       SELECT d.id as doc_id, vc.performed_date as start_date, vc.institution_id, vc.patient_id, vc.clinical_specialty_id " +
                "       FROM {h-schema}document AS d " +
                "       JOIN {h-schema}vaccine_consultation AS vc ON (d.source_id = vc.id  AND d.source_type_id = 5)" +
                "       WHERE d.id = :documentId " +
                "       )" +
                "       SELECT i.name as institution, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names, g.description, " +
                "               pe.birth_date, it.description as idType, pe.identification_number, t.start_date, pr.proced as hasProcedures, " +
                "               cs.name, i.sisa_code, prob.descriptions as problems  " +
                "       FROM t " +
                "           JOIN {h-schema}Institution AS i ON (t.institution_id = i.id) " +
                "           JOIN {h-schema}Patient AS pa ON (t.patient_id = pa.id) " +
                "           LEFT JOIN {h-schema}Person AS pe ON (pe.id = pa.person_id) " +
                "           LEFT JOIN {h-schema}Identification_type AS it ON (it.id = pe.identification_type_id)" +
                "           LEFT JOIN {h-schema}Gender AS g ON (pe.gender_id = g.id) " +
                "           LEFT JOIN {h-schema}clinical_specialty AS cs ON (t.clinical_specialty_id = cs.id)" +
                "           LEFT JOIN ( " +
                "               SELECT dp.document_id, CAST(1 AS BIT) as proced " +
                "               FROM {h-schema}document_procedure dp " +
                "               GROUP BY proced, dp.document_id " +
                "           ) pr ON (t.doc_id = pr.document_id) " +
                "           LEFT JOIN ( " +
                "           SELECT dhc.document_id, STRING_AGG(( " +
                "               CASE WHEN hc.cie10_codes IS NULL THEN sno.pt ELSE CONCAT(sno.pt, ' (',hc.cie10_codes, ')') END), '| '" +
                "           ) as descriptions  " +
                "           FROM {h-schema}document_health_condition dhc " +
                "           JOIN {h-schema}health_condition hc ON (dhc.health_condition_id = hc.id) " +
                "           JOIN {h-schema}snomed sno ON (hc.snomed_id = sno.id) " +
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

	@Override
	public Optional<AnnexIIOutpatientVo> getOdontologyConsultationAnnexGeneralInfo(Long documentId) {
		String query = "WITH t AS (" +
				"       SELECT d.id as doc_id, oc.performed_date, oc.institution_id, oc.patient_id, oc.clinical_specialty_id " +
				"       FROM {h-schema}document AS d " +
				"       JOIN {h-schema}odontology_consultation AS oc ON (d.source_id = oc.id  AND d.source_type_id = 6)" +
				"       WHERE d.id = :documentId) " +
				"       SELECT i.name as institution, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names, g.description, " +
				"               pe.birth_date, it.description as idType, pe.identification_number, t.performed_date, null as hasProcedures, " +
				"               null, i.sisa_code, null as problems  " +
				"       FROM t " +
				"           JOIN {h-schema}Institution AS i ON (t.institution_id = i.id) " +
				"           JOIN {h-schema}Patient AS pa ON (t.patient_id = pa.id) " +
				"           LEFT JOIN {h-schema}Person AS pe ON (pe.id = pa.person_id) " +
				"           LEFT JOIN {h-schema}Identification_type AS it ON (it.id = pe.identification_type_id)" +
				"           LEFT JOIN {h-schema}Gender AS g ON (pe.gender_id = g.id) " +
				"           LEFT JOIN {h-schema}clinical_specialty AS cs ON (t.clinical_specialty_id = cs.id) ";

		Optional<Object[]> queryResult =  entityManager.createNativeQuery(query)
				.setParameter("documentId", documentId)
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

	@Override
	public Optional<AnnexIIOdontologyVo> getOdontologyConsultationAnnexDataInfo(Long documentId) {
		String query = "select cs.name, " +
				"			concat(string_agg(case when hc.cie10_codes is not null then concat(s2.pt, ' (', hc.cie10_codes , ')') else s2.pt end, '| '), '| ', " +
				"			string_agg(case when od.cie10_codes is not null then concat(sno.pt, ' (', od.cie10_codes , ')') else sno.pt end, '| '))," +
				"			case when count(s3.pt) > 0 or count(s4.pt) > 0 then true else false end " +
				"		from {h-schema}document doc " +
				"			join {h-schema}odontology_consultation oc ON (oc.id = doc.source_id) " +
				"			left join {h-schema}clinical_specialty cs on(oc.clinical_specialty_id = cs.id) " +
				"			left join {h-schema}document_odontology_diagnostic dod ON (doc.id = dod.document_id) " +
				"			left join {h-schema}odontology_diagnostic od ON (dod.odontology_diagnostic_id = od.id) " +
				"			left join {h-schema}snomed sno ON (od.snomed_id = sno.id) " +
				"			left join {h-schema}document_health_condition dhc on(doc.id = dhc.document_id) " +
				"			left join {h-schema}health_condition hc on(dhc.health_condition_id = hc.id) " +
				"			left join {h-schema}snomed s2 on(hc.snomed_id = s2.id) " +
				"			left join {h-schema}document_procedure dp on(doc.id = dp.document_id) " +
				"			left join {h-schema}procedures p on(dp.procedure_id = p.id) " +
				"			left join {h-schema}snomed s3 on(p.snomed_id = s3.id) " +
				"			left join {h-schema}document_odontology_procedure dop on(dop.document_id = doc.id) " +
				"			left join {h-schema}odontology_procedure op on(op.id = dop.odontology_procedure_id) " +
				"			left join {h-schema}snomed s4 on(s4.id = op.snomed_id) " +
				"		where doc.id = :documentId and doc.source_type_id = 6 " +
				"		group by cs.name, sno.pt, s2.pt, od.cie10_codes, hc.cie10_codes, s3.pt ";

		Optional<Object[]> queryResult =  entityManager.createNativeQuery(query)
				.setParameter("documentId", documentId)
				.setMaxResults(1)
				.getResultList().stream().findFirst();

		Optional<AnnexIIOdontologyVo> result = queryResult.map(a -> new AnnexIIOdontologyVo(
				(String) a[0],
				(String) a[1],
				(Boolean) a[2]
		));

		return result;
	}
}
