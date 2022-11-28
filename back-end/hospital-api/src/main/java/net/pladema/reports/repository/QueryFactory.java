package net.pladema.reports.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;

@Repository
public class QueryFactory {

    @PersistenceContext
    private final EntityManager entityManager;

    public QueryFactory(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public List<ConsultationDetail> query(Integer institutionId, LocalDate start, LocalDate end,
                                          Integer clinicalSpecialtyId, Integer doctorId) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

        Query outpatientQuery = entityManager.createNamedQuery("Reports.ConsultationDetail");
        outpatientQuery.setParameter("institutionId", institutionId);
        outpatientQuery.setParameter("startDate", startDate);
        outpatientQuery.setParameter("endDate", endDate);
        List<ConsultationDetail> data = outpatientQuery.getResultList();

		Query odontologyQuery = entityManager.createNamedQuery("Reports.OdontologyConsultationDetail");
		odontologyQuery.setParameter("institutionId", institutionId);
		odontologyQuery.setParameter("startDate", startDate);
		odontologyQuery.setParameter("endDate", endDate);
		List<ConsultationDetail> odontologyData = formatProblemsAndProcedures(odontologyQuery.getResultList());

		Query nursingQuery = entityManager.createNamedQuery("Reports.NursingConsultationDetail");
		nursingQuery.setParameter("institutionId", institutionId);
		nursingQuery.setParameter("startDate", startDate);
		nursingQuery.setParameter("endDate", endDate);

		data.addAll(odontologyData);
		data.addAll(nursingQuery.getResultList());
		data.sort(Comparator.comparing(ConsultationDetail::getPatientSurname));

        //Optional filter: by specialty or professional if specified
        return data.stream()
                .filter(doctorId != null ? oc -> oc.getProfessionalId().equals(doctorId) : c -> true)
                .filter(clinicalSpecialtyId != null ? oc -> oc.getClinicalSpecialtyId().equals(clinicalSpecialtyId) : c -> true)
                .collect(Collectors.toList());
    }

	private List<ConsultationDetail> formatProblemsAndProcedures(List<ConsultationDetail> list){
		for(ConsultationDetail fila : list){
			if(fila.getProblems() != null) {
				String problems = StringUtils.stripAccents(fila.getProblems());
				List withRepeated = List.of(problems.split("/split/"));
				Set<String> noRepeated = new HashSet<String>(withRepeated);
				fila.setProblems(this.reduceAndFormat(withRepeated, noRepeated));
			}
			if(fila.getProcedures() != null) {
				String procedures = StringUtils.stripAccents(fila.getProcedures());
				List withRepeated = List.of(procedures.split("/split/"));
				Set<String> noRepeated = new HashSet<String>(withRepeated);
				fila.setProcedures(this.reduceAndFormat(withRepeated, noRepeated));
			}
		}
		return list;
	}

	private String reduceAndFormat(List withRepeated, Set<String> noRepeated){
		String result = "";
		for (String item : noRepeated) {
			int repetitions = Collections.frequency(withRepeated, item);
			if (result.equals("")) {
				if(repetitions > 1)
					result += "(" + repetitions + ") " + item;
				else
					result += item;
			}
			else {
				if (repetitions > 1)
					result += ", (" + repetitions + ") " + item;
				else
					result += ", " + item;
			}
		}
		return result;
	}

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<ConsultationSummary> fetchConsultationSummaryData(Integer institutionId, LocalDate startDate, LocalDate endDate){
		List<ConsultationSummary> result = new ArrayList<>();
		result.addAll(entityManager.createNamedQuery("Reports.ConsultationSummary")
				.setParameter("institutionId", institutionId)
				.setParameter("from", startDate)
				.setParameter("to", endDate)
				.getResultList());
		result.addAll(entityManager.createNamedQuery("Reports.OdontologyConsultationSummary")
				.setParameter("institutionId", institutionId)
				.setParameter("from", startDate)
				.setParameter("to", endDate)
				.getResultList());
		result.addAll(entityManager.createNamedQuery("Reports.NursingConsultationSummary")
				.setParameter("institutionId", institutionId)
				.setParameter("from", startDate)
				.setParameter("to", endDate)
				.getResultList());
		return result;
    }
}
