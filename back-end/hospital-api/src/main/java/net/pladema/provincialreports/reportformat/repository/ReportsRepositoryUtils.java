package net.pladema.provincialreports.reportformat.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ReportsRepositoryUtils {

	@PersistenceContext
	private EntityManager em;

	// query factory method(s)

	@SuppressWarnings("unchecked")
	public <T> List<T> executeQuery(String queryName, Class<T> resultClass, Object... parameters) {
		Query query = em.createNamedQuery(queryName, resultClass);

		for (int i = 0; i < parameters.length; i++) {
			query.setParameter(i + 1, parameters[i]);
		}

		return query.getResultList();
	}
}
