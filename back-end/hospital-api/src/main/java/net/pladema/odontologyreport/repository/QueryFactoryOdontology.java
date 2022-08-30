package net.pladema.odontologyreport.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@Repository
public class QueryFactoryOdontology {

	@PersistenceContext
	private final EntityManager entityManager;

	public QueryFactoryOdontology(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailOdontology> queryReporteIPromocion(Integer institutionId){
		Query query = entityManager.createNamedQuery("OdontologyReports.ConsultationDetailReporteIPromocion");
		query.setParameter("institutionId", institutionId);
		List<ConsultationDetailOdontology> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailOdontology> queryReporteIIPrevencion(Integer institutionId){
		Query query = entityManager.createNamedQuery("OdontologyReports.ConsultationDetailReporteIIPrevencion");
		query.setParameter("institutionId", institutionId);
		List<ConsultationDetailOdontology> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailOdontology> queryReporteIIIPrevencionGrupal(Integer institutionId){
		Query query = entityManager.createNamedQuery("OdontologyReports.ConsultationDetailReporteIIIPrevencionGrupal");
		query.setParameter("institutionId", institutionId);
		List<ConsultationDetailOdontology> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailOdontology> queryReporteIVOperatoria(Integer institutionId){
		Query query = entityManager.createNamedQuery("OdontologyReports.ConsultationDetailReporteIVOperatoria");
		query.setParameter("institutionId", institutionId);
		List<ConsultationDetailOdontology> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailOdontology> queryReporteVEndodoncia(Integer institutionId){
		Query query = entityManager.createNamedQuery("OdontologyReports.ConsultationDetailReporteVEndodoncia");
		query.setParameter("institutionId", institutionId);
		List<ConsultationDetailOdontology> data = query.getResultList();
		return data;
	}
}
