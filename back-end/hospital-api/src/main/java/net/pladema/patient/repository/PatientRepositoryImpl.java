package net.pladema.patient.repository;

import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.service.domain.PatientSearch;
import ar.lamansys.sgx.shared.repositories.QueryPart;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.math.BigInteger;
import java.util.List;

import static net.pladema.patient.repository.PatientSearchQuery.AND_JOINING_OPERATOR;
import static net.pladema.patient.repository.PatientSearchQuery.LIKE_COMPARATOR;

@Repository
public class PatientRepositoryImpl implements PatientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public PatientRepositoryImpl(EntityManager entityManager){
        super();
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PatientSearch> getAllByOptionalFilter(PatientSearchFilter searchFilter, Integer resultSize) {
        PatientSearchQuery patientSearchQuery = new PatientSearchQuery(searchFilter);
        QueryPart queryPart = new QueryPart(
                "SELECT ")
                .concatPart(patientSearchQuery.select())
                .concat(" FROM ")
                .concatPart(patientSearchQuery.from())
                .concat("WHERE ")
                .concatPart(patientSearchQuery.whereWithAllAttributes(AND_JOINING_OPERATOR, LIKE_COMPARATOR));

        if (searchFilter.getFilterByNameSelfDetermination()) {
			queryPart.concat("UNION ");
			queryPart.concatPart(patientSearchQuery.addUnion());
		}

		Query query = entityManager.createNativeQuery(queryPart.toString());
		query.setMaxResults(resultSize);
		queryPart.configParams(query);

        return patientSearchQuery.construct(query.getResultList());
    }

    public Integer getCountByOptionalFilter(PatientSearchFilter searchFilter) {
        PatientSearchQuery patientSearchQuery = new PatientSearchQuery(searchFilter);

        QueryPart queryPart = new QueryPart(
                "SELECT COUNT(DISTINCT result.id) \n")
                .concat("FROM ( \n")
				.concat("	SELECT patient.id as id \n")
				.concat("	FROM \n")
                .concatPart(patientSearchQuery.from())
                .concat("	WHERE \n")
                .concatPart(patientSearchQuery.whereWithAllAttributes(AND_JOINING_OPERATOR, LIKE_COMPARATOR));

        if (searchFilter.getFilterByNameSelfDetermination()) {
			queryPart.concat("UNION \n" +
					"SELECT patient.id as id \n");
			queryPart.concat("FROM \n")
					.concatPart(patientSearchQuery.from())
					.concat("WHERE \n")
					.concatPart(patientSearchQuery.whereWithAllAttributesAndNameSelfDetermination(AND_JOINING_OPERATOR, LIKE_COMPARATOR));
		}

        queryPart.concat(") as result");

        Query query = entityManager.createNativeQuery(queryPart.toString());
		queryPart.configParams(query);

		BigInteger result = (BigInteger) query.getSingleResult();
		return result.intValue();
    }
}
