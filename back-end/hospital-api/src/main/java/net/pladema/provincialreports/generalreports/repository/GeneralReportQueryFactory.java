package net.pladema.provincialreports.generalreports.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class GeneralReportQueryFactory {

	@PersistenceContext
	private final EntityManager entityManager;

	public GeneralReportQueryFactory(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<EmergencyConsultationDetail> queryEmergency(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("GeneralReports.EmergencyConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<EmergencyConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<DiabeticHypertensionConsultationDetail> queryDiabetics(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("GeneralReports.DiabeticsConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<DiabeticHypertensionConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<DiabeticHypertensionConsultationDetail> queryHypertensive(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("GeneralReports.HypertensiveConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<DiabeticHypertensionConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<ComplementaryStudiesConsultationDetail> queryComplementaryStudies(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(),start.getMonth(),start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59);

		Query query = entityManager.createNamedQuery("GeneralReports.ComplementaryStudiesConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<ComplementaryStudiesConsultationDetail> data = query.getResultList();
		return data;

	}

}
