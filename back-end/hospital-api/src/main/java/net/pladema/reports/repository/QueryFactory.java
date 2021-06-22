package net.pladema.reports.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Repository
public class QueryFactory {

    @PersistenceContext
    private final EntityManager entityManager;

    public QueryFactory(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public Query query(Integer institutionId, LocalDate startDate, LocalDate endDate, Integer clinicalSpecialtyId, Integer doctorId) {

        String clinicalSpecialtyIdCondition = clinicalSpecialtyId != null? String.format("AND oc.clinical_specialty_id=%d ", clinicalSpecialtyId) : "";
        String doctorIdCondition = doctorId != null? String.format("AND oc.doctor_id=%d ", doctorId) : "";

        String finalQuery = String.format(
                Queries.GET_MONTHLY_REPORT,
                clinicalSpecialtyIdCondition,
                doctorIdCondition);

        Query query = entityManager.createNativeQuery(finalQuery);
        query.setParameter("institutionId", institutionId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<OutpatientSummary> getOutpatientSummaryData(Integer institutionId, LocalDate startDate, LocalDate endDate){
        return entityManager.createNamedQuery("Reports.OutpatientSummary")
                .setParameter("institutionId", institutionId)
                .setParameter("from", startDate)
                .setParameter("to", endDate)
                .getResultList();
    }
}
