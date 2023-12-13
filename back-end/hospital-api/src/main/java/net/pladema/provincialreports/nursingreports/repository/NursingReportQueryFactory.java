package net.pladema.provincialreports.nursingreports.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class NursingReportQueryFactory {

	@PersistenceContext
	private final EntityManager entityManager;

	public NursingReportQueryFactory(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<NursingEmergencyConsultationDetail> queryNursingEmergency(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59);

		Query query = entityManager.createNamedQuery("NursingReports.NursingEmergencyConsultationDetail");
		query.setParameter("institutionId", institutionId);
		// query.setParameter("startDate", startDate);
		// query.setParameter("endDate", endDate);
		List<NursingEmergencyConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<NursingOutpatientConsultationDetail> queryNursingOutpatient(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("NursingReports.NursingOutpatientConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<NursingOutpatientConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<NursingHospitalizationConsultationDetail> queryNursingHospitalization(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59);

		Query query = entityManager.createNamedQuery("NursingReports.NursingHospitalizationConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<NursingHospitalizationConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<NursingProceduresConsultationDetail> queryNursingProcedures(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(),start.getMonth(),start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59);

		Query query = entityManager.createNamedQuery("NursingReports.NursingProceduresConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<NursingProceduresConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<NursingVaccineConsultationDetail> queryNursingVaccine(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59);

		Query query = entityManager.createNamedQuery("NursingReports.NursingVaccineConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<NursingVaccineConsultationDetail> data = query.getResultList();
		return data;

	}

}
