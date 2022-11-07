package net.pladema.odontologyreport.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class QueryFactoryOdontology {

	@PersistenceContext
	private final EntityManager entityManager;

	public QueryFactoryOdontology(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailOdontology> queryReporteIPromocion(Integer institutionId, LocalDate start, LocalDate end){

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("OdontologyReports.ConsultationDetailReporteIPromocion");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<ConsultationDetailOdontology> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailOdontology> queryReporteIIPrevencion(Integer institutionId, LocalDate start, LocalDate end){

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("OdontologyReports.ConsultationDetailReporteIIPrevencion");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<ConsultationDetailOdontology> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailOdontology> queryReporteIIIPrevencionGrupal(Integer institutionId, LocalDate start, LocalDate end){

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("OdontologyReports.ConsultationDetailReporteIIIPrevencionGrupal");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<ConsultationDetailOdontology> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailOdontology> queryReporteIVOperatoria(Integer institutionId, LocalDate start, LocalDate end){

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("OdontologyReports.ConsultationDetailReporteIVOperatoria");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<ConsultationDetailOdontology> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailOdontology> queryReporteVEndodoncia(Integer institutionId, LocalDate start, LocalDate end){

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("OdontologyReports.ConsultationDetailReporteVEndodoncia");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<ConsultationDetailOdontology> data = query.getResultList();
		return data;
	}
}
