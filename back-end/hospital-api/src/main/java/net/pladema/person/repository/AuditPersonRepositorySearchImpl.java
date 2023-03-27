package net.pladema.person.repository;

import ar.lamansys.sgx.shared.repositories.QueryPart;
import net.pladema.patient.controller.dto.AuditPatientSearch;
import net.pladema.person.repository.domain.DuplicatePersonVo;
import net.pladema.person.repository.domain.PersonSearchResultVo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

public class AuditPersonRepositorySearchImpl implements AuditPersonRepositorySearch {

	@PersistenceContext
	private EntityManager entityManager;

	public AuditPersonRepositorySearchImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}


	@Override
	public List<DuplicatePersonVo> getAllByFilter(AuditPatientSearch auditPatientSearch) {
		AuditPersonSearchQuery auditPersonSearchQuery = new AuditPersonSearchQuery(auditPatientSearch);
		QueryPart queryPart = buildQuery(auditPersonSearchQuery);
		Query query = entityManager.createNativeQuery(queryPart.toString());
		queryPart.configParams(query);
		return auditPersonSearchQuery.construct(query.getResultList());
	}

	@Override
	public List<PersonSearchResultVo> getPersonSearchResultByAttributes(DuplicatePersonVo duplicatePersonVo) {
		PersonSearchQuery personSearchQuery = new PersonSearchQuery(duplicatePersonVo);
		QueryPart queryPart =buildQueryPersonSearch(personSearchQuery);
		Query query = entityManager.createNativeQuery(queryPart.toString());
		queryPart.configParams(query);
		return personSearchQuery.construct(query.getResultList());
	}

	private QueryPart buildQuery(AuditPersonSearchQuery auditPersonSearchQuery) {
		QueryPart queryPart = new QueryPart(
				"SELECT ")
				.concatPart(auditPersonSearchQuery.select())
				.concat(" FROM ")
				.concatPart(auditPersonSearchQuery.from())
				.concat(" WHERE ")
				.concatPart(auditPersonSearchQuery.where())
				.concat(" GROUP BY ")
				.concatPart(auditPersonSearchQuery.groupBy())
				.concat(" HAVING ")
				.concatPart(auditPersonSearchQuery.having())
				.concat(" ORDER BY ")
				.concatPart(auditPersonSearchQuery.orderBy())
				.concat(";");
		return queryPart;
	}

	private QueryPart buildQueryPersonSearch(PersonSearchQuery personSearchQuery) {
		QueryPart queryPart = new QueryPart(
				"SELECT ")
				.concatPart(personSearchQuery.select())
				.concat(" FROM ")
				.concatPart(personSearchQuery.from())
				.concat(" WHERE ")
				.concatPart(personSearchQuery.where())
				.concat(";");
		return queryPart;
	}
}
