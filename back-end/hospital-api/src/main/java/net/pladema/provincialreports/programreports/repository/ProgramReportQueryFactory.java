package net.pladema.provincialreports.programreports.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProgramReportQueryFactory {

	@PersistenceContext
	private final EntityManager entityManager;

	public ProgramReportQueryFactory(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<EpidemiologyOneConsultationDetail> queryEpidemiologyOne(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("ProgramReports.EpidemiologyOneConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<EpidemiologyOneConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<EpidemiologyTwoConsultationDetail> queryEpidemiologyTwo(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("ProgramReports.EpidemiologyTwoConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<EpidemiologyTwoConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<RecuperoGeneralConsultationDetail> queryRecuperoGeneral(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("ProgramReports.RecuperoGeneralConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<RecuperoGeneralConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<OdontologicalConsultationDetail> queryRecuperoOdontologico(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("ProgramReports.RecuperoOdontologicoConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<OdontologicalConsultationDetail> data = query.getResultList();
		return data;

	}


	@SuppressWarnings("unchecked")
	public List<SumarGeneralConsultationDetail> querySumarGeneral(Integer institutionId, LocalDate start, LocalDate end, Integer clinicalSpecialtyId, Integer doctorId) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("ProgramReports.SumarGeneralConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<SumarGeneralConsultationDetail> data = query.getResultList();

		return data.stream()
				.filter(doctorId != null ? oc -> oc.getProfessionalId().equals(doctorId) : c -> true)
				.filter(clinicalSpecialtyId != null ? oc -> oc.getClinicalSpecialtyId() != null : oc -> true)
				.filter(clinicalSpecialtyId != null ? oc -> oc.getClinicalSpecialtyId().equals(clinicalSpecialtyId) : c -> true)
				.collect(Collectors.toList());

	}

	@SuppressWarnings("unchecked")
	public List<OdontologicalConsultationDetail> querySumarOdontologico(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("ProgramReports.SumarOdontologicoConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<OdontologicalConsultationDetail> data = query.getResultList();
		return data;

	}

}
