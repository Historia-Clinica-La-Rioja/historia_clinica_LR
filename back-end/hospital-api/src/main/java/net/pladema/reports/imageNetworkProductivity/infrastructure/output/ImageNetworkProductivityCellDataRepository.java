package net.pladema.reports.imageNetworkProductivity.infrastructure.output;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.reports.imageNetworkProductivity.domain.CellDataBo;

import net.pladema.reports.imageNetworkProductivity.domain.ImageNetworkProductivityFilterBo;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Repository
public class ImageNetworkProductivityCellDataRepository {

	private EntityManager entityManager;

	public List<CellDataBo> run(ImageNetworkProductivityFilterBo filter) {
		log.debug("Input parameters -> filter {}", filter);
		String queryString =
				"WITH non_transcribed_practices AS (" +
				"SELECT DISTINCT a.id, s.pt AS concept_term " +
				"FROM {h-schema}diagnostic_report dr " +
				"JOIN {h-schema}snomed s ON (s.id = dr.snomed_id) " +
				"JOIN {h-schema}appointment_order_image aoi ON (aoi.study_id = dr.id) " +
				"JOIN {h-schema}appointment a ON (a.id = aoi.appointment_id) " +
				"), " +
				"transcribed_practices AS (" +
				"SELECT DISTINCT ON (a.id) a.id, s.pt AS concept_term " +
				"FROM {h-schema}appointment a " +
				"JOIN {h-schema}appointment_order_image aoi ON (aoi.appointment_id = a.id) " +
				"JOIN {h-schema}transcribed_service_request tsr ON (tsr.id = aoi.transcribed_order_id) " +
				"JOIN {h-schema}transcribed_service_request_diagnostic_report tsrdr ON (tsrdr.transcribed_service_request_id = tsr.id) " +
				"JOIN {h-schema}diagnostic_report dr ON (dr.id = tsrdr.diagnostic_report_id) " +
				"JOIN {h-schema}snomed s ON (s.id = dr.snomed_id) " +
				"), " +
				"non_transcribed_problems AS (" +
				"SELECT DISTINCT a.id, s.pt AS concept_term " +
				"FROM {h-schema}diagnostic_report dr " +
				"JOIN {h-schema}appointment_order_image aoi ON (aoi.study_id = dr.id) " +
				"JOIN {h-schema}appointment a ON (a.id = aoi.appointment_id) " +
				"JOIN {h-schema}health_condition hc ON (hc.id = dr.health_condition_id) " +
				"JOIN {h-schema}snomed s ON (s.id = hc.snomed_id) " +
				"), " +
				"transcribed_problems AS (" +
				"SELECT DISTINCT ON (a.id) a.id, s.pt AS concept_term " +
				"FROM {h-schema}appointment a " +
				"JOIN {h-schema}appointment_order_image aoi ON (aoi.appointment_id = a.id) " +
				"JOIN {h-schema}transcribed_service_request tsr ON (tsr.id = aoi.transcribed_order_id) " +
				"JOIN {h-schema}transcribed_service_request_diagnostic_report tsrdr ON (tsrdr.transcribed_service_request_id = tsr.id) " +
				"JOIN {h-schema}diagnostic_report dr ON (dr.id = tsrdr.diagnostic_report_id) " +
				"JOIN {h-schema}health_condition hc ON (hc.id = dr.health_condition_id) " +
				"JOIN {h-schema}snomed s ON (s.id = hc.snomed_id) " +
				") " +
				"SELECT DISTINCT p2.last_name AS patient_last_name, p2.first_name AS patient_first_name, pe.name_self_determination AS patient_self_determination, " +
				"it.description AS identification_type, p2.identification_number, spg.description, a2.street, a2.number, p2.birth_date, pe.phone_prefix, pe.phone_number, pe.email, " +
				"mc.name AS medical_coverage, pmc.affiliate_number, a.date_type_id AS appointment_date, a.hour AS appointment_hour, " +
				"COALESCE(ntp2.concept_term, tp2.concept_term) AS problem, m.description AS modality, COALESCE(ntp.concept_term, tp.concept_term) AS practice, " +
				"up.person_id AS technician_person_id, up2.person_id as informer_person_id, tsr.healthcare_professional_name, hp.person_id AS request_professional_person_id, " +
				"st.description AS source_type " +
				"FROM {h-schema}appointment a " +
				"JOIN {h-schema}equipment_appointment_assn eaa ON (eaa.appointment_id = a.id) " +
				"JOIN {h-schema}patient p ON (p.id = a.patient_id) " +
				"JOIN {h-schema}equipment_diary ed ON (ed.id = eaa.equipment_diary_id) " +
				"JOIN {h-schema}equipment e ON (e.id = ed.equipment_id) " +
				"LEFT JOIN {h-schema}sector s ON (s.id = e.sector_id) " +
				"LEFT JOIN {h-schema}institution i ON (i.id = s.institution_id) " +
				"LEFT JOIN {h-schema}person p2 ON (p2.id = p.person_id) " +
				"LEFT JOIN {h-schema}identification_type it ON (it.id = p2.identification_type_id) " +
				"LEFT JOIN {h-schema}person_extended pe ON (pe.person_id = p2.id) " +
				"LEFT JOIN {h-schema}self_perceived_gender spg ON (spg.id = pe.gender_self_determination) " +
				"LEFT JOIN {h-schema}address a2 ON (a2.id = pe.address_id) " +
				"LEFT JOIN {h-schema}patient_medical_coverage pmc ON (pmc.id = a.patient_medical_coverage_id) " +
				"LEFT JOIN {h-schema}medical_coverage mc ON (mc.id = pmc.medical_coverage_id) " +
				"JOIN {h-schema}modality m ON (m.id = e.modality_id) " +
				"LEFT JOIN {h-schema}appointment_order_image aoi ON (aoi.appointment_id = a.id) " +
				"LEFT JOIN {h-schema}transcribed_service_request tsr ON (tsr.id = aoi.transcribed_order_id) " +
				"LEFT JOIN {h-schema}non_transcribed_practices ntp ON (ntp.id = a.id) " +
				"LEFT JOIN {h-schema}transcribed_practices tp ON (tp.id = a.id) " +
				"LEFT JOIN {h-schema}non_transcribed_problems ntp2 ON (ntp2.id = a.id) " +
				"LEFT JOIN {h-schema}transcribed_problems tp2 ON (tp2.id = a.id)" +
				"JOIN {h-schema}details_order_image doi ON (doi.appointment_id = a.id) " +
				"JOIN {h-schema}user_person up ON (up.user_id = doi.completed_by) " +
				"LEFT JOIN {h-schema}document d2 ON (d2.id = aoi.document_id) " +
				"LEFT JOIN {h-schema}user_person up2 ON (up2.user_id = d2.created_by) " +
				"LEFT JOIN {h-schema}service_request sr ON (sr.id = aoi.order_id) " +
				"LEFT JOIN {h-schema}healthcare_professional hp ON (hp.id = sr.doctor_id) " +
				"LEFT JOIN {h-schema}person p6 ON (p6.id = hp.person_id) " +
				"LEFT JOIN {h-schema}source_type st ON (st.id = sr.source_type_id) " +
				"LEFT JOIN {h-schema}professional_professions pp ON (pp.healthcare_professional_id = hp.id) " +
				"LEFT JOIN {h-schema}healthcare_professional_specialty hps ON (hps.professional_profession_id = pp.id) " +
				"WHERE a.date_type_id BETWEEN :from AND :to " +
				"AND s.institution_id = :institutionId " +
				(filter.getHealthcareProfessionalId() != null ? "AND hp.id = :healthcareProfessionalId " : "") +
				(filter.getClinicalSpecialtyId() != null ? "AND hps.clinical_specialty_id = :clinicalSpecialtyId " : "");
		Query query = createQuery(filter, queryString);
		List<Object[]> queryResult = query.getResultList();
		List<CellDataBo> result = parseDataList(queryResult);
		log.debug("Output -> {}", result);
		return result;
	}

