package ar.lamansys.virtualConsultation.infrastructure.output.repository;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationBo;
import ar.lamansys.virtualConsultation.domain.VirtualConsultationFilterBo;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Repository
public class ListVirtualConsultationRepositoryImpl implements ListVirtualConsultationRepository {

	private final EntityManager entityManager;

	@Override
	public List<VirtualConsultationBo> getDomainVirtualConsultation(List<Integer> clinicalSpecialties, List<Integer> careLines, VirtualConsultationFilterBo filter) {
		log.debug("Input parameters -> filter {}", filter);
		String sqlQuery = "SELECT vc.id, p.id AS patient_id, p2.first_name, pe.name_self_determination, p2.last_name, EXTRACT(YEAR FROM (AGE(p2.birth_date))), spg.description, s2.pt AS problem, s.pt AS motive, " +
				"cs.name, cl.description AS care_line, vc.institution_id, i.name as institution_name, vc.status_id, hp.id AS healthcare_professional_id, " +
				"CASE WHEN (vcpa.available IS TRUE AND vcpa.institution_id = vc.institution_id) THEN TRUE ELSE FALSE END, vc.priority_id, vc.created_on, " +
				"vc.call_id " +
				"FROM virtual_consultation vc " +
				"JOIN patient p ON (p.id = vc.patient_id) " +
				"JOIN person p2 ON (p2.id = p.person_id) " +
				"JOIN person_extended pe ON (pe.person_id = p2.id) " +
				"LEFT JOIN self_perceived_gender spg ON (spg.id = p2.gender_id) " +
				"JOIN snomed s ON (s.id = vc.motive_id) " +
				"LEFT JOIN snomed s2 ON (s2.id = vc.problem_id) " +
				"JOIN clinical_specialty cs ON (cs.id = vc.clinical_specialty_id) " +
				"JOIN care_line cl ON (cl.id = vc.care_line_id) " +
				"JOIN institution i ON (i.id = vc.institution_id) " +
				"JOIN healthcare_professional hp ON (hp.id = vc.responsible_healthcare_professional_id) " +
				"JOIN person p3 ON (p3.id = hp.person_id) " +
				"LEFT JOIN virtual_consultation_responsible_professional_availability vcpa ON (vcpa.healthcare_professional_id = hp.id AND vcpa.institution_id = vc.institution_id) " +
				"WHERE vc.clinical_specialty_id IN :clinicalSpecialties " +
				"AND vc.care_line_id IN :careLines " +
				(filter.getInstitutionId() != null ? "AND vc.institution_id = :institutionId " : "") +
				(filter.getCareLineId() != null ? "AND vc.care_line_id = :careLineId " : "") +
				(filter.getClinicalSpecialtyId() != null ? "AND vc.clinical_specialty_id = :clinicalSpecialtyId " : "") +
				(filter.getPriorityId() != null ? "AND vc.priority_id = :priorityId " : "") +
				(filter.getAvailability() != null ? "AND vc.available = :available " : "") +
				(filter.getResponsibleHealthcareProfessionalId() != null ? "AND vc.responsible_healthcare_professional_id = :responsibleHealthcareProfessionalId " : "") +
				(filter.getStatusId() != null ? "AND vc.status_id = :statusId " : "");

		Query query = entityManager.createNativeQuery(sqlQuery);

		query.setParameter("clinicalSpecialties", clinicalSpecialties);
		query.setParameter("careLines", careLines);

		if (filter.getInstitutionId() != null)
			query.setParameter("institutionId", filter.getInstitutionId());
		if (filter.getCareLineId() != null)
			query.setParameter("careLineId", filter.getCareLineId());
		if (filter.getClinicalSpecialtyId() != null)
			query.setParameter("clinicalSpecialtyId", filter.getClinicalSpecialtyId());
		if (filter.getPriorityId() != null)
			query.setParameter("priorityId", filter.getPriorityId());
		if (filter.getAvailability() != null)
			query.setParameter("available", filter.getAvailability());
		if (filter.getResponsibleHealthcareProfessionalId() != null)
			query.setParameter("responsibleHealthcareProfessionalId", filter.getResponsibleHealthcareProfessionalId());
		if (filter.getStatusId() != null)
			query.setParameter("statusId", filter.getStatusId());

		List<Object[]> queryResult = query.getResultList();

		List<VirtualConsultationBo> result = new ArrayList<>();

		queryResult.forEach(element -> {
			VirtualConsultationBo aux = new VirtualConsultationBo();
			aux.setId((Integer) element[0]);
			aux.setPatientId((Integer) element[1]);
			aux.setPatientName((String) element[2]);
			aux.setPatientSelfPerceivedName((String) element[3]);
			aux.setPatientLastName((String) element[4]);
			aux.setPatientAge(((Double) element[5]).intValue());
			aux.setPatientGender((String) element[6]);
			aux.setProblem((String) element[7]);
			aux.setMotive((String) element[8]);
			aux.setClinicalSpecialty((String) element[9]);
			aux.setCareLine((String) element[10]);
			aux.setInstitutionId((Integer) element[11]);
			aux.setInstitutionName((String) element[12]);
			aux.setStatusId((Short) element[13]);
			aux.setResponsibleHealthcareProfessionalId((Integer) element[14]);
			aux.setResponsibleAvailability((Boolean) element[15]);
			aux.setPriorityId((Short) element[16]);
			aux.setCreationDateTime(((Timestamp) element[17]).toLocalDateTime());
			aux.setCallId((String) element[18]);
			result.add(aux);
		});
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public List<VirtualConsultationBo> getInstitutionVirtualConsultation(VirtualConsultationFilterBo filter) {
		log.debug("Input parameters -> filter {}", filter);
		String sqlQuery = "SELECT DISTINCT vc.id, p.id AS patient_id, p2.first_name, pe.name_self_determination, p2.last_name, EXTRACT(YEAR FROM (AGE(p2.birth_date))), spg.description, s2.pt, " +
				"s.pt AS motive, cs.name, cl.description AS care_line, vc.institution_id, vc.status_id, p3.first_name AS professional_first_name, p3.last_name AS professional_last_name, hp.id AS healthcare_professional_id, " +
				"CASE WHEN (vcpa.available IS TRUE AND vcpa.institution_id = vc.institution_id) THEN TRUE ELSE FALSE END, vc.priority_id, vc.created_on, " +
				"vvcpa.available_professional_amount " +
				"FROM virtual_consultation vc " +
				"JOIN patient p ON (p.id = vc.patient_id) " +
				"JOIN person p2 ON (p2.id = p.person_id) " +
				"JOIN person_extended pe ON (pe.person_id = p2.id) " +
				"LEFT JOIN self_perceived_gender spg ON (spg.id = p2.gender_id) " +
				"JOIN snomed s ON (s.id = vc.motive_id) " +
				"LEFT JOIN snomed s2 ON (s2.id = vc.problem_id) " +
				"JOIN clinical_specialty cs ON (cs.id = vc.clinical_specialty_id) " +
				"JOIN care_line cl ON (cl.id = vc.care_line_id) " +
				"JOIN healthcare_professional hp ON (hp.id = vc.responsible_healthcare_professional_id) " +
				"JOIN person p3 ON (p3.id = hp.person_id) " +
				"LEFT JOIN virtual_consultation_responsible_professional_availability vcpa ON (vcpa.healthcare_professional_id = hp.id AND vcpa.institution_id = vc.institution_id) " +
				"JOIN v_virtual_consultation_professional_amount vvcpa ON (vvcpa.virtual_consultation_id = vc.id) " +
				"WHERE vc.institution_id = :institutionId " +
				(filter.getCareLineId() != null ? "AND vc.care_line_id = :careLineId " : "") +
				(filter.getClinicalSpecialtyId() != null ? "AND vc.clinical_specialty_id = :clinicalSpecialtyId " : "") +
				(filter.getPriorityId() != null ? "AND vc.priority_id = :priorityId " : "") +
				(filter.getAvailability() != null ? "AND vc.available = :available " : "") +
				(filter.getResponsibleHealthcareProfessionalId() != null ? "AND vc.responsible_healthcare_professional_id = :responsibleHealthcareProfessionalId " : "") +
				(filter.getStatusId() != null ? "AND vc.status_id = :statusId " : "");

		Query query = entityManager.createNativeQuery(sqlQuery)
				.setParameter("institutionId", filter.getInstitutionId());

		if (filter.getCareLineId() != null)
			query.setParameter("careLineId", filter.getCareLineId());
		if (filter.getClinicalSpecialtyId() != null)
			query.setParameter("clinicalSpecialtyId", filter.getClinicalSpecialtyId());
		if (filter.getPriorityId() != null)
			query.setParameter("priorityId", filter.getPriorityId());
		if (filter.getAvailability() != null)
			query.setParameter("available", filter.getAvailability());
		if (filter.getResponsibleHealthcareProfessionalId() != null)
			query.setParameter("responsibleHealthcareProfessionalId", filter.getResponsibleHealthcareProfessionalId());
		if (filter.getStatusId() != null)
			query.setParameter("statusId", filter.getStatusId());

		List<Object[]> queryResult = query.getResultList();

		List<VirtualConsultationBo> result = new ArrayList<>();

		queryResult.forEach(element -> {
			VirtualConsultationBo aux = new VirtualConsultationBo();
			aux.setId((Integer) element[0]);
			aux.setPatientId((Integer) element[1]);
			aux.setPatientName((String) element[2]);
			aux.setPatientSelfPerceivedName((String) element[3]);
			aux.setPatientLastName((String) element[4]);
			aux.setPatientAge(((Double) element[5]).intValue());
			aux.setPatientGender((String) element[6]);
			aux.setProblem((String) element[7]);
			aux.setMotive((String) element[8]);
			aux.setClinicalSpecialty((String) element[9]);
			aux.setCareLine((String) element[10]);
			aux.setInstitutionId((Integer) element[11]);
			aux.setStatusId((Short) element[12]);
			aux.setResponsibleFirstName((String) element[13]);
			aux.setResponsibleLastName((String) element[14]);
			aux.setResponsibleHealthcareProfessionalId((Integer) element[15]);
			aux.setResponsibleAvailability((Boolean) element[16]);
			aux.setPriorityId((Short) element[17]);
			aux.setCreationDateTime(((Timestamp) element[18]).toLocalDateTime());
			aux.setAvailableProfessionalsAmount((Integer) element[19]);
			result.add(aux);
		});
		log.debug("Output -> {}", result);
		return result;
	}

}
