package net.pladema.patient.repository;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.service.domain.PatientSearch;
import net.pladema.sgx.repository.QueryPart;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static net.pladema.patient.repository.PatientSearchQuery.EQUAL_COMPARATOR;
import static net.pladema.patient.repository.PatientSearchQuery.OR_JOINING_OPERATOR;

public class PatientRepositorySearchImpl implements PatientRepositorySearch {

    @PersistenceContext
    private EntityManager entityManager;

    public PatientRepositorySearchImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @Override
    public List<PatientSearch> getAllByFilter(PatientSearchFilter searchFilter) {
        PatientSearchQuery patientSearchQuery = new PatientSearchQuery(searchFilter);
        QueryPart queryPart = new QueryPart(
                "SELECT ")
                .concatPart(patientSearchQuery.select())
                .concat(" FROM ")
                .concatPart(patientSearchQuery.from())
                .concat("WHERE ")
                .concatPart(patientSearchQuery.whereWithBasicAttributes(OR_JOINING_OPERATOR, EQUAL_COMPARATOR));
        Query query = entityManager.createQuery(queryPart.toString());
        queryPart.configParams(query);
        return patientSearchQuery.construct(query.getResultList());
    }
}
