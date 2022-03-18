package net.pladema.patient.repository;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.service.domain.PatientSearch;
import ar.lamansys.sgx.shared.repositories.QueryPart;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

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
        QueryPart queryPart = buildQuery(patientSearchQuery, searchFilter);
        Query query = entityManager.createNativeQuery(queryPart.toString());
        queryPart.configParams(query);
        return patientSearchQuery.construct(query.getResultList());
    }

    private QueryPart buildQuery(PatientSearchQuery patientSearchQuery, PatientSearchFilter searchFilter){
        QueryPart queryPart = new QueryPart(
                "SELECT ")
                .concatPart(patientSearchQuery.select())
                .concat(" FROM ")
                .concatPart(patientSearchQuery.from());
        if (hasBasicSearchAttributes(searchFilter))
                queryPart.concat("WHERE ")
                .concatPart(patientSearchQuery.whereWithBasicAttributes(OR_JOINING_OPERATOR));
        return queryPart;
    }

    private boolean hasBasicSearchAttributes(PatientSearchFilter searchFilter) {
        return (searchFilter.getFirstName() != null ||
                searchFilter.getLastName() != null ||
                searchFilter.getIdentificationNumber() != null ||
                searchFilter.getBirthDate() != null);
    }
}
