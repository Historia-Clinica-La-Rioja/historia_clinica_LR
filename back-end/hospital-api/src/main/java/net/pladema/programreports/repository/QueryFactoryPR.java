package net.pladema.programreports.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class QueryFactoryPR {

	@PersistenceContext
	private final EntityManager entityManager;

	public QueryFactoryPR(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailEpiI> queryEpidemiologiaI(Integer institutionId, LocalDate start, LocalDate end){

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("ProgramReports.ConsultationDetailEpiI");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<ConsultationDetailEpiI> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailEpiII> queryEpidemiologiaII(Integer institutionId, LocalDate start, LocalDate end){

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("ProgramReports.ConsultationDetailEpiII");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<ConsultationDetailEpiII> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailRecupero> queryRecupero(Integer institutionId, LocalDate start, LocalDate end){

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("ProgramReports.ConsultationDetailRecupero");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<ConsultationDetailRecupero> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<ConsultationDetailRecupero> querySumar(Integer institutionId, LocalDate start, LocalDate end){

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("ProgramReports.ConsultationDetailSumar");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<ConsultationDetailRecupero> data = query.getResultList();
		return data;
	}

	public List<ConsultationDetailOdontologia> queryOdontologia(Integer institutionId, LocalDate start, LocalDate end){

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("ProgramReports.ConsultationDetailOdontologia");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<ConsultationDetailOdontologia> data = query.getResultList();
		return data;
	}

	public List<PatientEmergencies> queryPatientEmergencies(Integer institutionId){

		//var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		//var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("ProgramReports.PatientEmergencies");
		query.setParameter("institutionId", institutionId);
		//query.setParameter("startDate", startDate);
		//query.setParameter("endDate", endDate);
		List<PatientEmergencies>data = query.getResultList();
		return data;

	}
}
