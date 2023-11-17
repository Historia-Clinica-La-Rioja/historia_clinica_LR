package net.pladema.provincialreports.pregnantpeoplereports.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PregnantPeopleReportQueryFactory {

	@PersistenceContext
	private final EntityManager entityManager;

	public PregnantPeopleReportQueryFactory(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<PregnantAttentionsConsultationDetail> queryPregnantAttentions(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59);

		Query query = entityManager.createNamedQuery("PregnantPeopleReports.PregnantAttentionsConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<PregnantAttentionsConsultationDetail> data = query.getResultList();
		return data;

	}

}
