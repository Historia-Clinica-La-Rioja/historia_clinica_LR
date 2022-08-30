package net.pladema.programreports.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@Repository
public class QueryFactoryPR {

	@PersistenceContext
	private final EntityManager entityManager;

	public QueryFactoryPR(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailEpiI> queryEpidemiologiaI(Integer institutionId){
		Query query = entityManager.createNamedQuery("ProgramReports.ConsultationDetailEpiI");
		query.setParameter("institutionId", institutionId);
		List<ConsultationDetailEpiI> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailEpiII> queryEpidemiologiaII(Integer institutionId){
		Query query = entityManager.createNamedQuery("ProgramReports.ConsultationDetailEpiII");
		query.setParameter("institutionId", institutionId);
		List<ConsultationDetailEpiII> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailRecupero> queryRecupero(Integer institutionId){
		Query query = entityManager.createNamedQuery("ProgramReports.ConsultationDetailRecupero");
		query.setParameter("institutionId", institutionId);
		List<ConsultationDetailRecupero> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailRecupero> querySumar(Integer institutionId){
		Query query = entityManager.createNamedQuery("ProgramReports.ConsultationDetailSumar");
		query.setParameter("institutionId", institutionId);
		List<ConsultationDetailRecupero> data = query.getResultList();
		return data;
	}

	public List<ConsultationDetailOdontologia> queryOdontologia(Integer institutionId){
		Query query = entityManager.createNamedQuery("ProgramReports.ConsultationDetailOdontologia");
		query.setParameter("institutionId", institutionId);
		List<ConsultationDetailOdontologia> data = query.getResultList();
		return data;
	}
}
