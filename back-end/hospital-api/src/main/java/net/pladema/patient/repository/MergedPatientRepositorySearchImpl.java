package net.pladema.patient.repository;

import ar.lamansys.sgx.shared.repositories.QueryPart;
import net.pladema.patient.controller.dto.MergedPatientSearchFilter;
import net.pladema.patient.controller.dto.PatientSearchFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static net.pladema.patient.repository.PatientSearchQuery.OR_JOINING_OPERATOR;

public class MergedPatientRepositorySearchImpl implements MergedPatientRepositorySearch{

	@PersistenceContext
	private EntityManager entityManager;

	public MergedPatientRepositorySearchImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Override
	public List<Integer> getAllByFilter(MergedPatientSearchFilter mergedPatientSearchFilter) {
		MergedPatientSearchQuery mergedPatientSearchQuery = new MergedPatientSearchQuery(mergedPatientSearchFilter);
		return null;
	}

	private QueryPart buildQuery(MergedPatientSearchQuery mergedPatientSearchQuery, MergedPatientSearchFilter mergedPatientSearchFilter){
		QueryPart queryPart = new QueryPart(
				"SELECT ")
				.concatPart(mergedPatientSearchQuery.select())
				.concat(" FROM ")
				.concatPart(mergedPatientSearchQuery.from());
		queryPart.concat("WHERE patient.deleted = false");
		if (hasBasicSearchAttributes(mergedPatientSearchFilter))
			queryPart.concatPart(mergedPatientSearchQuery.where());
		if (hasPatientTypeFilters(mergedPatientSearchFilter))
			queryPart.concatPart(mergedPatientSearchQuery.whereWithPatientTypesValidation());
		return queryPart;
	}

	private boolean hasBasicSearchAttributes(MergedPatientSearchFilter mergedPatientSearchFilter) {
		return (mergedPatientSearchFilter.getFirstName() != null ||
				mergedPatientSearchFilter.getMiddleNames() != null ||
				mergedPatientSearchFilter.getLastName() != null ||
				mergedPatientSearchFilter.getOtherLastNames() != null ||
				mergedPatientSearchFilter.getGenderId() != null ||
				(mergedPatientSearchFilter.getIdentificationTypeId()!= null && mergedPatientSearchFilter.getIdentificationNumber() != null) ||
				mergedPatientSearchFilter.getBirthDate() != null);
	}

	private boolean hasPatientTypeFilters(MergedPatientSearchFilter mergedPatientSearchFilter) {
		if (mergedPatientSearchFilter.getPermanentNotValidated() != null && mergedPatientSearchFilter.getPermanentNotValidated())
			return true;
		if (mergedPatientSearchFilter.getValidated() != null && mergedPatientSearchFilter.getValidated())
			return true;
		if (mergedPatientSearchFilter.getTemporary() != null && mergedPatientSearchFilter.getTemporary())
			return true;
		if (mergedPatientSearchFilter.getPermanent() != null && mergedPatientSearchFilter.getPermanent())
			return true;
		return false;
	}

}
