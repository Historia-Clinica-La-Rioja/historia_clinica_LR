package net.pladema.provincialreports.reportformat.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class ReportsRepositoryUtils {

	@PersistenceContext
	private EntityManager em;

	// query factory method(s)

	@SuppressWarnings("unchecked")
	public <T> List<T> executeQuery(String queryName, Integer institutionId, LocalDate start, LocalDate end) {
		Query query = em.createNamedQuery(queryName);

		query.setParameter("institutionId", institutionId);

		if (start != null && end != null) {
			LocalDateTime startDate = start.atStartOfDay();
			LocalDateTime endDate = end.atTime(LocalTime.MAX);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
		}

		return query.getResultList();
	}
}
