package net.pladema.reports.repository.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.RequiredArgsConstructor;
import net.pladema.reports.repository.AnnexReportRepository;
import net.pladema.reports.repository.entity.AnnexIIAppointmentVo;
import net.pladema.reports.repository.entity.AnnexIIOdontologyDataVo;
import net.pladema.reports.repository.entity.AnnexIIOdontologyVo;
import net.pladema.reports.repository.entity.AnnexIIOutpatientVo;
import net.pladema.reports.repository.entity.AnnexIIReportDataVo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class AnnexReportRepositoryImpl implements AnnexReportRepository {

	@PersistenceContext
    private final EntityManager entityManager;

	private static AnnexIIOutpatientVo toAnnexIIOutpatientVo(Object[] a) {
		return new AnnexIIOutpatientVo(
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
			(String) a[13],
			(String) a[14],
			(String) a[15],
			(Integer) a[16],
			mapCreatedOn(a[17]),
			(Integer) a[18],
			(Integer) a[19]
		);
	}

	/**
	 * The created_on column is stored as UTC.
	 * @param o column value at UTC.
	 * @return the created_on date time at UTC-3.
	 */
	private static LocalDateTime mapCreatedOn(Object o) {
		if (o == null) return null;
		return ((Timestamp)o)
			.toLocalDateTime()
			.atZone(ZoneId.of(JacksonDateFormatConfig.UTC_ZONE_ID))
			.withZoneSameInstant(ZoneId.of(JacksonDateFormatConfig.ZONE_ID))
			.toLocalDateTime();
	}

	@Override
    @Transactional(readOnly = true)
    public Optional<AnnexIIAppointmentVo> getAppointmentAnnexInfo(Integer appointmentId) {
        String query = "SELECT NEW net.pladema.reports.repository.entity.AnnexIIAppointmentVo(i.name, pe.firstName, pe.middleNames, " +
                "           pe.lastName, pe.otherLastNames, g.description, pe.birthDate, it.description, pe.identificationNumber, " +
                "           aps.description, a.dateTypeId, mc.name, mc.cuit, i.sisaCode, hi.rnos, apias.patientIdentityAccreditationStatusId) " +
                "       FROM Appointment AS a " +
                "           JOIN AppointmentAssn AS assn ON (a.id = assn.pk.appointmentId) " +
                "           JOIN Diary AS d ON (assn.pk.diaryId = d.id) " +
                "           JOIN DoctorsOffice AS doff ON (d.doctorsOfficeId = doff.id) " +
                "           JOIN Institution AS i ON (doff.institutionId = i.id) " +
                "           JOIN AppointmentState AS aps ON (a.appointmentStateId = aps.id) " +
                "           LEFT JOIN PatientMedicalCoverageAssn AS pmca ON (a.patientMedicalCoverageId = pmca.id) " +
                "           LEFT JOIN MedicalCoverage AS mc ON (pmca.medicalCoverageId = mc.id) " +
				"			LEFT JOIN HealthInsurance AS hi ON (mc.id = hi.id) " +
                "           JOIN Patient AS pa ON (a.patientId = pa.id) " +
                "           LEFT JOIN Person AS pe ON (pe.id = pa.personId) " +
                "           LEFT JOIN IdentificationType AS it ON (it.id = pe.identificationTypeId) " +
                "           LEFT JOIN Gender AS g ON (pe.genderId = g.id) " +
				"			LEFT JOIN AppointmentPatientIdentityAccreditationStatus apias ON (apias.appointmentId = a.id) " +
                "       WHERE a.id = :appointmentId ";

        return entityManager.createQuery(query)
                .setParameter("appointmentId", appointmentId)
                .setMaxResults(1)
                .getResultList().stream().findFirst();
    }

    @Override
	@Transactional(readOnly = true)
    public Optional<AnnexIIOutpatientVo> getConsultationAnnexInfo(Long documentId) {
		int outPatientValue = SourceType.OUTPATIENT;
		int vaccineValue = SourceType.IMMUNIZATION;
        String query = "WITH t AS (" +
                "       SELECT " +
				"        CASE " +
				"            WHEN d.source_type_id = :outPatientValue THEN oc.id " +
				"            WHEN d.source_type_id = :vaccineValue THEN vc.id " +
				"        END as id, " +
				"        d.id as doc_id, " +
				"        CASE " +
				"            WHEN d.source_type_id = :outPatientValue THEN oc.start_date " +
				"            WHEN d.source_type_id = :vaccineValue THEN vc.performed_date " +
				"        END as start_date, " +
				"        COALESCE(oc.institution_id, vc.institution_id) as institution_id, " +
				"        COALESCE(oc.patient_id, vc.patient_id) as patient_id, " +
				"        COALESCE(oc.clinical_specialty_id, vc.clinical_specialty_id) as clinical_specialty_id, " +
				"        COALESCE(oc.patient_medical_coverage_id, vc.patient_medical_coverage_id) as patient_medical_coverage_id, " +
				"        d.created_on, " +
				"        COALESCE(oc.doctor_id, vc.doctor_id) as doctor_id " +
				"       FROM {h-schema}document AS d " +
				"           LEFT JOIN {h-schema}outpatient_consultation AS oc ON (d.source_id = oc.id AND d.source_type_id = :outPatientValue) " +
				"           LEFT JOIN {h-schema}vaccine_consultation AS vc ON (d.source_id = vc.id AND d.source_type_id = :vaccineValue) " +
				"       WHERE d.id = :documentId " +
				"           AND (d.source_type_id = :outPatientValue OR d.source_type_id = :vaccineValue) " +
                "       )" +
                "       SELECT i.name as institution, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names, g.description, " +
                "               pe.birth_date, it.description as idType, pe.identification_number, t.start_date, pr.proced as hasProcedures, " +
                "               cs.name, i.sisa_code, prob.descriptions as problems, mc.name as medicalCoverageName, mc.cuit as medicalCoverageCuit, hi.rnos, " +
                "				t.created_on as createdOn, t.id as id, t.doctor_id " +
                "       FROM t " +
                "           JOIN {h-schema}Institution AS i ON (t.institution_id = i.id) " +
                "           JOIN {h-schema}Patient AS pa ON (t.patient_id = pa.id) " +
				"           LEFT JOIN {h-schema}patient_medical_coverage AS pmc ON (t.patient_medical_coverage_id = pmc.id) " +
				"           LEFT JOIN {h-schema}medical_coverage AS mc ON (pmc.medical_coverage_id = mc.id) " +
				"			LEFT JOIN {h-schema}health_insurance AS hi ON (mc.id = hi.id) " +
                "           LEFT JOIN {h-schema}Person AS pe ON (pe.id = pa.person_id) " +
                "           LEFT JOIN {h-schema}Identification_type AS it ON (it.id = pe.identification_type_id)" +
                "           LEFT JOIN {h-schema}Gender AS g ON (pe.gender_id = g.id) " +
                "           LEFT JOIN {h-schema}clinical_specialty AS cs ON (t.clinical_specialty_id = cs.id)" +
                "           LEFT JOIN ( " +
                "               SELECT dp.document_id, CAST(1 AS BIT) as proced " +
                "               FROM {h-schema}document_procedure dp " +
				"               WHERE dp.document_id = :documentId " +
                "               GROUP BY proced, dp.document_id " +
                "           ) pr ON (t.doc_id = pr.document_id) " +
                "           LEFT JOIN ( " +
					"           SELECT dhc.document_id, STRING_AGG(( " +
					"               CASE WHEN hc.cie10_codes IS NULL THEN sno.pt ELSE CONCAT(sno.pt, ' (',hc.cie10_codes, ')') END), '| '" +
					"           ) as descriptions  " +
					"           FROM {h-schema}document_health_condition dhc " +
					"           JOIN {h-schema}health_condition hc ON (dhc.health_condition_id = hc.id) " +
					"           JOIN {h-schema}snomed sno ON (hc.snomed_id = sno.id) " +
					"           WHERE hc.problem_id IN (:problemTypes) AND dhc.document_id = :documentId " +
					"           GROUP BY dhc.document_id " +
					"           ) prob ON (t.doc_id = prob.document_id) ";

        Optional<Object[]> queryResult = (Optional<Object[]>) entityManager.createNativeQuery(query)
                .setParameter("documentId", documentId)
                .setParameter("problemTypes", List.of(ProblemType.PROBLEM, ProblemType.CHRONIC))
				.setParameter("outPatientValue", outPatientValue)
				.setParameter("vaccineValue", vaccineValue)
				.setMaxResults(1)
				.getResultList()
				.stream().findFirst();

        Optional<AnnexIIOutpatientVo> result = queryResult.map(AnnexReportRepositoryImpl::toAnnexIIOutpatientVo);
        return result;
    }

	@Override
	@Transactional(readOnly = true)
	public Optional<AnnexIIOutpatientVo> getOdontologyConsultationAnnexGeneralInfo(Long documentId) {
		String query = "WITH t AS (" +
				"       SELECT oc.id, d.id as doc_id, oc.performed_date, oc.institution_id, oc.patient_id, oc.clinical_specialty_id, oc.patient_medical_coverage_id, d.created_on, oc.doctor_id " +
				"       FROM {h-schema}document AS d " +
				"       JOIN {h-schema}odontology_consultation AS oc ON (d.source_id = oc.id  AND d.source_type_id = 6)" +
				"       WHERE d.id = :documentId) " +
				"       SELECT i.name as institution, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names, g.description, " +
				"               pe.birth_date, it.description as idType, pe.identification_number, t.performed_date, null as hasProcedures, " +
				"               null, i.sisa_code, null as problems, mc.name as medicalCoverageName, mc.cuit as medicalCoverageCuit, hi.rnos,	" +
				"               t.created_on as createdOn, t.id as id, t.doctor_id" +
				"       FROM t " +
				"           JOIN {h-schema}Institution AS i ON (t.institution_id = i.id) " +
				"           JOIN {h-schema}Patient AS pa ON (t.patient_id = pa.id) " +
				"			LEFT JOIN {h-schema}patient_medical_coverage AS pmc ON (t.patient_medical_coverage_id = pmc.id) " +
				"           LEFT JOIN {h-schema}medical_coverage AS mc ON (pmc.medical_coverage_id = mc.id) " +
				"			LEFT JOIN {h-schema}health_insurance AS hi ON (mc.id = hi.id) " +
				"           LEFT JOIN {h-schema}Person AS pe ON (pe.id = pa.person_id) " +
				"           LEFT JOIN {h-schema}Identification_type AS it ON (it.id = pe.identification_type_id)" +
				"           LEFT JOIN {h-schema}Gender AS g ON (pe.gender_id = g.id) " +
				"           LEFT JOIN {h-schema}clinical_specialty AS cs ON (t.clinical_specialty_id = cs.id) ";

		Optional<Object[]> queryResult =  entityManager.createNativeQuery(query)
				.setParameter("documentId", documentId)
				.setMaxResults(1)
				.getResultList().stream().findFirst();

		Optional<AnnexIIOutpatientVo> result = queryResult.map(AnnexReportRepositoryImpl::toAnnexIIOutpatientVo);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<AnnexIIOdontologyVo> getOdontologyConsultationAnnexSpecialityAndHasProcedures(Long documentId) {
		String query = "select cs.name, " +
				"		case when count(s3.pt) > 0 or count(s4.pt) > 0 then true else false end " +
				"		from {h-schema}document doc " +
				"			join {h-schema}odontology_consultation oc ON (oc.id = doc.source_id) " +
				"			left join {h-schema}clinical_specialty cs on(oc.clinical_specialty_id = cs.id) " +
				"			left join {h-schema}document_procedure dp on(doc.id = dp.document_id) " +
				"			left join {h-schema}procedures p on(dp.procedure_id = p.id) " +
				"			left join {h-schema}snomed s3 on(p.snomed_id = s3.id) " +
				"			left join {h-schema}document_odontology_procedure dop on(dop.document_id = doc.id) " +
				"			left join {h-schema}odontology_procedure op on(op.id = dop.odontology_procedure_id) " +
				"			left join {h-schema}snomed s4 on(s4.id = op.snomed_id) " +
				"		where doc.id = :documentId and doc.source_type_id = 6 " +
				"		group by cs.name, s3.pt, s4.pt ";

		Optional<Object[]> queryResult =  entityManager.createNativeQuery(query)
				.setParameter("documentId", documentId)
				.setMaxResults(1)
				.getResultList().stream().findFirst();

		return queryResult
				.map(a -> new AnnexIIOdontologyVo(
						(String) a[0],
						(Boolean) a[1]
				));
	}

	@Override
	@Transactional(readOnly = true)
	public List<AnnexIIOdontologyDataVo> getOdontologyConsultationAnnexDataInfo(Long documentId) {
		String query = "SELECT NEW net.pladema.reports.repository.entity.AnnexIIOdontologyDataVo(s.pt, od.cie10Codes) " +
				"		FROM Document AS d " +
				"			JOIN OdontologyConsultation AS oc ON (d.sourceId = oc.id) " +
				"			LEFT JOIN DocumentOdontologyDiagnostic AS dod ON (d.id = dod.pk.documentId) " +
				"			JOIN OdontologyDiagnostic AS od ON (dod.pk.odontologyDiagnosticId = od.id) " +
				"			JOIN Snomed AS s ON (s.id = od.snomedId) " +
				"		WHERE d.id = :documentId " +
				"		GROUP BY s.pt, od.cie10Codes ";

		return entityManager.createQuery(query)
				.setParameter("documentId", documentId)
				.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<AnnexIIOdontologyDataVo> getOdontologyConsultationAnnexOtherDataInfo(Long documentId) {
		String query = "SELECT NEW net.pladema.reports.repository.entity.AnnexIIOdontologyDataVo(s.pt, hc.cie10Codes) " +
				"		FROM Document AS d " +
				"			JOIN OdontologyConsultation AS oc ON (d.sourceId = oc.id AND d.sourceTypeId = 6) " +
				"			LEFT JOIN DocumentHealthCondition AS dhc ON (d.id = dhc.pk.documentId) " +
				"			JOIN HealthCondition AS hc ON (dhc.pk.healthConditionId = hc.id) " +
				"			JOIN Snomed AS s ON (s.id = hc.snomedId) " +
				"		WHERE d.id = :documentId " +
				"		GROUP BY s.pt, hc.cie10Codes ";

		return entityManager.createQuery(query)
				.setParameter("documentId", documentId)
				.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<AnnexIIOutpatientVo> getNursingConsultationAnnexGeneralInfo(Long documentId) {
		String query = "WITH t AS (" +
				"       SELECT oc.id, d.id as doc_id, oc.performed_date, oc.institution_id, oc.patient_id, oc.clinical_specialty_id, oc.patient_medical_coverage_id, d.created_on, oc.doctor_id " +
				"       FROM {h-schema}document AS d " +
				"       JOIN {h-schema}nursing_consultation AS oc ON (d.source_id = oc.id  AND d.source_type_id = 7)" +
				"       WHERE d.id = :documentId) " +
				"       SELECT i.name as institution, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names, g.description, " +
				"               pe.birth_date, it.description as idType, pe.identification_number, t.performed_date, null as hasProcedures, " +
				"               null, i.sisa_code, null as problems, mc.name as medicalCoverageName, mc.cuit as medicalCoverageCuit, hi.rnos,   " +
				"               t.created_on as createdOn, t.id as id, t.doctor_id" +
				"       FROM t " +
				"           JOIN {h-schema}Institution AS i ON (t.institution_id = i.id) " +
				"           JOIN {h-schema}Patient AS pa ON (t.patient_id = pa.id) " +
				"			LEFT JOIN {h-schema}patient_medical_coverage AS pmc ON (t.patient_medical_coverage_id = pmc.id) " +
				"           LEFT JOIN {h-schema}medical_coverage AS mc ON (pmc.medical_coverage_id = mc.id) " +
				"			LEFT JOIN {h-schema}health_insurance AS hi ON (mc.id = hi.id) " +
				"           LEFT JOIN {h-schema}Person AS pe ON (pe.id = pa.person_id) " +
				"           LEFT JOIN {h-schema}Identification_type AS it ON (it.id = pe.identification_type_id)" +
				"           LEFT JOIN {h-schema}Gender AS g ON (pe.gender_id = g.id) " +
				"           LEFT JOIN {h-schema}clinical_specialty AS cs ON (t.clinical_specialty_id = cs.id) ";

		Optional<Object[]> queryResult =  entityManager.createNativeQuery(query)
				.setParameter("documentId", documentId)
				.setMaxResults(1)
				.getResultList().stream().findFirst();

		Optional<AnnexIIOutpatientVo> result = queryResult.map(AnnexReportRepositoryImpl::toAnnexIIOutpatientVo);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<AnnexIIReportDataVo> getNursingConsultationAnnexDataInfo(Long documentId) {
		String query = "SELECT NEW net.pladema.reports.repository.entity.AnnexIIReportDataVo(cs.name, " +
				"			CASE WHEN hc.cie10Codes IS NULL THEN s.pt ELSE CONCAT(s.pt, ' (',hc.cie10Codes, ')') END, " +
				"			CASE WHEN COUNT(s2.pt) > 0 THEN true ELSE false END) " +
				"		FROM Document AS doc " +
				"			JOIN NursingConsultation AS nc ON (doc.sourceId = nc.id) " +
				"			LEFT JOIN ClinicalSpecialty AS cs ON (nc.clinicalSpecialtyId = cs.id) " +
				"			JOIN DocumentHealthCondition AS dhc ON (doc.id = dhc.pk.documentId) " +
				"			JOIN HealthCondition AS hc ON (dhc.pk.healthConditionId = hc.id) " +
				"			JOIN Snomed AS s ON (s.id = hc.snomedId) " +
				"			LEFT JOIN DocumentProcedure AS dp ON (doc.id = dp.pk.documentId) " +
				"			LEFT JOIN Procedure AS p ON (dp.pk.procedureId = p.id) " +
				"			LEFT JOIN Snomed AS s2 ON (p.snomedId = s2.id) " +
				"		WHERE doc.id = :documentId " +
				"		GROUP BY cs.name, s.pt, hc.cie10Codes ";


		return entityManager.createQuery(query)
				.setParameter("documentId", documentId)
				.setMaxResults(1)
				.getResultList().stream().findFirst();
	}
}
