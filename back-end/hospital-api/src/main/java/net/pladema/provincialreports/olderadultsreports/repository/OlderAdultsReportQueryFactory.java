package net.pladema.provincialreports.olderadultsreports.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class OlderAdultsReportQueryFactory {

	@PersistenceContext
	private final EntityManager entityManager;

	public OlderAdultsReportQueryFactory(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<OlderAdultsOutpatientConsultationDetail> queryOlderAdultsOutpatient(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(),start.getMonth(),start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59);

		Query query = entityManager.createNamedQuery("OlderAdultsReports.OlderAdultsOutpatientConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<OlderAdultsOutpatientConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<OlderAdultsHospitalizationConsultationDetail> queryOlderAdultsHospitalization(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(),start.getMonth(),start.getDayOfMonth(),0, 0);
		var endDate = LocalDateTime.of(end.getYear(),end.getMonth(),end.getDayOfMonth(), 23, 59, 59);

		Query query = entityManager.createNamedQuery("OlderAdultsReports.OlderAdultsHospitalizationConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<OlderAdultsHospitalizationConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<PolypharmacyConsultationDetail> queryPolypharmacy(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("OlderAdultsReports.PolypharmacyConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<PolypharmacyConsultationDetail> data = query.getResultList();
		return data;

	}

}
