package net.pladema.generalreports.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@Repository
public class QueryFactoryGR {

	@PersistenceContext
	private final EntityManager entityManager;

	public QueryFactoryGR(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailEmergencias> queryEmergencias(Integer institutionId){
		Query query = entityManager.createNamedQuery("GeneralReports.ConsultationDetailEmergencias");
		query.setParameter("institutionId", institutionId);
		List<ConsultationDetailEmergencias> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailDiabeticosHipertensos> queryDiabeticos(Integer institutionId){
		Query query = entityManager.createNamedQuery("GeneralReports.ConsultationDetailDiabeticos");
		query.setParameter("institutionId", institutionId);
		List<ConsultationDetailDiabeticosHipertensos> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailDiabeticosHipertensos> queryHipertensos(Integer institutionId){
		Query query = entityManager.createNamedQuery("GeneralReports.ConsultationDetailHipertensos");
		query.setParameter("institutionId", institutionId);
		List<ConsultationDetailDiabeticosHipertensos> data = query.getResultList();
		return data;
	}
}
