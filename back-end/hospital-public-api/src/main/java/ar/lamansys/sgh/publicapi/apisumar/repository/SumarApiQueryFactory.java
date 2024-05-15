package ar.lamansys.sgh.publicapi.apisumar.repository;

import ar.lamansys.sgh.publicapi.apisumar.repository.model.ConsultationDetailData;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@Repository
public class SumarApiQueryFactory {

	@PersistenceContext
	private final EntityManager entityManager;

	public SumarApiQueryFactory(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailData> getAllConsultationsData() {

		Query query = entityManager.createNamedQuery("ApiSumar.ConsultationDetailData");
		List<ConsultationDetailData> data = query.getResultList();
		return data;

	}
}
