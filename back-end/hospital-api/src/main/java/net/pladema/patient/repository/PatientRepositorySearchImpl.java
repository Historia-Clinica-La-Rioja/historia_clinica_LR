package net.pladema.patient.repository;

import net.pladema.patient.controller.dto.PatientRegistrationSearchFilter;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.service.domain.PatientRegistrationSearch;
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

	@Override
	public List<PatientRegistrationSearch> getAllRegistrationByFilter(PatientRegistrationSearchFilter searchFilter) {
		PatientRegistrationSearchQuery patientRegistrationSearchQuery = new PatientRegistrationSearchQuery(searchFilter);
		QueryPart queryPart = buildQueryRegistrationSearch(patientRegistrationSearchQuery, searchFilter);
		Query query = entityManager.createNativeQuery(queryPart.toString());
		queryPart.configParams(query);
		return patientRegistrationSearchQuery.construct(query.getResultList());
	}

	private QueryPart buildQuery(PatientSearchQuery patientSearchQuery, PatientSearchFilter searchFilter){
        QueryPart queryPart = new QueryPart(
                "SELECT ")
                .concatPart(patientSearchQuery.select())
                .concat(" FROM ")
                .concatPart(patientSearchQuery.from());
		queryPart.concat("WHERE patient.deleted = false");
        if (hasBasicSearchAttributes(searchFilter))
                queryPart.concatPart(patientSearchQuery.whereWithBasicAttributes(OR_JOINING_OPERATOR));
        return queryPart;
    }

	private QueryPart buildQueryRegistrationSearch(PatientRegistrationSearchQuery patientRegistrationSearchQuery, PatientRegistrationSearchFilter searchFilter) {
		QueryPart queryPart = new QueryPart(
				"SELECT ")
				.concatPart(patientRegistrationSearchQuery.select())
				.concat(" FROM ")
				.concatPart(patientRegistrationSearchQuery.from());
		queryPart.concat("WHERE patient.deleted = false");
		if (hasBasicSearchAttributes(searchFilter))
			queryPart.concatPart(patientRegistrationSearchQuery.whereWithBasicAttributes());
		if (hasFilters(searchFilter))
			queryPart.concatPart(patientRegistrationSearchQuery.whereWithPatientTypesValidation());
		if (searchFilter.getToAudit() != null && searchFilter.getToAudit())
			queryPart.concatPart(patientRegistrationSearchQuery.whereWithToAuditValidation());

		// falta filtro por validacion de automatica/manual en un futuro

		return queryPart;
	}

    private boolean hasBasicSearchAttributes(PatientSearchFilter searchFilter) {
        return (searchFilter.getFirstName() != null ||
                searchFilter.getLastName() != null ||
                searchFilter.getIdentificationNumber() != null ||
                searchFilter.getBirthDate() != null);
    }

	private boolean hasFilters(PatientRegistrationSearchFilter searchFilter) {
		if (searchFilter.getPermanentNotValidated() != null && searchFilter.getPermanentNotValidated())
			return true;
		if (searchFilter.getValidated() != null && searchFilter.getValidated())
			return true;
		if (searchFilter.getTemporary() != null && searchFilter.getTemporary())
			return true;
		if (searchFilter.getRejected() != null && searchFilter.getRejected())
			return true;
		return false;
	}
}
