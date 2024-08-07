package net.pladema.provincialreports.generalreports.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@Transactional
public class NewGeneralReportsQueryFactory {

	@PersistenceContext
	private EntityManager em;

	public List<EmergencyConsultationDetail> queryEmergency(Integer institutionId, LocalDate start, LocalDate end) {
		return executeQuery("GeneralReports.EmergencyConsultationDetail", EmergencyConsultationDetail.class, institutionId, start, end);
	}

	public List<DiabeticHypertensionConsultationDetail> queryDiabetics(Integer institutionId, LocalDate start, LocalDate end) {
		return executeQuery("GeneralReports.DiabeticsConsultationDetail", DiabeticHypertensionConsultationDetail.class, institutionId, start, end);
	}

	public List<DiabeticHypertensionConsultationDetail> queryHypertensive(Integer institutionId, LocalDate start, LocalDate end) {
		return executeQuery("GeneralReports.HypertensiveConsultationDetail", DiabeticHypertensionConsultationDetail.class, institutionId, start, end);
	}

	public List<ComplementaryStudiesConsultationDetail> queryComplementaryStudies(Integer institutionId, LocalDate start, LocalDate end) {
		return executeQuery("GeneralReports.ComplementaryStudiesConsultationDetail", ComplementaryStudiesConsultationDetail.class, institutionId, start, end);
	}

	private <T> List<T> executeQuery(String queryName, Class<T> resultClass, Integer institutionId, LocalDate start, LocalDate end) {
		LocalDateTime startDate = start.atStartOfDay();
		LocalDateTime endDate = end.atTime(LocalTime.MAX);

		TypedQuery<T> query = em.createQuery(queryName, resultClass);
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		return query.getResultList();
	}

}
