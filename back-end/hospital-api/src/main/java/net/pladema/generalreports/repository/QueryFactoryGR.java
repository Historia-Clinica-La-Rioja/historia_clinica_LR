package net.pladema.generalreports.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
	public List<ConsultationDetailDiabeticosHipertensos> queryDiabeticos(Integer institutionId, LocalDate start, LocalDate end){

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("GeneralReports.ConsultationDetailDiabeticos");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<ConsultationDetailDiabeticosHipertensos> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailDiabeticosHipertensos> queryHipertensos(Integer institutionId, LocalDate start, LocalDate end){

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("GeneralReports.ConsultationDetailHipertensos");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<ConsultationDetailDiabeticosHipertensos> data = query.getResultList();
		return data;
	}

	public List<PatientEmergencies> queryPatientEmergencies(Integer institutionId){

		//var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		//var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("GeneralReports.PatientEmergencies");
		query.setParameter("institutionId", institutionId);
		//query.setParameter("startDate", startDate);
		//query.setParameter("endDate", endDate);
		List<PatientEmergencies>data = query.getResultList();
		return data;
	}
}
