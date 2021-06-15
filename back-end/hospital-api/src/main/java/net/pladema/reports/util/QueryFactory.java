package net.pladema.reports.util;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;

@Repository
public class QueryFactory {

    @PersistenceContext
    private EntityManager entityManager;

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
}
