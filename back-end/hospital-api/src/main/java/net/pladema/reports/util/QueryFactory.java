package net.pladema.reports.util;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;

@Service
public class QueryFactory {

    @PersistenceContext
    private EntityManager entityManager;

    public Query query(Integer institutionId, LocalDate startDate, LocalDate endDate, Integer clinicalSpecialtyId, Integer doctorId) {
        Query query = entityManager.createNativeQuery(
                String.format(Queries.GET_MONTHLY_REPORT, ""));
        query.setParameter("institutionId", institutionId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("clinicalSpecialtyId", clinicalSpecialtyId);
        query.setParameter("doctorId", doctorId);
        return query;
    }
}
