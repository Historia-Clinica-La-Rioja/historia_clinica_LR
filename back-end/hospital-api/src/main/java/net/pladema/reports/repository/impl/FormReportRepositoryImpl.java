package net.pladema.reports.repository.impl;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import net.pladema.reports.repository.FormReportRepository;
import net.pladema.reports.repository.entity.FormVAppointmentVo;
import net.pladema.reports.repository.entity.FormVReportDataVo;
import net.pladema.reports.repository.entity.FormVOutpatientVo;

@RequiredArgsConstructor
@Repository
public class FormReportRepositoryImpl implements FormReportRepository {

	@PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Optional<FormVAppointmentVo> getAppointmentFormVInfo(Integer appointmentId) {
        String query = "SELECT NEW net.pladema.reports.repository.entity.FormVAppointmentVo(i.name, pe.firstName, pe.middleNames, "
                +
                "               pe.lastName, pe.otherLastNames, g.description, pe.birthDate, it.description, " +
                "               pe.identificationNumber, ad.street, ad.number, ci.description, mc.name, pmca.affiliateNumber, i.sisaCode, " +
				"				CASE WHEN a.appointmentStateId = " + AppointmentState.SERVED + " THEN a.updateable.updatedOn ELSE a.dateTypeId END) "
                +
                "       FROM Appointment AS a " +
                "           JOIN AppointmentAssn AS assn ON (a.id = assn.pk.appointmentId) " +
                "           JOIN Diary AS d ON (assn.pk.diaryId = d.id) " +
                "           JOIN DoctorsOffice AS doff ON (d.doctorsOfficeId = doff.id) " +
                "           JOIN Institution AS i ON (doff.institutionId = i.id) " +
                "           LEFT JOIN PatientMedicalCoverageAssn AS pmca ON (a.patientMedicalCoverageId = pmca.id) " +
                "           LEFT JOIN MedicalCoverage AS mc ON (pmca.medicalCoverageId = mc.id) " +
                "           JOIN Patient AS pa ON (a.patientId = pa.id) " +
                "           LEFT JOIN Person AS pe ON (pe.id = pa.personId) " +
                "           LEFT JOIN PersonExtended AS pex ON (pe.id = pex.id) " +
                "           LEFT JOIN Address AS ad ON (pex.addressId = ad.id) " +
                "           LEFT JOIN City AS ci ON (ad.cityId = ci.id) " +
                "           LEFT JOIN IdentificationType AS it ON (it.id = pe.identificationTypeId) " +
                "           LEFT JOIN Gender AS g ON (pe.genderId = g.id) " +
                "       WHERE a.id = :appointmentId ";
        return entityManager.createQuery(query)
                .setParameter("appointmentId", appointmentId)
                .setMaxResults(1)
                .getResultList().stream().findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormVOutpatientVo> getConsultationFormVInfo(Long documentId) {
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
                "       SELECT i.name, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names, " +
                "              g.description, pe.birth_date, it.description as idType, pe.identification_number, " +
                "              t.start_date, prob.descriptions as problems, " +
                "              ad.street, ad.number, ci.description as city, i.sisa_code, prob.cie10Codes as cie10Codes, " +
				"				mc.name as medicalCoverageName, pmc.affiliate_number " +
                "       FROM t " +
                "           JOIN {h-schema}Institution AS i ON (t.institution_id = i.id) " +
                "           JOIN {h-schema}Patient AS pa ON (t.patient_id = pa.id) " +
				"           LEFT JOIN {h-schema}patient_medical_coverage AS pmc ON (t.patient_medical_coverage_id = pmc.id) " +
				"           LEFT JOIN {h-schema}medical_coverage AS mc ON (pmc.medical_coverage_id = mc.id) " +
                "           LEFT JOIN {h-schema}Person AS pe ON (pe.id = pa.person_id) " +
                "           LEFT JOIN {h-schema}Person_extended AS pex ON (pe.id = pex.person_id) " +
                "           LEFT JOIN {h-schema}Address AS ad ON (pex.address_id = ad.id) " +
                "           LEFT JOIN {h-schema}City AS ci ON (ad.city_id = ci.id) " +
                "           LEFT JOIN {h-schema}Identification_type AS it ON (it.id = pe.identification_type_id) " +
                "           LEFT JOIN {h-schema}Gender AS g ON (pe.gender_id = g.id) " +
                "           LEFT JOIN ( " +
                "               SELECT dhc.document_id, STRING_AGG(sno.pt, '| ') as descriptions, " +
                "                      STRING_AGG((CASE WHEN hc.cie10_codes IS NULL THEN '-' ELSE hc.cie10_codes END), '| ') as cie10Codes "
                +
                "               FROM {h-schema}document_health_condition dhc " +
                "               JOIN {h-schema}health_condition hc ON (dhc.health_condition_id = hc.id) " +
                "               JOIN {h-schema}snomed sno ON (hc.snomed_id = sno.id) " +
                "               WHERE hc.problem_id IN (:problemTypes) AND dhc.document_id = :documentId " +
                "               GROUP BY dhc.document_id " +
                "            ) prob ON (t.doc_id = prob.document_id)";
        Optional<Object[]> queryResult = entityManager.createNativeQuery(query)
                .setParameter("documentId", documentId)
                .setParameter("problemTypes", List.of(ProblemType.PROBLEM, ProblemType.CHRONIC))
				.setParameter("outPatientValue", outPatientValue)
				.setParameter("vaccineValue", vaccineValue)
				.setMaxResults(1)
				.getResultList().stream().findFirst();

        Optional<FormVOutpatientVo> result = queryResult.map(a -> new FormVOutpatientVo(
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
                (String) a[10],
                (String) a[11],
                (String) a[12],
                (String) a[13],
                (String) a[14],
                (String) a[15],
				(String) a[16],
				(String) a[17]

        ));
        return result;
    }

    @Override
	@Transactional(readOnly = true)
    public Optional<FormVOutpatientVo> getOdontologyConsultationFormVGeneralInfo(Long documentId) {
        String query = "WITH t AS (" +
                "       SELECT d.id as doc_id, oc.performed_date, oc.institution_id, oc.patient_id, oc.clinical_specialty_id, oc.patient_medical_coverage_id "
                +
                "       FROM {h-schema}document AS d " +
                "       JOIN {h-schema}odontology_consultation AS oc ON (d.source_id = oc.id  AND d.source_type_id = 6)"
                +
                "       WHERE d.id = :documentId)" +
                "       SELECT i.name, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names, " +
                "              g.description, pe.birth_date, it.description as idType, pe.identification_number, " +
                "              t.performed_date, null as problems, " +
                "              ad.street, ad.number, ci.description as city, i.sisa_code, null as cie10Codes, " +
				"				mc.name as medicalCoverageName, pmc.affiliate_number " +
                "       FROM t " +
                "           JOIN {h-schema}Institution AS i ON (t.institution_id = i.id) " +
                "           JOIN {h-schema}Patient AS pa ON (t.patient_id = pa.id) " +
				"           LEFT JOIN {h-schema}patient_medical_coverage AS pmc ON (t.patient_medical_coverage_id = pmc.id) " +
				"           LEFT JOIN {h-schema}medical_coverage AS mc ON (pmc.medical_coverage_id = mc.id) " +
                "           LEFT JOIN {h-schema}Person AS pe ON (pe.id = pa.person_id) " +
                "           LEFT JOIN {h-schema}Person_extended AS pex ON (pe.id = pex.person_id) " +
                "           LEFT JOIN {h-schema}Address AS ad ON (pex.address_id = ad.id) " +
                "           LEFT JOIN {h-schema}City AS ci ON (ad.city_id = ci.id) " +
                "           LEFT JOIN {h-schema}Identification_type AS it ON (it.id = pe.identification_type_id) " +
                "           LEFT JOIN {h-schema}Gender AS g ON (pe.gender_id = g.id)";
        Optional<Object[]> queryResult = entityManager.createNativeQuery(query)
                .setParameter("documentId", documentId)
                .setMaxResults(1)
                .getResultList().stream().findFirst();

        Optional<FormVOutpatientVo> result = queryResult.map(a -> new FormVOutpatientVo(
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
                (String) a[10],
                (String) a[11],
                (String) a[12],
                (String) a[13],
                (String) a[14],
                (String) a[15],
				(String) a[16],
				(String) a[17]

        ));
        return result;
    }

    @Override
	@Transactional(readOnly = true)
    public List<FormVReportDataVo> getOdontologyConsultationFormVDataInfo(Long documentId) {
        String query = "SELECT NEW net.pladema.reports.repository.entity.FormVReportDataVo(s.pt, od.cie10Codes) " +
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
    public List<FormVReportDataVo> getOdontologyConsultationFormVOtherDataInfo(Long documentId) {
        String query = "SELECT NEW net.pladema.reports.repository.entity.FormVReportDataVo(s.pt, hc.cie10Codes) " +
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
	public Optional<FormVOutpatientVo> getNursingConsultationFormVGeneralInfo(Long documentId) {
		String query = "WITH t AS (" +
				"       SELECT d.id as doc_id, nc.performed_date, nc.institution_id, nc.patient_id, nc.clinical_specialty_id, nc.patient_medical_coverage_id " +
				"       FROM {h-schema}document AS d " +
				"       JOIN {h-schema}nursing_consultation AS nc ON (d.source_id = nc.id  AND d.source_type_id = 7)" +
				"       WHERE d.id = :documentId)" +
				"       SELECT i.name, pe.first_name, pe.middle_names, pe.last_name, pe.other_last_names, " +
				"              g.description, pe.birth_date, it.description as idType, pe.identification_number, " +
				"              t.performed_date, null as problems, "+
				"              ad.street, ad.number, ci.description as city, i.sisa_code, null as cie10Codes, "+
				"				mc.name as medicalCoverageName, pmc.affiliate_number " +
				"       FROM t "+
				"           JOIN {h-schema}Institution AS i ON (t.institution_id = i.id) " +
				"           JOIN {h-schema}Patient AS pa ON (t.patient_id = pa.id) " +
				"           LEFT JOIN {h-schema}patient_medical_coverage AS pmc ON (t.patient_medical_coverage_id = pmc.id) " +
				"           LEFT JOIN {h-schema}medical_coverage AS mc ON (pmc.medical_coverage_id = mc.id) " +
				"           LEFT JOIN {h-schema}Person AS pe ON (pe.id = pa.person_id) " +
				"           LEFT JOIN {h-schema}Person_extended AS pex ON (pe.id = pex.person_id) " +
				"           LEFT JOIN {h-schema}Address AS ad ON (pex.address_id = ad.id) " +
				"           LEFT JOIN {h-schema}City AS ci ON (ad.city_id = ci.id) " +
				"           LEFT JOIN {h-schema}Identification_type AS it ON (it.id = pe.identification_type_id) " +
				"           LEFT JOIN {h-schema}Gender AS g ON (pe.gender_id = g.id)";
		Optional<Object[]> queryResult =  entityManager.createNativeQuery(query)
				.setParameter("documentId", documentId)
				.setMaxResults(1)
				.getResultList().stream().findFirst();

		Optional<FormVOutpatientVo> result = queryResult.map(a -> new FormVOutpatientVo(
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
				(String) a[10],
				(String) a[11],
				(String) a[12],
				(String) a[13],
				(String) a[14],
				(String) a[15],
				(String) a[16],
				(String) a[17]

		));
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<FormVReportDataVo> getNursingConsultationFormVDataInfo(Long documentId) {
		String query = "SELECT NEW net.pladema.reports.repository.entity.FormVReportDataVo(s.pt, hc.cie10Codes) " +
				"		FROM Document AS d " +
				"			JOIN NursingConsultation AS nc ON (d.sourceId = nc.id) " +
				"			JOIN DocumentHealthCondition AS dhc ON (d.id = dhc.pk.documentId) " +
				"			JOIN HealthCondition AS hc ON (dhc.pk.healthConditionId = hc.id) " +
				"			JOIN Snomed AS s ON (s.id = hc.snomedId) " +
				"		WHERE d.id = :documentId " +
				"		GROUP BY s.pt, hc.cie10Codes ";


		return entityManager.createQuery(query)
				.setParameter("documentId", documentId)
				.setMaxResults(1)
				.getResultList().stream().findFirst();
	}



}
