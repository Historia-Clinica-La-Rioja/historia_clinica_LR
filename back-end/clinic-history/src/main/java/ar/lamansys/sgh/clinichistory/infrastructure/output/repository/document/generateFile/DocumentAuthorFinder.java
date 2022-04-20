package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import ar.lamansys.sgh.clinichistory.domain.document.AuthorBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentAuthorFinder {

    private final Logger logger;

    private final EntityManager entityManager;

    public DocumentAuthorFinder(EntityManager entityManager) {
        logger = LoggerFactory.getLogger(this.getClass());
        this.entityManager = entityManager;
    }

    public AuthorBo getAuthor(Long documentId) {
        logger.debug("Get author from document -> documentId={}", documentId);

        String sqlString = "" +
                "SELECT hp.id, p.first_name, p.last_name, hp.license_number " +
                "FROM document AS d " +
                "JOIN user_person AS up ON (d.created_by = up.user_id) " +
                "JOIN healthcare_professional hp ON (up.person_id = hp.person_id) " +
                "JOIN person p ON (hp.person_id = p.id) " +
                "WHERE d.id = :documentId" +
                "";

		List<Object[]> rows = new ArrayList<>();
		Integer searchTries = 0;
        Query query;

		while (rows.isEmpty() && (searchTries < 10)){
			query = entityManager.createNativeQuery(sqlString);
			query.setParameter("documentId", documentId);
			rows = query.getResultList();
			searchTries++;
		}

        if (rows.isEmpty())
            return null;
        Object[] row = rows.get(0);
        AuthorBo result =  new AuthorBo((Integer) row[0], (String) row[1], (String) row[2], (String) row[3]);
        logger.trace("execute result query -> {}", result);
        return result;
    }
}