	private Query createQuery(ImageNetworkProductivityFilterBo filter, String queryString) {
		Query query = entityManager.createNativeQuery(queryString);
		query.setParameter("from", filter.getFrom());
		query.setParameter("to", filter.getTo());
		query.setParameter("institutionId", filter.getInstitutionId());
		if (filter.getHealthcareProfessionalId() != null)
			query.setParameter("healthcareProfessionalId", filter.getHealthcareProfessionalId());
		if (filter.getClinicalSpecialtyId() != null)
			query.setParameter("clinicalSpecialtyId", filter.getClinicalSpecialtyId());
		return query;
	}

	private List<CellDataBo> parseDataList(List<Object[]> data) {
		return data.stream().map(this::parseCellDataBo).collect(Collectors.toList());
	}

	private CellDataBo parseCellDataBo(Object[] data) {
		CellDataBo result = new CellDataBo();
		result.setPatientLastName((String) data[0]);
		result.setPatientFirstName((String) data[1]);
		result.setPatientSelfDeterminationName((String) data[2]);
		result.setIdentificationType((String) data[3]);
		result.setIdentificationNumber((String) data[4]);
		result.setSelfDeterminationGender((String) data[5]);
		result.setPatientStreetName((String) data[6]);
		result.setPatientStreetNumber((String) data[7]);
		result.setPatientBirthDate(data[8] != null ? ((Date) data[8]).toLocalDate() : null);
		result.setPhonePrefix((String) data[9]);
		result.setPhoneNumber((String) data[10]);
		result.setPatientEmail((String) data[11]);
		result.setMedicalCoverageName((String) data[12]);
		result.setAffiliateNumber((String) data[13]);
		result.setAppointmentDate(((Date) data[14]).toLocalDate());
		result.setAppointmentHour(((Time) data[15]).toLocalTime());
		result.setProblem((String) data[16]);
		result.setModality((String) data[17]);
		result.setPractice((String) data[18]);
		result.setTechnicianPersonId((Integer) data[19]);
		result.setInformerPersonId((Integer) data[20]);
		result.setTranscribedRequestProfessional((String) data[21]);
		result.setNonTranscribedRequestProfessionalPersonId((Integer) data[22]);
		result.setSourceType((String) data[23]);
		return result;
	}

}
