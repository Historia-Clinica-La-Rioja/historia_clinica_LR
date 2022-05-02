package net.pladema.reports.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    public List<ConsultationDetail> query(Integer institutionId, LocalDate startDate, LocalDate endDate,
                                          Integer clinicalSpecialtyId, Integer doctorId) {

        Query outpatientQuery = entityManager.createNamedQuery("Reports.ConsultationDetail");
        outpatientQuery.setParameter("institutionId", institutionId);
        outpatientQuery.setParameter("startDate", startDate);
        outpatientQuery.setParameter("endDate", endDate);
        outpatientQuery.setParameter("problemTypes", List.of(ProblemType.PROBLEM, ProblemType.CHRONIC));
        List<ConsultationDetail> data = outpatientQuery.getResultList();

		Query odontologyQuery = entityManager.createNamedQuery("Reports.OdontologyConsultationDetail");
		odontologyQuery.setParameter("institutionId", institutionId);
		odontologyQuery.setParameter("startDate", startDate);
		odontologyQuery.setParameter("endDate", endDate);

		data.addAll(odontologyQuery.getResultList());
		data.sort(Comparator.comparing(ConsultationDetail::getPatientSurname));

        //Optional filter: by specialty or professional if specified
        return data.stream()
                .filter(doctorId != null ? oc -> oc.getProfessionalId().equals(doctorId) : c -> true)
                .filter(clinicalSpecialtyId != null ? oc -> oc.getClinicalSpecialtyId().equals(clinicalSpecialtyId) : c -> true)
                .collect(Collectors.toList());
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
		return result;
    }
}
